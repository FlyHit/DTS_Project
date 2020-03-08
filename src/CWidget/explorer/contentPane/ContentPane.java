package CWidget.explorer.contentPane;

import CWidget.explorer.contentPane.catalogPane.CatalogPane;
import CWidget.explorer.contentPane.favoritePane.FavoritePane;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.PageBook;

public class ContentPane extends Composite {
    private PageBook pageBook;
    private CatalogPane catalogPane;
    private FavoritePane favoritePane;
    private Composite searchComposite;
    private IContentTreeModel model;

    public ContentPane(Composite parent, IContentTreeModel model) {
        super(parent, SWT.FLAT);
        this.model = model;
        setData("name", "contentPane");
        setLayout(new FillLayout());

        pageBook = new PageBook(this, SWT.FLAT);
        catalogPane = new CatalogPane(pageBook, model);
        favoritePane = new FavoritePane(pageBook, model);
        searchComposite = new Composite(pageBook, SWT.FLAT);
        pageBook.showPage(catalogPane);
    }

    /**
     * 切换当前显示的页面
     *
     * @param page 需要显示的页面
     */
    public void switchPage(PAGE page) {
        Control control = null;
        switch (page) {
            case FAVORITE:
                control = favoritePane;
                break;
            case VIEWER:
                control = catalogPane;
                break;
            case SEARCH:
                control = searchComposite;
                break;
        }

        pageBook.showPage(control);
    }

    public FavoritePane getFavoritePane() {
        return favoritePane;
    }

    public enum PAGE {
        FAVORITE, VIEWER, SEARCH
    }

    public void setContentProvider(ITreeContentProvider contentProvider) {
        catalogPane.setContentProvider(contentProvider);
    }

    public void setLabelProvider(ILabelProvider labelProvider) {
        catalogPane.setLabelProvider(labelProvider);
    }

    public void refresh() {
        catalogPane.updateState();
    }
}
