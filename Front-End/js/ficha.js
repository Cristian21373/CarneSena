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

                celdaNumero_ficha.innerText = ficha.nombre_programa;
                celdaNombre_programa.innerText = ficha.codigo_ficha;
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


//Eliminar usuario
function eliminarFicha(id_ficha) {
    $.ajax({
        url: urlEliminarFicha + id_ficha,
        type: "DELETE",
        success: function (result) {
            Swal.fire(
                '¡Eliminado!',
                'El usuario ha sido eliminado.',
                'success'
            );
            listaFicha();
        }
    });
}

document.addEventListener('DOMContentLoaded', function () {
    const modalAgregarFicha = document.getElementById('modalAgregarFicha');
    const btnAbrirModalFicha = document.getElementById('btnAbrirModalFicha');
    const cerrarModalNis = document.getElementById('cerrarModalNis');
    const opcionManual = document.getElementById('opcionManual');
    const modalManualNis = document.getElementById('modalManualNis');

    // Abrir el primer modal
    btnAbrirModalFicha.addEventListener('click', function () {
        modalAgregarFicha.style.display = 'flex';
    });

    // Cerrar el modal de selección
    cerrarModalNis.addEventListener('click', function () {
        modalAgregarFicha.style.display = 'none';
    });

    // Abrir el modal manual
    opcionManual.addEventListener('click', function () {
        modalAgregarFicha.style.display = 'none';
        modalManualNis.style.display = 'flex';
    });

});
