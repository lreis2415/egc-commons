package org.egc.commons.test;

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

    String shp =  "H:\\gisdemo\\in\\in\\UserStreams.shp";


    @Test
    public void test(){
        VectorMetadata metadata = VectorUtils.getShapefileMetadata(shp);
        System.out.println(metadata);
    }
}
