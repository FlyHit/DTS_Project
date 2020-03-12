package CWidget.explorer;

import CWidget.explorer.addressBar.AddressBar;
import CWidget.explorer.addressBar.IAddressBarController;
import CWidget.explorer.contentPane.IContentTreeModel;
import CWidget.explorer.contentPane.Node;

public class AddressBarController implements IAddressBarController {
    private IContentTreeModel model;
    private AddressBar addressBar;

    public AddressBarController(AddressBar addressBar, IContentTreeModel model) {
        this.model = model;
        this.addressBar = addressBar;
    }

    @Override
    public boolean handleInput(String address) {
        String realAddress = "C:\\Program Files\\eclipse rcp\\eclipse201912\\" + address;
        boolean isValid = model.setRoots(
                new Node(getNameByAddress(realAddress), realAddress));
        // TODO 地址不合法，可出现一些提示信息
        addressBar.forceFocus();
        return isValid;
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
