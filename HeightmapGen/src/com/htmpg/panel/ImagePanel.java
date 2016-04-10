package com.htmpg.panel;

import com.htmpg.Landscape;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Tom on 02/04/2016.
 */
public class ImagePanel extends JPanel {

    private BufferedImage bufferedImage;
    private int posX, posY;
    private float scale;

    public ImagePanel(String filePath) {
        try {
            bufferedImage = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        posX = 0;
        posY = 0;
        scale = 0.5f;
    }

    public ImagePanel() {
        bufferedImage = null;
        posX = 0;
        posY = 0;
        scale = 0.5f;
    }

    public void loadLandscape(Landscape landscape) {
        landscape.export16BitGrayscale("temp.png");
        try {
            bufferedImage = ImageIO.read(new File("temp.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        repaint();
    }


    public void translateImage(int x, int y) {
        posX += x;
        posY += y;
        repaint();
    }

    public void multiplyScale(float coeff, int x, int y) {
        scale *= coeff;
        posX += (float) (posX - x) * (coeff - 1f);
        posY += (float) (posY - y) * (coeff - 1f);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (bufferedImage != null) {
            g.clearRect(-10000, -10000, 300000, 300000);
            g.drawImage(bufferedImage, posX, posY, (int) (bufferedImage.getWidth() * scale), (int) (bufferedImage.getHeight() * scale), null);
        }
    }
}
