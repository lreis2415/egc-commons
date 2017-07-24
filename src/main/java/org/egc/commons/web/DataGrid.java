package org.egc.commons.web;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * 返回数据给前台的table
 * 主要针对 Bootstrap-Table
 * @author houzhiwei
 * @date 2017/2/28 0:14
 */
public class DataGrid implements Serializable {
    private static final long serialVersionUID = 418288642877122071L;
    /**
     * 升序
     */
    public static final String ORDER_ASC = "asc";
    /**
     * 降序
     */
    public static final String ORDER_DESC = "desc";

//    private static final String PAGE_SIZE = "limit";//"pageSize";
//    private static final String PAGE_NUMBER = "offset";//"pageNumber";
//    private static final String SEARCH_TEXT = "search";//"searchText";

    private long total;
    private List<?> rows;
    private String order;

    public DataGrid(List<?> dataList, long total, String order)
    {
        this.rows = dataList;
        this.total = total;
        this.order = order;
    }

    public DataGrid(List<?> dataList, long total)
    {
        this.rows = dataList;
        this.total = total;
        this.order = ORDER_ASC;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public List<?> getRows()
    {
        return rows;
    }

    public void setRows(List<?> rows)
    {
        this.rows = rows;
    }

    //region useless
    /*
    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }

    private int pageSize;
    private int pageNumber;
    private int offset;
    private int limit;

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public int getPageNumber()
    {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    public int getOffset()
    {
        return offset;
    }

    public void setOffset(int offset)
    {
        this.offset = offset;
    }
    */
    //endregion
}
