package dts_project.views.propView;

import dts_project.property.CPropertySheetPage;
import dts_project.views.mainView.MainView;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;

public class PropView extends ViewPart {
	public static final String ID = "dts_project.views.propView";

	public PropView() {
	}

	@Override
	public void createPartControl(Composite parent) {
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
