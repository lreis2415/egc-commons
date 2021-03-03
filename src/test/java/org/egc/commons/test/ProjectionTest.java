package org.egc.commons.test;

import org.egc.commons.gis.CoordinateTransformUtil;
import org.egc.commons.gis.UtmUtilities;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.osgeo.proj4j.ProjCoordinate;

/**
 * @author houzhiwei
 * @date 2021/3/2 9:29
 */
public class ProjectionTest {
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
    public void utm() {
        int utmEpsg = UtmUtilities.getUtmEpsg(116.738, 27.180);
        System.out.println(utmEpsg);
        System.out.println(UtmUtilities.utmZone(116.738));
    }

    @Test
    public void coordinateExtent() throws TransformException, FactoryException {
//        double[] extent = CoordinateTransformUtil.transformExtentByGdal(4326, 32631, 115.580, 25.934, 116.738, 27.180);
//        System.out.println(extent);

        double x = 115.580;
        double y = 25.934;
        Coordinate transform = CoordinateTransformUtil.transform(4326, 32650, y, x);
        double[] newCoord = CoordinateTransformUtil.transformByGdal(4326, 32650, x, y);
        System.out.println(newCoord[0]);//688876.5583467542
        System.out.println(newCoord[1]);//3435505.2951291953
    }
}
