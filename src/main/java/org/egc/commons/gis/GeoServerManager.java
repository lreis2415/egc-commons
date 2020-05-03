package org.egc.commons.gis;

import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.exception.BusinessException;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
public class GeoServerManager {

    public static final String VECTOR_STORE = "vector";
    public static final String RASTER_STORE = "raster";
    public static final String SAMPLE_STORE = "sample";
    public static final String DEFAULT_SRS = "EPSG:4326";

    public static boolean publishGeoTiff(@NotNull GeoServerRESTManager manager, String workspace, String storeName, @NotNull File geoTiff, String srs, String styleName) throws FileNotFoundException {
        checkWorkspace(manager, workspace);
        if (StringUtils.isBlank(srs)) {
            srs = DEFAULT_SRS;
        }
        if (StringUtils.isBlank(storeName)) {
            storeName = RASTER_STORE;
        }
        boolean b = manager.getPublisher().publishExternalGeoTIFF(workspace, storeName, geoTiff, geoTiff.getName(), srs, GSResourceEncoder.ProjectionPolicy.FORCE_DECLARED, styleName);
        return b;
    }

    /**
     * publish zipped shapefile to GeoServer
     *
     * @param manager   GeoServer REST Manager
     * @param workspace user's workspace name
     * @param storeName store name
     * @param zipFile   zipped shapefile
     * @param srs       "EPSG:4326" if null. the srs for this shapefile.
     *                  It will be forced to use this one in GeoServer
     * @param styleName style intend to use for the shapefile. can be null
     * @return
     * @throws FileNotFoundException
     */
    public static boolean publishShapefile(@NotNull GeoServerRESTManager manager, String workspace, String storeName, @NotNull File zipFile, String srs, String styleName) throws FileNotFoundException {
        checkWorkspace(manager, workspace);
        String layerName = getShpNameFromZippedFile(zipFile);
        if (!StringUtils.isNotBlank(srs)) {
            srs = DEFAULT_SRS;
        }
        if (!StringUtils.isNotBlank(storeName)) {
            storeName = VECTOR_STORE;
        }
        boolean b = manager.getPublisher().publishShp(workspace, storeName, layerName, zipFile, srs, styleName);
        log.info("Shapefile {} published to workspace {} in store {} with style {}", layerName, workspace, storeName, styleName);
        return b;
    }

    public boolean publishStyle(@NotNull GeoServerRESTManager manager, @NotNull File sldFile) throws FileNotFoundException, IOException {
        boolean b = false;
        String name = FilenameUtils.getBaseName(sldFile.getName());
        if (!manager.getReader().existsStyle(name)) {
            b = manager.getPublisher().publishStyle(sldFile, name);
            log.info("Publishing style {} as {}", sldFile, name);
        }
        log.info(name + " already exists!");
        return b;
    }

    public static void checkWorkspace(GeoServerRESTManager manager, String workspace) {
        if (!manager.getReader().existsWorkspace(workspace)) {
            manager.getPublisher().createWorkspace(workspace);
        }
    }

    /**
     * get *.shp filename from zipped shapefile
     *
     * @param zipShp zipped file path of shapefile
     * @return *.shp filename
     */
    public static String getShpNameFromZippedFile(File zipShp) {
        String layerName = null;
        try {
            ZipFile zipFile = new ZipFile(zipShp);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            String name;
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                name = entry.getName();
                if ("shp".equalsIgnoreCase(FilenameUtils.getExtension(name))) {
                    layerName = FilenameUtils.getBaseName(name);
                    break;
                }
            }
        } catch (IOException ex) {
            log.error("Read zipped shapefile failed.");
            throw new BusinessException("Read zipped shapefile failed.");
        }
        return layerName;
    }
}
