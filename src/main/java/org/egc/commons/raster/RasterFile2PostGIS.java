package org.egc.commons.raster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by lp on 2017/4/26.
 * update houzhiwei
 * TODO 使用commons-exec改写，测试Linux下运行
 */
public class RasterFile2PostGIS {
	Logger logger = LoggerFactory.getLogger(this.getClass());

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
			logger.error(e.getMessage());
		}
		return flag;
	}

}
