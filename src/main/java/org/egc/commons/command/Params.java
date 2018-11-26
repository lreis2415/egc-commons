package org.egc.commons.command;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

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
 * @date 2018/11/19 11:36
 */
public interface Params extends Serializable {
    /**
     * 为输出数据设置默认名称文件名
     * 必要时可以覆盖重新实现此方法
     * @param input       输入文件名
     * @param outputParam 输出参数名
     * @return
     */
    default String namingOutput(String input, String outputParam, String fileType, String formatExtension) {
        String extension = "";
        if (StringUtils.isNotBlank(formatExtension)) {
            extension = formatExtension;
        } else {
            // 抓取的知识没有 formatExtension，而且都是字符串
            if (StringUtils.isNotBlank(fileType)) {
                switch (fileType) {
                    case "Raster Layer":
                        extension = "tif";
                        break;
                    case "Raster Dataset":
                        extension = "tif";
                        break;
                    case "Grid":
                        extension = "tif";
                        break;
                    case "Shapes":
                        extension = "shp";
                        break;
                    case "Shapefile":
                        extension = "shp";
                        break;
                    case "Feature Layer":
                        extension = "shp";
                        break;
                    case "File":
                        extension = "txt";
                        break;
                    case "Text File":
                        extension = "txt";
                        break;
                    case "DBFile":
                        extension = "dbf";
                        break;
                    case "Table":
                        extension = "dbf";
                        break;
                    default:
                        extension = FilenameUtils.getExtension(input);
                        break;
                }
            }
        }

        String baseName = FilenameUtils.getBaseName(input);

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

        if (new File(outputParam).exists()) {
            outputParam = FilenameUtils.getBaseName(outputParam) + "_" +
                    DateUtils.getFragmentInMilliseconds(new Date(), Calendar.MINUTE) + "." + extension;
        }
        return outputParam;
    }
}
