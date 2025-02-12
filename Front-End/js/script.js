document.addEventListener("DOMContentLoaded", function () {
    if (!localStorage.getItem("alertaMostrada")) {
        Swal.fire({
            title: "Aviso Importante",
            text: "Este sistema está habilitado únicamente para la Regional Huila – Neiva. Usuarios de otras regiones o centros de formación no podrán utilizar esta plataforma.",
            icon: "info",
            confirmButtonText: "Entendido"
        });
        localStorage.setItem("alertaMostrada", "true");
    }
});