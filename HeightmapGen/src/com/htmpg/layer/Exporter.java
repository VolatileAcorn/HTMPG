package com.htmpg.layer;

import com.htmpg.Landscape;

/**
 * Created by Tom on 06/04/2016.
 */
public class Exporter extends Layer {

    private FileType fileExtension;
    private String filePath;

    public Exporter(FileType fileExtension, String filePath) {
        this.fileExtension = fileExtension;
        this.filePath = filePath;

        this.layerType = LayerType.EXPORTER;
    }

    @Override
    public Landscape applyLayer(Landscape landscape) {
        //export landscape
        if (fileExtension == FileType.PNG8) {
            landscape.exportRGB8Bit(filePath);

            landscape.export16BitGrayscale(filePath);
        } else if (fileExtension == FileType.PNG16) {
            landscape.export16BitGrayscale(filePath);
        }
        //return landscape unaltered
        return landscape;
    }

    public FileType getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(FileType fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public  enum FileType {
        PNG8, PNG16
    }

}
