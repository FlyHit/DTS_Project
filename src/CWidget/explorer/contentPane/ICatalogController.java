package CWidget.explorer.contentPane;

import org.eclipse.nebula.widgets.gallery.GalleryItem;

public interface ICatalogController {
    /**
     * 打开文件夹/文件
     *
     * @param galleryItem
     */
    void open(GalleryItem galleryItem);
}
