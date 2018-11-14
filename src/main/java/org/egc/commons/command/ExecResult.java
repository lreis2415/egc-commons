package org.egc.commons.command;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 命令行执行结果
 *
 * @author houzhiwei
 * @date 2018/11/3 14:27
 */
@Data
public class ExecResult {

    public ExecResult(String out) {
        this.out = out;
    }

    private String out;
    /**
     * 某些程序的 error 中输出的内容类似 This run may take on the order of 1 minutes to complete，并非错误信息
     */
    private String error;
    /**
     * 退出值
     */
    private int exitValue;
    private List<String> resultFiles;

    private Map<String, Object> resultFilesMap;
    private Boolean success;

    /**
     * list of result/output file names, e.g. "path_of_xcDEM_Pit_Removed_Elevation_Grid.tif"
     * <p><i>for (k,v) in outputFiles, list of v</i>
     */
    public List<String> getResultFiles(){
        return this.resultFiles;
    }

    /**
     * key is FilenameUtils.getBaseName(result/output file name without extension), e.g. "xcDEM_Pit_Removed_Elevation_Grid"
     * <p> value is result/output file name, e.g.  "path_of_xcDEM_Pit_Removed_Elevation_Grid.tif"
     * <p> <i>for (k,v) in outputFiles,
     * key and value of resultFilesMap is < FilenameUtils.getBaseName(v), v ></i>
     */
    public Map<String, Object> getResultFilesMap() {
        return this.resultFilesMap;
    }
}
