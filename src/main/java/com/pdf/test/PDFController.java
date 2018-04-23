package com.pdf.test;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
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
    private final static int X = 50;
    private static float Y = 700f;
    @RequestMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPDF() {


        try (PDDocument document = new PDDocument(); ByteArrayOutputStream ous = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDFont bold = PDType1Font.HELVETICA_BOLD;
            PDFont plain = PDType1Font.HELVETICA;
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

//          ADD HEADER
            String text = "Personal Information";
            String[] header;
            String s;
            header = WordUtils.wrap(text, 75).split("\\r?\\n");

            for (int i = 0; i < header.length; i++) {
                contentStream.beginText();
                contentStream.setFont(bold, 16);
                Y -= i * 15;
                contentStream.newLineAtOffset(X, Y);
                s = header[i];
                contentStream.showText(s);
                contentStream.endText();
            }

//          ADD PERSON
            Person person = new Person("John", "Doe", 43, true );

            contentStream.beginText();
            Y += -30;
            contentStream.newLineAtOffset(X, Y);
            contentStream.setFont(bold, 12);
            contentStream.showText("Name: ");
            contentStream.setFont(plain, 12);
            contentStream.showText(person.getFirstName() + " " + person.getLastName());
            contentStream.endText();

            contentStream.beginText();
            Y += -15;
            contentStream.newLineAtOffset(X, Y);
            contentStream.setFont(bold, 12);
            contentStream.showText("Age: ");
            contentStream.setFont(plain, 12);
            contentStream.showText(String.valueOf(person.getAge()));
            contentStream.endText();

            contentStream.beginText();
            Y += -15;
            contentStream.newLineAtOffset(X, Y);
            contentStream.setFont(bold, 12);
            contentStream.showText("Married: ");
            contentStream.setFont(plain, 12);
            contentStream.showText(String.valueOf(person.isMarried()));
            contentStream.endText();

//          ADD IMAGE
            Y += -15;

            int  height = 150;
            int  width = 250;
            PDImageXObject image = PDImageXObject.createFromFile("src/images/signature.png", document);
            contentStream.drawImage(image, X, Y - height, width, height);

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
