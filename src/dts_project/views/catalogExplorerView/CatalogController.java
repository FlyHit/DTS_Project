package dts_project.views.catalogExplorerView;

import org.eclipse.nebula.widgets.gallery.GalleryItem;

public class CatalogController implements ICatalogController {
    private CatalogTreeViewer viewer;
    private ICatalogTreeModel model;

    public CatalogController(CatalogTreeViewer viewer, ICatalogTreeModel model) {
        this.viewer = viewer;
        this.model = model;
    }

    @Override
    public void open(GalleryItem galleryItem) {
        String text = galleryItem.getText();
        String newRootNode = model.getRootNode() + text + "\\";
        model.setRootNode(newRootNode);
    }
}
