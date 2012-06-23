package com.sfeir.common.gwt.client.component.formator;


/**
 * 
 * met le texte en majuscule
 * @author sfeir
 */
public class UpperCaseFormator extends Formator<String> {
    @Override
    public String format(String value) {
        return value.toUpperCase();
    }
}
