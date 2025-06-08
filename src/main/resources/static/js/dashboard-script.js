// Variables globales
let budgets = [];
let currentBudgetId = null;

// Elementos DOM
const addModal = document.getElementById("add-modal");
const editModal = document.getElementById("edit-modal");
const addBudgetBtn = document.getElementById("add-budget");
const closeBtns = document.querySelectorAll(".close-btn");
const addBudgetForm = document.getElementById("add-budget-form");
const editBudgetForm = document.getElementById("edit-budget-form");
const currentMonthElement = document.getElementById("current-month");

// Configurar fecha actual
function setupCurrentDate() {
    const now = new Date();
    const options = { month: "long", year: "numeric" };
    currentMonthElement.textContent = now.toLocaleDateString("es-ES", options);
}
//
// let budgetChart = null
// function updateChart() {
//     const ctx = document.getElementById('budgetChart').getContext('2d')
//
//     // Extraer datos
//     const labels = budgets.map(b => b.name)
//     const spentData = budgets.map(b => b.budgetCount)
//     const backgroundColors = budgets.map(b => b.color)
//
//     // Si ya existe un gráfico, destruirlo para evitar duplicados
//     if (budgetChart) {
//         budgetChart.destroy()
//     }
//
//     // Crear nuevo gráfico
//     budgetChart = new Chart(ctx, {
//         type: 'doughnut',
//         data: {
//             labels: labels,
//             datasets: [{
//                 label: 'Gasto por categoría',
//                 data: spentData,
//                 backgroundColor: backgroundColors,
//                 borderWidth: 1
//             }]
//         },
//         options: {
//             responsive: true,
//             plugins: {
//                 legend: {
//                     position: 'bottom'
//                 },
//                 tooltip: {
//                     callbacks: {
//                         label: function(context) {
//                             const total = spentData.reduce((a, b) => a + b, 0)
//                             const value = context.parsed
//                             const percent = ((value / total) * 100).toFixed(1)
//                             return `${context.label}: ${value.toFixed(2)} € (${percent}%)`
//                         }
//                     }
//                 }
//             }
//         }
//     })
// }

// Cargar presupuestos desde el archivo JSON
async function loadBudgets() {
    try {
        const response = await fetch("/budget/getAll");
        if (!response.ok) {
            throw new Error("Error al cargar los presupuestos");
        }

        budgets = await response.json();
        displayBudgets();
    } catch (error) {
        console.error("Error:", error);
        showNotification("Error al cargar los presupuestos", "error");
    }
}

// Mostrar los presupuestos en la interfaz
// Mostrar los presupuestos en la interfaz
function displayBudgets() {
    let budgetContainer = document.querySelector(".budget-container");
    if (!budgetContainer) {
        budgetContainer = document.createElement("div");
        budgetContainer.className = "budget-container";
        document.querySelector(".container").appendChild(budgetContainer);
    }

    // Limpiar contenido anterior
    budgetContainer.innerHTML = "";

    budgets.forEach((budget) => {
        const percentage =
            budget.budget > 0
                ? ((budget.budgetCount / budget.budget) * 100).toFixed(1)
                : 0;

        const budgetElement = document.createElement("div");
        budgetElement.className = "budget-item";
        budgetElement.dataset.name = budget.name;

        budgetElement.innerHTML = `
            <div class="budget-header">
                <h3>${budget.name}</h3>
                <span class="budget-amount">${budget.budget.toFixed(2)} €</span>
            </div>
            <div class="budget-progress">
                <div class="progress-bar" style="width: ${percentage}%; background-color: ${
            budget.color
        }; max-width: 100%"></div>
            </div>
            <div class="budget-footer">
                <span class="budget-percentage">${percentage}% (${budget.budgetCount.toFixed(
            2
        )} €)</span>
                <div class="budget-actions">
                    <button class="edit-item-btn" data-name="${
            budget.name
        }"><i class="fas fa-edit"></i></button>
                    <button class="delete-item-btn" data-name="${
            budget.name
        }"><i class="fas fa-trash"></i></button>
                </div>
            </div>
        `;

        budgetContainer.appendChild(budgetElement);
    });

    // Añadir total
    const totalBudget = budgets.reduce((sum, b) => sum + b.budget, 0);
    const totalSpent = budgets.reduce((sum, b) => sum + b.budgetCount, 0);

    const totalElement = document.createElement("div");
    totalElement.className = "budget-total";
    totalElement.innerHTML = `
        <h3>Total</h3>
        <div class="total-details">
            <span class="total-budget">Presupuesto: ${totalBudget.toFixed(
        2
    )} €</span>
            <span class="total-spent">Gastado: ${totalSpent.toFixed(2)} €</span>
        </div>
    `;
    budgetContainer.appendChild(totalElement);

    // IMPORTANTE: Reasignar eventos DESPUÉS de que todo esté en el DOM
    setTimeout(() => {
        document.querySelectorAll(".edit-item-btn").forEach((btn) => {
            btn.removeEventListener("click", null); // Eliminar eventos anteriores
            btn.addEventListener("click", function(e) {
                e.preventDefault();
                e.stopPropagation();
                const name = this.getAttribute("data-name");
                openEditModal(name);
            });
        });

        document.querySelectorAll(".delete-item-btn").forEach((btn) => {
            btn.addEventListener("click", (e) => {
                e.preventDefault();
                deleteBudget(btn.dataset.name);
            });
        });
    }, 0);
}
// Añadir total

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
    };

    // Convertir el nombre a minúsculas y sin acentos para la comparación
    const normalizedName = name
        .toLowerCase()
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "");

    // Buscar si hay un color predefinido para esta categoría
    for (const [category, color] of Object.entries(categoryColors)) {
        if (normalizedName.includes(category)) {
            return color;
        }
    }

    // Si no hay un color predefinido, generar uno basado en el nombre
    let hash = 0;
    for (let i = 0; i < name.length; i++) {
        hash = name.charCodeAt(i) + ((hash << 5) - hash);
    }

    let color = "#";
    for (let i = 0; i < 3; i++) {
        const value = (hash >> (i * 8)) & 0xff;
        color += ("00" + value.toString(16)).slice(-2);
    }

    return color;
}

