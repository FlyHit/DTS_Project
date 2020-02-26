package dts_project.property;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import java.util.List;

public class PropertySourceAdapterFactory implements IAdapterFactory {
    @Override
    public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
        if (adapterType == IPropertySource.class)
            return (T) new PropertySource((List<Property>) adaptableObject);
        return null;
    }

    @Override
    public Class<?>[] getAdapterList() {
        return new Class[]{IPropertySource.class};
    }
}
