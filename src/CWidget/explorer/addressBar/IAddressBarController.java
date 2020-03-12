package CWidget.explorer.addressBar;

import CWidget.explorer.contentPane.Node;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public interface IAddressBarController {
    /**
     * 处理输入的地址（跳转到地址）
     *
     * @param address 输入的地址
     */
    boolean handleInput(String address);

    /**
     * 跳转到目录
     */
    void jumpToCatalog(Node catalog);

    /**
     * 将节点的imageDescriptor转换为适合菜单显示的图片
     *
     * @param imageDescriptor 节点的imageDescriptor
     * @return 菜单项的图片
     */
    Image getProperImage(ImageDescriptor imageDescriptor);
}
