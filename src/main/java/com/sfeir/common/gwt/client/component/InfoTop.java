package com.sfeir.common.gwt.client.component;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.Window.ScrollHandler;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

/**
 * Affiche un message dans le coin supérieur droite, en empilant les messages.
 * Le message est affiché pendant 5 secondes
 * 
 * @author sfeir
 */
public class InfoTop extends DecoratedPopupPanel implements PositionCallback {

	
	static {
		Window.addWindowScrollHandler(new ScrollHandler() {
			
			@Override
			public void onWindowScroll(ScrollEvent event) {
				InfoTop.recalcAllPosition();
			}
		});
		Window.addResizeHandler(new ResizeHandler() {
			
			@Override
			public void onResize(ResizeEvent event) {
				InfoTop.recalcAllPosition();
			}
		});
	}
    /**
     * Affiche le message donne
     * 
     * @param text Le message a affiche
     */
    public static void display(String text) {
        display(text, 5000);
    }
    
    /**
     * Affiche le message donne
     * 
     * @param text Le message a affiche
     * @param delayMillis Temps d'affichage du message
     */
    public static void display(String text, int delayMillis) {
        final InfoTop popupInfo = new InfoTop(text, delayMillis);
        popupInfo.show();
    }

    /**
     * Cree un nouveau message et l'affiche
     * @param text
     */
    private InfoTop(String text, final int delayMillis) {
        super();
        this.delayMillis = delayMillis;
        setAnimationEnabled(true);
        setWidget(new Label(text));
        setPopupPositionAndShow(this);
    }
    
    private int saveTop;
    private int saveLeft;
	private int delayMillis;
    
    public void setPosition(int offsetWidth, int offsetHeight) {
    	saveLeft = offsetWidth - 10;
    	saveTop = getTop(InfoTop.this, offsetHeight);
    	calcPosition();
    	Timer t = new Timer() {
    		public void run() {
    			hide();
    			listeHeight.remove(InfoTop.this);
    		}
    	};
    	t.schedule(delayMillis);
    }
    /**
     * Calcule la position du message
     * @param offsetWidth
     * @param offsetHeight
     */
    protected void calcPosition() {
    	int width = Window.getClientWidth();
    	int left = Window.getScrollLeft() + width - saveLeft;
    	int top = Window.getScrollTop() + saveTop;
    	setPopupPosition(left, top);
    }

    protected static Map<InfoTop,Integer> listeHeight = new LinkedHashMap<InfoTop, Integer>();
    
    /**
     * Retourne la hauteur où placer le nouveau message en additionnant les hauteurs des précédents messages
     * 
     * @param info Popup Message à afficher
     * @param offsetHeight Hauteur de message à afficher
     * @return
     */
    protected static Integer getTop(InfoTop info, int offsetHeight) {
        int h = 40;
        for (Integer wh : listeHeight.values()) {
            h += wh + 20;
        }
        listeHeight.put(info, offsetHeight);
        return h;
    }
    
    /**
     * Recalcule toutes les positions
     */
    protected static void recalcAllPosition() {
    	for (InfoTop info : listeHeight.keySet()) {
           info.calcPosition();
        }
    }
}
