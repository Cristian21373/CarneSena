const letrasPermitidas = [
    'A', 'Á', 'B', 'C', 'D', 'E', 'É', 'F', 'G', 'H', 'I', 'Í', 'J', 'K', 'L', 'M',
    'N', 'Ñ', 'O', 'Ó', 'P', 'Q', 'R', 'S', 'T', 'U', 'Ú', 'Ü', 'V', 'W', 'X', 'Y', 'Z',
    'a', 'á', 'b', 'c', 'd', 'e', 'é', 'f', 'g', 'h', 'i', 'í', 'j', 'k', 'l', 'm',
    'n', 'ñ', 'o', 'ó', 'p', 'q', 'r', 's', 't', 'u', 'ú', 'ü', 'v', 'w', 'x', 'y', 'z', ' '
];
const numerosPermitidos = [
    '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
];
const signosPermitidos = [
    '.', ',', '@', '_', '-', '#', '', '(',')'
];

function soloLetras(event) {

    if (!(letrasPermitidas.includes(event.key))) {
        event.preventDefault();
        return;
    }
}
function soloNumeros(event) {

    if (!(numerosPermitidos.includes(event.key))) {
        event.preventDefault();
        return;
    }
}
function alfaNumericosSignos(event) {

    if (!((numerosPermitidos.includes(event.key)) || (letrasPermitidas.includes(event.key)) || (signosPermitidos.includes(event.key)))) {
        event.preventDefault();
        return;
    }
}