// Abrir modal para añadir presupuesto
function openAddModal() {
    addModal.style.display = "block";
    document.getElementById("amount").value = "";

    // Actualizar el selector de categorías
    const categorySelect = document.getElementById("category");
    categorySelect.innerHTML = ""; // Limpiar opciones existentes

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
    ];

    // Añadir categorías predefinidas
    categoriasPredefinidas.forEach((categoria) => {
        const option = document.createElement("option");
        option.value = categoria;
        option.textContent = categoria;
        categorySelect.appendChild(option);
    });

    categorySelect.selectedIndex = 0;
}

// Abrir modal para editar presupuesto
function openEditModal(name) {
    const budget = budgets.find((b) => b.name === name);
    if (!budget) {
        console.error("Presupuesto no encontrado:", name);
        return;
    }

    // Establecer el ID del presupuesto actual
    currentBudgetId = name;

    // Llenar el formulario con los valores actuales
    document.getElementById("edit-amount").value = budget.budget;
    const editCategorySelect = document.getElementById("edit-category");

    // Establecer el valor de la categoría
    Array.from(editCategorySelect.options).forEach(option => {
        if (option.text.toLowerCase() === name.toLowerCase()) {
            option.selected = true;
        }
    });

    // Mostrar el modal
    document.getElementById("edit-modal").style.display = "block";
}
// Cerrar modales
function closeModals() {
    addModal.style.display = "none";
    editModal.style.display = "none";
    currentBudgetId = null;
}

// Añadir nuevo presupuesto
async function addBudget(event) {
    event.preventDefault();

    const categorySelect = document.getElementById("category");
    const categoryText = categorySelect.options[categorySelect.selectedIndex].text;
    const amount = Number.parseFloat(document.getElementById("amount").value);

    // Verificar si ya existe un presupuesto con este nombre
    const existingBudget = budgets.find((b) => b.name === categoryText);
    if (existingBudget) {
        // Preguntar al usuario si desea modificar el presupuesto existente
        if (confirm(`Ya existe un presupuesto para "${categoryText}" con un valor de ${existingBudget.budget.toFixed(2)} €. ¿Desea modificarlo?`)) {
            // Actualizar directamente el presupuesto existente
            try {
                await editBudget(categoryText, amount);
                closeModals();
                return;
            } catch (error) {
                console.error("Error al actualizar el presupuesto:", error);
                showNotification("Error al actualizar el presupuesto", "error");
                return;
            }
        } else {
            // Si el usuario cancela, no hacer nada
            return;
        }
    }

    // Si no existe, crear nuevo presupuesto
    const color = getRandomColor(categoryText);
    const newBudget = {
        name: categoryText,
        budget: amount,
        budgetCount: 0,
        color: color,
    };

    try {
        const response = await fetch("/budget/new", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(newBudget),
        });

        if (response.ok) {
            budgets.push(newBudget);
            displayBudgets();
            closeModals();
            showNotification("Presupuesto añadido correctamente", "success");
        } else {
            const text = await response.text();
            console.warn("Error en la respuesta:", text);
            showNotification("Error al crear el presupuesto", "error");
        }
    } catch (error) {
        console.error("Error:", error);
        showNotification("Error al crear el presupuesto", "error");
    }
}

