package org.egc.commons.gis;

import lombok.Getter;
import org.egc.commons.util.EnumUtils;

import java.util.function.Function;

/**
 * Description
 * <pre>
 * GDAL Driver Names
 * For GDAL 2.1.3, released 2017/20/01
 * 不带下划线的推荐使用.{@link #name}方式获取名称
 * 带下划线的使用.{@link #getName()}方式获取名称
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/9
 */
@Getter
public enum GDALDriversEnum {
    BAG("BAG", "raster", "read-only", "Bathymetry Attributed Grid", "bag"),
    FITS("FITS", "raster", "read, write and update", "Flexible Image Transport System", "fits"),
    GMT("GMT", "raster", "read or write", "GMT NetCDF Grid Format", ""),
    HDF4("HDF4", "raster", "ros", "Hierarchical Data Format Release 4", "hdf"),
    HDF4Image("HDF4Image", "raster", "read, write and update", "HDF4 Dataset", ""),
    HDF5("HDF5", "raster", "ros", "Hierarchical Data Format Release 5", "hdf5"),
    HDF5Image("HDF5Image", "raster", "read-only", "HDF5 Dataset", ""),
    KEA("KEA", "raster", "read, write and update", "KEA Image Format", "kea"),
    netCDF("netCDF", "raster, vector", "rw+s", "Network Common Data Format", "nc"),
    VRT("VRT", "raster", "read, write and update, supporting virtual IO", "Virtual Raster", "vrt"),
    GTiff("GTiff", "raster", "read, write and update, supporting virtual IO and subdatasets", "GeoTIFF", "tif"),
    NITF("NITF", "raster", "read, write and update, supporting virtual IO and subdatasets",
            "National Imagery Transmission Format", "ntf"),
    RPFTOC("RPFTOC", "raster", "rovs", "Raster Product Format TOC format", "toc"),
    ECRGTOC("ECRGTOC", "raster", "rovs", "ECRG TOC format", "xml"),
    HFA("HFA", "raster", "read, write and update, supporting virtual IO", "Erdas Imagine Images", "img"),
    SAR_CEOS("SAR_CEOS", "raster", " read-only and virtual IO", "CEOS SAR Image", ""),
    CEOS("CEOS", "raster", " read-only and virtual IO", "CEOS Image", ""),
    JAXAPALSAR("JAXAPALSAR", "raster", " read-only and virtual IO", "JAXA PALSAR Product Reader (Level 1.1/1.5)", ""),
    GFF("GFF", "raster", " read-only and virtual IO", "Ground-based SAR Applications Testbed File Format", "gff"),
    ELAS("ELAS", "raster", "read, write and update, supporting virtual IO", "ELAS", ""),
    AIG("AIG", "raster", " read-only and virtual IO", "Arc/Info Binary Grid", "adf"),
    AAIGrid("AAIGrid", "raster", "rwv", "Arc/Info ASCII Grid", "asc"),
    GRASSASCIIGrid("GRASSASCIIGrid", "raster", " read-only and virtual IO", "GRASS ASCII Grid", ""),
    SDTS("SDTS", "raster", " read-only and virtual IO", "SDTS Raster", "ddf"),
    DTED("DTED", "raster", "rwv", "DTED Elevation Raster", "dt0/dt1/dt2"),
    PNG("PNG", "raster", "rwv", "Portable Network Graphics", "png"),
    JPEG("JPEG", "raster", "rwv", "JPEG JFIF", "jpg"),
    MEM("MEM", "raster", "read, write and update", "In Memory Raster", ""),
    JDEM("JDEM", "raster", " read-only and virtual IO", "Japanese DEM", "mem"),
    GIF("GIF", "raster", "rwv", "Graphics Interchange Format", "gif"),
    BIGGIF("BIGGIF", "raster", " read-only and virtual IO", "Graphics Interchange Format", "gif"),
    ESAT("ESAT", "raster", " read-only and virtual IO", "Envisat Image Format", "n1"),
    BSB("BSB", "raster", " read-only and virtual IO", "Maptech BSB Nautical Charts", "kap"),
    XPM("XPM", "raster", "rwv", "X11 PixMap Format", "xpm"),
    BMP("BMP", "raster", "read, write and update, supporting virtual IO", "MS Windows Device Independent Bitmap",
            "bmp"),
    DIMAP("DIMAP", "raster", " read-only and virtual IO", "SPOT DIMAP", ""),
    AirSAR("AirSAR", "raster", " read-only and virtual IO", "AirSAR Polarimetric Image", ""),
    RS2("RS2", "raster", "ros", "RadarSat 2 XML Product", ""),
    SAFE("SAFE", "raster", " read-only and virtual IO", "Sentinel-1 SAR SAFE Product", ""),
    PCRaster("PCRaster", "raster", "read, write and update", "PCRaster Raster File", "map"),
    ILWIS("ILWIS", "raster", "read, write and update, supporting virtual IO", "ILWIS Raster Map", "mpr/mpl"),
    SGI("SGI", "raster", "read, write and update", "SGI Image File Format 1.0", "rgb"),
    SRTMHGT("SRTMHGT", "raster", "rwv", "SRTMHGT File Format", "hgt"),
    Leveller("Leveller", "raster", "read, write and update", "Leveller heightfield", ""),
    Terragen("Terragen", "raster", "read, write and update", "Terragen heightfield", "ter"),
    ISIS3("ISIS3", "raster", " read-only and virtual IO", "USGS Astrogeology ISIS cube (Version 3)", ""),
    ISIS2("ISIS2", "raster", "read, write and update, supporting virtual IO", "USGS Astrogeology ISIS cube (Version 2)",
            ""),
    PDS("PDS", "raster", " read-only and virtual IO", "NASA Planetary Data System", ""),
    VICAR("VICAR", "raster", " read-only and virtual IO", "MIPL VICAR file", ""),
    TIL("TIL", "raster", " read-only and virtual IO", "EarthWatch .TIL", ""),
    ERS("ERS", "raster", "read, write and update, supporting virtual IO", "ERMapper .ers Labelled", "ers"),
    JP2OpenJPEG("JP2OpenJPEG", "raster, vector", "rwv", "JPEG-2000 driver based on OpenJPEG library", "jp2"),
    L1B("L1B", "raster", "rovs", "NOAA Polar Orbiter Level 1b Data Set", ""),
    FIT("FIT", "raster", "rwv", "FIT Image", ""),
    GRIB("GRIB", "raster", " read-only and virtual IO", "GRIdded Binary", "grb"),
    RMF("RMF", "raster", "read, write and update, supporting virtual IO", "Raster Matrix Format", "rsw"),
    WCS("WCS", "raster", "rovs", "OGC Web Coverage Service", ""),
    WMS("WMS", "raster", "rwvs", "OGC Web Map Service", ""),
    MSGN("MSGN", "raster", "read-only", "EUMETSAT Archive native", "nat"),
    RST("RST", "raster", "read, write and update, supporting virtual IO", "Idrisi Raster A.1", "rst"),
    INGR("INGR", "raster", "read, write and update, supporting virtual IO", "Intergraph Raster", ""),
    GSAG("GSAG", "raster", "rwv", "Golden Software ASCII Grid (.grd)", ""),
    GSBG("GSBG", "raster", "read, write and update, supporting virtual IO", "Golden Software Binary Grid (.grd)", ""),
    GS7BG("GS7BG", "raster", "read, write and update, supporting virtual IO", "Golden Software 7 Binary Grid (.grd)",
            ""),
    COSAR("COSAR", "raster", " read-only and virtual IO", "COSAR Annotated Binary Matrix (TerraSAR-X)", ""),
    TSX("TSX", "raster", " read-only and virtual IO", "TerraSAR-X Product", ""),
    COASP("COASP", "raster", "read-only", "DRDC COASP SAR Processor Raster", ""),
    R("R", "raster", "rwv", "R Object Data Store", "rda"),
    MAP("MAP", "raster", " read-only and virtual IO", "OziExplorer .MAP", ""),
    KMLSUPEROVERLAY("KMLSUPEROVERLAY", "raster", "rwv", "Kml Super Overlay", ""),
    Rasterlite("Rasterlite", "raster", "rws", "Rasterlite", "sqlite"),
    MBTiles("MBTiles", "raster", "read, write and update, supporting virtual IO", "MBTiles", "mbtiles"),
    PLMOSAIC("PLMOSAIC", "raster", "read-only", "Planet Labs Mosaics API", ""),
    CALS("CALS", "raster", "read or write", "CALS (Type 1)", ""),
    WMTS("WMTS", "raster", "rwv", "OGC Web Mab Tile Service", ""),
    SENTINEL2("SENTINEL2", "raster", "rovs", "Sentinel 2", ""),
    MRF("MRF", "raster", "read, write and update, supporting virtual IO", "Meta Raster Format", ""),
    PNM("PNM", "raster", "read, write and update, supporting virtual IO", "Portable Pixmap Format (netpbm)", "pnm"),
    DOQ1("DOQ1", "raster", " read-only and virtual IO", "USGS DOQ (Old Style)", ""),
    DOQ2("DOQ2", "raster", " read-only and virtual IO", "USGS DOQ (New Style)", ""),
    GenBin("GenBin", "raster", " read-only and virtual IO", "Generic Binary (.hdr Labelled)", "hdr"),
    PAux("PAux", "raster", "read, write and update", "PCI .aux Labelled", ""),
    MFF("MFF", "raster", "read, write and update, supporting virtual IO", "Vexcel MFF Raster", "hdr"),
    MFF2("MFF2", "raster", "read, write and update", "Vexcel MFF2 (HKV) Raster", ""),
    FujiBAS("FujiBAS", "raster", "read-only", "Fuji BAS Scanner Image", ""),
    GSC("GSC", "raster", " read-only and virtual IO", "GSC Geogrid", ""),
    FAST("FAST", "raster", " read-only and virtual IO", "EOSAT FAST Format", ""),
    BT("BT", "raster", "read, write and update, supporting virtual IO", "VTP .bt (Binary Terrain) 1.3 Format", "bt"),
    LAN("LAN", "raster", "read, write and update, supporting virtual IO", "Erdas .LAN/.GIS", ""),
    CPG("CPG", "raster", "read-only", "Convair PolGASP", ""),
    IDA("IDA", "raster", "read, write and update, supporting virtual IO", "Image Data and Analysis", ""),
    NDF("NDF", "raster", " read-only and virtual IO", "NLAPS Data Format", ""),
    EIR("EIR", "raster", " read-only and virtual IO", "Erdas Imagine Raw", ""),
    DIPEx("DIPEx", "raster", " read-only and virtual IO", "DIPEx", ""),
    LCP("LCP", "raster", "rwv", "FARSITE v.4 Landscape File", "lcp"),
    GTX("GTX", "raster", "read, write and update, supporting virtual IO", "NOAA Vertical Datum .GTX", "gtx"),
    LOSLAS("LOSLAS", "raster", " read-only and virtual IO", "NADCON .los/.las Datum Grid Shift", "los/las"),
    NTv2("NTv2", "raster", "read, write and update, supporting virtual IO and subdatasets", "NTv2 Datum Grid Shift",
            "gsb"),
    CTable2("CTable2", "raster", "read, write and update, supporting virtual IO", "CTable2 Datum Grid Shift", ""),
    ACE2("ACE2", "raster", " read-only and virtual IO", "ACE2", "ACE2"),
    SNODAS("SNODAS", "raster", " read-only and virtual IO", "Snow Data Assimilation System", "hdr"),
    KRO("KRO", "raster", "read, write and update, supporting virtual IO", "KOLOR Raw", "kro"),
    ROI_PAC("ROI_PAC", "raster", "read, write and update, supporting virtual IO", "ROI_PAC raster", ""),
    ENVI("ENVI", "raster", "read, write and update, supporting virtual IO", "ENVI .hdr Labelled", "bsq/bil/bip"),
    EHdr("EHdr", "raster", "read, write and update, supporting virtual IO", "ESRI .hdr Labelled", "hdr/bil"),
    ISCE("ISCE", "raster", "read, write and update, supporting virtual IO", "ISCE raster", ""),
    ARG("ARG", "raster", "rwv", "Azavea Raster Grid format", ""),
    RIK("RIK", "raster", " read-only and virtual IO", "Swedish Grid RIK", "rik"),
    USGSDEM("USGSDEM", "raster", "rwv", "USGS Optional ASCII DEM (and CDED)", "dem"),
    GXF("GXF", "raster", "read-only", "GeoSoft Grid Exchange Format", "gxf"),
    NWT_GRD("NWT_GRD", "raster", " read-only and virtual IO", "Northwood Numeric Grid Format ", "grd"),
    NWT_GRC("NWT_GRC", "raster", " read-only and virtual IO", "Northwood Classified Grid Format ", "grc"),
    ADRG("ADRG", "raster", "read, write and update, supporting virtual IO and subdatasets",
            "ARC Digitized Raster Graphics", "gen"),
    SRP("SRP", "raster", "rovs", "Standard Raster Product (ASRP/USRP)", "img"),
    BLX("BLX", "raster", "rwv", "Magellan topo", "blx"),
    PostGISRaster("PostGISRaster", "raster", "rws", "PostGIS Raster driver", ""),
    SAGA("SAGA", "raster", "read, write and update, supporting virtual IO", "SAGA GIS Binary Grid", "sdat"),
    XYZ("XYZ", "raster", "rwv", "ASCII Gridded XYZ", "xyz"),
    HF2("HF2", "raster", "rwv", "HF2/HFZ heightfield raster", "hf2"),
    OZI("OZI", "raster", " read-only and virtual IO", "OziExplorer Image File", ""),
    CTG("CTG", "raster", " read-only and virtual IO", "USGS LULC Composite Theme Grid", ""),
    E00GRID("E00GRID", "raster", " read-only and virtual IO", "Arc/Info Export E00 GRID", "e00"),
    ZMap("ZMap", "raster", "rwv", "ZMap Plus Grid", "dat"),
    NGSGEOID("NGSGEOID", "raster", " read-only and virtual IO", "NOAA NGS Geoid Height Grids", "bin"),
    IRIS("IRIS", "raster", " read-only and virtual IO", "IRIS data (.PPI, .CAPPi etc)", "ppi"),
    DB2ODBC("DB2ODBC", "raster, vector", "read, write and update", "IBM DB2 Spatial Database", ""),
    PCIDSK("PCIDSK", "raster, vector", "read, write and update, supporting virtual IO", "PCIDSK Database File", "pix"),
    PDF("PDF", "raster, vector", "w+", "Geospatial PDF", "pdf"),
    ESRI_Shapefile("ESRI Shapefile", "vector", "read, write and update, supporting virtual IO", "ESRI Shapefile", ""),
    MapInfo_File("MapInfo File", "vector", "read, write and update, supporting virtual IO", "MapInfo File", ""),
    UK_NTF("UK .NTF", "vector", "read-only", "UK .NTF", ""),
    OGR_SDTS("OGR_SDTS", "vector", "read-only", "SDTS", ""),
    S57("S57", "vector", "read, write and update, supporting virtual IO", "IHO S-57 (ENC)", "000"),
    DGN("DGN", "vector", "read, write and update", "Microstation DGN", "dgn"),
    OGR_VRT("OGR_VRT", "vector", " read-only and virtual IO", "VRT,Virtual Datasource", "vrt"),
    REC("REC", "vector", "read-only", "EPIInfo .REC", "rec"),
    Memory("Memory", "vector", "read, write and update", "Memory", ""),
    BNA("BNA", "vector", "read, write and update, supporting virtual IO", "Atlas BNA", "bna"),
    CSV("CSV", "vector", "read, write and update, supporting virtual IO", "Comma Separated Value", "csv"),
    GML("GML", "vector", "read, write and update, supporting virtual IO", "Geography Markup Language(GML)", "gml"),
    GPX("GPX", "vector", "read, write and update, supporting virtual IO", "GPX", "gpx"),
    KML("KML", "vector", "read, write and update, supporting virtual IO", "Keyhole Markup Language (KML)", "kml"),
    GeoJSON("GeoJSON", "vector", "read, write and update, supporting virtual IO", "GeoJSON", "json"),
    OGR_GMT("OGR_GMT", "vector", "read, write and update", "GMT ASCII Vectors", "gmt"),
    SQLite("SQLite", "vector", "read, write and update, supporting virtual IO", "SQLite / Spatialite", "sqlite"),
    ODBC("ODBC", "vector", "read, write and update", "ODBC", ""),
    WAsP("WAsP", "vector", "read, write and update, supporting virtual IO", "WAsP .map format", "map"),
    PGeo("PGeo", "vector", "read-only", "ESRI Personal GeoDatabase", ""),
    MSSQLSpatial("MSSQLSpatial", "vector", "read, write and update", "Microsoft SQL Server Spatial Database", ""),
    OpenFileGDB("OpenFileGDB", "vector", " read-only and virtual IO", "ESRI FileGDB", "gdb"),
    XPlane("XPlane", "vector", " read-only and virtual IO", "X-Plane/Flightgear aeronautical data", "dat"),
    DXF("DXF", "vector", "read, write and update, supporting virtual IO", "AutoCAD DXF", "dxf"),
    Geoconcept("Geoconcept", "vector", "read, write and update", "Geoconcept", ""),
    GeoRSS("GeoRSS", "vector", "read, write and update, supporting virtual IO", "GeoRSS", ""),
    GPSTrackMaker("GPSTrackMaker", "vector", "read, write and update, supporting virtual IO", "GPSTrackMaker", ""),
    VFK("VFK", "vector", "read-only", "Czech Cadastral Exchange Data Format", "vfk"),
    PGDUMP("PGDUMP", "vector", "w+v", "PostgreSQL SQL dump", ""),
    OSM("OSM", "vector", " read-only and virtual IO", "OpenStreetMap XML and PBF", ""),
    GPSBabel("GPSBabel", "vector", "read, write and update", "GPSBabel", ""),
    SUA("SUA", "vector", " read-only and virtual IO", "Tim Newport-Peace's Special Use Airspace Format", ""),
    OpenAir("OpenAir", "vector", " read-only and virtual IO", "OpenAir", ""),
    OGR_PDS("OGR_PDS", "vector", " read-only and virtual IO", "Planetary Data Systems TABLE", ""),
    WFS("WFS", "vector", " read-only and virtual IO", "OGC WFS (Web Feature Service)", ""),
    HTF("HTF", "vector", " read-only and virtual IO", "Hydrographic Transfer Vector", ""),
    AeronavFAA("AeronavFAA", "vector", " read-only and virtual IO", "Aeronav FAA", ""),
    Geomedia("Geomedia", "vector", "read-only", "Geomedia .mdb", "mdb"),
    EDIGEO("EDIGEO", "vector", " read-only and virtual IO", "French EDIGEO exchange format", "thf"),
    GFT("GFT", "vector", "read, write and update", "Google Fusion Tables", ""),
    GME("GME", "vector", "read, write and update", "Google Maps Engine", ""),
    SVG("SVG", "vector", " read-only and virtual IO", "Scalable Vector Graphics", "svg"),
    CouchDB("CouchDB", "vector", "read, write and update", "CouchDB / GeoCouch", ""),
    Cloudant("Cloudant", "vector", "read, write and update", "Cloudant / CouchDB", ""),
    Idrisi("Idrisi", "vector", " read-only and virtual IO", "Idrisi Vector", "vct"),
    ARCGEN("ARCGEN", "vector", " read-only and virtual IO", "Arc/Info Generate", ""),
    SEGUKOOA("SEGUKOOA", "vector", " read-only and virtual IO", "SEG-P1 / UKOOA P1/90", ""),
    SEGY("SEGY", "vector", " read-only and virtual IO", "SEG-Y", ""),
    XLS("XLS", "vector", "read-only", "MS Excel format", "xls"),
    ODS("ODS", "vector", "read, write and update, supporting virtual IO",
            "Open Document/ LibreOffice / OpenOffice Spreadsheet", "ods"),
    XLSX("XLSX", "vector", "read, write and update, supporting virtual IO", "MS Office Open XML spreadsheet", "xlsx"),
    ElasticSearch("ElasticSearch", "vector", "w+", "Elastic Search", ""),
    Walk("Walk", "vector", "read-only", "Walk", ""),
    CartoDB("CartoDB", "vector", "read, write and update", "CartoDB", ""),
    SXF("SXF", "vector", "read-only", "Storage and eXchange Format", "sxf"),
    Selafin("Selafin", "vector", "read, write and update, supporting virtual IO", "Selafin", ""),
    JML("JML", "vector", "read, write and update, supporting virtual IO", "OpenJUMP JML", "jml"),
    PLSCENES("PLSCENES", "raster, vector", "read-only", "Planet Labs Scenes API", ""),
    CSW("CSW", "vector", "read-only", "OGC CSW (Catalog Service for the Web)", ""),
    TIGER("TIGER", "vector", "read, write and update, supporting virtual IO", "U.S. Census TIGER/Line", ""),
    AVCBin("AVCBin", "vector", "read-only", "Arc/Info Binary Coverage", ""),
    AVCE00("AVCE00", "vector", "read-only", "Arc/Info E00 (ASCII) Coverage", "e00"),
    GPKG("GPKG", "raster, vector", "read, write and update, supporting virtual IO and subdatasets", "GeoPackage",
            "gpkg"),
    HTTP("HTTP", "raster, vector", "read-only", "HTTP Fetching Wrapper", ""),
    ECW("ECW", "raster", "read, write", "ERDAS Compressed Wavelets", "ecw");

    private String name;
    private String type;
    private String readOrWrite;
    private String description;
    private String extension;

    GDALDriversEnum(String name, String type, String readOrWrite, String description, String extension) {
        this.name = name;
        this.type = type;
        this.readOrWrite = readOrWrite;
        this.description = description;
        this.extension = extension;
    }

    private static final Function<String, GDALDriversEnum> func =
            EnumUtils.lookupMap(GDALDriversEnum.class, GDALDriversEnum::getExtension);

    public static GDALDriversEnum lookupByExtension(String ext) {
        return func.apply(ext);
    }
}
