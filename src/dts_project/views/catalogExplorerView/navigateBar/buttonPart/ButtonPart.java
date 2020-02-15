package dts_project.views.catalogExplorerView.navigateBar.buttonPart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

    public ButtonPart(Composite parent) {
        toolBar = new ToolBar(parent, SWT.FLAT);
        backItem = new ToolItem(toolBar, SWT.PUSH);
        favoriteItem = new ToolItem(toolBar, SWT.DROP_DOWN);
        controller = new ButtonPartController();
        addListener();
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
