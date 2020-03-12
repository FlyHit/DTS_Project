package CWidget.explorer.contentPane.favoritePane;

import CWidget.explorer.contentPane.IContentTreeModel;
import CWidget.explorer.contentPane.Node;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * 显示收藏内容的面板
 */
public class FavoritePane extends Composite implements FavoriteListObserver {
    private Gallery gallery;
    private GalleryItem galleryGroup;
    private IContentTreeModel model;
    private FavoriteController controller;

    public FavoritePane(Composite parent, IContentTreeModel model) {
        super(parent, SWT.FLAT);
        setLayout(new FillLayout());
        this.model = model;
        this.controller = new FavoriteController(this, model);

        gallery = new Gallery(this, SWT.SINGLE | SWT.V_SCROLL);
        NoGroupRenderer noGroupRenderer = new NoGroupRenderer();
        noGroupRenderer.setItemSize(64, 64);
        gallery.setGroupRenderer(noGroupRenderer);
        gallery.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                controller.open();
            }
        });
        galleryGroup = new GalleryItem(gallery, SWT.NONE);

        model.registerFavoriteObserver(this);
    }

    private void add(Node node) {
        GalleryItem galleryItem = new GalleryItem(galleryGroup, SWT.NONE);
        galleryItem.setText(node.getName());
        galleryItem.setImage(node.getImageDescriptor().createImage(true));
        galleryItem.setData("node", node);
        layout();
    }

    private void remove(Node node) {
        for (GalleryItem item : galleryGroup.getItems()) {
            Node n = (Node) item.getData("node");
            if (n.getData().equals(node.getData())) {
                galleryGroup.remove(item);
            }
        }

        layout();
    }

    public GalleryItem[] getItems() {
        return galleryGroup.getItems();
    }

    @Override
    public void updateState(boolean isAdd, Node node) {
        if (isAdd) {
            add(node);
        } else {
            remove(node);
        }
    }

    Gallery getGallery() {
        return gallery;
    }
}
