package dts_project.views.catalogExplorerView;

import java.util.List;

public interface ICatalogTreeModel<T> {
    Object[] getChildren(Object parentElement);

    Object getParent(Object element);

    boolean hasChildren(Object element);

    Object getRootNode();

    /**
     * @return 父节点列表（父节点，父父节点...)
     */
    List<T> getRoots();

    void setRoots(Object rootNode);

    void registerObserver(RootNodeObserver observer);

    void notifyObserver();

    void open(String itemName);

    void back();

    void handleInput(Object input);
}
