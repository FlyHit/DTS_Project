package CWidget.explorer.buttonPart;

import CWidget.explorer.ExplorerImageKeys;
import dts_project.Application;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * 地址栏旁边的按钮部分：返回按钮+收藏按钮
 */
public class ButtonPart extends Composite {
    private ImageDescriptor backImage;
    private ImageDescriptor favoriteImage;
    private IButtonPartController controller;
	private ToolBar toolBar;
	private ToolItem backButton;
	private ToolItem favoriteButton;
	private Color white;
    // 空白占位符，使buttonPart居中
    private Label blankLabel;

    public ButtonPart(Composite parent) {
		super(parent, SWT.FLAT);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        setLayout(gridLayout);
		white = new Color(getDisplay(), 255, 255, 255);
		setBackground(white);

        blankLabel = new Label(this, SWT.FLAT);
        blankLabel.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, true));

        backImage = ResourceLocator.imageDescriptorFromBundle(Application.PLUGIN_ID, ExplorerImageKeys.Back)
                .orElse(ImageDescriptor.getMissingImageDescriptor());
        favoriteImage = ResourceLocator.imageDescriptorFromBundle(Application.PLUGIN_ID, ExplorerImageKeys.FAVORITE)
                .orElse(ImageDescriptor.getMissingImageDescriptor());

		toolBar = new ToolBar(this, SWT.FLAT);
		toolBar.setBackground(white);

		backButton = new ToolItem(toolBar, SWT.FLAT);
        backButton.setImage(backImage.createImage());
		backButton.setEnabled(false);
		favoriteButton = new ToolItem(toolBar, SWT.DROP_DOWN);
        favoriteButton.setImage(favoriteImage.createImage());

		addDisposeListener(ButtonPart.this::widgetDisposed);
    }

    private void addListener() {
        backButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                controller.back();
                setFocus();
            }
        });

		favoriteButton.addListener(SWT.Selection, event -> {
			if (event.detail == SWT.ARROW) {
				controller.previewFavorite(favoriteButton);
			} else {
				controller.jumpToFavorite();
			}

            setFocus();
		});
	}

    /**
     * 设置backButton的状态
     *
     * @param isEnable 若为true，则使能；反之，禁用
     */
    public void setBackEnable(boolean isEnable) {
        backButton.setEnabled(isEnable);
	}

	private void widgetDisposed(DisposeEvent e) {
		white.dispose();
    }

    public void setController(IButtonPartController controller) {
        this.controller = controller;
        addListener();
	}
}
