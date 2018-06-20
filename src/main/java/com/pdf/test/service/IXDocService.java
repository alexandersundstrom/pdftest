package com.pdf.test.service;

import com.pdf.test.model.Person;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ByteArrayImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.NullImageBehaviour;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class IXDocService {

    public byte[] generatePDFBytes(Person person) throws Exception {

        InputStream in = new FileInputStream(new File("src/main/resources/templates/pdf/pdf.odt"));

        // create report
        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in,TemplateEngineKind.Freemarker);

        // options for converting
        IContext context = report.createContext();
        Options options = Options.getTo(ConverterTypeTo.PDF);
        context.put("person", person);
        context.put("interests", person.getInterests());

        String profilePath = "src/main/resources/images/" + person.getImagePath();

        try (InputStream inputStream = new FileInputStream(new File(profilePath))) {
            IImageProvider companyLogoImage = getiImageProvider(IOUtils.toByteArray(inputStream), 100);
            context.put("profilePicture", companyLogoImage);
        } catch (IOException e) {
            System.out.println("Could not load image logo, will not use in pdf template");
        }

        FieldsMetadata metadata = new FieldsMetadata();
        metadata.addFieldAsImage("profilePicture");
        metadata.setBehaviour(NullImageBehaviour.RemoveImageTemplate);

        metadata.addFieldAsList("interests");
        report.setFieldsMetadata(metadata);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            report.convert(context, options, out);
            return out.toByteArray();
        } catch (Exception e) {
            throw e;
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    private IImageProvider getiImageProvider(byte[] signature, float width) {
        IImageProvider profilePicture = new ByteArrayImageProvider(signature);
        profilePicture.setWidth(width);
        profilePicture.setUseImageSize(true);
        profilePicture.setResize(true);
        return profilePicture;
    }
}
