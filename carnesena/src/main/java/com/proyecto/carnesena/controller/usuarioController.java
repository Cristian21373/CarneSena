package com.proyecto.carnesena.controller;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.proyecto.carnesena.model.usuario;

@RestController
@RequestMapping("/api/v1/usuario")
public class usuarioController {

    @Autowired
    private Iusuario usuarioService;
    private static final String UPLOAD_DIR = "uploads/";

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
        usuarioService.save(nuevoUsuario);

        return new ResponseEntity<>("NIS registrado correctamente", HttpStatus.OK);
    }

    // @PostMapping("/subir-imagen/{id_usuario}")
    // public ResponseEntity<Object> subirImagen(@PathVariable String id_usuario,
    //         @RequestParam("file") MultipartFile file) {
    //     try {
    //         // 1️⃣ Buscar el usuario
    //         usuario usuario = usuarioService.findById(id_usuario).orElse(null);
    //         if (usuario == null) {
    //             return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
    //         }

    //         // 2️⃣ Validar que el archivo no esté vacío
    //         if (file.isEmpty()) {
    //             return new ResponseEntity<>("El archivo está vacío", HttpStatus.BAD_REQUEST);
    //         }

    //         // 3️⃣ Definir la ruta de almacenamiento
    //         String directorio = "uploads/";
    //         File carpeta = new File(directorio);
    //         if (!carpeta.exists()) {
    //             carpeta.mkdirs(); // Crea la carpeta si no existe
    //         }

    //         // 4️⃣ Guardar la imagen en el servidor
    //         String nombreArchivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
    //         Path rutaArchivo = Paths.get(directorio + nombreArchivo);
    //         Files.write(rutaArchivo, file.getBytes());

    //         // 5️⃣ Generar la URL (puedes cambiarla según tu configuración)
    //         String urlImagen = "http://localhost:8080/uploads/" + nombreArchivo;

    //         // 6️⃣ Guardar la URL en la BD
    //         usuario.setFoto(urlImagen);
    //         usuarioService.save(usuario);

    //         return new ResponseEntity<>("Imagen subida correctamente: " + urlImagen, HttpStatus.OK);

    //     } catch (Exception e) {
    //         return new ResponseEntity<>("Error al subir la imagen", HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Crear la carpeta "uploads" si no existe
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            // Generar un nombre único para la imagen
            String nombreArchivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path rutaArchivo = Paths.get(UPLOAD_DIR).resolve(nombreArchivo);
            
            // Guardar la imagen en el servidor
            Files.copy(file.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            // URL de la imagen
            String urlImagen = "http://localhost:8080/uploads/" + nombreArchivo;
            return ResponseEntity.ok().body("{\"url\": \"" + urlImagen + "\"}");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al subir la imagen");
        }
    }

    
    @GetMapping("/verificar-nis/{nis}")
    public ResponseEntity<Object> verificarNis(@PathVariable("nis") int nis) {
        Optional<usuario> usuarioExistente = usuarioService.findByNis(nis);

        if (!usuarioExistente.isPresent()) {
            return new ResponseEntity<>("El NIS no está registrado", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(usuarioExistente.get(), HttpStatus.OK);
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
        // Buscar usuario por NIS
        Optional<usuario> usuarioExistente = usuarioService.findByNis(nis);

        if (!usuarioExistente.isPresent()) {
            return new ResponseEntity<>("El usuario con ese NIS no existe", HttpStatus.NOT_FOUND);
        }

        usuario usuario = usuarioExistente.get();
        usuario.setFoto(usuarioUpdate.getFoto());
        usuario.setNombre(usuarioUpdate.getNombre());
        usuario.setApellidos(usuarioUpdate.getApellidos());
        usuario.setTipo_documento(usuarioUpdate.getTipo_documento());
        usuario.setNumero_documento(usuarioUpdate.getNumero_documento());
        usuario.setTipo_sangre(usuarioUpdate.getTipo_sangre());
        usuario.setFicha(usuarioUpdate.getFicha());
        usuario.setEstado_user(usuarioUpdate.getEstado_user());

        usuarioService.save(usuario);
        return new ResponseEntity<>("Usuario actualizado correctamente", HttpStatus.OK);
    }

}
