package org.egc.commons.test;

import com.alibaba.fastjson.JSON;
import org.egc.commons.gis.*;
import org.gdal.gdal.gdal;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.osgeo.proj4j.ProjCoordinate;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;


/**
 * Created by lp on 2017/4/25.
 */
public class RasterTest {

    String tif = "d8FlowDirection.tif";
    String rasterTableName = " public.t_rasters";
    String dateBaseInfo = " -d db_cyberSolim -U postgres";
    String postGISPath = "D:\\webExe\\raster2pgsql.exe";
    String passWord = "123";

    public String tifPath() throws IOException {
        File file = new ClassPathResource(tif).getFile();
        return file.getAbsolutePath();
    }

    @Test
    public void rasterFile2PostGIS() throws IOException {
        String filename = tifPath();
        RasterFile2PostGIS rasterFile2PostGIS = new RasterFile2PostGIS();
        rasterFile2PostGIS.file2PostGIS(32650, filename, rasterTableName, dateBaseInfo, postGISPath, passWord);
        //String a = "raster2pgsql -s 32650  -I -a -M  D:\\SampleBase\\dem.tif  -a  public.t_rasters | psql  dbname=db_cyberSolim user=postgres password=123";
    }


    @Test
    public void testSplit() {
        String a = "D:\\ee/wo.ww";
        String[] b = a.split("/");
        System.out.println(b[1]);
    }

    @Test
    public void gdalVersion() {
        System.out.println(gdal.VersionInfo());
        Assert.assertEquals("2040400", gdal.VersionInfo());
    }

    @Test
    public void testGetRasterInfo() throws IOException {
        String filename = tifPath();
        GeoInfoExtraction geoInfoExtraction = new GeoInfoExtraction();
        RasterInfo rasterInfo = geoInfoExtraction.readMetaData(filename);
        System.out.println(rasterInfo.getPixelSize());
        System.out.println(rasterInfo.getSrid());
    }

    //geotools
    @Test
    public void testMeta() throws IOException {
        String filename = tifPath();
        String s = GeoTiffUtils.getMetadata(filename).toString();
        System.out.println(s);
    }

    @Test
    public void testMetaGdal() throws IOException {
        String filename = "F:\\data\\hsm_data\\data\\aspect_in_categories.tif";
//        String filename = tifPath();
        RasterMetadata metadata = GeoTiffUtils.getMetadataByGDAL(filename);
        System.out.println(metadata.getQuantileBreaks());
        System.out.println(JSON.toJSONString(metadata, true));
    }

}
