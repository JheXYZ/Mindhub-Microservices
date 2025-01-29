package com.mindhub.email_service.services;

import com.lowagie.text.DocumentException;
import com.mindhub.email_service.dtos.order.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;


@Service
public class PdfService {

    private static final Logger log = LoggerFactory.getLogger(PdfService.class);

    @Autowired
    private TemplateEngine templateEngine;

    public byte[] generateOrderPdf(OrderDTO order) {
        // Crear el contexto para la plantilla Thymeleaf
        Context context = new Context();

        context.setVariable("order", order);

        // Procesar la plantilla Thymeleaf
        String htmlContent = templateEngine.process("order_creation_template", context);

        // Generar el PDF desde el HTML
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        try {
            renderer.createPDF(outputStream);
        } catch (DocumentException e) {
            log.error(e.getLocalizedMessage());
        }

        return outputStream.toByteArray();
    }
}
