package com.sfeir.common.gwt.client.component;

import com.google.gwt.text.client.IntegerParser;
import com.google.gwt.text.client.IntegerRenderer;

/**
 * This textbox don't allow to insert other character than digits and a minus on the first character et only one decimal point 
 * 
 */
public class NumericIntegerBox extends NumericBox<Integer> {

    public NumericIntegerBox() {
	super(false, IntegerRenderer.instance(),
		IntegerParser.instance());
    }
}
