package CWidget.explorer;

import CWidget.explorer.addressBar.breadcrumb.Breadcrumb;
import CWidget.explorer.buttonPart.ButtonPart;
import CWidget.explorer.contentPane.ContentPane;
import CWidget.explorer.contentPane.IContentTreeModel;
import CWidget.explorer.searchBar.SearchBox;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class Explorer extends Composite {
    private SearchBox searchBox;
    private Breadcrumb breadcrumb;
    private ButtonPart buttonPart;
    private ContentPane contentPane;
    private Color white;

    public Explorer(Composite parent, IContentTreeModel model) {
        super(parent, SWT.FLAT);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        setLayout(gridLayout);
        white = new Color(getDisplay(), 255, 255, 255);
        setBackground(white);

        searchBox = new SearchBox(this, model);
        searchBox.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

        Composite middleComposite = new Composite(this, SWT.FLAT | SWT.BORDER);
        middleComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        middleComposite.setBackground(white);
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 2;
        gridLayout1.marginHeight = 0;
        gridLayout1.marginWidth = 0;
        middleComposite.setLayout(gridLayout1);
        buttonPart = new ButtonPart(middleComposite, model);
        buttonPart.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, true));
        breadcrumb = new Breadcrumb(middleComposite, model);
        breadcrumb.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        contentPane = new ContentPane(this, model);
        contentPane.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        addDisposeListener(Explorer.this::widgetDisposed);
    }

    public void setContentProvider(ITreeContentProvider contentProvider) {
        contentPane.setContentProvider(contentProvider);
    }

    public void setLabelProvider(ILabelProvider labelProvider) {
        contentPane.setLabelProvider(labelProvider);
    }

    public void refresh() {
        contentPane.refresh();
    }

    public void setBackImage(Image image) {
        buttonPart.setBackImage(image);
    }

    public void setSearchImage(Image image) {
        searchBox.setSearchImage(image);
    }

    public void setFavoriteImage(Image image) {
        buttonPart.setFavoriteImage(image);
    }

    private void widgetDisposed(DisposeEvent e) {
        white.dispose();
    }
}
