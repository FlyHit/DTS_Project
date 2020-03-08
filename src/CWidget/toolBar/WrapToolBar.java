package CWidget.toolBar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class WrapToolBar extends Composite {
    private ToolBar toolBar;
    private Composite composite;
    private Composite composite1;

    public WrapToolBar(Composite parent, int style) {
        super(parent, style);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        setLayout(gridLayout);

        composite = new Composite(this, SWT.None);
//		composite.setLayout(new FillLayout());
        GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
        composite.setLayoutData(gridData);
        toolBar = new ToolBar(composite, SWT.FLAT);
        toolBar.setLocation(0, 0);
        toolBar.setSize(200, 100);
        new ToolItem(toolBar, SWT.DROP_DOWN).setText("Hello1");
        new ToolItem(toolBar, SWT.DROP_DOWN).setText("Hello2");
        new ToolItem(toolBar, SWT.DROP_DOWN).setText("Hello3");

        composite1 = new Composite(this, SWT.FLAT);
        GridData gridData1 = new GridData(GridData.FILL, GridData.FILL, true, true);
        gridData1.minimumWidth = 100;
        composite1.setLayoutData(gridData1);
        composite1.setBackground(new Color(getDisplay(), 0, 0, 0));
    }
}
