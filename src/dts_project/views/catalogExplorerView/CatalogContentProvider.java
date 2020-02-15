package dts_project.views.catalogExplorerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CatalogContentProvider implements ICatalogContentProvider {
    private String rootNode;
    // Gallery为两层结构，使用roots保存根节点来丰富层次
    // 可能没必要使用树保存完整结构，感觉遍历开销比较大
    private List<String> roots;

    public CatalogContentProvider() {
        this.rootNode = "C:\\Program Files\\eclipse rcp\\eclipse201912\\eclipse\\";
        this.roots = new ArrayList<>();
        this.roots.add(rootNode);
    }

    @Override
    public Object[] getElements(Object inputElement) {
        return new Object[]{rootNode};
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        List<String> childList = new ArrayList<>();
        File file = new File((String) parentElement);

        for (File f : Objects.requireNonNull(file.listFiles())) {
            childList.add(f.getPath());
        }

        return childList.toArray();
    }

    @Override
    public Object getParent(Object element) {
        if (element == rootNode) {
            int index = roots.indexOf(rootNode);
            // index为-1则不存在
            if (index > 0) {
                return roots.get(index - 1);
            } else {
                return null;
            }
        } else {
            return rootNode;
        }
    }

    @Override
    public boolean hasChildren(Object element) {
        return new File((String) element).isDirectory();
    }

    public String getRootNode() {
        return rootNode;
    }

    /**
     * 设置文件浏览器的根目录。
     * 如果rootNode非有效路径，则操作失败。
     *
     * @param rootNode 文件的根路径
     */
    public void setRootNode(String rootNode) {
        File file = new File(rootNode);
        if (file.exists() && file.isDirectory()) {
            this.rootNode = rootNode;
        }
    }
}
