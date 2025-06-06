from flask import Flask
from flask_sock import Sock
import requests
import json
import time
from datetime import datetime

app = Flask(__name__)
sock = Sock(app)

# Configuración para API pública
COINGECKO_API = "https://api.coingecko.com/api/v3"
UPDATE_INTERVAL = 30  # segundos

def format_number(number):
    if number is None:
        return "€0.00"
    try:
        number = float(number)
        if number >= 1_000_000_000:
            return f"€{number/1_000_000_000:.2f}B"
        elif number >= 1_000_000:
            return f"€{number/1_000_000:.2f}M"
        else:
            return f"€{number:,.2f}"
    except (TypeError, ValueError):
        return "€0.00"

def get_crypto_data():
    try:
        global_response = requests.get(f"{COINGECKO_API}/global")
        global_response.raise_for_status()
        global_data = global_response.json()

        coins_response = requests.get(
            f"{COINGECKO_API}/coins/markets",
            params={
                "vs_currency": "eur",
                "order": "market_cap_desc",
                "per_page": 50,
                "sparkline": False
            }
        )
        coins_response.raise_for_status()
        coins = coins_response.json()

        # Validación de datos globales
        market_cap = global_data.get("data", {}).get("total_market_cap", {}).get("eur", 0)
        volume_24h = global_data.get("data", {}).get("total_volume", {}).get("eur", 0)

        market_data = {
            "marketCap": format_number(market_cap),
            "volume24h": format_number(volume_24h),
            "lastUpdated": datetime.now().strftime("%H:%M:%S")
        }

        assets = [{
            "id": coin.get("id", ""),
            "name": coin.get("name", ""),
            "symbol": coin.get("symbol", "").upper(),
            "price": format_number(coin.get("current_price")),
            "volume": format_number(coin.get("total_volume")),
            "isFavorite": False
        } for coin in coins if coin]

        return {
            "type": "crypto",
            "market": market_data,
            "assets": assets
        }
    except Exception as e:
        print(f"Error obteniendo datos: {e}")
        return None

@sock.route('/ws')
def handle_websocket(ws):
    while True:
        data = get_crypto_data()
        if data:
            ws.send(json.dumps(data))
        time.sleep(UPDATE_INTERVAL)

# ================== NFT ==================

import os

MORALIS_API = "https://deep-index.moralis.io/api/v2/nft/market"
MORALIS_API_KEY = os.getenv("MORALIS_API_KEY")  # Debes definir tu API Key

def get_nft_data():
    try:
        headers = {
            "accept": "application/json",
            "X-API-Key": MORALIS_API_KEY
        }
        response = requests.get(MORALIS_API, headers=headers, params={"chain": "eth", "limit": 20})
        response.raise_for_status()
        nfts = response.json().get("result", [])

        market_data = {
            "marketCap": "N/A",  # Puedes calcular si la API lo permite
            "volume24h": "N/A",
            "lastUpdated": datetime.now().strftime("%H:%M:%S")
        }

        assets = [{
            "id": nft.get("token_address", "") + "-" + nft.get("token_id", ""),
            "name": nft.get("name", "NFT"),
            "symbol": nft.get("symbol", "NFT"),
            "price": nft.get("price", "N/A"),  # Si la API lo permite
            "volume": nft.get("amount", "N/A"),
            "image": nft.get("image", ""),
            "isFavorite": False
        } for nft in nfts]

        return {
            "type": "nft",
            "market": market_data,
            "assets": assets
        }
    except Exception as e:
        print(f"Error obteniendo NFTs: {e}")
        return None

@sock.route('/ws_nfts')
def handle_nft_websocket(ws):
    while True:
        data = get_nft_data()
        if data:
            ws.send(json.dumps(data))
        time.sleep(UPDATE_INTERVAL)

if __name__ == '__main__':
    app.run(port=8001, debug=True)