package com.pdf.test.controller;

import com.pdf.test.Database.SignatureRepository;
import com.pdf.test.model.Person;
import com.pdf.test.model.Signature;
import com.pdf.test.service.FOPService;
import com.pdf.test.service.IXDocService;
import com.pdf.test.service.PDFBoxService;
import org.apache.commons.io.FileUtils;
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
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;

@RestController
@RequestMapping("/pdf")
public class PDFController {

    @Autowired
    private PDFBoxService pdfService;

    @Autowired
    private FOPService fopService;

    @Autowired
    private IXDocService ixDocService;

    @Autowired
    SignatureRepository repository;

    @RequestMapping(value = "/box", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPDFBox() throws IOException {
        Person person = new Person("John", "Doe", 43, true);
        ByteArrayInputStream inputStream = null;
        try (PDDocument document = pdfService.getPersonalDocument(person);
             ByteArrayOutputStream ous = new ByteArrayOutputStream()) {
            document.save(ous);
            document.close();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=example.pdf");

            byte[] bytes = ous.toByteArray();

            inputStream = new ByteArrayInputStream(bytes);
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(inputStream));

        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/fop")
    public ResponseEntity<InputStreamResource> getPDFop() {
        Person person = new Person("John", "Doe", 43, true);
        byte[] bytes = fopService.generatePDFBytes(person);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=example.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(bytes)));

    }

    @RequestMapping("/ixdoc")
    public ResponseEntity<InputStreamResource> getIXDox() throws Exception {
        Person person = new Person("John", "Doe", 43, true, "johndoe.jpg", Arrays.asList("Music", "Running", "Family"));
        byte[] bytes = ixDocService.generatePDFBytes(person);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=example.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(bytes)));

    }



    @RequestMapping("/addBlob")
    public void addBlob() {
        byte[] fileContent = null;
        try {
            fileContent = FileUtils.readFileToByteArray(new File("src/images/signature.png"));
        } catch (IOException e) {
            System.out.println("Unable to convert file to byte array. " + e.getMessage());
        }

        try {
            Blob blob = new javax.sql.rowset.serial.SerialBlob(fileContent);
            repository.save(new Signature(blob));
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


}
