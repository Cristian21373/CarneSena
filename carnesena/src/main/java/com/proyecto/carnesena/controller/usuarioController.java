package com.proyecto.carnesena.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/")
    public ResponseEntity<Object> save(@RequestBody usuario usuario) {
        // List<usuario> usuario2 = usuarioService.filtroUsuario(usuario.getNis());
        // if (!usuario2.isEmpty()) {
        //     return new ResponseEntity<>("El nis ya esta registrado", HttpStatus.BAD_REQUEST);
        // }

        if (usuario.getFoto().equals("")) {
            return new ResponseEntity<>("Es obligatorios", HttpStatus.BAD_REQUEST);
        }
        
        if (usuario.getNombre().equals("")) {
            return new ResponseEntity<>("Los nombres son obligatorios", HttpStatus.BAD_REQUEST);
        }

        if (usuario.getApellidos().equals("")) {

            return new ResponseEntity<>("Los apellidos son obligatorios", HttpStatus.BAD_REQUEST);
            
        }

        if (usuario.getTipo_documento().equals("")) {
            return new ResponseEntity<>("Tipo de documento obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (usuario.getTipo_sangre().equals("")) {
            return new ResponseEntity<>("Tipo de sangre obligatorio", HttpStatus.BAD_REQUEST);
            
        }

        if (usuario.getNumero_documento()==0) {
            return new ResponseEntity<>("numero de documento obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (usuario.getNis()==0) {
            return new ResponseEntity<>("Nis obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (usuario.getFicha().equals("")) {
            return new ResponseEntity<>("ficha obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (usuario.getEstado_user().equals("")) {
            return new ResponseEntity<>("estado obligatorio", HttpStatus.BAD_REQUEST);
        }


        usuarioService.save(usuario);
        return new ResponseEntity<>(usuario,HttpStatus.OK);
    }


}
