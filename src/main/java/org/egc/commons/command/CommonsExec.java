package org.egc.commons.command;

import org.apache.commons.exec.*;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <pre>
 * 使用 apache.commons.exec 执行命令行程序
 * </pre>
 * TODO 测试
 * 参考 http://wuhongyu.iteye.com/blog/461477
 *
 * @author houzhiwei
 * @date 2017/9/16 10:46
 */
public class CommonsExec {

    private static final Logger log = LoggerFactory.getLogger(CommonsExec.class);

    /**
     * <pre>
     * Execute command line use {@link Executor#execute(CommandLine)} API.
     * Default exit Value is 0 on success
     * <pre/>
     *
     * @param cmd  the CommandLine;
     *             <p>recommend use {@link CommandLine#addArgument(String)} to add command arguments <br/>
     *             or use {@link StringBuilder } to build commands and <br/>
     *             use {@link CommandLine#parse(String)}
     *             to parse command string to CommandLine
     * <p/>
     * @return 0 : success; 1: failed
     * @throws IOException the io exception
     */
    public static int exec(CommandLine cmd) throws IOException, InterruptedException {
        return exec(cmd, 0);
    }

    /**
     * <pre>
     * Execute command line use {@link Executor#execute(CommandLine)} API.
     * use {@link CommandLine#addArgument(String)} to add command arguments
     * </pre>
     *
     * @param exitValue the exit value to be considered as successful execution <b>if it is not 0<b/>
     * @param cmd       the CommandLine
     * @return 0 : success; 1: failed
     * @throws IOException the io exception
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
     * @param cmd
     * @return command line output
     * @throws IOException
     */
    public static String execWithOutput(CommandLine cmd) throws IOException {

        Executor executor = new DefaultExecutor();
        ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
        executor.setWatchdog(watchdog);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        executor.setStreamHandler(streamHandler);
        executor.execute(cmd);
        return outputStream.toString("UTF-8");
    }

}
