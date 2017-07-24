package org.egc.commons.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表格：格式化数据为前端表格提供json
 *
 * @author houzhiwei
 * @date 2016/7/9 17:35.
 */
public class DataTable implements Serializable {

    private static final long serialVersionUID = 2052103483654583307L;

    /**
     * 升序
     */
    public static final String ORDER_ASC = "asc";
    /**
     * 降序
     */
    public static final String ORDER_DESC = "desc";
    /**
     * 数据内容
     */
    private static final String ROWS = "rows";
    /**
     * 总数据量
     */
    private static final String TOTAL = "total";
    private static final String PAGE_SIZE = "limit";//"pageSize";
    private static final String PAGE_NUMBER = "offset";//"pageNumber";
    private static final String SEARCH_TEXT = "search";//"searchText";
    private static final String SORT_NAME = "sort";
    private static final String SORT_ORDER = "order";

    private Map<String, Object> data = null;

    public Map<String, Object> getDataMap()
    {
        return data;
    }

    /**
     * 组成一个规范化的map，便于转换为JSON供前端table接受
     *
     * @param dataList 数据
     * @param total    数据量
     */
    public DataTable(List<?> dataList, long total, int pageSize, int pageNumber, String order)
    {
        data = new HashMap<>();
        data.put(ROWS, dataList);
        data.put(SORT_ORDER, order);
        data.put(TOTAL, total);
        data.put(PAGE_NUMBER, pageNumber);
        data.put(PAGE_SIZE, pageSize);
    }

    public void setSortOrder(String sortOrder)
    {
        data.put(SORT_ORDER, sortOrder);
    }

    public void setSortName(String sortName)
    {
        data.put(SORT_NAME, sortName);
    }

    public void setSearchText(String searchText)
    {
        data.put(SEARCH_TEXT, searchText);
    }

    public void setPageNumber(int pageNumber)
    {
        data.put(PAGE_NUMBER, pageNumber);
    }

    public void setPageSize(int pageSize)
    {
        data.put(PAGE_SIZE, pageSize);
    }
}

