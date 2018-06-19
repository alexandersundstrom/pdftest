# PDF Test
Spring Boot REST API that generates PDF using two different JAVA APIs, PDFBox And FOP.

For this to work you need to have an local mysql database running, and a table called signature based on the Signature.java model

For the FOP API I choosed to generate the signature from an blob in the database. In my case I have a local MYSQL database.

Once that is in place and tha application is started, you can access
http://localhost:8080/pdf/addBlob to add an entry in the database, which
is needed for the pdf/fop endpoint.

#### Endpoints
http://localhost:8080/pdf/box Creating an pdf using PDF Box from Apache. <br>
http://localhost:8080/pdf/fop Creating an pdf using FOP from Apache.<br>
http://localhost:8080/pdf/ixdoc Creating an pdf using XDocreport


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

###### XDoc PRO

* The freedom of using a  odt (Open Office document) template to generate the content with placeholder, makes it very flexible, and easy to setup.
* Support for iterations of contents from lists, and images etc.

###### XDoc CON

* Requires some trial and error to get the right content in the pdf when adding list and images, but it's pretty straight forward 