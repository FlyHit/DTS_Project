package dts_project.views.catalogExplorerView.navigateBar.breadcrumb;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolItem;

public class BreadcrumbItem {
    private ToolItem toolItem;

    public BreadcrumbItem(Breadcrumb parent) {
        toolItem = new ToolItem(parent.getToolBar(), SWT.DROP_DOWN);
        toolItem.addListener(SWT.Selection, event -> {
            IBreadcrumbController controller = parent.getController();
            ToolItem item = (ToolItem) event.widget;
            String path = (String) item.getData("path");

            if (event.detail == SWT.ARROW) {
                controller.previewCatalog(item);
            } else {
                controller.jumpToCatalog(path);
            }
        });
    }

    public void setText(String text) {
        String itemName = BreadcrumbController.getItemName(text);
        toolItem.setText(itemName);
        toolItem.setData("path", text);
    }
}
