package com.sfeir.common.gwt.client.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sfeir.common.gwt.client.component.event.BeforeCloseTabEvent;
import com.sfeir.common.gwt.client.component.event.BeforeCloseTabHandler;
import com.sfeir.common.gwt.client.component.event.HasBeforeCloseTabHandlers;


/**
 * 
 * Onglet possédant un bouton pour fermer l'onglet
 * @author sfeir
 */
@SuppressWarnings("deprecation")
public class ClosableTabPanel extends TabPanel implements HasBeforeCloseTabHandlers, HasCloseHandlers<ClosableTabPanel> {
    protected CloseTabImage closeImages;
    private Boolean closable = false;
    private FlowPanel tabWrapper;
    private Widget tabBar;
    private boolean enableTabScroll = false;
    private boolean rendered = false;
    
    
    public ClosableTabPanel() {
        this((CloseTabImage) GWT.create(CloseTabImage.class));
    }

    public ClosableTabPanel(CloseTabImage closeImages) {
        super();
        this.closeImages = closeImages;
        VerticalPanel panel = (VerticalPanel) getWidget();
        tabWrapper = new FlowPanel();
        tabBar = panel.getWidget(0);
        tabWrapper.add(tabBar);
        panel.insert(tabWrapper, 0);
    }

    /**
     * Ajoute un onglet
     */
    public void add(Widget w, String tabText) {
        insert(w, tabText, getWidgetCount(), closable);
    }

    /**
     * Ajoute un onglet
     */
    public void add(Widget w, String tabText, boolean asHTML) {
        insert(w, tabText, asHTML, getWidgetCount(), closable);
    }

    /**
     * Ajoute un onglet
     */
    public void add(Widget w, Widget tabWidget) {
        insert(w, tabWidget, getWidgetCount(), closable);
    }

    /**
     * Ajoute un onglet
     */
    public void add(Widget widget, String tabText, Boolean closable) {
        insert(widget, tabText, getWidgetCount(), closable);
    }

    /**
     * Ajoute un onglet
     */
    public void add(Widget widget, String tabText, boolean isHTML, Boolean closable) {
        insert(widget, tabText, isHTML, getWidgetCount(), closable);
    }

    /**
     * Ajoute un onglet
     */
    public void add(Widget widget, Widget tabWidget, Boolean closable) {
        insert(widget, tabWidget, getWidgetCount(), closable);
    }

    /**
     * Ajoute un onglet
     */
    public void add(Widget widget, Widget tabWidget, boolean isHTML, Boolean closable) {
        insert(widget, tabWidget, getWidgetCount(), closable);
    }

    /**
     * Ajoute un onglet
     */
    public void insert(Widget widget, String tabText, int beforeIndex) {
        insert(widget, tabText, false, beforeIndex, closable);
    }

    /**
     * Ajoute un onglet
     */
    public void insert(Widget widget, Widget tabWidget, int beforeIndex) {
        insert(widget, tabWidget, beforeIndex, closable);
    }

    /**
     * Ajoute un onglet
     */
    public void insert(final Widget widget, String tabText, boolean isHTML, int beforeIndex) {
        insert(widget, tabText, isHTML, beforeIndex, false);
    }
    
    /**
     * Ajoute un onglet
     */
    public void insert(Widget widget, String tabText, int beforeIndex, Boolean closable) {
        insert(widget, tabText, false, beforeIndex, closable);
    }

    /**
     * Ajoute un onglet
     */
    public void insert(Widget widget, Widget tabWidget, int beforeIndex, Boolean closable) {
        Widget tabItem = createTabWidget(widget, tabWidget, closable);
        super.insert(widget, tabItem, beforeIndex);
    }

    /**
     * Ajoute un onglet
     */
    public void insert(final Widget widget, String tabText, boolean isHTML, int beforeIndex, Boolean closable) {
        Label tabWidget = (isHTML) ? new InlineHTML(tabText) : new InlineLabel(tabText);
        tabWidget.setWordWrap(false);
        Widget tabItem = createTabWidget(widget, tabWidget, closable);
        super.insert(widget, tabItem, beforeIndex);
    }

