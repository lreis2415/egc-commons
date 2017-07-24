package org.egc.commons.web;

import java.io.Serializable;
import java.util.List;

/**
 * 侧边栏树形组件数据结构
 *
 * @author houzhiwei
 * @date 2017/7/21 15:09
 */
public class SidebarTreeNode implements Serializable {

    private static final long serialVersionUID = -92087004981430101L;

    private String name; // 用于树节点显示
    private String title; // 用于显示提示
    private Integer id;
    private Integer parentId; // 父节点id
    private boolean showDetail = false; // 是否添加 “show detail” 按钮
    private boolean showOnMap = false; // 是否添加 “show on map” 按钮
    private List<SidebarTreeNode> children;

    // Integer 可以为null
    public SidebarTreeNode(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * Getter for property 'parentId'.
     *
     * @return Value for property 'parentId'.
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * Setter for property 'parentId'.
     *
     * @param parentId Value to set for property 'parentId'.
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for property 'name'.
     *
     * @param name Value to set for property 'name'.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for property 'title'.
     *
     * @return Value for property 'title'.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for property 'title'.
     *
     * @param title Value to set for property 'title'.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for property 'id'.
     *
     * @return Value for property 'id'.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter for property 'id'.
     *
     * @param id Value to set for property 'id'.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter for property 'showDetail'.
     *
     * @return Value for property 'showDetail'.
     */
    public boolean isShowDetail() {
        return showDetail;
    }

    /**
     * Setter for property 'showDetail'.
     *
     * @param showDetail Value to set for property 'showDetail'.
     */
    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }

    /**
     * Getter for property 'showOnMap'.
     *
     * @return Value for property 'showOnMap'.
     */
    public boolean isShowOnMap() {
        return showOnMap;
    }

    /**
     * Setter for property 'showOnMap'.
     *
     * @param showOnMap Value to set for property 'showOnMap'.
     */
    public void setShowOnMap(boolean showOnMap) {
        this.showOnMap = showOnMap;
    }

    /**
     * Getter for property 'children'.
     *
     * @return Value for property 'children'.
     */
    public List<SidebarTreeNode> getChildren() {
        return children;
    }

    /**
     * Setter for property 'children'.
     *
     * @param children Value to set for property 'children'.
     */
    public void setChildren(List<SidebarTreeNode> children) {
        this.children = children;
    }

    private List buttons;
}
