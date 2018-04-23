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
    private static float Y_HEIGHT;
    private final static PDFont BOLD = PDType1Font.HELVETICA_BOLD;
    private final static PDFont PLAIN = PDType1Font.HELVETICA;

    @RequestMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPDF() {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream ous = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            Y_HEIGHT = 700F;

//          ADD HEADER
            addWrappedTextCentered("Personal Information", 22, BOLD, contentStream, page);

//          ADD PERSON
            Person person = new Person("John", "Doe", 43, true);

            addY(15);
            addHeaderAndWrappedText("Name: ", person.getFirstName() + " " + person.getLastName(), contentStream);
            addHeaderAndWrappedText("Age: ", String.valueOf(person.getAge()), contentStream);
            addHeaderAndWrappedText("Married: ", String.valueOf(person.isMarried()), contentStream);
            addHeaderAndWrappedText("Extra: ", "Shields up. Boldly, modern space suits virtually lower an evasive, post-apocalyptic machine. All hands view, devastation!", contentStream);

//          ADD IMAGE
            addY(25);
            addWrappedText("Signature", 14, BOLD, contentStream);
            addY(15);
            drawImage("src/images/signature.png", 75, document, contentStream);


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

    private void drawImage(String path, float height, PDDocument document, PDPageContentStream contentStream) throws IOException {
        PDImageXObject signature = PDImageXObject.createFromFile(path, document);

        float ratio =  signature.getHeight() / height;
        float width =  signature.getWidth() / ratio;

        contentStream.drawImage(signature, X, Y_HEIGHT - height, width, height);
    }

    private void addY(int i) {
        Y_HEIGHT += -i;
    }

    /**
     * Some information
     * @param text
     * @param size
     * @param font
     * @param contentStream
     * @throws IOException
     */
    private void addWrappedText(String text, int size, PDFont font, PDPageContentStream contentStream) throws IOException {
        String[] wrappedText = WordUtils.wrap(text, 125).split("\\r?\\n");
        String string;
        for (int i = 0; i < wrappedText.length; i++) {
            addY(15);
            contentStream.beginText();
            contentStream.setFont(font, size);
            contentStream.newLineAtOffset(X, Y_HEIGHT);
            string = wrappedText[i];
            contentStream.showText(string);
            contentStream.endText();
        }
    }

    private void addWrappedTextCentered(String text, int size, PDFont font, PDPageContentStream contentStream, PDPage page) throws IOException {
        String[] wrappedText = WordUtils.wrap(text, 75).split("\\r?\\n");
        String string;
        for (int i = 0; i < wrappedText.length; i++) {
            addY(15);
            contentStream.beginText();
            contentStream.setFont(font, size);
            string = wrappedText[i];
            float titleWidth = font.getStringWidth(string) / 1000 * size;
            contentStream.newLineAtOffset((page.getMediaBox().getWidth() - titleWidth) / 2, Y_HEIGHT);
            contentStream.showText(string);
            contentStream.endText();
        }
    }

    private void addHeaderAndWrappedText(String strHeader, String text, PDPageContentStream contentStream) throws IOException {
        addY(15);
        contentStream.beginText();
        contentStream.newLineAtOffset(X, Y_HEIGHT);
        contentStream.setFont(BOLD, 12);
        contentStream.showText(strHeader);
        contentStream.setFont(PLAIN, 12);
        String[] wrappedText = WordUtils.wrap(text, 75).split("\\r?\\n");
        String string;
        for (int i = 0; i < wrappedText.length; i++) {
            if (i != 0) {
                addY(15);
                contentStream.beginText();
                contentStream.newLineAtOffset(X, Y_HEIGHT);
            }
            string = wrappedText[i];
            contentStream.showText(string);
            contentStream.endText();
        }
    }
}
