package dts_project.views.catalogExplorerView;

import CWidget.explorer.Explorer;
import CWidget.explorer.contentPane.IContentTreeModel;
import CWidget.explorer.contentPane.Node;
import CWidget.explorer.contentPane.catalogPane.CatalogLabelProvider;
import CWidget.explorer.contentPane.catalogPane.CatalogTreeContentProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class CatalogExplorerView extends ViewPart {
    private Explorer explorer;
    private IContentTreeModel model;

    public CatalogExplorerView() {

    }

    @Override
    public void createPartControl(Composite parent) {
        Node node = new FileNode("C:\\Program Files\\eclipse rcp\\eclipse201912\\eclipse");
        model = new FileTreeModel(node);
        explorer = new Explorer(parent, model);
        explorer.setContentProvider(new CatalogTreeContentProvider());
        explorer.setLabelProvider(new CatalogLabelProvider());
        explorer.refresh();
    }

    @Override
    public void setFocus() {
    }
}
