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
 * ִ�������г���
 * </pre>
 * TODO ����
 * �ο���http://wuhongyu.iteye.com/blog/461477
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
        Runtime runtime = Runtime.getRuntime();//�����뵱ǰ Java Ӧ�ó�����ص�����ʱ����
        Process process = runtime.exec(cmd);// ������һ��������ִ������
        BufferedInputStream in = new BufferedInputStream(process.getInputStream()); //���ִ������ı�׼���
        BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
        String lineStr;
        while ((lineStr = inBr.readLine()) != null) {
            //�������ִ�к��ڿ���̨�������Ϣ
            log.debug(lineStr);// ��ӡ�����Ϣ
        }
        //��������Ƿ�ִ��ʧ�ܡ�
        if (process.waitFor() != 0) {
            if (process.exitValue() == 1)//exitValue()==0 ��ʾ����������1������������
            {
                log.error("����ִ��ʧ��!");//���ڲ���
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
        ExecuteWatchdog watchdog = new ExecuteWatchdog(60000); // ��ʱʱ��Ϊ1����
        executor.setWatchdog(watchdog);
        int exitValue = executor.execute(cmd);// �˳�ֵ

        return exitValue;
    }
    // TODO �첽������ִ��
    //DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
}
