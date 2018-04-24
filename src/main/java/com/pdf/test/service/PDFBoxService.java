package com.pdf.test.service;

import com.pdf.test.model.Person;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PDFBoxService {

    public final static PDFont BOLD = PDType1Font.HELVETICA_BOLD;
    public final static PDFont PLAIN = PDType1Font.HELVETICA;
    private float Y;
    public final static int X = 50;

    public PDDocument getPersonalDocument(Person person) {
        try {
            Y = 700;
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            addWrappedTextCentered("Personal Information", 22, BOLD, contentStream, page);

            updateY(-15);
            addHeaderAndWrappedText("Name: ", person.getFirstName() + " " + person.getLastName(), contentStream);
            addHeaderAndWrappedText("Age: ", String.valueOf(person.getAge()), contentStream);
            addHeaderAndWrappedText("Married: ", String.valueOf(person.isMarried()), contentStream);
            addHeaderAndWrappedText("Extra: ", "Shields up. Boldly, modern space suits virtually lower an evasive, post-apocalyptic machine. All hands view, devastation!", contentStream);


            updateY(-25);
            addWrappedText("Signature", 14, BOLD, contentStream);
            updateY(-15);
            drawImage("src/images/signature.png", 75, document, contentStream);
            contentStream.close();
            return document;
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }

        return new PDDocument();
    }

    private void drawImage(String path, float height, PDDocument document, PDPageContentStream contentStream) throws IOException {
        PDImageXObject signature = PDImageXObject.createFromFile(path, document);

        float ratio = signature.getHeight() / height;
        float width = signature.getWidth() / ratio;

        contentStream.drawImage(signature, X, Y - height, width, height);
    }

    private void addWrappedText(String text, int size, PDFont font, PDPageContentStream contentStream) throws IOException {
        String[] wrappedText = WordUtils.wrap(text, 125).split("\\r?\\n");
        String string;
        for (int i = 0; i < wrappedText.length; i++) {
            updateY(-15);
            contentStream.beginText();
            contentStream.setFont(font, size);
            contentStream.newLineAtOffset(X, Y);
            string = wrappedText[i];
            contentStream.showText(string);
            contentStream.endText();
        }
    }

    private void addWrappedTextCentered(String text, int size, PDFont font, PDPageContentStream contentStream, PDPage page) throws IOException {
        String[] wrappedText = WordUtils.wrap(text, 75).split("\\r?\\n");
        String string;
        for (int i = 0; i < wrappedText.length; i++) {
            updateY(-15);
            contentStream.beginText();
            contentStream.setFont(font, size);
            string = wrappedText[i];
            float titleWidth = font.getStringWidth(string) / 1000 * size;
            contentStream.newLineAtOffset((page.getMediaBox().getWidth() - titleWidth) / 2, Y);
            contentStream.showText(string);
            contentStream.endText();
        }
    }

    private void addHeaderAndWrappedText(String strHeader, String text, PDPageContentStream contentStream) throws IOException {
        updateY(-15);
        contentStream.beginText();
        contentStream.newLineAtOffset(X, Y);
        contentStream.setFont(BOLD, 12);
        contentStream.showText(strHeader);
        contentStream.setFont(PLAIN, 12);
        String[] wrappedText = WordUtils.wrap(text, 75).split("\\r?\\n");
        String string;
        for (int i = 0; i < wrappedText.length; i++) {
            if (i != 0) {
                updateY(-15);
                contentStream.beginText();
                contentStream.newLineAtOffset(X, Y);
            }
            string = wrappedText[i];
            contentStream.showText(string);
            contentStream.endText();
        }
    }

    private void updateY(int i) {
        Y += i;
    }

}
