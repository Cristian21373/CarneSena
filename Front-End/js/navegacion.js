document.querySelectorAll('a.no-historial').forEach(link => {
    link.addEventListener('click', function(e) {
        e.preventDefault(); // Evita que el navegador siga el link
        window.location.replace(this.href); // Reemplaza sin guardar en historial
    });
});