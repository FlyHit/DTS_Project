package CWidget.explorer.addressBar;

public interface IAddressBarController {
    /**
     * 处理输入的地址（跳转到地址）
     *
     * @param address 输入的地址
     */
    boolean handleInput(String address);
}
