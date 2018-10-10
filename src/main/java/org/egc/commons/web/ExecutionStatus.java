package org.egc.commons.web;

import lombok.Getter;

/**
 * Description:
 * <pre>
 * Enum ExecutionStatus
 * 执行状态枚举
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/10 22:21
 */
@Getter
public enum ExecutionStatus {

    SUCCEEDED(0, "Execution succeeded"),
    FAILED(1, "Oops, execution failed"),
    INACTIVE(2, "Execution has not been activated"),
    IN_PROGRESS(3, "Execution is in progress");

    private int code;
    private String description;

    private ExecutionStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
