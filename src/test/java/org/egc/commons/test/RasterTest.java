package org.egc.commons.test;

import org.egc.commons.raster.GeoInfoExtraction;
import org.egc.commons.raster.PointCoordinateTransform;
import org.egc.commons.raster.RasterFile2PostGIS;
import org.egc.commons.raster.RasterInfo;
import org.junit.Test;

import java.io.Console;
import java.sql.Timestamp;
import java.util.Date;


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
    @Test
    public void rasterInfoTest(){
        GeoInfoExtraction geoInfoExtraction = new GeoInfoExtraction();
        rasterInfo = geoInfoExtraction.readMetaData(path);
        System.out.println("");
    }
    @Test
    public void tt(){
        rasterFile2PostGIS.file2PostGIS(32650, "D:/DataBase/xuancheng/slope.tif",rasterTableName,dateBaseInfo, postGISPath,passWord);
        //String a = "raster2pgsql -s 32650  -I -a -M  D:\\SampleBase\\dem.tif  -a  public.t_rasters | psql  dbname=db_cyberSolim user=postgres password=123";
    }

    @Test
    public void coordTest(){
        double x = 13244689.433917364;
        double y = 3637696.4693184034;
        PointCoordinateTransform pointCoordinateTransform = new PointCoordinateTransform();
        double[] newCoord = pointCoordinateTransform.coordinateTransform(3857, 32650, x, y);
        System.out.println(newCoord[0]);
        System.out.print(newCoord[1]);
    }
    @Test
    public void testSplit(){
        String a = "D:\\ee/wo.ww";
        String[] b = a.split("/");
        System.out.println(b[1]);
    }
    @Test
    public void testGetRasterInfo(){
        String path = "D:/DataBase/xuancheng/slope.tif";
        GeoInfoExtraction geoInfoExtraction = new GeoInfoExtraction();
        RasterInfo rasterInfo = geoInfoExtraction.readMetaData(path);
        System.out.println(rasterInfo.getPixelSize());
    }
}
