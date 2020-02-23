package dts_project.views.catalogExplorerView;

import CWidget.explorer.contentPane.ICatalogTreeModel;
import CWidget.explorer.contentPane.RootNodeObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileTreeModel implements ICatalogTreeModel {
    private final String ROOTNODE = "C:\\Program Files\\eclipse rcp\\eclipse201912\\eclipse";
    private String rootNode;
    public static final String BASEPATH = "C:\\Program Files\\eclipse rcp\\eclipse201912\\";
    // Gallery为两层结构，使用roots保存根节点来丰富层次
    // 可能没必要使用树保存完整结构，感觉遍历开销比较大
    private List<String> roots;
    private ArrayList<RootNodeObserver> rootNodeObservers;

    public FileTreeModel() {
        this.rootNode = ROOTNODE;
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

    /*
     * 文件已经保存在硬盘上了，没必要特意再将他们保存起来，只需要 知道如何查找parent和children就行了
     */
    @Override
    public Object getParent(Object element) {
        // 最底层目录没有parent
        if (!element.equals(ROOTNODE) && isFilePath(element)) {
            return new File((String) element).getParent();
        }

        return null;
    }

    /*
     * 创建树时如果某节点是展开状态便会调用该方法判断有无children，
     * 如果有便调用getChildren()。由于GalleryItem（节点）都是展开状态，
     * 因此会遍历每个节点的所有对象并创建。如果按照hasChildren()按普通树的
     * 写法(不止两层，而Gallery的层次是2）,那么创建树时会遍历所有节点并创建， 这么一来重新创建树时（如后退）就会卡顿（对象过多）
     */

    /**
     * 判断有无children，层次为2
     *
     * @param element 特定的节点
     * @return 如果有，则true；反之为false
     */
    @Override
    public boolean hasChildren(Object element) {
        return rootNode == element;
    }

    @Override
    public Object getRootNode() {
        return rootNode;
    }

    @Override
    public List<String> getRoots() {
        return roots;
    }

    @Override
    public void setRoots(Object rootNode) {
        if (setRootNode(rootNode)) {
            updateRoots((String) rootNode);
            notifyObserver();
        }
    }

    /**
     * 设置文件浏览器的根目录。 如果rootNode非有效路径，则操作失败。
     *
     * @param rootNode 文件的根路径
     * @return 如果设置成功，返回true；反之，false
     */
    private boolean setRootNode(Object rootNode) {
        if (rootNode instanceof String) {
            File file = new File((String) rootNode);
            if (file.exists() && file.isDirectory()) {
                this.rootNode = (String) rootNode;

                return true;
            } else {
                // Todo 抛出异常会不会更好
                System.out.println("非有效文件根路径");
            }
        } else {
            System.out.println("设置失败，rootNode必须是String类型！");
        }

        return false;
    }

    /**
     * 打开文件夹
     */
    public void open(String itemName) {
        String selectedNode = getRootNode() + "\\" + itemName;

        if (setRootNode(selectedNode)) {
            roots.add(selectedNode);
            notifyObserver();
        }
    }

    /**
     * 返回上一级，如果已是最上级则忽略。
     */
    @Override
    public void back() {
        if (!rootNode.equals(ROOTNODE)) {
            int lastIndex = roots.size() - 1;

            if (setRootNode(roots.get(lastIndex - 1))) {
                roots.remove(lastIndex);
                notifyObserver();
            }
        }
    }

    @Override
    public void handleInput(Object input) {
        String pathname = BASEPATH + input;
        if (pathname.endsWith("\\")) {
            pathname = pathname.substring(0, pathname.length() - 1);
        }

        setRoots(pathname);
        notifyObserver();
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

    /**
     * 判断给定的对象是否是文件路径
     *
     * @param element 给定的对象
     * @return 如果是文件路径，则true；反之为false；
     */
    private boolean isFilePath(Object element) {
        if (element instanceof String) {
            String path = (String) element;
            File file = new File(path);
            return file.exists();
        }

        return false;
    }

    /**
     * 更新根节点列表。 给定根节点，将其parent一一添加到根节点列表roots，直至ROOTNODE
     *
     * @param element 当前的根节点
     */
    private void updateRoots(String element) {
        String parent = (String) getParent(element);

        if (!element.equals(ROOTNODE)) {
            if (!parent.equals(ROOTNODE)) {
                updateRoots(parent);
            } else {
                roots = new ArrayList<>();
                roots.add(parent);
            }
        } else {
            roots = new ArrayList<>();
        }

        roots.add(element);
    }
}
