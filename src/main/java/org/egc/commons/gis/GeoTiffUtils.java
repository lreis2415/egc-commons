package org.egc.commons.gis;

import com.google.common.base.Strings;
import it.geosolutions.jaiext.range.NoDataContainer;
import lombok.extern.slf4j.Slf4j;
import org.egc.commons.exception.BusinessException;
import org.egc.commons.util.StringUtil;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.osr.SpatialReference;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.data.DataSourceException;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.Envelope2D;
import org.geotools.process.classify.ClassificationMethod;
import org.geotools.process.raster.CoverageClassStats;
import org.geotools.referencing.CRS;
import org.jaitools.numeric.Statistic;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.osgeo.proj4j.CRSFactory;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * 使用geotools读取geotiff，获取元数据信息
 * 参考
 * https://gis.stackexchange.com/questions/106882/reading-each-pixel-of-each-band-of-multiband-geotiff-with-geotools-java
 *
 * @author houzhiwei
 * @date 2018/9/21
 */
@Slf4j
public class GeoTiffUtils {

    /**
     * Read geotiff file.
     *
     * @param tif the geotiff
     * @return the grid coverage 2 d
     * @throws IOException the io exception
     */
    public static GridCoverage2D read(String tif) {
        String msg;
        File rasterFile = new File(tif);
        GridCoverage2DReader reader = null;
        try {
            reader = new GeoTiffReader(new FileInputStream(rasterFile));
        } catch (DataSourceException e) {
            msg = "Error in GeoTIFF file!";
            log.error(msg, e);
            throw new BusinessException(e, msg + e.getLocalizedMessage());
        } catch (FileNotFoundException e) {
            msg = "GeoTIFF file not found! ";
            log.error(msg, e);
            throw new BusinessException("GeoTIFF file not found! " + e.getLocalizedMessage());
        }

        ParameterValue<OverviewPolicy> policy = AbstractGridFormat.OVERVIEW_POLICY.createValue();
        policy.setValue(OverviewPolicy.IGNORE);

        //this will basically read 4 tiles worth of data at once from the disk...
        ParameterValue<String> gridSize = AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();

        //Setting read type: use JAI ImageRead (true) or ImageReaders read methods (false)
        ParameterValue<Boolean> useJaiRead = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJaiRead.setValue(true);

        GridCoverage2D coverage = null;
        try {
            coverage = reader.read(new GeneralParameterValue[]{policy, gridSize, useJaiRead});
        } catch (IOException e) {
            msg = "Error while reading GeoTIFF file!";
            log.error(msg, e);
            throw new BusinessException(msg + e.getLocalizedMessage());
        }
        return coverage;
    }

    /**
     * Read geotiff and gets metadata. <br/>
     * Recommend to use {@link #getMetadataByGDAL(String)} <br/>
     * 暂不包含 UniqueValues 及 Quantile values
     *
     * @param tif the geotiff
     * @return the raster metadata
     */
    public static RasterMetadata getMetadata(String tif) {
        GridCoverage2D coverage2D = read(tif);
        RasterMetadata metadata = getCoverageMetadata(coverage2D);
        metadata.setFormat("GeoTIFF");
        return metadata;
    }

