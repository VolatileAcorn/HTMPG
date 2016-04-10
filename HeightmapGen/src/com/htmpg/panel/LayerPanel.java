package com.htmpg.panel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tom on 08/04/2016.
 */
public class LayerPanel extends JPanel {

    JScrollPane layerScrollPane;
    JPanel buttonPanel;

    public LayerPanel() {
        layerScrollPane = new JScrollPane();
        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(new JButton("butt 1"));
        buttonPanel.add(new JButton("butt 2"));
        buttonPanel.add(new JButton("butt 3"));
        layerScrollPane.add(buttonPanel);
        add(layerScrollPane);
    }

}
