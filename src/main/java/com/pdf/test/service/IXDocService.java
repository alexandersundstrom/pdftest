package com.pdf.test.service;

import com.pdf.test.model.Person;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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
}
