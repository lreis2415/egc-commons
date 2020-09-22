package org.egc.commons.test;

import com.alibaba.fastjson.JSON;
import org.egc.commons.command.ExecResult;
import org.egc.commons.gis.*;
import org.gdal.osr.SpatialReference;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;


public class VectorTest {

    // no srid
    String shp = "F:\\data\\SWAT global\\World_Data_Grids\\country.shp";
    //3857
    String shp2 = "H:/gisdemo/in/simplified-land-polygons-complete-3857/simplified_land_polygons.shp";
    String shp31 = "D:\\data\\WebSites\\egcDataFiles\\1\\49\\basin.shp";
    String shp3 = "D:\\data\\WebSites\\egcDataFiles\\1\\49\\basin_4326.shp";

    @Test
    public void test() {
        VectorMetadata metadata = VectorUtils.getShapefileMetadata(shp);
        System.out.println(JSON.toJSONString(metadata, true));
    }

    @Test
    public void wkt() {
        String wkt = VectorUtils.shp2Wkt("D:\\data\\WebSites\\egcDataFiles\\1\\49\\basin_polygon.shp");
        System.out.println(wkt.startsWith("MULTIPOLYGON"));
    }

    @Test
    public void project() {
        VectorUtils.reprojectToWgs84(shp31, "D:\\data\\WebSites\\egcDataFiles\\1\\49\\basin_polygon.shp");
    }

    @Test
    public void shap2pgis() {
        PostGISInfo info = new PostGISInfo("giser", "pgis", "pg_gis");
        info.setShapeTable("t_shp");
        ExecResult result = File2PostGIS.shp2PostGIS(3857, shp2, info);
        System.out.println(result.getOut());
        System.out.println(result.getError());
    }

    @Test
    public void point() {
        PointCoordinateTransform transform = new PointCoordinateTransform();
        transform.coordinateTransform(0, 4326, 119.15, 30.97);
    }

    @Test
    public void getName() {
        String filename = this.getClass().getClassLoader().getResource("shp.zip").getFile();
        String name = GeoServerManager.getShpNameFromZippedFile(new File(filename));
        assertEquals("robReach", name);
    }
}
