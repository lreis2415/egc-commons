package org.egc.commons.test;

import com.alibaba.fastjson.JSON;
import org.egc.commons.command.ExecResult;
import org.egc.commons.gis.File2PostGIS;
import org.egc.commons.gis.PostGISInfo;
import org.egc.commons.gis.VectorMetadata;
import org.egc.commons.gis.VectorUtils;
import org.junit.Test;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/7 23:17
 */
public class VectorTest {

    // no srid
    String shp =  "H:\\gisdemo\\in\\in\\UserStreams.shp";
    //3857
    String shp2 =  "H:/gisdemo/in/simplified-land-polygons-complete-3857/simplified_land_polygons.shp";


    @Test
    public void test(){
        VectorMetadata metadata = VectorUtils.getShapefileMetadata(shp2);
        System.out.println(JSON.toJSONString(metadata,true));
    }

    @Test
    public void shap2pgis(){
        PostGISInfo info = new PostGISInfo("giser", "pgis", "pg_gis");
        info.setShapeTable("t_shp");
        ExecResult result = File2PostGIS.shp2PostGIS(3857, shp2, info);
        System.out.println(result.getOut());
        System.out.println(result.getError());
    }
}
