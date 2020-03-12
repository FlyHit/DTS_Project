package CWidget.explorer.contentPane;

import CWidget.explorer.contentPane.favoritePane.FavoriteListObserver;

import java.util.List;

/**
 * 所有显示在Explorer上的model必须实现该接口
 */
public interface IContentTreeModel {
    Node[] getChildren(Node parentElement);

    Node getParent(Node element);

    boolean hasChildren(Node element);

    Node getRootNode();

    /**
     * @return 父节点列表（父节点，父父节点...)
     */
    List<Node> getRoots();

    void setRoots(Node rootNode);

    void registerRootNodeObserver(RootNodeObserver observer);

    void registerFavoriteObserver(FavoriteListObserver observer);

    void open(String itemName);

    /**
     * 返回上级目录
     *
     * @param isFavorite 当前的目录是否是收藏目录
     */
    void back(boolean isFavorite);

    /**
     * 处理输入的地址
     *
     * @param input 输入的地址
     */
    void handleInput(String input);

    /**
     * 添加到收藏
     *
     * @param itemName 添加项目的名称
     */
    void addToFavorite(String itemName);

    /**
     * 移除收藏的项目
     *
     * @param itemName 收藏项目的名称
     */
    void removeFromFavorite(String itemName);

    List<Node> getFavoriteList();
}
