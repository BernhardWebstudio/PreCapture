/**
 * ContainerLabel.java
 * edu.harvard.mcz.precapture.ui
 * Copyright © 2012 President and Fellows of Harvard College
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
 * Author: mole
 */
package edu.harvard.mcz.precapture.ui;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import edu.harvard.mcz.precapture.PreCaptureProperties;
import edu.harvard.mcz.precapture.PreCaptureSingleton;
import edu.harvard.mcz.precapture.xml.Field;
import edu.harvard.mcz.precapture.xml.labels.LabelDefinitionType;
import edu.harvard.mcz.precapture.xml.labels.TextOrentationType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;

/**
 * The list of fields that go with a single label for a container (a folder,
 * or unit tray or other container).
 */
public class ContainerLabel {
    private static final Log log = LogFactory.getLog(ContainerLabel.class);
    //private final int fontIncrement = -1; //traditional value
    //private final int fontIncrement = 1; //latest value
    private final int fontIncrement = -1; //font minus 2 THIS ONE ROD LIKES!!!
    private final int lineSpacing = 2;
    private int numberToPrint = 0;
    private ArrayList<FieldPlusText> fields;
    //private final int fontIncrement = 0; //font minus 1
    //private FontFamily fontFamily = Font.FontFamily.TIMES_ROMAN;
    private FontFamily fontFamily = Font.FontFamily.HELVETICA;
	
	/*private Font fontNormal = new Font(Font.FontFamily.TIMES_ROMAN, 
 		   fields.get(i).getField().getFontSize() + printDefinition.getFontDelta(), 
 		   Font.ITALIC);*/

    /**
     * Default no argument constructor, constructs a new ContainerLabel instance.
     */
    public ContainerLabel() {
        numberToPrint = 1;
        this.fields = new ArrayList<FieldPlusText>();

    }


