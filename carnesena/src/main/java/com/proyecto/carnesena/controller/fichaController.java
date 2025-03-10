package com.proyecto.carnesena.controller;

// import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.carnesena.interfaces.Ificha;
import com.proyecto.carnesena.model.ficha;

@RestController
@RequestMapping("/api/v1/ficha")
public class fichaController {

    @Autowired
    private Ificha fichaService;

    @PostMapping("/")
    public ResponseEntity<Object> save(@RequestBody ficha ficha) {
        // List<ficha> ficha2 = fichaService.findByCodigo_ficha(ficha.getCodigo_ficha());
        // if (!ficha2.isEmpty()) {
        //     return new ResponseEntity<>("El codigo de la ficha ya esta registrado", HttpStatus.BAD_REQUEST);
        // }

        if (ficha.getNombre_programa().equals("")) {
            return new ResponseEntity<>("Nombre obligatorio", HttpStatus.BAD_REQUEST);
        }
        
        if (ficha.getCodigo_ficha()==0) {
            return new ResponseEntity<>("Codigo obligatorio", HttpStatus.BAD_REQUEST);
            
        }

        if (ficha.getFecha_inicio().equals("")) {
            return new ResponseEntity<>("Fecha de inicio obligatoria ", HttpStatus.BAD_REQUEST);
        }
        
        if (ficha.getFecha_fin().equals("")) {
            return new ResponseEntity<>("Fecha fin obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (ficha.getEstado_ficha().equals("")) {
            return new ResponseEntity<>("Estado obligatorio", HttpStatus.BAD_REQUEST);
        }


        fichaService.save(ficha);
        return new ResponseEntity<>(ficha,HttpStatus.OK);
    }



}
