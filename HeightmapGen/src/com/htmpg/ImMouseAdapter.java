package com.htmpg;

import com.htmpg.panel.ImagePanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by Tom on 02/04/2016.
 */
public class ImMouseAdapter extends MouseAdapter {
    private ImagePanel imagePanel;

    int[] previousMousePosition;

    public ImMouseAdapter(ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
        this.previousMousePosition = new int[]{0,0};
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            previousMousePosition[0] = e.getX();
            previousMousePosition[1] = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                imagePanel.translateImage(e.getX() -previousMousePosition[0], e.getY() - previousMousePosition[1]);
                previousMousePosition[0] = e.getX();
                previousMousePosition[1] = e.getY();
            }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
            imagePanel.translateImage(e.getX() - previousMousePosition[0], e.getY() - previousMousePosition[1]);
            previousMousePosition[0] = e.getX();
            previousMousePosition[1] = e.getY();
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        float multiplier;
        if (e.getWheelRotation() == 1) {
            multiplier = 1f/1.1f;
        }else {
            multiplier = 1.1f;
        }
        imagePanel.multiplyScale(multiplier,e.getX(),e.getY());
    }
}

