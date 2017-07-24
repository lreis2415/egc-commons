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

    private List<SidebarTreeNode> children;

    public SidebarTree(String rootTitle) {
        SidebarTreeNode root = new SidebarTreeNode(null);
        root.setName("root");
        root.setId(0);
        root.setTitle(rootTitle);
    }

    public List<SidebarTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<SidebarTreeNode> children) {
        this.children = children;
    }


}
