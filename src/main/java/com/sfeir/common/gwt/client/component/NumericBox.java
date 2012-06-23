package com.sfeir.common.gwt.client.component;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.ValueBox;

/**
 * This textbox don't allow to insert other character than digits and a minus on the first character et only one decimal point 
 * 
 */
public class NumericBox<T extends Number> extends ValueBox<T> implements KeyPressHandler, KeyUpHandler {

    private final Boolean allowFloatPoint;

    public NumericBox(Boolean allowFloatPoint, Renderer<T> renderer, Parser<T> parser) {
	super(Document.get().createTextInputElement(), renderer, parser);
	this.allowFloatPoint = allowFloatPoint;
	addKeyUpHandler(this);
	addKeyPressHandler(this);
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
	int index = getCursorPos();
	String previousText = getText().replace(',', '.');
	String newText = previousText.replaceAll(allowFloatPoint?"([^0-9.-]+)":"([^0-9-]+)", "");
	if (newText.indexOf(45, 1) > 0) {
	    newText = newText.substring(0, 1) + newText.substring(1).replace("-", "");
	}
	if (newText.indexOf(46) != newText.lastIndexOf(46)) {
	    newText = newText.substring(0, newText.lastIndexOf(46)).replace(".", "") + newText.substring(newText.lastIndexOf(46));
	}
	if (!previousText.equals(newText)) {
	    setText(newText);
	    setCursorPos(index > 0 ? index - (previousText.length() - newText.length()) : 0);
	}
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
	int index = getCursorPos();
	String previousText = getText();
	String newText;
	char charCode = event.getCharCode();
	if (getSelectionLength() > 0) {
	    newText = previousText.substring(0, getCursorPos()) + charCode + previousText.substring(getCursorPos() + getSelectionLength(), previousText.length());
	} else {
	    newText = previousText.substring(0, index) + charCode + previousText.substring(index, previousText.length());
	}
	try {
	    Double.parseDouble(newText);
	} catch (Exception e) {
	    if (!event.isControlKeyDown() && !Character.isDigit(charCode) && charCode != 45 && charCode != KeyCodes.KEY_BACKSPACE && charCode != KeyCodes.KEY_DELETE
		    && charCode != KeyCodes.KEY_LEFT && charCode != KeyCodes.KEY_RIGHT && charCode != KeyCodes.KEY_UP && charCode != KeyCodes.KEY_DOWN
		    && charCode != KeyCodes.KEY_HOME && charCode != KeyCodes.KEY_END && charCode != KeyCodes.KEY_ENTER && charCode != KeyCodes.KEY_ALT
		    && charCode != KeyCodes.KEY_CTRL && charCode != KeyCodes.KEY_SHIFT && charCode != KeyCodes.KEY_TAB) {
		cancelKey();
	    }
	}
    }
}
