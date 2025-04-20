function listaFicha() {
    var capturarFiltro = document.querySelector(".search-input").value;
    if (capturarFiltro !== "") {
        urlFicha += "busquedafiltro/" + capturarFiltro;
    }

    $.ajax({
        url: urlFicha,
        type: "GET",
        success: function (result) {
            var cuerpoTabla = document.getElementById("cuerpoTabla");
            cuerpoTabla.innerHTML = "";

            result.forEach(function (ficha) {
                var trRegistro = document.createElement("tr");


                var celdaNumero_ficha = document.createElement("td");
                var celdaNombre_programa = document.createElement("td");
                var celdaFecha_inicio = document.createElement("td");
                var celdaFecha_fin = document.createElement("td");
                var celdaEstado = document.createElement("td");
                var celdaOpciones = document.createElement("td");


                celdaNumero_ficha.innerText = ficha.codigo_ficha;
                celdaNombre_programa.innerText = ficha.nombre_programa;
                celdaFecha_inicio.innerText = ficha.fecha_inicio;
                celdaFecha_fin.innerText = ficha.fecha_fin;
                celdaEstado.innerText = ficha.estado_ficha;

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
                            eliminarFicha(ficha.id_ficha);
                        }
                    });
                };

                // Botón de Actualizar con ícono
                var botonActualizarFicha = document.createElement("button");
                botonActualizarFicha.className = "btn-icon editar-btn";
                botonActualizarFicha.innerHTML = '<i class="fas fa-edit"></i>';
                botonActualizarFicha.onclick = function () {
                    $('#modal-Ficha').modal('show');
                    consultarFichaID(ficha.id_ficha);
                };

                // Contenedor para los botones
                var divBotones = document.createElement("div");
                divBotones.className = "btn-group";
                divBotones.appendChild(botonEliminar);
                divBotones.appendChild(botonActualizarFicha);


                // Añadir el contenedor a la celda de acciones
                celdaOpciones.appendChild(divBotones);

                trRegistro.appendChild(celdaNumero_ficha);
                trRegistro.appendChild(celdaNombre_programa);
                trRegistro.appendChild(celdaFecha_inicio);
                trRegistro.appendChild(celdaFecha_fin);
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
    listaFicha();
});


function eliminarFicha(id_ficha) {
    $.ajax({
        url: urlEliminarFicha + id_ficha,
        type: "DELETE",
        success: function (result) {
            Swal.fire(
                '¡Eliminado!',
                'La ficha ha sido eliminada.',
                'success'
            );
            listaFicha();
        },
        error: function (xhr) {
            let mensajeError = xhr.responseText || (xhr.responseJSON && xhr.responseJSON.message) || "Error desconocido";

            if (xhr.status === 400 && mensajeError.includes("usuario(s) asociados")) {
                Swal.fire(
                    'Error',
                    mensajeError,
                    'error'
                );
            } else {
                Swal.fire(
                    'Error',
                    'Ocurrió un error inesperado al eliminar la ficha.',
                    'error'
                );
            }
        }
    });
}




document.addEventListener('DOMContentLoaded', function () {
    const modalAgregarFicha = document.getElementById('modalAgregarFicha');
    const btnAbrirModalFicha = document.querySelector('.btnAbrirModalFicha');
    const cerrarModalFicha = document.getElementById('cerrarModalFicha');

    btnAbrirModalFicha.addEventListener('click', function () {
        modalAgregarFicha.style.display = 'flex';
    });

    cerrarModalFicha.addEventListener('click', function () {
        modalAgregarFicha.style.display = 'none';
    });
});


