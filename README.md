# PDF Test
Spring Boot REST API that generates PDF using two different JAVA APIs.

#### Endpoints
http://localhost:8080/pdf/box Creating an pdf using PDF Box from Apache. <br>
http://localhost:8080/pdf/fop Creating an pdf using FOP from Apache.

#### Tests
There are two tests: <br>
* PDFServiceTest
* FOPServiceTest

Both checks that the text is present and
follow the layout expected with wrapping, as well as that the signature is added. <br>
FOPServiceTest is using the PDDocument to read from the byte[], that way there is no need to create a file on the system. <br>

### Pros and Cons

###### PDFBOX PRO
* Simple to start, as you could use JAVA to generate the content in the PDF.


###### PDFBOX CON 
* Does not have built in support for linebreaks, and centering text etc. Had to build helper methods for this

###### FOP PRO
* Using an template for the content, and placeholders to replace values dynamically
* Support for adding margins, centering text etx, seems like support for basic css

###### FOP CON
* The intial setup took more time as templates needs to be created, and figuring out how to load them and replace placeholder.