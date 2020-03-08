package CWidget.test.explorer.file;

import CWidget.explorer.contentPane.Node;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import java.io.File;
import java.net.URL;

public class CatalogLabelProvider extends LabelProvider {
    private ResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources());

    @Override
    public Image getImage(Object element) {
        if (element instanceof Node) {
            Node node = (Node) element;
            File file = new File((String) node.getData());

            URL url;
            if (file.isDirectory()) {
                url = CatalogLabelProvider.class.getResource("/icons/folder_icon_32.png");
            } else {
                url = CatalogLabelProvider.class.getResource("/icons/file_icon_32.png");
            }

            ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
            node.setImageDescriptor(imageDescriptor);

            // return the image being created by the resourceManager
            return resourceManager.createImage(imageDescriptor);
        }

        return super.getImage(element);
//		return null;
    }

    @Override
    public void dispose() {
        super.dispose();

        // dispose the ResourceManager yourself
        resourceManager.dispose();
    }

    @Override
    public String getText(Object element) {
        String text = "";

        if (element instanceof Node) {
            Node node = (Node) element;
            File file = new File((String) node.getData());
            text = file.getName();

            if (text.length() == 0) {
                String path = file.getPath();
                // 减去末尾的\，如C:\
                text = path.substring(0, path.length() - 1);
            }
        }

        return text;
    }
}
