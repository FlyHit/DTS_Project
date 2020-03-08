package dts_project.views.catalogExplorerView.navigateBar.buttonPart;

import CWidget.explorer.contentPane.ContentPane;
import CWidget.explorer.contentPane.IContentTreeModel;
import CWidget.explorer.contentPane.Node;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

import java.util.List;

class ButtonPartController {
    private IContentTreeModel model;
    private ButtonPart buttonPart;
    private ContentPane contentPane;
    private boolean isFavorite;

    ButtonPartController(ButtonPart buttonPart, IContentTreeModel model) {
        this.buttonPart = buttonPart;
        this.model = model;
    }

    /**
     * 返回上级目录，其中收藏夹的上级目录是根目录
     */
    void back() {
        if (isFavorite) {
            contentPane.switchPage(ContentPane.PAGE.VIEWER);
            isFavorite = false;
            model.setRoots(buttonPart.ROOTNODE);
        } else {
            model.back();
        }

        buttonPart.setFocus();
    }

    /**
     * 跳转到收藏文件夹（目录）
     */
    void jumpToFavorite() {
        setContentPane();
        contentPane.switchPage(ContentPane.PAGE.FAVORITE);
        isFavorite = true;
        buttonPart.getBackButton().setEnabled(true);
        buttonPart.setFocus();
    }

    /**
     * 预览收藏的项目
     */
    void previewFavorite(ToolItem toolItem) {
        setContentPane();
        List<Node> nodeList = model.getFavoriteList();

        final Menu menu = new Menu(contentPane.getShell(), SWT.POP_UP);

        for (Node node : nodeList) {
            String menuItemName = node.getName();
            MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
            menuItem.setText(menuItemName);
            menuItem.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    model.setRoots(node);
                    contentPane.switchPage(ContentPane.PAGE.VIEWER);
                    isFavorite = false;
                }
            });
        }

        Rectangle rect = toolItem.getBounds();
        Point pt = new Point(rect.x, rect.y + rect.height);
        pt = toolItem.getParent().toDisplay(pt);
        menu.setLocation(pt.x, pt.y);
        menu.setVisible(true);
    }

    /**
     * 获取contentPane,并保存一份引用
     */
    private void setContentPane() {
        if (contentPane == null) {
            Composite explorer = buttonPart.getParent().getParent();
            for (Control child : explorer.getChildren()) {
                Object name = child.getData("name");
                if (name != null && name.equals("contentPane")) {
                    this.contentPane = (ContentPane) child;
                }
            }
        }
    }
}
