package org.egc.commons.command;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <pre>
 * 执行命令行程序
 * </pre>
 * TODO 测试
 * 参考：http://wuhongyu.iteye.com/blog/461477
 *
 * @author houzhiwei
 * @date 2017 /9/16 10:46
 */
public abstract class RunCommand {

    private static final Logger log = LoggerFactory.getLogger(RunCommand.class);

    /**
     * Execute command line use Java {@link Runtime#exec(String)} API.
     *
     * @param sb the Command StringBuilder
     * @return 0 : success; 1: failed
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    protected int run(StringBuilder sb) throws IOException, InterruptedException {
        String cmd = sb.toString();
        Runtime runtime = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象
        Process process = runtime.exec(cmd);// 启动另一个进程来执行命令
        BufferedInputStream in = new BufferedInputStream(process.getInputStream()); //获得执行命令的标准输出
        BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
        String lineStr;
        while ((lineStr = inBr.readLine()) != null) {
            //获得命令执行后在控制台的输出信息
            log.debug(lineStr);// 打印输出信息
        }
        //检查命令是否执行失败。
        if (process.waitFor() != 0) {
            if (process.exitValue() == 1)//exitValue()==0 表示正常结束，1：非正常结束
            {
                log.error("命令执行失败!");//便于查找
                log.error("Run command [ {} ] failed.", cmd);
            }
            return 1;
        }
        inBr.close();
        in.close();
        return 0;
    }

    /**
     * Execute command line use {@link org.apache.commons.exec.Executor#execute(CommandLine)} API.
     *
     * @param sb the Command StringBuilder
     * @return 0 : success; 1: failed
     * @throws IOException the io exception
     */
    protected int exec(StringBuilder sb) throws IOException {
        CommandLine command = CommandLine.parse(sb.toString());
        return exec(command);
    }

    /**
     * <pre>
     * Execute command line use {@link org.apache.commons.exec.Executor#execute(CommandLine)} API.
     * use {@link CommandLine#addArgument(String)} to add command arguments
     * </pre>
     *
     * @param cmd the CommandLine
     * @return 0 : success; 1: failed
     * @throws IOException the io exception
     */
    protected int exec(CommandLine cmd) throws IOException {
        Executor executor = new DefaultExecutor();
        ExecuteWatchdog watchdog = new ExecuteWatchdog(60000); // 超时时间为1分钟
        executor.setWatchdog(watchdog);
        int exitValue = executor.execute(cmd);// 退出值

        return exitValue;
    }
    // TODO 异步非阻塞执行
    //DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
}
