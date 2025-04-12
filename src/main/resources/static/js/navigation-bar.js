document.addEventListener('DOMContentLoaded', function() {
    // Obtener la ruta actual
    const currentPath = window.location.pathname;

    // Determinar qué botón debe estar activo basado en la ruta
    if (currentPath.includes('dashboard.html')) {
        document.getElementById('nav-presupuestos').classList.add('active');
    } else if (currentPath.includes('monthly-overview.html')) {
        document.getElementById('nav-resumen').classList.add('active');
    } else if (currentPath.includes('image-upload-ocr.html')) {
        document.getElementById('nav-anadir').classList.add('active');
    } else if (currentPath.includes('settings.html')) {
        document.getElementById('nav-ajustes').classList.add('active');
    }
});

