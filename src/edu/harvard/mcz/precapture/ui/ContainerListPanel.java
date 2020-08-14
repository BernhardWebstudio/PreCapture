/**
 * ContainerListPanel.java
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

import edu.harvard.mcz.precapture.PreCaptureSingleton;
import edu.harvard.mcz.precapture.encoder.LabelEncoder;
import edu.harvard.mcz.precapture.exceptions.PrintFailedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 */
public class ContainerListPanel extends JPanel {
    private static final Log log = LogFactory.getLog(ContainerListPanel.class);

    private JTable tablePrintList;
    private JFrame parent;
    private JPopupMenu popupMenu;
    private int clickedOnRow;

    public ContainerListPanel(JFrame parent) {
        this.parent = parent;
        init();
    }

    private void init() {
        setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        tablePrintList = new JTable();
        tablePrintList.setModel(
                PreCaptureSingleton.getInstance().getCurrentLabelList());
        tablePrintList.setAutoCreateRowSorter(true);
        tablePrintList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    clickedOnRow = ((JTable) e.getComponent()).getSelectedRow();
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    clickedOnRow = ((JTable) e.getComponent()).getSelectedRow();
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        scrollPane.setViewportView(tablePrintList);

        JPanel panel_6 = new JPanel();
        FlowLayout flowLayout_1 = (FlowLayout) panel_6.getLayout();
        flowLayout_1.setAlignment(FlowLayout.RIGHT);
        add(panel_6, BorderLayout.SOUTH);

        JButton btnPrint_1 = new JButton("Print");
        panel_6.add(btnPrint_1);
        btnPrint_1.setMnemonic(KeyEvent.VK_P);
        btnPrint_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    LabelEncoder.printList(
                            PreCaptureSingleton.getInstance()
                                    .getCurrentLabelList()
                                    .getSortedLabels(tablePrintList.getRowSorter()));
                } catch (PrintFailedException e1) {
                    log.error(e1.getMessage());
                    JOptionPane.showMessageDialog(parent,
                            "Failed to print. " + e1.getMessage());
                }
            }
        });

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"Yes, clear list.", "Cancel."};
                int n = JOptionPane.showOptionDialog(
                        parent, "Are you sure you want to clear the print list?",
                        "Confirm clear", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                if (n == 0) {
                    PreCaptureSingleton.getInstance().getCurrentLabelList().clearList();
                }
            }
        });
        panel_6.add(btnClear);

        popupMenu = new JPopupMenu();

        JMenuItem mntmCloneRow = new JMenuItem("Clone Row");
        mntmCloneRow.setMnemonic(KeyEvent.VK_C);
        mntmCloneRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (clickedOnRow < 0) {
                        ((ContainerListTableModel) tablePrintList.getModel()).addRow();
                    } else {
                        ((ContainerListTableModel) tablePrintList.getModel())
                                .cloneRow(clickedOnRow);
                    }
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    JOptionPane.showMessageDialog(
                            parent, "Failed to clone an Inventory row. " + ex.getMessage());
                }
            }
        });
        popupMenu.add(mntmCloneRow);

        JMenuItem mntmDeleteRow = new JMenuItem("Delete Row");
        mntmDeleteRow.setMnemonic(KeyEvent.VK_D);
        mntmDeleteRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (clickedOnRow >= 0) {
                        ((ContainerListTableModel) tablePrintList.getModel())
                                .deleteRow(clickedOnRow);
                    }
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    JOptionPane.showMessageDialog(
                            parent, "Failed to delete an Inventory row. " + ex.getMessage());
                }
            }
        });
        popupMenu.add(mntmDeleteRow);

        JMenuItem mntmAddRow = new JMenuItem("Add Row");
        mntmDeleteRow.setMnemonic(KeyEvent.VK_A);
        mntmAddRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ((ContainerListTableModel) tablePrintList.getModel()).addRow();
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    JOptionPane.showMessageDialog(
                            parent, "Failed to add an Inventory row. " + ex.getMessage());
                }
            }
        });
        popupMenu.add(mntmAddRow);
    }
}
