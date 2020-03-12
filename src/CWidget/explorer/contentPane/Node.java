package CWidget.explorer.contentPane;

import org.eclipse.jface.resource.ImageDescriptor;

public abstract class Node {
    private String name;
    private ImageDescriptor imageDescriptor;
    private Object data;

    public Node(Object data) {
        this.data = data;
        setNodeName();
        setNodeImageDescriptor();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageDescriptor getImageDescriptor() {
        return imageDescriptor;
    }

    public void setImageDescriptor(ImageDescriptor imageDescriptor) {
        this.imageDescriptor = imageDescriptor;
    }

    /**
     * 利用data设置节点名
     */
    protected abstract void setNodeName();

    /**
     * 利用data设置节点的imageDescriptor
     */
    protected abstract void setNodeImageDescriptor();
}
