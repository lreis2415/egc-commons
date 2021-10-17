package org.egc.commons.gis;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.*;
import com.google.common.primitives.Floats;
import it.geosolutions.jaiext.range.NoDataContainer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.exception.BusinessException;
import org.egc.commons.util.StringUtil;
import org.gdal.gdal.*;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.ogr.ogr;
import org.gdal.osr.SpatialReference;
import org.gdal.osr.osr;
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
import org.opengis.referencing.crs.ProjectedCRS;
import org.osgeo.proj4j.CRSFactory;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    static {
        gdal.UseExceptions();
        ogr.UseExceptions();
        osr.UseExceptions();
        gdal.AllRegister();
        ogr.RegisterAll();
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
        //gdal.SetConfigOption("GDAL_DATA", "C:\\Program Files\\GDAL\\gdal-data" );
    }

    /**
     * Read geotiff file.
     *
     * @param tif the geotiff
     * @return the grid coverage 2 d
     */
    public static GridCoverage2D read(String tif) {
        String msg;
        File rasterFile = new File(tif);
        GridCoverage2DReader reader = null;
        try {
            reader = new GeoTiffReader(new FileInputStream(rasterFile));
        } catch (DataSourceException e) {
            msg = "Error in GeoTIFF file!";
            throw new BusinessException(e, msg + e.getLocalizedMessage(), true);
        } catch (FileNotFoundException e) {
            throw new BusinessException("GeoTIFF file not found! " + e.getLocalizedMessage(), true);
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
        if (crs instanceof ProjectedCRS) {
            metadata.setProjected(true);
        }
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
            throw new BusinessException(e, "Process raster file statistics failed");
        }
        metadata.setMinValue(results.value(0, Statistic.MIN));
        metadata.setMaxValue(results.value(0, Statistic.MAX));
        metadata.setMeanValue(results.value(0, Statistic.MEAN));
        metadata.setSdev(results.value(0, Statistic.SDEV));
        return metadata;
    }

    /**
     * Gets metadata using GDAL.
     *
     * @param tif          the tif
     * @param uniqueValues calculate unique values?
     * @param quantile     calculate quantiles?
     * @return the metadata by gdal
     */
    public static RasterMetadata getMetadataByGDAL(String tif, boolean uniqueValues, boolean quantile) {
        long s = System.currentTimeMillis();
        StringUtil.isNullOrEmptyPrecondition(tif, "Raster file must exists");
//        gdal.AllRegister();
        RasterMetadata metadata = new RasterMetadata();
        final Dataset dataset = gdal.Open(tif, gdalconstConstants.GA_ReadOnly);
        final Driver driver = dataset.GetDriver();
        metadata.setFormat(driver.getShortName());
        SpatialReference sr = new SpatialReference(dataset.GetProjection());
        metadata.setCrsProj4(sr.ExportToProj4());
        metadata.setCrsWkt(sr.ExportToWkt());
        String authorityCode = sr.GetAuthorityCode(null);
        // 存在一种情况，authorityCode = null，
        // epsg 存在，但是 epsg 是 GEOGCS 的编码，而不是 PROJCS对应的编码
        String epsg = sr.GetAttrValue("AUTHORITY", 1);
        if (StringUtils.isNotBlank(epsg)) {
            metadata.setEpsg(Integer.parseInt(epsg));
        } else {
            log.error("Cannot get valid EPSG code from [ {} ]!", tif);
        }
        if (StringUtils.isNotBlank(authorityCode)) {
            Integer srid = Integer.parseInt(authorityCode);
            metadata.setSrid(srid);
        } else {
            log.warn("Authority Code of {} is null!", tif);
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

        final Band band = dataset.GetRasterBand(1);
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

        if (quantile || uniqueValues) {
            float[] dataBuf = new float[xSize * ySize];
            band.ReadRaster(0, 0, xSize, ySize, dataBuf);
            if (quantile) {
                metadata.setQuantileBreaks(getQuantileByGuava(dataBuf, nodata, 15));
            }
            if (uniqueValues) {
                metadata.setUniqueValues(getUniqueValues(dataBuf, nodata));
            }
        }
        closeDataSet(dataset);
        log.debug("time used {} ms", System.currentTimeMillis() - s);
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
        return getMetadataByGDAL(tif, true, true);
    }

    /*原来的计算方法参考ws_metadata_extract/RasterMetaDataExtractor.cs*/

    private static String getUniqueValues(float[] dataBuf, Double nodata) {
        Arrays.sort(dataBuf);
        List<Float> dataBufList = Floats.asList(dataBuf);
        //release
        dataBuf = null;
        //Collections.sort(dataBufList);
        List<Float> uniqueList = dataBufList.stream().distinct().collect(Collectors.toList());
        if (uniqueList.indexOf(nodata.floatValue()) > -1) {
            uniqueList.remove(uniqueList.indexOf(nodata.floatValue()));
        }
        return Joiner.on(" ").join(uniqueList);
    }

    /**
     * 分位数 use guava
     * 比之前的稍微快一点
     *
     * @param dataBuf     dataBuf
     * @param nodata      nodata
     * @param numQuantile num of quantiles
     * @return quantile
     */
    private static String getQuantileByGuava(float[] dataBuf, Double nodata, int numQuantile) {
        long start = System.currentTimeMillis();
        Double[] quantileBreaks = new Double[numQuantile - 1];
        List<Float> dataBufList = Floats.asList(dataBuf);
        //dataBuf = null;
        //移除空值, 移除 nodata
        Collection<Float> removedList = Collections2.filter(dataBufList, input -> !Objects.equals(nodata.floatValue(), input));

        Ordering<Float> floatOrdering = Ordering.natural();
        List<Float> sortedRemovedList = floatOrdering.sortedCopy(removedList);
        int size = sortedRemovedList.size();
        //分位数位置  (n+1)*p, 0<p<1, 如  0.25，0.5,0.75
        int numDataInQuantile = ((size + 1) / numQuantile);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numQuantile - 1; i++) {
            quantileBreaks[i] = (double) (sortedRemovedList.get((i + 1) * numDataInQuantile));
            sb.append(" ");
            sb.append(quantileBreaks[i].toString());
        }
        log.debug("getQuantileByGuava time used {}", System.currentTimeMillis() - start);
        return sb.toString().substring(1);
    }

    /**
     * 分位数
     *
     * @param dataBuf     dataBuf
     * @param nodata      nodata
     * @param numQuantile num of quantiles
     * @return quantile
     * @deprecated use {@link #getQuantileByGuava(float[], Double, int)}
     */
    private static String getQuantile(float[] dataBuf, Double nodata, int numQuantile) {
        long start = System.currentTimeMillis();
        Double[] quantileBreaks = new Double[numQuantile - 1];
        List<Float> dataBufList = Floats.asList(dataBuf);
        dataBuf = null;
        //移除空值, 移除 nodata
        List<Float> removedList = dataBufList.stream().filter(x -> {
            if (x != null) {
                //为 true 的会被保留
                return !x.equals(nodata.floatValue());
            }
            return false;
        }).sorted().collect(Collectors.toList());
        dataBufList = null;
        int size = removedList.size();
        //分位数位置  (n+1)*p, 0<p<1, 如  0.25，0.5,0.75
        int numDataInQuantile = ((size + 1) / numQuantile);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numQuantile - 1; i++) {
            quantileBreaks[i] = (double) (removedList.get((i + 1) * numDataInQuantile));
            sb.append(" ");
            sb.append(quantileBreaks[i].toString());
        }
        log.debug("getQuantile time used {}", System.currentTimeMillis() - start);
        return sb.toString().substring(1);
    }

    public static Area getArea(Dataset dataset) {
        SpatialReference sr = new SpatialReference(dataset.GetProjectionRef());
        Band band = dataset.GetRasterBand(1);
        Double[] nodataVal = new Double[1];
        band.GetNoDataValue(nodataVal);
        Double nodata = nodataVal[0];
        if (nodata == null) {
            nodata = -9999d;
        }
        int xSize = dataset.GetRasterXSize();
        int ySize = dataset.GetRasterYSize();

        float[] dataBuf = new float[xSize * ySize];
        double[] gt = dataset.GetGeoTransform();
        band.ReadRaster(0, 0, xSize, ySize, dataBuf);
        double wePixelResolution = gt[1];
        double nsPixelResolution = Math.abs(gt[5]);
        int count = 0;
        for (float d : dataBuf) {
            if (d != nodata.floatValue()) {
                count++;
            }
        }
        return new Area(count * wePixelResolution * nsPixelResolution, sr.GetLinearUnitsName());
    }

    public static Area getArea(String rasterFile) {
        StringUtil.isNullOrEmptyPrecondition(rasterFile, "Raster file must exists");
        //gdal.AllRegister();
        final Dataset dataset = gdal.Open(rasterFile, gdalconstConstants.GA_ReadOnly);
        Area area = getArea(dataset);
        closeDataSet(dataset);
        return area;
    }

    private static float area(float[] dataBuf, Double nodata,
                              int wePixelResolution, int nsPixelResolution) {
        int count = 0;
        for (float d : dataBuf) {
            if (d != nodata.floatValue()) {
                count++;
            }
        }
        return count * wePixelResolution * nsPixelResolution;
    }

    /**
     * Reproject use epsg code.
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @param dstEpsg the dst epsg
     */
    public static void reprojectUseEpsg(String srcFile, String dstFile, int dstEpsg) {
        reproject(srcFile, dstFile, "EPSG:" + dstEpsg);
    }

    /**
     * Reproject use PROJ.4 declarations.
     *
     * @param srcFile  the src file
     * @param dstFile  the dst file
     * @param dstProj4 the dst proj4
     */
    public static void reprojectUseProj4(String srcFile, String dstFile, String dstProj4) {
        reproject(srcFile, dstFile, dstProj4);
    }

    public static String formatConvert(String srcFile, String dstFile, GDALDriversEnum format) throws IOException {
        Dataset dataset = readUseGdal(srcFile);
        String ext = format.getExtension();
        String newName = outputName(srcFile, dstFile, ext);
        log.info("Convert from {} to {}", srcFile, newName);
        Driver driver = gdal.GetDriverByName(format.name());
        if (driver == null) {
            log.error("Output Format {} Not Supported", ext);
            throw new IOException("Output Format " + ext + " Not Supported");
        }
        Vector<String> vector = new Vector<String>();
        vector.add("-of");
        vector.add(driver.getShortName());

        //        vector.add("-outsize");
        //        vector.add("128");
        //        vector.add("128");

        TranslateOptions options = new TranslateOptions(vector);
        Dataset translate = gdal.Translate(newName, dataset, options);
        closeDataSet(dataset);
        return translate != null ? newName : null;
    }

    /**
     * Reproject use gdal.Warp
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @param dstSrs  the target spatial reference
     */
    public static void reproject(String srcFile, String dstFile, String dstSrs) {
        final Dataset ds = readUseGdal(srcFile);
        Vector<String> v = new Vector<>();
        v.add("-t_srs");
        v.add(dstSrs);
        v.add("-overwrite");
        WarpOptions options = new WarpOptions(v);
        gdal.Warp(dstFile, new Dataset[]{ds}, options);
        //关闭数据集
        closeDataSet(ds);
        //不要写下面这句，gdal只会在本类初始化时register一次，关闭后就无法再使用了
//        gdal.GDALDestroyDriverManager();
    }

    private static Dataset readUseGdal(String file) {
        //gdal.AllRegister();
        // 默认 gdalconst.GA_ReadOnly
        return gdal.Open(file);
    }

    private static String outputName(String srcFile, String dstFile, String ext) {
        String newName;
        String inPath = FilenameUtils.getFullPath(srcFile);
        String outPath = FilenameUtils.getFullPath(dstFile);
        if (StringUtils.isBlank(dstFile)) {
            newName = inPath + File.separator + FilenameUtils.getBaseName(srcFile) + "." + ext;
        } else {
            if (StringUtils.isBlank(outPath)) {
                newName = inPath + File.separator + FilenameUtils.getBaseName(dstFile) + "." + ext;
            } else {
                newName = outPath + File.separator + FilenameUtils.getBaseName(dstFile) + "." + ext;
            }
        }
        return newName;
    }

    /**
     * Closes the given {@link Dataset}.
     *
     * @param ds {@link Dataset} to close.
     */
    public static void closeDataSet(Dataset ds) {
        if (ds == null) {
            throw new NullPointerException("The provided dataset is null");
        }
        try {
            ds.delete();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw new BusinessException(e.getLocalizedMessage());
        }
    }
}
