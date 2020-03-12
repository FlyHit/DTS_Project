package dts_project.views.catalogExplorerView;

import CWidget.explorer.contentPane.ContentTreeModel;
import CWidget.explorer.contentPane.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileTreeModel extends ContentTreeModel {

    public FileTreeModel(Node rootNode) {
        super(rootNode);
    }

    @Override
    protected boolean setRootNode(Node rootNode) {
        if (rootNode.getData() instanceof String) {
            File file = new File((String) rootNode.getData());
            if (file.exists() && file.isDirectory()) {
                this.rootNode = rootNode;

                return true;
            }
            // Todo 抛出异常会不会更好
            System.out.println("非有效文件根路径");
        } else {
            System.out.println("设置失败，rootNode必须是String类型！");
        }

        return false;
    }

    @Override
    public Node[] getChildren(Node parentElement) {
        List<Node> childList = new ArrayList<>();
        File file = new File((String) parentElement.getData());

        for (File f : Objects.requireNonNull(file.listFiles())) {
            Node node = new FileNode(f.getPath());
            childList.add(node);
        }

        Node[] nodes = new Node[childList.size()];
        for (int i = 0; i < childList.size(); i++) {
            nodes[i] = childList.get(i);
        }

        return nodes;
    }

    @Override
    public Node getParent(Node element) {
        // 最底层目录没有parent
        if (!element.getData().equals(ROOT_NODE.getData()) && isFilePath(element.getData())) {
            String parent = new File((String) element.getData()).getParent();
            Node node = new FileNode(parent);
            return node;
        }

        return null;
    }

    private static boolean isFilePath(Object element) {
        if (element instanceof String) {
            String path = (String) element;
            File file = new File(path);
            return file.exists();
        }

        return false;
    }

    @Override
    protected Node convertInput(String input) {
        String pathname = input;

        if (pathname.endsWith("\\")) {
            pathname = pathname.substring(0, pathname.length() - 1);
        }

        return new FileNode(pathname);
    }
}
