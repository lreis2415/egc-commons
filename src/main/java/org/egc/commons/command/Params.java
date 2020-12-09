package org.egc.commons.command;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.egc.commons.util.IniUtils;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * <pre>
 *  Command parameters identifier
 *  命令行方法的参数对象应当实现此接口，以便调用方法能够从 {@link ExecResult#getParams()}  中获取输出
 *  parameter object of a commandline method should implements this interface,
 *  so that the calling methods could get outputs from {@link ExecResult#getParams()}
 *  需要对输出变量赋值: 用户输入或者自动命名
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /11/19 11:36
 */
public interface Params extends Serializable {
    /**
     * 为输出数据设置默认名称文件名
     * 必要时可以覆盖重新实现此方法
     *
     * @param inputFilename           输入文件名
     * @param outputParam     输出参数名
     * @param fileType        the file type. 有些抓取的知识没有 formatExtension，而且都是字符串
     * @param formatExtension the format extension.
     * @return string
     */
    default String namingOutput(String inputFilename, String outputParam, String fileType, String formatExtension) {
        String extension = "";
        if (StringUtils.isNotBlank(formatExtension)) {
            extension = formatExtension;
        } else {
            Ini ini = IniUtils.getIniFromResource("formats", null);
            Profile.Section formats = ini.get("formats");
            // 抓取的知识没有 formatExtension，而且都是字符串
            if (StringUtils.isNotBlank(fileType)) {
                extension = formats.get(fileType);
                if (StringUtils.isBlank(extension)) {
                    extension = FilenameUtils.getExtension(inputFilename);
                }
            }
        }

        String baseName = FilenameUtils.getBaseName(inputFilename);

        StringBuilder stringBuilder = new StringBuilder();
        if (baseName.contains("_") && !baseName.startsWith("_")) {
            baseName = baseName.substring(0, baseName.indexOf("_"));
        }
        stringBuilder.append(baseName);
        stringBuilder.append("_");
        stringBuilder.append(outputParam.replaceFirst("Output_", ""));
        stringBuilder.append(".");
        stringBuilder.append(extension);
        outputParam = stringBuilder.toString();

        //避免重复
        if (new File(outputParam).exists()) {
            outputParam = FilenameUtils.getBaseName(outputParam) + "_" +
                    DateUtils.getFragmentInMilliseconds(new Date(), Calendar.MINUTE) + "." + extension;
        }
        return outputParam;
    }


    /**
     * Naming output with uuid string. <br/>
     * [uuid_outputParam.extension]
     * @param inputFilename           the inputFilename
     * @param outputParam     the output param
     * @param fileType        the file type
     * @param formatExtension the format extension
     * @return the string
     */
    default String namingOutputWithUUID(String inputFilename, String outputParam, String fileType, String formatExtension) {
        String extension = "";
        if (StringUtils.isNotBlank(formatExtension)) {
            extension = formatExtension;
        } else {
            Ini ini = IniUtils.getIniFromResource("formats", null);
            Profile.Section formats = ini.get("formats");
            // 抓取的知识没有 formatExtension，而且都是字符串
            if (StringUtils.isNotBlank(fileType)) {
                extension = formats.get(fileType);
                if (StringUtils.isBlank(extension)) {
                    extension = FilenameUtils.getExtension(inputFilename);
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(UUID.randomUUID().toString());
        stringBuilder.append("_");
        stringBuilder.append(outputParam.replaceFirst("[Oo]utput_", ""));
        stringBuilder.append(".");
        stringBuilder.append(extension);
        return stringBuilder.toString();
    }
}
