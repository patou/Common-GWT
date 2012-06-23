package com.sfeir.common.gwt.theme.twitter.button;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Button important
 * 
 */
public class BtnInfo extends Btn {

	public BtnInfo() {
		super();
		addStyleName(getClientBundle().style().btnInfo());
	}

	/**
	 * Creates a button with the given HTML caption.
	 * 
	 * @param html
	 *            the HTML caption
	 */
	public BtnInfo(SafeHtml html) {
		this(html.asString());
	}

	/**
	 * Creates a button with the given HTML caption.
	 * 
	 * @param html
	 *            the HTML caption
	 */
	public BtnInfo(String html) {
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
	public BtnInfo(SafeHtml html, ClickHandler handler) {
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
	public BtnInfo(String html, ClickHandler handler) {
		this(html);
		addClickHandler(handler);
	}
}
