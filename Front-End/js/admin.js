function listaAdmin() {

    $.ajax({
        url: urlAdmin,
        type: "GET",
        success: function (result) {
            var cuerpoTabla = document.getElementById("cuerpoTabla");
            cuerpoTabla.innerHTML = "";

            result.forEach(function (admin) {
                // No mostrar al superadmin
                if (admin.role === "SUPERADMIN") {
                    return; // Saltar este usuario y pasar al siguiente
                }
            
                var trRegistro = document.createElement("tr");
            
                var celdaNombres = document.createElement("td");
                var celdaApellidos = document.createElement("td");
                var celdaCorreo_electronico = document.createElement("td");
                var celdaOpciones = document.createElement("td");
            
                celdaNombres.innerText = admin.first_name;
                celdaApellidos.innerText = admin.last_name;
                celdaCorreo_electronico.innerText = admin.username;
            
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
                            eliminarAdmin(admin.id);
                        }
                    });
                };
            
                var divBotones = document.createElement("div");
                divBotones.className = "btn-group";
                divBotones.appendChild(botonEliminar);
            
                celdaOpciones.appendChild(divBotones);
            
                trRegistro.appendChild(celdaNombres);
                trRegistro.appendChild(celdaApellidos);
                trRegistro.appendChild(celdaCorreo_electronico);
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
    listaAdmin();
});


function eliminarAdmin(id) {
    $.ajax({
        url: urlEliminarAdmin + id,
        type: "DELETE",
        success: function (result) {
            Swal.fire(
                '¡Eliminado!',
                'El usuario ha sido eliminado.',
                'success'
            );
            listaAdmin();
        }
    });
}


$(document).ready(function () {
    const modal = $('#modalRegistrarAdmin');

     
        $('#btnAbrirModalAdmin').on('click', function (e) {
            e.preventDefault();
            $('#modalRegistrarAdmin').css('display', 'flex').hide().fadeIn();
        });

        //
        $('#cerrarModalRegistrarAdmin').click(function () {
            modal.hide();
        });

   
        $(window).click(function (event) {
            if (event.target === modal[0]) {
                modal.hide();
            }
        });

    $('#formRegistrarAdmin').on('submit', function (e) {
        e.preventDefault();

        const nuevoAdmin = {
            username: $('#username').val(),
            password: $('#password').val(),
            first_name: $('#first_name').val(),
            last_name: $('#last_name').val()
        };

        $.ajax({
            url: urlNuevo_admin,
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(nuevoAdmin),
            success: function () {
                Swal.fire('¡Registrado!', 'El administrador ha sido creado.', 'success');
                modal.fadeOut();
                listaAdmin();
                $('#formRegistrarAdmin')[0].reset();
            },
            error: function (error) {
                Swal.fire('Error', 'No se pudo registrar el administrador.', 'error');
                console.error(error);
            }
        });
    });
});
