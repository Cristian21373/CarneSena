$(document).ready(function () {
    $('form').on('submit', function (e) {
        e.preventDefault(); // Evita el envío normal del formulario

        const username = $('#username').val();

        if (!username) {
            Swal.fire({
                icon: 'warning',
                title: 'Campo vacío',
                text: 'Por favor, ingresa tu correo electrónico.',
                didOpen: () => {
                    // Mantenemos scroll en la posición actual
                    window.scrollTo({
                        top: window.scrollY,
                        behavior: 'instant'
                    });
                }
            });
            return;
        }

        // Mostramos alerta de carga
        Swal.fire({
            title: 'Enviando...',
            text: 'Por favor espera un momento',
            allowOutsideClick: false,
            allowEscapeKey: false,
            didOpen: () => {
                Swal.showLoading();

                // Previene que el scroll suba al abrir el modal
                window.scrollTo({
                    top: window.scrollY,
                    behavior: 'instant'
                });
            }
        });

        $.ajax({
            url: urlOlvidoContra,
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ username: username }),
            success: function (response) {
                Swal.fire({
                    icon: 'success',
                    title: 'Correo enviado',
                    text: response,
                    confirmButtonText: 'Aceptar',
                    didOpen: () => {
                        window.scrollTo({
                            top: window.scrollY,
                            behavior: 'instant'
                        });
                    }
                }).then(() => {
                    window.location.href = "/Front-End/html/login/login.html";
                });
            },
            error: function (xhr) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: xhr.responseText,
                    didOpen: () => {
                        window.scrollTo({
                            top: window.scrollY,
                            behavior: 'instant'
                        });
                    }
                });
            }
        });
    });
});
