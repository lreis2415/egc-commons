package org.egc.commons.command;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.*;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 使用 apache.commons.exec 执行命令行程序
 * </pre>
 * TODO 测试
 * 参考 http://wuhongyu.iteye.com/blog/461477
 *
 * @author houzhiwei
 * @date 2017 /9/16 10:46
 */
@Slf4j
public class CommonsExec {

    private static final String UTF8 = "UTF-8";
    private static final String GBK = "GBK";
    private static final String ISO_8859_1 = "ISO-8859-1";
    /**
     * The constant OUT.
     */
    public static final String OUT = "out";
    /**
     * The constant ERROR.
     */
    public static final String ERROR = "error";
    /**
     * The constant EXIT_VALUE.
     */
    public static final String EXIT_VALUE = "exitValue";

    /**
     * <pre>
     * Execute command line use {@link Executor#execute(CommandLine)} API.
     * Default exit Value is 0 on success
     * <pre/>
     *
     * @param cmd use {@link CommandLine#addArgument(String)} or {@link CommandLine#parse(String)} to parse command string to CommandLine <p/>
     * @return 0 : success; 1: failed
     * @throws IOException the io exception
     * @throws InterruptedException the interrupted exception
     */
    public static int exec(CommandLine cmd) throws IOException, InterruptedException {
        return exec(cmd, 0);
    }

    /**
     * Exec int.
     *
     * @param commandLine the command line
     * @return 0 : success; 1: failed
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    public static int exec(String commandLine) throws IOException, InterruptedException {
        CommandLine cmd = CommandLine.parse(commandLine);
        return exec(cmd, 0);
    }

    /**
     * <pre>
     * Execute command line use {@link Executor#execute(CommandLine)} API.
     * use {@link CommandLine#addArgument(String)} to add command arguments
     * </pre>
     *
     * @param cmd       the CommandLine
     * @param exitValue the exit value to be considered as successful execution <b>if it is not 0<b/>
     * @return 0 : success; 1: failed
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    public static int exec(CommandLine cmd, int exitValue) throws IOException, InterruptedException {
        Executor executor = new DefaultExecutor();
        // 设置超时时间，毫秒
        ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
        executor.setWatchdog(watchdog);
        executor.setExitValue(exitValue);
        int exit = executor.execute(cmd);
        return exit;
    }

    /**
     * Execute command line and return output string
     *
     * @param cmd the cmd
     * @return the output string map with key： {@link CommonsExec#OUT} and {@link CommonsExec#ERROR} and {@link CommonsExec#EXIT_VALUE}
     * @throws IOException the io exception
     */
    public static Map<String, Object> execWithOutput(CommandLine cmd) throws IOException {
        return execWithOutput(cmd, null, null, 0, null);
    }

    /**
     * Execute command line and return output string
     *
     * @param cmd       命令行. 如果是字符串，则使用 {@link CommandLine#parse(String)} 转换
     * @param workspace 工作目录(数据输出目录)， blank 时使用当前目录（<code>new File(".")</code>）
     * @return the output string map with key： {@link CommonsExec#OUT} and {@link CommonsExec#ERROR} and {@link CommonsExec#EXIT_VALUE}
     * @throws IOException the io exception
     */
    public static Map<String, Object> execWithOutput(CommandLine cmd, String workspace) throws IOException {
        return execWithOutput(cmd, workspace, null, 0, null);
    }

    /**
     * Exec with output string.
     *
     * @param cmd          命令行. 如果是字符串，则使用 {@link CommandLine#parse(String)} 转换
     * @param envKeyValues the environmental variable list in format: <b>key=value</b>
     * @return the output string map with key： {@link CommonsExec#OUT} and {@link CommonsExec#ERROR} and {@link CommonsExec#EXIT_VALUE}
     * @throws IOException the io exception
     */
    public static Map<String, Object> execWithOutput(CommandLine cmd, List<String> envKeyValues) throws IOException {
        return execWithOutput(cmd, null, envKeyValues, 0, null);
    }

    /**
     * Exec with output string.
     *
     * @param cmd          命令行. 如果是字符串，则使用 {@link CommandLine#parse(String)} 转换
     * @param workspace    工作目录(数据输出目录)， blank 时使用当前目录（<code>new File(".")</code>）
     * @param exitValue    运行退出值，通常为 0
     * @param timeout      超时时间，默认 60000L ms
     * @param envKeyValues the environmental variable list in format: <b>key=value</b>
     * @return the output string map with key： {@link CommonsExec#OUT} and {@link CommonsExec#ERROR} and {@link CommonsExec#EXIT_VALUE}<br/>
     * 注意：有些程序在执行成功之后，部分信息会出现在 error 中，因此不能根据 error 是否有内容来判断是否执行失败
     * @throws IOException the io exception
     */
    public static Map<String, Object> execWithOutput(CommandLine cmd, String workspace, List<String> envKeyValues,
                                                     int exitValue, Long timeout) throws IOException
    {
        Executor executor = new DefaultExecutor();
        if (timeout == null) {
            timeout = 60000L;
        }
        ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
        executor.setWatchdog(watchdog);
        File dir = new File(FilenameUtils.normalize(workspace == null ? "" : workspace));
        if (StringUtils.isNotBlank(workspace) && dir.exists()) {
            executor.setWorkingDirectory(dir);
        } else {
            log.warn("Workspace not set or illegal [ " + workspace + " ]. Use current working directory.");
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
        executor.setStreamHandler(streamHandler);
        executor.setExitValue(exitValue);

        if (envKeyValues == null || envKeyValues.size() == 0) {
            exitValue = executor.execute(cmd);
        } else {
            Map env = EnvironmentUtils.getProcEnvironment();
            for (String kv : envKeyValues) {
                EnvironmentUtils.addVariableToEnvironment(env, kv);
            }
            exitValue = executor.execute(cmd, env);
        }
        Map out = new HashMap();
        out.put(OUT, outputStream.toString(UTF8));
        out.put(ERROR, errorStream.toString(UTF8));
        out.put(EXIT_VALUE, exitValue);
        return out;
    }
}
