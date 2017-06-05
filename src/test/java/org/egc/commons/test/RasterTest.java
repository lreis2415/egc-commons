package org.egc.commons.test;

import org.egc.commons.raster.GeoInfoExtraction;
import org.egc.commons.raster.RasterInfo;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;


/**
 * Created by lp on 2017/4/25.
 */
public class RasterTest {
    String path = "D:\\SampleBase\\dem.tif";
    RasterInfo rasterInfo = new RasterInfo();
    @Test
    public void rasterInfoTest(){
        GeoInfoExtraction geoInfoExtraction = new GeoInfoExtraction();
        rasterInfo = geoInfoExtraction.readMetaData(path);




    }
    @Test
    public void tt(){
        Date date = new Date();
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        date = ts;
        System.out.println(date);
    }
}
