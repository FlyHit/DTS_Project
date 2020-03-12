package dts_project.views.catalogExplorerView;

import CWidget.explorer.contentPane.Node;
import dts_project.Application;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import java.io.File;

public class CatalogLabelProvider extends LabelProvider {
    private ImageDescriptor fileImage;
    private ImageDescriptor folderImage;

    public CatalogLabelProvider() {
        fileImage = ResourceLocator.imageDescriptorFromBundle(Application.PLUGIN_ID, CatalogImageKeys.FILE)
                .orElse(ImageDescriptor.getMissingImageDescriptor());
        folderImage = ResourceLocator.imageDescriptorFromBundle(Application.PLUGIN_ID, CatalogImageKeys.FOLDER)
                .orElse(ImageDescriptor.getMissingImageDescriptor());
    }

    @Override
    public Image getImage(Object element) {
        if (element instanceof Node) {
            Node node = (Node) element;
            File file = new File((String) node.getData());

            ImageDescriptor imageDescriptor = (file.isDirectory()) ? folderImage : fileImage;
            node.setImageDescriptor(imageDescriptor);

            // return the image being created by the resourceManager
            return imageDescriptor.createImage();
        }

        return super.getImage(element);
//		return null;
    }

    @Override
    public void dispose() {
        super.dispose();
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
