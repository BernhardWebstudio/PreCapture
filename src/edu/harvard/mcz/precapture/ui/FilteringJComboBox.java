/**
 * FilteringJComboBox.java
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

import edu.harvard.mcz.precapture.PreCaptureProperties;
import edu.harvard.mcz.precapture.PreCaptureSingleton;
import edu.harvard.mcz.precapture.data.UnitTrayLabel;
import edu.harvard.mcz.precapture.data.UnitTrayLabelLifeCycle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 */
public class FilteringJComboBox extends JComboBox implements FocusListener {
    private static final long serialVersionUID = -7988464282872345110L;
    private static final Log log = LogFactory.getLog(FilteringJComboBox.class);

    private String familyLimit;
    private String genusLimit;
    private UnitTrayLabelComboBoxModel initialModel;

    /**
     * Default no argument constructor, constructs a new FilteringJComboBox instance.
     */
    public FilteringJComboBox() {
        UnitTrayLabelLifeCycle uls = new UnitTrayLabelLifeCycle();
        initialModel = new UnitTrayLabelComboBoxModel(uls.findAll());
        this.setModel(initialModel);
        init();
    }

    /**
     * @param model
     */
    public FilteringJComboBox(UnitTrayLabelComboBoxModel model) {
        super(model);
        init();
    }

    private void init() {
        familyLimit = "";
        genusLimit = "";
        // listen for loss of focus on the text field
        this.getEditor().getEditorComponent().addFocusListener(this);
        this.setEditable(true);
        final JTextField textfield = (JTextField) this.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent keyEvent) {
                log.debug(keyEvent);
                // some keys for which we do not want to filter anew
                int[] excludedKeys = new int[]{KeyEvent.VK_CONTEXT_MENU,
                        KeyEvent.VK_KP_DOWN, KeyEvent.VK_KP_UP, KeyEvent.VK_KP_RIGHT, KeyEvent.VK_KP_LEFT,
                        KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_PAGE_UP,
                        KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT,
                        KeyEvent.VK_ESCAPE};
                if (IntStream.of(excludedKeys).anyMatch(x -> keyEvent.getKeyCode() == x)) {
                    log.debug("Not filtering due to key: " + keyEvent.getKeyCode());
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            filter(textfield.getText(), true);
                        }
                    });
                }
            }
        });

    }

    public void setUTLModel(UnitTrayLabelComboBoxModel model) {
        super.setModel(model);
    }

    public void resetFilter(boolean changePopupState) {
        log.debug("Resetting filter...");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                filter(null, changePopupState);
                log.debug("Filter reset.");
            }
        });
    }

    protected void filter(String enteredText, boolean changePopupState) {
        // first, setup the list to filter from
        UnitTrayLabelComboBoxModel nextModel = this.initialModel;
        if (enteredText == null || enteredText.length() == 0) {
            // If entry is blank, show full list.
            // otherwise, fetch the list via example
            // performance improvement: memoize family & genus -> no db access if still same
            UnitTrayLabelLifeCycle uls = new UnitTrayLabelLifeCycle();
            if ((familyLimit == null || familyLimit.length() == 0) && (genusLimit == null || genusLimit.length() == 0)) {
                log.debug("No need to filter initial model");
            } else {
                // Filter by family/genus.
                UnitTrayLabel pattern = new UnitTrayLabel();
                if (familyLimit != null && familyLimit.length() > 0) {
                    pattern.setFamily(familyLimit);
                }
                if (genusLimit != null && genusLimit.length() > 0) {
                    pattern.setGenus(genusLimit);
                }
                nextModel = new UnitTrayLabelComboBoxModel(uls.findByExample(pattern));
            }
        }
        if (!changePopupState) {
            this.firePopupMenuCanceled();
        }
        if (changePopupState && !this.isPopupVisible()) {
            this.showPopup();
        }

        // then, actually do filter
        int lengthThreshold = Integer.valueOf(PreCaptureSingleton.getInstance().getProperties().getProperties().getProperty(PreCaptureProperties.KEY_FILTER_LENGTH_THRESHOLD));
        if (enteredText != null && enteredText.length() >= lengthThreshold) {
            log.debug("Filtering on '" + enteredText + "'");

            boolean isExactMatch = false;
            List<UnitTrayLabel> arrayWithFilter = new ArrayList<>();
            // filter
            for (int i = 0; i < nextModel.getSize(); i++) {
                if (nextModel.getDataElementAt(i).toString().toLowerCase()
                        .contains(enteredText.toLowerCase())) {
                    arrayWithFilter.add(nextModel.getDataElementAt(i));
                }
            }
            log.debug("Filtered down to " + arrayWithFilter.size() + " entities.");
            UnitTrayLabelComboBoxModel currentModel = (UnitTrayLabelComboBoxModel) this.getModel();
            currentModel.removeAllElements();
            for (UnitTrayLabel label : arrayWithFilter) {
                currentModel.addElement(label);
                if (label.toString().equalsIgnoreCase(enteredText)) {
                    isExactMatch = true;
                    this.getModel().setSelectedItem(label);
                }
            }
            final JTextField textField = (JTextField) this.getEditor().getEditorComponent();
            textField.setText(enteredText);

            if (changePopupState) {
                this.hidePopup();
                if (isExactMatch) {
                    super.firePopupMenuCanceled();
                } else {
                    this.showPopup();
                }
            }
        }
        log.debug("Filtering done.");
    }

    public void focusGained(FocusEvent e) {
        super.getModel().setSelectedItem("");
        JTextField textfield = (JTextField) this.getEditor().getEditorComponent();
        textfield.setText("");
    }

    public void focusLost(FocusEvent e) {
        // when focus is lost on the text field (editor box part of the combo box),
        // set the value of the text field to the selected item on the list, if any.
        log.debug(e.toString());
        if (super.getModel().getSelectedItem() != null) {
            JTextField textfield = (JTextField) this.getEditor().getEditorComponent();
            textfield.setText(super.getModel().getSelectedItem().toString());
        }
    }

    /**
     * Sets the familial filter limit criterion on the picklist.
     *
     * @param selectedFamily the family to limit the picklist to.
     */
    public void setFamilyLimit(Object selectedFamily) {
        if (selectedFamily != null && selectedFamily.toString().length() > 0) {
            this.familyLimit = selectedFamily.toString();
            this.genusLimit = null;
        } else {
            selectedFamily = null;
        }
        resetFilter(false);
    }

    /**
     * Sets the generic filter limit criterion on the picklist.
     *
     * @param selectedGenus the genus to limit the picklist to.
     */
    public void setGenusLimit(Object selectedGenus) {
        if (selectedGenus != null && selectedGenus.toString().length() > 0) {
            this.genusLimit = selectedGenus.toString();
            this.familyLimit = null;
        } else {
            genusLimit = null;
        }
        resetFilter(false);
    }

}
