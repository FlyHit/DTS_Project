package dts_project.views.catalogExplorerView;

import CWidget.explorer.Explorer;
import CWidget.explorer.contentPane.ICatalogTreeModel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class CatalogExplorerView extends ViewPart {
    private Explorer explorer;
    private ICatalogTreeModel model;

    public CatalogExplorerView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        model = new FileTreeModel();
        explorer = new Explorer(parent, model);
        explorer.setContentProvider(new CatalogContentProvider());
        explorer.setLabelProvider(new CatalogLabelProvider());
        explorer.refresh();
    }

    @Override
    public void setFocus() {
    }
}
