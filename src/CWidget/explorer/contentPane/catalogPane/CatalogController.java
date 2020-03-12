package CWidget.explorer.contentPane.catalogPane;

import CWidget.explorer.contentPane.IContentTreeModel;
import dts_project.Application;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.GalleryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CatalogController implements ICatalogController {
    private CatalogPane catalogPane;
    private IContentTreeModel model;
    private ImageDescriptor starDescriptor;

    public CatalogController(CatalogPane catalogPane, IContentTreeModel model) {
        this.catalogPane = catalogPane;
        this.model = model;
        starDescriptor = ResourceLocator
                .imageDescriptorFromBundle(Application.PLUGIN_ID, "resources/icons/star_icon_16.png")
                .orElse(ImageDescriptor.getMissingImageDescriptor());
    }

    @Override
    public void open(GalleryItem galleryItem) {
        String itemName = galleryItem.getText();
        model.open(itemName);
    }

    @Override
    public List<IContributionItem> createMenuItem() {
        List<IContributionItem> contributionItems = new ArrayList<>();

        GalleryItem item = catalogPane.getSelectionItem();
        if (item != null) {
            if (item.getData(DefaultGalleryItemRenderer.OVERLAY_BOTTOM_RIGHT) != null) {
                contributionItems.add(new ActionContributionItem(new RemoveFromFavorite()));
            } else {
                contributionItems.add(new ActionContributionItem(new AddToFavorite()));
            }
        }

        contributionItems.add(new ActionContributionItem(new ViewAction()));
        return contributionItems;
    }

    class AddToFavorite extends Action {
        @Override
        public void run() {
            Optional.ofNullable(catalogPane.getSelectionItem()).ifPresent(item -> {
                model.addToFavorite(item.getText(0));
                item.setData(DefaultGalleryItemRenderer.OVERLAY_BOTTOM_RIGHT, starDescriptor.createImage());
                catalogPane.getGallery().redraw(item);
            });
        }

        AddToFavorite() {
            setText("添加到收藏");
            // TODO For action icons, the recommended format is a 16x16 pixel image in the
            // GIF format
            setImageDescriptor(starDescriptor);
        }
    }

    class RemoveFromFavorite extends Action {
        @Override
        public void run() {
            Optional.ofNullable(catalogPane.getSelectionItem()).ifPresent(item -> {
                model.removeFromFavorite(item.getText(0));
                item.setData(DefaultGalleryItemRenderer.OVERLAY_BOTTOM_RIGHT, null);
                catalogPane.getGallery().redraw(item);
            });
        }

        RemoveFromFavorite() {
            setText("从收藏列表中移除");
            setImageDescriptor(starDescriptor);
        }
    }

    class ViewAction extends Action {
        ViewAction() {
            setText("查看");
        }
    }
}
