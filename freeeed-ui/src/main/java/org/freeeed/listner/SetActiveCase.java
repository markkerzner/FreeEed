package org.freeeed.listner;

import org.freeeed.Entity.Project;
import org.freeeed.ServiceDao.ProjectService;
import org.freeeed.ui.FreeEedUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SetActiveCase implements ListSelectionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(FreeEedUI.class);
    private JTable caseTable;

    public SetActiveCase(JTable caseTable) {
        this.caseTable = caseTable;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            try {
                openProject();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private void openProject() {
        int row = caseTable.getSelectedRow();
        if (row >= 0) {
            int projectId = Integer.parseInt(caseTable.getValueAt(row, 0).toString().trim());
            Project project = ProjectService.getInstance().getProject(projectId);
            LOGGER.debug("Opening project {} - {}", projectId, project.getName());
            FreeEedUI.getInstance().setStatusBarProjectName(project.getName());
            Project.setActiveProject(project);
        }

    }


}
