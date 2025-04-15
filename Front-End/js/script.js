allowOutsideClick: false;
allowEscapeKey: false;
allowEnterKey: false;



document.addEventListener("DOMContentLoaded", function () {
    if (!sessionStorage.getItem("alertaMostrada")) {
        Swal.fire({
            title: "Aviso Importante",
            text: "Este sistema está habilitado únicamente para la Regional Huila – Neiva. Usuarios de otras regiones o centros de formación no podrán utilizar esta plataforma.",
            icon: "info",
            confirmButtonText: "Entendido",
            allowOutsideClick: false,
            allowEscapeKey: false,
            allowEnterKey: false
        });
        sessionStorage.setItem("alertaMostrada", "true");
    }
});