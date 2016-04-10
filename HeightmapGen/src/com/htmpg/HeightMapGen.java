package com.htmpg;

import com.htmpg.panel.ImagePanel;
import com.htmpg.panel.LayerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


/**
 * Created by Tom on 31/03/2016.
 */
public class HeightMapGen {

    private static JFrame frame;
    private static JMenuBar menuBar;
    private static JMenu menu;
    private static JMenuItem menuItem;
    private static Project project;

    private static void createGUI(){

        frame = new JFrame("HeightmapGen");
        ImageIcon imageIcon = new ImageIcon("Logo.png");
        frame.setIconImage(imageIcon.getImage());

        menuBar = new JMenuBar();

        //split pane (image panel and tool panel)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        ImagePanel imagePanel = new ImagePanel();
        ImMouseAdapter mouseAdapter = new ImMouseAdapter(imagePanel);
        imagePanel.addMouseListener(mouseAdapter);
        imagePanel.addMouseMotionListener(mouseAdapter);
        imagePanel.addMouseWheelListener(mouseAdapter);

        //
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(15,1));
        buttonPanel.setMinimumSize(new Dimension(100,300));
        buttonPanel.setPreferredSize(new Dimension(100,800));
        buttonPanel.setMaximumSize(new Dimension(100, 2000));
        for (int i = 0; i < 15; i++) {
            JButton butt = new JButton("button: " + i);
            butt.setSize(new Dimension(150,70));
            buttonPanel.add(butt);
        }
        JScrollPane layerScrollPane = new JScrollPane(buttonPanel);
        layerScrollPane.setMinimumSize(new Dimension(150, 800));
        layerScrollPane.createVerticalScrollBar();
        //


        splitPane.setLeftComponent(imagePanel);
        splitPane.setRightComponent(layerScrollPane);

        imagePanel.setMinimumSize(new Dimension(600,300));
        imagePanel.setPreferredSize(new Dimension(1000,800));

        frame.add(splitPane);

        //File menu
        menu = new JMenu("File");
        menuBar.add(menu);

        menuItem = new JMenuItem("New project");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal;
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                returnVal = fc.showDialog(new JFrame(),"Create");
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    //create a new file
                    File file = fc.getSelectedFile();

                }
            }
        });

        menuItem = new JMenuItem("Open...");
        menu.add(menuItem);

        menu.addSeparator();
        menuItem = new JMenuItem("Save");
        menu.add(menuItem);
        menuItem = new JMenuItem("Save as");
        menu.add(menuItem);

        //Edit menu
        menu = new JMenu("Edit");
        menuBar.add(menu);

        menuItem = new JMenuItem("Undo");
        menu.add(menuItem);
        menuItem = new JMenuItem("Redo");
        menu.add(menuItem);

        //set Menubar to frame
        frame.setJMenuBar(menuBar);


        frame.pack();
    }

    private static void runGUI(){
        javax.swing.SwingUtilities.invokeLater(() -> {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280,720);
            frame.setVisible(true);
        });
    }


    public static void main(String[] args) {
        createGUI();
        runGUI();
    }
}
