package com.sfeir.common.gwt.client.component;

import com.google.gwt.text.client.DoubleParser;
import com.google.gwt.text.client.DoubleRenderer;

/**
 * This textbox don't allow to insert other character than digits and a minus on the first character et only one decimal point 
 * 
 */
public class NumericDoubleBox extends NumericBox<Double> {

    public NumericDoubleBox() {
	super(false, DoubleRenderer.instance(),
		DoubleParser.instance());
    }
}
