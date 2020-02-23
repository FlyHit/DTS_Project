package dts_project.views.catalogExplorerView;

import dts_project.views.catalogExplorerView.navigateBar.breadcrumb.Breadcrumb;
import dts_project.views.catalogExplorerView.navigateBar.buttonPart.ButtonPart;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class CatalogExplorerView extends ViewPart {
    private Composite catalogComposite;
    private Composite searchComposite;
    private Composite navigateComposite;
    private Composite exploreComposite;

    private Gallery gallery;
    private CatalogTreeViewer catalogTreeViewer;
    private ICatalogTreeModel model;
    private ButtonPart buttonPart;
    private Breadcrumb breadcrumb;
    private final int SEARCHHEIGHT = 40;  // 搜索面板高度
    private final int NAVIGATEHEIGHT = 40;  // 导航面板高度
    private final int BUTTONPARTWIDTH = 80;

    public CatalogExplorerView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        catalogComposite = new Composite(parent, SWT.FLAT);
        GridLayout gridLayout = new GridLayout();
        catalogComposite.setLayout(gridLayout);

        model = new FileTreeModel();

        initSearchComposite();
        initNavigateComposite();
        initExploreComposite();
    }

    private void initSearchComposite() {
        GridData gridData = new GridData(GridData.FILL, GridData.FILL,
                true, false);
        searchComposite = new Composite(catalogComposite, SWT.FLAT);
        gridData.heightHint = SEARCHHEIGHT;
        searchComposite.setLayoutData(gridData);
        searchComposite.setLayout(new FillLayout());
    }

    private void initNavigateComposite() {
        GridData gridData = new GridData(GridData.FILL, GridData.FILL,
                true, false);
        navigateComposite = new Composite(catalogComposite, SWT.FLAT);
        gridData.heightHint = NAVIGATEHEIGHT;
        navigateComposite.setLayoutData(gridData);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        navigateComposite.setLayout(gridLayout);

        GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL,
                false, true);
        gridData_1.widthHint = BUTTONPARTWIDTH;
        buttonPart = new ButtonPart(navigateComposite, model);
        buttonPart.setLayoutData(gridData_1);

        breadcrumb = new Breadcrumb(navigateComposite, model);
    }

    private void initExploreComposite() {
        exploreComposite = new Composite(catalogComposite, SWT.FLAT);
        exploreComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
                true, true));
        exploreComposite.setLayout(new FillLayout());
        // TODO galleryItem显示text有省略号，改成分行显示
        // 单选&垂直滚动条
        gallery = new Gallery(exploreComposite, SWT.SINGLE | SWT.V_SCROLL);
        // 不显示group的标题
        NoGroupRenderer noGroupRenderer = new NoGroupRenderer();
        noGroupRenderer.setItemSize(64, 64);
//		DefaultGalleryItemRenderer itemRenderer = new DefaultGalleryItemRenderer();
//		// item被选择后的背景颜色
//		itemRenderer.setSelectionBackgroundColor(new Color(parent.getDisplay(), 180,225,255));
        gallery.setGroupRenderer(noGroupRenderer);
//		gallery.setItemRenderer(itemRenderer);
        catalogTreeViewer = new CatalogTreeViewer(gallery, model);
        catalogTreeViewer.setContentProvider(new CatalogContentProvider());
        catalogTreeViewer.setLabelProvider(new CatalogLabelProvider());
        catalogTreeViewer.setInput();
        catalogTreeViewer.addDoubleClickListener(event -> {
            catalogTreeViewer.open();
        });
    }

    @Override
    public void setFocus() {
    }

}
