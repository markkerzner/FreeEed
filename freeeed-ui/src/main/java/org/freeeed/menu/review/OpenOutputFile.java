package org.freeeed.menu.review;

import org.freeeed.Entity.Project;
import org.freeeed.ui.FreeEedUI;
import org.freeeed.util.OsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class OpenOutputFile implements ActionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(FreeEedUI.class);

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!areResultsPresent()) {
            return;
        }
        String resultsFolder = Project.getActiveProject().getStagingDir();
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(resultsFolder));
            } else if (OsUtil.isLinux()) {
                String command = "nautilus " + resultsFolder;
                OsUtil.runCommand(command);
            } else if (OsUtil.isMac()) {
                String command = "open " + resultsFolder;
                OsUtil.runCommand(command);
            }
        } catch (IOException ex) {
            LOGGER.error("error OS util ", ex);
        }
    }

    private boolean areResultsPresent() {
        Project project = Project.getActiveProject();
        if (project == null) {
            JOptionPane.showMessageDialog(FreeEedUI.getInstance(), "Please open a project first");
            return false;
        }
        File outputFolder = new File(project.getStagingDir());
        if (outputFolder.exists()) {
            return true;
        }
        JOptionPane.showMessageDialog(FreeEedUI.getInstance(), "No results yet");
        return false;
    }
}
