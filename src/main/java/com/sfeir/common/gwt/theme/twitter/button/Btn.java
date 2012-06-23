package com.sfeir.common.gwt.theme.twitter.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Button;

public class Btn extends Button {

	public Btn() {
		super();
		setStyleName(getClientBundle().style().btn());
	}

	/**
	 * Creates a button with the given HTML caption.
	 * 
	 * @param html
	 *            the HTML caption
	 */
	public Btn(SafeHtml html) {
		this(html.asString());
	}

	/**
	 * Creates a button with the given HTML caption.
	 * 
	 * @param html
	 *            the HTML caption
	 */
	public Btn(String html) {
		this();
		setHTML(html);
	}

	/**
	 * Creates a button with the given HTML caption and click listener.
	 * 
	 * @param html
	 *            the html caption
	 * @param handler
	 *            the click handler
	 */
	public Btn(SafeHtml html, ClickHandler handler) {
		this(html.asString(), handler);
	}

	/**
	 * Creates a button with the given HTML caption and click listener.
	 * 
	 * @param html
	 *            the HTML caption
	 * @param handler
	 *            the click handler
	 */
	public Btn(String html, ClickHandler handler) {
		this(html);
		addClickHandler(handler);
	}
	
	protected static BtnClientBundle getClientBundle() {
		if (clientBundle == null) {
			clientBundle = GWT.create(BtnClientBundle.class);
			clientBundle.style().ensureInjected();
		}
		return clientBundle;
	}
	static BtnClientBundle clientBundle = null;
	public static interface BtnClientBundle extends ClientBundle {
		@Source({"../defaultstyle.css","button.css"})
		ButtonStyle style();
	}
}
