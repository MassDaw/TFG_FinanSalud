document.addEventListener('DOMContentLoaded', function() {
    // Variables para almacenar datos
    let cryptoMarket = {
        marketCap: "€0.00M",
        volume24h: "€0.00M",
        lastUpdated: "..."
    };

    let stockMarket = {
        marketCap: "€0.00M",
        volume24h: "€0.00M",
        lastUpdated: "..."
    };

    let cryptos = [];
    let stocks = [];

    // Variables de estado
    let showOnlyFavorites = false;
    let searchQuery = '';
    let websocket = null;

    // Elementos DOM
    const searchInput = document.getElementById('search-input');
    const favoritesBtn = document.getElementById('favorites-btn');
    const cryptoTableBody = document.getElementById('crypto-table-body');
    const stockTableBody = document.getElementById('stock-table-body');
    const cryptoCountElement = document.getElementById('crypto-count');
    const stockCountElement = document.getElementById('stock-count');
    const connectionStatusElement = document.getElementById('connection-status');

    // Configurar eventos
    searchInput.addEventListener('input', function(e) {
        searchQuery = e.target.value.toLowerCase();
        renderAssetTables();
    });

    favoritesBtn.addEventListener('click', function() {
        showOnlyFavorites = !showOnlyFavorites;
        this.classList.toggle('active', showOnlyFavorites);
        renderAssetTables();
    });

    // Función para conectar al WebSocket de Python
    function connectWebSocket() {
        // Usar la URL del servidor WebSocket de Python
        websocket = new WebSocket('ws://localhost:8001');

        websocket.onopen = function(event) {
            console.log('Conectado al servidor WebSocket de Python');
            updateConnectionStatus('Conectado', true);
        };

        websocket.onmessage = function(event) {
            try {
                const data = JSON.parse(event.data);
                console.log('Datos recibidos:', data.type);

                if (data.type === 'crypto') {
                    updateCryptoData(data);
                } else if (data.type === 'stock') {
                    updateStockData(data);
                }
            } catch (error) {
                console.error('Error al procesar mensaje:', error);
            }
        };

        websocket.onclose = function(event) {
            console.log('Desconectado del servidor WebSocket');
            updateConnectionStatus('Desconectado', false);

            // Intentar reconectar después de 5 segundos
            setTimeout(connectWebSocket, 5000);
        };

        websocket.onerror = function(error) {
            console.error('Error de WebSocket:', error);
            updateConnectionStatus('Error de conexión', false);
        };
    }

    // Función para actualizar el estado de conexión
    function updateConnectionStatus(status, isConnected) {
        if (connectionStatusElement) {
            connectionStatusElement.textContent = status;
            connectionStatusElement.className = isConnected ? 'status-connected' : 'status-disconnected';
        }
    }

    // Función para actualizar datos de criptomonedas
    function updateCryptoData(data) {
        if (data && data.market && data.assets) {
            cryptoMarket = data.market;

            // Preservar el estado de favoritos
            const favoriteMap = {};
            cryptos.forEach(crypto => {
                if (crypto.isFavorite) {
                    favoriteMap[crypto.id] = true;
                }
            });

            cryptos = data.assets.map(asset => ({
                ...asset,
                isFavorite: favoriteMap[asset.id] || asset.isFavorite || false
            }));

            updateMarketData();
            renderAssetTables();
        }
    }

    // Función para actualizar datos de acciones
    function updateStockData(data) {
        if (data && data.market && data.assets) {
            stockMarket = data.market;

            // Preservar el estado de favoritos
            const favoriteMap = {};
            stocks.forEach(stock => {
                if (stock.isFavorite) {
                    favoriteMap[stock.id] = true;
                }
            });

            stocks = data.assets.map(asset => ({
                ...asset,
                isFavorite: favoriteMap[asset.id] || asset.isFavorite || false
            }));

            updateMarketData();
            renderAssetTables();
        }
    }

    // Función para actualizar datos de mercado en la UI
    function updateMarketData() {
        document.getElementById('crypto-market-cap').textContent = cryptoMarket.marketCap;
        document.getElementById('crypto-volume').textContent = cryptoMarket.volume24h;
        document.getElementById('crypto-update-time').textContent = cryptoMarket.lastUpdated;

        document.getElementById('stock-market-cap').textContent = stockMarket.marketCap;
        document.getElementById('stock-volume').textContent = stockMarket.volume24h;
        document.getElementById('stock-update-time').textContent = stockMarket.lastUpdated;
    }

    // Función para renderizar tablas de activos
    function renderAssetTables() {
        const filteredCryptos = filterAssets(cryptos);
        const filteredStocks = filterAssets(stocks);

        renderAssetTable(cryptoTableBody, filteredCryptos, 'crypto');
        renderAssetTable(stockTableBody, filteredStocks, 'stock');

        // Actualizar contadores
        cryptoCountElement.textContent = filteredCryptos.length;
        stockCountElement.textContent = filteredStocks.length;
    }

    // Función para filtrar activos según búsqueda y favoritos
    function filterAssets(assets) {
        return assets.filter(asset => {
            const matchesSearch =
                searchQuery === '' ||
                asset.name.toLowerCase().includes(searchQuery) ||
                asset.symbol.toLowerCase().includes(searchQuery);

            return matchesSearch && (!showOnlyFavorites || asset.isFavorite);
        });
    }

    // Función para renderizar una tabla de activos
    function renderAssetTable(tableBody, assets, type) {
        tableBody.innerHTML = '';

        if (assets.length === 0) {
            const row = document.createElement('tr');
            const cell = document.createElement('td');
            cell.colSpan = 3;
            cell.className = 'empty-message';
            cell.textContent = 'No se encontraron activos';
            row.appendChild(cell);
            tableBody.appendChild(row);
            return;
        }

        assets.forEach(asset => {
            const row = document.createElement('tr');

            // Celda de nombre
            const nameCell = document.createElement('td');
            nameCell.className = 'text-left';

            const nameContainer = document.createElement('div');
            nameContainer.className = 'asset-name-container';

            const starIcon = document.createElement('i');
            starIcon.className = `fas fa-star favorite-star ${asset.isFavorite ? 'active' : ''}`;
            starIcon.setAttribute('data-id', asset.id);
            starIcon.setAttribute('data-type', type);
            starIcon.addEventListener('click', function() {
                const id = this.getAttribute('data-id');
                const assetType = this.getAttribute('data-type');
                toggleFavorite(id, assetType);
            });

            const nameDiv = document.createElement('div');
            nameDiv.className = 'asset-name';

            const nameText = document.createElement('div');
            nameText.textContent = asset.name;

            const symbolText = document.createElement('div');
            symbolText.className = 'asset-symbol';
            symbolText.textContent = asset.symbol;

            nameDiv.appendChild(nameText);
            nameDiv.appendChild(symbolText);
            nameContainer.appendChild(starIcon);
            nameContainer.appendChild(nameDiv);
            nameCell.appendChild(nameContainer);

            // Celda de precio
            const priceCell = document.createElement('td');
            priceCell.className = 'text-right';

            const priceContainer = document.createElement('div');
            priceContainer.className = 'asset-price-container';

            const priceText = document.createElement('div');
            priceText.textContent = asset.price;

            const volumeText = document.createElement('div');
            volumeText.className = 'asset-volume';
            volumeText.textContent = `Vol: ${asset.volume}`;

            priceContainer.appendChild(priceText);
            priceContainer.appendChild(volumeText);
            priceCell.appendChild(priceContainer);

            // Celda de botón comprar
            const buyCell = document.createElement('td');
            buyCell.className = 'text-center';

            const buyButton = document.createElement('button');
            buyButton.className = 'buy-btn';
            buyButton.textContent = 'Comprar';
            buyButton.addEventListener('click', function() {
                alert(`Comprando ${asset.name} (${asset.symbol})`);
            });

            buyCell.appendChild(buyButton);

            // Añadir celdas a la fila
            row.appendChild(nameCell);
            row.appendChild(priceCell);
            row.appendChild(buyCell);

            // Añadir fila a la tabla
            tableBody.appendChild(row);
        });
    }

    // Función para marcar/desmarcar favoritos
    function toggleFavorite(id, type) {
        if (type === 'crypto') {
            cryptos = cryptos.map(crypto =>
                crypto.id === id ? {...crypto, isFavorite: !crypto.isFavorite} : crypto
            );
        } else {
            stocks = stocks.map(stock =>
                stock.id === id ? {...stock, isFavorite: !stock.isFavorite} : stock
            );
        }

        // Enviar actualización al servidor WebSocket
        if (websocket && websocket.readyState === WebSocket.OPEN) {
            websocket.send(JSON.stringify({
                action: 'toggleFavorite',
                id: id,
                type: type
            }));
        }

        renderAssetTables();
    }

    // Función para cargar datos iniciales (fallback si WebSocket falla)
    async function fetchInitialData() {
        try {
            const cryptoResponse = await fetch('http://localhost:8000/api/crypto-market');
            const cryptoData = await cryptoResponse.json();

            cryptoMarket = cryptoData.market;
            cryptos = cryptoData.assets;

            const stockResponse = await fetch('http://localhost:8000/api/stock-market');
            const stockData = await stockResponse.json();

            stockMarket = stockData.market;
            stocks = stockData.assets;

            updateMarketData();
            renderAssetTables();

            console.log("Datos iniciales cargados");
        } catch (error) {
            console.error("Error al cargar datos iniciales:", error);
        }
    }

    // Iniciar conexión WebSocket
    connectWebSocket();

    // Cargar datos iniciales como fallback
    fetchInitialData();

    // Manejar desconexión al cerrar la página
    window.addEventListener('beforeunload', function() {
        if (websocket) {
            websocket.close();
        }
    });
});