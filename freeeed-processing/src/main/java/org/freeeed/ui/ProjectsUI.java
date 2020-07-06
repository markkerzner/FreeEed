/*
 *
 * Copyright SHMsoft, Inc. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.freeeed.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import org.freeeed.db.DbLocalUtils;
import org.freeeed.services.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mark
 */
public class ProjectsUI extends javax.swing.JDialog {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectsUI.class);
    private static final String[] columns = new String[]{
        "Project ID", "Name", "Date created"
    };
    private Map<Integer, Project> projects = null;

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;

    /**
     * Creates new form ProjectsUI
     *
     * @param parent
     * @param modal
     */
    public ProjectsUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = rootPane.getActionMap();
        actionMap.put(cancelName, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        newButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        projectScrollPane = new javax.swing.JScrollPane();
        projectTable = new javax.swing.JTable();
        editProjectButton = new javax.swing.JButton();

        setTitle("FreeEed projects");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setText("Select");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        newButton.setText("New");
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        projectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Project ID", "Name", "Date created"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        projectTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                projectTableMouseClicked(evt);
            }
        });
        projectTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                projectTableKeyPressed(evt);
            }
        });
        projectScrollPane.setViewportView(projectTable);

        editProjectButton.setText("Edit");
        editProjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProjectButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(projectScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editProjectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(projectScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton)
                    .addComponent(newButton)
                    .addComponent(deleteButton)
                    .addComponent(editProjectButton))
                .addContainerGap())
        );

        rootPane.setDefaultButton(okButton);
        rootPane.setDefaultButton(okButton);
        rootPane.setDefaultButton(okButton);
        rootPane.setDefaultButton(okButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        try {
            openProject();
        } catch (Exception e) {
            LOGGER.error("Problem opening project", e);
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        try {
            newProject();
        } catch (Exception e) {
            LOGGER.error("Problem creating new project", e);
        }
    }//GEN-LAST:event_newButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        try {
            deleteProject();
        } catch (Exception e) {
            LOGGER.error("Problem deleting project", e);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void projectTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_projectTableKeyPressed
        try {
            openWithKeyPress(evt);
        } catch (Exception e) {
            LOGGER.error("Problem opening project", e);
        }
    }//GEN-LAST:event_projectTableKeyPressed

    private void projectTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_projectTableMouseClicked
        try {
            openWithDoubleClick(evt);
        } catch (Exception e) {
            LOGGER.error("Problem opening project", e);
        }
    }//GEN-LAST:event_projectTableMouseClicked
    private void editProjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editProjectButtonActionPerformed
        try {
            openProjectForEditing();
        } catch (Exception e) {
            LOGGER.error("Problem opening project for editing");
        }        
    }//GEN-LAST:event_editProjectButtonActionPerformed
    
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editProjectButton;
    private javax.swing.JButton newButton;
    private javax.swing.JButton okButton;
    private javax.swing.JScrollPane projectScrollPane;
    private javax.swing.JTable projectTable;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;
    
    private void showProjectTableData() throws Exception {
        projectTable.setModel(new DefaultTableModel(getProjectTableData(), columns) {
            Class[] types = new Class[]{
                String.class, String.class, String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false
            };
            
            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
    }
    
    @Override
    public void setVisible(boolean b) {
        try {
            if (b) {
                myInit();
            }
            super.setVisible(b);
        } catch (Exception e) {
            LOGGER.error("Internal db problem", e);
            JOptionPane.showMessageDialog(getParent(),
                    "Problem with internal database", "Sorry", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void myInit() throws Exception {
        showProjectTableData();
    }
    
    private Object[][] getProjectTableData() throws Exception {
        projects = DbLocalUtils.getProjects();
        Set<Integer> keys = projects.keySet();
        List<Integer> list = new ArrayList(keys);
        Collections.sort(list);
        Object[][] data = new Object[projects.size()][3];
        int row = 0;
        for (int projectId : list) {
            Project project = projects.get(projectId);
            data[row][0] = projectId;
            data[row][1] = project.getProjectName();
            data[row][2] = project.getCreated();
            row++;
        }
        return data;
    }
    
    private void openProjectForEditing() throws Exception {
        int row = projectTable.getSelectedRow();
        if (row >= 0) {
            int projectId = (Integer) projectTable.getValueAt(row, 0);
            Project project = projects.get(projectId);
            Project.setCurrentProject(project);
            LOGGER.debug("Opening project {}", projectId);
            doClose(RET_OK);
            FreeEedUI.getInstance().showProcessingOptions();
        }
    }
    
    private void openProject() throws Exception {
        int row = projectTable.getSelectedRow();
        if (row >= 0) {
            int projectId = (Integer) projectTable.getValueAt(row, 0);
            Project project = projects.get(projectId);
            Project.setCurrentProject(project);
            LOGGER.debug("Opening project {}", projectId);
            doClose(RET_OK);
            FreeEedUI.getInstance().updateTitle(project.getProjectName());
        }
    }
    
    private void openWithKeyPress(KeyEvent evt) throws Exception {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            openProject();
        }
    }
    
    private void openWithDoubleClick(MouseEvent evt) throws Exception {
        if (evt.getClickCount() == 2) {
            openProjectForEditing();
        }
    }
    
    private void deleteProject() throws Exception {
        int row = projectTable.getSelectedRow();
        if (row >= 0) {
            int projectId = (Integer) projectTable.getValueAt(row, 0);
            int retStatus = JOptionPane.showConfirmDialog(this, "Delete project " + projectId + "?");
            if (retStatus == JOptionPane.OK_OPTION) {
                DbLocalUtils.deleteProject(projectId);
                showProjectTableData();
            }
            LOGGER.debug("Deleted project {}", projectId);
        }
    }
    
    private void newProject() throws Exception {
        Project project = DbLocalUtils.createNewProject();
        Project.setCurrentProject(project);
        LOGGER.debug("Opening project {}", project.getProjectCode());
        doClose(RET_OK);
        FreeEedUI.getInstance().showProcessingOptions();
    }
}