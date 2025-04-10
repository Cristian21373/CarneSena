function listaUsuario() {
    var capturarFiltro = document.querySelector(".search-input").value;
    if (capturarFiltro !== "") {
        urlUsuario += "busquedafiltro/" + capturarFiltro;
    }

    $.ajax({
        url: urlUsuario,
        type: "GET",
        success: function (result) {
            var cuerpoTabla = document.getElementById("cuerpoTabla");
            cuerpoTabla.innerHTML = "";

            result.forEach(function (usuario) {
                var trRegistro = document.createElement("tr");

                // Foto
                var celdaFoto = document.createElement("td");
                var img = document.createElement("img");
                if (usuario.foto) {
                    img.src = usuario.foto;
                } else {
                    img.src = "/Front-End/img/default.png";
                }
                img.alt = "Foto";
                img.style.width = "24px";
                img.style.height = "24px";
                img.style.objectFit = "cover";
                celdaFoto.appendChild(img);

                var celdaNIS = document.createElement("td");
                var celdaNombres = document.createElement("td");
                var celdaApellidos = document.createElement("td");
                var celdaTipo_documento = document.createElement("td");
                var celdaNumero_documento = document.createElement("td");
                var celdaNumero_ficha = document.createElement("td");
                var celdaNombre_programa = document.createElement("td");
                var celdaEstado = document.createElement("td");

                celdaNIS.innerText = usuario.nis;
                celdaNombres.innerText = usuario.nombre;
                celdaApellidos.innerText = usuario.apellidos;
                celdaTipo_documento.innerText = usuario.tipo_documento;
                celdaNumero_documento.innerText = usuario.numero_documento;
                // Verifica si ficha existe
                if (usuario.ficha) {
                    celdaNumero_ficha.innerText = usuario.ficha.codigo_ficha;
                    celdaNombre_programa.innerText = usuario.ficha.nombre_programa;
                } else {
                    celdaNumero_ficha.innerText = "N/A";
                    celdaNombre_programa.innerText = "N/A";
                }
                celdaEstado.innerText = usuario.estado_user;




                trRegistro.appendChild(celdaFoto);
                trRegistro.appendChild(celdaNIS);
                trRegistro.appendChild(celdaNombres);
                trRegistro.appendChild(celdaApellidos);
                trRegistro.appendChild(celdaTipo_documento);
                trRegistro.appendChild(celdaNumero_documento);
                trRegistro.appendChild(celdaNumero_ficha);
                trRegistro.appendChild(celdaNombre_programa);
                trRegistro.appendChild(celdaEstado);

                cuerpoTabla.appendChild(trRegistro);
            });
        },
        error: function (error) {
            console.error("Error en la petición:", error);
            alert("Error en la petición: " + error.statusText);
        }
    });
}

$(document).ready(function () {
    listaUsuario();
});

document.addEventListener('DOMContentLoaded', function () {
    const modalAgregarNis = document.getElementById('modalAgregarNis');
    const btnAbrirModalNis = document.getElementById('btnAbrirModalNis');
    const cerrarModalNis = document.getElementById('cerrarModalNis');
    const opcionManual = document.getElementById('opcionManual');
    const modalManualNis = document.getElementById('modalManualNis');
    const cerrarModalManual = document.getElementById('cerrarModalManual');

    // Abrir el primer modal
    btnAbrirModalNis.addEventListener('click', function () {
        modalAgregarNis.style.display = 'flex';
    });

    // Cerrar el modal de selección
    cerrarModalNis.addEventListener('click', function () {
        modalAgregarNis.style.display = 'none';
    });

    // Abrir el modal manual
    opcionManual.addEventListener('click', function () {
        modalAgregarNis.style.display = 'none';
        modalManualNis.style.display = 'flex';
    });

});

document.getElementById('btnVolver').addEventListener('click', () => {
    document.getElementById('modalManualNis').style.display = 'none';
    document.getElementById('modalAgregarNis').style.display = 'flex';
});


$(document).ready(function () {
    $('#registrarNisBtn').click(function () {
        const nis = $('#nisInput').val().trim();

        if (nis === '') {
            Swal.fire({
                icon: 'warning',
                title: 'Campo vacío',
                text: 'Por favor ingresa el NIS antes de registrar',
                confirmButtonColor: '#10b981'
            });
            return;
        }

        if (nis.length >= 10) {
            Swal.fire({
                icon: 'warning',
                title: 'NIS inválido',
                text: 'El NIS ingresado es demasiado largo. Verifica que tenga menos de 10 dígitos.',
                confirmButtonColor: '#10b981'
            });
            return;
        }

        $.ajax({
            url: urlNuevo_usuario,
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ nis: parseInt(nis) }),
            success: function (response) {
                Swal.fire({
                    icon: 'success',
                    title: '¡Éxito!',
                    text: response,
                    confirmButtonColor: '#10b981'
                }).then(() => {
                    $('#modalManualNis').hide();
                    $('#nisInput').val('');
                    listaUsuario(); 
                });
            },
            error: function (xhr) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: xhr.responseText,
                    confirmButtonColor: '#ef4444'
                });
            }
        });
    });
});






$(document).ready(function () {
    // Abrir modal Excel
    $('#abrirModalExcel').click(function () {
        $('#modalAgregarNis').hide();
        $('#modalExcel').css('display', 'flex');
    });

    // Volver del modal Excel
    $('#btnVolverExcel').click(function () {
        $('#modalExcel').hide();
        $('#modalAgregarNis').show();
    });

    // Subir archivo Excel
    $('#btnSubirExcel').click(function () {
        const archivo = $('#archivoExcel')[0].files[0];

        if (!archivo) {
            Swal.fire({
                icon: 'warning',
                title: 'Archivo no seleccionado',
                text: 'Por favor selecciona un archivo Excel para continuar.',
                confirmButtonColor: '#f59e0b'
            });
            return;
        }

        const formData = new FormData();
        formData.append('file', archivo);

        $.ajax({
            url: 'http://localhost:8080/api/v1/usuario/carga-masiva-nis',
            method: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                Swal.fire({
                    icon: 'success',
                    title: '¡Carga completa!',
                    html: `
                        <p><strong>NIS registrados:</strong> ${response.registrados}</p>
                        <p><strong>NIS duplicados:</strong> ${response.duplicados}</p>
                    `,
                    confirmButtonColor: '#10b981'
                }).then(() => {
                    $('#modalExcel').hide();
                    $('#archivoExcel').val('');
                    listaUsuario(); 
                });
            },
            error: function (xhr) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: xhr.responseText || 'Error al subir el archivo',
                    confirmButtonColor: '#ef4444'
                });
            }
        });
    });
});
