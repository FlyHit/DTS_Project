package CWidget.explorer.contentPane.catalogPane;

import CWidget.explorer.contentPane.IContentTreeModel;
import CWidget.explorer.contentPane.Node;
import CWidget.explorer.contentPane.RootNodeObserver;
import dts_project.Application;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.nebula.jface.galleryviewer.GalleryTreeViewer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;

import java.util.ArrayList;
import java.util.List;

public class CatalogPane extends Composite implements RootNodeObserver {
    private Gallery gallery;
    private GalleryTreeViewer galleryTreeViewer;
    private IContentTreeModel model;
    private ICatalogController controller;
    private ImageDescriptor starDescriptor;

    public CatalogPane(Composite parent, IContentTreeModel model) {
        super(parent, SWT.FLAT);
        setLayout(new FillLayout());
        starDescriptor = ResourceLocator
                .imageDescriptorFromBundle(Application.PLUGIN_ID, "resources/icons/star_icon_16.png")
                .orElse(ImageDescriptor.getMissingImageDescriptor());
        // TODO galleryItem显示text有省略号，改成分行显示
        // 单选&垂直滚动条
        gallery = new Gallery(this, SWT.SINGLE | SWT.V_SCROLL);
        // 不显示group的标题
        NoGroupRenderer noGroupRenderer = new NoGroupRenderer();
        noGroupRenderer.setItemSize(64, 64);
//		DefaultGalleryItemRenderer itemRenderer = new DefaultGalleryItemRenderer();
//		// item被选择后的背景颜色
//		itemRenderer.setSelectionBackgroundColor(new Color(parent.getDisplay(), 180,225,255));
        gallery.setGroupRenderer(noGroupRenderer);
        this.galleryTreeViewer = new GalleryTreeViewer(gallery);
        this.model = model;
        this.model.registerRootNodeObserver(this);
        this.controller = new CatalogController(this, model);
        galleryTreeViewer.addDoubleClickListener(this::doubleClick);
        createContextMenu();
    }

    public void setContentProvider(ITreeContentProvider contentProvider) {
        galleryTreeViewer.setContentProvider(contentProvider);
    }

    public void setLabelProvider(ILabelProvider labelProvider) {
        galleryTreeViewer.setLabelProvider(labelProvider);
    }

    /**
     * 返回当前选中的项目。该viewer设计为只能单选。
     *
     * @return 当前选中的项目
     */
    GalleryItem getSelectionItem() {
        GalleryItem[] galleryItems = gallery.getSelection();
        if (galleryItems.length != 0) {
            return galleryItems[0];
        }

        return null;
    }

    /**
     * 打开文件夹或文件
     */
    private void open() {
        GalleryItem galleryItem = getSelectionItem();
        controller.open(galleryItem);
    }

    @Override
    public void updateState() {
        /*
         * GalleryTreeViewer只有两层结构。 这里调用setInput()会调用contentProvider的inputChanged(),
         * 相当于重新解析了一遍（调用getChildren()等），起到了刷新的作用。
         */
        galleryTreeViewer.setInput(model);

        // 与收藏列表比较，为收藏的项目添加星星
        GalleryItem[] items = gallery.getItems()[0].getItems();
        List<Object> favoriteList = new ArrayList<>();
        for (Node node : model.getFavoriteList()) {
            favoriteList.add(node.getData());
        }

        for (GalleryItem item : items) {
            Node node = (Node) item.getData();
            Image image = null;
            if (node != null && favoriteList.contains(node.getData())) {
                image = starDescriptor.createImage();
            }
            item.setData(DefaultGalleryItemRenderer.OVERLAY_BOTTOM_RIGHT, image);
        }
    }

    private void doubleClick(DoubleClickEvent event) {
        open();
    }

    private void createContextMenu() {
        MenuManager contextMenu = new MenuManager("#ViewerMenu"); //$NON-NLS-1$
        contextMenu.setRemoveAllWhenShown(true);
        contextMenu.addMenuListener(this::fillContextMenu);

        Menu menu = contextMenu.createContextMenu(galleryTreeViewer.getControl());
        galleryTreeViewer.getControl().setMenu(menu);
    }

    private void fillContextMenu(IMenuManager contextMenu) {
        contextMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

        for (IContributionItem item : controller.createMenuItem()) {
            contextMenu.add(item);
        }
    }

    Gallery getGallery() {
        return gallery;
    }
}
