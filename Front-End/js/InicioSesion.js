
$(document).ready(function () {
    $("form").on("submit", function (e) {
        e.preventDefault(); // Evita que el formulario se recargue
        
        let username = $("#username").val().trim();
        let password = $("#password").val().trim();

        if (username === "" || password === "") {
            Swal.fire("Error", "Todos los campos son obligatorios", "error");
            return;
        }

        $.ajax({
            url: urlLogin, 
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({ username: username, password: password }),
            success: function (response) {
                console.log("Token recibido:", response.token); // DEBUG: Verificar el token recibido
                localStorage.setItem("token", response.token); 

                // Decodificar el token para obtener el rol
                let tokenData = parseJwt(response.token);
                console.log("Contenido del token:", tokenData); // DEBUG: Verificar qué contiene el token
                
                if (tokenData.role === "ADMIN" || tokenData.role === "SUPERADMIN") {
                    window.location.href = "/Front-End/html/roles/administrador/panelCarnet.html";
                } else {
                    Swal.fire("Acceso denegado", "No tienes permisos de administrador", "error");
                }
            },
            error: function (xhr) {
                let errorMessage = "Error en la autenticación";

                if (xhr.status === 401) {
                    errorMessage = "Usuario o contraseña incorrectos. Inténtalo nuevamente.";
                } else if (xhr.status === 403) {
                    errorMessage = "No tienes permisos para acceder.";
                } else if (xhr.status === 500) {
                    errorMessage = "Error del servidor. Intenta más tarde.";
                }

                Swal.fire("Error", errorMessage, "error");
            }
        });
    });
});

// Función para decodificar el token JWT y obtener el rol
function parseJwt(token) {
    let base64Url = token.split(".")[1];
    let base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    let jsonPayload = decodeURIComponent(
        atob(base64)
            .split("")
            .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
            .join("")
    );
    return JSON.parse(jsonPayload);
}
