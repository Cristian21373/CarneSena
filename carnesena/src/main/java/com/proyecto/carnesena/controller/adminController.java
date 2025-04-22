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

        // Generar el cuerpo del correo HTML
        String cuerpoCorreo = "<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><style>"
                +
                "body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; color: #333; }"
                +
                ".container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); overflow: hidden; }"
                +
                ".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; font-size: 24px; font-weight: bold; }"
                +
                ".content { padding: 20px; }" +
                ".content h2 { color: #333; font-size: 20px; margin-bottom: 10px; }" +
                ".content p { color: #555; font-size: 16px; line-height: 1.6; }" +
                ".content .important { color: #4CAF50; font-weight: bold; }" +
                ".footer { background-color: #f4f4f4; text-align: center; padding: 10px; font-size: 14px; color: #777; border-top: 1px solid #ddd; }"
                +
                "@media (max-width: 600px) { .container { width: 100%; padding: 10px; } .header { font-size: 20px; } .content h2 { font-size: 18px; } }"
                +
                "</style></head><body>" +
                "<div class=\"container\">" +
                "<div class=\"header\">Recuperación de Contraseña</div>" +
                "<div class=\"content\">" +
                "<h2>Hola " + admin.getFirst_name() + ":</h2>" +
                "<p>Recibimos una solicitud para recuperar tu contraseña. Aquí está tu nueva contraseña:</p>" +
                "<p class=\"important\">" + nuevaContrasena + "</p>" +
                "<p>Si no solicitaste un cambio de contraseña, por favor ignora este mensaje o contacta con el soporte.</p>"
                +
                "</div>" +
                "<div class=\"footer\"><p>Este es un correo automático, por favor no respondas.</p></div>" +
                "</div>" +
                "</body></html>";

        emailService.enviarCorreo(admin.getUsername(), "Recuperación de contraseña", cuerpoCorreo);

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
