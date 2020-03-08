package dts_project.views.catalogExplorerView.navigateBar.breadcrumb;

import CWidget.explorer.contentPane.Node;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolItem;

public class BreadcrumbItem {
    private ToolItem toolItem;

    public BreadcrumbItem(Breadcrumb parent) {
        toolItem = new ToolItem(parent.getToolBar(), SWT.DROP_DOWN);
        toolItem.addListener(SWT.Selection, event -> {
            IBreadcrumbController controller = parent.getController();
            ToolItem item = (ToolItem) event.widget;
            Node catalog = (Node) item.getData("node");

            if (event.detail == SWT.ARROW) {
                controller.previewCatalog(item);
            } else {
                controller.jumpToCatalog(catalog);
            }
        });
    }

    public void setText(String text) {
        toolItem.setText(text);
    }

    public void setNode(Node node) {
        toolItem.setData("node", node);
    }
}
