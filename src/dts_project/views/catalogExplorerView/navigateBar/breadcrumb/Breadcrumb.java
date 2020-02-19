package dts_project.views.catalogExplorerView.navigateBar.breadcrumb;

import dts_project.views.catalogExplorerView.ICatalogTreeModel;
import dts_project.views.catalogExplorerView.RootNodeObserver;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * breadcrumb样式的导航栏，单击跳转，使用ComboBox预览下级目录
 */
public class Breadcrumb implements RootNodeObserver {
	private IBreadcrumbController controller;
	private ICatalogTreeModel model;
	private ToolBar toolBar;
	private Composite composite;

	public Breadcrumb(Composite parent, ICatalogTreeModel model) {
		this.composite = parent;
		this.model = model;
		model.registerObserver(this);
		toolBar = new ToolBar(parent, SWT.DROP_DOWN);
		controller = new BreadcrumbController(model, this);
		addAll();
	}

	public void add(String itemName) {
		new BreadcrumbItem(this).setText(itemName);
		composite.layout();
	}

	public void addAll() {
		removeAll();

		for (Object root : model.getRoots()) {
			if (root instanceof String) {
				add((String) root);
			} else {
				System.out.println("创建失败，参数必须为String类型");
				break;
			}
		}
	}

	public void remove() {
		int lastItemIndex = toolBar.getItemCount() - 1;
		toolBar.getItem(lastItemIndex).dispose();
	}

	public void removeAll() {
		for (ToolItem item : toolBar.getItems()) {
			item.dispose();
		}
	}

	ToolBar getToolBar() {
		return toolBar;
	}

	IBreadcrumbController getController() {
		return controller;
	}

	@Override
	public void update() {
//		List list = model.getRoots();
		addAll();
//		int listSize = list.size();
//		int breadcrumbSize = toolBar.getItemCount();
//		if (listSize < breadcrumbSize) {
//			remove();
//		} else {
//			add((String) list.get(listSize - 1));
//		}
	}

	public void setLayoutData(Object layout) {
		toolBar.setLayoutData(layout);
	}
}
