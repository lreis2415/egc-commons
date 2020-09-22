package org.egc.commons.gis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.exception.BusinessException;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.VectorTranslateOptions;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.ogr.*;
import org.gdal.osr.SpatialReference;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Vector;

/**
 * Description:
 * <pre>
 * utilities for vector data
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/3 19:35
 */
@Slf4j
public class VectorUtils {

    /**
     * read shapefile and get metadata use OGR
     *
     * @param shapefile shapefile file path
     * @return Metadata
     */
    public static VectorMetadata getShapefileMetadata(String shapefile) {
        if (StringUtils.isBlank(shapefile)) {
            throw new BusinessException("No shapefile to read!", false);
        } else if (!new File(shapefile).exists()) {
            throw new BusinessException("Shapefile [ " + shapefile + " ] not found!", false);
        }
        ogr.RegisterAll();
        DataSource ds = ogr.Open(shapefile, false);
        Driver driver = ds.GetDriver();

        VectorMetadata metadata = new VectorMetadata();
        metadata.setShapeEncoding(gdal.GetConfigOption("SHAPE_ENCODING"));
        metadata.setName(FilenameUtils.getName(shapefile));
        metadata.setFormat(driver.GetName());
        metadata.setLayerCount(ds.GetLayerCount());
        Layer layer = ds.GetLayer(0);
        metadata.setGeomType(layer.GetGeomType());
        metadata.setFeatureCount(layer.GetFeatureCount());
        metadata.setGeometry(layer.GetNextFeature().GetGeometryRef().GetGeometryName());
        //feature id 通常包含字母 “ID"
        FeatureDefn layerDefn = layer.GetLayerDefn();
        for (int i = 0; i < layerDefn.GetFieldCount(); i++) {
            FieldDefn fieldDefn = layerDefn.GetFieldDefn(i);
            if (fieldDefn.GetName().toUpperCase().endsWith("ID")) {
                metadata.setFeatureIdField(fieldDefn.GetName());
            }
        }
        SpatialReference sr = layer.GetSpatialRef();
        // 若没有投影则设置为 wgs 84
        if (sr == null) {
            log.warn("No projection found in {} ! Project to WGS84.", shapefile);
            // 没有参考说明没有prj文件，需要创建一个同名的prj文件
            sr = new SpatialReference();
            sr.ImportFromEPSG(4326);
            //sr.MorphToESRI(); // 这样的话拿不到epsg，但是如果是读取自ESRI的wkt，则必须包含此句，否则报错：不支持的SRS
            File prj = new File(FilenameUtils.removeExtension(shapefile) + ".prj");
            try {
                Files.write(prj.toPath(), sr.ExportToWkt().getBytes());
            } catch (IOException e) {
                log.warn("Create prj file failed.", e);
            }
        }
        String authorityCode = sr.GetAttrValue("Authority", 1);
        if (StringUtils.isNotBlank(authorityCode)) {
            metadata.setSrid(Integer.parseInt(authorityCode));
        }
        String geogcs = sr.GetAttrValue("GEOGCS");
        if (sr.IsProjected() == 1) {
            String projcs = sr.GetAttrValue("PROJCS");
            metadata.setCrs(projcs);
        } else {
            metadata.setCrs(geogcs);
        }
        metadata.setUnit(sr.GetAttrValue("UNIT"));
        metadata.setCrsProj4(sr.ExportToProj4());
        metadata.setCrsWkt(sr.ExportToWkt());
        SpatialReference a = new SpatialReference();
        a.ImportFromProj4(sr.ExportToProj4());
        double[] extent = layer.GetExtent();
        metadata.setMinX(extent[0]);
        metadata.setMaxX(extent[1]);
        metadata.setMinY(extent[2]);
        metadata.setMaxY(extent[3]);

        ds.delete();
        driver.delete();
        gdal.GDALDestroyDriverManager();
        return metadata;
    }

    /**
     * shapefile geometry to wkt string
     *
     * @param shapefile file path
     * @return wkt string
     */
    public static String shp2Wkt(String shapefile) {
        ogr.RegisterAll();
        DataSource ds = ogr.Open(shapefile);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ds.GetLayerCount(); i++) {
            Layer layer = ds.GetLayer(i);
            sb.append(layer.GetNextFeature().GetGeometryRef().ExportToWkt());
        }
        ds.delete();
        return sb.toString();
    }

    /**
     * shapefile geometry to geojson string
     *
     * @param shapefile file path
     * @return geojson string
     */
    public static String shp2geojson(String shapefile) {
        ogr.RegisterAll();
        DataSource ds = ogr.Open(shapefile);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ds.GetLayerCount(); i++) {
            Layer layer = ds.GetLayer(i);
            sb.append(layer.GetNextFeature().GetGeometryRef().ExportToJson());
        }
        ds.delete();
        return sb.toString();
    }

    /**
     * Reproject to wgs 84.
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     */
    public static void reprojectToWgs84(String srcFile, String dstFile) {
        SpatialReference srs = new SpatialReference();
        srs.ImportFromEPSG(4326);
        reproject(srcFile, dstFile, srs);
    }

    /**
     * <pre>
     * Re-project data.
     * https://pcjericks.github.io/py-gdalogr-cookbook/projection.html
     * </pre>
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @param dstSRS  the target srs
     */
    public static void reproject(String srcFile, String dstFile, SpatialReference dstSRS) {
        if (gdal.GetDriverCount() == 0) {
            gdal.AllRegister();
        }
        Dataset ds = gdal.OpenEx(srcFile, gdalconst.OF_VECTOR | gdalconst.OF_VERBOSE_ERROR);
        Vector<String> options = new Vector<>();
        options.add("-f");
        options.add("ESRI Shapefile");
        options.add("-t_srs");
        options.add(dstSRS.ExportToProj4());
        options.add("-overwrite");
        gdal.VectorTranslate(dstFile, ds, new VectorTranslateOptions(options));
        ds.delete();
        gdal.GDALDestroyDriverManager();
    }
}
