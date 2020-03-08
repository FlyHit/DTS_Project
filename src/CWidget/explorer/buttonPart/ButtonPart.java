package CWidget.explorer.buttonPart;

import CWidget.explorer.contentPane.IContentTreeModel;
import CWidget.explorer.contentPane.Node;
import CWidget.explorer.contentPane.RootNodeObserver;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * 地址栏旁边的按钮部分：返回按钮+收藏按钮
 */
public class ButtonPart extends Composite implements RootNodeObserver {
	private ButtonPartController controller;
	private ToolBar toolBar;
	private ToolItem backButton;
	private ToolItem favoriteButton;
	private Color white;
	private IContentTreeModel model;
	Node ROOTNODE;

	public ButtonPart(Composite parent, IContentTreeModel model) {
		super(parent, SWT.FLAT);
		this.model = model;
		this.ROOTNODE = model.getRootNode();
		setLayout(new FillLayout());
		white = new Color(getDisplay(), 255, 255, 255);
		setBackground(white);

		toolBar = new ToolBar(this, SWT.FLAT);
		toolBar.setBackground(white);

		backButton = new ToolItem(toolBar, SWT.FLAT);
		backButton.setText("◀");
		backButton.setEnabled(false);
		favoriteButton = new ToolItem(toolBar, SWT.DROP_DOWN);
		favoriteButton.setText("★");
		controller = new ButtonPartController(this, model);

		addListener();
		addDisposeListener(ButtonPart.this::widgetDisposed);

    public void setLayoutData(Object layoutData) {
        toolBar.setLayoutData(layoutData);
    }

    private void addListener() {
        backItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                controller.back();
            }
        });

		favoriteButton.addListener(SWT.Selection, event -> {
			if (event.detail == SWT.ARROW) {
				controller.previewFavorite(favoriteButton);
			} else {
				controller.jumpToFavorite();
			}
		});
	}

	public void setBackImage(Image image) {
		backButton.setImage(image);
	}

	public void setFavoriteImage(Image image) {
		favoriteButton.setImage(image);
	}

	private void widgetDisposed(DisposeEvent e) {
		white.dispose();
	}

	@Override
	public void updateState() {
		// 在最上级目录的时候，返回按钮变灰
		if (model.getRootNode().getData().equals(ROOTNODE.getData())) {
			backButton.setEnabled(false);
		} else {
			backButton.setEnabled(true);
		}
	}

    Composite getComposite() {
        return composite;
    }
}