    /**
     * <pre>
     * Get coverage metadata.
     * slower than gdal
     * </pre>
     * TODO geogcs,projcs,isProjected
     *
     * @param coverage the coverage
     * @return the raster metadata
     */
    public static RasterMetadata getCoverageMetadata(GridCoverage2D coverage) {
        RasterMetadata metadata = new RasterMetadata();
        metadata.setFormat(coverage.getName().toString());
        NoDataContainer noDataProperty = CoverageUtilities.getNoDataProperty(coverage);
        //default no data value
        double nodata = -9999d;
        if (noDataProperty != null) {
            nodata = noDataProperty.getAsSingleValue();
        }
        metadata.setNodata(nodata);
        CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem();

        //获取wkt格式的投影信息
        metadata.setCrs(crs.getName().getCode());
        metadata.setCrsWkt(crs.toWKT());
        metadata.setUnit(crs.getCoordinateSystem().getAxis(0).getUnit().toString());
        try {
            Integer epsg = CRS.lookupEpsgCode(crs, true);
            metadata.setEpsg(epsg);
            metadata.setSrid(epsg);
            CRSFactory csFactory = new CRSFactory();
            if (epsg != null) {
                org.osgeo.proj4j.CoordinateReferenceSystem crsProj = csFactory.createFromName("EPSG:" + epsg);
                //获取proj4格式的投影信息
                metadata.setCrsProj4(crsProj.getProjection().getPROJ4Description());
            }
        } catch (FactoryException e) {
            log.error("Error occured while searching for the srid.", e);
            throw new BusinessException(e, "Error occured while searching for the CRS srid");
        }

        Envelope2D envelope2D = coverage.getEnvelope2D();
        metadata.setHeight(envelope2D.height);
        metadata.setWidth(envelope2D.width);
        metadata.setMaxX(envelope2D.getMaxX());
        metadata.setMinX(envelope2D.getMinX());
        metadata.setMaxY(envelope2D.getMaxY());
        metadata.setMinY(envelope2D.getMinY());
        metadata.setCenterX(envelope2D.getCenterX());
        metadata.setCenterY(envelope2D.getCenterY());
        RenderedImage image = coverage.getRenderedImage();
        metadata.setSizeHeight(image.getHeight());
        metadata.setSizeWidth(image.getWidth());
        metadata.setPixelSize((envelope2D.getMaxY() - envelope2D.getMinY()) / image.getHeight());
        CoverageClassStats rasterProcess = new CoverageClassStats();
        Set<Statistic> set = new HashSet();
        set.add(Statistic.MAX);
        set.add(Statistic.MIN);
        set.add(Statistic.MEAN);
        set.add(Statistic.SDEV);
        int band_i = CoverageUtilities.getVisibleBand(image);
        //classes 分段数
        CoverageClassStats.Results results = null;
        try {
            results = rasterProcess.execute(coverage, set, band_i, 1,
                    ClassificationMethod.QUANTILE, nodata, null);
        } catch (IOException e) {
            log.error("Process raster file statistics failed", e);
            throw new BusinessException(e, "Process raster file statistics failed");
        }
        metadata.setMinValue(results.value(0, Statistic.MIN));
        metadata.setMaxValue(results.value(0, Statistic.MAX));
        metadata.setMeanValue(results.value(0, Statistic.MEAN));
        metadata.setSdev(results.value(0, Statistic.SDEV));
        return metadata;
    }

