package dts_project.property.propertyDescriptor;

import dts_project.property.Property;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class CTextPropertyDescriptor extends TextPropertyDescriptor {
    public CTextPropertyDescriptor(Property property) {
        // TODO id设为什么比较好
        super(property.getId(), property.getName());
        String type = property.getType();
        setValidator(new ValidatorFactory().getValidator(type));
        setDescription(property.getValue() + new UnitFactory().getUnit(type));
        setCategory(property.getCategory());
    }
}
