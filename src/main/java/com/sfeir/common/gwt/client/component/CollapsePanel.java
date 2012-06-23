
package com.sfeir.common.gwt.client.component;

/*
 * Copyright 2008 Google Inc. Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanelImages;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget that consists of a header and a content panel that discloses the content when a user
 * clicks on the header. <h3>CSS Style Rules</h3> <ul class="css"> <li>.gwt-CollapsePanel { the
 * panel's primary style }</li> <li>.gwt-CollapsePanel-open { dependent style set when panel is
 * open }</li> <li>.gwt-CollapsePanel-closed { dependent style set when panel is closed }</li>

 * <p>
 * The header and content sections can be easily selected using css with a child selector:<br/>
 * .gwt-CollapsePanel-open .header { ... }
 * </p>
 */
@SuppressWarnings("deprecation")
public final class CollapsePanel extends Composite implements HasAnimation,
            HasOpenHandlers<CollapsePanel>, HasCloseHandlers<CollapsePanel> {
    public static final Boolean DIRECTION_LEFT = true;
    
    public static final Boolean DIRECTION_RIGHT = false;
    
    /**
     * The duration of the animation.
     */
    private static final int ANIMATION_DURATION = 350;
    
    private Boolean direction = DIRECTION_LEFT;
    
    /**
     * An {@link Animation} used to open the content.
     */
    private static class ContentAnimation extends Animation {
        /**
         * Whether the item is being opened or closed.
         */
        private boolean opening;
        
        /**
         * The {@link CollapsePanel} being affected.
         */
        private CollapsePanel curPanel;
        
        /**
         * Open or close the content.
         * 
         * @param panel the panel to open or close
         * @param animate true to animate, false to open instantly
         */
        public void setOpen(CollapsePanel panel, boolean animate) {
            // Immediately complete previous open
            cancel();
            
            // Open the new item
            if (animate) {
                curPanel = panel;
                opening = panel.isOpen;
                run(ANIMATION_DURATION);
            }
            else {
                panel.contentWrapper.setVisible(panel.isOpen);
                if (panel.isOpen) {
                    // Special treatment on the visible case to ensure LazyPanel works
                    panel.getContent().setVisible(true);
                }
            }
        }
        
        @Override
        protected void onComplete() {
            if (!opening) {
                curPanel.contentWrapper.setVisible(false);
            }
            DOM.setStyleAttribute(curPanel.contentWrapper.getElement(), "width", "auto");
            curPanel = null;
        }
        
        @Override
        protected void onStart() {
            super.onStart();
            if (opening) {
                curPanel.contentWrapper.setVisible(true);
                // Special treatment on the visible case to ensure LazyPanel works
                curPanel.getContent().setVisible(true);
            }
        }
        
        @Override
        protected void onUpdate(double progress) {
            int scrollWidth = DOM.getElementPropertyInt(curPanel.contentWrapper.getElement(),
                "scrollWidth");
            int width = (int) (progress * scrollWidth);
            if (!opening) {
                width = scrollWidth - width;
            }
            width = Math.max(width, 1);
            DOM.setStyleAttribute(curPanel.contentWrapper.getElement(), "width", width + "px");
            DOM.setStyleAttribute(curPanel.contentWrapper.getElement(), "height", "auto");
        }
    }
    
    /**
     * Used to wrap widgets in the header to provide click support. Effectively wraps the widget in
     * an <code>anchor</code> to get automatic keyboard access.
     */
    private final class ClickableHeader extends SimplePanel {
        
        private ClickableHeader() {
            // Anchor is used to allow keyboard access.
            super(DOM.createAnchor());
            Element elem = getElement();
            DOM.setElementProperty(elem, "href", "javascript:void(0);");
            // Avoids layout problems from having blocks in inlines.
            DOM.setStyleAttribute(elem, "display", "block");
            DOM.setStyleAttribute(elem, "height", "100%");
            sinkEvents(Event.ONCLICK);
            setStyleName(STYLENAME_HEADER);
        }
        
        @Override
        public void onBrowserEvent(Event event) {
            // no need to call super.
            switch (DOM.eventGetType(event)) {
                case Event.ONCLICK:
                    // Prevent link default action.
                    DOM.eventPreventDefault(event);
                    setOpen(!isOpen);
            }
        }
    }
    
    /**
     * The default header widget used within a {@link CollapsePanel}.
     */
    private class DefaultHeader extends Widget implements HasText, OpenHandler<CollapsePanel>,
                CloseHandler<CollapsePanel> {
        
        /**
         * imageTD holds the image for the icon, not null. labelTD holds the text for the label.
         */
        private final Element labelTD;
        
        private final Image iconImage;
        
        private final DisclosurePanelImages images;
        
        private DefaultHeader(DisclosurePanelImages images, String text) {
            this.images = images;
            
            iconImage = isOpen ? images.disclosurePanelOpen().createImage()
                        : images.disclosurePanelClosed().createImage();
            
            // I do not need any Widgets here, just a DOM structure.
            Element root = DOM.createTable();
            Element tbody = DOM.createTBody();
            Element tr = DOM.createTR();
            final Element imageTD = DOM.createTD();
            Element trLabel = DOM.createTR();
            labelTD = DOM.createTD();
            
            setElement(root);
            
            DOM.appendChild(root, tbody);
            DOM.appendChild(tbody, tr);
            DOM.appendChild(tbody, trLabel);
            DOM.appendChild(tr, imageTD);
            DOM.appendChild(trLabel, labelTD);
            
            // set image TD to be same width as image.
            DOM.setElementProperty(imageTD, "align", "center");
            DOM.setElementProperty(imageTD, "valign", "middle");
            DOM.setStyleAttribute(imageTD, "width", iconImage.getWidth() + "px");
            DOM.setStyleAttribute(labelTD, "width", iconImage.getWidth() + "px");
            DOM.setStyleAttribute(labelTD, "height", "100%");
            DOM.setStyleAttribute(root, "height", "100%");
            DOM.setStyleAttribute(trLabel, "height", "100%");
            
            DOM.appendChild(imageTD, iconImage.getElement());
            
            setText(text);
            
            addOpenHandler(this);
            addCloseHandler(this);
            setStyle();
        }
        
        public final String getText() {
            return DOM.getInnerText(labelTD);
        }
        
        public final void onClose(CloseEvent<CollapsePanel> event) {
            setStyle();
        }
        
        public final void onOpen(OpenEvent<CollapsePanel> event) {
            setStyle();
        }
        
        public final void setText(String text) {
            text = text.replaceAll("(.)", "$1<br />");
            DOM.setInnerHTML(labelTD, "&nbsp;<br />" + text + "<br />&nbsp;");
        }
        
        private void setStyle() {
            if (isOpen) {
                images.disclosurePanelOpen().applyTo(iconImage);
            }
            else {
                images.disclosurePanelClosed().applyTo(iconImage);
            }
        }
    }
    
    // Stylename constants.
    private static final String STYLENAME_DEFAULT = "gwt-CollapsePanel";
    
    private static final String STYLENAME_SUFFIX_OPEN = "open";
    
    private static final String STYLENAME_SUFFIX_CLOSED = "closed";
    
    private static final String STYLENAME_HEADER = "header";
    
    private static final String STYLENAME_CONTENT = "content";
    
    /**
     * The {@link Animation} used to open and close the content.
     */
    private static ContentAnimation contentAnimation;
    
    private static DisclosurePanelImages createDefaultImages() {
        return GWT.create(CollapsePanelImages.class);
    }
    
    private static DisclosurePanelImages createDefaultImages(Boolean direction) {
        if (direction) {
            return GWT.create(CollapsePanelImages.class);
        }
        else {
            return GWT.create(CollapsePanelImagesRight.class);
        }
    }
    
    /**
     * top level widget. The first child will be a reference to {@link #header}. The second child
     * will be a reference to {@link #contentWrapper}.
     */
    private final HorizontalPanel mainPanel = new HorizontalPanel();
    
    /**
     * The wrapper around the content widget.
     */
    private final SimplePanel contentWrapper = new SimplePanel();
    
    /**
     * holds the header widget.
     */
    private final ClickableHeader header = new ClickableHeader();
    
    private boolean isAnimationEnabled = true;
    
    private boolean isOpen = false;
    
    /**
     * Creates an empty DisclosurePanel that is initially closed.
     */
    public CollapsePanel() {
        init(false);
    }
    
    /**
     * Creates an empty DisclosurePanel that is initially closed.
     */
    public CollapsePanel(Boolean direction) {
        this.direction = direction;
        init(false);
    }
    
    /**
     * Creates a DisclosurePanel with the specified header text, an initial open/close state and a
     * bundle of images to be used in the default header widget.
     * 
     * @param images a bundle that provides disclosure panel specific images
     * @param headerText the text to be displayed in the header
     * @param isOpen the initial open/close state of the content panel
     */
    public CollapsePanel(DisclosurePanelImages images, String headerText, boolean isOpen) {
        init(isOpen);
        setHeader(new DefaultHeader(images, headerText));
    }
    
    /**
     * Creates a DisclosurePanel with the specified header text, an initial open/close state and a
     * bundle of images to be used in the default header widget.
     * 
     * @param images a bundle that provides disclosure panel specific images
     * @param headerText the text to be displayed in the header
     * @param isOpen the initial open/close state of the content panel
     * @param direction If the collapse is left or right
     */
    public CollapsePanel(DisclosurePanelImages images,
                         String headerText,
                         boolean isOpen,
                         Boolean direction) {
        this.direction = direction;
        init(isOpen);
        setHeader(new DefaultHeader(images, headerText));
    }
    
    /**
     * Creates a DisclosurePanel that will be initially closed using the specified text in the
     * header.
     * 
     * @param headerText the text to be displayed in the header
     */
    public CollapsePanel(String headerText) {
        this(createDefaultImages(), headerText, false);
    }
    
    /**
     * Creates a DisclosurePanel that will be initially closed using the specified text in the
     * header.
     * 
     * @param headerText the text to be displayed in the header
     * @param direction If the collapse is left or right
     */
    public CollapsePanel(String headerText, Boolean direction) {
        this(createDefaultImages(direction), headerText, false, direction);
    }
    
    /**
     * Creates a DisclosurePanel with the specified header text and an initial open/close state.
     * 
     * @param headerText the text to be displayed in the header
     * @param isOpen the initial open/close state of the content panel
     */
    public CollapsePanel(String headerText, boolean isOpen) {
        this(createDefaultImages(), headerText, isOpen);
    }
    
    /**
     * Creates a DisclosurePanel with the specified header text and an initial open/close state.
     * 
     * @param headerText the text to be displayed in the header
     * @param isOpen the initial open/close state of the content panel
     * @param direction If the collapse is left or right
     */
    public CollapsePanel(String headerText, boolean isOpen, Boolean direction) {
        this(createDefaultImages(direction), headerText, isOpen, direction);
    }
    
    /**
     * Creates a DisclosurePanel that will be initially closed using a widget as the header.
     * 
     * @param header the widget to be used as a header
     */
    public CollapsePanel(Widget header) {
        this(header, false);
    }
    
    /**
     * Creates a DisclosurePanel that will be initially closed using a widget as the header.
     * 
     * @param header the widget to be used as a header
     * @param direction If the collapse is left or right
     */
    public CollapsePanel(Widget header, Boolean direction) {
        this(header, false, direction);
    }
    
    /**
     * Creates a DisclosurePanel using a widget as the header and an initial open/close state.
     * 
     * @param header the widget to be used as a header
     * @param isOpen the initial open/close state of the content panel
     */
    public CollapsePanel(Widget header, boolean isOpen) {
        init(isOpen);
        setHeader(header);
    }
    
    /**
     * Creates a DisclosurePanel using a widget as the header and an initial open/close state.
     * 
     * @param header the widget to be used as a header
     * @param isOpen the initial open/close state of the content panel
     */
    public CollapsePanel(Widget header, boolean isOpen, Boolean direction) {
        this.direction = direction;
        init(isOpen);
        setHeader(header);
    }
    
    public void add(Widget w) {
        if (this.getContent() == null) {
            setContent(w);
        }
        else {
            throw new IllegalStateException("A DisclosurePanel can only contain two Widgets.");
        }
    }
    
    public HandlerRegistration addCloseHandler(CloseHandler<CollapsePanel> handler) {
        return addHandler(handler, CloseEvent.getType());
    }
    
    public HandlerRegistration addOpenHandler(OpenHandler<CollapsePanel> handler) {
        return addHandler(handler, OpenEvent.getType());
    }
    
    public void clear() {
        setContent(null);
    }
    
    /**
     * Gets the widget that was previously set in {@link #setContent(Widget)}.
     * 
     * @return the panel's current content widget
     */
    public Widget getContent() {
        return contentWrapper.getWidget();
    }
    
    /**
     * Gets the widget that is currently being used as a header.
     * 
     * @return the widget currently being used as a header
     */
    public Widget getHeader() {
        return header.getWidget();
    }
    
    /**
     * Gets a {@link HasText} instance to provide access to the headers's text, if the header widget
     * does provide such access.
     * 
     * @return a reference to the header widget if it implements {@link HasText}, <code>null</code>
     *         otherwise
     */
    public HasText getHeaderTextAccessor() {
        Widget widget = header.getWidget();
        return (widget instanceof HasText) ? (HasText) widget : null;
    }
    
    public boolean isAnimationEnabled() {
        return isAnimationEnabled;
    }
    
    /**
     * Determines whether the panel is open.
     * 
     * @return <code>true</code> if panel is in open state
     */
    public boolean isOpen() {
        return isOpen;
    }
    
    public boolean remove(Widget w) {
        if (w == getContent()) {
            setContent(null);
            return true;
        }
        return false;
    }
    
    public void setAnimationEnabled(boolean enable) {
        isAnimationEnabled = enable;
    }
    
    /**
     * Sets the content widget which can be opened and closed by this panel. If there is a
     * preexisting content widget, it will be detached.
     * 
     * @param content the widget to be used as the content panel
     */
    public void setContent(Widget content) {
        final Widget currentContent = getContent();
        
        // Remove existing content widget.
        if (currentContent != null) {
            contentWrapper.setWidget(null);
            currentContent.removeStyleName(STYLENAME_CONTENT);
        }
        
        // Add new content widget if != null.
        if (content != null) {
            contentWrapper.setWidget(content);
            content.addStyleName(STYLENAME_CONTENT);
            setContentDisplay(false);
        }
    }
    
    /**
     * Sets the widget used as the header for the panel.
     * 
     * @param headerWidget the widget to be used as the header
     */
    public void setHeader(Widget headerWidget) {
        header.setWidget(headerWidget);
    }
    
    /**
     * Changes the visible state of this <code>DisclosurePanel</code>.
     * 
     * @param isOpen <code>true</code> to open the panel, <code>false</code> to close
     */
    public void setOpen(boolean isOpen) {
        if (this.isOpen != isOpen) {
            this.isOpen = isOpen;
            setContentDisplay(true);
            fireEvent();
        }
    }
    
    /**
     * <b>Affected Elements:</b>
     * <ul>
     * <li>-header = the clickable header.</li>
     * </ul>
     * 
     * @see UIObject#onEnsureDebugId(String)
     */
    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        header.ensureDebugId(baseID + "-header");
    }
    
    private void fireEvent() {
        if (isOpen) {
            OpenEvent.fire(this, this);
        }
        else {
            CloseEvent.fire(this, this);
        }
    }
    
    private void init(boolean isOpen) {
        initWidget(mainPanel);
        if (direction) {
            mainPanel.add(contentWrapper);
            mainPanel.add(header);
        }
        else {
            mainPanel.add(header);
            mainPanel.add(contentWrapper);
        }
        DOM.setStyleAttribute(contentWrapper.getElement(), "padding", "0px");
        DOM.setStyleAttribute(contentWrapper.getElement(), "overflow", "hidden");
        this.isOpen = isOpen;
        setStyleName(STYLENAME_DEFAULT);
        setContentDisplay(false);
    }
    
    private void setContentDisplay(boolean animate) {
        if (isOpen) {
            removeStyleDependentName(STYLENAME_SUFFIX_CLOSED);
            addStyleDependentName(STYLENAME_SUFFIX_OPEN);
        }
        else {
            removeStyleDependentName(STYLENAME_SUFFIX_OPEN);
            addStyleDependentName(STYLENAME_SUFFIX_CLOSED);
        }
        
        if (getContent() != null) {
            if (contentAnimation == null) {
                contentAnimation = new ContentAnimation();
            }
            contentAnimation.setOpen(this, animate && isAnimationEnabled);
        }
    }
}
