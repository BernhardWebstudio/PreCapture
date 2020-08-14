/**
 * HelpFrame.java
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


import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import edu.harvard.mcz.precapture.PreCaptureApp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;

/**
 * UI Display of information about the application.
 *
 *
 */
public class AboutFrame extends JFrame {
    private static final long serialVersionUID = 6681758841979694874L;
    private static final Log log = LogFactory.getLog(AboutFrame.class);
    private JPanel contentPane;
    private JFrame frame;
    private JTextField textFieldVersion;
    private JTextField textFieldSVN;
    private JTextField textFieldAuthors;
    private JTextField textFieldLicense;
    private JTextField textFieldCopyright;
    private JTextField textFieldName;
    private JTextArea textAreaHistory;

    /**
     * Default no argument constructor, constructs a new AboutFrame instance.
     */
    public AboutFrame() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(AboutFrame.class.getResource("/edu/harvard/mcz/precapture/resources/icon.png")));
        setTitle("About");
        frame = this;
        init();
        this.pack();
    }

    private void init() {
        JPanel panel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        getContentPane().add(panel, BorderLayout.SOUTH);

        JButton btnNewButton = new JButton("Close");
        btnNewButton.setMnemonic(KeyEvent.VK_C);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });
        btnNewButton.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(btnNewButton);

        JPanel panel_1 = new JPanel();
        panel_1.setMaximumSize(new Dimension(900, 900));
        getContentPane().add(panel_1, BorderLayout.CENTER);
        panel_1.setLayout(new FormLayout(new ColumnSpec[]{
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("max(250dlu;pref):grow"),},
                new RowSpec[]{
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("default:grow"),
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.MIN_ROWSPEC,}));

        JLabel lblName = new JLabel("Name");
        panel_1.add(lblName, "2, 2, right, default");

        textFieldName = new JTextField(PreCaptureApp.NAME);
        textFieldName.setEditable(false);
        panel_1.add(textFieldName, "4, 2, fill, default");
        textFieldName.setColumns(10);

        JLabel lblVersion = new JLabel("Version");
        panel_1.add(lblVersion, "2, 4, right, default");

        textFieldVersion = new JTextField(PreCaptureApp.VERSION);
        textFieldVersion.setEditable(false);
        panel_1.add(textFieldVersion, "4, 4, fill, default");
        textFieldVersion.setColumns(10);

        JLabel lblSvn = new JLabel("SVN ID");
        panel_1.add(lblSvn, "2, 6, right, default");

        textFieldSVN = new JTextField(PreCaptureApp.SVN_ID);
        textFieldSVN.setEditable(false);
        panel_1.add(textFieldSVN, "4, 6, fill, default");
        textFieldSVN.setColumns(10);

        JLabel lblAuthors = new JLabel("Authors");
        panel_1.add(lblAuthors, "2, 8, right, default");

        textFieldAuthors = new JTextField(PreCaptureApp.AUTHORS);
        textFieldAuthors.setEditable(false);
        panel_1.add(textFieldAuthors, "4, 8, fill, default");
        textFieldAuthors.setColumns(10);

        JLabel lblCopyright = new JLabel("Copyright");
        panel_1.add(lblCopyright, "2, 10, right, default");

        textFieldCopyright = new JTextField(PreCaptureApp.COPYRIGHT);
        textFieldCopyright.setEditable(false);
        panel_1.add(textFieldCopyright, "4, 10, fill, default");
        textFieldCopyright.setColumns(10);

        JLabel lblLicense = new JLabel("License");
        panel_1.add(lblLicense, "2, 12, right, default");

        textFieldLicense = new JTextField(PreCaptureApp.LICENSE);
        textFieldLicense.setEditable(false);
        panel_1.add(textFieldLicense, "4, 12, fill, default");
        textFieldLicense.setColumns(10);

        URL helpURL = PreCaptureApp.class.getResource("resources/gpl-2.0.txt");

        JLabel lblLibraries = new JLabel("Libraries");
        panel_1.add(lblLibraries, "2, 14, right, default");

        JTextArea textAreaLibraries = new JTextArea();
        textAreaLibraries.setEditable(false);
        textAreaLibraries.setLineWrap(true);
        textAreaLibraries.setWrapStyleWord(true);
        textAreaLibraries.setText(PreCaptureApp.LIBRARIES);
        textAreaLibraries.setRows(3);
        panel_1.add(textAreaLibraries, "4, 14, fill, fill");

        JLabel lblHistory = new JLabel("History");
        panel_1.add(lblHistory, "2, 16, right, default");

        textAreaHistory = new JTextArea();
        textAreaHistory.setEditable(false);
        textAreaHistory.setLineWrap(true);
        textAreaHistory.setWrapStyleWord(true);
        textAreaHistory.setText(PreCaptureApp.NARATIVE);
        textAreaHistory.setRows(4);
        panel_1.add(textAreaHistory, "4, 16, fill, default");
        textAreaHistory.setColumns(10);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setMinimumSize(new Dimension(600, 400));
        scrollPane.setMaximumSize(new Dimension(600, 600));
        panel_1.add(scrollPane, "2, 18, 3, 1, fill, top");

        JEditorPane editorPane;
        try {
            editorPane = new JEditorPane(helpURL);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            editorPane = new JEditorPane();
        }
        scrollPane.setViewportView(editorPane);

    }

}
