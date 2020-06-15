package org.freeeed.menu.file;
import org.freeeed.ui.FreeEedUI;
import org.freeeed.ui.ProjectUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenNewCase implements ActionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(FreeEedUI.class);
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            LOGGER.debug("New Project");
            ProjectUI dialog = new ProjectUI(FreeEedUI.getInstance(),true);
            dialog.setVisible(true);
            dialog.setLocationRelativeTo(FreeEedUI.getInstance());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
