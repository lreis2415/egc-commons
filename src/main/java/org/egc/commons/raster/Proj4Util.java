package org.egc.commons.raster;

import org.osgeo.proj4j.*;

/**
 * Description:
 * <pre>
 * 空间坐标投影库 proj4j 工具类
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/9/24 16:17
 */
public class Proj4Util {

    /**
     * 坐标点转换
     *
     * @param sourceEPSG 源坐标EPSG码
     * @param targetEPSG 目标坐标EPSG码
     * @param x          源坐标 x
     * @param y          源坐标 y
     * @return 转换后坐标，通过{@link ProjCoordinate#x} 和 {@link ProjCoordinate#y} 获取值
     */
    public static ProjCoordinate coordinateTransform(int sourceEPSG, int targetEPSG, double x, double y) {
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
}
