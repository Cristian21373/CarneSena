let datosCompletos = []; // Aquí guardamos los datos traídos de la API
let currentPage = 1;
let rowsPerPage = 10;

function mostrarDatosPaginados(data) {
    datosCompletos = data; // Guardamos los datos originales
    renderTabla();
    setupPaginacion();
}

function renderTabla() {
    const cuerpoTabla = document.getElementById("cuerpoTabla");
    cuerpoTabla.innerHTML = "";

    let start = (currentPage - 1) * rowsPerPage;
    let end = start + rowsPerPage;
    let paginados = datosCompletos.slice(start, end);

    if (paginados.length === 0) {
        cuerpoTabla.innerHTML = "<tr><td colspan='10'>No hay datos para mostrar</td></tr>";
        return;
    }

    for (let dato of paginados) {
        let fila = `<tr>
            <td><img src="${dato.foto}" width="50"></td>
            <td>${dato.nis}</td>
            <td>${dato.nombres}</td>
            <td>${dato.apellidos}</td>
            <td>${dato.tipoDocumento}</td>
            <td>${dato.numeroDocumento}</td>
            <td>${dato.numeroFicha}</td>
            <td>${dato.programa}</td>
            <td>${dato.estado}</td>
            <td>Acciones</td>
        </tr>`;
        cuerpoTabla.innerHTML += fila;
    }
}

function setupPaginacion() {
    const pagDiv = document.getElementById("pagination");
    pagDiv.innerHTML = "";

    let totalPages = Math.ceil(datosCompletos.length / rowsPerPage);

    for (let i = 1; i <= totalPages; i++) {
        let btn = document.createElement("button");
        btn.textContent = i;
        btn.className = (i === currentPage) ? "active" : "";
        btn.addEventListener("click", () => {
            currentPage = i;
            renderTabla();
            setupPaginacion();
        });
        pagDiv.appendChild(btn);
    }
}

// Manejar cambio de cantidad por página
document.getElementById("rowsPerPage").addEventListener("change", (e) => {
    rowsPerPage = parseInt(e.target.value);
    currentPage = 1; // Vuelve a la primera página
    renderTabla();
    setupPaginacion();
});
