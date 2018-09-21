package org.egc.commons.raster;

import org.egc.commons.exception.BusinessException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.Envelope2D;
import org.geotools.process.classify.ClassificationMethod;
import org.geotools.process.raster.CoverageClassStats;
import org.geotools.referencing.CRS;
import org.geotools.resources.coverage.CoverageUtilities;
import org.jaitools.numeric.Statistic;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.osgeo.proj4j.CRSFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 使用geotools读取geotiff，获取元数据信息
 * 参考
 * https://gis.stackexchange.com/questions/106882/reading-each-pixel-of-each-band-of-multiband-geotiff-with-geotools-java
 *
 * @author houzhiwei
 * @date 2018/9/21
 */
public class GeoTiffUtil {

    private static final Logger logger = LoggerFactory.getLogger(GeoTiffUtil.class);

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
            logger.error(msg, e);
            throw new BusinessException(e, msg + e.getLocalizedMessage());
        } catch (FileNotFoundException e) {
            msg = "GeoTIFF file not found! ";
            logger.error(msg, e);
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
            logger.error(msg, e);
            throw new BusinessException(msg + e.getLocalizedMessage());
        }
        return coverage;
    }

    /**
     * Read geotiff and return metadata.
     *
     * @param tif the tif
     * @return the raster metadata
     * @throws IOException the io exception
     */
    public static RasterMetadata getGeoTiffMetadata(String tif) {
        GridCoverage2D coverage2D = read(tif);
        RasterMetadata metadata = getCoverageMetadata(coverage2D);
        metadata.setFormat("GeoTIFF");
        return metadata;
    }

    /**
     * <pre>
     * Read metadata raster metadata.
     * format not set
     * </pre>
     *
     * @param coverage the coverage
     * @return the raster metadata
     * @throws IOException the io exception
     */
    public static RasterMetadata getCoverageMetadata(GridCoverage2D coverage) {
        RasterMetadata metadata = new RasterMetadata();
        double nodata = CoverageUtilities.getNoDataProperty(coverage).getAsSingleValue();
        metadata.setNodata(nodata);
        CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem();
        metadata.setCrsWkt(crs.toWKT());
        try {
            metadata.setEpsg(CRS.lookupEpsgCode(crs, true));
        } catch (FactoryException e) {
            logger.error("Error occured while searching for the identifier.", e);
            throw new BusinessException(e, "Error occured while searching for the CRS identifier");
        }
        CRSFactory csFactory = new CRSFactory();
        org.osgeo.proj4j.CoordinateReferenceSystem crsProj = csFactory.createFromName(crs.getIdentifiers().toArray()[0].toString());
        metadata.setProjStr(crsProj.getProjection().getPROJ4Description());
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
            logger.error("Process raster file statistics failed", e);
            throw new BusinessException(e, "Process raster file statistics failed");
        }
        metadata.setMinValue(results.value(0, Statistic.MIN));
        metadata.setMaxValue(results.value(0, Statistic.MAX));
        metadata.setMeanValue(results.value(0, Statistic.MEAN));
        metadata.setSdev(results.value(0, Statistic.SDEV));
        return metadata;
    }
}
