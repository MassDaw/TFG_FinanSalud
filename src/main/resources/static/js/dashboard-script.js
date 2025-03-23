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

    // Obtener elementos del DOM
    const addModal = document.getElementById("add-modal")
    const editModal = document.getElementById("edit-modal")
    const addBtn = document.getElementById("add-budget")
    const editBtn = document.getElementById("edit-budget")
    const closeBtns = document.querySelectorAll(".close-btn")
    const addForm = document.getElementById("add-budget-form")
    const editForm = document.getElementById("edit-budget-form")

    // Datos de presupuesto (simulados)
    const budgetData = {
        alimentacion: { presupuesto: 400, gastado: 350, visible: true },
        ocio: { presupuesto: 200, gastado: 120, visible: true },
        servicios: { presupuesto: 250, gastado: 180, visible: true },
        mascotas: { presupuesto: 100, gastado: 50, visible: true },
        transporte: { presupuesto: 150, gastado: 80, visible: false },
        salud: { presupuesto: 120, gastado: 30, visible: false },
        educacion: { presupuesto: 180, gastado: 100, visible: false },
        hogar: { presupuesto: 300, gastado: 200, visible: false },
        otros: { presupuesto: 250, gastado: 50, visible: false },
    }

    // Calcular y actualizar el presupuesto total
    function actualizarPresupuestoTotal() {
        let totalPresupuesto = 0
        let totalGastado = 0

        // Solo sumar las categorías visibles
        for (const categoria in budgetData) {
            if (budgetData[categoria].visible) {
                totalPresupuesto += budgetData[categoria].presupuesto
                totalGastado += budgetData[categoria].gastado
            }
        }

        const porcentajeUtilizado = (totalGastado / totalPresupuesto) * 100

        document.querySelector(".budget-amount").textContent = `€${totalPresupuesto.toFixed(2)}`
        document.querySelector(".spent-amount").textContent = `Gastado: €${totalGastado.toFixed(2)}`
        document.querySelector(".total-budget-card .progress").style.width = `${porcentajeUtilizado}%`
        document.querySelector(".progress-text").textContent = `${porcentajeUtilizado.toFixed(1)}% utilizado`
    }

    // Abrir modal de añadir presupuesto
    addBtn.addEventListener("click", () => {
        // Filtrar las categorías que no están visibles para el selector
        const categorySelect = document.getElementById("category")
        categorySelect.innerHTML = "" // Limpiar opciones existentes

        // Primero añadir las categorías ya visibles
        for (const categoria in budgetData) {
            if (budgetData[categoria].visible) {
                const option = document.createElement("option")
                option.value = categoria
                option.textContent = categoria.charAt(0).toUpperCase() + categoria.slice(1)
                categorySelect.appendChild(option)
            }
        }

        // Añadir un separador si hay categorías no visibles
        let hayCategoriaNoVisible = false
        for (const categoria in budgetData) {
            if (!budgetData[categoria].visible) {
                hayCategoriaNoVisible = true
                break
            }
        }

        if (hayCategoriaNoVisible) {
            const separator = document.createElement("option")
            separator.disabled = true
            separator.textContent = "-- Añadir nueva categoría --"
            categorySelect.appendChild(separator)

            // Luego añadir las categorías no visibles
            for (const categoria in budgetData) {
                if (!budgetData[categoria].visible) {
                    const option = document.createElement("option")
                    option.value = categoria
                    option.textContent = categoria.charAt(0).toUpperCase() + categoria.slice(1) + " (Nueva)"
                    categorySelect.appendChild(option)
                }
            }
        }

        addModal.style.display = "flex"
    })

    // Abrir modal de editar presupuesto
    editBtn.addEventListener("click", () => {
        // Solo permitir editar categorías visibles
        const editCategorySelect = document.getElementById("edit-category")
        editCategorySelect.innerHTML = "" // Limpiar opciones existentes

        // Añadir solo las categorías visibles
        for (const categoria in budgetData) {
            if (budgetData[categoria].visible) {
                const option = document.createElement("option")
                option.value = categoria
                option.textContent = categoria.charAt(0).toUpperCase() + categoria.slice(1)
                editCategorySelect.appendChild(option)
            }
        }

        // Precargar el formulario con la primera categoría visible
        let primerCategoriaVisible = null
        for (const categoria in budgetData) {
            if (budgetData[categoria].visible) {
                primerCategoriaVisible = categoria
                break
            }
        }

        if (primerCategoriaVisible) {
            document.getElementById("edit-category").value = primerCategoriaVisible
            document.getElementById("edit-amount").value = budgetData[primerCategoriaVisible].presupuesto
            editModal.style.display = "flex"
        } else {
            alert("No hay categorías disponibles para editar.")
        }
    })

    // Cerrar modales
    closeBtns.forEach((btn) => {
        btn.addEventListener("click", () => {
            addModal.style.display = "none"
            editModal.style.display = "none"
        })
    })

    // Cerrar modal al hacer clic fuera del contenido
    window.addEventListener("click", (event) => {
        if (event.target === addModal) {
            addModal.style.display = "none"
        }
        if (event.target === editModal) {
            editModal.style.display = "none"
        }
    })

    // Manejar envío del formulario de añadir presupuesto
    addForm.addEventListener("submit", (event) => {
        event.preventDefault()

        const categoria = document.getElementById("category").value
        const cantidad = Number.parseFloat(document.getElementById("amount").value)

        if (categoria && !isNaN(cantidad)) {
            // Verificar si es una categoría ya visible con presupuesto
            if (budgetData[categoria].visible && budgetData[categoria].presupuesto > 0) {
                // Preguntar al usuario si desea actualizar el presupuesto existente
                const confirmar = confirm(
                    `Ya existe un presupuesto para ${categoria.charAt(0).toUpperCase() + categoria.slice(1)} de €${budgetData[categoria].presupuesto.toFixed(2)}. ¿Desea actualizarlo a €${cantidad.toFixed(2)}?`,
                )

                if (!confirmar) {
                    return // Si el usuario cancela, no hacer nada
                }
            } else if (!budgetData[categoria].visible) {
                // Si es una categoría nueva (no visible), hacerla visible
                budgetData[categoria].visible = true
            }

            // Actualizar datos
            budgetData[categoria].presupuesto = cantidad

            // Actualizar la interfaz
            actualizarInterfaz()

            // Cerrar el modal
            addModal.style.display = "none"

            // Resetear el formulario
            addForm.reset()
        }
    })

    // Manejar envío del formulario de editar presupuesto
    editForm.addEventListener("submit", (event) => {
        event.preventDefault()

        const categoria = document.getElementById("edit-category").value
        const cantidad = Number.parseFloat(document.getElementById("edit-amount").value)

        if (categoria && !isNaN(cantidad)) {
            // Actualizar datos
            budgetData[categoria].presupuesto = cantidad

            // Actualizar la interfaz
            actualizarInterfaz()

            // Cerrar el modal
            editModal.style.display = "none"

            // Resetear el formulario
            editForm.reset()
        }
    })

    // Función para actualizar la interfaz con los datos actuales
    function actualizarInterfaz() {
        // Actualizar el presupuesto total
        actualizarPresupuestoTotal()

        // Obtener el contenedor de categorías
        const categoriesContainer = document.querySelector(".budget-categories")

        // Limpiar el contenedor
        categoriesContainer.innerHTML = ""

        // Colores e iconos para cada categoría
        const categoryStyles = {
            alimentacion: { bgColor: "#e6f7ff", iconColor: "#1890ff", icon: "fa-utensils" },
            ocio: { bgColor: "#fff2e8", iconColor: "#fa8c16", icon: "fa-film" },
            servicios: { bgColor: "#e6fffb", iconColor: "#13c2c2", icon: "fa-bolt" },
            mascotas: { bgColor: "#f9f0ff", iconColor: "#722ed1", icon: "fa-paw" },
            transporte: { bgColor: "#fcf4ff", iconColor: "#eb2f96", icon: "fa-car" },
            salud: { bgColor: "#f6ffed", iconColor: "#52c41a", icon: "fa-heartbeat" },
            educacion: { bgColor: "#e6f7ff", iconColor: "#1890ff", icon: "fa-book" },
            hogar: { bgColor: "#fff7e6", iconColor: "#fa8c16", icon: "fa-home" },
            otros: { bgColor: "#fcffe6", iconColor: "#a0d911", icon: "fa-ellipsis-h" },
        }

        // Crear tarjetas solo para categorías visibles
        for (const categoria in budgetData) {
            // Solo mostrar categorías marcadas como visibles
            if (budgetData[categoria].visible) {
                const datos = budgetData[categoria]
                const presupuesto = datos.presupuesto
                const gastado = datos.gastado
                const porcentaje = (gastado / presupuesto) * 100
                const style = categoryStyles[categoria]

                // Crear elemento de tarjeta
                const card = document.createElement("div")
                card.className = "category-card"
                card.innerHTML = `
                <div class="category-icon" style="background-color: ${style.bgColor};">
                    <i class="fas ${style.icon}" style="color: ${style.iconColor};"></i>
                </div>
                <div class="category-details">
                    <div class="category-header">
                        <h3>${categoria.charAt(0).toUpperCase() + categoria.slice(1)}</h3>
                        <span class="amount">-€${gastado.toFixed(2)}</span>
                    </div>
                    <div class="category-date">Presupuesto: €${presupuesto.toFixed(2)}</div>
                    <div class="progress-container">
                        <div class="progress-bar">
                            <div class="progress" style="width: ${porcentaje}%; background-color: ${style.iconColor};"></div>
                        </div>
                    </div>
                </div>
            `

                // Añadir la tarjeta al contenedor
                categoriesContainer.appendChild(card)
            }
        }

        // Actualizar el texto del botón de añadir según si hay categorías no visibles
        const addBtn = document.getElementById("add-budget")
        let hayCategoriaNoVisible = false
        for (const categoria in budgetData) {
            if (!budgetData[categoria].visible) {
                hayCategoriaNoVisible = true
                break
            }
        }

        if (hayCategoriaNoVisible) {
            addBtn.innerHTML = '<i class="fas fa-plus"></i> Añadir Presupuesto'
        } else {
            addBtn.innerHTML = '<i class="fas fa-edit"></i> Añadir Presupuesto'
        }
    }

    // Inicializar la interfaz
    actualizarInterfaz()
})

