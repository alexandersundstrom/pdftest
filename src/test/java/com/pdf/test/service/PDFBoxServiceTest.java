package com.pdf.test.service;

import com.pdf.test.model.Person;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PDFBoxServiceTest {

    @Test
    void testTextLayout() {
        Person person = new Person("John", "Doe", 43, true);
        PDFBoxService pdfService = new PDFBoxService();
        try (PDDocument document = pdfService.getPersonalDocument(person)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            String expected = "Personal Information\n" +
                    "Name: John Doe\n" +
                    "Age: 43\n" +
                    "Married: true\n" +
                    "Extra: Shields up. Boldly, modern space suits virtually lower an evasive,\n" +
                    "post-apocalyptic machine. All hands view, devastation!\n" +
                    "Signature\n";
            Assertions.assertEquals(expected, text, "Extracted text was not as expected");

        } catch (Exception e) {

        }
    }

    @Test
    void isSignatureAdded() {
        Person person = new Person("John", "Doe", 43, true);
        PDFBoxService pdfService = new PDFBoxService();
        try (PDDocument document = pdfService.getPersonalDocument(person)) {
            boolean hasImage = false;
            PDPage page = document.getPage(0);
            PDResources resources = page.getResources();
            for (COSName cos : page.getResources().getXObjectNames()) {
                if (resources.isImageXObject(cos)) {
                    hasImage = true;
                    break;
                }
            }
            Assertions.assertEquals(true, hasImage, "No Signature found");
        } catch (Exception e) {

        }
    }
}