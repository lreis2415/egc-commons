package org.egc.commons.web;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * 树形组件数据
 * 默认生成id 为 0 的根节点
 * </pre>
 *
 * @author houzhiwei
 * @date 2017/7/24 9:59
 */
public class SidebarTree implements Serializable {

    private boolean showLoading = false;
    private List<SidebarTreeNode> children;

    private SidebarTreeNode root;

    public SidebarTree(String rootTitle) {
        root = new SidebarTreeNode(-1);
        root.setName("root");
        root.setId(0);
        root.setTitle(rootTitle);
    }

    public void setChildren(List<SidebarTreeNode> children) {
        this.children = children;
        root.setChildren(this.children);
    }

    /**
     * Getter for property 'root'.
     *
     * @return Value for property 'root'.
     */
    public SidebarTreeNode getRoot() {
        return root;
    }

    /**
     * Getter for property 'showLoading'.
     *
     * @return Value for property 'showLoading'.
     */
    public boolean isShowLoading() {
        return showLoading;
    }

    /**
     * Setter for property 'showLoading'.
     *
     * @param showLoading Value to set for property 'showLoading'.
     */
    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
    }
}
