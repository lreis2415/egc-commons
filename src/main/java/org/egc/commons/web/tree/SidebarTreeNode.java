package org.egc.commons.web.tree;

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
    private boolean checked = false;

    /**
     * Getter for property 'checked'.
     *
     * @return Value for property 'checked'.
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * Setter for property 'checked'.
     *
     * @param checked Value to set for property 'checked'.
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * Getter for property 'checkable'.
     *
     * @return Value for property 'checkable'.
     */
    public boolean isCheckable() {
        return checkable;
    }

    /**
     * Setter for property 'checkable'.
     *
     * @param checkable Value to set for property 'checkable'.
     */
    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    private boolean checkable = false;
    // 用于树节点提示
    private String name;
    // 用于显示
    private String title;
    /**
     * 用于指示数据类型： Samples，Vector，Raster
     */
    private String type;
    private Integer id;
    // 父节点id
    private Integer parentId;
    private List<SidebarTreeNode> children;
    // 按钮数组，如 {”map“,"detail"}
    private String[] buttons;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
