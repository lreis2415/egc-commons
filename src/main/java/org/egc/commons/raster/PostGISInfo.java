package org.egc.commons.raster;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/9/26 18:03
 */
@Data
@Slf4j
public class PostGISInfo {

    public PostGISInfo() {
    }

    public PostGISInfo(String username, String database, String password) {
        this.username = username;
        this.database = database;
        this.password = password;
    }

    private String username;
    private String database;
    private String password;
    private Integer port = 5432;
    private String host = "localhost";
    private String schema = "public";
    private String rasterTable;
    private String shapeTable;
    /**
     * postgis executable files directory
     */
    private String binDirectory = System.getenv("PSQL");
}
