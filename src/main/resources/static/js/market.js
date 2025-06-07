document.addEventListener('DOMContentLoaded', function() {
    const ws = new WebSocket('ws://localhost:8001/ws');
    let cryptoData = null;
    let showOnlyFavorites = false;
    let searchQuery = '';

    const searchInput = document.getElementById('search-input');
    const favoritesBtn = document.getElementById('favorites-btn');
    const cryptoTableBody = document.getElementById('crypto-table-body');
    const cryptoCountElement = document.getElementById('crypto-count');
    // Configuración de los listeners de eventos
    searchInput.addEventListener('input', (e) => {
        searchQuery = e.target.value.toLowerCase();
        renderCryptoTable();
    });

    favoritesBtn.addEventListener('click', () => {
        showOnlyFavorites = !showOnlyFavorites;
        favoritesBtn.classList.toggle('active', showOnlyFavorites);
        renderCryptoTable();
    });

    // Gestión de la conexión WebSocket
    ws.onopen = () => {
        console.log('Conexión WebSocket establecida');
    };

    ws.onclose = () => {
        console.log('Conexión WebSocket cerrada');
        // Intentar reconectar cada 5 segundos
        setTimeout(() => {
            window.location.reload();
        }, 5000);
    };

    ws.onerror = (error) => {
        console.error('Error en WebSocket:', error);
    };

    ws.onmessage = (event) => {
        try {
            const data = JSON.parse(event.data);
            if (data.type === 'crypto' && data.market) {
                if (cryptoData?.assets) {
                    const favorites = new Set(
                        cryptoData.assets.filter(a => a.isFavorite).map(a => a.id)
                    );
                    data.assets = data.assets.map(asset => ({
                        ...asset,
                        isFavorite: favorites.has(asset.id)
                    }));
                }
                cryptoData = data;
                updateUI();
            }
        } catch (error) {
            console.error('Error procesando mensaje:', error);
        }
    };

    function updateUI() {
        if (!cryptoData || !cryptoData.market) return;
        document.getElementById('crypto-market-cap').textContent = cryptoData.market.marketCap;
        document.getElementById('crypto-volume').textContent = cryptoData.market.volume24h;
        document.getElementById('crypto-update-time').textContent = cryptoData.market.lastUpdated;
        renderCryptoTable();
    }

    function renderCryptoTable() {
        if (!cryptoData || !cryptoData.assets) return;
        const filteredAssets = cryptoData.assets.filter(asset => {
            const matchesSearch =
                searchQuery === '' ||
                asset.name.toLowerCase().includes(searchQuery) ||
                asset.symbol.toLowerCase().includes(searchQuery);
            return matchesSearch && (!showOnlyFavorites || asset.isFavorite);
        });

        cryptoCountElement.textContent = filteredAssets.length;

        if (filteredAssets.length === 0) {
            cryptoTableBody.innerHTML = '<tr><td colspan="3" class="empty-message">No se encontraron activos</td></tr>';
            return;
        }

        cryptoTableBody.parentElement.innerHTML = `
    <thead>
        <tr>
            <th class="text-left">Activo</th>
            <th class="text-right">Precio</th>
            <th class="text-center">Comprar</th>
        </tr>
    </thead>
    <tbody id="crypto-table-body">
        ${filteredAssets.map(asset => `
            <tr>
                <td class="text-left">
                    <div class="asset-name-container">
                        <i class="fas fa-star favorite-star ${asset.isFavorite ? 'active' : ''}" data-id="${asset.id}"></i>
                        <div class="asset-name">
                            <div>${asset.name}</div>
                            <div class="asset-symbol">${asset.symbol}</div>
                        </div>
                    </div>
                </td>
<td class="text-center">
    <div class="asset-price-container">
        <div>${asset.price}</div>
        <div class="asset-volume">Vol: ${asset.volume}</div>
    </div>
</td>
                <td class="text-center">
                    <button class="buy-btn" onclick="window.location.href='https://www.binance.com/es/trade/${asset.symbol}_EUR?type=spot'">
                        Comprar
                    </button>
                </td>
            </tr>
        `).join('')}
    </tbody>
`;
        // Agregar listeners para los favoritos
        cryptoTableBody.querySelectorAll('.favorite-star').forEach(star => {
            star.addEventListener('click', (e) => {
                e.stopPropagation();
                toggleFavorite(e.target.dataset.id);
            });
        });
    }

    function toggleFavorite(id) {
        cryptoData.assets = cryptoData.assets.map(asset =>
            asset.id === id ? { ...asset, isFavorite: !asset.isFavorite } : asset
        );
        renderCryptoTable();
    }

});