    /**
     * Ajoute et affiche un onglet
     * @param w
     * @param tabText
     */
    public void addAndShow(Widget w, String tabText) {
        insertAndShow(w, tabText, getWidgetCount(), closable);
    }

    /**
     * Ajoute et affiche un onglet
     * @param w
     * @param tabText
     * @param asHTML
     */
    public void addAndShow(Widget w, String tabText, boolean asHTML) {
        insertAndShow(w, tabText, asHTML, getWidgetCount(), closable);
    }

    /**
     * Ajoute et affiche un onglet
     * @param w
     * @param tabWidget
     */
    public void addAndShow(Widget w, Widget tabWidget) {
        insertAndShow(w, tabWidget, getWidgetCount(), closable);
    }

    /**
     * Ajoute et affiche un onglet
     * @param widget
     * @param tabText
     * @param closable
     */
    public void addAndShow(Widget widget, String tabText, Boolean closable) {
        insertAndShow(widget, tabText, getWidgetCount(), closable);
    }

    /**
     * Ajoute et affiche un onglet
     * @param widget
     * @param tabText
     * @param isHTML
     * @param closable
     */
    public void addAndShow(Widget widget, String tabText, boolean isHTML, Boolean closable) {
        insertAndShow(widget, tabText, isHTML, getWidgetCount(), closable);
    }

    /**
     * Ajoute et affiche un onglet
     * @param widget
     * @param tabWidget
     * @param closable
     */
    public void addAndShow(Widget widget, Widget tabWidget, Boolean closable) {
        insertAndShow(widget, tabWidget, getWidgetCount(), closable);
    }

    /**
     * Ajoute et affiche un onglet
     * @param widget
     * @param tabWidget
     * @param isHTML
     * @param closable
     */
    public void addAndShow(Widget widget, Widget tabWidget, boolean isHTML, Boolean closable) {
        insertAndShow(widget, tabWidget, getWidgetCount(), closable);
    }

    /**
     * Ajoute et affiche un onglet
     * @param widget
     * @param tabText
     * @param beforeIndex
     */
    public void insertAndShow(Widget widget, String tabText, int beforeIndex) {
        insertAndShow(widget, tabText, false, beforeIndex, closable);
    }

    /**
     * Ajoute et affiche un onglet
     * @param widget
     * @param tabWidget
     * @param beforeIndex
     */
    public void insertAndShow(Widget widget, Widget tabWidget, int beforeIndex) {
        insertAndShow(widget, tabWidget, beforeIndex, closable);
    }

    /**
     * Ajoute et affiche un onglet
     * @param widget
     * @param tabText
     * @param isHTML
     * @param beforeIndex
     */
    public void insertAndShow(final Widget widget, String tabText, boolean isHTML, int beforeIndex) {
        insertAndShow(widget, tabText, isHTML, beforeIndex, false);
    }
    
    /**
     * Ajoute et affiche un onglet
     * @param widget
     * @param tabText
     * @param beforeIndex
     * @param closable
     */
    public void insertAndShow(Widget widget, String tabText, int beforeIndex, Boolean closable) {
        insertAndShow(widget, tabText, false, beforeIndex, closable);
    }

    /**
     * Ajoute et affiche un onglet
     * @param widget
     * @param tabWidget
     * @param beforeIndex
     * @param closable
     */
    public void insertAndShow(Widget widget, Widget tabWidget, int beforeIndex, Boolean closable) {
        insert(widget, tabWidget, beforeIndex, closable);
        selectTab(getWidgetIndex(widget));
    }

    /**
     * Ajoute et affiche un onglet
     * @param widget
     * @param tabText
     * @param isHTML
     * @param beforeIndex
     * @param closable
     */
    public void insertAndShow(final Widget widget, String tabText, boolean isHTML, int beforeIndex, Boolean closable) {
        insert(widget, tabText, isHTML, beforeIndex, closable);
        selectTab(getWidgetIndex(widget));
    }

