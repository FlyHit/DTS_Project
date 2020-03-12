package CWidget.explorer.contentPane.catalogPane;

import CWidget.explorer.contentPane.IContentTreeModel;
import CWidget.explorer.contentPane.Node;
import org.eclipse.jface.viewers.ITreeContentProvider;

public class CatalogTreeContentProvider implements ITreeContentProvider {
    private IContentTreeModel model;

    @Override
    public Object[] getElements(Object inputElement) {
        this.model = (IContentTreeModel) inputElement;
        Object rootNode = model.getRootNode();

        return new Object[]{rootNode};
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof Node) {
            Node parentNode = (Node) parentElement;
            return model.getChildren(parentNode);
        }

        return null;
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof Node) {
            Node node = (Node) element;
            return model.getParent(node);
        }

        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof Node) {
            Node node = (Node) element;
            return model.hasChildren(node);
        }

        return false;
    }
}
