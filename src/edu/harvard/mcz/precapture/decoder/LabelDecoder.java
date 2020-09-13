/**
 * LabelDecoder.java
 * edu.harvard.mcz.precapture.decoder
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
package edu.harvard.mcz.precapture.decoder;

import boofcv.abst.fiducial.QrCodeDetector;
import boofcv.alg.fiducial.qrcode.QrCode;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import edu.harvard.mcz.precapture.PreCaptureSingleton;
import edu.harvard.mcz.precapture.exceptions.BarcodeReadException;
import edu.harvard.mcz.precapture.ui.ContainerLabel;
import edu.harvard.mcz.precapture.ui.FieldPlusText;
import edu.harvard.mcz.precapture.utils.GZipCompressor;
import edu.harvard.mcz.precapture.xml.Field;
import edu.harvard.mcz.precapture.xml.MappingList;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

/**
 *
 */
public class LabelDecoder {

    private static final Log log = LogFactory.getLog(LabelDecoder.class);

    private ContainerLabel label;

    /**
     * Default no argument constructor.
     */
    public LabelDecoder() {

    }

    /**
     * LabelDecoder for a particular container label.
     * <p>
     * No supporting functionality implemented yet.
     * Use the static decodeImage and decodeImageToResult methods.
     * <p>
     * May be removed.
     *
     * @param containerLabel
     */
    public LabelDecoder(ContainerLabel containerLabel) {
        // TODO: Implement or remove.
        this.label = containerLabel;
    }

    public static Map<String, String> getVocabularyValueMap(String jsonString) {
        HashMap<String, String> result = new HashMap<String, String>();
        JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonString);
        String project = (String) json.get("m1p");
        String version = (String) json.get("m2v");

        boolean foundMapping = false;
        // Check if current mapping list is the one used in this json
        MappingList mapping = PreCaptureSingleton.getInstance().getMappingList();
        List<String> projects = mapping.getSupportedProject();
        Iterator<String> i = projects.iterator();
        while (!foundMapping && i.hasNext()) {
            if (i.next().equals(project) && mapping.getVersion().equals(version)) {
                foundMapping = true;
            }
        }
        //TODO Find other mappings

        if (foundMapping) {
            Iterator keys = json.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String term = getTermForKey(mapping, key);
                String value = (String) json.get(key);
                log.debug(key + ":" + term + ":" + value);
                result.put(term, value);
            }
        }
        return result;
    }

    /**
     * Given a string containing JSON, construct a ContanierLabel object
     *
     * @param jsonString
     * @return
     */
    public static ContainerLabel createLabelFromJson(String jsonString) {
        ContainerLabel result = null;
        JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonString);
        String project = (String) json.get("m1p");
        String version = (String) json.get("m2v");
        log.debug("Project: " + project);
        log.debug("Version: " + version);

        boolean foundMapping = false;
        // Check if current mapping list is the one used in this json
        MappingList mapping = PreCaptureSingleton.getInstance().getMappingList();
        List<String> projects = mapping.getSupportedProject();
        Iterator<String> i = projects.iterator();
        while (!foundMapping && i.hasNext()) {
            String projectInMapping = i.next();
            if (
                    (projectInMapping.equals(project) || project.equals("[" + projectInMapping + "]"))
                            && mapping.getVersion().equals(version)
            ) {
                foundMapping = true;
            }
        }
        //TODO Find other mappings

        if (foundMapping) {
            ArrayList<FieldPlusText> textFields = new ArrayList<FieldPlusText>();
            int fieldCount = PreCaptureSingleton.getInstance().getMappingList().getFieldInList().size();
            for (int j = 0; j < fieldCount; j++) {
                textFields.add(new FieldPlusText(PreCaptureSingleton.getInstance().getMappingList().getFieldInList().get(j), new JTextField()));
            }
            result = new ContainerLabel(textFields);
            Iterator keys = json.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String term = getTermForKey(mapping, key);
                String value = (String) json.get(key);

                ArrayList<FieldPlusText> fields = result.getFields();
                Iterator<FieldPlusText> f = fields.iterator();
                while (f.hasNext()) {
                    FieldPlusText field = f.next();
                    if (field.getField().getVocabularyTerm().equals(term)) {
                        field.getTextField().setText(value);
                    }
                }
                log.debug(key + ":" + value);
            }
        } else {
            log.error("Failed to find a configured mapping in JSON, check project (" + project + ") and version (" + version + ").");
            log.debug("Supported Version: " + mapping.getVersion());
            log.debug("Supported Projects:");
            i = projects.iterator();
            while (i.hasNext()) {
                log.debug(i.next());
            }
        }

        return result;
    }

    /**
     * Given a mapping list and a key, return the vocabulary term for that key
     *
     * @param mappings the mapping definition to check for the code and vocabulary term.
     * @param key      the short code used in the JSON as a key for a field concept.
     * @return string value of the vocabulary term that the code translates to.
     */
    public static String getTermForKey(MappingList mappings, String key) {
        String result = null;
        List<Field> fields = mappings.getFieldInList();
        Iterator<Field> i = fields.iterator();
        while (i.hasNext()) {
            Field field = i.next();
            if (field.getCode().equals(key)) {
                result = field.getVocabularyTerm();
            }
        }
        return result;
    }

    /**
     * Given an image, returns the decoded text of a QRCode within that image.
     *
     * @param image a bufferedimage containing a QRCode barcode
     * @return the text encoded in the QRCode.
     * @throws BarcodeReadException if unable to read the barcode, recasts 4 different
     *                              of ZXing exceptions as common BarcodeReadException.
     */
    public static String decodeImage(BufferedImage image) throws BarcodeReadException {
        String result = null;
        GrayU8 gray = ConvertBufferedImage.convertFrom(image, (GrayU8) null);

        QrCodeDetector<GrayU8> detector = FactoryFiducial.qrcode(null, GrayU8.class);

        detector.process(gray);

        // Get's a list of all the qr codes it could successfully detect and decode
        List<QrCode> detections = detector.getDetections();
        log.debug("BoofCV QRScanner found " + detections.size() + " barcodes. Failures: " + detector.getFailures().size());
        if (detections.size() != 1) {
            throw new BarcodeReadException("Error reading barcode: found " + detections.size() + " instead of 1 barcode.");
        }
        byte[] dataBytes = detections.get(0).corrected;
        if (GZipCompressor.isCompressed(dataBytes)) {
            try {
                // try decompress
                result = GZipCompressor.decompress(dataBytes);
            } catch (IOException e) {
                throw new BarcodeReadException("Error reading barcode. IOException. " + e.getMessage());
            }
        } else {
            result = detections.get(0).message;
        }
        return result;
    }


}