function registrarFicha() {
    let codigo_ficha = document.getElementById("numeroInput").value;
    let nombre_programa = document.getElementById("nombreInput").value;
    let fecha_inicio = document.getElementById("fechaInicioInput").value;
    let fecha_fin = document.getElementById("fechaFinInput").value;

    // Obtener fecha de hoy como yyyy-MM-dd
    let hoy = new Date();
    let yyyy = hoy.getFullYear();
    let mm = String(hoy.getMonth() + 1).padStart(2, '0');
    let dd = String(hoy.getDate()).padStart(2, '0');
    let fechaHoyStr = `${yyyy}-${mm}-${dd}`;

    if (fecha_inicio < fechaHoyStr) {
        Swal.fire({
            title: "Error",
            text: "La fecha de inicio no puede ser anterior a hoy.",
            icon: "error"
        });
        return;
    }

    if (fecha_fin < fechaHoyStr) {
        Swal.fire({
            title: "Error",
            text: "La fecha de fin no puede ser anterior a hoy.",
            icon: "error"
        });
        return;
    }

    if (fecha_fin < fecha_inicio) {
        Swal.fire({
            title: "Error",
            text: "La fecha de fin no puede ser anterior a la fecha de inicio.",
            icon: "error"
        });
        return;
    }


    let formData = {
        "codigo_ficha": codigo_ficha,
        "nombre_programa": nombre_programa,
        "fecha_inicio": fecha_inicio,
        "fecha_fin": fecha_fin,
        "estado_ficha": "habilitado"
    };

    $.ajax({
        url: urlFicha,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(formData),
        success: function (result) {
            Swal.fire({
                title: "¡Excelente!",
                text: "Se guardó correctamente",
                icon: "success"
            }).then(() => {
                limpiarFormulario();
                listaFicha();
                document.getElementById("modalAgregarFicha").style.display = "none";
            });
        },
        error: function (xhr) {
            let mensaje = xhr.responseText || "Error al guardar la ficha";
            Swal.fire({
                title: "Error",
                text: mensaje,
                icon: "error"
            });
        }
    });
}




function limpiarFormulario() {
    document.getElementById("numeroInput").value = "";
    document.getElementById("nombreInput").value = "";
    document.getElementById("fechaInicioInput").value = "";
    document.getElementById("fechaFinInput").value = "";
}


//Esta funcion llenara el modal con los datos del ficha seleccionado
function consultarFichaID(id_ficha) {
    $.ajax({
        url: urlFicha + id_ficha,
        type: "GET",
        success: function (data) {
            $("#inputIdFicha").val(data.id_ficha);
            $("#inputCodigo").val(data.codigo_ficha);
            $("#inputNombre").val(data.nombre_programa);
            $("#inputFecha_inicio").val(data.fecha_inicio);
            $("#inputFecha_fin").val(data.fecha_fin);
            $("#inputEstado").val(data.estado_ficha);
        },
        error: function (err) {
            console.error("Error al consultar ficha por ID:", err);
            Swal.fire("Error", "No se pudo cargar la ficha.", "error");
        }
    });
}


function actualizarFicha() {
    const id_ficha = $("#inputIdFicha").val();
    const fichaActualizada = {
        codigo_ficha: $("#inputCodigo").val(),
        nombre_programa: $("#inputNombre").val(),
        fecha_inicio: $("#inputFecha_inicio").val(),
        fecha_fin: $("#inputFecha_fin").val(),
        estado_ficha: $("#inputEstado").val()
    };

    $.ajax({
        url: urlFicha + id_ficha,
        type: "PUT",
        contentType: "application/json",
        data: JSON.stringify(fichaActualizada),
        success: function (res) {
            Swal.fire("Ficha actualizada", "La ficha se actualizó correctamente", "success");
            $("#modal-Ficha").modal("hide");
            listaFicha();
        },
        error: function (err) {
            console.error("Error al actualizar ficha:", err);
            Swal.fire("Error", "No se pudo actualizar la ficha", "error");
        }
    });
}

$("#formEditarFicha").submit(function (e) {
    e.preventDefault();
    actualizarFicha();
});



$(document).ready(function () {
    $('#formEditarFicha').submit(function (e) {
        e.preventDefault();
        const id_ficha = $('#inputIdFicha').val();
        actualizarFicha(id_ficha);
    });
});