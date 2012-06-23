package com.sfeir.common.gwt.client.component;

import com.google.gwt.text.client.LongParser;
import com.google.gwt.text.client.LongRenderer;

/**
 * This textbox don't allow to insert other character than digits and a minus on the first character et only one decimal point 
 * 
 */
public class NumericLongBox extends NumericBox<Long> {

    public NumericLongBox() {
	super(false, LongRenderer.instance(),
	        LongParser.instance());
    }
}
