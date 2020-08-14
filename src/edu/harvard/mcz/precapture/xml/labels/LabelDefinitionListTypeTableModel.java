/**
 * LabelDefinitionListTypeTableModel.java
 * edu.harvard.mcz.precapture.xml.labels
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
package edu.harvard.mcz.precapture.xml.labels;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.table.AbstractTableModel;

/**
 *
 */
public class LabelDefinitionListTypeTableModel extends AbstractTableModel {
    private static final Log log =
            LogFactory.getLog(LabelDefinitionListTypeTableModel.class);

    private LabelDefinitionListType definitionList;

    /**
     * Default no argument constructor, constructs a new
     * LabelDefinitionListTypeTableModel instance.
     */
    public LabelDefinitionListTypeTableModel() {
    }

    /**
     * Constructor that takes a LabelDefinitionList as a parameter.
     *
     * @param labelDefinitionListType
     */
    public LabelDefinitionListTypeTableModel(
            LabelDefinitionListType labelDefinitionListType) {
        definitionList = labelDefinitionListType;
    }

    public int getRowCount() {
        return definitionList.getLabelDefinition().size();
    }

    public int getColumnCount() {
        return 5;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object result = null;
        if (definitionList != null) {
            if (getRowCount() > 0) {
                try {
                    switch (columnIndex) {
                        case 0:
                            result =
                                    definitionList.getLabelDefinition().get(rowIndex).getTitle();
                            break;
                        case 1:
                            result =
                                    definitionList.getLabelDefinition().get(rowIndex).getColumns();
                            break;
                        case 2:
                            result = definitionList.getLabelDefinition()
                                    .get(rowIndex)
                                    .getLabelHeight();
                            break;
                        case 3:
                            result = definitionList.getLabelDefinition()
                                    .get(rowIndex)
                                    .getLabelWidth();
                            break;
                        case 4:
                            result = definitionList.getLabelDefinition()
                                    .get(rowIndex)
                                    .getFontDelta();
                            break;
                    }
                } catch (Exception e) {
                    // asked for something that isn't there
                }
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int columnIndex) {
        String result = super.getColumnName(columnIndex);
        switch (columnIndex) {
            case 0:
                result = "Print Format";
                break;
            case 1:
                result = "Columns";
                break;
            case 2:
                result = "Label Height";
                break;
            case 3:
                result = "Label Width";
                break;
            case 4:
                result = "Font size change";
                break;
        }
        return result;
    }
}
