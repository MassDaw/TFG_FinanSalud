// Variables globales
let budgets = []
let currentBudgetId = null

// Elementos DOM
const addModal = document.getElementById("add-modal")
const editModal = document.getElementById("edit-modal")
const addBudgetBtn = document.getElementById("add-budget")
const editBudgetBtn = document.getElementById("edit-budget")
const closeBtns = document.querySelectorAll(".close-btn")
const addBudgetForm = document.getElementById("add-budget-form")
const editBudgetForm = document.getElementById("edit-budget-form")
const currentMonthElement = document.getElementById("current-month")

// Configurar fecha actual
function setupCurrentDate() {
    const now = new Date()
    const options = { month: "long", year: "numeric" }
    currentMonthElement.textContent = now.toLocaleDateString("es-ES", options)
}

// Cargar presupuestos desde el archivo JSON
async function loadBudgets() {
    try {
        const response = await fetch("/budgets.json")
        if (!response.ok) {
            throw new Error("Error al cargar los presupuestos")
        }

        budgets = await response.json()
        displayBudgets()
    } catch (error) {
        console.error("Error:", error)
        showNotification("Error al cargar los presupuestos", "error")
    }
}

// Mostrar los presupuestos en la interfaz
function displayBudgets() {
    // Crear contenedor si no existe
    let budgetContainer = document.querySelector(".budget-container")
    if (!budgetContainer) {
        budgetContainer = document.createElement("div")
        budgetContainer.className = "budget-container"
        document.querySelector(".container").appendChild(budgetContainer)
    } else {
        budgetContainer.innerHTML = ""
    }

    // Calcular el total de presupuestos
    const totalBudget = budgets.reduce((sum, budget) => sum + budget.budget, 0)
    const totalSpent = budgets.reduce((sum, budget) => sum + budget.budgetCount, 0)

    // Crear elementos para cada presupuesto
    budgets.forEach((budget) => {
        // Asegurarse de que budgetCount existe y es un número
        if (budget.budgetCount === undefined || budget.budgetCount === null) {
            budget.budgetCount = 0
        }
        const percentage = budget.budget > 0 ? ((budget.budgetCount / budget.budget) * 100).toFixed(1) : 0

        const budgetElement = document.createElement("div")
        budgetElement.className = "budget-item"
        budgetElement.dataset.name = budget.name

        // Asegurarse de que el color existe
        if (!budget.color) {
            budget.color = getRandomColor(budget.name)
        }
        budgetElement.innerHTML = `
            <div class="budget-header">
                <h3>${budget.name}</h3>
                <span class="budget-amount">${budget.budget.toFixed(2)} €</span>
            </div>
            <div class="budget-progress">
                <div class="progress-bar" style="width: ${percentage}%; background-color: ${budget.color}"></div>
            </div>
            <div class="budget-footer">
                <span class="budget-percentage">${percentage}% (${budget.budgetCount.toFixed(2)} €)</span>
                <div class="budget-actions">
                    <button class="edit-item-btn" data-name="${budget.name}"><i class="fas fa-edit"></i></button>
                    <button class="delete-item-btn" data-name="${budget.name}"><i class="fas fa-trash"></i></button>
                </div>
            </div>
        `

        budgetContainer.appendChild(budgetElement)
    })

    // Añadir total
    const totalElement = document.createElement("div")
    totalElement.className = "budget-total"
    totalElement.innerHTML = `
        <h3>Total</h3>
        <div class="total-details">
            <span class="total-budget">Presupuesto: ${totalBudget.toFixed(2)} €</span>
            <span class="total-spent">Gastado: ${totalSpent.toFixed(2)} €</span>
        </div>
    `
    budgetContainer.appendChild(totalElement)

    // Añadir event listeners a los botones de editar y eliminar
    document.querySelectorAll(".edit-item-btn").forEach((btn) => {
        btn.addEventListener("click", () => openEditModal(btn.dataset.name))
    })

    document.querySelectorAll(".delete-item-btn").forEach((btn) => {
        btn.addEventListener("click", () => deleteBudget(btn.dataset.name))
    })
}

// Generar un color basado en el nombre de la categoría
function getRandomColor(name) {
    // Colores predefinidos para categorías comunes
    const categoryColors = {
        alimentacion: "#4CAF50", // Verde
        alimentos: "#4CAF50",
        ocio: "#2196F3", // Azul
        entretenimiento: "#2196F3",
        servicios: "#FFC107", // Amarillo
        transporte: "#FF5722", // Naranja
        salud: "#E91E63", // Rosa
        educacion: "#9C27B0", // Púrpura
        hogar: "#795548", // Marrón
        vivienda: "#795548",
        mascotas: "#607D8B", // Gris azulado
        otros: "#9E9E9E", // Gris
    }

    // Convertir el nombre a minúsculas y sin acentos para la comparación
    const normalizedName = name
        .toLowerCase()
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")

    // Buscar si hay un color predefinido para esta categoría
    for (const [category, color] of Object.entries(categoryColors)) {
        if (normalizedName.includes(category)) {
            return color
        }
    }

    // Si no hay un color predefinido, generar uno basado en el nombre
    let hash = 0
    for (let i = 0; i < name.length; i++) {
        hash = name.charCodeAt(i) + ((hash << 5) - hash)
    }

    let color = "#";
    for (let i = 0; i < 3; i++) {
        const value = (hash >> (i * 8)) & 0xff;
        color += ("00" + value.toString(16)).slice(-2);
    }

    return color
}

