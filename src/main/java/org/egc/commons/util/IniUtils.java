package org.egc.commons.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ini4j.Ini;

import java.io.FileReader;
import java.io.IOException;

/**
 * @author houzhiwei
 * @date 2019/1/2 10:39
 */
@Slf4j
public class IniUtils {
    /**
     * <pre/>
     * 读取 src/main/resources/config 下面的 ini 文件
     *
     * @param iniFileName 文件名(不用后缀)/ filename without ".ini"
     * @return Ini
     */
    public static Ini getIniFromConfig(String iniFileName) {
        String path = IniUtils.class.getResource("/config/" + iniFileName + ".ini").getPath();
        return getIni(path);
    }

    /**
     * <pre/>
     * read ini file from "src/main/resources/${folder}"
     * if folder is Null or empty, read from "src/main/resources" directly
     *
     * @param iniFileName 文件名(不用后缀)/ filename without ".ini"
     * @return Ini
     */
    public static Ini getIniFromResource(String iniFileName, String folder) {
        String path = null;
        if (StringUtils.isBlank(folder)) {
            path = IniUtils.class.getResource("/" + iniFileName + ".ini").getPath();
        } else {
            path = IniUtils.class.getResource("/" + folder + "/" + iniFileName + ".ini").getPath();
        }
        return getIni(path);
    }

    public static Ini getIni(String iniFilePath) {
        Ini ini = new Ini();
        try {
            ini.load(new FileReader(iniFilePath));
        } catch (IOException e) {
            log.error("Could not read ini file from " + iniFilePath);
            return null;
        }
        return ini;
    }

}
