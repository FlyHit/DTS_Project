package dts_project.views.catalogExplorerView.navigateBar.buttonPart;

import dts_project.views.catalogExplorerView.ICatalogTreeModel;

public class ButtonPartController {
    private ICatalogTreeModel model;

    public ButtonPartController(ICatalogTreeModel model) {
        this.model = model;
    }

    /**
     * 后退
     */
    public void back() {
        model.back();
    }

    /**
     * 跳转到收藏文件夹（目录）
     */
    public void jumpToCatalog() {

    }

    /**
     * 预览收藏的项目
     */
    public void previewCatalog() {

    }
}
