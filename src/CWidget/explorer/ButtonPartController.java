package CWidget.explorer;

import CWidget.explorer.buttonPart.IButtonPartController;
import CWidget.explorer.contentPane.ContentPane;
import CWidget.explorer.contentPane.IContentTreeModel;
import CWidget.explorer.contentPane.Node;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;

import java.util.List;

class ButtonPartController implements IButtonPartController {
    private Explorer explorer;
    private IContentTreeModel model;
    private boolean isFavorite;

    ButtonPartController(Explorer explorer, IContentTreeModel model) {
        this.explorer = explorer;
        this.model = model;
    }

    /**
     * 返回上级目录，其中收藏夹的上级目录是根目录
     */
    @Override
    public void back() {
        model.back(isFavorite);
        isFavorite = false;
        explorer.switchPage(ContentPane.PAGE.VIEWER);
    }

    /**
     * 跳转到收藏文件夹（目录）
     */
    @Override
    public void jumpToFavorite() {
        explorer.switchPage(ContentPane.PAGE.FAVORITE);
        explorer.setBackEnable(true);
        isFavorite = true;
    }

    /**
     * 预览收藏的项目
     */
    @Override
    public void previewFavorite(ToolItem toolItem) {
        List<Node> nodeList = model.getFavoriteList();

        final Menu menu = new Menu(explorer.getShell(), SWT.POP_UP);

        for (Node node : nodeList) {
            String menuItemName = node.getName();
            Image image = node.getImageDescriptor().createImage();
            MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
            menuItem.setText(menuItemName);
            // TODO 这里也有图标尺寸的问题，但是breadcrumb那种解决方法感觉并不是很好，所以先不改
            menuItem.setImage(image);
            menuItem.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    model.setRoots(node);
                    explorer.switchPage(ContentPane.PAGE.VIEWER);
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
}
