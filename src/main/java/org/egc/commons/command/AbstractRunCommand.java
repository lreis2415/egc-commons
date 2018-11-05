package org.egc.commons.command;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <pre>
 * RunCommand 接口的默认实现, 必要时复写本类中方法
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/25 22:45
 */
@Slf4j
public abstract class AbstractRunCommand implements RunCommand {

    /**
     * 未实现。需要时再重写本方法
     *
     * @return null;
     */
    @Override
    public ExecResult run(@NotBlank String exec, Map<String, Object> params) {
        return null;
    }

    /**
     * 检查是否对CommandLine进行了有效的初始化
     *
     * @param exec
     * @return
     */
    private CommandLine checkInitCmd(String exec) {
        CommandLine cmd = initCmd();
        if (StringUtils.isBlank(cmd.getExecutable())) {
            cmd = new CommandLine(exec);
        } else if (!cmd.getExecutable().equalsIgnoreCase(exec.toLowerCase())) {
            cmd.addArgument(exec);
        }
        return cmd;
    }

    @Override
    public ExecResult run(@NotBlank String exec, Map<String, String> fileParams, Map<String, Object> params) throws
            IOException
    {
        CommandLine cmd = checkInitCmd(exec);
        RunUtils.addParams(cmd, params);
        Map files = new HashMap(fileParams.size());
        RunUtils.addFileParams(cmd, files, fileParams, null);
        cmd.setSubstitutionMap(files);
        return CommonsExec.execWithOutput(cmd);
    }

    @Override
    public ExecResult run(@NotBlank String exec, Map<String, String> inputFiles, Map<String, String> outputFiles,
                          Map<String, Object> params, String inputDir, String outputDir) throws IOException
    {
        CommandLine cmd = checkInitCmd(exec);
        RunUtils.addParams(cmd, params);
        Map files = new HashMap(inputFiles.size() + outputFiles.size());
        RunUtils.addFileParams(cmd, files, inputFiles, inputDir);
        RunUtils.addFileParams(cmd, files, outputFiles, null);
        cmd.setSubstitutionMap(files);
        ExecResult result = CommonsExec.execWithOutput(cmd, outputDir);
        result.setResultFiles(mapFiles2List(outputFiles));
        return result;
    }

    /**
     * 执行命令行，记录执行日志
     *
     * @param exec       可执行程序，如 PitRemove
     * @param fileParams 文件型参数，如 &lt; "-z", Input_Elevation_Grid &gt;， Input_Elevation_Grid 为文件
     * @param params     非文件类型参数，如 &lt; "-4way", Fill_Considering_only_4_way_neighbors &gt; Fill_Considering_only_4_way_neighbors 为布尔值
     * @return
     */
    protected ExecResult runCommand(@NotBlank String exec, Map<String, String> fileParams, Map<String, Object> params) {
        ExecResult result = null;
        try {
            result = run(exec, fileParams, params);
            log.info(result.getOut());
            log.info(result.getError());
            result.setSuccess(true);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e.getCause());
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 执行命令行，记录执行日志
     *
     * @param exec        可执行程序，如 PitRemove
     * @param inputFiles  输入文件型参数，如 &lt; "-z", Input_Elevation_Grid &gt;， Input_Elevation_Grid 为文件
     * @param outputFiles 输出文件参数
     * @param params      非文件类型参数，如 &lt; "-4way", Fill_Considering_only_4_way_neighbors &gt; Fill_Considering_only_4_way_neighbors 为布尔值
     * @param inputDir    输入文件目录。当所有输入文件都在此目录下时，参数中不只需文件名
     * @param outputDir   工作目录，所有输出文件默认存放目录
     * @return
     */
    protected ExecResult runCommand(@NotBlank String exec, Map<String, String> inputFiles,
                                    Map<String, String> outputFiles, Map<String, Object> params, String inputDir,
                                    String outputDir)
    {
        ExecResult result = null;
        try {
            result = run(exec, inputFiles, outputFiles, params, inputDir, outputDir);
            log.info(result.getOut());
            log.info(result.getError());
            result.setSuccess(true);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e.getCause());
            result.setSuccess(false);
        }
        return result;
    }


    private List<String> mapFiles2List(Map<String, String> mapFiles) {
        Preconditions.checkNotNull(mapFiles, "files map can not be null!");
        List<String> files = new ArrayList<>(mapFiles.size());
        mapFiles.forEach((k, v) -> {
            files.add(v);
        });
        return files;
    }
}
