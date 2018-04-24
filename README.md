# PDF Test
Spring Boot REST API that generates PDF using two different JAVA APIs.

#### Endpoints
/pdfbox Creating an pdf using PDF Box from Apache. Contains information for a mocked person as well as a signature image

#### Tests
PDFServiceTest - For PDFBox, we check that the text is present and
follow the layout expected with wrapping, as well as that the signature is added.
