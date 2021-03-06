package dts_project.renderer;

import dts_project.ribbon.RibbonComposite;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.e4.ui.workbench.renderers.swt.PerspectiveRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

import java.io.IOException;

@SuppressWarnings("restriction")
public class RibbonPerspectiveRenderer extends PerspectiveRenderer {

	@SuppressWarnings("deprecation")
	@Override
	public Widget createWidget(MUIElement element, Object parent) {
		if (!(element instanceof MPerspective) || !(parent instanceof Composite))
			return null;

		Composite com = (Composite) parent;

		ViewForm viewForm = new ViewForm(com, SWT.FLAT);

		((StackLayout) com.getLayout()).topControl = viewForm;

		Composite topComposite = new Composite(viewForm, SWT.FLAT);
		FillLayout fillLayout = new FillLayout();

		topComposite.setLayout(fillLayout);
		RibbonComposite ribbon = null;
		try {
			ribbon = new RibbonComposite(topComposite, SWT.FLAT | SWT.NO_TRIM);
		} catch (IOException e) {
			e.printStackTrace();
		}
		viewForm.setTopLeft(topComposite);

		IStylingEngine stylingEngine = (IStylingEngine) getContext(element).get(IStylingEngine.SERVICE_NAME);
		stylingEngine.setClassname(ribbon, "ribbonComposite");

		Composite perspParent = new Composite(viewForm, SWT.FLAT);
		viewForm.setContent(perspParent);
		perspParent.setLayout(new StackLayout());

		Composite perspArea = new Composite(perspParent, SWT.FLAT);
		perspArea.setLayout(new FillLayout());
		stylingEngine.setClassname(perspArea, "perspectiveLayout"); //$NON-NLS-1$
		return perspArea;
	}

}
