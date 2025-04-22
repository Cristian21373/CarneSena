package com.proyecto.carnesena.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void enviarCorreo(String destino, String asunto, String cuerpo) {
        try {
            // Crear un mensaje MIME para enviar contenido HTML
            MimeMessage mensaje = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

            // Establecer los detalles del correo
            helper.setTo(destino);
            helper.setSubject(asunto);
            helper.setText(cuerpo, true);  // true indica que el contenido es HTML

            // Enviar el mensaje
            javaMailSender.send(mensaje);

        } catch (MessagingException | MailException e) {
            // Capturar y manejar posibles errores
            e.printStackTrace();
        }
    }

}
