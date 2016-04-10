package com.htmpg.layer;

import com.htmpg.Landscape;

/**
 * Created by Tom on 05/04/2016.
 */
public class MeanFilter extends Layer {

    int strength;

    public MeanFilter(int strength) {
        this.strength = strength;

        this.layerType = LayerType.MEAN_FILTER;
    }

    @Override
    public Landscape applyLayer(Landscape landscape) {
        return landscape.applyMeanFiltering(strength);
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }
}
