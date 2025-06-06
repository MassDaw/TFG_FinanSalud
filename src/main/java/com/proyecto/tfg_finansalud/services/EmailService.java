package com.proyecto.tfg_finansalud.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirma tu correo electrónico");
        message.setText("Por favor, haz clic en el siguiente enlace para verificar tu correo electrónico: "
                + "http://localhost:8080/api/auth/verify?token=" + token);
        mailSender.send(message);
    }
}