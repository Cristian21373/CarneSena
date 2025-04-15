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

                celdaNumero_ficha.innerText = ficha.nombre_programa;
                celdaNombre_programa.innerText = ficha.codigo_ficha;
                celdaFecha_inicio.innerText = ficha.fecha_inicio;
                celdaFecha_fin.innerText = ficha.fecha_fin;
                celdaEstado.innerText = ficha.estado_ficha;
                
                trRegistro.appendChild(celdaNumero_ficha);
                trRegistro.appendChild(celdaNombre_programa);
                trRegistro.appendChild(celdaFecha_inicio);
                trRegistro.appendChild(celdaFecha_fin);
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
    listaFicha();
});