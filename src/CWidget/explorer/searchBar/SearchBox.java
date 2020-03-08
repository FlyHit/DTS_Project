package CWidget.explorer.searchBar;

import CWidget.explorer.contentPane.IContentTreeModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SearchBox extends Composite {
    private Label searchButton;
    private Text searchText;
    private SearchController controller;
    private IContentTreeModel model;

    public SearchBox(Composite parent, IContentTreeModel model) {
        super(parent, SWT.FLAT);
        this.model = model;
        this.controller = new SearchController(model);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        setLayout(gridLayout);

        searchText = new Text(this, SWT.SINGLE);
        searchText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        searchButton = new Label(this, SWT.NONE);
        searchButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, true));
        searchButton.setText("🔍");
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                controller.search(searchText.getText());
            }
        });
    }

    /**
     * 设置搜索图标
     *
     * @param image 图片
     */
    public void setSearchImage(Image image) {
        searchButton.setText("");
        searchButton.setImage(image);
    }
}
