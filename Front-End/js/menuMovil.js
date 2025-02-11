document.addEventListener("DOMContentLoaded", function () {
    const hamburger = document.getElementById("hamburger");
    const mobileMenu = document.getElementById("mobile-menu");
    const closeBtn = document.getElementById("close-btn");

    // Mostrar menú al hacer clic en el ícono hamburguesa
    hamburger.addEventListener("click", function () {
        mobileMenu.style.right = "0";
    });

    // Cerrar menú al hacer clic en el botón de cerrar
    closeBtn.addEventListener("click", function () {
        mobileMenu.style.right = "-100%";
    });

    // Cerrar menú si se hace clic fuera de él
    document.addEventListener("click", function (event) {
        if (!mobileMenu.contains(event.target) && !hamburger.contains(event.target)) {
            mobileMenu.style.right = "-100%";
        }
    });
});