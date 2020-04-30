package org.egc.commons.gis;

import lombok.Data;
import org.egc.commons.util.Formatter;

import java.io.Serializable;

/**
 * 栅格数据元数据
 *
 * @author houzhiwei
 * @date 2018 /8/28 18:55
 */
@Data
public class RasterMetadata implements Serializable {

    /**
     * Coordinate Reference System Identify
     */
    private String crs;
    /**
     * Coordinate Reference System string in PROJ.4 format
     */
    private String crsProj4;
    /**
     * Coordinate Reference System string in WKT format
     */
    private String crsWkt;
    /**
     * 空间引用标识符 (spatial reference identifier)，
     * 通常为EPSG代码，如4326。
     * 参考：http://epsg.io
     */
    private Integer srid;
    private String epsg;
    private double nodata;
    private String format;
    private String semantic;
    private double maxValue;
    private double minValue;
    private double meanValue;
    private boolean isProjected = false;
    /**
     * sample standard deviation
     */
    private double sdev;
    /**
     * left/west
     */
    private double minX;
    /**
     * right/east
     */
    private double maxX;
    /**
     * bottom/south
     */
    private double minY;
    /**
     * upper/north
     */
    private double maxY;
    private double centerX;
    private double centerY;
    private double pixelSize;
    /**
     * 高度
     */
    private double height;

    private double width;
    /**
     * 像素个数
     */
    private int sizeHeight;
    private int sizeWidth;

    private String unit;

    public String getExtent() {
        return Formatter.formatDoubleStr(minX, 4) + " " + Formatter.formatDoubleStr(minY, 4)
                + " " + Formatter.formatDoubleStr(maxX, 4) + " " + Formatter.formatDoubleStr(maxY, 4);
    }

    private String extent;
    private double[] upperLeft;
    private double[] upperRight;
    private double[] lowerLeft;
    private double[] lowerRight;

    /**
     * Get upper left .
     *
     * @return [minX, maxY]
     */
    public double[] getUpperLeft() {
        this.upperLeft = new double[]{minX, maxY};
        return upperLeft;
    }

    /**
     * Get upper right.
     *
     * @return [maxX, maxY]
     */
    public double[] getUpperRight() {
        this.upperRight = new double[]{maxX, maxY};
        return upperRight;
    }

    /**
     * Get lower left.
     *
     * @return [minX, minY]
     */
    public double[] getLowerLeft() {
        this.lowerLeft = new double[]{minX, minY};
        return lowerLeft;
    }

    /**
     * Get lower right:  .
     *
     * @return the [minX, minY]
     */
    public double[] getLowerRight() {
        this.lowerRight = new double[]{minX, minY};
        return lowerRight;
    }

}
