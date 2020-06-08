package org.freeeed.menu.help;

import org.freeeed.ui.UtilUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenHelp implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String url = "http://bit.ly/30fiIul";
        UtilUI.openBrowser(null, url);
    }
}
