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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Instances of this class represent a selectable user interface object that
 * represents an item of a breadcrumb.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>TOGGLE,PUSH,NONE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 */
public class BreadcrumbItem extends Item {
    // 箭头部分所占的宽度
    private static final int ARROW_WIDTH = 20;
    private static final int MIN_WIDTH = 40;
    private static final int MARGIN_WIDTH = 2;
    private static final int MARGIN_HEIGHT = 4;
    private static Color SELECTED_COLOR = SWTGraphicUtil.getColorSafely(0, 102, 204);
    private static Color HOVER_LINE_COLOR = SWTGraphicUtil.getColorSafely(200, 235, 255);
    private static Color HOVER_COLOR = SWTGraphicUtil.getColorSafely(215, 235, 255);

    private final Breadcrumb parentBreadcrumb;
    private final List<SelectionListener> selectionListeners;
    private Rectangle bounds;
    private boolean enabled;
    private boolean selection;
    private boolean hover;
    private int width;
    private int height;
    private Image disabledImage;
    private Image selectionImage;
    private int alignment;
    private Color textColorSelected;
    private Color textColor;
    private String tooltipText;
    private GC gc;
    private int toolbarHeight;
    // 点击箭头出现的菜单
    private Menu menu;
    private int xCoordinate;
    private boolean isMouseDown;
    // 当item由于breadcrumb调整大小而消失时，将bounds中的x设为INVISIBLE
    private final int INVISIBLE = -10000;

    /**
     * Constructs a new instance of this class given its parent (which must be a
     * <code>Breadcrumb</code>) and a style value describing its behavior and
     * appearance. The item is added to the end of the items maintained by its
     * parent.
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
     * @throws IllegalArgumentException <ul>
     *                                                                   <li>ERROR_NULL_ARGUMENT - if the parent is
     *                                                                   null</li>
     *                                                                   </ul>
     * @throws SWTException             <ul>
     *                                                                   <li>ERROR_THREAD_INVALID_ACCESS - if not
     *                                                                   called from the thread that created the
     *                                                                   parent</li>
     *                                                                   <li>ERROR_INVALID_SUBCLASS - if this class
     *                                                                   is not an allowed subclass</li>
     *                                                                   </ul>
     * @see Widget#getStyle
     */
    public BreadcrumbItem(final Breadcrumb parent) {
        this(parent, SWT.NONE);
    }

    /**
     * Constructs a new instance of this class given its parent (which must be a
     * <code>Breadcrumb</code>) and a style value describing its behavior and
     * appearance. The item is added to the end of the items maintained by its
     * parent.
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
     *                                                                   <li>ERROR_NULL_ARGUMENT - if the parent is
     *                                                                   null</li>
     *                                                                   </ul>
     * @throws SWTException             <ul>
     *                                                                   <li>ERROR_THREAD_INVALID_ACCESS - if not
     *                                                                   called from the thread that created the
     *                                                                   parent</li>
     *                                                                   <li>ERROR_INVALID_SUBCLASS - if this class
     *                                                                   is not an allowed subclass</li>
     *                                                                   </ul>
     * @see Widget#getStyle
     */
    public BreadcrumbItem(final Breadcrumb parent, final int style) {
        super(parent, checkStyle(style));
        parent.addItem(this);
        parentBreadcrumb = parent;
        textColor = parent.getDisplay().getSystemColor(SWT.COLOR_BLACK);
        textColorSelected = parent.getDisplay().getSystemColor(SWT.COLOR_BLACK);
        enabled = true;

        if ((style & SWT.LEFT) != 0) {
            alignment = SWT.LEFT;
        }
        if ((style & SWT.CENTER) != 0) {
            alignment = SWT.CENTER;
        }
        if ((style & SWT.RIGHT) != 0) {
            alignment = SWT.RIGHT;
        }

        selectionListeners = new ArrayList<>();
        width = height = -1;
        // 初始化bounds
        bounds = new Rectangle(-1, 0, 0, 0);

        menu = new Menu(parent.getShell(), SWT.POP_UP);
    }

