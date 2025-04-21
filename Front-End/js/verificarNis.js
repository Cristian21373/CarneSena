window.onload = function () {
    if (!localStorage.getItem('alertShown')) {
        Swal.fire({
            title: '¡Bienvenido!',
            text: 'Si el NIS que ingrese no está registrado en esta plataforma, no podrá descargar su carné. Esto no significa que no esté registrado en Sofía Plus. Un administrador deberá registrar su NIS en esta plataforma para que pueda proceder con la descarga.',
            icon: 'info',
            confirmButtonText: 'Entendido'
        });

        localStorage.setItem('alertShown', 'true');
    }
}

document.addEventListener("DOMContentLoaded", function () {
    cargarListaFicha();
});

function cargarListaFicha() {
    var ficha = document.getElementById("ficha");

    if (ficha) {
        ficha.innerHTML = "<option value=''>Seleccione una ficha</option>";

        $.ajax({
            url: urlFicha,
            type: "GET",
            success: function (result) {
                for (var i = 0; i < result.length; i++) {
                    if (result[i].estado_ficha === "habilitado") { 
                        var option = document.createElement("option");
                        option.value = result[i].id_ficha;
                        option.text = result[i].nombre_programa + " - " + result[i].codigo_ficha;
                        ficha.appendChild(option);
                    }
                }
            },
            error: function (error) {
                console.error("Error al obtener la lista de la ficha: " + error);
            }
        });
    }
}


function verificarNIS() {
    let nis = document.getElementById("nis").value;
    if (!nis) {
        Swal.fire("Error", "Ingrese un NIS", "error");
        return;
    }
    
    $.ajax({
        url: urlVerificar + `${nis}`,
        method: "GET",
        success: function (data) {
            // Validar el estado del NIS
            const estado = data.estado?.toLowerCase(); // o data.estado_user si lo cambias

            if (estado === 'creado' || estado === 'actualizacion') {
                Swal.fire('Listo', 'NIS disponible para registro.', 'success');
                $(".verification-container").hide();
                $("#formularioCampos").slideDown();
            } else if (estado === 'completo' || estado === 'descargado') {
                Swal.fire({
                    icon: 'info',
                    title: 'Registro ya completado',
                    text: 'Este NIS ya completó el registro y no se puede modificar.'
                });
                $("#formularioCampos").hide();
            } else {
                Swal.fire('Error', 'Estado del NIS no reconocido.', 'error');
                $("#formularioCampos").hide();
                $("#btnDescargarCarnet").show(); // mostrar el botón

            }
        },
        error: function (xhr) {
            try {
                const data = JSON.parse(xhr.responseText);
                if (data.registrado === false) {
                    Swal.fire({
                        icon: 'error',
                        title: 'NIS no registrado',
                        text: data.mensaje || 'Este NIS no está registrado.'
                    });
                } else {
                    Swal.fire('Error', 'No se pudo verificar el NIS.', 'error');
                }
            } catch (e) {
                Swal.fire('Error', 'Ocurrió un error inesperado.', 'error');
            }

            $("#formularioCampos").hide();
        }
    });
}

document.getElementById("foto").addEventListener("change", function (event) {
    let file = event.target.files[0];

    if (file) {
        const allowedExtensions = ["png", "jpg", "jpeg"]; // Incluí jpeg por si acaso
        const fileExtension = file.name.split(".").pop().toLowerCase();
        if (!allowedExtensions.includes(fileExtension)) {
            Swal.fire("Error", "Solo se permiten archivos con formato PNG y JPG.", "error");
            this.value = ""; // Limpiar el input file
            document.getElementById("preview").src = "#";
            document.getElementById("preview").style.display = "none";
            return;
        }

        const maxSize = 4 * 1024 * 1024;
        if (file.size > maxSize) {
            Swal.fire("Error", "La foto no debe exceder los 4 MB.", "error");
            this.value = ""; // Limpiar el input file
            document.getElementById("preview").src = "#";
            document.getElementById("preview").style.display = "none";
            return;
        }

        if (file.name.includes(" ")) {
            Swal.fire("Advertencia", "El nombre del archivo de la foto no debe contener espacios. Por favor, renombra el archivo.", "warning");
            return;
        }

        let reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById("preview").src = e.target.result;
            document.getElementById("preview").style.display = "block";
        };
        reader.readAsDataURL(file);
    } else {
        document.getElementById("preview").src = "#";
        document.getElementById("preview").style.display = "none";
    }
});




function registrarUsuario() {
    const foto = document.getElementById("foto").files[0];
    const nombre = document.getElementById("nombre").value.trim();
    const apellidos = document.getElementById("apellidos").value.trim();
    const tipoDocumento = document.getElementById("tipo_documento").value;
    const numeroDocumento = document.getElementById("numero_documento").value.trim();
    const tipoSangre = document.getElementById("tipo_sangre").value;
    const ficha = document.getElementById("ficha").value;

    if (!foto || !nombre || !apellidos || !tipoDocumento || !numeroDocumento || !tipoSangre || !ficha) {
        Swal.fire("Campos incompletos", "Por favor, completa todos los campos antes de registrar.", "warning");
        return;
    }

    let formData = new FormData();

    formData.append("file", document.getElementById("foto").files[0]);
    fetch(urlFoto, {
        method: "POST",
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            let usuario = {
                foto: data.url,
                nombre: document.getElementById("nombre").value,
                apellidos: document.getElementById("apellidos").value,
                tipo_documento: document.getElementById("tipo_documento").value,
                tipo_sangre: document.getElementById("tipo_sangre").value,
                numero_documento: document.getElementById("numero_documento").value,
                ficha: { id_ficha: document.getElementById("ficha").value },
                estado_user: "completo"
            };
            return fetch(urlActualizar + document.getElementById("nis").value, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(usuario)
            });
        })
        .then(response => response.text())
        .then(message => {
            Swal.fire("Éxito", message, "success");

            descargarCarnet($("#nis").val()); // Aquí llamas automáticamente

        })
        .catch(error => {
            Swal.fire("Error", "No se pudo registrar el usuario", "error");
        });
}

function descargarCarnet() {
    let nis = document.getElementById("nis").value;

    if (!nis) {
        Swal.fire("Error", "No se encontró el NIS para descargar el carné.", "error");
        return;
    }


    window.open(urlCarnet + `${nis}`);
}