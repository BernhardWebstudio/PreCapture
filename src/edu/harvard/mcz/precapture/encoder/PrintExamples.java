/**
 * PrintExamples.java
 * edu.harvard.mcz.precapture.encoder
 */
package edu.harvard.mcz.precapture.encoder;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPrintPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.awt.print.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Examples of PDF printing
 */
public class PrintExamples {
    private static final Log log = LogFactory.getLog(PrintExamples.class);

    /**
     * Default no argument constructor, constructs a new PrintExamples instance.
     */
    public PrintExamples() {

    }

    private void printTest() {
        // from http://www.coderanch.com/t/410208/java/java/java-printing-printing-pdf
        // adapted from java print api example
        FileInputStream psStream = null;
        try {
            psStream = new FileInputStream("some.pdf");
        } catch (FileNotFoundException ffne) {
            ffne.printStackTrace();
        }
        if (psStream == null) {
            return;
        }
        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc myDoc = new SimpleDoc(psStream, psInFormat, null);
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);

        // this step is necessary because I have several printers configured
        PrintService myPrinter = null;
        for (int i = 0; i < services.length; i++) {
            String svcName = services[i].toString();
            System.out.println("service found: " + svcName);
            if (svcName.contains("printer closest to me")) {
                myPrinter = services[i];
                System.out.println("my printer found: " + svcName);
                break;
            }
        }

        if (myPrinter != null) {
            DocPrintJob job = myPrinter.createPrintJob();
            try {
                job.print(myDoc, aset);

            } catch (Exception pe) {
                pe.printStackTrace();
            }
        } else {
            System.out.println("no printer services found");
        }
    }

    private void examplePrint() {
        // Example of printing with pdf-renderer
        // From: http://lynema.org/2010/12/29/printing-a-pdf-in-java-with-pdfrenderer
        File f = null;
        RandomAccessFile fis = null;
        FileChannel fc = null;
        ByteBuffer bb = null;
        try {
            String printer = "YOUR_PRINTER_NAME";

            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            PrintService[] services = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.PDF, aset);
            log.debug(services[0].getName());

            PrintService printService = services[0];

            f = new File("filename");
            //Read only access would work too
            fis = new RandomAccessFile(f, "r");
            fc = fis.getChannel();
            bb = ByteBuffer.allocate((int) fc.size());
            fc.read(bb);

            //Do not map the file to a ByteBuffer as the examples show.
            // There is a reason why in java bug #474038
            // http://bugs.sun.com/view_bug.do?bug_id=4724038
            //fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size());
            //bb = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size());

            PDFFile pdfFile = new PDFFile(bb); // Create PDF Print Page
            PDFPrintPage pages = new PDFPrintPage(pdfFile);
            // Create Print Job
            PrinterJob pjob = PrinterJob.getPrinterJob();
            pjob.setPrintService(printService);

            PageFormat pf = PrinterJob.getPrinterJob().defaultPage();

            pf.setOrientation(PageFormat.PORTRAIT);

            Paper paper = new Paper();

            //This is to fix an error in PDF-Renderer
            //View http://juixe.com/techknow/index.php/2008/01/17/print-a-pdf-document-in-java/ for details
            //Printing a PDF is also possible by sending the bytes directly to the printer, but
            //  the printer would have to support it.

            paper.setImageableArea(0, 0, paper.getWidth() * 2, paper.getHeight());

            pf.setPaper(paper);

            pjob.setJobName(f.getName());

            Book book = new Book();
            book.append(pages, pf, pdfFile.getNumPages());
            pjob.setPageable(book);
            pjob.print();

        } catch (FileNotFoundException e) {
            //do your error action
        } catch (IOException e) {
            //do your error action
        } catch (PrinterException e) {
            //do your error action
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                    fc = null;
                }
            } catch (IOException e) {
                log.error(e);
                //handle error here
            }
            try {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
            } catch (IOException e) {
                //handle error here
            }
            if (bb != null) {
                bb.clear();
            }
        }
    }

}
