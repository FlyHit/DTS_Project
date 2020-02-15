package dts_project.views.catalogExplorerView;

import org.eclipse.jface.viewers.ITreeContentProvider;

public interface ICatalogContentProvider extends ITreeContentProvider {
    void setRootNode(String rootNode);

    String getRootNode();
}
