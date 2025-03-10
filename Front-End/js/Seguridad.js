/*Este codigo funiona para cuando no se a iniciado sesion y se pone la url de algun panel 
pues basicamnete lo devuelve al login*/

document.addEventListener("DOMContentLoaded", function () {
    let token = localStorage.getItem("token");

    if (!token) {
        window.location.href = "/Front-End/html/login/login.html";
        return;
    }

    let tokenData = parseJwt(token);
    if (!tokenData || tokenData.role !== "ADMIN") {
        localStorage.removeItem("token"); // Borrar token inválido
        window.location.href = "/Front-End/html/login/login.html";
    }
});

function parseJwt(token) {
    try {
        let base64Url = token.split(".")[1];
        let base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
        let jsonPayload = decodeURIComponent(atob(base64)
            .split("")
            .map(c => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
            .join(""));
        return JSON.parse(jsonPayload);
    } catch (e) {
        return null;
    }
}




//Este codigo funciona para cuando ahi inactividad 
// automaticamente lo saque o si el admin quiere continuar en sesion pues ahi estara el boton para ello

$(document).ready(function () {
    let inactivityTime = 20 * 1000; // 20 segundos de inactividad antes de la advertencia
    let warningTime = 30 * 1000; // 30 segundos para decidir antes de cerrar sesión automáticamente
    let timeout, warningTimeout;

    function resetTimer() {
        clearTimeout(timeout);
        clearTimeout(warningTimeout);
        timeout = setTimeout(showWarning, inactivityTime); // Mostrar advertencia después de 20s
    }

    function showWarning() {
        let timerInterval;
        Swal.fire({
            title: "⚠ Tu sesión está a punto de expirar",
            text: "¿Deseas continuar o cerrar sesión?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Cerrar sesión",
            cancelButtonText: "Seguir aquí",
            timer: warningTime,
            timerProgressBar: true,
            allowOutsideClick: false,
            willOpen: () => {
                const content = Swal.getHtmlContainer();
                if (content) {
                    timerInterval = setInterval(() => {
                        const timer = Swal.getTimerLeft();
                        content.innerHTML = `Tu sesión se cerrará en <b>${Math.round(timer / 1000)}</b> segundos.`;
                    }, 1000);
                }
            },
            willClose: () => {
                clearInterval(timerInterval);
            }
        }).then((result) => {
            if (result.isConfirmed) {
                logout(); // Si presiona "Cerrar sesión", cerramos sesión de inmediato
            } else {
                resetTimer(); // Si presiona "Seguir aquí", reiniciamos el temporizador
            }
        });

        // Si no elige ninguna opción en 30s, cerrar sesión automáticamente
        warningTimeout = setTimeout(logout, warningTime);
    }

    function logout() {
        localStorage.removeItem("token"); // Eliminamos el token inmediatamente

        Swal.fire({
            title: "⏳ Sesión Expirada",
            text: "Has estado inactivo por mucho tiempo. Inicia sesión nuevamente.",
            icon: "error",
            showConfirmButton: false,
            timer: 2000
        });

        setTimeout(() => {
            window.location.replace("/Front-End/html/login/login.html"); // Redirige al login
        }, 2000); // Redirige en 2 segundos
    }

    // Eventos para detectar actividad (mueve el mouse, teclea, clic)
    $(document).on("mousemove keydown click", resetTimer);

    // Verifica el token al cargar la página
    function checkAuth() {
        let token = localStorage.getItem("token");
        if (!token) {
            logout(); // Si no hay token, salir de inmediato
        }
    }

    checkAuth();
    resetTimer();
});

window.addEventListener("pageshow", function (event) {
    if (event.persisted) {
        location.reload();
    }
});



$(document).ready(function () {
    $(".menu-list li a:contains('Cerrar sesión')").click(function (e) {
        e.preventDefault(); // Evita la recarga de la página

        Swal.fire({
            title: "¿Estás seguro?",
            text: "Se cerrará tu sesión y deberás volver a iniciar sesión.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Sí, cerrar sesión",
            cancelButtonText: "Cancelar",
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                localStorage.removeItem("token"); // Eliminar el token
                window.location.href = "/Front-End/html/login/login.html"; // Redirigir al login
            }
        });
    });
});
