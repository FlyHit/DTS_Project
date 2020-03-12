package CWidget.explorer.buttonPart;

import org.eclipse.swt.widgets.ToolItem;

public interface IButtonPartController {
    void back();

    void previewFavorite(ToolItem item);

    void jumpToFavorite();
}
