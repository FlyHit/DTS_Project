/*******************************************************************************
 * Copyright (c) 2012 Laurent CARON.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Laurent CARON (laurent.caron at gmail dot com) - initial API and implementation
 *******************************************************************************/
package CWidget.breadcrumb;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Instances of this class support the layout of selectable bar items displayed
 * in a bread crumb.
 * <p>
 * The item children that may be added to instances of this class must be of
 * type <code>BreadcrumbItem</code>.
 * </p>
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 */
public class Breadcrumb extends Canvas {

    private static final String IS_BUTTON_PRESSED = Breadcrumb.class.toString() + "_pressed";
    private final List<BreadcrumbItem> items;
    private static Color START_GRADIENT_COLOR = SWTGraphicUtil.getColorSafely(255, 255, 255);
    private static Color END_GRADIENT_COLOR = SWTGraphicUtil.getColorSafely(255, 255, 255);
    static Color BORDER_COLOR = SWTGraphicUtil.getColorSafely(128, 128, 128);
    static Color BORDER_COLOR_1 = SWTGraphicUtil.getColorSafely(212, 212, 212);
    static Color BORDER_COLOR_2 = SWTGraphicUtil.getColorSafely(229, 229, 229);
    static Color BORDER_COLOR_3 = SWTGraphicUtil.getColorSafely(243, 243, 243);
    boolean hasBorder = false;
    private int indexOfLastItem = -1; // 鼠标移动时上次经过的breadcrumbItem序号
    // 开始在breadcrumb上显示的item的序号，之前的item不显示
    private int beginIndex = 0;
    // 双箭头区域的宽度，点击此处会显示menu
    private final int DOUBLE_ARROW_WIDTH = 22;
    private boolean hasDrawArrow;
    // 调整大小时，不显示的item会列在menu上
    private Menu menu;
    private Listener blankMouseUpListener;
    private Listener showMenuListener;

    /**
     * Constructs a new instance of this class given its parent and a style value
     * describing its behavior and appearance.
     * <p>
     * The style value is either one of the style constants defined in class
     * <code>SWT</code> which is applicable to instances of this class, or must be
     * built by <em>bitwise OR</em>'ing together (that is, using the
     * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
     * constants. The class description lists the style constants that are
     * applicable to the class. Style bits are also inherited from superclasses.
     * </p>
     *
     * @param parent a composite control which will be the parent of the new
     *               instance (cannot be null)
     * @param style  the style of control to construct
     * @throws IllegalArgumentException <ul>
     *                                  <li>ERROR_NULL_ARGUMENT - if the parent is
     *                                  null</li>
     *                                  </ul>
     * @throws SWTException             <ul>
     *                                  <li>ERROR_THREAD_INVALID_ACCESS - if not
     *                                  called from the thread that created the
     *                                  parent</li>
     *                                  <li>ERROR_INVALID_SUBCLASS - if this class
     *                                  is not an allowed subclass</li>
     *                                  </ul>
     * @see Widget#getStyle()
     */
    public Breadcrumb(final Composite parent, final int style) {
        super(parent, checkStyle(style) | SWT.DOUBLE_BUFFERED);
        items = new ArrayList<>();
        hasBorder = (style & SWT.BORDER) != 0;
        addListeners();

        menu = new Menu(getShell(), SWT.POP_UP);
    }

    // 计算调整大小后能够显示的item
    private int computeBeginIndex() {
        int _beginIndex = 0;
        int clientAreaWidth = getClientArea().width;
        int itemsWidth = 0;
        for (int i = getItemCount() - 1; i >= 0; i--) {
            itemsWidth += getItem(i).getWidth();
            itemsWidth += hasDrawArrow ? DOUBLE_ARROW_WIDTH : 0;
            if (itemsWidth > clientAreaWidth) {
                _beginIndex = i + 1;
                return _beginIndex;
            }
        }
        return _beginIndex;
    }

    private static int checkStyle(final int style) {
        if ((style & SWT.BORDER) != 0) {
            return style & ~SWT.BORDER;
        }
        return 0;
    }

    private void addListeners() {
        addMouseDownListener();
        addMouseUpListener();
        addMouseHoverListener();
        // 利用MouseMove和MouseExit事件，使鼠标在item上时，item改变背景色
        addMouseMoveListener();
        addMouseExitListener();
        addPaintListener(this::paintControl);
    }

    private void addMouseDownListener() {
        addListener(SWT.MouseDown, event -> {

            final BreadcrumbItem item = getItem(new Point(event.x, event.y));
            if (item == null) {
                // 点击双箭头出现菜单
                if (hasDrawArrow && shouldShowMenu(event.x, event.y)) {
                    showMenuListener.handleEvent(event);
                    showMenu();
                }
                return;
            }

            int relativeX = event.x;
            if (hasDrawArrow) {
                relativeX -= DOUBLE_ARROW_WIDTH;
            }
            // 将坐标x转换为相对于被点击item的坐标值
            for (int i = beginIndex; i < indexOf(item); i++) {
                relativeX -= getItem(i).getWidth();
            }
            // 传递鼠标的x坐标
            item.setMouseDownXCoordinate(relativeX);

            final boolean isToggle = (item.getStyle() & SWT.TOGGLE) != 0;
            final boolean isPush = (item.getStyle() & SWT.PUSH) != 0;
            if (isToggle || isPush) {
                item.setSelection(!item.getSelection());
                redraw();
                update();
            }
            item.setData(IS_BUTTON_PRESSED, "*");
        });
    }

