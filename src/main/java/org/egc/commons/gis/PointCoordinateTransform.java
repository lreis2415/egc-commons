package org.egc.commons.gis;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

/**
 * use {@link CoordinateTransformUtil#transformByGdal(int, int, double, double)}
 */
@Deprecated
public class PointCoordinateTransform {

    public double[] coordinateTransform(int sourceEPSG, int destinationEPSG, double x, double y){
        SpatialReference source = new SpatialReference();
        source.ImportFromEPSG(sourceEPSG);
        SpatialReference destination = new SpatialReference();
        destination.ImportFromEPSG(destinationEPSG);
        CoordinateTransformation coordinateTransformation = new CoordinateTransformation(source, destination);
        return coordinateTransformation.TransformPoint(x, y);
    }

}
