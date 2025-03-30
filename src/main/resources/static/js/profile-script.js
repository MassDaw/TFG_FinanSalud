// Script para la página de perfil

document.addEventListener("DOMContentLoaded", () => {
    // Botón de editar perfil
    const editProfileBtn = document.querySelector(".edit-profile-btn")

    editProfileBtn.addEventListener("click", () => {
        // Aquí se podría abrir un modal para editar el perfil
        alert("Función de editar perfil no implementada aún")
    })

    // Botón de logout
    const logoutBtn = document.querySelector(".menu-item.logout")

    logoutBtn.addEventListener("click", (e) => {
        e.preventDefault()
        if (confirm("¿Estás seguro de que quieres cerrar sesión?")) {
            // Aquí se implementaría la lógica de logout
            alert("Sesión cerrada")
            // Redirigir a la página de login
            // window.location.href = 'login.html';
        }
    })

    // Obtener fecha actual para mostrar el mes actual
    const currentDate = new Date()
    const options = { month: "long" }
    const currentMonth = currentDate.toLocaleDateString("es-ES", options)

    // Actualizar el texto "este mes" con el mes actual
    const statPeriod = document.querySelector(".stat-period")
    if (statPeriod) {
        statPeriod.textContent = `este ${currentMonth}`
    }
})

