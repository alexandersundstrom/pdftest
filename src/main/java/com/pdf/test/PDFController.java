package com.pdf.test;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class PDFController {

    @RequestMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPDF() {
        PDPageContentStream contentStream = null;
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDFont font = PDType1Font.HELVETICA_BOLD;
            contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();
            contentStream.setFont(font, 14);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Hello World");
            contentStream.endText();
            contentStream.close();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=citiesreport.pdf");

            document.save("blank.pdf");
            File file = new File("blank.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(new FileInputStream(file)));

        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
