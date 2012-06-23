package com.sfeir.common.gwt.client.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

/**
 * ClientBundle for the BluePrint framework
 * 
 */
public interface BluePrint extends ClientBundle {
    BluePrint INSTANCE = GWT.create(BluePrint.class);
    @Source("screen.css")
    Screen style();

}
