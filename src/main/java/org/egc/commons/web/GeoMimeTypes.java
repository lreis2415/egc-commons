package org.egc.commons.web;

import java.io.File;
import java.nio.file.Path;

/**
 * <pre>
 * 空间数据的MimeType
 * MIME(Multipurpose Internet Mail Extensions)多用途互联网邮件扩展类型
 * 普通文件的MimeType常量(例如“application/json”)可通过下述两个类之一获得
 * {@link org.springframework.http.MediaType}
 * {@link com.google.common.net.MediaType}
 * 对于一个具体的非空间数据文件，可使用下述方法之一获取其MimeType<br/>
 * 1. {@link javax.activation.MimetypesFileTypeMap#getContentType(File)}
 * 2. {@link java.nio.file.Files#probeContentType(Path)}<br/>
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/9/13 16:34
 */
public class GeoMimeTypes {

    public static final String APPLICATION_DBASE = "application/dbase";
    public static final String APPLICATION_DGN = "application/dgn";
    public static final String APPLICATION_HDF4_EOS = "application/hdf4-eos";
    public static final String APPLICATION_IMAGE_ASCII_GRASS = "application/image-ascii-grass";
    public static final String APPLICATION_X_ZIPPED_SHP = "application/x-zipped-shp";
    public static final String APPLICATION_X_ZIPPED_WKT = "application/x-zipped-wkt";
    public static final String APPLICATION_ZIP = "application/zip";
    public static final String IMAGE_GEOTIFF = "image/geotiff";
    public static final String IMAGE_X_ZIPPED_GEOTIFF = "image/x-zipped-geotiff";
    public static final String IMAGE_X_ZIPPED_TIFF = "image/x-zipped-tiff";
    public static final String TEXT_CSV = "text/csv";
    public static final String APPLICATION_SHP = "application/shp";
    public static final String APPLICATION_IMG = "application/img";
    public static final String APPLICATION_GEOTIFF = "application/geotiff";
    public static final String IMAGE_TIFF = "image/tiff";
    public static final String APPLICATION_REMAP = "application/remap";
    public static final String APPLICATION_X_GEOTIFF = "application/x-geotiff";
    public static final String APPLICATION_X_ERDAS_HFA = "application/x-erdas-hfa";
    public static final String APPLICATION_NETCDF = "application/netcdf";
    public static final String APPLICATION_X_NETCDF = "application/x-netcdf";
    public static final String APPLICATION_VND_GOOGLE_EARTH_KML_XML = "application/vnd.google-earth.kml+xml";
    public static final String APPLICATION_HDF4_EOSHDF = "application/hdf4-eoshdf";
    public static final String APPLICATION_X_RDA = "application/x-rda";
    public static final String APPLICATION_X_OGC_PLAYLIST = "application/x-ogc-playlist";
    public static final String APPLICATION_RDATA = "application/rData";
    public static final String APPLICATION_RDATA_SPATIAL = "application/rData+Spatial";
    public static final String APPLICATION_RDATA_SPATIALPOINTS = "application/rData+SpatialPoints";
    public static final String APPLICATION_RDATA_SPATIALPOLYGONS = "application/rData+SpatialPolygons";

}
