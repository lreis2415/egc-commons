package org.egc.commons.test;

import org.egc.commons.gis.*;
import org.junit.Test;
import org.osgeo.proj4j.ProjCoordinate;


/**
 * Created by lp on 2017/4/25.
 */
public class RasterTest {
    String path = "D:/DataBase/xuancheng/geo.tif";
    String rasterTableName = " public.t_rasters";
    String dateBaseInfo = " -d db_cyberSolim -U postgres";
    String postGISPath = "D:\\webExe\\raster2pgsql.exe";
    String passWord = "123";
    RasterInfo rasterInfo = new RasterInfo();
    RasterFile2PostGIS rasterFile2PostGIS = new RasterFile2PostGIS();
    String tif = "H:\\dem_TX.tif";
    String tif2 = "D:\\data\\WebSites\\egcDataFiles\\upload\\20181006\\raster\\bdbb2fce7d81d3d70d284e37d92bb08b.tif";
    @Test
    public void rasterInfoTest() {
        GeoInfoExtraction geoInfoExtraction = new GeoInfoExtraction();
        rasterInfo = geoInfoExtraction.readMetaData(path);
        System.out.println("");
    }

    @Test
    public void tt() {
        rasterFile2PostGIS.file2PostGIS(32650, "D:/DataBase/xuancheng/slope.tif", rasterTableName, dateBaseInfo, postGISPath, passWord);
        //String a = "raster2pgsql -s 32650  -I -a -M  D:\\SampleBase\\dem.tif  -a  public.t_rasters | psql  dbname=db_cyberSolim user=postgres password=123";
    }

    @Test
    public void coordTest() {
        double x = 13244689.433917364;
        double y = 3637696.4693184034;
        double[] newCoord = CoordinateTransformUtil.transformByGdal(3857, 32650, x, y);
        System.out.println(newCoord[0]);//688876.5583467542
        System.out.println(newCoord[1]);//3435505.2951291953

        ProjCoordinate coordinate = CoordinateTransformUtil.transformByProj4(3857, 32650, x, y);
        System.out.println(coordinate.x);//688876.5583467542
        System.out.println(coordinate.y);//3435505.2951291953
    }

    @Test
    public void testSplit() {
        String a = "D:\\ee/wo.ww";
        String[] b = a.split("/");
        System.out.println(b[1]);
    }

    @Test
    public void testGetRasterInfo() {
        GeoInfoExtraction geoInfoExtraction = new GeoInfoExtraction();
        RasterInfo rasterInfo = geoInfoExtraction.readMetaData(tif);
        System.out.println(rasterInfo.getPixelSize());
        System.out.println(rasterInfo.getSrid());
    }


    @Test
    public void testMeta(){
//        String s= GeoTiffUtils.getMetadata(tif2).toString();
        String s= GeoTiffUtils.getMetadata(tif).toString();
        System.out.println(s);
    }
}