    /**
     * 利用 gdal获取栅格数据元数据 <br/>
     * 包含 UniqueValues 及 Quantile values
     *
     * @param tif 栅格数据
     * @return 元数据信息
     */
    public static RasterMetadata getMetadataByGDAL(String tif) {
        StringUtil.isNullOrEmptyPrecondition(tif, "Raster file must exists");
        gdal.AllRegister();
        //gdal.SetConfigOption("GDAL_DATA", "C:\\Program Files\\GDAL\\gdal-data" );
        RasterMetadata metadata = new RasterMetadata();
        Dataset dataset = gdal.Open(tif, gdalconstConstants.GA_ReadOnly);
        Driver driver = dataset.GetDriver();
        metadata.setFormat(driver.getShortName());
        SpatialReference sr = new SpatialReference(dataset.GetProjectionRef());
        metadata.setCrsProj4(sr.ExportToProj4());
        metadata.setCrsWkt(sr.ExportToWkt());
        String authorityCode = sr.GetAuthorityCode(null);
        String epsg = sr.GetAttrValue("Authority", 1);
        metadata.setEpsg(Integer.parseInt(epsg));
        if (authorityCode != null) {
            Integer srid = Integer.parseInt(authorityCode);
            metadata.setSrid(srid);
        }
        String projcs = sr.GetAttrValue("PROJCS");
        String geogcs = sr.GetAttrValue("GEOGCS");
        if (Strings.isNullOrEmpty(projcs)) {
            metadata.setCrs(geogcs);
        } else {
            metadata.setCrs(projcs);
            metadata.setProjected(true);
        }

        metadata.setUnit(sr.GetAttrValue("UNIT"));

        Band band = dataset.GetRasterBand(1);
        Double[] nodataVal = new Double[1];
        band.GetNoDataValue(nodataVal);
        Double nodata = nodataVal[0];
        if (nodata == null) {
            nodata = -9999d;
        }
        metadata.setNodata(nodata);
        double[] min = new double[1], max = new double[1], stddev = new double[1], mean = new double[1];
        band.GetStatistics(true, true, min, max, mean, stddev);
        metadata.setMinValue(min[0]);
        metadata.setMaxValue(max[0]);
        metadata.setMeanValue(mean[0]);
        metadata.setSdev(stddev[0]);

        int xSize = dataset.GetRasterXSize();
        int ySize = dataset.GetRasterYSize();

        double[] gt = dataset.GetGeoTransform();
       /*
        adfGeoTransform[0] // top left x
        adfGeoTransform[1] // w-e pixel resolution
        adfGeoTransform[2] // 0
        adfGeoTransform[3] // top left y
        adfGeoTransform[4] // 0
        adfGeoTransform[5] // n-s pixel resolution (negative value)
        */
        metadata.setMinX(gt[0]);
        metadata.setMaxX(gt[0] + gt[1] * xSize);
        metadata.setCenterX(gt[0] + gt[1] * xSize / 2);
        metadata.setMaxY(gt[3]);
        metadata.setMinY(gt[3] + gt[5] * ySize);
        metadata.setCenterY(gt[3] + gt[5] * ySize / 2);
        //gt[5]
        metadata.setPixelSize(gt[1]);

        metadata.setWidth(xSize * gt[1]);
        metadata.setHeight(ySize * gt[5]);
        metadata.setSizeHeight(ySize);
        metadata.setSizeWidth(xSize);

        float[] dataBuf = new float[xSize * ySize];
        //TODO test
        // 没有 band.ReadRaster(0, 0, xSize, ySize, dataBuf, xSize, ySize, 0, 0);
        band.ReadRaster(0, 0, xSize, ySize, dataBuf);
        metadata.setQuantileBreaks(getQuantile(dataBuf, nodata));
        metadata.setQuantileBreaks(getUniqueValues(dataBuf, nodata));
        dataset.delete();
        gdal.GDALDestroyDriverManager();
        return metadata;
    }

    private static String getUniqueValues(float[] dataBuf, Double nodata) {
        List<Float> uniqueValues = new ArrayList<Float>();
        List<Float> dataBufList = new ArrayList<>();
        for (float dataBufVal : dataBuf) {
            dataBufList.add(dataBufVal);
        }
        Collections.sort(dataBufList);
        int lastNodataIndex = dataBufList.lastIndexOf(nodata.floatValue());

        uniqueValues.add(dataBufList.get(lastNodataIndex + 1));
        String rasterUniqueValues = String.valueOf(dataBufList.get(lastNodataIndex + 1).intValue());
        StringBuilder sb = new StringBuilder(rasterUniqueValues);

        for (int i = lastNodataIndex + 2; i < dataBufList.size() - 1; i++) {
            if (!dataBufList.get(i).equals(uniqueValues.get(uniqueValues.size() - 1))) {
                uniqueValues.add(dataBufList.get(i));
                sb.append(" ");
                sb.append(dataBufList.get(i).intValue());
            }
        }
        return sb.toString();
    }

    //分位数
    private static String getQuantile(float[] dataBuf, Double nodata) {
        int numQuantile = 15;
        Double[] quantileBreaks = new Double[numQuantile - 1];
        List<Float> dataBufList = new ArrayList<>();
        for (float dataBufVal : dataBuf) {
            dataBufList.add(dataBufVal);
        }
        Collections.sort(dataBufList);
        int lastNoDataIndex = dataBufList.lastIndexOf(nodata.floatValue());
        int lastDataIndex = dataBufList.size() - 1;
        int numDataInQuantile = (int) ((lastDataIndex - lastNoDataIndex) / numQuantile + 0.5);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numQuantile - 1; i++) {
            quantileBreaks[i] = Double.valueOf(dataBufList.get(lastNoDataIndex + (i + 1) * numDataInQuantile));
            sb.append(" ");
            sb.append(quantileBreaks[i].toString());
        }
        return sb.toString().substring(1);
    }
}
