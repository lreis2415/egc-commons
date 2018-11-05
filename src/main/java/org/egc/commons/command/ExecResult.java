package org.egc.commons.command;

import lombok.Data;

import java.util.List;

/**
 * 命令行执行结果
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
     *  某些程序的 error 中输出的内容类似 This run may take on the order of 1 minutes to complete，并非错误信息
     */
    private String error;
    /**
     * 退出值
     */
    private int exitValue;
    private List<String> resultFiles;
    private Boolean success;
}
