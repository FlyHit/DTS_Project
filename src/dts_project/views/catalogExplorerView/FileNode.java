package dts_project.views.catalogExplorerView;

import CWidget.explorer.contentPane.Node;
import dts_project.Application;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ResourceLocator;

import java.io.File;

public class FileNode extends Node {
    private static ImageDescriptor fileImage;
    private static ImageDescriptor folderImage;

    static {
        fileImage = ResourceLocator.imageDescriptorFromBundle(Application.PLUGIN_ID, CatalogImageKeys.FILE)
                .orElse(ImageDescriptor.getMissingImageDescriptor());
        folderImage = ResourceLocator.imageDescriptorFromBundle(Application.PLUGIN_ID, CatalogImageKeys.FOLDER)
                .orElse(ImageDescriptor.getMissingImageDescriptor());
    }

    public FileNode(Object data) {
        super(data);
    }

    @Override
    protected void setNodeName() {
        setName(getFileName((String) getData()));
    }

    @Override
    protected void setNodeImageDescriptor() {
        File file = new File((String) getData());
        ImageDescriptor imageDescriptor = (file.isDirectory()) ? folderImage : fileImage;
        setImageDescriptor(imageDescriptor);
    }

    private static String getFileName(String data) {
        String itemName = "";
        String path = data;
        int lastIndex = path.length() - 1;

        for (int i = lastIndex; i > 0; i--) {
            if (path.charAt(i) == '\\') {
                // subString的结果不包含endIndex处的字符
                itemName = path.substring(i + 1, lastIndex + 1);
                break;
            }
        }

        return itemName;
    }

}
