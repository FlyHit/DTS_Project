package CWidget.explorer.searchBar;

import CWidget.explorer.IImageKeys;
import dts_project.Application;
import org.eclipse.jface.resource.ResourceLocator;
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

    public SearchBox(Composite parent, int style) {
        super(parent, style);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        setLayout(gridLayout);

        searchText = new Text(this, SWT.SINGLE);
        searchText.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
                true, true));

        final Image[] buttonImg = {null};
        ResourceLocator.imageDescriptorFromBundle(Application.PLUGIN_ID, IImageKeys.BACK)
                .ifPresent(imageDescriptor -> buttonImg[0] = imageDescriptor.createImage());
        searchButton = new Label(this, SWT.NONE);
        searchButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
                false, true));
        searchButton.setImage(buttonImg[0]);
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                // TODO 搜索
            }
        });
    }
}
