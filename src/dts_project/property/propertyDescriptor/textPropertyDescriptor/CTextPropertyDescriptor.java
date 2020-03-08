package dts_project.property.propertyDescriptor.textPropertyDescriptor;

import dts_project.property.Property;
import dts_project.property.propertyDescriptor.ValidatorFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertySheetEntry;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class CTextPropertyDescriptor extends TextPropertyDescriptor {
    public static Boolean hasCategory = true;
    private String unit;  // 值的单位

    public CTextPropertyDescriptor(Property property) {
        // TODO id设为什么比较好
        super(property.getId(), property.getName());
        String type = property.getType();
        this.unit = new UnitFactory().getUnit(type);
        setDescription(property.getValue() + unit);

        if (!property.isSimple()) {
            setFilterFlags(new String[]{IPropertySheetEntry.FILTER_ID_EXPERT});
        }

        setValidator(new ValidatorFactory().getValidator("Unit", unit));

        if (hasCategory) {
            setCategory(property.getCategory());
        } else {
            setCategory(null);
        }
    }

    @Override
    public CellEditor createPropertyEditor(Composite parent) {
        CellEditor editor = new CTextCellEditor(parent, unit);
        if (getValidator() != null) {
            editor.setValidator(getValidator());
        }
        return editor;
    }
}