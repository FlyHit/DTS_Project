package CWidget.explorer.addressBar;

import CWidget.breadcrumb.Breadcrumb;
import CWidget.breadcrumb.BreadcrumbItem;
import CWidget.explorer.contentPane.IContentTreeModel;
import CWidget.explorer.contentPane.Node;
import CWidget.explorer.contentPane.RootNodeObserver;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class AddressBar extends Composite implements RootNodeObserver {
    private IBreadcrumbController controller;
    private Breadcrumb breadcrumb;
    private Text addressText;
    private IContentTreeModel model;
    private GridData bcGridData;
    private GridData textGridData;
    private boolean isFavorite;

    public AddressBar(Composite parent, IContentTreeModel model) {
        super(parent, SWT.FLAT);
        this.model = model;

        GridLayout gridLayout_1 = new GridLayout(2, false);
        gridLayout_1.marginWidth = 0;
        gridLayout_1.marginHeight = 0;
        setLayout(gridLayout_1);

        breadcrumb = new Breadcrumb(this, SWT.FLAT | SWT.CENTER);
        bcGridData = new GridData(GridData.FILL, GridData.FILL, true, true);
        breadcrumb.setLayoutData(bcGridData);
        breadcrumb.setBlankMouseUpListener(this::handleMouseUpEvent);
        breadcrumb.setShowMenuListener(this::handleMouseDownEvent);

        addressText = new Text(this, SWT.FLAT);
        textGridData = new GridData(GridData.FILL, GridData.FILL, true, true);
        addressText.setLayoutData(textGridData);
        addressText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                AddressBar.this.focusGained();
            }

            @Override
            public void focusLost(FocusEvent e) {
                showAddressText(false);
                showBreadcrumb(true);
                layout();
            }
        });
        showAddressText(false);
        addAll();
        model.registerRootNodeObserver(this);
    }

    private void focusGained() {
        StringBuilder address = new StringBuilder();
        for (BreadcrumbItem item : breadcrumb.getItems()) {
            address.append(item.getText()).append("\\");
        }
        // 减掉末尾的'\'
        address.deleteCharAt(address.length() - 1);
        addressText.setText(address.toString());
        addressText.selectAll();
    }

    // TODO 显示text时，text的高度和旁边的button一样，整个变矮了
    private void handleMouseUpEvent(Event event) {
        showBreadcrumb(false);
        showAddressText(true);
        addressText.setFocus();
        // TODO 替换为requestLayout()
        layout();
    }

    private void handleMouseDownEvent(Event event) {
        Menu menu = breadcrumb.getMenu();
        for (MenuItem menuItem : menu.getItems()) {
            menuItem.dispose();
        }

        for (BreadcrumbItem item : breadcrumb.getItems()) {
            // x<0表示隐藏的item
            if (item.getBounds().x < 0) {
                MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
                menuItem.setText(item.getText());
                Node node = (Node) item.getData("node");
                // TODO 菜单的图片大小一般是16px，而其他地方不是这尺寸，需要进行缩放
                ImageDescriptor imageDescriptor = node.getImageDescriptor();
                if (!imageDescriptor.equals(ImageDescriptor.getMissingImageDescriptor())) {
                    menuItem.setImage(imageDescriptor.createImage());
                }

                menuItem.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        model.setRoots(node);
                    }
                });
                continue;
            }

            break;
        }
    }

    /**
     * 显示addressText
     *
     * @param isShow 若为true，则显示；反之，不显示
     */
    private void showAddressText(boolean isShow) {
        textGridData.exclude = !isShow;
        addressText.setVisible(isShow);
    }

    /**
     * 显示breadcrumb
     *
     * @param isShow 若为true，则显示；反之，不显示
     */
    private void showBreadcrumb(boolean isShow) {
        bcGridData.exclude = !isShow;
        breadcrumb.setVisible(isShow);
    }

    public void setController(IBreadcrumbController controller) {
        this.controller = controller;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    private void add(Node node) {
        BreadcrumbItem item = new BreadcrumbItem(breadcrumb, SWT.PUSH);
        item.setText(node.getName());
        item.setData("node", node);
        item.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                model.setRoots(node);
            }
        });

        Menu menu = item.getMenu();
        for (MenuItem menuItem : menu.getItems()) {
            menuItem.dispose();
        }
        for (Node child : model.getChildren(node)) {
            MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
            menuItem.setText(child.getName());
            // TODO node设置imageDescriptor，这里菜单没有图标显示
            ImageDescriptor imageDescriptor = child.getImageDescriptor();
            if (!imageDescriptor.equals(ImageDescriptor.getMissingImageDescriptor())) {
                menuItem.setImage(imageDescriptor.createImage());
            }

            menuItem.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    model.setRoots(child);
                }
            });
        }
    }

    private void addAll() {
        removeAllItem();

        for (Node root : model.getRoots()) {
            add(root);
        }

        breadcrumb.pack(true);
        // 重新layout，刷新breadcrumb的clientArea以正确计算beginIndex
        requestLayout();
    }

    private void removeAllItem() {
        for (BreadcrumbItem item : breadcrumb.getItems()) {
            breadcrumb.removeItem(item);
        }
    }

    @Override
    public void updateState() {
        if (isFavorite) {

        } else {

        }

        addAll();
    }
}
