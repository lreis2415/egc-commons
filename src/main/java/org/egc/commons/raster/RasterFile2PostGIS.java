package org.egc.commons.raster;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by lp on 2017/4/26.
 */
public class RasterFile2PostGIS {
    //TODO:下面四个字符串迁移到配置文件
    private String postGISPath = "C:\\Program Files\\PostgreSQL\\9.5\\bin\\";
    private String dataPath = "E:/upload/";
    private String tableName = "public.t_rasters";
    private String dateBaseInfo = "dbname=db_cyberSolim user=postgres password=123";
    public void file2PostGIS(Integer srid, String relativePath){
        String filePath = dataPath + relativePath;
    //TODO:添加日志
        try{
            Process process = Runtime.getRuntime().exec("cmd" + postGISPath + "raster2pgsql -s"
                    + srid + " -I -a -M " + filePath + " -a " + tableName + " | psql " + dateBaseInfo);
            String str;
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(process.getInputStream()));
            //TODO:下面while语句改为输入到日志中，system打印耗费时间
            while ( (str=bufferedReader.readLine()) !=null){System.out.println(str);}
            process.waitFor();
        }catch (IOException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        }
}
