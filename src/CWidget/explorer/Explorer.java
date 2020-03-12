package CWidget.explorer;

import CWidget.explorer.addressBar.AddressBar;
import CWidget.explorer.buttonPart.ButtonPart;
import CWidget.explorer.contentPane.ContentPane;
import CWidget.explorer.contentPane.IContentTreeModel;
import CWidget.explorer.contentPane.Node;
import CWidget.explorer.contentPane.RootNodeObserver;
import CWidget.searchBox.SearchBox;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class Explorer extends Composite implements RootNodeObserver {
	private SearchBox searchBox;
	private ButtonPart buttonPart;
	private ContentPane contentPane;
	private Color white;
	private IContentTreeModel model;
	private Node ROOT_NODE;

	public Explorer(Composite parent, IContentTreeModel model) {
		super(parent, SWT.FLAT);
		this.model = model;
		this.ROOT_NODE = model.getRootNode();
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		white = new Color(getDisplay(), 255, 255, 255);
		setBackground(white);

		searchBox = new SearchBox(this, SWT.FLAT);
		searchBox.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		Composite middleComposite = new Composite(this, SWT.FLAT | SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		middleComposite.setLayoutData(gridData);
		middleComposite.setBackground(white);
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		gridLayout1.marginHeight = 0;
		gridLayout1.marginWidth = 0;
		middleComposite.setLayout(gridLayout1);
		buttonPart = new ButtonPart(middleComposite);
		buttonPart.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, true));
		buttonPart.setController(new ButtonPartController(this, model));
		new AddressBar(middleComposite, model).setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		contentPane = new ContentPane(this, model);
		contentPane.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		addDisposeListener(Explorer.this::widgetDisposed);

		model.registerRootNodeObserver(this);
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

	private void widgetDisposed(DisposeEvent e) {
		white.dispose();
	}

	@Override
	public void updateState() {

		// 在最上级目录的时候，返回按钮变灰
		if (model.getRootNode().getData().equals(ROOT_NODE.getData())) {
			setBackEnable(false);
		} else {
			setBackEnable(true);
		}
	}

	/**
	 * 设置backButton的状态
	 *
	 * @param isEnable 若为true，则使能；反之，禁用
	 */
	void setBackEnable(boolean isEnable) {
		buttonPart.setBackEnable(isEnable);
	}

	void switchPage(ContentPane.PAGE page) {
		contentPane.switchPage(page);
	}
}
