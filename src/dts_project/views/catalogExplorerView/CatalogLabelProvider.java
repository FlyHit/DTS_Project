package dts_project.views.catalogExplorerView;

import dts_project.Application;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import java.io.File;

public class CatalogLabelProvider implements ILabelProvider {
	@Override
	public Image getImage(Object element) {
		final Image[] image = { null };
		String imageKey;  // 图片路径

		if (element instanceof String) {
			File file = new File((String) element);

            imageKey = (file.isDirectory()) ? IImageKeys.FOLDER : IImageKeys.FILE;
            ResourceLocator.imageDescriptorFromBundle(Application.PLUGIN_ID, imageKey)
                    .ifPresent(imageDescriptor -> image[0] = imageDescriptor.createImage());
		}

		return image[0];
	}

	@Override
	public String getText(Object element) {
		String text = "";

        if (element instanceof String){
            File file = new File((String) element);
            text = file.getName();

            if (text.length() == 0) {
                String path = file.getPath();
				// 减去末尾的\，如C:\
                text = path.substring(0, path.length() - 1);
            }
        }

        return text;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}
}
