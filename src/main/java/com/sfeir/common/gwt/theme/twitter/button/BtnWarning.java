package com.sfeir.common.gwt.theme.twitter.button;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Button important
 * 
 */
public class BtnWarning extends Btn {

	public BtnWarning() {
		super();
		addStyleName(getClientBundle().style().btnWarning());
	}

	/**
	 * Creates a button with the given HTML caption.
	 * 
	 * @param html
	 *            the HTML caption
	 */
	public BtnWarning(SafeHtml html) {
		this(html.asString());
	}

	/**
	 * Creates a button with the given HTML caption.
	 * 
	 * @param html
	 *            the HTML caption
	 */
	public BtnWarning(String html) {
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
	public BtnWarning(SafeHtml html, ClickHandler handler) {
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
	public BtnWarning(String html, ClickHandler handler) {
		this(html);
		addClickHandler(handler);
	}
}
