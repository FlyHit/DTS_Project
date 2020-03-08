package CWidget.searchBox;

import CWidget.IImageKeys;
import dts_project.Application;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SearchBox extends Composite {
    private ImageDescriptor searchImage;
    private ImageDescriptor cancelImage;
    // 搜索框是否含搜索文本的标识
    private boolean hasSearchContent;
    private Label searchButton;
    private Text searchText;
    private ISearchController controller;

    public SearchBox(Composite parent, int style) {
        super(parent, style);

        searchImage = ResourceLocator.imageDescriptorFromBundle(Application.PLUGIN_ID, IImageKeys.SEARCH)
                .orElse(ImageDescriptor.getMissingImageDescriptor());
        cancelImage = ResourceLocator.imageDescriptorFromBundle(Application.PLUGIN_ID, IImageKeys.CANCEL)
                .orElse(ImageDescriptor.getMissingImageDescriptor());
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        setLayout(gridLayout);

        searchText = new Text(this, SWT.SINGLE);
        searchText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        searchButton = new Label(this, SWT.NONE);
        searchButton.setImage(searchImage.createImage());
        searchButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, true));
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                if (hasSearchContent) {
                    searchText.setText("");
                }
            }
        });

        searchText.addModifyListener(e -> {
            // 输入搜索内容时，图标会发生变化
            String text = searchText.getText();

            // TODO 让text控件lost focus便捷的方法(让其他控件通过点击都能获取focus）
            if (text != null && text.length() > 0) {
                searchButton.setImage(cancelImage.createImage());
                hasSearchContent = true;
            } else {
                searchButton.setImage(searchImage.createImage());
            }

            if (controller != null) {
                controller.search(text);
            }
        });
    }

    public void setController(ISearchController controller) {
        this.controller = controller;
    }
}
