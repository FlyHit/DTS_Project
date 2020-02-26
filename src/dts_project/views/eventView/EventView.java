package dts_project.views.eventView;

import dts_project.property.CPropertySheetPage;
import dts_project.views.mainView.MainView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;

public class EventView extends ViewPart {
    public static final String ID = "dts_project.views.eventView";
    private SashForm sashForm;
    private Composite propComposite;
    // 显示属性解释的面板
    private Composite helpComposite;
    private StyledText styledText;

    @Override
    public void createPartControl(Composite parent) {
        sashForm = new SashForm(parent, SWT.FLAT | SWT.VERTICAL | SWT.BORDER);
        propComposite = new Composite(sashForm, SWT.FLAT);
        propComposite.setLayout(new FillLayout());

        helpComposite = new Composite(sashForm, SWT.FLAT);
        helpComposite.setLayout(new FillLayout());
        styledText = new StyledText(helpComposite, SWT.FLAT | SWT.READ_ONLY);

        IPropertySheetPage propertySheetPage = new CPropertySheetPage(ID, styledText);
//		propertySheetPage.setActionBars();
        propertySheetPage.createControl(propComposite);

        sashForm.setWeights(new int[]{70, 30});
        ISelectionService selectionService = getSite().getWorkbenchWindow().getSelectionService();
        selectionService.addSelectionListener(MainView.ID, propertySheetPage);
    }

    @Override
    public void setFocus() {

    }
}
