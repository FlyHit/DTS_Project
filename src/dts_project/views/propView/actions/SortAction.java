package dts_project.views.propView.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.views.properties.IPropertySheetPage;

import java.beans.IntrospectionException;
import java.beans.Introspector;

public class SortAction extends Action {
    private IPropertySheetPage propertySheetPage;

    protected SortAction() throws IntrospectionException {
        super();
        Introspector.getBeanInfo(this.getClass());
    }

    @Override
    public void run() {
    }
}
