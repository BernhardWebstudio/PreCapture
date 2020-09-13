/**
 * UnitTrayLabelComboBoxModel.java
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

import edu.harvard.mcz.precapture.data.UnitTrayLabel;
import edu.harvard.mcz.precapture.data.UnitTrayLabelLifeCycle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class UnitTrayLabelComboBoxModel implements ComboBoxModel {
    private static final Log log =
            LogFactory.getLog(UnitTrayLabelComboBoxModel.class);

    ArrayList<UnitTrayLabel> model;
    ArrayList<ListDataListener> dataListeners;
    UnitTrayLabel selectedItem = null;

    /**
     * Default no argument constructor, constructs a new
     * UnitTrayLabelComboBoxModel instance.
     */
    public UnitTrayLabelComboBoxModel() {
        log.debug("invoked UnitTrayLabelComboBoxModel()");
        model = new ArrayList<UnitTrayLabel>();
        // add a blank row
        model.add(new UnitTrayLabel());
        dataListeners = new ArrayList<ListDataListener>();
    }

    /**
     * Construct a new UnitTrayLabelComboBoxModel from a list of UnitTrayLabels.
     *
     * @param unitTrayLabelList
     */
    public UnitTrayLabelComboBoxModel(List<UnitTrayLabel> unitTrayLabelList) {
        log.debug("invoked UnitTrayLabelComboBoxModel(list unit tray label)");
        model = (ArrayList<UnitTrayLabel>) unitTrayLabelList;
        if (model == null) {
            model = new ArrayList<UnitTrayLabel>();
            // add a blank row.
            model.add(0, new UnitTrayLabel());
        }
        dataListeners = new ArrayList<ListDataListener>();
        log.debug("done: UnitTrayLabelComboBoxModel(list unit tray label)");
        // if (model.size()>0) {
        //	selectedItem = model.get(0);
        //}
    }

    public UnitTrayLabelComboBoxModel(List<UnitTrayLabel> unitTrayLabelList,
                                      boolean selectFirst) {
        log.debug("invoked UnitTrayLabelComboBoxModel(list unit tray label, selectFirst)");
        model = (ArrayList<UnitTrayLabel>) unitTrayLabelList;
        if (model == null) {
            model = new ArrayList<UnitTrayLabel>();
        }
        // add a blank row.
        model.add(0, new UnitTrayLabel());
        dataListeners = new ArrayList<ListDataListener>();
        if (selectFirst && model.size() > 0) {
            selectedItem = model.get(0);
        }
    }

    /**
     * Add a unit tray label to the model.
     *
     * @param label the label to add.
     */
    public void addElement(UnitTrayLabel label) {
        model.add(label);
    }

    /**
     * @return a copy of the model.
     */
    public List<UnitTrayLabel> getModel() {
        return (List<UnitTrayLabel>) model.clone();
    }

    public int getSize() {
        return model.size();
    }

    public Object getElementAt(int index) {
        if (model.get(index) != null) {
            return model.get(index).getScientificName();
        } else {
            return null;
        }
    }

    /**
     * For dealing directly with the UnitTrayLabel objects in the model.
     *
     * @param index
     * @return
     */
    public UnitTrayLabel getDataElementAt(int index) {
        return model.get(index);
    }

    public void addListDataListener(ListDataListener l) {
        dataListeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        dataListeners.remove(l);
    }

    public Object getSelectedItem() {
        if (selectedItem != null) {
            return selectedItem.getScientificName();
        }
        return null;
    }

    public void setSelectedItem(Object anItem) {
        Iterator<UnitTrayLabel> i = model.iterator();
        boolean done = false;
        while (i.hasNext() && !done) {
            UnitTrayLabel label = i.next();
            if (UnitTrayLabelLifeCycle.getScientificName(label).equals(anItem)) {
                selectedItem = label;
                done = true;
            }
        }
    }

    public UnitTrayLabel getSelectedContainerLabel() {
        return selectedItem;
    }

    /**
     *
     */
    public void removeAllElements() {
        model = new ArrayList<UnitTrayLabel>();
    }
}
