package org.egc.commons.raster;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.egc.commons.command.RunCommand;

/**
 * Created by lp on 2017/4/26.
 */
public class RasterFile2PostGIS {

	public boolean file2PostGIS(Integer srid, String filePath,String tableName, String dateBaseInfo, String postGISPath, String passWord) {
		boolean flag = true;
		try {
			String cmdString =  " -s " + srid + " -I -M " +
					filePath + " -a " + tableName ;
			String temp = "cmd /C set PGPASSWORD=" + passWord + "&" + postGISPath+
					cmdString +" | " + " psql "+ dateBaseInfo;
			Process process = Runtime.getRuntime().exec("cmd /C set PGPASSWORD=" + passWord + "&" + postGISPath+
					cmdString +" | " + " psql "+ dateBaseInfo);
			String str;
			InputStreamReader isr = new InputStreamReader(process.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(isr);
			while ((str = bufferedReader.readLine()) != null) {
				System.out.println(str);
			}
			process.waitFor();
		} catch (IOException | InterruptedException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

}
