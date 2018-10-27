package org.egc.commons.command;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.HashMap;
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
    public Map run(@NotBlank String exec, Map<String, Object> params) {
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
    public Map run(@NotBlank String exec, Map<String, String> fileParams, Map<String, Object> params) throws IOException {
        CommandLine cmd = checkInitCmd(exec);
        RunUtils.addParams(cmd, params);
        Map files = new HashMap(fileParams.size());
        RunUtils.addFileParams(cmd, files, fileParams, null);
        cmd.setSubstitutionMap(files);
        return CommonsExec.execWithOutput(cmd);
    }

    @Override
    public Map run(@NotBlank String exec, Map<String, String> inputFiles, Map<String, String> outputFiles, Map<String, Object> params, String inputDir, String outputDir) throws IOException {
        CommandLine cmd = checkInitCmd(exec);
        RunUtils.addParams(cmd, params);
        Map files = new HashMap(inputFiles.size() + outputFiles.size());
        RunUtils.addFileParams(cmd, files, inputFiles, inputDir);
        RunUtils.addFileParams(cmd, files, outputFiles, null);
        cmd.setSubstitutionMap(files);
        return CommonsExec.execWithOutput(cmd, outputDir);
    }

    /**
     * 执行命令行，记录执行日志
     * @param exec
     * @param fileParams
     * @param params
     * @return true if successeded
     */
    protected boolean runCommand(@NotBlank String exec, Map<String, String> fileParams, Map<String, Object> params) {
        try {
            Map result = run(exec, fileParams, params);
            log.info(String.valueOf(result.get(CommonsExec.OUT)));
            log.info(String.valueOf(result.get(CommonsExec.ERROR)));
            return true;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e.getCause());
            return false;
        }
    }

    protected boolean runCommand(@NotBlank String exec, Map<String, String> inputFiles, Map<String, String> outputFiles, Map<String, Object> params, String inputDir, String outputDir) {
        try {
            Map result = run(exec, inputFiles, outputFiles, params, inputDir, outputDir);
            log.info(String.valueOf(result.get(CommonsExec.OUT)));
            log.info(String.valueOf(result.get(CommonsExec.ERROR)));
            return true;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e.getCause());
            return false;
        }
    }
}
