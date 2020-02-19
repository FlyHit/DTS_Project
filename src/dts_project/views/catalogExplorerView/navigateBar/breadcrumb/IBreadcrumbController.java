package dts_project.views.catalogExplorerView.navigateBar.breadcrumb;

public interface IBreadcrumbController {
    /**
     * 跳转到目录
     */
    void jumpToCatalog(Object catalog);

    /**
     * 预览目录
     *
     * @param item
     */
    void previewCatalog(Object item);

    void inputCatalog(Object catalog);
}
