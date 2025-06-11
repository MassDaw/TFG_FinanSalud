package com.proyecto.tfg_finansalud.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String token) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Confirma tu correo electrónico");
        String url = "https://finan-salud-front.vercel.app/verify?token=" + token;

        String htmlTemplate = """
        <!DOCTYPE html>
        <html lang="es">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Verificación de Correo - FinanSalud</title>
        </head>
        <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;">
            <table width="100%%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td align="center" style="padding: 20px 0;">
                        <table width="600" border="0" cellspacing="0" cellpadding="0" style="background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
                            <tr>
                                <td align="center" style="padding: 30px 0; background-color: #10b981; border-radius: 8px 8px 0 0;">
                                    <h1 style="color: #ffffff; margin: 0; font-size: 28px;">FinanSalud</h1>
                                    <p style="color: #ffffff; margin: 10px 0 0; font-size: 16px;">Tu camino hacia la salud financiera</p>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 40px 30px;">
                                    <h2 style="color: #333333;">¡Bienvenido/a a FinanSalud!</h2>
                                    <p style="color: #555555;">Gracias por registrarte. Para comenzar, verifica tu dirección de correo haciendo clic en el botón a continuación:</p>
                                    <p style="text-align: center; margin: 30px 0;">
                                        <a href="{{URL}}" style="background-color: #10b981; color: white; padding: 15px 30px; text-decoration: none; border-radius: 50px; font-weight: bold;">Verificar mi correo</a>
                                    </p>
                                    <p style="color: #777777;">Si no solicitaste esto, ignora este correo.</p>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 30px; background-color: #f8f8f8; border-radius: 0 0 8px 8px; text-align: center;">
                                    <p style="color: #777777;">© 2025 FinanSalud. Todos los derechos reservados.</p>
                                    <p style="color: #777777;">
                                        <a href="#" style="color: #10b981;">Términos y Condiciones</a> |
                                        <a href="#" style="color: #10b981;">Política de Privacidad</a>
                                    </p>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </body>
        </html>
        """;

        String htmlContent = htmlTemplate.replace("{{URL}}", url);
        helper.setText(htmlContent, true); // `true` indica que es HTML

        mailSender.send(mimeMessage);
    }
}