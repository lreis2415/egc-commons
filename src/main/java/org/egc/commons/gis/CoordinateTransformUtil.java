package org.egc.commons.gis;

import org.egc.commons.exception.BusinessException;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.osgeo.proj4j.*;

/**
 * Description:
 * <pre>
 * 坐标转换工具类
 * TODO rest web service
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/9/24 16:17
 */
public class CoordinateTransformUtil {

    /**
     * 坐标点转换
     * use easy-gis/gis-proj module
     * @param sourceEPSG 源坐标EPSG码
     * @param targetEPSG 目标坐标EPSG码
     * @param x          源坐标 x
     * @param y          源坐标 y
     * @return 转换后坐标，通过{@link ProjCoordinate#x} 和 {@link ProjCoordinate#y} 获取值
     */
    @Deprecated
    public static ProjCoordinate transformByProj4(int sourceEPSG, int targetEPSG, double x, double y) {
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CRSFactory csFactory = new CRSFactory();
        CoordinateReferenceSystem source = csFactory.createFromName("EPSG:" + sourceEPSG);
        CoordinateReferenceSystem target = csFactory.createFromName("EPSG:" + targetEPSG);
        CoordinateTransform trans = ctFactory.createTransform(source, target);
        ProjCoordinate sourceP = new ProjCoordinate();
        sourceP.x = x;
        sourceP.y = y;
        ProjCoordinate targetP = new ProjCoordinate();
        trans.transform(sourceP, targetP);
        return targetP;
    }

    /**
     * Transform with epsg coordinate.
     *
     * @param fromEPSG 源 epsg, 如 4326
     * @param toEPSG   目标 epsg
     * @param x        x 坐标或<b>纬度/lat</b>
     * @param y        y 坐标或<b>经度/lon</b>
     * @return the coordinate
     * @throws TransformException the transform exception
     * @throws FactoryException   the factory exception
     */
    public static Coordinate transform(int fromEPSG, int toEPSG, double x, double y) throws TransformException, FactoryException {
        return transform("EPSG:" + fromEPSG, "EPSG:" + toEPSG, x, y);
    }

    /**
     * Transform coordinate.
     *
     * @param fromAuthority the source AuthorityCode, e.g. "EPSG:4326", "AUTO:42001"
     * @param toAuthority   the target AuthorityCode
     * @param x             x 坐标或<b>纬度/lat</b>
     * @param y             y 坐标或<b>经度/lon</b>
     * @return the coordinate
     * @throws TransformException the transform exception
     * @throws FactoryException   the factory exception
     */
    public static Coordinate transform(String fromAuthority, String toAuthority, double x, double y) throws TransformException, FactoryException {
        org.opengis.referencing.crs.CoordinateReferenceSystem sourceCRS = CRS.decode(fromAuthority);
        org.opengis.referencing.crs.CoordinateReferenceSystem targetCRS = CRS.decode(toAuthority);
        // allow for some error due to different datums
        boolean lenient = true;
        MathTransform mathTransform = CRS.findMathTransform(sourceCRS, targetCRS, lenient);
        // x 竖轴，纬度
        // y 横轴，经度
        Coordinate source = new Coordinate(x, y);
        Coordinate target = new Coordinate();
        JTS.transform(source, target, mathTransform);
        return target;
    }

    /**
     * 使用 GDAL 转换坐标
     *
     * @param sourceEPSG      源 epsg
     * @param destinationEPSG 目标 epsg
     * @param x               x 坐标
     * @param y               y 坐标
     * @return [x, y]
     * @author lp
     */
    public static double[] transformByGdal(int sourceEPSG, int destinationEPSG, double x, double y) {
        SpatialReference source = new SpatialReference();
        SpatialReference destination = new SpatialReference();
        try {
            source.ImportFromEPSG(sourceEPSG);
            destination.ImportFromEPSG(destinationEPSG);
        } catch (RuntimeException e) {
            throw new BusinessException(e, "Unsupported SRS", true);
        }
        CoordinateTransformation coordinateTransformation = new CoordinateTransformation(source, destination);
        return coordinateTransformation.TransformPoint(x, y);
    }

    /**
     * Transform extent by gdal.
     *
     * @param fromEPSG the source epsg
     * @param toEPSG   the destination epsg
     * @param minX     the min x
     * @param minY     the min y
     * @param maxX     the max x
     * @param maxY     the max y
     * @return the double[]: minx,miny,maxx,maxy
     */
    public static double[] transformExtentByGdal(int fromEPSG, int toEPSG, double minX, double minY, double maxX, double maxY) {
        SpatialReference source = new SpatialReference();
        SpatialReference destination = new SpatialReference();
        try {
            source.ImportFromEPSG(fromEPSG);
            destination.ImportFromEPSG(toEPSG);
        } catch (RuntimeException e) {
            throw new BusinessException(e, "Unsupported SRS", true);
        }
        CoordinateTransformation coordinateTransformation = new CoordinateTransformation(source, destination);
        double[] ll = coordinateTransformation.TransformPoint(minX, minY);
        double[] ur = coordinateTransformation.TransformPoint(maxX, maxY);
        return new double[]{ll[0], ll[1], ur[0], ur[1]};
    }
}
