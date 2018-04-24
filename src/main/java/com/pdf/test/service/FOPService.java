package com.pdf.test.service;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;

@Service
public class FOPService {

    public byte[] generatePDF() {
        try (ByteArrayOutputStream ous = new ByteArrayOutputStream()) {

            FopFactory fopFactory = FopFactory.newInstance(new File("src/fop/configuration.xconf"));
            // Step 3: Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, ous);

            // Step 4: Setup JAXP using identity transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(); // identity transformer

            // Step 5: Setup input and output for XSLT transformation
            // Setup input stream
            Source src = new StreamSource(new File("src/fop/fonts.fo"));

            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Step 6: Start XSLT transformation and FOP processing
            transformer.transform(src, res);


            byte[] bytes = ous.toByteArray();
            return bytes;

        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
        return null;
    }
}
