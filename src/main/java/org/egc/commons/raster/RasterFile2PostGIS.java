package org.egc.commons.raster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lp on 2017/4/26.
 */
public class RasterFile2PostGIS {
	//TODO:下面四个字符串迁移到配置文件
	private String postGISPath = "D:\\webExe\\raster2pgsql.exe";
	private String dataPath = "E:/upload/";
	private String tableName = "public.t_rasters";
	private String dateBaseInfo = "\"" + "dbname=db_cyberSolim user=postgres password=123" + "\"";

	public void file2PostGIS(Integer srid, String relativePath) {
		String filePath = dataPath + relativePath;
		//TODO:添加日志
		try {
			String cmdString = postGISPath + " -s " + srid + " -I -a -M " +
					filePath + " -a " + tableName + " | psql " + dateBaseInfo;
//			Process process = Runtime.getRuntime().exec("cmd  " + postGISPath + "raster2pgsql -s"
//																+ srid + " -I -a -M " + filePath + " -a " + tableName + " | psql " + dateBaseInfo);
			//Process process = Runtime.getRuntime().exec(cmdString);
			//String str;
			List<String> args = new ArrayList<String>();
			args.add("cmd ");

			args.add(postGISPath);
			args.add(" -s " );
			args.add(srid.toString());
			args.add(" -I -a -M ");
			args.add(filePath);
			args.add(" -a ");
			args.add(tableName);
			args.add(" | psql ");
			args.add(dateBaseInfo);
			ProcessBuilder pb = new ProcessBuilder(args);
			//pb.getInputStream();
			Process p = pb.start();
			p.waitFor();
			System.out.println(cmdString);
			//process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
