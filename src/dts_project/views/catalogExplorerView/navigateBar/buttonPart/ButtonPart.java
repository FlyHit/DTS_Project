package dts_project.views.catalogExplorerView.navigateBar.buttonPart;

import dts_project.Application;
import dts_project.views.catalogExplorerView.ICatalogTreeModel;
import dts_project.views.catalogExplorerView.IImageKeys;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * 导航栏的按钮部分：返回按钮+收藏按钮
 */
public class ButtonPart {
    private ButtonPartController controller;
    private ToolBar toolBar;
    private ToolItem backItem;
    private ToolItem favoriteItem;

    public ButtonPart(Composite parent, ICatalogTreeModel model) {
        toolBar = new ToolBar(parent, SWT.FLAT);
        final Image[] image = {null};
        backItem = new ToolItem(toolBar, SWT.PUSH);
        ResourceLocator.imageDescriptorFromBundle(Application.PLUGIN_ID, IImageKeys.BACK)
                .ifPresent(imageDescriptor -> image[0] = imageDescriptor.createImage());
        backItem.setImage(image[0]);
        favoriteItem = new ToolItem(toolBar, SWT.DROP_DOWN);
        ResourceLocator.imageDescriptorFromBundle(Application.PLUGIN_ID, IImageKeys.STAR)
                .ifPresent(imageDescriptor -> image[0] = imageDescriptor.createImage());
        favoriteItem.setImage(image[0]);
        controller = new ButtonPartController(model);
        addListener();
    }

    public void setLayoutData(Object layoutData) {
        toolBar.setLayoutData(layoutData);
    }

    private void addListener() {
        backItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                controller.back();
            }
        });

        favoriteItem.addListener(SWT.Selection, event -> {
            if (event.detail == SWT.ARROW) {
                controller.previewCatalog();
            } else {
                controller.jumpToCatalog();
            }
        });
    }
}