    private boolean shouldShowMenu(int x, int y) {
        return x >= 0 && x <= DOUBLE_ARROW_WIDTH && y >= 0 && y <= getBounds().height;
    }

    private void showMenu() {
        Point size = getSize();
        Point location = toDisplay(0, size.y);
        menu.setLocation(location);
        menu.setVisible(true);
    }

    private void addMouseUpListener() {
        addListener(SWT.MouseUp, event -> {

            final BreadcrumbItem item = getItem(new Point(event.x, event.y));
            if (item == null) {
                // 只有点在非菜单非item区域才触发blank事件
                if (hasDrawArrow && shouldShowMenu(event.x, event.y)) {
                    return;
                }

                Optional.ofNullable(blankMouseUpListener)
                        .ifPresent(listener -> listener.handleEvent(event));
                return;
            }

            if (item.getData(IS_BUTTON_PRESSED) == null) {
                // The button was not pressed
                return;
            }
            item.setData(IS_BUTTON_PRESSED, null);

            if ((item.getStyle() & SWT.PUSH) != 0) {
                item.setSelection(false);
            }

            if ((item.getStyle() & (SWT.TOGGLE | SWT.PUSH)) != 0) {
                item.fireSelectionEvent();
                redraw();
                update();
            }
        });
    }

    private void addMouseHoverListener() {
        addListener(SWT.MouseHover, event -> {
            final BreadcrumbItem item = getItem(new Point(event.x, event.y));
            if (item == null) {
                return;
            }
            setToolTipText(item.getTooltipText() == null ? "" : item.getTooltipText());
        });
    }

    private void addMouseMoveListener() {
        addListener(SWT.MouseMove, event -> {
            final BreadcrumbItem item = getItem(new Point(event.x, event.y));

            // 鼠标在一个item内移动，不重绘
            if (item == null || indexOf(item) == indexOfLastItem) {
                return;
            }

            if (indexOfLastItem != -1) {
                BreadcrumbItem lastItem = getItem(indexOfLastItem);
                lastItem.setHover(false);
            }

            indexOfLastItem = indexOf(item);
            item.setHover(true);

            redraw();
            update();
        });
    }

    private void addMouseExitListener() {
        addListener(SWT.MouseExit, event -> {
            if (indexOfLastItem != -1) {
                BreadcrumbItem item = getItem(indexOfLastItem);
                item.setHover(false);
                indexOfLastItem = -1;
                redraw();
                update();
            }
        });
    }

    /**
     * Paint the component
     *
     * @param e event
     */
    private void paintControl(final PaintEvent e) {
        final GC gc = e.gc;
        gc.setAdvanced(true);
        gc.setAntialias(SWT.ON);

        final int width = getSize().x;
        final int height = getSize().y;

        drawBackground(gc, width, height);

        beginIndex = computeBeginIndex();
        // beginIndex不是0，说明有些item不显示，此时绘制双箭头
        if (beginIndex > 0) {
            gc.setBackground(BORDER_COLOR);
            gc.setForeground(BORDER_COLOR);
            gc.drawLine(6, height / 2, 10, height / 2 - 2);
            gc.drawLine(6, height / 2, 10, height / 2 + 2);
            gc.drawLine(12, height / 2, 16, height / 2 - 2);
            gc.drawLine(12, height / 2, 16, height / 2 + 2);
            hasDrawArrow = true;
        } else {
            hasDrawArrow = false;
        }

        int x = hasDrawArrow ? DOUBLE_ARROW_WIDTH : 0;
        // 只绘制显示区域能够显示的item
        for (int i = beginIndex; i < items.size(); i++) {
            final BreadcrumbItem item = items.get(i);
            item.setGc(gc).setToolbarHeight(height);
            item.drawButtonAtPosition(x);
            x += item.getWidth();
        }

        // 其余的item状态设为不可见
        for (int i = 0; i < beginIndex; i++) {
            final BreadcrumbItem item = items.get(i);
            item.setInvisible();
        }
    }

    private void drawBackground(final GC gc, final int width, final int height) {
        gc.setForeground(START_GRADIENT_COLOR);
        gc.setBackground(END_GRADIENT_COLOR);
        gc.fillGradientRectangle(0, 0, width, height, true);

        if (hasBorder) {
            gc.setForeground(BORDER_COLOR);
            gc.drawRectangle(0, 0, width - 1, height - 1);
        }
    }

    /**
     * Add an item to the toolbar
     *
     * @param item roundedToolItem to add
     */
    void addItem(final BreadcrumbItem item) {
        items.add(item);
    }

