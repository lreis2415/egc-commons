package org.egc.commons.command;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 使用 Java 内置方法执行命令行
 *
 * @author houzhiwei
 * @date 2018/9/3 20:17
 */
@Slf4j
public class InternalRuntime {
    /**
     * Execute command line use Java {@link Runtime#exec(String)} API.
     *
     * @param sb the Command StringBuilder
     * @return 0: success <br/>
     * 1: failed
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    public int run(StringBuilder sb) throws IOException, InterruptedException {
        String cmd = sb.toString();
        //返回与当前 Java 应用程序相关的运行时对象
        Runtime runtime = Runtime.getRuntime();
        // 启动另一个进程来执行命令;
        Process process = runtime.exec(cmd);
        BufferedInputStream in = new BufferedInputStream(process.getInputStream());
        BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
        String lineStr;
        while ((lineStr = inBr.readLine()) != null) {
            //获得命令执行后在控制台的输出信息
            log.debug(lineStr);
        }
        //检查命令是否执行失败
        if (process.waitFor() != 0) {
            //p.exitValue()==0表示正常结束，1：非正常结束
            if (process.exitValue() == 1) {
                log.error("Run command [ {} ] failed.", cmd);
            }
            return 1;
        }
        inBr.close();
        in.close();
        return 0;
    }
}