    public ContainerLabel(ArrayList<FieldPlusText> fields) {
        numberToPrint = 1;
        this.fields = fields;
        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).getField().getDefaultValue().size() == 1) {
                fields.get(i).getTextField().setText(fields.get(i).getField().getDefaultValue().get(0));
            }
            if (fields.get(i).getField().getVocabularyTerm().equals("dwc:collectionCode") &&
                    PreCaptureSingleton.getInstance().getProperties().getProperties().getProperty(PreCaptureProperties.KEY_MY_COLLECTION_CODE).length() > 0) {
                fields.get(i).getTextField().setText(PreCaptureSingleton.getInstance().getProperties().getProperties().getProperty(PreCaptureProperties.KEY_MY_COLLECTION_CODE));
            }
        }
    }

    public ContainerLabel(ArrayList<FieldPlusText> fields, boolean setDefaults) {
        numberToPrint = 1;
        this.fields = fields;
        if (setDefaults) {
            for (int i = 0; i < fields.size(); i++) {
                if (fields.get(i).getField().getDefaultValue().size() == 1) {
                    fields.get(i).getTextField().setText(fields.get(i).getField().getDefaultValue().get(0));
                }
                if (fields.get(i).getField().getVocabularyTerm().equals("dwc:collectionCode") &&
                        PreCaptureSingleton.getInstance().getProperties().getProperties().getProperty(PreCaptureProperties.KEY_MY_COLLECTION_CODE).length() > 0) {
                    fields.get(i).getTextField().setText(PreCaptureSingleton.getInstance().getProperties().getProperties().getProperty(PreCaptureProperties.KEY_MY_COLLECTION_CODE));
                }
            }
        }
    }


    /**
     * @return the fields
     */
    public ArrayList<FieldPlusText> getFields() {
        return fields;
    }


    /**
     * @return the numberToPrint
     */
    public int getNumberToPrint() {
        return numberToPrint;
    }


    /**
     * Set how many copies of this label to print.
     *
     * @param numberToPrint the numberToPrint to set
     */
    public void setNumberToPrint(int numberToPrint) {
        this.numberToPrint = numberToPrint;
    }

    /**
     * Resets number to print to 1 and content of each text field to an empty string.
     */
    public void resetToBlank() {
        numberToPrint = 1;
        for (int i = 0; i < fields.size(); i++) {
            // Set default value for fields that have just one default.
            if (fields.get(i).getField().getDefaultValue().size() == 1) {
                if (fields.get(i).getField().getDefaultValue().get(0).length() > 0) {
                    fields.get(i).getTextField().setText(fields.get(i).getField().getDefaultValue().get(0));
                }
            }
            // TODO: Make user configurable.
            //if (!fields.get(i).getField().getVocabularyTerm().equals("dwc:genus")) {
            fields.get(i).getTextField().setText("");
            //}
            if (fields.get(i).getField().getVocabularyTerm().equals("dwc:collectionCode") &&
                    PreCaptureSingleton.getInstance().getProperties().getProperties().getProperty(PreCaptureProperties.KEY_MY_COLLECTION_CODE).length() > 0) {
                fields.get(i).getTextField().setText(PreCaptureSingleton.getInstance().getProperties().getProperties().getProperty(PreCaptureProperties.KEY_MY_COLLECTION_CODE));
            }
        }
    }

    /**
     * Get a fragmentary JSON encoding of the project and version of the precapture field
     * configuration used to encode the field names in JSON.
     *
     * @return a JSON represenation of the metadata.
     */
    public String metadataToJSON() {
        // RFC4627 allows space after the value separator character (and the other
        // structural characters), but we'll leave spaces out to reduce the number
        // of characters involved in the QRcode.
        StringBuffer data = new StringBuffer();
        // m for metadata
        // 1p for project (actually a list of projects, thus square brackets).
        // 2v for version.
        // numbers are included to make it less likely that there will be a
        // collision with field names.
        data.append("\"m1p\":\"").append(PreCaptureSingleton.getInstance().getMappingList().getSupportedProject()).append("\",");
        String content = PreCaptureSingleton.getInstance().getMappingList().getVersion();
        if (content == null || content.equals("null")) {
            // null is a special case that isn't quoted
            data.append("\"m2v\":").append(content);
        } else {
            data.append("\"m2v\":\"").append(content).append("\"");
        }
        return data.toString();
    }

    /**
     * @return a JSON representation of the keys and values of the fields in this set.
     */
    public String toJSON() {

        StringBuffer data = new StringBuffer();
        data.append("{");
        data.append(metadataToJSON());
        String comma = "";
        if (metadataToJSON().length() > 0) {
            // don't add a leading comma if
            comma = ",";
        }
        for (int i = 0; i < fields.size(); i++) {
            if (!fields.get(i).getTextField().getText().isEmpty()) {
                data.append(comma);
                String content = fields.get(i).getTextField().getText();
                content = content.replace("\"", "\\\""); // escape quotation marks occurring in the content.
                if (content.equals("null")) {
                    // null is a special case that isn't quoted.
                    data.append("\"").append(fields.get(i).getField().getCode()).append("\":").append(content);
                } else {
                    data.append("\"").append(fields.get(i).getField().getCode()).append("\":\"").append(content).append("\"");
                }
                comma = ",";
            }
        }
        data.append("}");

        return data.toString();

    }

    public String toString() {
        StringBuffer data = new StringBuffer();
        for (int i = 0; i < fields.size(); i++) {
            if (!fields.get(i).getTextField().getText().isEmpty()) {
                data.append(fields.get(i).getTextField().getText()).append(" ");
            }
        }
        return data.toString();
    }

    private boolean hasTextWithFieldVocabularyTerm(String vocabTerm) {
        for (int i = 0; i < fields.size(); i++) {
            Field tmpField = fields.get(i).getField();
            if (tmpField.getVocabularyTerm().equals(vocabTerm)) {
                return !fields.get(i).getTextField().getText().trim().equalsIgnoreCase("");
            }
        }
        return false;
    }

    /**
     * @return a PDF paragraph cell containing a text encoding of the fields in this set.
     */
    public PdfPCell toPDFCell(LabelDefinitionType printDefinition) {
        PdfPCell cell = new PdfPCell();
        if (printDefinition.getTextOrentation().toString().toLowerCase().equals(TextOrentationType.VERTICAL.toString().toLowerCase())) {
            log.debug("Print orientation of text is Vertical");
            cell.setRotation(90);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        }
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
        cell.disableBorderSide(PdfPCell.RIGHT);
        cell.setPaddingLeft(3);
        cell.setNoWrap(false);

        int leading = (int) (fields.get(0).getField().getFontSize() + printDefinition.getFontDelta() + fontIncrement) - 1;
        Paragraph higher = new Paragraph(leading, "", new Font(fontFamily,
                fields.get(0).getField().getFontSize() + printDefinition.getFontDelta() + fontIncrement,
                Font.NORMAL));
        higher.setSpacingBefore(lineSpacing);
        higher.setSpacingAfter(lineSpacing);

        boolean added = false;
        boolean hasContent = false;
        for (int i = 0; i < fields.size(); i++) {
            log.debug("Handling field #" + i);
            if (fields.get(i).getField().isNewLine() || (i == fields.size() - 1)) {
                if (!higher.isEmpty()) {
                    log.debug("Higher has content: " + higher.getContent());
                    cell.addElement(higher);
                }
                leading = (int) (fields.get(i).getField().getFontSize() + printDefinition.getFontDelta() + fontIncrement) - 1;
                higher = new Paragraph(leading, "", new Font(fontFamily,
                        fields.get(i).getField().getFontSize() + printDefinition.getFontDelta() + fontIncrement,
                        Font.NORMAL));
                higher.setSpacingBefore(lineSpacing);
                higher.setSpacingAfter(lineSpacing);
                added = false;
                hasContent = false;
            }

            String text = fields.get(i).getTextField().getText().trim();
            log.debug("Text of field #" + i + ": " + text);

//            if (!text.equals("")
//                    && !fields.get(i).getField().getVocabularyTerm().equals("dwc:specificEpithet")
//                    && !fields.get(i).getField().getVocabularyTerm().equals("dwc:subspecificEpithet")
//                    && !fields.get(i).getField().getVocabularyTerm().equals("dwc:infraspecificRank")
//                    && !fields.get(i).getField().getVocabularyTerm().equals("dwc:infraspecificEpithet")) {
//                text = StringUtils.capitalise(text);
//            }

            Chunk chunk = new Chunk(text);
            if ((fields.get(i).getField().getVocabularyTerm().equals("dwc:specificEpithet") && text.equals("sp."))) {
                chunk.setFont(new Font(fontFamily,
                        fields.get(i).getField().getFontSize() + printDefinition.getFontDelta() + fontIncrement,
                        Font.NORMAL));
            } else if (fields.get(i).getField().isUseItalic()) {
                chunk.setFont(new Font(fontFamily,
                        fields.get(i).getField().getFontSize() + printDefinition.getFontDelta() + fontIncrement,
                        Font.ITALIC));
            } else {
                chunk.setFont(new Font(fontFamily,
                        fields.get(i).getField().getFontSize() + printDefinition.getFontDelta() + fontIncrement,
                        Font.NORMAL));
            }
            if (!chunk.isEmpty()) {
                hasContent = true;
                higher.setSpacingBefore(lineSpacing);
                higher.setSpacingAfter(lineSpacing);
                higher.add(chunk);
                String suffix = fields.get(i).getField().getSuffix();
                String suffixCondition = fields.get(i).getField().getSuffixCondition();
                log.debug("Field " + i + " has suffix: " + suffix);
                if (suffix != null && suffix.length() > 0 && (suffixCondition == null || this.hasTextWithFieldVocabularyTerm(suffixCondition))) {
                    higher.add(new Chunk(fields.get(i).getField().getSuffix()));
                }
                if (fields.get(i).getTextField().getText().trim().length() > 0) {
                    // add a trailing space as a separator if there was something to separate.
                    higher.add(new Chunk(" "));
                }
            }
        }
        if (!added) {
            log.debug("Not added. Higher content: " + higher.getContent());
            cell.addElement(higher);
        }
        log.debug("Assembled text: " + higher.toString());
		/*String extraText = PreCaptureSingleton.getInstance().getProperties().getProperties().getProperty(PreCaptureProperties.KEY_EXTRAHUMANTEXT);
		if (extraText!=null && extraText.length()>0) { 
			log.debug(extraText);
		    cell.addElement(new Chunk(extraText));
		}*/

        return cell;
    }

    protected ContainerLabel clone() {
        ContainerLabel result = new ContainerLabel();
        result.setNumberToPrint(numberToPrint);
        for (int i = 0; i < this.fields.size(); i++) {
            result.getFields().add(getFields().get(i).clone());
        }
        return result;
    }


}
