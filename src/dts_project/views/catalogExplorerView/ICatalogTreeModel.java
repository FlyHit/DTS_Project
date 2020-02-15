package dts_project.views.catalogExplorerView;

public interface ICatalogTreeModel {
    Object[] getChildren(Object parentElement);

    Object getParent(Object element);

    boolean hasChildren(Object element);

    Object getRootNode();

    void setRootNode(Object rootNode);

    void registerObserver(RootNodeObserver observer);

    void notifyObserver();
}
