package org.egc.commons.test;

import com.google.common.collect.Lists;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.egc.commons.command.CommonsExec;
import org.egc.commons.command.ExecResult;
import org.egc.commons.gis.File2PostGIS;
import org.egc.commons.gis.GeoTiffUtils;
import org.egc.commons.gis.PostGISInfo;
import org.egc.commons.gis.RasterMetadata;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/9/28 14:34
 */
public class CommonsExecTest {
    @Test
    public void env() throws IOException, InterruptedException {
        Map map = EnvironmentUtils.getProcEnvironment();
        EnvironmentUtils.addVariableToEnvironment(map, "PGPASSWORD=lll");
        System.out.println(map.get("PGPASSWORD"));
        System.out.println(System.getenv("PGPASSWORD"));//null
        System.out.println(System.getenv("path"));//null
        String cmd2 = "cmd /C \"D:/Program Files/PostgreSQL/9.5/bin/raster2pgsql\" -s 9001 -I -M H:\\dem_TX_out.tif -a public.t_rasters | psql -U egcadmin -d db_cybersolim";
        String cmd3 = "\"D:/Program Files/PostgreSQL/9.5/bin/raster2pgsql\" -s 9001 -I -M H:\\dem_TX_out.tif -a public.t_rasters | psql -U egcadmin -d db_cybersolim";

        CommonsExec.execWithOutput(CommandLine.parse(cmd3), Lists.newArrayList("PGPASSWORD=lreis2415"));
    }

    @Test
    public void withOut() throws IOException {

        CommandLine commandLine = new CommandLine("cmd");
//        CommandLine commandLine = new CommandLine( "raster2pgsql");
        commandLine.addArgument("/C");
        commandLine.addArgument("raster2pgsql");
        commandLine.addArgument("-s 9001", false);
        commandLine.addArgument("-I -M", false);
        commandLine.addArgument("${file}");
        commandLine.addArgument("-a public.t_rasters", false);
        commandLine.addArgument("|");
        commandLine.addArgument("psql");
        commandLine.addArgument("-U egcadmin", false);
        commandLine.addArgument("-d db_cybersolim", false);
        Map map = new HashMap();
        map.put("file", new File("H:\\dem_TX_out.tif"));
        commandLine.setSubstitutionMap(map);

        String cmd = String.join(" ", commandLine.toStrings());
        System.out.println(cmd);

        ExecResult map3 = CommonsExec.execWithOutput(CommandLine.parse(cmd), Lists.newArrayList("PGPASSWORD=lreis2415"));
        System.out.println("out: " + map3.getOut());
        System.out.println("error: " + map3.getError());
    }


    String file = "G:\\DDL Driver\\Projects\\CyberSoLIM\\Training\\DEM_ah.tif";//32650
    String file2 = "H:\\dem_TX_out.tif";//9001

    @Test
    public void loadToPg() {
        RasterMetadata metadata = GeoTiffUtils.getMetadata(file);
        System.out.println(metadata.getSrid());
        PostGISInfo info = new PostGISInfo("egcadmin", "db_cybersolim", "lreis2415");

       /*CommandLine commandLine = new CommandLine(FileUtil.normalizeDirectory(info.getBinDirectory()) +  "raster2pgsql");
        commandLine.addArgument("-G");
        try {
            String out = CommonsExec.execWithOutput(commandLine);
            System.out.println(out);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        info.setRasterTable("t_rasters");
        ExecResult out = File2PostGIS.raster2PostGIS(32650, file, info);
        System.out.println(out.getOut());
        System.out.println(out.getError());
        System.out.println(out.getExitValue());
    }
}
