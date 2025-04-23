package com.proyecto.carnesena.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto.carnesena.interfaces.Iusuario;
import com.proyecto.carnesena.model.estado_user;
import com.proyecto.carnesena.model.ficha;
import com.proyecto.carnesena.model.tipo_documento;
import com.proyecto.carnesena.model.usuario;
import com.proyecto.carnesena.service.fichaService;

@RestController
@RequestMapping("/api/v1/usuario")
public class usuarioController {

    @Autowired
    private Iusuario usuarioService;
    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private fichaService fichaService;

    @PostMapping("/registrar-nis")
    public ResponseEntity<Object> registrarNis(@RequestBody usuario usuario) {
        if (usuario.getNis() == 0) {
            return new ResponseEntity<>("El NIS es obligatorio", HttpStatus.BAD_REQUEST);
        }

        Optional<usuario> usuarioExistente = usuarioService.findByNis(usuario.getNis());
        if (usuarioExistente.isPresent()) {
            return new ResponseEntity<>("El NIS ya está registrado", HttpStatus.BAD_REQUEST);
        }

        usuario nuevoUsuario = new usuario();
        nuevoUsuario.setNis(usuario.getNis());
        nuevoUsuario.setEstado_user(estado_user.creado);

        nuevoUsuario.setTipo_documento(tipo_documento.CC);
        nuevoUsuario.setNumero_documento(0);
        nuevoUsuario.setTipo_sangre("A+");

        usuarioService.save(nuevoUsuario);

        return new ResponseEntity<>("NIS registrado correctamente", HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Validar el tipo de archivo (solo PNG y JPG)
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equals("image/png") && !contentType.equals("image/jpeg"))) {
                return ResponseEntity.badRequest().body("Solo se permiten imágenes en formato PNG o JPG.");
            }

            // Obtener el nombre original del archivo
            String nombreOriginal = file.getOriginalFilename();

            // Validar que el nombre del archivo no contenga espacios ni caracteres
            // inválidos
            if (nombreOriginal == null || nombreOriginal.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre del archivo no puede estar vacío.");
            }

            // Expresión regular para verificar caracteres no permitidos (espacios y
            // caracteres especiales)
            if (!nombreOriginal.matches("^[a-zA-Z0-9._-]+$")) {
                return ResponseEntity.badRequest().body(
                        "El nombre del archivo contiene espacios o caracteres no permitidos. Usa solo letras, números, guiones y puntos.");
            }

            // Crear la carpeta "uploads" si no existe
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            // Generar un nombre único para la imagen
            String nombreArchivo = UUID.randomUUID() + "_" + nombreOriginal;
            Path rutaArchivo = Paths.get(UPLOAD_DIR).resolve(nombreArchivo);

