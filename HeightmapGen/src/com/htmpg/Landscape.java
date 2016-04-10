package com.htmpg;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Tom on 12/03/2016.
 */
public class Landscape {

    public enum LandscapeType {
        ROLLING_HILLS, FLAT_LANDS
    }

    //double[][] in format { { steps between generated noise, weighting of this layer}, ....}
    private static final HashMap<LandscapeType, int[][]> landscapeTypeToInterpolationModel = new HashMap<>();
    static {
        landscapeTypeToInterpolationModel.put(LandscapeType.ROLLING_HILLS, new int[][]{  {3,1}, {5,1} ,{11,1} ,{17,1} ,{27,2}, {43,2},{78,1}, {99,1} ,{300,2} }  );
        landscapeTypeToInterpolationModel.put(LandscapeType.FLAT_LANDS, new int[][]{  {78,1}, {89,1}, {93,1}, {95,1}, {97,1}, {99,1}, {103,1} , {130,1}, {156,1}, {270,1},{300,1} }  );
    }

    private double[][] array2d;
    private Random randomNumGen;
    private int[][] weightingPerInterpolation;


    public Landscape(LandscapeType landscapeType, long seed, int x, int y) {
        this.randomNumGen = new Random(seed);
        this.weightingPerInterpolation = landscapeTypeToInterpolationModel.get(landscapeType);
        this.generateNewLandscape(x, y);

    }

    private void generateNewLandscape(int x, int y) {
        int totalWeighting = 0;

        for (int i = 0; i < weightingPerInterpolation.length; i++) {
            totalWeighting += weightingPerInterpolation[i][1];
        }


        this.array2d = new double[x][y];
        double[][] currentLayer = new double[x][y];
        double[][] allLayers = new double[x][y];

        int gapMinusModx;
        int gapMinusMody;
        int negModx;
        int negMody;
        double topLeft;
        double topRight;
        double bottomLeft;
        double bottomRight;
        double top;
        double bottom;
        double interpolatedPoint;

        for (int layer = 0; layer < weightingPerInterpolation.length; layer++) {
            int step = weightingPerInterpolation[layer][0];
            //initial number generation
            for (int i = 0; i < x; i += step) {
                for (int j = 0; j < y; j += step) {
                    currentLayer[i][j] = randomNumGen.nextDouble();
                }
            }


            for (int i = 0; i < x; i += 1) {
                for (int j = 0; j < y; j += 1) {

                    //interpolate from four closest neighbours
                    negModx = i - (i % step);
                    negMody = j - (j % step);
                    gapMinusModx = (i + step - (i % step) >= x) ? negModx : i + step - (i % step);
                    gapMinusMody = (j + step - (j % step) >= y) ? negMody : j + step - (j % step);

                    topLeft = currentLayer[negModx][negMody];
                    topRight = currentLayer[gapMinusModx][negMody];
                    bottomRight = currentLayer[gapMinusModx][gapMinusMody];
                    bottomLeft = currentLayer[negModx][gapMinusMody];

                    //top interpolated
                    top = ((topRight * (i % step) ) + (topLeft * (step - (i % step) )))/step;
                    //bottom interpolated
                    bottom = ((bottomRight * (i % step) ) + (bottomLeft * (step - (i % step) )))/step;
                    //point interpolated
                    interpolatedPoint = ((bottom * (j % step) ) + (top * (step - (j % step) )))/step;


                    allLayers[i][j] += ( (double)weightingPerInterpolation[layer][1] / (double)totalWeighting ) * interpolatedPoint;
                }
            }
        }
        array2d = allLayers;

    }

