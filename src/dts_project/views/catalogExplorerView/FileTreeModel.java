package dts_project.views.catalogExplorerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileTreeModel implements ICatalogTreeModel {
    private String rootNode;
    // Gallery为两层结构，使用roots保存根节点来丰富层次
    // 可能没必要使用树保存完整结构，感觉遍历开销比较大
    private List<String> roots;
    private ArrayList<RootNodeObserver> rootNodeObservers;

    public FileTreeModel() {
        this.rootNode = "C:\\Program Files\\eclipse rcp\\eclipse201912\\eclipse\\";
        this.roots = new ArrayList<>();
        roots.add(rootNode);
        rootNodeObservers = new ArrayList<>();
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

    @Override
    public Object getRootNode() {
        return rootNode;
    }

    /**
     * 设置文件浏览器的根目录。
     * 如果rootNode非有效路径，则操作失败。
     *
     * @param rootNode 文件的根路径
     */
    @Override
    public void setRootNode(Object rootNode) {
        if (rootNode instanceof String) {
            File file = new File((String) rootNode);
            if (file.exists() && file.isDirectory()) {
                this.rootNode = (String) rootNode;
                notifyObserver();
            } else {
                // Todo 抛出异常会不会更好
                System.out.println("非有效文件根路径");
            }
        } else {
            System.out.println("设置失败，rootNode必须是String类型！");
        }
    }

    @Override
    public void registerObserver(RootNodeObserver observer) {
        rootNodeObservers.add(observer);
    }

    @Override
    public void notifyObserver() {
        for (RootNodeObserver observer : rootNodeObservers) {
            observer.update();
        }
    }
}
