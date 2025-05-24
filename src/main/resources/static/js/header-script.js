document.addEventListener('DOMContentLoaded', function() {
    const currentMonthElement = document.getElementById('current-month');
    if (currentMonthElement) {
        const now = new Date();
        const options = { month: 'long', year: 'numeric' };
        currentMonthElement.textContent = now.toLocaleDateString('es-ES', options).replace(/^\w/, c => c.toUpperCase());
    }
});