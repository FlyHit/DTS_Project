package dts_project.property;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import java.util.List;

public class PropertySource implements IPropertySource {
    private List<Property> properties;

    public PropertySource(List<Property> properties) {
        this.properties = properties;
    }

    @Override
    public Object getEditableValue() {
        return null;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[properties.size()];
        for (int i = 0; i < properties.size(); i++) {
            propertyDescriptors[i] = new PropertyDescriptorFactory().
                    getPropertyDescriptor(properties.get(i));
        }

        return propertyDescriptors;
//        return null;
    }

    @Override
    public Object getPropertyValue(Object id) {
        String propId = (String) id;

        for (Property property : properties) {
            if (property.getId().equals(propId)) {
                return property.getValue();
            }
        }

        return null;
    }

    // 用于观察属性默认值是否改变。如果属性是否是默认值不重要就全部返回false
    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    @Override
    public void resetPropertyValue(Object id) {
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        String propId = (String) id;

        for (Property property : properties) {
            if (property.getId().equals(propId)) {
                // TODO 值是要有不同的类型还是从字符串转换成不同的类型
                property.setValue((String) value);
            }
        }
    }
}
