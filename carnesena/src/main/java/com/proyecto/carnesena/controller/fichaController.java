package com.proyecto.carnesena.controller;

import java.time.LocalDate;
import java.util.List;

// import java.util.List;

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

import com.proyecto.carnesena.interfaceService.IUsuarioService;
import com.proyecto.carnesena.interfaces.Ificha;
import com.proyecto.carnesena.model.ficha;

@RestController
@RequestMapping("/api/v1/ficha")
public class fichaController {

    @Autowired
    private Ificha fichaService;

    @Autowired
    private IUsuarioService usuarioService;

    // @PostMapping("/")
    // public ResponseEntity<Object> save(@RequestBody ficha ficha) {
    // // List<ficha> ficha2 =
    // // fichaService.findByCodigo_ficha(ficha.getCodigo_ficha());
    // // if (!ficha2.isEmpty()) {
    // // return new ResponseEntity<>("El codigo de la ficha ya esta registrado",
    // // HttpStatus.BAD_REQUEST);
    // // }

    // if (ficha.getNombre_programa().equals("")) {
    // return new ResponseEntity<>("Nombre obligatorio", HttpStatus.BAD_REQUEST);
    // }

    // if (ficha.getCodigo_ficha() == 0) {
    // return new ResponseEntity<>("Codigo obligatorio", HttpStatus.BAD_REQUEST);

    // }

    // if (ficha.getFecha_inicio().equals("")) {
    // return new ResponseEntity<>("Fecha de inicio obligatoria ",
    // HttpStatus.BAD_REQUEST);
    // }

    // if (ficha.getFecha_fin().equals("")) {
    // return new ResponseEntity<>("Fecha fin obligatorio", HttpStatus.BAD_REQUEST);
    // }

    // if (ficha.getEstado_ficha().equals("")) {
    // return new ResponseEntity<>("Estado obligatorio", HttpStatus.BAD_REQUEST);
    // }

    // List<ficha> fichaExistente =
    // fichaService.filtroFicha(ficha.getCodigo_ficha());
    // if (!fichaExistente.isEmpty()) {
    // return new ResponseEntity<>("El código de la ficha ya está registrado",
    // HttpStatus.BAD_REQUEST);
    // }

    // fichaService.save(ficha);
    // return new ResponseEntity<>(ficha, HttpStatus.OK);
    // }

    @PostMapping("/")
    public ResponseEntity<Object> save(@RequestBody ficha ficha) {

        if (ficha.getNombre_programa().equals("")) {
            return new ResponseEntity<>("Nombre obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (ficha.getCodigo_ficha() == 0) {
            return new ResponseEntity<>("Código obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (ficha.getFecha_inicio() == null) {
            return new ResponseEntity<>("Fecha de inicio obligatoria", HttpStatus.BAD_REQUEST);
        }

        if (ficha.getFecha_fin() == null) {
            return new ResponseEntity<>("Fecha fin obligatoria", HttpStatus.BAD_REQUEST);
        }

        if (ficha.getEstado_ficha() == null) {
            return new ResponseEntity<>("Estado obligatorio", HttpStatus.BAD_REQUEST);
        }

        // ✅ Validación de fechas con LocalDate directamente
        LocalDate hoy = LocalDate.now();

        if (ficha.getFecha_inicio().isBefore(hoy)) {
            return new ResponseEntity<>("La fecha de inicio no puede ser anterior a hoy", HttpStatus.BAD_REQUEST);
        }

        if (ficha.getFecha_fin().isBefore(hoy)) {
            return new ResponseEntity<>("La fecha de fin no puede ser anterior a hoy", HttpStatus.BAD_REQUEST);
        }

        if (ficha.getFecha_fin().isBefore(ficha.getFecha_inicio())) {
            return new ResponseEntity<>("La fecha de fin no puede ser anterior a la de inicio", HttpStatus.BAD_REQUEST);
        }

        List<ficha> fichaExistente = fichaService.filtroFicha(ficha.getCodigo_ficha());
        if (!fichaExistente.isEmpty()) {
            return new ResponseEntity<>("El código de la ficha ya está registrado", HttpStatus.BAD_REQUEST);
        }

        fichaService.save(ficha);
        return new ResponseEntity<>(ficha, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Object> findAll() {
        var ListFicha = fichaService.findAll();
        return new ResponseEntity<>(ListFicha, HttpStatus.OK);
    }

    @GetMapping("/busquedafiltro/{filtro}")
    public ResponseEntity<Object> findFiltro(@PathVariable int filtro) {
        var ListFicha = fichaService.filtroFicha(filtro);
        return new ResponseEntity<>(ListFicha, HttpStatus.OK);
    }

    @GetMapping("/{id_ficha}")
    public ResponseEntity<Object> findOne(@PathVariable("id_ficha") String id) {
        var ficha = fichaService.findById(id);
        return new ResponseEntity<>(ficha, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id_ficha}")
    public ResponseEntity<Object> delete(@PathVariable("id_ficha") String id_ficha) {
        int cantidadUsuarios = usuarioService.contarUsuariosPorFicha(id_ficha);

        if (cantidadUsuarios > 0) {
            return new ResponseEntity<>(
                    "No se puede eliminar la ficha. Existen " + cantidadUsuarios
                            + " usuario(s) asociados. Por favor elimínelos primero.",
                    HttpStatus.BAD_REQUEST);
        }

        fichaService.deleteById(id_ficha);
        return new ResponseEntity<>("Ficha eliminada correctamente", HttpStatus.OK);
    }

    @PutMapping("/{id_ficha}")
    public ResponseEntity<Object> update(@PathVariable("id_ficha") String id_ficha, @RequestBody ficha fichaUpdate) {
        var ficha = fichaService.findById(id_ficha).orElse(null);
        if (ficha != null) {
            ficha.setNombre_programa(fichaUpdate.getNombre_programa().toUpperCase());
            ficha.setCodigo_ficha(fichaUpdate.getCodigo_ficha());
            ficha.setEstado_ficha(fichaUpdate.getEstado_ficha());
            ficha.setFecha_inicio(fichaUpdate.getFecha_inicio());
            ficha.setFecha_fin(fichaUpdate.getFecha_fin());

            fichaService.save(ficha);
            return new ResponseEntity<>("Guardado", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error: ficha no encontrado", HttpStatus.BAD_REQUEST);
        }
    }

}