    /**
     * @see Composite#computeSize(int, int, boolean)
     */
    @Override
    public Point computeSize(final int wHint, final int hHint, final boolean changed) {
        int width = 0, height = 0;
        for (final BreadcrumbItem item : items) {
            width += item.getWidth();
            height = Math.max(height, item.getHeight());
        }
        return new Point(Math.max(width, wHint), Math.max(height, hHint));
    }

    /**
     * Returns the item at the given, zero-relative index in the receiver. Throws an
     * exception if the index is out of range.
     *
     * @param index the index of the item to return
     * @return the item at the given index
     * @throws IllegalArgumentException <ul>
     *                                  <li>ERROR_INVALID_RANGE - if the index is
     *                                  not between 0 and the number of elements in
     *                                  the list minus 1 (inclusive)</li>
     *                                  </ul>
     *                                  // * @throws SWTException
     *                                                                                                    <ul>
     *                                                                                                    <li>ERROR_WIDGET_DISPOSED - if the receiver
     *                                                                                                    has been disposed</li>
     *                                                                                                    <li>ERROR_THREAD_INVALID_ACCESS - if not
     *                                                                                                    called from the thread that created the
     *                                                                                                    receiver</li>
     *                                                                                                    </ul>
     */
    public BreadcrumbItem getItem(final int index) {
        checkWidget();
        if (index < 0 || index > items.size()) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        return items.get(index);
    }

    /**
     * Returns the item at the given point in the receiver or null if no such item
     * exists. The point is in the coordinate system of the receiver.
     *
     * @param point the point used to locate the item
     * @return the item at the given point
     * @throws IllegalArgumentException <ul>
     *                                  <li>ERROR_NULL_ARGUMENT - if the point is
     *                                  null</li> // *
     *                                  </ul>
     * @throws SWTException             <ul>
     *                                  <li>ERROR_WIDGET_DISPOSED - if the receiver
     *                                  has been disposed</li>
     *                                  <li>ERROR_THREAD_INVALID_ACCESS - if not
     *                                  called from the thread that created the
     *                                  receiver</li>
     *                                  </ul>
     */
    public BreadcrumbItem getItem(final Point point) {
        checkWidget();

        final BreadcrumbItem item = items.stream()//
                .filter(element -> element.getBounds().contains(point)) //
                .findFirst() //
                .orElse(null);
        return item;
    }

    /**
     * Returns the number of items contained in the receiver.
     *
     * @return the number of items
     * @throws SWTException <ul>
     *                      <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                      disposed</li>
     *                      <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                      thread that created the receiver</li>
     *                      </ul>
     */
    public int getItemCount() {
        checkWidget();
        return items.size();
    }

    /**
     * Returns an array of <code>BreadcrumbItem</code>s which are the items in the
     * receiver.
     * <p>
     * Note: This is not the actual structure used by the receiver to maintain its
     * list of items, so modifying the array will not affect the receiver.
     * </p>
     *
     * @return the items in the receiver
     * @throws SWTException <ul>
     *                      <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                      disposed</li>
     *                      <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                      thread that created the receiver</li>
     *                      </ul>
     */
    public BreadcrumbItem[] getItems() {
        checkWidget();
        return items.toArray(new BreadcrumbItem[items.size()]);
    }

    /**
     * Searches the receiver's list starting at the first item (index 0) until an
     * item is found that is equal to the argument, and returns the index of that
     * item. If no item is found, returns -1.
     *
     * @param item the search item
     * @return the index of the item
     * @throws IllegalArgumentException <ul>
     *                                  <li>ERROR_NULL_ARGUMENT - if the item is
     *                                  null</li>
     *                                  <li>ERROR_INVALID_ARGUMENT - if the item has
     *                                  been disposed</li>
     *                                  </ul>
     * @throws SWTException             <ul>
     *                                  <li>ERROR_WIDGET_DISPOSED - if the receiver
     *                                  has been disposed</li>
     *                                  <li>ERROR_THREAD_INVALID_ACCESS - if not
     *                                  called from the thread that created the
     *                                  receiver</li>
     *                                  </ul>
     */
    public int indexOf(final BreadcrumbItem item) {
        checkWidget();
        return items.indexOf(item);
    }

    /**
     * Remove an item to the toolbar
     *
     * @param item item to remove
     */
    public void removeItem(final BreadcrumbItem item) {
        items.remove(item);
    }

    /**
     * 获取菜单。该菜单用于显示因调整大小而不可见的item， 默认是显示这些item的名字，可获取菜单来自定义
     *
     * @return 菜单
     */
    @Override
    public Menu getMenu() {
        checkWidget();
        return menu;
    }

    /**
     * 设置监听发生于breadcrumb空白处的MouseUp事件的listener
     *
     * @param listener 监听发生于breadcrumb空白处的MouseUp事件的listener
     */
    public void setBlankMouseUpListener(Listener listener) {
        this.blankMouseUpListener = listener;
    }

    public void setShowMenuListener(Listener showMenuListener) {
        this.showMenuListener = showMenuListener;
    }
}
