# PDF Test
Spring Boot REST API that generates PDF using two different JAVA APIs.

#### Endpoints
/pdfbox Creating an pdf using PDF Box from Apache. 
/pdffop Creating an pdf using FOP from Apache.

#### Tests
PDFServiceTest - For PDFBox, we check that the text is present and
follow the layout expected with wrapping, as well as that the signature is added.
