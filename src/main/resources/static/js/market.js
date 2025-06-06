document.addEventListener('DOMContentLoaded', function() {
    const wsStocks = new WebSocket('ws://localhost:8001/ws_nfts');
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

        cryptoTableBody.innerHTML = filteredAssets.map(asset => `
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
                <td class="text-right">
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
        `).join('');

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

    const wsNFTs = new WebSocket('ws://localhost:8001/ws_nfts');
    let nftData = null;
    const nftTableBody = document.getElementById('nft-table-body');
    const nftCountElement = document.getElementById('nft-count');

    wsNFTs.onmessage = (event) => {
        try {
            const data = JSON.parse(event.data);
            if (data.type === 'nft' && data.market) {
                if (nftData?.assets) {
                    const favorites = new Set(
                        nftData.assets.filter(a => a.isFavorite).map(a => a.id)
                    );
                    data.assets = data.assets.map(asset => ({
                        ...asset,
                        isFavorite: favorites.has(asset.id)
                    }));
                }
                nftData = data;
                updateNFTUI();
            }
        } catch (error) {
            console.error('Error procesando mensaje de NFTs:', error);
        }
    };

    function updateNFTUI() {
        if (!nftData || !nftData.market) return;
        document.getElementById('nft-market-cap').textContent = nftData.market.marketCap;
        document.getElementById('nft-volume').textContent = nftData.market.volume24h;
        document.getElementById('nft-update-time').textContent = nftData.market.lastUpdated;
        renderNFTTable();
    }

    function renderNFTTable() {
        if (!nftData || !nftData.assets) return;
        const filteredAssets = nftData.assets.filter(asset => {
            const matchesSearch =
                searchQuery === '' ||
                asset.name.toLowerCase().includes(searchQuery) ||
                asset.symbol.toLowerCase().includes(searchQuery);
            return matchesSearch && (!showOnlyFavorites || asset.isFavorite);
        });

        nftCountElement.textContent = filteredAssets.length;

        if (filteredAssets.length === 0) {
            nftTableBody.innerHTML = '<tr><td colspan="4" class="empty-message">No se encontraron NFTs</td></tr>';
            return;
        }

        nftTableBody.innerHTML = filteredAssets.map(asset => `
        <tr>
            <td class="text-left">
                <div class="asset-name-container">
                    <i class="fas fa-star favorite-star ${asset.isFavorite ? 'active' : ''}" data-id="${asset.id}"></i>
                    <img src="${asset.image}" alt="${asset.name}" class="nft-image"/>
                    <div class="asset-name">
                        <div>${asset.name}</div>
                        <div class="asset-symbol">${asset.symbol}</div>
                    </div>
                </div>
            </td>
            <td class="text-right">${asset.price}</td>
            <td class="text-right">${asset.volume}</td>
            <td class="text-center">
                <button class="buy-btn" onclick="window.open('https://opensea.io/assets/${asset.id}', '_blank')">
                    Ver NFT
                </button>
            </td>
        </tr>
    `).join('');

        nftTableBody.querySelectorAll('.favorite-star').forEach(star => {
            star.addEventListener('click', (e) => {
                e.stopPropagation();
                toggleNFTFavorite(e.target.dataset.id);
            });
        });
    }

    function toggleNFTFavorite(id) {
        nftData.assets = nftData.assets.map(asset =>
            asset.id === id ? { ...asset, isFavorite: !asset.isFavorite } : asset
        );
        renderNFTTable();
    }
});