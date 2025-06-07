document.addEventListener("DOMContentLoaded", () => {
    // Actualizar fecha actual
    const monthNames = [
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    ];
    const currentDate = new Date();
    const currentMonthYear = `${monthNames[currentDate.getMonth()]} ${currentDate.getFullYear()}`;
    document.getElementById("current-month").textContent = currentMonthYear;

    // Obtener datos de la API y calcular totales
    fetch('/item/currentMonth')
        .then(response => response.json())
        .then(data => {
            let totalIngresos = 0;
            let totalGastos = 0;

            // Recorrer las categorías y sumar ingresos y gastos
            for (const items of Object.values(data)) {
                items.forEach(item => {
                    if (item.income) {
                        totalIngresos += item.itemPrice;
                    } else {
                        totalGastos += item.itemPrice;
                    }
                });
            }

            // Calcular balance total
            const balanceTotal = totalIngresos - totalGastos;

            // Actualizar los valores en el DOM
            document.getElementById("ingresosTotales").textContent = `€${totalIngresos.toFixed(2)}`;
            document.getElementById("gastosTotales").textContent = `€${totalGastos.toFixed(2)}`;
            document.getElementById("balanceTotal").textContent = `€${balanceTotal.toFixed(2)}`;
        })
        .catch(error => {
            console.error("Error al cargar los datos:", error);
        });

    // Cargar transacciones recientes
    fetch('/item/currentMonth')
        .then(response => response.json())
        .then(data => {
            const allItems = [];

            // Recorre cada categoría y sus ítems
            for (const [category, items] of Object.entries(data)) {
                items.forEach(item => {
                    item.category = category; // Agrega la categoría a cada item
                    allItems.push(item);
                });
            }

            const container = document.getElementById('recent-transactions-list');
            container.innerHTML = '';

            const filteredItems = allItems.filter(item => item && item.itemName && item.itemPrice !== undefined);

            if (filteredItems.length === 0) {
                container.innerHTML = `
                    <div class="text-center text-muted py-3">
                        <i class="fas fa-info-circle mb-2"></i>
                        <p>No hay transacciones recientes</p>
                    </div>
                `;
                return;
            }

            filteredItems.slice(0, 5).forEach(item => {
                container.innerHTML += `
                    <div class="list-group-item d-flex align-items-center py-3 px-0">
                        <div class="flex-grow-1">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <h6 class="mb-0">${item.itemName}</h6>
                                    <p class="text-muted small mb-0">${item.category} ${item.itemDescription ? `– ${item.itemDescription}` : ''}</p>
                                </div>
                                <div class="fw-bold">€${parseFloat(item.itemPrice).toFixed(2)}</div>
                            </div>
                        </div>
                    </div>
                `;
            });
        })
        .catch(error => {
            console.error('Error al cargar transacciones:', error);
            const container = document.getElementById('recent-transactions-list');
            container.innerHTML = `
                <div class="text-center text-muted py-3">
                    <i class="fas fa-exclamation-circle mb-2"></i>
                    <p>No se pudieron cargar las transacciones</p>
                </div>
            `;
        });
});