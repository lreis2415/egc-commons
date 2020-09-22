package org.egc.commons.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * 分页数据表，用于传递分页后的数据给前端显示
 * 可用于 Bootstrap-Table 插件
 * </pre>
 *
 * @author houzhiwei
 * @date 2017/2/28 0:14
 * @date 2018/6/20
 */
public class PaginatedDataTable implements Serializable {
    private static final long serialVersionUID = 418288642877122071L;
    private final Logger logger = LoggerFactory.getLogger(PaginatedDataTable.class);

    /**
     * 升序
     */
    public static final String ORDER_ASC = "asc";
    /**
     * 降序
     */
    public static final String ORDER_DESC = "desc";

    private long total;
    private List<?> rows;
    private String order;

    public PaginatedDataTable(List<?> rows, long total, String order) {
        this.rows = rows;
        this.total = total;
        this.order = order;
    }

    /**
     * 推荐使用此构造函数
     *
     * @param rows  数据行，需要传递的数据记录
     * @param total 数据库记录总数
     */
    public PaginatedDataTable(List<?> rows, long total) {
        this.rows = rows;
        this.total = total;
        this.order = ORDER_ASC;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    //region useless
    /*
    private int pageSize; // 每页的记录数
    private int pageNumber; // current,  当前页码
    private int offset; // 偏移量，即数据库查询起始行数 offset = (current-1)*pageSize
    private int limit; //  pageSize
    */
    //endregion
}
