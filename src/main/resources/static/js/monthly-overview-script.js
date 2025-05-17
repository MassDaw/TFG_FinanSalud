document.addEventListener("DOMContentLoaded", () => {

    // Actualizar fecha actual
    const monthNames = [
        "Enero",
        "Febrero",
        "Marzo",
        "Abril",
        "Mayo",
        "Junio",
        "Julio",
        "Agosto",
        "Septiembre",
        "Octubre",
        "Noviembre",
        "Diciembre",
    ]

    const currentDate = new Date()
    const currentMonthYear = `${monthNames[currentDate.getMonth()]} ${currentDate.getFullYear()}`
    document.getElementById("current-month").textContent = currentMonthYear

    // Datos financieros simulados
    const financialData = {
        monthly: {
            labels: ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"],
            incomes: [3200, 3400, 3850, 3500, 3600, 3900, 3700, 3800, 0, 0, 0, 0],
            expenses: [1200, 1350, 1399.25, 1300, 1450, 1500, 1400, 1550, 0, 0, 0, 0],
        },
        quarterly: {
            labels: ["Q1", "Q2", "Q3", "Q4"],
            incomes: [10450, 11000, 11300, 0],
            expenses: [3949.25, 4250, 4350, 0],
        },
        yearly: {
            labels: ["2022", "2023", "2024", "2025"],
            incomes: [38000, 40500, 42000, 10450],
            expenses: [15000, 16200, 17500, 3949.25],
        },
    }

    // Configurar el gráfico
    const ctx = document.getElementById("financial-chart").getContext("2d")
    const financialChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: financialData.monthly.labels,
            datasets: [
                {
                    label: "Ingresos",
                    data: financialData.monthly.incomes,
                    backgroundColor: "#6c5ce7",
                    borderColor: "#6c5ce7",
                    borderWidth: 1,
                    borderRadius: 5,
                    barPercentage: 0.6,
                    categoryPercentage: 0.7,
                },
                {
                    label: "Gastos",
                    data: financialData.monthly.expenses,
                    backgroundColor: "#e2d9ff",
                    borderColor: "#e2d9ff",
                    borderWidth: 1,
                    borderRadius: 5,
                    barPercentage: 0.6,
                    categoryPercentage: 0.7,
                },
            ],
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false,
                },
                tooltip: {
                    mode: "index",
                    intersect: false,
                    callbacks: {
                        label: (context) => {
                            let label = context.dataset.label || ""
                            if (label) {
                                label += ": "
                            }
                            if (context.parsed.y !== null) {
                                label += new Intl.NumberFormat("es-ES", { style: "currency", currency: "EUR" }).format(context.parsed.y)
                            }
                            return label
                        },
                    },
                },
            },
            scales: {
                x: {
                    grid: {
                        display: false,
                    },
                },
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: (value) => "€" + value,
                    },
                },
            },
        },
    })

    // Cambiar periodo del gráfico
    document.getElementById("period-selector").addEventListener("change", function () {
        const period = this.value
        updateChart(period)
    })

    function updateChart(period) {
        financialChart.data.labels = financialData[period].labels
        financialChart.data.datasets[0].data = financialData[period].incomes
        financialChart.data.datasets[1].data = financialData[period].expenses
        financialChart.update()
    }

    // Calcular porcentajes para las barras de progreso
    function calcularPorcentajes() {
        // Para ingresos
        const ingresos = [2800, 450, 320, 150, 130] // Valores de ingresos
        const maxIngreso = Math.max(...ingresos)

        const barrasIngresos = document.querySelectorAll("#income-tab .progress")
        barrasIngresos.forEach((barra, index) => {
            const porcentaje = (ingresos[index] / maxIngreso) * 100
            barra.style.width = `${porcentaje}%`
        })

        // Para gastos
        const gastos = [850, 125.5, 85.3, 65.99, 68.75, 45, 24, 89.95, 44.99] // Valores de gastos
        const maxGasto = Math.max(...gastos)

        const barrasGastos = document.querySelectorAll("#expense-tab .progress")
        barrasGastos.forEach((barra, index) => {
            const porcentaje = (gastos[index] / maxGasto) * 100
            barra.style.width = `${porcentaje}%`
        })
    }


    // Llamar a la función cuando se cargue la página
    calcularPorcentajes()

    // Cambiar entre pestañas de ingresos y g   astos
    const tabButtons = document.querySelectorAll(".tab-btn")
    const tabContents = document.querySelectorAll(".tab-content")

    tabButtons.forEach((button) => {
        button.addEventListener("click", () => {
            // Remover clase active de todos los botones y contenidos
            tabButtons.forEach((btn) => btn.classList.remove("active"))
            tabContents.forEach((content) => content.classList.remove("active"))

            // Añadir clase active al botón clickeado
            button.classList.add("active")

            // Mostrar el contenido correspondiente
            const tabId = button.getAttribute("data-tab")
            document.getElementById(`${tabId}-tab`).classList.add("active")
        })
    })

    // Animación para las tarjetas de resumen
    const summaryCards = document.querySelectorAll(".summary-card")
    summaryCards.forEach((card, index) => {
        setTimeout(() => {
            card.style.opacity = "1"
            card.style.transform = "translateY(0)"
        }, 100 * index)
    })

    // Animación para las transacciones
    const transactionItems = document.querySelectorAll(".transaction-item")
    transactionItems.forEach((item, index) => {
        setTimeout(() => {
            item.style.opacity = "1"
            item.style.transform = "translateX(0)"
        }, 100 * index)
    })

    // Inicializar estilos de animación
    summaryCards.forEach((card) => {
        card.style.opacity = "0"
        card.style.transform = "translateY(20px)"
        card.style.transition = "opacity 0.5s ease, transform 0.5s ease"
    })

    transactionItems.forEach((item) => {
        item.style.opacity = "0"
        item.style.transform = "translateX(20px)"
        item.style.transition = "opacity 0.5s ease, transform 0.5s ease"
    })

})