// Abrir modal para añadir presupuesto
function openAddModal() {
    addModal.style.display = "block"
    document.getElementById("amount").value = ""

    // Actualizar el selector de categorías
    const categorySelect = document.getElementById("category")
    categorySelect.innerHTML = "" // Limpiar opciones existentes

    // Lista de categorías predefinidas
    const categoriasPredefinidas = [
        "Alimentación",
        "Ocio",
        "Servicios",
        "Transporte",
        "Salud",
        "Educación",
        "Hogar",
        "Mascotas",
        "Otros",
    ]

    // Añadir categorías predefinidas
    categoriasPredefinidas.forEach((categoria) => {
        const option = document.createElement("option")
        option.value = categoria
        option.textContent = categoria
        categorySelect.appendChild(option)
    })

    categorySelect.selectedIndex = 0
}

// Abrir modal para editar presupuesto
function openEditModal(name) {
    const budget = budgets.find((b) => b.name === name)
    if (!budget) return

    currentBudgetId = name
    document.getElementById("edit-amount").value = budget.budget

    // Actualizar el selector de categorías para mostrar solo los presupuestos existentes
    const categorySelect = document.getElementById("edit-category")
    categorySelect.innerHTML = "" // Limpiar opciones existentes

    // Añadir solo las categorías existentes
    budgets.forEach((budget) => {
        const option = document.createElement("option")
        option.value = budget.name
        option.textContent = budget.name
        categorySelect.appendChild(option)
    })

    // Seleccionar la categoría actual
    for (let i = 0; i < categorySelect.options.length; i++) {
        if (categorySelect.options[i].value === name) {
            categorySelect.selectedIndex = i
            break
        }
    }

    editModal.style.display = "block"
}

// Cerrar modales
function closeModals() {
    addModal.style.display = "none"
    editModal.style.display = "none"
    currentBudgetId = null
}

// Añadir nuevo presupuesto
function addBudget(event) {
    event.preventDefault()

    const categorySelect = document.getElementById("category")
    const categoryText = categorySelect.options[categorySelect.selectedIndex].text
    const amount = Number.parseFloat(document.getElementById("amount").value)

    // Verificar si ya existe un presupuesto con este nombre
    const existingBudget = budgets.find((b) => b.name === categoryText)
    if (existingBudget) {
        // Preguntar al usuario si desea modificar el presupuesto existente
        if (
            confirm(
                `Ya existe un presupuesto para "${categoryText}" con un valor de ${existingBudget.budget.toFixed(2)} €. ¿Desea modificarlo?`,
            )
        ) {
            // Si el usuario confirma, abrir el modal de edición para este presupuesto
            closeModals()
            openEditModal(categoryText)
            return
        } else {
            // Si el usuario cancela, no hacer nada
            return
        }
    }

    // Generar un color aleatorio para el nuevo presupuesto
    const color = getRandomColor(categoryText)

    const newBudget = {
        name: categoryText,
        budget: amount,
        budgetCount: 0, // Inicialmente no hay gasto
        color: color,
    }

    // Añadir el nuevo presupuesto al array
    budgets.push(newBudget)

    // Actualizar la interfaz
    displayBudgets()
    closeModals()
    showNotification("Presupuesto añadido correctamente", "success")
}

// Actualizar presupuesto existente
function updateBudget(event) {
    event.preventDefault()

    if (!currentBudgetId) return

    const amount = Number.parseFloat(document.getElementById("edit-amount").value)

    // Encontrar el presupuesto a actualizar
    const index = budgets.findIndex((b) => b.name === currentBudgetId)
    if (index !== -1) {
        // Actualizar solo el monto del presupuesto
        budgets[index].budget = amount
    }

    // Actualizar la interfaz
    displayBudgets()
    closeModals()
    showNotification("Presupuesto actualizado correctamente", "success")
}

// Eliminar presupuesto
function deleteBudget(name) {
    if (!confirm("¿Estás seguro de que quieres eliminar este presupuesto?")) {
        return
    }

    // Filtrar el presupuesto a eliminar
    budgets = budgets.filter((b) => b.name !== name)

    // Actualizar la interfaz
    displayBudgets()
    showNotification("Presupuesto eliminado correctamente", "success")
}

// Mostrar notificación
function showNotification(message, type = "info") {
    // Crear elemento de notificación si no existe
    let notification = document.querySelector(".notification")
    if (!notification) {
        notification = document.createElement("div")
        notification.className = "notification"
        document.body.appendChild(notification)
    }

    // Establecer clase según el tipo
    notification.className = `notification ${type}`
    notification.textContent = message
    notification.style.display = "block"

    // Ocultar después de 3 segundos
    setTimeout(() => {
        notification.style.display = "none"
    }, 3000)
}

// Event Listeners
document.addEventListener("DOMContentLoaded", async () => {
    setupCurrentDate()
    await loadBudgets()  // Esperar a que los presupuestos se carguen

    addBudgetBtn.addEventListener("click", openAddModal)
    editBudgetBtn.addEventListener("click", () => {
        if (budgets.length > 0) {
            openEditModal(budgets[0].name)
        } else {
            showNotification("No hay presupuestos para editar", "info")
        }
    })

    closeBtns.forEach((btn) => {
        btn.addEventListener("click", closeModals)
    })

    addBudgetForm.addEventListener("submit", addBudget)
    editBudgetForm.addEventListener("submit", updateBudget)

    // Cerrar modal al hacer clic fuera
    window.addEventListener("click", (event) => {
        if (event.target === addModal) {
            closeModals()
        }
        if (event.target === editModal) {
            closeModals()
        }
    })
})

