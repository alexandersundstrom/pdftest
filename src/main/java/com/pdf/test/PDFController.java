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
    private static float Y;
    private final static PDFont BOLD = PDType1Font.HELVETICA_BOLD;
    private final static PDFont PLAIN = PDType1Font.HELVETICA;

    @RequestMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPDF() {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream ous = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            Y = 700F;

//          ADD HEADER
            addWrappedText("Personal Information",16, BOLD, contentStream);

//          ADD PERSON
            Person person = new Person("John", "Doe", 43, true);

            updateY(15);
            addHeaderAndText("Name: ", person.getFirstName() + " " + person.getLastName(), contentStream);
            addHeaderAndText("Age: ", String.valueOf(person.getAge()), contentStream);
            addHeaderAndText("Married: ", String.valueOf(person.isMarried()), contentStream);
//            addHeaderAndText("Extra: ", "Shields up. Boldly, modern space suits virtually lower an evasive, post-apocalyptic machine. All hands view, devastation!", contentStream);

//          ADD IMAGE
            updateY(15);
            int height = 150;
            int width = 250;
            PDImageXObject signature = PDImageXObject.createFromFile("src/images/signature.png", document);
            contentStream.drawImage(signature, X, Y - height, width, height);

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

    private void updateY(int i) {
        Y += -i;
    }

    private void addWrappedText(String text, int size, PDFont font, PDPageContentStream contentStream) throws IOException {
        String[] wrappedText = WordUtils.wrap(text, 75).split("\\r?\\n");
        String string;
        for (int i = 0; i < wrappedText.length; i++) {
            updateY(15);
            contentStream.beginText();
            contentStream.setFont(font, size);
            contentStream.newLineAtOffset(X, Y);
            string = wrappedText[i];
            contentStream.showText(string);
            contentStream.endText();
        }
    }

    private void addHeaderAndText(String strHeader, String text, PDPageContentStream contentStream) throws IOException {
        updateY(15);
        contentStream.beginText();
        contentStream.newLineAtOffset(X, Y);
        contentStream.setFont(BOLD, 12);
        contentStream.showText(strHeader);

        contentStream.setFont(PLAIN, 12);
        contentStream.showText(text);
        contentStream.endText();


    }

}
