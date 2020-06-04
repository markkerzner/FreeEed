package org.freeeed.main;

import org.freeeed.Entity.Project;
import org.freeeed.ServiceDao.ProjectService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class PopulateCaseList {

    public static void Populate(JTable caseTable) {
        final String[] columns = new String[]{"Case ID", "Scaia ID", "Name", "Date created", ""};
        List<Project> projects = ProjectService.getInstance().getProjects();
        Object[][] data = new Object[projects.size()][4];
        int row = 0;
        for (Project prj : projects) {
            data[row][0] = prj.getProjectId();
            data[row][1] = prj.getProjectId();
            data[row][2] = "  " + prj.getName();
            data[row][3] = "  " + prj.getCreateTime();
            row++;
        }
        caseTable.setModel(new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
    }


}
