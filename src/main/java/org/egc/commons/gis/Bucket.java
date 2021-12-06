package org.egc.commons.gis;

public class Bucket {
    public double min;
    public double max;
    public long size;
    public long remain;

    public Bucket(double min,double max, long size){
        this.min=min;
        this.max=max;
        this.size=size;
        this.remain=size;
    }
    public double drain(long items){
        this.remain-=items;
        return min+((double)(size-remain)/size)*(max-min);
    }
}
