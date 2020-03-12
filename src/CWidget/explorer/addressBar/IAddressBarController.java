package CWidget.explorer.addressBar;

import CWidget.explorer.contentPane.Node;

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
}
