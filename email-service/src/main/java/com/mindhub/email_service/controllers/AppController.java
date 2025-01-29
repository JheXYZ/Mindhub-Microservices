package com.mindhub.email_service.controllers;

import com.mindhub.email_service.dtos.order.OrderDTO;
import com.mindhub.email_service.services.EmailService;
import com.mindhub.email_service.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AppController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private PdfService pdfService;

    @PostMapping("/email/newOrder")
    @ResponseStatus(HttpStatus.CREATED)
    public String newOrder(@RequestBody OrderDTO orderDTO) {
        emailService.listenerCreateOrder(orderDTO);
        return "created";
    }

    @PostMapping("/pdf")
    public ResponseEntity<ByteArrayResource> createPDF(@RequestBody OrderDTO orderDTO) {
        byte[] pdfBytes = pdfService.generateOrderPdf(orderDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resumen_orden.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(pdfBytes));
    }
}
