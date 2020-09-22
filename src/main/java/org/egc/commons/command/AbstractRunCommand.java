package org.egc.commons.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMultimap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
     * 检查是否对CommandLine进行了有效的初始化
     *
     * @param exec executable
     * @return cmd
     */
    private CommandLine checkInitCmd(String exec) {
        CommandLine cmd = initCmd();
        if (cmd == null || StringUtils.isBlank(cmd.getExecutable())) {
            cmd = new CommandLine(exec);
        } else if (!cmd.getExecutable().equalsIgnoreCase(exec.toLowerCase())) {
            cmd.addArgument(exec);
        }
        return cmd;
    }

    @Override
    public ExecResult run(@NotBlank String exec, Map<String, String> fileParams, LinkedHashMultimap<String, Object> params) {
        return run(exec, fileParams, params, null);
    }

    @Override
    public ExecResult run(@NotBlank String exec, Map<String, String> fileParams, LinkedHashMultimap<String, Object> params, List<String> envKeyValues) {
        CommandLine cmd = checkInitCmd(exec);
        RunUtils.addParams(cmd, params);
        Map<String, String> files = new LinkedHashMap<>(fileParams.size());
        RunUtils.addFileParams(cmd, files, fileParams, null);
        cmd.setSubstitutionMap(files);
        ExecResult result = new ExecResult();
        try {
            cmd = handleEqualSign(cmd);
            if (envKeyValues != null && envKeyValues.size() > 0) {
                result = CommonsExec.execWithOutput(cmd, null, envKeyValues, null, null);
            } else {
                result = CommonsExec.execWithOutput(cmd);
            }
            result = CommonsExec.execWithOutput(cmd);
            log.info(result.getOut());
            log.info(result.getError());
            result.setSuccess(true);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
            result.setSuccess(false);
        }
        return result;
    }

    @Override
    public ExecResult run(@NotBlank String exec, Map<String, String> inputFiles, Map<String, String> outputFiles,
                          LinkedHashMultimap<String, Object> inParams, String outputDir) {
        /*CommandLine cmd = checkInitCmd(exec);
        RunUtils.addParams(cmd, inParams);
        Map files = new LinkedHashMap(inputFiles.size() + outputFiles.size());
        RunUtils.addFileParams(cmd, files, inputFiles, null);
        RunUtils.addFileParams(cmd, files, outputFiles, outputDir);
        cmd.setSubstitutionMap(files);
        ExecResult result = null;
        try {
            cmd = handleEqualSign(cmd);
            result = CommonsExec.execWithOutput(cmd, outputDir);
            log.info("Execution Info/Error: {}", result.getError());
            result.setSuccess(true);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
            result.setSuccess(false);
        }*/
        return run(exec, inputFiles, outputFiles, inParams, outputDir, null);
    }

    @Override
    public ExecResult run(@NotBlank String exec, Map<String, String> inputFiles, Map<String, String> outputFiles,
                          LinkedHashMultimap<String, Object> inParams, String outputDir, List<String> envKeyValues) {
        CommandLine cmd = checkInitCmd(exec);
        RunUtils.addParams(cmd, inParams);
        Map<String, String> files = new LinkedHashMap<>(inputFiles.size() + outputFiles.size());
        RunUtils.addFileParams(cmd, files, inputFiles, null);
        RunUtils.addFileParams(cmd, files, outputFiles, outputDir);
        cmd.setSubstitutionMap(files);
        ExecResult result = new ExecResult();
        try {
            cmd = handleEqualSign(cmd);
            if (envKeyValues != null && envKeyValues.size() > 0) {
                result = CommonsExec.execWithOutput(cmd, outputDir, envKeyValues, null, null);
            } else {
                result = CommonsExec.execWithOutput(cmd, outputDir);
            }
            log.info("Execution Info/Error: {}", result.getError());
            result.setSuccess(true);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 处理命令行中可能出现的 = 号.
     * <p>如WhiteBox的命令行
     * <p>{@code
     * whitebox_tools -r=ExtractValleys -v --wd="/path/to/data/" --dem=pointer.tif
     * -o=out.tif
     * --variant='JandR'
     * --line_thin}
     * <p> 不处理的话，= 后存在空格，执行报错
     *
     * @param cmd CommandLine 对象
     * @return 处理后或无需处理的 CommandLine 对象
     */
    private CommandLine handleEqualSign(CommandLine cmd) {
        String cmdStr = String.join(" ", cmd.toStrings());
        if (cmdStr.indexOf("= ") > 0) {
            cmdStr = cmdStr.replaceAll("= ", "=");
            cmd = CommandLine.parse(cmdStr);
        }
        return cmd;
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
