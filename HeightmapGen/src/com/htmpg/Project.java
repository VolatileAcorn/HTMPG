package com.htmpg;

import com.htmpg.layer.Base;
import com.htmpg.layer.Layer;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by Tom on 06/04/2016.
 */


public class Project {

    private LinkedList<Layer> layers;
    private File file;

    public Project(File file) {
        this.layers = new LinkedList<>();
        this.layers.add(new Base());
        this.file = file;
    }

    /**
     * calls the applyLayer of each layer in order, taking the previous layers result as the next layers input.
     */
    public void runNodes(){

        Landscape landscape = new Landscape(Landscape.LandscapeType.FLAT_LANDS,0l,10,10);

        for (int i = 0; i < layers.size(); i++) {
            landscape = layers.get(i).applyLayer(landscape);
        }
    }

    public Layer getLayer(int index){
        return layers.get(index);
    }

    /**
     * adds a layer to the end of the list
     * @param layer layer to be added to the list of layers
     */
    public void addLayer(Layer layer){
        this.layers.add(layer);
    }

    /**
     * adds a layer to the specified position in the list (0 is the first element). All layers after the inserted layer are shuffled down once.
     * @param index position to add layer
     * @param layer layer to be added to the list of layers
     */
    public void addLayer(int index, Layer layer){
        this.layers.add(index, layer);
    }


}