            // Guardar la imagen en el servidor
            Files.copy(file.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            // URL de la imagen
            String urlImagen = "http://82.25.92.211:8080/uploads/" + nombreArchivo;
            return ResponseEntity.ok().body("{\"url\": \"" + urlImagen + "\"}");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al subir la imagen");
        }
    }

    @GetMapping("/verificar-nis/{nis}")
    public ResponseEntity<?> verificarNIS(@PathVariable int nis) {
        Optional<usuario> usuarioOptional = usuarioService.findByNis(nis);

        if (!usuarioOptional.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("registrado", false);
            response.put("mensaje", "NIS no encontrado");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        usuario usuario = usuarioOptional.get();
        usuario.setVerificado(true);
        usuarioService.save(usuario);

        Map<String, Object> response = new HashMap<>();
        response.put("registrado", true);
        response.put("estado", usuario.getEstado_user());
        response.put("mensaje", "NIS encontrado");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Object> findAll() {
        var ListUsuario = usuarioService.findAll();
        return new ResponseEntity<>(ListUsuario, HttpStatus.OK);
    }

    @GetMapping("/busquedafiltro/{filtro}")
    public ResponseEntity<Object> findFiltro(@PathVariable String filtro) {
        var ListUsuario = usuarioService.filtroUsuario(filtro);
        return new ResponseEntity<>(ListUsuario, HttpStatus.OK);
    }

    @GetMapping("/{id_usuario}")
    public ResponseEntity<Object> findOne(@PathVariable("id_usuario") String id) {
        var usuario = usuarioService.findById(id);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @DeleteMapping("/{id_usuario}")
    public ResponseEntity<Object> delete(@PathVariable("id_usuario") String id_usuario) {
        usuarioService.deleteById(id_usuario);
        return new ResponseEntity<>("Registro Eliminado", HttpStatus.OK);
    }

    @PutMapping("/actualizar-datos/{nis}")
    public ResponseEntity<Object> actualizarUsuario(@PathVariable("nis") int nis, @RequestBody usuario usuarioUpdate) {
        Optional<usuario> usuarioExistente = usuarioService.findByNis(nis);

        if (!usuarioExistente.isPresent()) {
            return new ResponseEntity<>("El usuario con ese NIS no existe", HttpStatus.NOT_FOUND);
        }

        usuario usuario = usuarioExistente.get();

        // NO permitir si no ha verificado su NIS
        if (!usuario.isVerificado()) {
            return new ResponseEntity<>("Debe verificar su NIS antes de completar el formulario", HttpStatus.FORBIDDEN);
        }

        // NO permitir si ya está completado o descargado
        if (usuario.getEstado_user() == estado_user.completo || usuario.getEstado_user() == estado_user.descargado) {
            return new ResponseEntity<>(
                    "No se pueden registrar datos porque el usuario ya está en estado COMPLETADO o DESCARGADO",
                    HttpStatus.FORBIDDEN);
        }

        // Si está verificado y aún no completado, permite actualizar
        usuario.setFoto(usuarioUpdate.getFoto());
        usuario.setNombre(usuarioUpdate.getNombre().toUpperCase());
        usuario.setApellidos(usuarioUpdate.getApellidos().toUpperCase());
        usuario.setTipo_documento(usuarioUpdate.getTipo_documento());
        usuario.setNumero_documento(usuarioUpdate.getNumero_documento());
        usuario.setTipo_sangre(usuarioUpdate.getTipo_sangre().toUpperCase());
        usuario.setFicha(usuarioUpdate.getFicha());
        usuario.setEstado_user(estado_user.completo);

        usuarioService.save(usuario);
        return new ResponseEntity<>("Datos de usuario registrados correctamente", HttpStatus.OK);
    }

    @PutMapping(value = "/editar/{id_usuario}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> editarUsuario(
            @PathVariable("id_usuario") String id,
            @RequestParam("nis") Integer nis,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("tipo_documento") String tipoDocStr,
            @RequestParam("numero_documento") String numero_documento,
            @RequestParam("tipo_sangre") String tipo_sangre,
            @RequestParam("estado_user") String estadoStr,
            @RequestParam("id_ficha") String id_ficha,
            @RequestParam(value = "ADMIN", required = false, defaultValue = "false") boolean ADMIN) {

        Optional<usuario> usuarioExistente = usuarioService.findById(id);

        if (!usuarioExistente.isPresent()) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        usuario usuario = usuarioExistente.get();

        if (!ADMIN && usuario.getEstado_user() == estado_user.completo) {
            return new ResponseEntity<>("No se puede editar un usuario en estado COMPLETO", HttpStatus.FORBIDDEN);
        }

        // Esto es para saber si la imagen se envia correctamente o si hubo un error
        if (file != null && !file.isEmpty()) {
            String nombreArchivo = file.getOriginalFilename();
            Path ruta = Paths.get("uploads/" + nombreArchivo);
            try {
                Files.write(ruta, file.getBytes());
                usuario.setFoto(ruta.toString());
            } catch (IOException e) {
                return new ResponseEntity<>("Error al guardar la imagen", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Actualizar datos
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setNis(nis);

        usuario.setTipo_documento(tipo_documento.valueOf(tipoDocStr));
        usuario.setEstado_user(estado_user.valueOf(estadoStr));

        try {
            usuario.setNumero_documento(Integer.parseInt(numero_documento));
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Número de documento inválido", HttpStatus.BAD_REQUEST);
        }

        usuario.setTipo_sangre(tipo_sangre);

        if (id_ficha != null && !id_ficha.isBlank()) {
            Optional<ficha> fichaNueva = fichaService.findById(id_ficha);
            fichaNueva.ifPresent(usuario::setFicha);
        }

        usuarioService.save(usuario);
        return new ResponseEntity<>("Datos de usuario actualizados correctamente", HttpStatus.OK);
    }

    @PostMapping("/carga-masiva-nis")
    public ResponseEntity<Object> cargaMasivaNis(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("El archivo está vacío", HttpStatus.BAD_REQUEST);
        }

        List<Integer> nisRegistrados = new ArrayList<>();
        List<Integer> nisDuplicados = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream(); Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Leer la primera hoja

            for (Row row : sheet) {
                Cell cell = row.getCell(2); // Primera columna (NIS)
                if (cell == null || cell.getCellType() != CellType.NUMERIC) {
                    continue; // Saltar filas vacías o con datos inválidos
                }

                int nis = (int) cell.getNumericCellValue();
                Optional<usuario> usuarioExistente = usuarioService.findByNis(nis);

                if (usuarioExistente.isPresent()) {
                    nisDuplicados.add(nis); // Si el NIS ya existe, no lo guardamos
                } else {
                    usuario nuevoUsuario = new usuario();
                    nuevoUsuario.setNis(nis);
                    nuevoUsuario.setEstado_user(estado_user.creado);

                    nuevoUsuario.setNumero_documento(0);
                    nuevoUsuario.setTipo_documento(tipo_documento.CC);

                    usuarioService.save(nuevoUsuario);
                    nisRegistrados.add(nis);
                }
            }

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("registrados", nisRegistrados.size());
            respuesta.put("duplicados", nisDuplicados.size());

            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error al procesar el archivo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
