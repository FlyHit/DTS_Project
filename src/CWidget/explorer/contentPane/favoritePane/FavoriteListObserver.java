package CWidget.explorer.contentPane.favoritePane;

import CWidget.explorer.contentPane.Node;

public interface FavoriteListObserver {
    void updateState(boolean isAdd, Node node);
}
