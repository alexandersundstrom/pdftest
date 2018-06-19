package com.pdf.test.service;

import com.pdf.test.Database.SignatureRepository;
import com.pdf.test.model.Person;
import com.pdf.test.model.Signature;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class FOPService {

    @Autowired
    SignatureRepository repository;

    public byte[] generatePDFBytes(Person person) {
        try (ByteArrayOutputStream ous = new ByteArrayOutputStream()) {

            FopFactory fopFactory = FopFactory.newInstance(new File("src/main/resources/xconf/configuration.xconf"));
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, ous);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Signature signature = repository.findAll().iterator().next();
            String base64Result = Base64.getEncoder().encodeToString(signature.getSignature().getBytes(1, (int) signature.getSignature().length()));

            Map<String, String> map = new HashMap<>();
            map.put("@name", person.getFirstName() + " " + person.getLastName());
            map.put("@age", String.valueOf(person.getAge()));
            map.put("@married", String.valueOf(person.isMarried()));
            map.put("@extra", "Shields up. Boldly, modern space suits virtually lower an evasive, post-apocalyptic machine. All hands view, devastation!");
            map.put("@signatureBase64", base64Result);

            StringBuilder templateBuilder = readTemplate("templates/pdf/test.xml", false);
            String xml = replaceVariables(map, templateBuilder.toString());
            Source src = new StreamSource(new StringReader(xml));
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);

            byte[] bytes = ous.toByteArray();
            return bytes;

        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
        return null;
    }

    private StringBuilder readTemplate(String templatePath, boolean newLine) {

        ClassPathResource classPathResource = new ClassPathResource(templatePath);

        StringBuilder template = new StringBuilder();
        try (InputStream inputStream = classPathResource.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                template.append(line);
                if (newLine) {
                    template.append("\n");
                }
            }
        } catch (Exception e) {

        }
        return template;
    }

    public String replaceVariables(Map<String, String> params, String template) {
        for (Map.Entry<String, String> e : params.entrySet()) {
            String value = e.getValue();
            if (value == null) value = "";
            template = template.replace(e.getKey(), value);
        }
        return template;
    }
}
