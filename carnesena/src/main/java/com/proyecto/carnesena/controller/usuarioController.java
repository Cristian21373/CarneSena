package com.proyecto.carnesena.controller;

import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.carnesena.interfaces.Iusuario;
import com.proyecto.carnesena.model.usuario;

@RestController
@RequestMapping("/api/v1/usuario")
public class usuarioController {

    @Autowired
    private Iusuario usuarioService;

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
