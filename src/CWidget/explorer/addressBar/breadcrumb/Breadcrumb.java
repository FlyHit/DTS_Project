package CWidget.explorer.addressBar.breadcrumb;

import CWidget.explorer.contentPane.IContentTreeModel;
import CWidget.explorer.contentPane.Node;
import CWidget.explorer.contentPane.RootNodeObserver;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * breadcrumb样式的导航栏，单击跳转，使用ComboBox预览下级目录
 */
public class Breadcrumb extends Composite implements RootNodeObserver {
	private IBreadcrumbController controller;
    private IContentTreeModel model;
	private ToolBar toolBar;
    private Text siteText;
    private GridData gridData;
    private Color white;

    public Breadcrumb(Composite parent, IContentTreeModel model) {
        super(parent, SWT.FLAT);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        setLayout(gridLayout);
        white = new Color(getDisplay(), 255, 255, 255);
        setBackground(white);
        addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                Breadcrumb.this.controlResized(e);
            }
        });

		this.model = model;
        model.registerRootNodeObserver(this);

        toolBar = new ToolBar(this, SWT.FLAT);
        toolBar.setBackground(white);
        gridData = new GridData();
        toolBar.setLayoutData(gridData);
        siteText = new Text(this, SWT.FLAT);
        GridData gridData1 = new GridData(GridData.FILL, GridData.FILL, true, true);
        siteText.setLayoutData(gridData1);
        siteText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Breadcrumb.this.textFocusGained(e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                Breadcrumb.this.textFocusLost(e);
            }
        });

        siteText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    Text text = (Text) e.widget;
                    controller.inputCatalog(text.getText());
                }
            }
        });

		controller = new BreadcrumbController(model, this);
		addAll();

        addDisposeListener(Breadcrumb.this::widgetDisposed);
    }

    private void add(Node node) {
        BreadcrumbItem item = new BreadcrumbItem(this);
        item.setText(node.getName());
        item.setNode(node);
        layout();
    }

    private void addAll() {
		removeAll();

        for (Node root : model.getRoots()) {
            add(root);
		}
	}

    private void removeAll() {
		for (ToolItem item : toolBar.getItems()) {
			item.dispose();
		}
	}

    public ToolBar getToolBar() {
		return toolBar;
	}

	IBreadcrumbController getController() {
		return controller;
	}

	@Override
    public void updateState() {
		addAll();
	}

    private void widgetDisposed(DisposeEvent e) {
        white.dispose();
    }

    // TODO 调整窗口时，breadcrumb的一些项目收起来不显示
    private void controlResized(ControlEvent e) {

    }

    private void textFocusGained(FocusEvent e) {
        Text text = (Text) e.widget;
        // TODO 这里不通用需要改
//        String path = (String) model.getRootNode();
//        text.setText(path);
        text.selectAll();
        gridData.exclude = true;
        toolBar.setVisible(false);
        layout();
    }

    private void textFocusLost(FocusEvent e) {
        Text text = (Text) e.widget;
        text.setText("");
        gridData.exclude = false;
        toolBar.setVisible(true);
        layout();
    }
}
