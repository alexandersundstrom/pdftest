package com.pdf.test;

import org.apache.commons.lang3.text.WordUtils;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
public class PDFController {

    @RequestMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPDF() {

        try (PDDocument document = new PDDocument(); ByteArrayOutputStream ous = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDFont bold = PDType1Font.HELVETICA_BOLD;
            PDFont plain = PDType1Font.HELVETICA;
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            String text = "Crewmates are the suns of the chemical love. Ship of a real energy, desire the core!";
            String[] header = null;
            String s = null;
            float y = 0f;
            header = WordUtils.wrap(text, 75).split("\\r?\\n");

            for (int i = 0; i < header.length; i++) {
                contentStream.beginText();
                contentStream.setFont(bold, 14);
                y = 700 - i * 15;
                contentStream.newLineAtOffset(50, y);
                s = header[i];
                contentStream.showText(s);
                contentStream.endText();
            }

            Person person = new Person("Alexander", "Sundström", 35, true );

            contentStream.beginText();
            y = y -30;
            contentStream.newLineAtOffset(50, y);
            contentStream.setFont(bold, 12);
            contentStream.showText("Name: ");
            contentStream.setFont(plain, 12);
            contentStream.showText(person.getFirstName() + " " + person.getLastName());
            contentStream.endText();

            contentStream.beginText();
            y = y -15;
            contentStream.newLineAtOffset(50, y);
            contentStream.setFont(bold, 12);
            contentStream.showText("Age: ");
            contentStream.setFont(plain, 12);
            contentStream.showText(String.valueOf(person.getAge()));
            contentStream.endText();

            contentStream.beginText();
            y = y -15;
            contentStream.newLineAtOffset(50, y);
            contentStream.setFont(bold, 12);
            contentStream.showText("Married: ");
            contentStream.setFont(plain, 12);
            contentStream.showText(String.valueOf(person.isMarried()));
            contentStream.endText();

            contentStream.close();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=example.pdf");

            document.save(ous);
            byte[] bytes = ous.toByteArray();

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(new ByteArrayInputStream(bytes)));

        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
