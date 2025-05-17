document.addEventListener('DOMContentLoaded', function() {
    // Activar el nav-link según la ruta
    const currentPath = window.location.pathname;
    const navPresupuestos = document.getElementById('nav-presupuestos');
    const navResumen = document.getElementById('nav-resumen');
    const navAnadir = document.getElementById('nav-anadir');
    const navProfile = document.getElementById('nav-profile');
    const navMarket = document.getElementById('nav-market');

    if (currentPath.includes('dashboard')) {
        navPresupuestos?.classList.add('active');
    } else if (currentPath.includes('monthly-overview')) {
        navResumen?.classList.add('active');
    } else if (currentPath.includes('image-upload-ocr')) {
        navAnadir?.classList.add('active');
    } else if (currentPath.includes('profile')) {
        navProfile?.classList.add('active');
    } else if (currentPath.includes('market')) {
        navMarket?.classList.add('active');
    }
});

