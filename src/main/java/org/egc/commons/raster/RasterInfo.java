package org.egc.commons.raster;

/**
 * Created by lp on 2017/4/24.
 */
public class RasterInfo {
    private Integer srid;
    private  double left;
    private  double right;
    private double top;
    private double down;
    private double noData;
    private double pixelSize;

    public void setSrid(Integer srid) {
        this.srid = srid;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public void setRight(double right) {
        this.right = right;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public void setDown(double down) {
        this.down = down;
    }

    public void setNoData(double noData) {
        this.noData = noData;
    }

    public void setPixelSize(double pixelSize) {
        this.pixelSize = pixelSize;
    }

    public Integer getSrid() {

        return srid;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    public double getTop() {
        return top;
    }

    public double getDown() {
        return down;
    }

    public double getNoData() {
        return noData;
    }

    public double getPixelSize() {
        return pixelSize;
    }




}
