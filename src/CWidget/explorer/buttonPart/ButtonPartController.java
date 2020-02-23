package dts_project.views.catalogExplorerView.navigateBar.buttonPart;

import CWidget.explorer.contentPane.ICatalogTreeModel;

public class ButtonPartController {
    private ICatalogTreeModel model;
    private ButtonPart buttonPart;

    public ButtonPartController(ButtonPart buttonPart, ICatalogTreeModel model) {
        this.buttonPart = buttonPart;
        this.model = model;
    }

    /**
     * 后退
     */
    public void back() {
        model.back();
        buttonPart.getComposite().setFocus();
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
