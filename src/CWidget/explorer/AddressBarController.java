package CWidget.explorer;

import CWidget.explorer.addressBar.AddressBar;
import CWidget.explorer.addressBar.IAddressBarController;
import CWidget.explorer.contentPane.ContentPane;
import CWidget.explorer.contentPane.IContentTreeModel;
import CWidget.explorer.contentPane.Node;
import dts_project.Application;
import dts_project.views.catalogExplorerView.CatalogImageKeys;
import dts_project.views.catalogExplorerView.FileNode;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.swt.graphics.Image;

public class AddressBarController implements IAddressBarController {
	private IContentTreeModel model;
	private AddressBar addressBar;
	private Explorer explorer;
	private static ImageDescriptor fileMenuImage;
	private static ImageDescriptor folderMenuImage;
	private static ImageDescriptor fileImage;
	private static ImageDescriptor folderImage;

	static {
		fileImage = ResourceLocator.imageDescriptorFromBundle(Application.PLUGIN_ID, CatalogImageKeys.FILE)
				.orElse(ImageDescriptor.getMissingImageDescriptor());
		folderImage = ResourceLocator.imageDescriptorFromBundle(Application.PLUGIN_ID, CatalogImageKeys.FOLDER)
				.orElse(ImageDescriptor.getMissingImageDescriptor());
		fileMenuImage = ResourceLocator.imageDescriptorFromBundle(Application.PLUGIN_ID, ExplorerImageKeys.FILE_MENU)
				.orElse(ImageDescriptor.getMissingImageDescriptor());
		folderMenuImage = ResourceLocator.imageDescriptorFromBundle(Application.PLUGIN_ID, ExplorerImageKeys.FOLDER_MENU)
				.orElse(ImageDescriptor.getMissingImageDescriptor());
	}

	public AddressBarController(Explorer explorer, AddressBar addressBar, IContentTreeModel model) {
		this.model = model;
		this.addressBar = addressBar;
		this.explorer = explorer;
	}

	@Override
	public boolean handleInput(String address) {
		String realAddress = "C:\\Program Files\\eclipse rcp\\eclipse201912\\" + address;
		boolean isValid = model.setRoots(new FileNode(realAddress));
		// TODO 地址不合法，可出现一些提示信息
		addressBar.forceFocus();
		return isValid;
	}

	@Override
	public void jumpToCatalog(Node catalog) {
		model.setRoots(catalog);
		explorer.switchPage(ContentPane.PAGE.VIEWER);
	}

	@Override
	public Image getProperImage(ImageDescriptor imageDescriptor) {
		if (imageDescriptor.equals(fileImage)) {
			return fileMenuImage.createImage();
		} else if (imageDescriptor.equals(folderImage)) {
			return folderMenuImage.createImage();
		}

		return null;
	}

	/**
	 * 通过输入的地址获取节点名。
	 *
	 * @param address 输入的地址
	 * @return 节点名
	 */
	private String getNameByAddress(String address) {
		char[] chars = address.toCharArray();
		String name = "";

		int length = chars.length;
		// 去掉末尾的'\'
		int endIndex = (chars[length - 1] == '\\') ? length - 2 : length - 1;
		for (int i = endIndex; i > 0; i--) {
			if (chars[i] == '\\') {
				name = address.substring(i + 1, endIndex + 1);
				return name;
			}
		}

		return name;
	}
}
