package com.proyecto.carnesena.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.carnesena.interfaces.Iadmin;
import com.proyecto.carnesena.model.CambiarPasswordRequest;
import com.proyecto.carnesena.model.admin;

import com.proyecto.carnesena.service.EmailService;
import com.proyecto.carnesena.service.authService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/")
public class adminController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Iadmin adminRepository;

    @Autowired
    private EmailService emailService;


    private final authService authService;

    @GetMapping("profile/")
    public ResponseEntity<String> profile(@RequestBody String request) {
        return new ResponseEntity<String>("End-point private profile", HttpStatus.OK);
    }

    @GetMapping("listado/")
    public ResponseEntity<Object> findAll() {
        var ListAdmin = authService.findAll();
        return new ResponseEntity<>(ListAdmin, HttpStatus.OK);
    }

    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        authService.deleteById(id);
        return new ResponseEntity<>("Registro Eliminado", HttpStatus.OK);
    }

    @PostMapping("olvide-password/")
    public ResponseEntity<String> olvidePassword(@RequestBody Map<String, String> body) {
        String username = body.get("username");

        Optional<admin> adminOpt = authService.findByUsername(username);

        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Correo no encontrado");
        }

        admin admin = adminOpt.get();
        String nuevaContrasena = authService.generarNuevaContrasena();
        admin.setPassword(passwordEncoder.encode(nuevaContrasena));
        authService.save(admin);

        String cuerpo = "Hola " + admin.getFirst_name() + ", tu nueva contraseña es: " + nuevaContrasena;
        emailService.enviarCorreo(admin.getUsername(), "Recuperación de contraseña", cuerpo);

        return ResponseEntity.ok("Se ha enviado una nueva contraseña al correo");
    }

    @PutMapping("cambiar-password/{id}")
    public ResponseEntity<?> cambiarPasswordAdmin(@PathVariable String id,
            @RequestBody CambiarPasswordRequest request) {
        Optional<admin> optionalAdmin = adminRepository.findById(id);
        if (!optionalAdmin.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Administrador no encontrado");
        }

        admin admin = optionalAdmin.get();

        // 1. Verificar si la contraseña actual coincide con la base de datos
        if (!passwordEncoder.matches(request.getPasswordActual(), admin.getPassword())) {
            return ResponseEntity.badRequest().body("La contraseña actual es incorrecta");
        }

        // 2. Verificar que la nueva contraseña sea diferente de la actual
        if (request.getPasswordActual().equals(request.getNuevaPassword())) {
            return ResponseEntity.badRequest().body("La nueva contraseña debe ser diferente de la actual");
        }

        // 3. Verificar que la nueva contraseña y su confirmación coincidan
        if (!request.getNuevaPassword().equals(request.getConfirmarPassword())) {
            return ResponseEntity.badRequest().body("La nueva contraseña y la confirmación no coinciden");
        }

        // Todo bien: se actualiza la contraseña
        String nuevaPasswordEncriptada = passwordEncoder.encode(request.getNuevaPassword());
        admin.setPassword(nuevaPasswordEncriptada);
        adminRepository.save(admin);

        return ResponseEntity.ok("Contraseña del administrador actualizada correctamente");
    }

}
