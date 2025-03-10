/**
 * LabelEncoder.java
 * edu.harvard.mcz.precapture.encoder
 * Copyright © 2009 President and Fellows of Harvard College
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Author: Paul J. Morris
 */
package edu.harvard.mcz.precapture.encoder;

import boofcv.alg.fiducial.qrcode.QrCode;
import boofcv.alg.fiducial.qrcode.QrCodeEncoder;
import boofcv.alg.fiducial.qrcode.QrCodeGeneratorImage;
import boofcv.io.image.ConvertBufferedImage;
import com.google.zxing.WriterException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import edu.harvard.mcz.precapture.PreCaptureProperties;
import edu.harvard.mcz.precapture.PreCaptureSingleton;
import edu.harvard.mcz.precapture.exceptions.BarcodeCreationException;
import edu.harvard.mcz.precapture.exceptions.PrintFailedException;
import edu.harvard.mcz.precapture.ui.ContainerLabel;
import edu.harvard.mcz.precapture.xml.labels.LabelDefinitionListType;
import edu.harvard.mcz.precapture.xml.labels.LabelDefinitionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * LabelEncoder
 *
 * @author Paul J. Morris
 */
public class LabelEncoder {

    private static final Log log = LogFactory.getLog(LabelEncoder.class);

    private ContainerLabel label;

    public LabelEncoder(ContainerLabel containerLabel) {
        label = containerLabel;
    }

//	/** Test fields for darwin core concepts that should be italicized in presentation.
//	 * @deprecated moved to configuration, IsItalic.
//	 * @param aField field to test.
//	 * @return true if field vocabularyTerm is one that should be italicized.
//	 * 
//	 */
//	public static boolean useItalic(Field aField) { 
//		boolean result = false;
//		if (aField.getVocabularyTerm().equals("dwc:genus")) { result = true; } 		
//		if (aField.getVocabularyTerm().equals("dwc:specificEpithet")) { result = true; } 		
//		if (aField.getVocabularyTerm().equals("dwc:infraspecificEpithet")) { result = true; } 		
//		return result;
//	}

    /**
     * @param containers
     * @return
     * @throws PrintFailedException
     */
    public static boolean printAnnotationLabels(ArrayList<ContainerLabel> containers) throws PrintFailedException {
        // TODO Refactor code from BarcodeParserPanel print action into here.
        return false;
    }

