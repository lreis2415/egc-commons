package org.egc.commons.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMultimap;
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
    public ExecResult run(@NotBlank String exec, Map<String, String> fileParams, LinkedHashMultimap<String, Object> params) {
        CommandLine cmd = checkInitCmd(exec);
        RunUtils.addParams(cmd, params);
        Map files = new HashMap(fileParams.size());
        RunUtils.addFileParams(cmd, files, fileParams, null);
        cmd.setSubstitutionMap(files);
        ExecResult result = null;
        try {
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
                          LinkedHashMultimap<String, Object> inParams, String outputDir)
    {
        CommandLine cmd = checkInitCmd(exec);
        RunUtils.addParams(cmd, inParams);
        Map files = new HashMap(inputFiles.size() + outputFiles.size());
        RunUtils.addFileParams(cmd, files, inputFiles, null);
        RunUtils.addFileParams(cmd, files, outputFiles, outputDir);
        cmd.setSubstitutionMap(files);
        ExecResult result = null;
        try {
            result = CommonsExec.execWithOutput(cmd, outputDir);
            log.info("Execution Info/Error: {}",result.getError());
            result.setSuccess(true);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
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
