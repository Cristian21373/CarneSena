function listaUsuario() {
    var capturarFiltro = document.querySelector(".search-input").value;
    var url = urlUsuario; // esta es una copia local

    if (capturarFiltro !== "") {
        url += "busquedafiltro/" + capturarFiltro;
    }

    $.ajax({
        url: url,
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
                    if (!usuario.foto.startsWith("http")) {
                        img.src = "http://localhost:8080/" + usuario.foto;
                    } else {
                        img.src = usuario.foto;
                    }
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
                var celdaOpciones = document.createElement("td");

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


                var botonEliminar = document.createElement("button");
                botonEliminar.className = "btn-icon eliminar-btn";
                botonEliminar.innerHTML = '<i class="fas fa-trash-alt"></i>';
                botonEliminar.onclick = function () {
                    Swal.fire({
                        title: '¿Estás seguro?',
                        text: "¡No podrás revertir esto!",
                        icon: 'warning',
                        showCancelButton: true,
                        confirmButtonColor: '#3085d6',
                        cancelButtonColor: '#d33',
                        confirmButtonText: 'Sí, eliminarlo'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            eliminarUsuario(usuario.id_usuario);
                        }
                    });
                };

                // Botón de Actualizar con ícono
                var botonActualizarUsuario = document.createElement("button");
                botonActualizarUsuario.className = "btn-icon editar-btn";
                botonActualizarUsuario.innerHTML = '<i class="fas fa-edit"></i>';
                botonActualizarUsuario.onclick = function () {
                    $('#modal-usuario').modal('show');
                    consultarUsuarioID(usuario.id_usuario);
                };

                // Contenedor para los botones
                var divBotones = document.createElement("div");
                divBotones.className = "btn-group";
                divBotones.appendChild(botonEliminar);
                divBotones.appendChild(botonActualizarUsuario);


                // Añadir el contenedor a la celda de acciones
                celdaOpciones.appendChild(divBotones);

                trRegistro.appendChild(celdaFoto);
                trRegistro.appendChild(celdaNIS);
                trRegistro.appendChild(celdaNombres);
                trRegistro.appendChild(celdaApellidos);
                trRegistro.appendChild(celdaTipo_documento);
                trRegistro.appendChild(celdaNumero_documento);
                trRegistro.appendChild(celdaNumero_ficha);
                trRegistro.appendChild(celdaNombre_programa);
                trRegistro.appendChild(celdaEstado);
                trRegistro.appendChild(celdaOpciones);

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

//Eliminar usuario
function eliminarUsuario(id_usuario) {
    $.ajax({
        url: urlEliminarUser + id_usuario,
        type: "DELETE",
        success: function (result) {
            Swal.fire(
                '¡Eliminado!',
                'El usuario ha sido eliminado.',
                'success'
            );
            listaUsuario();
        }
    });
}


document.addEventListener('DOMContentLoaded', function () {
    const modalAgregarNis = document.getElementById('modalAgregarNis');
    const btnAbrirModalNis = document.getElementById('btnAbrirModalNis');
    const cerrarModalNis = document.getElementById('cerrarModalNis');
    const opcionManual = document.getElementById('opcionManual');
    const modalManualNis = document.getElementById('modalManualNis');

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



//Esta funcion llenara el modal con los datos del usuario seleccionado
function consultarUsuarioID(id_usuario) {
    cargarListaFicha();

    $.ajax({
        url: urlUsuario + id_usuario,
        type: 'GET',
        success: function (usuario) {
            $('#inputIdUsuario').val(usuario.id_usuario);
            if (usuario.foto) {
                let rutaFoto = usuario.foto.startsWith("http") ? usuario.foto : `http://localhost:8080/${usuario.foto}`;
                $('#previewFoto').attr('src', rutaFoto).show();
            }

            $('#inputNIS').val(usuario.nis);
            $('#inputNombre').val(usuario.nombre);
            $('#inputApellidos').val(usuario.apellidos);
            $('#inputTipoSangre').val(usuario.tipo_sangre);
            $('#inputTipoDocumento').val(usuario.tipo_documento);
            $('#inputNumeroDocumento').val(usuario.numero_documento);
            $('#inputEstado').val(usuario.estado_user);
            if (usuario.estado_user === 'creado') {
                $('#inputEstado option[value="creado"]').show();
            }
            $('#inputFicha').val(usuario.ficha.id_ficha);



            $('#modal-usuario').modal('show');
        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'No se pudo obtener la información del usuario',
                confirmButtonColor: '#ef4444'
            });
        }
    });
}


function actualizarUsuario(id_usuario) {
    let formData = new FormData();

    // Añadir imagen solo si el usuario seleccionó una nueva
    const archivoFoto = $('#inputFoto')[0].files[0];
    if (archivoFoto) {
        formData.append("file", archivoFoto);
    }

    // Agregar los demás campos
    formData.append("id_usuario", id_usuario);
    formData.append("nis", $('#inputNIS').val());
    formData.append("nombre", $('#inputNombre').val());
    formData.append("apellidos", $('#inputApellidos').val());
    formData.append("tipo_sangre", $('#inputTipoSangre').val());
    formData.append("tipo_documento", $('#inputTipoDocumento').val());
    formData.append("numero_documento", $('#inputNumeroDocumento').val());
    formData.append("estado_user", $('#inputEstado').val());
    formData.append("id_ficha", $('#inputFicha').val());


    $.ajax({
        url: urlEditar_usuario + id_usuario + "?ADMIN=true",
        type: "PUT", 
        data: formData,
        processData: false, 
        contentType: false, 
        success: function (response) {
            $('#modal-usuario').modal('hide');
            Swal.fire('¡Actualizado!', 'Los datos del usuario han sido actualizados.', 'success');
            listaUsuario();
        },
        error: function (xhr, status, error) {
            Swal.fire('Error', 'No se pudo actualizar el usuario. ' + error, 'error');
        }
    });
}




$(document).ready(function () {
    $('#formEditarUsuario').submit(function (e) {
        e.preventDefault();
        const id_usuario = $('#inputIdUsuario').val();
        actualizarUsuario(id_usuario);
    });
});


$('#inputFoto').on('change', function () {
    const file = this.files[0];
    if (file) {
        const reader = new FileReader();

        reader.onload = function (e) {
            $('#previewFoto').attr('src', e.target.result).show();
        }

        reader.readAsDataURL(file);
    }
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
            url: urlCargaExcel,
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


document.addEventListener("DOMContentLoaded", function () {
    cargarListaFicha();
});

// Función para cargar la lista de usuarios
function cargarListaFicha() {
    var ficha = document.getElementById("inputFicha");

    if (ficha) {
        ficha.innerHTML = "";

        $.ajax({
            url: urlFicha,
            type: "GET",
            success: function (result) {
                for (var i = 0; i < result.length; i++) {
                    var option = document.createElement("option");
                    option.value = result[i].id_ficha;
                    option.text = result[i].nombre_programa + " - " + result[i].codigo_ficha;
                    ficha.appendChild(option);
                }
            },
            error: function (error) {
                console.error("Error al obtener la lista de la ficha: " + error);
            }
        });
    } else {
        console.error("Elemento con ID 'ficha' no encontrado.");
    }
}