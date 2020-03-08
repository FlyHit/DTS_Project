package dts_project.views.propView;

import CWidget.searchBox.SearchBox;
import dts_project.property.CPropertySheetPage;
import dts_project.views.mainView.MainView;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;

public class PropView extends ViewPart {
	public static final String ID = "dts_project.views.propView";
	private SashForm sashForm;
	private Composite propComposite;
	// 显示属性解释的面板
	private Composite helpComposite;
	private StyledText styledText;
	private SearchBox searchBox;

	public PropView() {
	}

	@Override
	public void createPartControl(Composite parent) {
//		sashForm = new SashForm(parent, SWT.FLAT | SWT.VERTICAL | SWT.BORDER);
//		propComposite = new Composite(sashForm, SWT.FLAT);
//		GridLayout gridLayout = new GridLayout();
//		gridLayout.marginWidth = 0;
//		gridLayout.marginHeight = 0;
//		propComposite.setLayout(gridLayout);
//
//		searchBox = new SearchBox(propComposite, SWT.FLAT);
//		searchBox.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
//		IPropertySheetPage propertySheetPage = new CPropertySheetPage(ID);
//
//		propertySheetPage.createControl(propComposite);

//
//		helpComposite = new Composite(sashForm, SWT.FLAT);
//		helpComposite.setLayout(new FillLayout());
//		styledText = new StyledText(helpComposite, SWT.FLAT | SWT.READ_ONLY);
//
//		sashForm.setWeights(new int[] { 70, 30 });
		IPropertySheetPage propertySheetPage = new CPropertySheetPage(ID);
		propertySheetPage.createControl(parent);
		propertySheetPage.setActionBars(getViewSite().getActionBars());
		ISelectionService selectionService = getSite().getWorkbenchWindow().getSelectionService();
		selectionService.addSelectionListener(MainView.ID, propertySheetPage);
	}

	@Override
	public void setFocus() {

	}
}
