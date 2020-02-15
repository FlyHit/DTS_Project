package dts_project.views.catalogExplorerView;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.nebula.jface.galleryviewer.FlatTreeContentProvider;
import org.eclipse.nebula.jface.galleryviewer.GalleryTreeViewer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class CatalogExplorerView extends ViewPart {
	private Gallery gallery;
	private CatalogTreeViewer catalogTreeViewer;

	public CatalogExplorerView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		// TODO galleryItem显示text有省略号，改成分行显示
		// 单选&垂直滚动条
		gallery = new Gallery(parent, SWT.SINGLE | SWT.V_SCROLL);
		// 不显示group的标题
		NoGroupRenderer noGroupRenderer = new NoGroupRenderer();
		noGroupRenderer.setItemSize(64, 64);
//		DefaultGalleryItemRenderer itemRenderer = new DefaultGalleryItemRenderer();
//		// item被选择后的背景颜色
//		itemRenderer.setSelectionBackgroundColor(new Color(parent.getDisplay(), 180,225,255));
		gallery.setGroupRenderer(noGroupRenderer);
//		gallery.setItemRenderer(itemRenderer);
		catalogTreeViewer = new CatalogTreeViewer(gallery);
		catalogTreeViewer.setContentProvider(new CatalogContentProvider());
		catalogTreeViewer.setLabelProvider(new CatalogLabelProvider());
		catalogTreeViewer.refresh();
		catalogTreeViewer.addDoubleClickListener(event -> {
			catalogTreeViewer.open();
		});
	}

	@Override
	public void setFocus() {
	}

}