    /**
     * normalize for
     * @param heightMap the layer to be scaled and turned into integer values
     * @return
     */
    private int[][] normalizeTo8BitRGBRange(double[][] heightMap) {
        int[][] stretchedArray = new int[heightMap.length][heightMap[0].length];

        double max = 0;
        double min = 1;
        //find min and max points
        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[0].length; j++) {
                if (heightMap[i][j] > max) {
                    max = heightMap[i][j];
                }
                if (heightMap[i][j] < min) {
                    min = heightMap[i][j];
                }
            }
        }


        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[0].length; j++) {
                stretchedArray[i][j] = (int) (((heightMap[i][j] - min) / (max - min))  * 255);
            }
        }

        return stretchedArray;
    }

    private int[][] normalizeTo16BitGray(double[][] heightMap) {
        int[][] stretchedArray = new int[heightMap.length][heightMap[0].length];

        double max = 0;
        double min = 1;
        //find min and max points
        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[0].length; j++) {
                if (heightMap[i][j] > max) {
                    max = heightMap[i][j];
                }
                if (heightMap[i][j] < min) {
                    min = heightMap[i][j];
                }
            }
        }


        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[0].length; j++) {
                stretchedArray[i][j] = (int) (((heightMap[i][j] - min) / (max - min))  * 65535);
            }
        }

        return stretchedArray;
    }


    public void exportRGB8Bit(String imagePath) {
        BufferedImage image = new BufferedImage(array2d.length,array2d[0].length,BufferedImage.TYPE_INT_ARGB);
        File outputFile = new File(imagePath);
        int rgb;
        int value;
        int[][] heightMapInt8Bit = normalizeTo8BitRGBRange(array2d);

        for (int i = 0; i < array2d.length; i++) {
            for (int j = 0; j < array2d[0].length; j++) {
                //convert to rgb representation
                value = heightMapInt8Bit[i][j];
                rgb = (255 << 8) + value;
                rgb = (rgb << 8) + value;
                rgb = (rgb << 8) + value;
                image.setRGB(i,j,rgb);
            }
        }

        try {
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void export16BitGrayscale(String imagePath) {
        BufferedImage image = new BufferedImage(array2d.length,array2d[0].length,BufferedImage.TYPE_USHORT_GRAY);
        WritableRaster raster = image.getRaster();
        File outputFile = new File(imagePath);

        int[][] heightMap16BitGray = normalizeTo16BitGray(array2d);

        for (int i = 0; i < array2d.length; i++) {
            for (int j = 0; j < array2d[0].length; j++) {
                raster.setPixel(i,j,new int[]{heightMap16BitGray[i][j]});
            }
        }
        image.setData(raster);
        try {
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Landscape applyMedianFiltering(int kernelRadii) {
        double[][] smoothedArray = new double[array2d.length][array2d[0].length];
        int listLength = (kernelRadii*2 + 1)*(kernelRadii*2 + 1);
        ArrayList<Double> kernelList;

        int offsetX;
        int offsetY;

        for (int i = 0; i < array2d.length; i++) {
            for (int j = 0; j < array2d[0].length; j++) {
                kernelList = new ArrayList<>(listLength);
                //apply median filter
                for (int i1 = -kernelRadii; i1 <= kernelRadii; i1++) {
                    for (int j1 = -kernelRadii; j1 <= kernelRadii; j1++) {

                        if (i + i1 < 0 || i + i1 >= array2d.length) {
                            offsetX = -i1;
                        }
                        else { offsetX = i1; }

                        if (j + j1 < 0 || j + j1 >= array2d[0].length) {
                            offsetY = -j1;
                        }
                        else { offsetY = j1; }

                        kernelList.add(array2d[i + offsetX][j + offsetY]);
                    }

                }
                //sort array to find median
                Collections.sort(kernelList, (o1, o2) -> Double.compare(o1,o2));

                smoothedArray[i][j] = kernelList.get((listLength/2) + 1);

            }

        }
        array2d = smoothedArray;

        return this;
    }


    public Landscape applyMeanFiltering(int kernelRadii) {
        double[][] smoothedArray = new double[array2d.length][array2d[0].length];
        int kernelSize = (kernelRadii*2 + 1)*(kernelRadii*2 + 1);
        double totalValue;

        int offsetX;
        int offsetY;

        for (int i = 0; i < array2d.length; i++) {
            for (int j = 0; j < array2d[0].length; j++) {
                totalValue = 0;
                //apply mean filter
                for (int i1 = -kernelRadii; i1 <= kernelRadii; i1++) {
                    for (int j1 = -kernelRadii; j1 <= kernelRadii; j1++) {

                        if (i + i1 < 0 || i + i1 >= array2d.length) {
                            offsetX = -i1;
                        }
                        else { offsetX = i1; }

                        if (j + j1 < 0 || j + j1 >= array2d[0].length) {
                            offsetY = -j1;
                        }
                        else { offsetY = j1; }

                        totalValue += array2d[i + offsetX][j + offsetY];
                    }

                }
                smoothedArray[i][j] = totalValue/kernelSize;

            }

        }
        array2d = smoothedArray;

        return this;
    }


    /**
     * interpolates cubically along one dimension
     * @param point0 the second point to the left of the interpolated point
     * @param point1 the first point to the left of the interpolated point
     * @param point2 the first point to the right of the interpolated point
     * @param point3 the second point to the right of the interpolated point
     * @param x the distance between point1 and the interpolated point, a fraction between 0 and 1 where 0 is point1 and 1 is point2
     * @return
     */
    private double interpolateCubic(double point0, double point1, double point2, double point3, double x){
        //using F(x) = ax^3 + bx^2 + cx + d, F'(x) = 3ax^2 + 2bx + c, x = 0 : point1, x = 1 : point2 etc.
        return ( -0.5*point0 + 1.5*point1 -1.5*point2 + 0.5*point3)*Math.pow(x,3) + (point0 -2.5*point1 + 2*point2 - 0.5*point3)*Math.pow(x,2) + (-0.5*point0 + 0.5*point2)*x + (point1);
    }

    public Landscape interpolateBicubic(int scale){
        int newWidth = (array2d[0].length - 1) * (scale) - 2;
        int newHeight = (array2d.length - 1) * (scale) - 2;
        double[][] interpolatedArray = new double[newHeight][newWidth];

        //16 surrounding points
        double[][] samplePoints;
        //4 interpolated columns
        double[] interpolatedColumns;

        //goes through all points in array 2d except the bottom and right hand edge which will be interpolated after
        for (int i = 0; i < array2d.length - 2; i++) {
            for (int j = 0; j < array2d[0].length - 2; j++) {

                //work out all 16 points beforehand since some could be out of bounds
                //checks for top side
                if(i < 1){
                    //top left
                    if(j < 1){
                        samplePoints = new double[][]{{ array2d[i][j], array2d[i][j], array2d[i][j+1], array2d[i][j+2]} , { array2d[i][j], array2d[i][j], array2d[i][j+1], array2d[i][j+2]} , { array2d[i+1][j], array2d[i+1][j], array2d[i+1][j+1], array2d[i+1][j+2]}, { array2d[i+2][j], array2d[i+2][j], array2d[i+2][j+1], array2d[i+2][j+2]} };
                    }//top right
                    else if(j >= array2d[0].length - 2){
                        samplePoints = new double[][]{{ array2d[i][j-1], array2d[i][j], array2d[i][j+1], array2d[i][j+1]} , { array2d[i][j-1], array2d[i][j], array2d[i][j+1], array2d[i][j+1]} , { array2d[i+1][j-1], array2d[i+1][j], array2d[i+1][j+1], array2d[i+1][j+1]}, { array2d[i+2][j-1], array2d[i+2][j], array2d[i+2][j+1], array2d[i+2][j+1]} };
                    }//top but not right or left
                    else {
                        samplePoints = new double[][]{{ array2d[i][j-1], array2d[i][j], array2d[i][j+1], array2d[i][j+2]} , { array2d[i][j-1], array2d[i][j], array2d[i][j+1], array2d[i][j+2]} , { array2d[i+1][j-1], array2d[i+1][j], array2d[i+1][j+1], array2d[i+1][j+2]}, { array2d[i+2][j-1], array2d[i+2][j], array2d[i+2][j+1], array2d[i+2][j+2]} };
                    }
                }
                //checks for bottom side
                else if(i >= array2d.length - 2){
                    //bottom left
                    if(j < 1){
                        samplePoints = new double[][]{{ array2d[i-1][j], array2d[i-1][j], array2d[i-1][j+1], array2d[i-1][j+2]} , { array2d[i][j], array2d[i][j], array2d[i][j+1], array2d[i][j+2]} , { array2d[i+1][j], array2d[i+1][j], array2d[i+1][j+1], array2d[i+1][j+2]}, { array2d[i+1][j], array2d[i+1][j], array2d[i+1][j+1], array2d[i+1][j+2]} };
                    }//bottom right
                    else if(j >= array2d[0].length - 2){
                        samplePoints = new double[][]{{ array2d[i-1][j-1], array2d[i-1][j], array2d[i-1][j+1], array2d[i-1][j+1]} , { array2d[i][j-1], array2d[i][j], array2d[i][j+1], array2d[i][j+1]} , { array2d[i+1][j-1], array2d[i+1][j], array2d[i+1][j+1], array2d[i+1][j+1]}, { array2d[i+1][j-1], array2d[i+1][j], array2d[i+1][j+1], array2d[i+1][j+1]} };
                    }//bottom but not right or left
                    else {
                        samplePoints = new double[][]{{ array2d[i-1][j-1], array2d[i-1][j], array2d[i-1][j+1], array2d[i-1][j+2]} , { array2d[i][j-1], array2d[i][j], array2d[i][j+1], array2d[i][j+2]} , { array2d[i+1][j-1], array2d[i+1][j], array2d[i+1][j+1], array2d[i+1][j+2]}, { array2d[i+1][j-1], array2d[i+1][j], array2d[i+1][j+1], array2d[i+1][j+2]} };
                    }
                }
                //checks just left
                else if(j < 1){
                    samplePoints = new double[][]{{ array2d[i-1][j], array2d[i-1][j], array2d[i-1][j+1], array2d[i-1][j+2]} , { array2d[i][j], array2d[i][j], array2d[i][j+1], array2d[i][j+2]} , { array2d[i+1][j], array2d[i+1][j], array2d[i+1][j+1], array2d[i+1][j+2]}, { array2d[i+2][j], array2d[i+2][j], array2d[i+2][j+1], array2d[i+2][j+2]} };
                }
                //checks just right
                else if(j >= array2d[0].length - 2) {
                    samplePoints = new double[][]{{ array2d[i-1][j-1], array2d[i-1][j], array2d[i-1][j+1], array2d[i-1][j+1]} , { array2d[i][j-1], array2d[i][j], array2d[i][j+1], array2d[i][j+1]} , { array2d[i+1][j-1], array2d[i+1][j], array2d[i+1][j+1], array2d[i+1][j+1]}, { array2d[i+2][j-1], array2d[i+2][j], array2d[i+2][j+1], array2d[i+2][j+1]} };
                }
                //not an edge case
                else {
                    samplePoints = new double[][]{{ array2d[i-1][j-1], array2d[i-1][j], array2d[i-1][j+1], array2d[i-1][j+2]} , { array2d[i][j-1], array2d[i][j], array2d[i][j+1], array2d[i][j+2]} , { array2d[i+1][j-1], array2d[i+1][j], array2d[i+1][j+1], array2d[i+1][j+2]}, { array2d[i+2][j-1], array2d[i+2][j], array2d[i+2][j+1], array2d[i+2][j+2]} };
                }

                for (int row = 0; row < scale; row++) {
                    // by doing one row at a time we can keep the column interpolated values and just change the x value
                    interpolatedColumns = new double[]{interpolateCubic(samplePoints[0][0],samplePoints[1][0],samplePoints[2][0],samplePoints[3][0],((double)row)/((double)scale)),
                            interpolateCubic(samplePoints[0][1],samplePoints[1][1],samplePoints[2][1],samplePoints[3][1],((double)row)/((double)scale)),
                            interpolateCubic(samplePoints[0][2],samplePoints[1][2],samplePoints[2][2],samplePoints[3][2],((double)row)/((double)scale)),
                            interpolateCubic(samplePoints[0][3],samplePoints[1][3],samplePoints[2][3],samplePoints[3][3],((double)row)/((double)scale)),};

                    for(int col = 0; col < scale; col++) {
                        interpolatedArray[(scale)*i + row][(scale)*j + col] = interpolateCubic(interpolatedColumns[0],interpolatedColumns[1],interpolatedColumns[2],interpolatedColumns[3],((double) col)/((double) scale));
                    }
                }

            }
        }
        array2d = interpolatedArray;

        return this;
    }

    public Landscape crop(int x1, int y1, int x2, int y2) {
        double[][] croppedArray = new double[y2 - y1][x2 - x1];

        for (int i = y1; i < y2; i++) {
            for (int j = x1; j < x2; j++) {
                croppedArray[i - y1][j - x1] = array2d[i][j];
            }
        }
            array2d = croppedArray;
        return this;
    }


}
