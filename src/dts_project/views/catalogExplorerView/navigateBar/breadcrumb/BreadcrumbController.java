package dts_project.views.catalogExplorerView.navigateBar.breadcrumb;

import dts_project.views.catalogExplorerView.ICatalogTreeModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;

public class BreadcrumbController implements IBreadcrumbController {
	private ICatalogTreeModel model;
	private Breadcrumb breadcrumb;

	public BreadcrumbController(ICatalogTreeModel model, Breadcrumb breadcrumb) {
		this.model = model;
		this.breadcrumb = breadcrumb;
	}

	@Override
	public void jumpToCatalog(Object catalog) {
		model.setRoots(catalog);
	}

	@Override
	public void previewCatalog(Object item) {
		ToolItem toolItem = (ToolItem) item;

		final Menu menu = new Menu(toolItem.getParent().getShell(), SWT.POP_UP);

		for (Object p : model.getChildren(toolItem.getData("path"))) {
			String menuItemName = getItemName((String) p);
			MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
			menuItem.setText(menuItemName);
			menuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					jumpToCatalog(p);
				}
			});
		}

		Rectangle rect = toolItem.getBounds();
		Point pt = new Point(rect.x, rect.y + rect.height);
		pt = toolItem.getParent().toDisplay(pt);
		menu.setLocation(pt.x, pt.y);
		menu.setVisible(true);
	}

	@Override
	public void inputCatalog(Object catalog) {
		model.handleInput(catalog);
		// 使siteText lost focus
		breadcrumb.getComposite().setFocus();
	}

	/**
	 * 在文件路径提取出文件/文件夹名 注意：文件路径以'\'结尾
	 *
	 * @param path 文件路径
	 * @return 文件/文件夹名
	 */
	static String getItemName(String path) {
		String itemName = "";
		int lastIndex = path.length() - 1;

		for (int i = lastIndex; i > 0; i--) {
			if (path.charAt(i) == '\\') {
				// subString的结果不包含endIndex处的字符
				itemName = path.substring(i + 1, lastIndex + 1);
				break;
			}
		}

		return itemName;
	}
}
