package org.egc.commons.xml;

public class CsvSampleInfo {
    private String id;
    private int srid;
    private double x;
    private double y;
    private double depthUp;
    private double depthDown;
    private String semanticName;
    private double semanticValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSrid() {
        return srid;
    }

    public void setSrid(int srid) {
        this.srid = srid;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDepthUp() {
        return depthUp;
    }

    public void setDepthUp(double depthUp) {
        this.depthUp = depthUp;
    }

    public double getDepthDown() {
        return depthDown;
    }

    public void setDepthDown(double depthDown) {
        this.depthDown = depthDown;
    }

    public String getSemanticName() {
        return semanticName;
    }

    public void setSemanticName(String semanticName) {
        this.semanticName = semanticName;
    }

    public double getSemanticValue() {
        return semanticValue;
    }

    public void setSemanticValue(double semanticValue) {
        this.semanticValue = semanticValue;
    }
}
