/**
 * PreCaptureSingleton.java
 * edu.harvard.mcz.precapture
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
package edu.harvard.mcz.precapture;

import edu.harvard.mcz.precapture.ui.ContainerListTableModel;
import edu.harvard.mcz.precapture.xml.MappingList;
import edu.harvard.mcz.precapture.xml.labels.LabelDefinitionListType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 */
public class PreCaptureSingleton {
    private static final Log log = LogFactory.getLog(PreCaptureSingleton.class);
    // Eagerly create an instance to let the JVM create the unique instance
    // during class load, this prevents the creation of multiple instances
    // in different threads.
    private static PreCaptureSingleton uniqueInstance = new PreCaptureSingleton();
    private PreCaptureProperties properties;
    private MappingList mappingList;
    private ContainerListTableModel currentLabelList;
    private LabelDefinitionListType printFormatDefintionList;
    private SplashScreen splashScreen;

    /**
     * Default no argument constructor is private to implement Singleton pattern.
     */
    private PreCaptureSingleton() {
    }

    /**
     * Point of access for the singleton instance.
     *
     * Example:
     * PreCaptureSingleton.getInstance().setProperties(new
     * PreCaptureProperties());
     *
     * @return the unique instance of the singleton.
     */
    public static PreCaptureSingleton getInstance() {
        return uniqueInstance;
    }

    /**
     * Whether or not the napkin Look and Feel should be used.
     *
     * @return
     */
    public static boolean useNapkin() {
        // Set to true to use the Napkin Look and Feel
        return false;
    }

    /**
     * @return the properties
     */
    public PreCaptureProperties getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(PreCaptureProperties properties) {
        this.properties = properties;
    }

    /**
     * @return the mappingList
     */
    public MappingList getMappingList() {
        return mappingList;
    }

    /**
     * @param mappingList the mappingList to set
     */
    public void setMappingList(MappingList mappingList) {
        this.mappingList = mappingList;
    }

    /**
     * @return the currentLabelList
     */
    public ContainerListTableModel getCurrentLabelList() {
        return currentLabelList;
    }

    /**
     * @param currentLabelList the currentLabelList to set
     */
    public void setCurrentLabelList(ContainerListTableModel currentLabelList) {
        this.currentLabelList = currentLabelList;
    }

    public LabelDefinitionListType getPrintFormatDefinitionList() {
        return this.printFormatDefintionList;
    }

    /**
     * @param printFormatDefintionList
     */
    public void setPrintFormatDefinitionList(
            LabelDefinitionListType printFormatDefintionList) {
        this.printFormatDefintionList = printFormatDefintionList;
    }

    /**
     * @return the splashScreen
     */
    public SplashScreen getSplashScreen() {
        return splashScreen;
    }

    /**
     * @param splashScreen the splashScreen to set
     */
    public void setSplashScreen(SplashScreen splashScreen) {
        this.splashScreen = splashScreen;
    }
}
