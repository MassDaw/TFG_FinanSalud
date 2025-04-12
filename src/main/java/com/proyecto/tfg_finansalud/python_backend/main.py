import asyncio
import json
import random
import websockets
import threading
import time
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Dict, Set
import uvicorn
from datetime import datetime

# FastAPI configuration
app = FastAPI(title="Dashboard Financial API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Data models
class Asset(BaseModel):
    id: str
    name: str
    symbol: str
    price: str
    volume: str
    isFavorite: bool = False

class MarketData(BaseModel):
    marketCap: str
    volume24h: str
    lastUpdated: str

class MarketResponse(BaseModel):
    market: MarketData
    assets: List[Asset]

# Market data
crypto_market = MarketData(
    marketCap="€2.74M",
    volume24h="€113.95M",
    lastUpdated="5 minutos"
)

stock_market = MarketData(
    marketCap="€48.2M",
    volume24h="€89.7M",
    lastUpdated="5 minutos"
)

cryptos = [
    Asset(id="1", name="Bitcoin", symbol="BTC", price="€82.882,76", volume="€42,08M"),
    Asset(id="2", name="Ethereum", symbol="ETH", price="€3.787,71", volume="€17,42M"),
    Asset(id="3", name="Tether", symbol="USDT", price="€1,00", volume="€62,87M"),
    Asset(id="4", name="Ripple", symbol="XRP", price="€2,07", volume="€4,50M"),
    Asset(id="5", name="Solana", symbol="SOL", price="€172,45", volume="€3,25M"),
]

stocks = [
    Asset(id="1", name="Apple Inc.", symbol="AAPL", price="€82.882,76", volume="€42,08M"),
    Asset(id="2", name="Microsoft", symbol="MSFT", price="€3.787,71", volume="€17,42M"),
    Asset(id="3", name="Alphabet", symbol="GOOGL", price="€1,00", volume="€62,87M"),
    Asset(id="4", name="Amazon", symbol="AMZN", price="€2,07", volume="€4,50M"),
    Asset(id="5", name="Tesla", symbol="TSLA", price="€172,45", volume="€3,25M"),
]

# Store active WebSocket connections
connected_clients: Set[websockets.WebSocketServerProtocol] = set()

# Convert price string to float
def convert_to_float(value: str) -> float:
    try:
        # Remove € and M, handle European number format
        value = value.replace('€', '').replace('M', '').strip()
        # Replace period (thousands separator) and comma (decimal separator)
        value = value.replace('.', '').replace(',', '.')
        return float(value)
    except ValueError:
        raise ValueError(f"Invalid price/volume format: {value}")

# Format float to European price string
def format_to_euro(value: float, is_volume: bool = False) -> str:
    # Format with two decimal places, using period for thousands and comma for decimals
    formatted = f"{value:,.2f}".replace(',', 'X').replace('.', ',').replace('X', '.')
    return f"€{formatted}M" if is_volume else f"€{formatted}"

# Simulate price updates
def update_prices():
    while True:
        try:
            for crypto in cryptos:
                current_price = convert_to_float(crypto.price)
                change = current_price * random.uniform(-0.02, 0.02)  # ±2%
                new_price = current_price + change
                crypto.price = format_to_euro(new_price)

                current_volume = convert_to_float(crypto.volume)
                volume_change = current_volume * random.uniform(-0.05, 0.05)  # ±5%
                new_volume = current_volume + volume_change
                crypto.volume = format_to_euro(new_volume, is_volume=True)

            for stock in stocks:
                current_price = convert_to_float(stock.price)
                change = current_price * random.uniform(-0.01, 0.01)  # ±1%
                new_price = current_price + change
                stock.price = format_to_euro(new_price)

                current_volume = convert_to_float(stock.volume)
                volume_change = current_volume * random.uniform(-0.03, 0.03)  # ±3%
                new_volume = current_volume + volume_change
                stock.volume = format_to_euro(new_volume, is_volume=True)

            crypto_market.lastUpdated = f"{datetime.now().strftime('%H:%M:%S')}"
            stock_market.lastUpdated = f"{datetime.now().strftime('%H:%M:%S')}"

            asyncio.run(broadcast_market_data())
            print(f"Prices updated: {datetime.now().strftime('%H:%M:%S')}")
            time.sleep(3)
        except Exception as e:
            print(f"Error updating prices: {e}")
            time.sleep(5)

# Broadcast market data to all WebSocket clients
async def broadcast_market_data():
    if not connected_clients:
        return

    crypto_data = {
        "type": "crypto",
        "market": crypto_market.dict(),
        "assets": [crypto.dict() for crypto in cryptos]
    }

    stock_data = {
        "type": "stock",
        "market": stock_market.dict(),
        "assets": [stock.dict() for stock in stocks]
    }

    crypto_json = json.dumps(crypto_data)
    stock_json = json.dumps(stock_data)

    disconnected_clients = set()
    for client in connected_clients:
        try:
            await client.send(crypto_json)
            await client.send(stock_json)
        except websockets.exceptions.ConnectionClosed:
            disconnected_clients.add(client)

    for client in disconnected_clients:
        connected_clients.remove(client)

# WebSocket handler
async def websocket_handler(websocket: websockets.WebSocketServerProtocol):
    connected_clients.add(websocket)
    print(f"Client connected. Total: {len(connected_clients)}")

    try:
        crypto_data = {
            "type": "crypto",
            "market": crypto_market.dict(),
            "assets": [crypto.dict() for crypto in cryptos]
        }

        stock_data = {
            "type": "stock",
            "market": stock_market.dict(),
            "assets": [stock.dict() for stock in stocks]
        }

        await websocket.send(json.dumps(crypto_data))
        await websocket.send(json.dumps(stock_data))

        async for message in websocket:
            data = json.loads(message)
            print(f"Received message: {data}")

            if data.get("action") == "toggleFavorite":
                asset_id = data.get("id")
                asset_type = data.get("type")

                if asset_type == "crypto":
                    for crypto in cryptos:
                        if crypto.id == asset_id:
                            crypto.isFavorite = not crypto.isFavorite
                            break
                elif asset_type == "stock":
                    for stock in stocks:
                        if stock.id == asset_id:
                            stock.isFavorite = not stock.isFavorite
                            break

    except websockets.exceptions.ConnectionClosed:
        print("Connection closed")
    finally:
        connected_clients.remove(websocket)
        print(f"Client disconnected. Total: {len(connected_clients)}")

# Start WebSocket server
async def start_websocket_server():
    async with websockets.serve(websocket_handler, "0.0.0.0", 8001):
        print("WebSocket server started at ws://0.0.0.0:8001")
        await asyncio.Future()  # Run forever

# Run WebSocket server in a separate thread
def run_websocket_server():
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    loop.run_until_complete(start_websocket_server())

# REST API endpoints
@app.get("/api/crypto-market", response_model=MarketResponse)
def get_crypto_market():
    return {"market": crypto_market, "assets": cryptos}

@app.get("/api/stock-market", response_model=MarketResponse)
def get_stock_market():
    return {"market": stock_market, "assets": stocks}

# Startup event
@app.on_event("startup")
def startup_event():
    price_thread = threading.Thread(target=update_prices, daemon=True)
    price_thread.start()

    websocket_thread = threading.Thread(target=run_websocket_server, daemon=True)
    websocket_thread.start()

# Main entry point
if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8008, reload=False)