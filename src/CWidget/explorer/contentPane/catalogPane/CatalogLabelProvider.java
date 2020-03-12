package CWidget.explorer.contentPane.catalogPane;

import CWidget.explorer.contentPane.Node;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class CatalogLabelProvider extends LabelProvider {

    @Override
    public Image getImage(Object element) {
        if (element instanceof Node) {
            Node node = (Node) element;
            return node.getImageDescriptor().createImage();
        }

        return super.getImage(element);
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
            text = node.getName();
        }

        return text;
    }
}
