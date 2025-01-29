package com.mindhub.email_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.email_service.dtos.order.OrderDTO;
import com.mindhub.email_service.dtos.user.UserDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private PdfService pdfService;
    @Value("${spring.mail.username}")
    private String sender;

    @RabbitListener(queues = "confirmOrder")
    public void listenerCreateOrder(OrderDTO orderDTO) {
        log.info("order received from {} and has {} items", orderDTO.getUser().email(), orderDTO.getProducts().size());

        byte[] pdfBytes = pdfService.generateOrderPdf(orderDTO);
        String toEmail = orderDTO.getUser().email(),
                subject = "New order with your " + orderDTO.getProducts().size() + (orderDTO.getProducts().size() > 1 ? " items" : " item") + '!',
                body = "Your new order has id " + orderDTO.getId() + ".\nHere we attach a PDF with your order.",
                pdfName = "Order " + orderDTO.getId() + ".pdf";
        log.info("sending email to {}...", orderDTO.getUser().email());
        sendEmailWithAttachment(toEmail, subject, body, pdfBytes, pdfName);
    }

    @RabbitListener(queues = "createUser")
    public void listenerCreateUser(UserDTO user) {
        log.info("user created: {}", user.email());

        String toEmail = user.email(),
                subject = "Welcome " + user.username() + " to our OrderApp!",
                body = "You have registered on our OrderApp with this " + user.email() + " email. We just wanted to thank you for trusting in our services! You can now access our products and make purchases by first creating your order and then confirming your purchase.\nYou can learn more on our site (currently doesn't exists xd).\nSigned: OrderApp Team.";
        log.info("sending email to {}...", toEmail);
        sendEmail(toEmail, subject, body);
    }

    public void sendEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(toEmail);
            mailMessage.setSubject(subject);
            mailMessage.setText(body);
            javaMailSender.send(mailMessage);
            log.info("email sent successfully to {}", toEmail);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void sendEmailWithAttachment(String toEmail, String subject, String body, byte[] pdfBytes, String attachmentName) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);

            ByteArrayResource pdfResource = new ByteArrayResource(pdfBytes);
            helper.addAttachment(attachmentName, pdfResource);

            javaMailSender.send(message);
            log.info("email of new order sent successfully to email {}", toEmail);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo", e);
        }
    }
}
