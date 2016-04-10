package com.htmpg.layer;

import com.htmpg.Landscape;

/**
 * Created by Tom on 05/04/2016.
 */
public class MedianFilter extends Layer {

    int strength;

    public MedianFilter(int strength) {
        this.strength = strength;

        this.layerType = LayerType.MEDIAN_FILTER;
    }

    @Override
    public Landscape applyLayer(Landscape landscape) {
        return landscape.applyMedianFiltering(strength);
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }
}