    private static int checkStyle(int style) {
        style = checkBits(style, SWT.NONE, SWT.PUSH, SWT.TOGGLE);
        if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
            return checkBits(style, SWT.CENTER, SWT.LEFT, SWT.RIGHT);
        }
        return style;
    }

    private static int checkBits(int style, final int int0, final int int1, final int int2) {
        final int mask = int0 | int1 | int2;
        if ((style & mask) == 0) {
            style |= int0;
        }
        if ((style & int0) != 0) {
            style = style & ~mask | int0;
        }
        if ((style & int1) != 0) {
            style = style & ~mask | int1;
        }
        if ((style & int2) != 0) {
            style = style & ~mask | int2;
        }
        return style;
    }

    /**
     * Adds the listener to the collection of listeners who will be notified when
     * the control is selected by the user, by sending it one of the messages
     * defined in the <code>SelectionListener</code> interface.
     * <p>
     * <code>widgetDefaultSelected</code> is not called.
     * </p>
     *
     * @param listener the listener which should be notified when the control is
     *                 selected by the user,
     * @throws IllegalArgumentException <ul>
     *                                                                   <li>ERROR_NULL_ARGUMENT - if the listener is
     *                                                                   null</li>
     *                                                                   </ul>
     * @throws SWTException             <ul>
     *                                                                   <li>ERROR_WIDGET_DISPOSED - if the receiver
     *                                                                   has been disposed</li>
     *                                                                   <li>ERROR_THREAD_INVALID_ACCESS - if not
     *                                                                   called from the thread that created the
     *                                                                   receiver</li>
     *                                                                   </ul>
     * @see SelectionListener
     * @see #removeSelectionListener
     * @see SelectionEvent
     */
    public void addSelectionListener(final SelectionListener listener) {
        checkWidget();
        if (listener == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }
        selectionListeners.add(listener);
    }

    /**
     * @see Widget#dispose()
     */
    @Override
    public void dispose() {
        getParent().removeItem(this);
        bounds = null;
        disabledImage = null;
        selectionImage = null;
        textColor = null;
        textColorSelected = null;
        super.dispose();
    }

    /**
     * Returns a value which describes the position of the text in the receiver. The
     * value will be one of <code>LEFT</code>, <code>RIGHT</code> or
     * <code>CENTER</code>.
     *
     * @return the alignment
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public int getAlignment() {
        checkWidget();
        return alignment;
    }

    /**
     * Returns a rectangle describing the receiver's size and location relative to
     * its parent (or its display if its parent is null), unless the receiver is a
     * shell. In this case, the location is relative to the display.
     *
     * @return the receiver's bounding rectangle
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public Rectangle getBounds() {
        checkWidget();
        return bounds;
    }

    /**
     * @return the image displayed when the button is disabled
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public Image getDisabledImage() {
        checkWidget();
        return disabledImage;
    }

    /**
     * Returns <code>true</code> if the receiver is enabled, and <code>false</code>
     * otherwise. A disabled control is typically not selectable from the user
     * interface and draws with an inactive or "grayed" look.
     *
     * @return the receiver's enabled state
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     * @see #isEnabled
     */
    public boolean getEnabled() {
        checkWidget();
        return enabled;
    }

    /**
     * Returns the whole height of the item.
     *
     * @return the receiver's height
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public int getHeight() {
        checkWidget();
        if (height == -1) {
            return computeDefaultSize().y;
        }
        return height;
    }

    /**
     * @return the default size of the item
     */
    private Point computeDefaultSize() {
        final Point sizeOfTextAndImages = computeSizeOfTextAndImages();
        return new Point(2 * MARGIN_WIDTH + sizeOfTextAndImages.x + ARROW_WIDTH,
                2 * MARGIN_HEIGHT + sizeOfTextAndImages.y);
    }

    /**
     * Returns the receiver's parent, which must be a <code>Breadcrumb</code>.
     *
     * @return the receiver's parent
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public Breadcrumb getParent() {
        checkWidget();
        return parentBreadcrumb;
    }

    /**
     * Returns <code>true</code> if the receiver is selected, and false otherwise.
     *
     * @return the selection state
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public boolean getSelection() {
        checkWidget();
        return selection;
    }

    /**
     * @return the image displayed when the button is selected
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public Image getSelectionImage() {
        checkWidget();
        return selectionImage;
    }

    /**
     * Returns the color of the text when the button is enabled and not selected.
     *
     * @return the receiver's text color
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public Color getTextColor() {
        checkWidget();
        return textColor;
    }

    /**
     * Returns the color of the text when the button is not selected.
     *
     * @return the receiver's text color
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */

    public Color getTextColorSelected() {
        checkWidget();
        return textColorSelected;
    }

    /**
     * Returns the receiver's tool tip text, or null if it has not been set.
     *
     * @return the receiver's tool tip text
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public String getTooltipText() {
        checkWidget();
        return tooltipText;
    }

    /**
     * Returns the whole width of the item.
     *
     * @return the receiver's height
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public int getWidth() {
        checkWidget();
        if (width == -1) {
            return Math.max(computeDefaultSize().x, MIN_WIDTH);
        }
        return Math.max(width, MIN_WIDTH);
    }

    /**
     * Returns <code>true</code> if the receiver is enabled, and <code>false</code>
     * otherwise. A disabled control is typically not selectable from the user
     * interface and draws with an inactive or "grayed" look.
     *
     * @return the receiver's enabled state
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     * @see #getEnabled
     */
    public boolean isEnabled() {
        checkWidget();
        return enabled;
    }

    /**
     * Removes the listener from the collection of listeners who will be notified
     * when the control is selected by the user.
     *
     * @param listener the listener which should no longer be notified
     * @throws IllegalArgumentException <ul>
     *                                                                   <li>ERROR_NULL_ARGUMENT - if the listener is
     *                                                                   null</li>
     *                                                                   </ul>
     * @throws SWTException             <ul>
     *                                                                   <li>ERROR_WIDGET_DISPOSED - if the receiver
     *                                                                   has been disposed</li>
     *                                                                   <li>ERROR_THREAD_INVALID_ACCESS - if not
     *                                                                   called from the thread that created the
     *                                                                   receiver</li>
     *                                                                   </ul>
     * @see SelectionListener
     * @see #addSelectionListener
     */
    public void removeSelectionListener(final SelectionListener listener) {
        checkWidget();
        if (listener == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }
        selectionListeners.remove(listener);
    }

    /**
     * Controls how text will be displayed in the receiver. The argument should be
     * one of <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>.
     *
     * @param alignment the new alignment
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public void setAlignment(final int alignment) {
        checkWidget();
        this.alignment = alignment;
    }

    /**
     * Sets the receiver's size and location to the rectangular area specified by
     * the argument. The <code>x</code> and <code>y</code> fields of the rectangle
     * are relative to the receiver's parent (or its display if its parent is null).
     * <p>
     * Note: Attempting to set the width or height of the receiver to a negative
     * number will cause that value to be set to zero instead.
     * </p>
     *
     * @param rectangle the new bounds for the receiver
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public void setBounds(final Rectangle rectangle) {
        checkWidget();
        if (bounds == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }
        bounds = new Rectangle(Math.max(0, rectangle.x), //
                Math.max(0, rectangle.y), //
                Math.max(0, rectangle.width), //
                Math.max(0, rectangle.height));
    }

    /**
     * Sets the receiver's image to the argument when this is one is disabled, which
     * may be null indicating that no image should be displayed.
     *
     * @param image the image to display on the receiver (may be null)
     * @throws IllegalArgumentException <ul>
     *                                                                   <li>ERROR_INVALID_ARGUMENT - if the image
     *                                                                   has been disposed</li>
     *                                                                   </ul>
     * @throws SWTException             <ul>
     *                                                                   <li>ERROR_WIDGET_DISPOSED - if the receiver
     *                                                                   has been disposed</li>
     *                                                                   <li>ERROR_THREAD_INVALID_ACCESS - if not
     *                                                                   called from the thread that created the
     *                                                                   receiver</li>
     *                                                                   </ul>
     */
    public void setDisabledImage(final Image image) {
        checkWidget();
        disabledImage = image;
    }

    /**
     * Enables the receiver if the argument is <code>true</code>, and disables it
     * otherwise.
     * <p>
     * A disabled control is typically not selectable from the user interface and
     * draws with an inactive or "grayed" look.
     * </p>
     *
     * @param enabled the new enabled state
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public void setEnabled(final boolean enabled) {
        checkWidget();
        this.enabled = enabled;
    }

    /**
     * Sets the height of the receiver.
     * <p>
     * Note: Attempting to set the width or height of the receiver to a negative
     * number will cause that value to be set to zero instead.
     * </p>
     *
     * @param height the new width
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public void setHeight(final int height) {
        checkWidget();
        this.height = Math.max(height, 0);
    }

    /**
     * Sets the selection state of the receiver.
     *
     * @param selected the new selection state
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public void setSelection(final boolean selected) {
        checkWidget();
        selection = selected;
    }

    public void setHover(final boolean hovered) {
        checkWidget();
        hover = hovered;
    }

    /**
     * Sets the receiver's image to the argument when this one is selected, which
     * may be null indicating that no image should be displayed.
     *
     * @param image the image to display on the receiver (may be null)
     * @throws IllegalArgumentException <ul>
     *                                                                   <li>ERROR_INVALID_ARGUMENT - if the image
     *                                                                   has been disposed</li>
     *                                                                   </ul>
     * @throws SWTException             <ul>
     *                                                                   <li>ERROR_WIDGET_DISPOSED - if the receiver
     *                                                                   has been disposed</li>
     *                                                                   <li>ERROR_THREAD_INVALID_ACCESS - if not
     *                                                                   called from the thread that created the
     *                                                                   receiver</li>
     *                                                                   </ul>
     */
    public void setSelectionImage(final Image image) {
        checkWidget();
        selectionImage = image;
    }

    /**
     * Sets the receiver's text color to the argument, which may be null indicating
     * that no image should be displayed.
     *
     * @param textColor the text color to display on the receiver (may be null)
     * @throws IllegalArgumentException <ul>
     *                                                                   <li>ERROR_INVALID_ARGUMENT - if the image
     *                                                                   has been disposed</li>
     *                                                                   </ul>
     * @throws SWTException             <ul>
     *                                                                   <li>ERROR_WIDGET_DISPOSED - if the receiver
     *                                                                   has been disposed</li>
     *                                                                   <li>ERROR_THREAD_INVALID_ACCESS - if not
     *                                                                   called from the thread that created the
     *                                                                   receiver</li>
     *                                                                   </ul>
     */
    public void setTextColor(final Color textColor) {
        checkWidget();
        this.textColor = textColor;
    }

    /**
     * Sets the receiver's text color to the argument when this one is selected,
     * which may be null indicating that no image should be displayed.
     *
     * @param textColor the text color to display on the receiver (may be null)
     * @throws IllegalArgumentException <ul>
     *                                                                   <li>ERROR_INVALID_ARGUMENT - if the image
     *                                                                   has been disposed</li>
     *                                                                   </ul>
     * @throws SWTException             <ul>
     *                                                                   <li>ERROR_WIDGET_DISPOSED - if the receiver
     *                                                                   has been disposed</li>
     *                                                                   <li>ERROR_THREAD_INVALID_ACCESS - if not
     *                                                                   called from the thread that created the
     *                                                                   receiver</li>
     *                                                                   </ul>
     */
    public void setTextColorSelected(final Color textColor) {
        checkWidget();
        textColorSelected = textColor;
    }

    /**
     * Sets the receiver's tool tip text to the argument, which may be null
     * indicating that the default tool tip for the control will be shown. For a
     * control that has a default tool tip, such as the Tree control on Windows,
     * setting the tool tip text to an empty string replaces the default, causing no
     * tool tip text to be shown.
     * <p>
     * The mnemonic indicator (character '&amp;') is not displayed in a tool tip. To
     * display a single '&amp;' in the tool tip, the character '&amp;' can be
     * escaped by doubling it in the string.
     * </p>
     *
     * @param string the new tool tip text (or null)
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public void setTooltipText(final String string) {
        checkWidget();
        tooltipText = string == null ? "" : string;
    }

    /**
     * Sets the width of the receiver.
     * <p>
     * Note: Attempting to set the width or height of the receiver to a negative
     * number will cause that value to be set to zero instead.
     * </p>
     *
     * @param width the new width
     * @throws SWTException <ul>
     *                                           <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                                           disposed</li>
     *                                           <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                                           thread that created the receiver</li>
     *                                           </ul>
     */
    public void setWidth(final int width) {
        checkWidget();
        this.width = Math.max(0, width);
    }

    // --------------------------------------------- Package visibility section
    void fireSelectionEvent() {
        final Event event = new Event();
        event.widget = parentBreadcrumb;
        event.display = getDisplay();
        event.item = this;
        event.type = SWT.Selection;
        for (final SelectionListener selectionListener : selectionListeners) {
            selectionListener.widgetSelected(new SelectionEvent(event));
        }
    }

    void setMouseDownXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
        this.isMouseDown = true;
        if (shouldShowMenu(xCoordinate)) {
            showMenu();
        }
    }

    private boolean shouldShowMenu(int x) {
        return x >= getWidth() - ARROW_WIDTH && x <= getWidth();
    }

    private void showMenu() {
        Rectangle bounds = getBounds();
        // 转换为Display的坐标系
        Point location = parentBreadcrumb.toDisplay(bounds.x, bounds.y);
        menu.setLocation(location.x + getWidth() / 2, location.y + toolbarHeight);
        menu.setVisible(true);
    }

    public Menu getMenu() {
        checkWidget();
        return menu;
    }

    /**
     * 将此item标记为不可见。（breadcrumb中判断当前点击的是哪个item
     * 是通过判断当前鼠标是否落在item的区域内来实现的，因此需要将不可见的item 的x坐标设为该数，即让鼠标永远不会落在该区域）
     */
    void setInvisible() {
        bounds.x = INVISIBLE;
    }

    void drawButtonAtPosition(final int x) {

        if (selection) {
            drawBackgroundAtPosition(x);
        }

        if (hover) {
            drawBackgroundAtPosition(x);
        }

        int xPosition = computeGap();

        final Image drawnedImage = drawImageAtPosition(x + xPosition);
        if (drawnedImage != null) {
            xPosition += drawnedImage.getBounds().width + 2 * MARGIN_WIDTH;
        }
        drawTextAtPosition(x + xPosition);
        drawTriangleAtPosition(x);

        bounds = new Rectangle(x, 0, getWidth(), toolbarHeight);
    }

    private void drawBackgroundAtPosition(final int x) {
        if (selection) {
            gc.setForeground(SELECTED_COLOR);
            gc.setBackground(SELECTED_COLOR);
        } else if (hover) {
            gc.setForeground(HOVER_COLOR);
            gc.setBackground(HOVER_COLOR);
        }

        final boolean hasBorder = parentBreadcrumb.hasBorder;
        final int borderWidth = hasBorder ? 1 : 0;

        final int xUpperLeft = x + borderWidth;
        final int yUpperLeft = borderWidth;
        final int rectWidth = getWidth() - 2 * borderWidth;
        final int rectHeight = getHeight() - 2 * borderWidth;
        gc.fillRectangle(xUpperLeft, yUpperLeft, rectWidth, rectHeight);

        // 画分割箭头和文本的线
        gc.setForeground(HOVER_LINE_COLOR);
        gc.setBackground(HOVER_LINE_COLOR);
        int arrowX = x + getWidth() - ARROW_WIDTH;
        gc.drawLine(arrowX, yUpperLeft, arrowX, rectHeight);
    }

    private void drawTriangleAtPosition(final int x) {
        gc.setLineWidth(1);
        int arrowX = x + getWidth() - ARROW_WIDTH;

        if (isMouseDown) {
            gc.drawLine(arrowX + ARROW_WIDTH / 2 - 4, toolbarHeight / 2 - 4, arrowX + ARROW_WIDTH / 2,
                    toolbarHeight / 2);
            gc.drawLine(arrowX + ARROW_WIDTH / 2, toolbarHeight / 2, arrowX + ARROW_WIDTH / 2 + 4,
                    toolbarHeight / 2 - 4);
        } else {
            gc.drawLine(arrowX + 8, 10, arrowX + 12, toolbarHeight / 2);
            gc.drawLine(arrowX + 12, toolbarHeight / 2, arrowX + 8, 18);
        }
    }

    private int computeGap() {
        final int widthOfTextAndImage = computeSizeOfTextAndImages().x;
        switch (alignment) {
            case SWT.CENTER:
                return (getWidth() - ARROW_WIDTH - widthOfTextAndImage) / 2;
            case SWT.RIGHT:
                return getWidth() - ARROW_WIDTH - widthOfTextAndImage - MARGIN_WIDTH;
            default:
                return MARGIN_WIDTH;
        }
    }

    private Point computeSizeOfTextAndImages() {
        int width = 0, height = 0;
        final boolean textISNotEmpty = getText() != null && !getText().equals("");

        if (textISNotEmpty) {
            final GC gc = new GC(parentBreadcrumb);
            gc.setFont(parentBreadcrumb.getFont());
            final Point extent = gc.stringExtent(getText());
            gc.dispose();
            width += extent.x;
            height = extent.y;
        }

        final Point imageSize = computeMaxWidthAndHeightForImages(getImage(), selectionImage, disabledImage);

        if (imageSize.x != -1) {
            width += imageSize.x;
            height = Math.max(imageSize.y, height);
            if (textISNotEmpty) {
                width += MARGIN_WIDTH * 2;
            }
        }
        width += MARGIN_WIDTH;
        return new Point(width, height);
    }

    private Point computeMaxWidthAndHeightForImages(final Image... images) {
        final Point imageSize = new Point(-1, -1);
        for (final Image image : images) {
            if (image == null) {
                continue;
            }
            final Rectangle imageBounds = image.getBounds();
            imageSize.x = Math.max(imageBounds.width, imageSize.x);
            imageSize.y = Math.max(imageBounds.height, imageSize.y);
        }
        return imageSize;
    }

    private Image drawImageAtPosition(final int xPosition) {
        Image image;
        if (!isEnabled()) {
            image = disabledImage;
        } else if (selection) {
            image = selectionImage;
        } else {
            image = getImage();
        }

        if (image == null) {
            return null;
        }

        final int yPosition = (toolbarHeight - image.getBounds().height) / 2;
        gc.drawImage(image, (int) (xPosition + MARGIN_WIDTH * 1.5), yPosition);
        return image;
    }

    private void drawTextAtPosition(final int xPosition) {
        gc.setFont(parentBreadcrumb.getFont());
        if (selection) {
            gc.setForeground(textColorSelected);
        } else {
            gc.setForeground(textColor);
        }

        final Point textSize = gc.stringExtent(getText());
        final int yPosition = (toolbarHeight - textSize.y) / 2;

        gc.drawText(getText(), xPosition, yPosition, true);
    }

    BreadcrumbItem setGc(final GC gc) {
        this.gc = gc;
        return this;
    }

    BreadcrumbItem setToolbarHeight(final int toolbarHeight) {
        this.toolbarHeight = toolbarHeight;
        return this;
    }


}
