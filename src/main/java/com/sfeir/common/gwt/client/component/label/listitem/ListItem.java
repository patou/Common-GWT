package com.sfeir.common.gwt.client.component.label.listitem;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.UIObject;

public class ListItem<V> extends UIObject implements HasText {
	private V value;
	private String text;
	
	public ListItem() {
	}
	
	public ListItem(String text, V value) {
		super();
		this.value = value;
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}
	@Override
	public void setText(String text) {
		this.text = text;
	}
	public void setValue(V value) {
		this.value = value;
	}
	public V getValue() {
		return value;
	}
}