    /**
     * Definir a vrai pour que tous les onglets soit fermable (sauf si definie specifiquement pour un onglet)
     * @param closable
     */
    public void setClosable(Boolean closable) {
        this.closable = closable;
    }

    public Boolean getClosable() {
        return closable;
    }

    /**
     * Active le défillement des onglets
     * Experimentale (Affiche une scrollbar orizontale)
     * @param enableTabScroll
     */
    public void setEnableTabScroll(boolean enableTabScroll) {
        this.enableTabScroll = enableTabScroll;
    }

    public boolean isEnableTabScroll() {
        return enableTabScroll;
    }

    /**
     * Evènement lancé lors avant la fermuture d'un onglet
     * Si la methode cancel de l'énènement est appelé, alors l'onglet n'est pas fermé
     */
    public HandlerRegistration addBeforeCloseTabHandler(BeforeCloseTabHandler handler) {
        return addHandler(handler, BeforeCloseTabEvent.getType());
    }

    /**
     * Evènement lancé après la fermeture d'un onglet
     */
    public HandlerRegistration addCloseHandler(CloseHandler<ClosableTabPanel> handler) {
        return addHandler(handler, CloseEvent.getType());
    }

    /**
     * Ferme un onglet et affiche l'onglet suivant
     * @param widget
     */
    public void closeTab(final Widget widget) {
        int index = getWidgetIndex(widget);
        BeforeCloseTabEvent beforeCloseTabEvent = new BeforeCloseTabEvent(index);
        fireEvent(beforeCloseTabEvent);
        if (beforeCloseTabEvent.isCanceled())
            return;
        remove(widget);
        if (index >= getWidgetCount())
            index = getWidgetCount() - 1;
        selectTab(index);
        CloseEvent.fire(this, this);
        updateSroll();
    }
    
    @Override
    protected void onAttach() {
        super.onAttach();
        rendered  = true;
        updateSroll();
    }
    
    @Override
    protected void onDetach() {
        super.onDetach();
        rendered = false;
    }
    
    private void updateSroll() {
        if (enableTabScroll && rendered) {
            Element wrapperElement = tabWrapper.getElement();
            DOM.setStyleAttribute(wrapperElement, "position", "relative");
            DOM.setStyleAttribute(wrapperElement, "overflow", "scroll-y");
            Element tabElement = tabBar.getElement();
            DOM.setStyleAttribute(tabElement, "position", "absolute");
        }
    }

    /**
     * Cree le widget contenant toute la table
     * @param widget
     * @param tabWidget
     * @param closable
     * @return
     */
    private Widget createTabWidget(final Widget widget, Widget tabWidget, Boolean closable) {
        FlowPanel tabHpItem = new FlowPanel();
        tabHpItem.setStylePrimaryName("gwt-TabBarItem-center");
        tabHpItem.add(tabWidget);
        if (closable) {
            final Image closeButton = closeImages.tab_close().createImage();
            closeButton.setHeight("22px");
            closeButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    closeTab(widget);
                }
            });
            
            closeButton.addMouseOverHandler(new MouseOverHandler() {
                public void onMouseOver(MouseOverEvent event) {
                    closeImages.tab_close_hover().applyTo(closeButton);
                    closeButton.setHeight("22px");
                }
            });
            closeButton.addMouseOutHandler(new MouseOutHandler() {
                public void onMouseOut(MouseOutEvent event) {
                    closeImages.tab_close().applyTo(closeButton);
                    closeButton.setHeight("22px");
                }
            });
            FlowPanel closeButtonWrapper = new FlowPanel();
            closeButtonWrapper.addStyleName("gwt-TabClosebutton");
            closeButtonWrapper.add(closeButton);
            tabHpItem.add(closeButtonWrapper);
        }
        updateSroll();
        return tabHpItem;
    }

    /**
     * 
     * Images de la croix pour fermer l'onglet
     * @author sfeir
     */
    public interface CloseTabImage extends ImageBundle {
        public AbstractImagePrototype tab_close();
        public AbstractImagePrototype tab_close_hover();
    }
}
