package dts_project.views.catalogExplorerView;

import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.nebula.jface.galleryviewer.GalleryTreeViewer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;

public class CatalogTreeViewer implements RootNodeObserver {
	private Gallery gallery;
	private GalleryTreeViewer galleryTreeViewer;
	private ICatalogTreeModel model;
	private ICatalogController controller;

	public CatalogTreeViewer(Gallery gallery, ICatalogTreeModel model) {
		// TODO check style : 传入的Gallery应为SWT.SINGLE
		this.gallery = gallery;
		this.galleryTreeViewer = new GalleryTreeViewer(gallery);
		this.model = model;
		this.model.registerObserver(this);
		this.controller = new CatalogController(this, model);
	}

	public void setContentProvider(ITreeContentProvider contentProvider) {
		galleryTreeViewer.setContentProvider(contentProvider);
	}

	public void setLabelProvider(ILabelProvider labelProvider) {
		galleryTreeViewer.setLabelProvider(labelProvider);
	}

	public void setInput() {
		galleryTreeViewer.setInput(model);
	}

	public void refresh() {
		/**
		 * GalleryTreeViewer只有两层结构，不需要输入模型。
		 * 模型实际上是在contentProvider中“定义”的（使用getElements()（一层）
		 * 和getChildren()（两层）），这里调用setInput()会调用contentProvider
		 * 的inputChanged(),相当于重新解析了一遍（调用getChildren()等），起到了 刷新的作用。
		 */
		galleryTreeViewer.setInput("new input");
	}

	public void addDoubleClickListener(IDoubleClickListener listener) {
		galleryTreeViewer.addDoubleClickListener(listener);
	}

	public void openCatalog() {
	}

	/**
	 * 返回当前选中的项目。该viewer设计为只能单选。
	 *
	 * @return 当前选中的项目
	 */
	private GalleryItem getSelectionItem() {
		GalleryItem[] galleryItems = gallery.getSelection();
		return galleryItems[0];
	}

	/**
	 * 打开文件夹或文件
	 */
	public void open() {
		GalleryItem galleryItem = getSelectionItem();
		controller.open(galleryItem);
	}

	@Override
	public void update() {
		galleryTreeViewer.setInput(model);
	}
}
