package com.pdf.test.controller;

import com.pdf.test.model.Person;
import com.pdf.test.service.FOPService;
import com.pdf.test.service.PDFBoxService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@RestController
public class PDFController {

    @Autowired
    private PDFBoxService pdfService;

    @Autowired
    private FOPService fopService;

    @RequestMapping(value = "/pdfbox", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPDFBox() {
        Person person = new Person("John", "Doe", 43, true);
        try (PDDocument document = pdfService.getPersonalDocument(person);
             ByteArrayOutputStream ous = new ByteArrayOutputStream()) {
            document.save(ous);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=example.pdf");

            byte[] bytes = ous.toByteArray();

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(new ByteArrayInputStream(bytes)));

        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/pdffop")
    public ResponseEntity<InputStreamResource> getPDFop() {
        byte[] bytes = fopService.generatePDF();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=example.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(bytes)));

    }


}