    @SuppressWarnings("hiding")
    public static boolean printList(ArrayList<ContainerLabel> containers) throws PrintFailedException {
        log.debug("Invoked printList ");
        boolean result = false;
        ContainerLabel label = new ContainerLabel();
        if (containers.isEmpty()) {
            log.debug("No labels to print.");
        } else {
            LabelDefinitionType printDefinition = null;
            LabelDefinitionListType printDefs = PreCaptureSingleton.getInstance().getPrintFormatDefinitionList();
            List<LabelDefinitionType> printDefList = printDefs.getLabelDefinition();
            Iterator<LabelDefinitionType> il = printDefList.iterator();
            while (il.hasNext()) {
                LabelDefinitionType def = il.next();
                if (def.getTitle().equals(PreCaptureSingleton.getInstance().getProperties().getProperties().getProperty(PreCaptureProperties.KEY_SELECTED_PRINT_DEFINITION))) {
                    printDefinition = def;
                }
            }
            if (printDefinition == null) {
                log.error("No selected print format defintion found.");
                //TODO change from message to error handling dialog that allows picking a print format.
                JOptionPane.showMessageDialog(null, "Unable to print.  No print format is selected.");
            } else {

                log.debug(printDefinition.getTitle());
                log.debug(printDefinition.getTextOrentation().toString());

                LabelEncoder encoder = new LabelEncoder(containers.get(0));
                try {
                    Image image = encoder.getImage();
                    Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream(PreCaptureSingleton.getInstance().getProperties().getProperties().getProperty(PreCaptureProperties.KEY_LABELPRINTFILE)));
                    // Convert units in print definition to points (72 points/inch, 28.346456 points/cm)

                    int paperWidthPoints = 612;  // 8.5"
                    int paperHeightPoints = 792;  // 11"
                    int marginsPoints = 36; // 0.5"
                    int labelWidthPoints = 540;  // 7.5"
                    int labelHeightPoints = 720; // 10"
                    int numColumns = 1;   // goes with above

                    numColumns = printDefinition.getColumns();
                    float relWidthTextCell = printDefinition.getRelWidthTextCell();
                    float relWidthBarcodeCell = printDefinition.getRelWidthBarcodeCell();
                    log.debug("relWidthTextCell = " + relWidthTextCell);
                    log.debug("relWidthBarcodeCell = " + relWidthBarcodeCell);

                    if (printDefinition.getUnits().toString().toLowerCase().equals("inches")) {
                        paperWidthPoints = (int) Math.floor(printDefinition.getPaperWidth() * 72f);
                        paperHeightPoints = (int) Math.floor(printDefinition.getPaperHeight() * 72f);
                        marginsPoints = (int) Math.floor(printDefinition.getMargins() * 72f);
                        labelWidthPoints = (int) Math.floor(printDefinition.getLabelWidth() * 72f);
                        labelHeightPoints = (int) Math.floor(printDefinition.getLabelHeight() * 72f);
                    }
                    if (printDefinition.getUnits().toString().toLowerCase().equals("cm")) {
                        paperWidthPoints = (int) Math.floor(printDefinition.getPaperWidth() * 28.346456f);
                        paperHeightPoints = (int) Math.floor(printDefinition.getPaperHeight() * 28.346456f);
                        marginsPoints = (int) Math.floor(printDefinition.getMargins() * 28.346456f);
                        labelWidthPoints = (int) Math.floor(printDefinition.getLabelWidth() * 28.346456f);
                        labelHeightPoints = (int) Math.floor(printDefinition.getLabelHeight() * 28.346456f);
                    }
                    if (printDefinition.getUnits().toString().toLowerCase().equals("points")) {
                        paperWidthPoints = (int) Math.floor(printDefinition.getPaperWidth() * 1f);
                        paperHeightPoints = (int) Math.floor(printDefinition.getPaperHeight() * 1f);
                        marginsPoints = (int) Math.floor(printDefinition.getMargins() * 1f);
                        labelWidthPoints = (int) Math.floor(printDefinition.getLabelWidth() * 1f);
                        labelHeightPoints = (int) Math.floor(printDefinition.getLabelHeight() * 1f);
                    }

                    if (paperWidthPoints == 612 && paperHeightPoints == 792) {
                        document.setPageSize(PageSize.LETTER);
                    } else {
                        document.setPageSize(new Rectangle(paperWidthPoints, paperHeightPoints));
                    }
                    document.setMargins(marginsPoints, marginsPoints, marginsPoints, marginsPoints);
                    document.open();

                    // Sanity check
                    if (paperWidthPoints <= 0) {
                        paperWidthPoints = 612;
                    }
                    if (paperHeightPoints <= 0) {
                        paperHeightPoints = 792;
                    }
                    if (marginsPoints < 0) {
                        marginsPoints = 0;
                    }
                    if (labelWidthPoints <= 0) {
                        labelWidthPoints = 540;
                    }
                    if (labelHeightPoints <= 0) {
                        labelHeightPoints = 720;
                    }
                    if (paperWidthPoints + (marginsPoints * 2) < labelWidthPoints) {
                        labelWidthPoints = paperWidthPoints + (marginsPoints * 2);
                        log.debug("Adjusting label width to fit printable page width");
                    }
                    if (paperHeightPoints + (marginsPoints * 2) < labelHeightPoints) {
                        labelHeightPoints = paperHeightPoints + (marginsPoints * 2);
                        log.debug("Adjusting label height to fit printable page height");
                    }

                    // calculate how many columns will fit on the paper.
                    int columns = (int) Math.floor((paperWidthPoints - (marginsPoints * 2)) / labelWidthPoints);
                    // if specified column count is smaller, use the specified.
                    if (numColumns < columns) {
                        columns = numColumns;
                        log.debug("Fewer columns specified in definition than will fit on page, using specified column count of " + numColumns);
                    }

                    // define two table cells per column, one for text one for barcode.
                    int subCellColumnCount = columns * 2;

                    // set the table, with an absolute width and relative widths of the cells in the table;
                    PdfPTable table = setupTable(paperWidthPoints, marginsPoints, labelWidthPoints, columns, subCellColumnCount, relWidthTextCell, relWidthBarcodeCell);
                    // figure out the width of the cells containing the barcodes.
                    float ratio = relWidthBarcodeCell / (relWidthBarcodeCell + relWidthTextCell);
                    float barcodeCellWidthPoints = (float) Math.floor(labelWidthPoints * ratio);
                    log.debug("Width of barcode cell in points: " + barcodeCellWidthPoints);

                    //Rectangle pageSizeRectangle = new Rectangle(paperWidthPoints, paperHeightPoints);
                    //table.setWidthPercentage(cellWidthsPoints, pageSizeRectangle);
                    //table.setTotalWidth(cellWidthsPoints);

                    // Calculate how many cells fit on a page (two cells per label).
                    int labelsPerColumn = (int) Math.floor((paperHeightPoints - (marginsPoints * 2)) / labelHeightPoints);
                    int cellsPerPage = subCellColumnCount * labelsPerColumn;
                    log.debug("Labels per column = " + labelsPerColumn);
                    log.debug("Cells per page = " + cellsPerPage);

                    Iterator<ContainerLabel> iterLabels = containers.iterator();

                    int cellCounter = 0;  // counts number of cells filled on a page.
                    int counter = 0;      // counts number of pre capture label data rows to print (each of which may request more than one copy).

                    // TODO: Doesn't fit on page.

                    while (iterLabels.hasNext()) {
                        // Loop through all of the container labels found to print
                        label = iterLabels.next();
                        if (label != null) {
                            log.debug(label);
                            log.debug("Label: " + counter + " " + label.toString());
                            for (int toPrint = 0; toPrint < label.getNumberToPrint(); toPrint++) {
                                // For each container label, loop through the number of requested copies
                                // Generate a text and a barcode cell for each, and add to array for page
                                int toPrintPlus = toPrint + 1;  // for pretty counter in log.
                                log.debug("Copy " + toPrintPlus + " of " + label.getNumberToPrint());

                                PdfPCell cell = label.toPDFCell(printDefinition);
                                cell.setFixedHeight(labelHeightPoints);
                                // Colors to illustrate where the cells are on the layout
                                if (PreCaptureSingleton.getInstance().getProperties().getProperties().getProperty(PreCaptureProperties.KEY_DEBUGLABEL).equals("true")) {
                                    cell.setBackgroundColor(new BaseColor(255, 255, 30));
                                }

                                PdfPCell cell_barcode = new PdfPCell();
                                cell_barcode.setBorderColor(BaseColor.LIGHT_GRAY);
                                cell_barcode.disableBorderSide(PdfPCell.LEFT);
                                cell_barcode.setVerticalAlignment(PdfPCell.ALIGN_TOP);
                                cell_barcode.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                cell_barcode.setFixedHeight(labelHeightPoints);
                                if (PreCaptureSingleton.getInstance().getProperties().getProperties().getProperty(PreCaptureProperties.KEY_DEBUGLABEL).equals("true")) {
                                    cell_barcode.setBackgroundColor(new BaseColor(255, 30, 255));
                                }

                                encoder = new LabelEncoder(label);
                                image = encoder.getImage();
                                image.setAlignment(Image.ALIGN_TOP);
                                //image.setAlignment(Image.ALIGN_LEFT);
                                image.setAlignment(Image.ALIGN_RIGHT);
                                image.scaleToFit(barcodeCellWidthPoints, labelHeightPoints);
                                cell_barcode.addElement(image);

                                table.addCell(cell);
                                table.addCell(cell_barcode);

                                cellCounter = cellCounter + 2;  // we've added two cells to the page (two cells per label).
                                log.debug("Cells " + cellCounter + " of " + cellsPerPage + " cells per page.");

                                // If we have hit a full set of labels for the page, add them to the document
                                // in each column, filling left to right
                                if (cellCounter >= cellsPerPage - 1) {
                                    log.debug("Page is full");
                                    log.debug("Table has " + table.getNumberOfColumns() + " columns and " + table.getRows().size() + " rows ");
                                    // Reset to begin next page
                                    cellCounter = 0;
                                    table.setLockedWidth(true);
                                    document.add(table);
                                    log.debug("Adding new page");
                                    document.newPage();
                                    table = setupTable(paperWidthPoints, marginsPoints, labelWidthPoints, columns, subCellColumnCount, relWidthTextCell, relWidthBarcodeCell);
                                    log.debug("Setup new table");
                                }
                            } // end loop through toPrint (for a taxon/precapture label data row)
                            counter++;  // Increment number of pre capture label data rows.
                        } // end if not null label
                    } // end while results has next (for all taxa requested)
                    // get any remaining cells in pairs
                    if (cellCounter > 0) {
                        log.debug("Adding remaining cells in partial page");
                        if (cellCounter <= cellsPerPage) {
                            for (int i = cellCounter; i <= cellsPerPage; i++) {
                                PdfPCell emptyCell = new PdfPCell();
                                emptyCell.setBorder(PdfPCell.NO_BORDER);
                                table.addCell(emptyCell);
                            }
                        }
                        log.debug("Table has " + table.getNumberOfColumns() + " columns and " + table.getRows().size() + " rows ");
                        table.setLockedWidth(true);
                        document.add(table);
                    }
                    document.close();

                    // send to printer
                    PrintingUtility.sendPDFToPrinter(printDefinition, paperWidthPoints, paperHeightPoints);

                    // Check to see if there was content in the document.
                    // Printed to pdf ok.
                    result = counter != 0;
                } catch (FileNotFoundException e) {
                    log.debug(e.getMessage(), e);
                    throw new PrintFailedException("File not found.");
                } catch (DocumentException e) {
                    log.error(e.getMessage(), e);
                    throw new PrintFailedException("Error building/printing PDF document. " + e.getMessage());
                } catch (OutOfMemoryError e) {
                    System.out.println("Out of memory error. " + e.getMessage());
                    System.out.println("Failed.  Too many labels.");
                    throw new PrintFailedException("Ran out of memory, too many labels at once.");
                } catch (BarcodeCreationException e) {
                    System.out.println("BarcodeCreationException. " + e.getMessage());
                    System.out.println("Failed.  Couldn't create barcode.");
                    throw new PrintFailedException("Unable to create barcode.  Probably too many characters to encode. " + e.getMessage());
                }
            }
            log.debug("printList Done. Success = " + result);
        }
        return result;
    }

    public static PdfPTable setupTable(int paperWidthPoints, int marginsPoints,
                                       int labelWidthPoints, int columns, int subCellColumnCount,
                                       float relWidthTextCell, float relWidthBarcodeCell
    ) throws DocumentException {
        PdfPTable table = new PdfPTable(subCellColumnCount);
        table.setLockedWidth(true);   // force use of totalWidth in points, rather than percentWidth.
        float percentWidth = ((((float) paperWidthPoints) - (2f * ((float) marginsPoints))) / ((float) paperWidthPoints)) * 100f;
        //percentWidth = 100f;
        log.debug("Table Width Percent = " + percentWidth);
        table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
        float[] cellWidthsRatio = new float[subCellColumnCount];
        int cellNumber = 0;
        for (int c = 0; c < columns; c++) {
            cellWidthsRatio[cellNumber] = relWidthTextCell; // width of text cell
            cellNumber++;
            if (subCellColumnCount > 1) {
                cellWidthsRatio[cellNumber] = relWidthBarcodeCell; // width of barcode cell
                cellNumber++;
            }
        }
        table.setTotalWidth(paperWidthPoints - 2 * marginsPoints);
        // must set total width before setting relative cell widths.
        table.setWidths(cellWidthsRatio);
        log.debug("Width:" + table.getTotalWidth());
        return table;
    }

    public static BufferedImage resizeImage(java.awt.Image originalImage, int newWidth, int newHeight) {
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = scaledImage.createGraphics();
        //graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        graphics.dispose();
        return scaledImage;
    }

    public static BufferedImage resizeImage(java.awt.Image originalImage, int newWidth, int newHeight, int padding) {
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = scaledImage.createGraphics();
        //graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        graphics.dispose();
        BufferedImage scaledImage2 = new BufferedImage(newWidth + 4, newHeight + 4, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics2 = scaledImage2.createGraphics();
        graphics2.setBackground(Color.WHITE);
        graphics2.setColor(Color.WHITE);
        graphics2.fillRect(0, 0, newWidth + padding, newHeight + padding);
        graphics2.drawImage(scaledImage, padding / 2, padding / 2, newWidth, newHeight, null);
        graphics2.dispose();
        return scaledImage2;
    }

    /**
     * Get the BitMatrix for the QR Code
     *
     * @return
     * @throws WriterException
     */
    private QrCode getQRCodeMatrix() throws WriterException {
        // turn into qr code
        QrCodeEncoder qr_writer = new QrCodeEncoder();
        String correctionLevel = PreCaptureSingleton.getInstance().getProperties().getProperties().getProperty(PreCaptureProperties.KEY_QRCODEECLEVEL, "M");
        switch (correctionLevel) {
            case "H":
                qr_writer.setError(QrCode.ErrorLevel.H); //30% loss, 174 char at level 10
                break;
            case "Q":
                qr_writer.setError(QrCode.ErrorLevel.Q); //25% loss, 221 char at level 10
                break;
            case "L":
                qr_writer.setError(QrCode.ErrorLevel.L); //15% loss, 311 char at level 10
                break;
            case "M":
            default:
                qr_writer.setError(QrCode.ErrorLevel.M); //7% loss, 395 char at level 10
                break;
        }
        String data = label.toJSON();

        byte[] compressedStrBytes = data.getBytes(StandardCharsets.UTF_8);
        // compress message
//        try {
//            compressedStrBytes = GZipCompressor.compress(data);
//        } catch (IOException e) {
//            // fallback to uncompressed
//            log.error("Failed to compress: " + e.getMessage());
//            log.error(e);
//        }
        // add the bytes to the QR Code
        qr_writer.addBytes(compressedStrBytes);

        return qr_writer.fixate();
    }

    public BufferedImage getBufferedImage() throws WriterException {
        QrCodeGeneratorImage render = new QrCodeGeneratorImage(20);

        render.render(getQRCodeMatrix());

        // Convert it to a BufferedImage for display purposes
        return ConvertBufferedImage.convertTo(render.getGray(), null);
    }

    public Image getImage() throws BarcodeCreationException {
        Image image = null;
        try {
            BufferedImage bufferedImage = getBufferedImage();
            image = Image.getInstance(bufferedImage, null);
            //image.setDpi(300, 300);
        } catch (WriterException e) {
            throw new BarcodeCreationException(e.getMessage());
        } catch (BadElementException e) {
            throw new BarcodeCreationException(e.getMessage());
        } catch (IOException e) {
            throw new BarcodeCreationException(e.getMessage());
        }
        return image;
    }

}
