package com.htmpg.layer;
import com.htmpg.Landscape;

/**
 * Created by Tom on 05/04/2016.
 */
public class Base extends Layer {

    private final Landscape.LandscapeType DEFAULT_LANDSCAPE_TYPE = Landscape.LandscapeType.ROLLING_HILLS;
    private final long DEFAULT_LANDSCAPE_SEED = 1337l;
    private final int DEFAULT_LANDSCAPE_SIZE = 400;

    private Landscape.LandscapeType landscapeType;
    private long seed;
    private int x;
    private int y;



    /**
     * creates a default landscape which can be edited and built upon
     */
    public Base() {
        this.landscapeType = this.DEFAULT_LANDSCAPE_TYPE;
        this.seed = this.DEFAULT_LANDSCAPE_SEED;
        this.x = this.DEFAULT_LANDSCAPE_SIZE;
        this.y = this.DEFAULT_LANDSCAPE_SIZE;

        this.layerType = LayerType.BASE;
    }

    public void setLandscape(Landscape.LandscapeType type, long seed, int sizeX, int sizeY){
        this.landscapeType = type;
        this.seed = seed;
        this.x = sizeX;
        this.y = sizeY;
    }

    @Override
    public Landscape applyLayer(Landscape landscape) {
        return new Landscape(landscapeType, seed, x, y);
    }
}
