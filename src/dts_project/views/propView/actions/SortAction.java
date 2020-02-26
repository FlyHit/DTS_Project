package dts_project.views.propView.actions;

import dts_project.property.propertyDescriptor.CTextPropertyDescriptor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ResourceLocator;

/**
 * 按钮动作：以字典顺序排序
 */
public class SortAction extends Action {
    public final static String ID = "dt_project.views.propView.actions.sortAction";

    public SortAction() {
        setId(ID);
        setToolTipText("排序");
        setImageDescriptor(ResourceLocator.imageDescriptorFromBundle("dts_project",
                ActionImageKeys.SORT).orElse(null));
    }

    @Override

    public void run() {
        CTextPropertyDescriptor.hasCategory = !CTextPropertyDescriptor.hasCategory;
    }
}
