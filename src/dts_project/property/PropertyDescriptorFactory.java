package dts_project.property;

import dts_project.property.propertyDescriptor.textPropertyDescriptor.CTextPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 *
 */
public class PropertyDescriptorFactory {
    public PropertyDescriptor getPropertyDescriptor(Property property) {
        return new CTextPropertyDescriptor(property);
    }
}
