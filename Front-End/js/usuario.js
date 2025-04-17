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


//Esta funcion llenara el modal con los datos del usuario seleccionado
function consultarUsuarioID(id_usuario) {
    cargarListaFicha();

    $.ajax({
        url: urlUsuario + id_usuario,
        type: 'GET',
        success: function (usuario) {
            $('#inputIdUsuario').val(usuario.id_usuario);
            $('#inputNIS').val(usuario.nis);
            $('#inputNombre').val(usuario.nombre);
            $('#inputApellidos').val(usuario.apellidos);
            $('#inputTipoSangre').val(usuario.tipo_sangre);
            $('#inputTipoDocumento').val(usuario.tipo_documento);
            $('#inputNumeroDocumento').val(usuario.numero_documento);
            $('#inputEstado').val(usuario.estado_user);
            $('#inputFicha').val(usuario.ficha.id_ficha); 

            if (usuario.foto) {
                $('#previewFoto').attr('src', usuario.foto).show();
            }

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
    const url = "http://localhost:8080/api/v1/usuario/editar/";

    var datos = {
        id_usuario: id_usuario,
        nis: $('#inputNIS').val(),
        nombre: $('#inputNombre').val(),
        apellidos: $('#inputApellidos').val(),
        tipo_sangre: $('#inputTipoSangre').val(),
        tipo_documento: $('#inputTipoDocumento').val(),
        numero_documento: $('#inputNumeroDocumento').val(),
        ficha: {
            id_ficha: $('#inputFicha').val()
        },
        estado_user: $('#inputEstado').val(),
        foto: $('#inputFoto').val()
    };

    $.ajax({
        url: url + id_usuario + "?ADMIN=true",
        type: "PUT",
        data: JSON.stringify(datos),
        contentType: "application/json",
        success: function (result) {
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