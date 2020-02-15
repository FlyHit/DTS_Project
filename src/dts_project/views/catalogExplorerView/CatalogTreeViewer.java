package dts_project.views.catalogExplorerView;

import org.eclipse.jface.viewers.*;
import org.eclipse.nebula.jface.galleryviewer.GalleryTreeViewer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;

import java.io.File;

public class CatalogTreeViewer {
    private Gallery gallery;
    private GalleryTreeViewer galleryTreeViewer;
    private ICatalogContentProvider contentProvider;

    public CatalogTreeViewer(Gallery gallery) {
        // TODO check style : 传入的Gallery应为SWT.SINGLE
        this.gallery = gallery;
        this.galleryTreeViewer = new GalleryTreeViewer(gallery);
    }

    public void setContentProvider(ICatalogContentProvider contentProvider) {
        galleryTreeViewer.setContentProvider(contentProvider);
        this.contentProvider = contentProvider;
    }

    public void setLabelProvider(ILabelProvider labelProvider) {
        galleryTreeViewer.setLabelProvider(labelProvider);
    }

    public void refresh() {
        /**
         * GalleryTreeViewer只有两层结构，不需要输入模型。
         * 模型实际上是在contentProvider中“定义”的（使用getElements()（一层）
         * 和getChildren()（两层）），这里调用setInput()会调用contentProvider
         * 的inputChanged(),相当于重新解析了一遍（调用getChildren()等），起到了
         * 刷新的作用。
         */
        galleryTreeViewer.setInput("new input");
    }

    public void setRootNode() {
    }

    public void addDoubleClickListener(IDoubleClickListener listener){
        galleryTreeViewer.addDoubleClickListener(listener);
    }

    public void openCatalog() {
    }

    /**
     * 返回当前选中的项目。该viewer设计为只能单选。
     * @return 当前选中的项目
     */
    public GalleryItem getSelectionItem() {
        GalleryItem[] galleryItems = gallery.getSelection();
        return galleryItems[0];
    }

    /**
     * 打开文件夹或文件
     */
    public void open() {
        GalleryItem galleryItem = getSelectionItem();
        String text = galleryItem.getText();
        String newRootNode = contentProvider.getRootNode() + text + "\\";
        contentProvider.setRootNode(newRootNode);
        refresh();
    }
}
