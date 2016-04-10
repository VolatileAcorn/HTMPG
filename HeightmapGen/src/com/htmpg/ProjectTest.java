package com.htmpg;

import com.htmpg.layer.Base;
import com.htmpg.layer.Exporter;
import com.htmpg.layer.MedianFilter;

import java.io.File;

/**
 * Created by Tom on 08/04/2016.
 */
public class ProjectTest {


    public static void main(String[] args){
        Project project = new Project(new File("lol.png"));
        ((Base) project.getLayer(0)).setLandscape(Landscape.LandscapeType.ROLLING_HILLS,80135l,1000,1000);
        project.addLayer(new Exporter(Exporter.FileType.PNG16,"exported.png"));
        project.runNodes();
    }
}
