package com.htmpg.layer;

import com.htmpg.Landscape;

/**
 * Created by Tom on 05/04/2016.
 */
public abstract class Layer {


    protected enum LayerType{
        BASE,MEAN_FILTER,MEDIAN_FILTER,EXPORTER
    }

    protected LayerType layerType;

    public Layer(){}

    /**
     * given the previous nodes landscape, apply the current nodes modifications and return that landscape
     * @param landscape landscape of previous layer in chain
     * @return given landscape with current nodes modification applied to it
     */
    public abstract Landscape applyLayer(Landscape landscape);

    public LayerType getLayerType(){
        return layerType;
    }

}
