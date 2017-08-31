package org.egc.commons.web;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * 侧边栏树形组件数据结构
 * 适用于自定义的 Vue Tree 组件
 * TODO  不适宜放到commons模块中？
 * </pre>
 *
 * @author houzhiwei
 * @date 2017/7/21 15:09
 */
public class SidebarTreeNode implements Serializable {

    private static final long serialVersionUID = -92087004981430101L;

    private String name; // 用于树节点提示
    private String title; // 用于显示
    private Integer id;
    private Integer parentId; // 父节点id
    private List<SidebarTreeNode> children;
    private String[] buttons;// 按钮数组，如 {”map“,"detail"}

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

    /**
     * Getter for property 'buttons'.
     *
     * @return Value for property 'buttons'.
     */
    public String[] getButtons() {
        return buttons;
    }

    /**
     * Setter for property 'buttons'.
     *
     * @param buttons Value to set for property 'buttons'.
     */
    public void setButtons(String[] buttons) {
        this.buttons = buttons;
    }


}
