package dts_project.views.catalogExplorerView;

import org.eclipse.jface.viewers.ITreeContentProvider;

public class CatalogContentProvider implements ITreeContentProvider {
    private ICatalogTreeModel model;
    private String rootNode;

    public CatalogContentProvider() {
    }

    @Override
    public Object[] getElements(Object inputElement) {
        this.model = (ICatalogTreeModel) inputElement;
        this.rootNode = (String) model.getRootNode();

        return new Object[]{rootNode};
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        return model.getChildren(parentElement);
    }

    @Override
    public Object getParent(Object element) {
        return model.getParent(element);
    }

    @Override
    public boolean hasChildren(Object element) {
        return model.hasChildren(element);
    }
}