// Actualizar presupuesto existente
// Actualizar presupuesto existente
async function updateBudget(event) {
    event.preventDefault();

    if (!currentBudgetId) return;

    const amount = Number.parseFloat(
        document.getElementById("edit-amount").value
    );

    // Encontrar el presupuesto a actualizar
    const index = budgets.findIndex((b) => b.name === currentBudgetId);
    if (index !== -1) {
        // Actualizar localmente primero
        budgets[index].budget = amount;

        // Llamar a la API y esperar la respuesta
        await editBudget(budgets[index].name, amount);
    }

    closeModals();
    // La notificación ya se muestra en editBudget(), así que no la duplicamos aquí
}
// Eliminar presupuesto
async function deleteBudget(name) {
    if (!confirm("¿Estás seguro de que quieres eliminar este presupuesto?")) {
        return;
    }

    try {
        const response = await fetch("/budget/delete", {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ name }),
        });

        const result = await response.json();

        if (response.ok) {
            // Filtrar el presupuesto a eliminar si la respuesta es exitosa
            budgets = budgets.filter((b) => b.name !== name);
            displayBudgets();
            showNotification("Presupuesto eliminado correctamente", "success");
        } else {
            showNotification(`Error: ${result.message}`, "error");
        }
    } catch (error) {
        console.error("Error al eliminar el presupuesto:", error);
        showNotification("Hubo un problema al eliminar el presupuesto", "error");
    }
}
async function editBudget(name, newBudgetAmount) {
    try {
        const response = await fetch("/budget/edit", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                name: name,
                budget: newBudgetAmount,
            }),
        });

        // Leer el texto de la respuesta (puede estar vacío)
        const text = await response.text();
        let result = {};

        // Intentar parsear como JSON si hay algo en la respuesta
        if (text) {
            try {
                result = JSON.parse(text);
            } catch (e) {
                console.warn("Respuesta no era JSON válido:", text);
            }
        }

        if (response.ok) {
            // Actualiza el presupuesto en la lista local
            budgets = budgets.map((b) =>
                b.name === name ? { ...b, budget: newBudgetAmount } : b
            );
            displayBudgets(); // Solo esta línea, sin updateChart()

            showNotification("Presupuesto actualizado correctamente", "success");
        } else {
            showNotification(
                `Error: ${result.message || "Error desconocido"}`,
                "error"
            );
        }
    } catch (error) {
        console.error("Error al actualizar el presupuesto:", error);
        showNotification("Hubo un problema al actualizar el presupuesto", "error");
    }
}

// Mostrar notificación
function showNotification(message, type = "info") {
    // Crear elemento de notificación si no existe
    let notification = document.querySelector(".notification");
    if (!notification) {
        notification = document.createElement("div");
        notification.className = "notification";
        document.body.appendChild(notification);
    }

    // Establecer clase según el tipo
    notification.className = `notification ${type}`;
    notification.textContent = message;
    notification.style.display = "block";

    // Ocultar después de 3 segundos
    setTimeout(() => {
        notification.style.display = "none";
    }, 3000);
}

// Event Listeners
document.addEventListener("DOMContentLoaded", async () => {
    setupCurrentDate();
    await loadBudgets(); // Esperar a que los presupuestos se carguen

    addBudgetBtn.addEventListener("click", openAddModal);

    closeBtns.forEach((btn) => {
        btn.addEventListener("click", closeModals);
    });

    addBudgetForm.addEventListener("submit", addBudget);
    editBudgetForm.addEventListener("submit", updateBudget);

    // Cerrar modal al hacer clic fuera
    window.addEventListener("click", (event) => {
        if (event.target === addModal) {
            closeModals();
        }
        if (event.target === editModal) {
            closeModals();
        }
    });
});
