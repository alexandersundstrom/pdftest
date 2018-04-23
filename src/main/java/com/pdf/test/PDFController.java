package com.pdf.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PDFController {

    @RequestMapping("/pdf")
    public String getHello() {
        return "Hi";
    }
}
