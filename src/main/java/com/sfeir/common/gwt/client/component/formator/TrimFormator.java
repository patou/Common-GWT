package com.sfeir.common.gwt.client.component.formator;

/**
 * 
 * Nettoie les espaces au début et à la fin du texte
 * @author sfeir
 */
public class TrimFormator extends Formator<String> {
    @Override
    public String format(String value) {
        return value.trim();
    }
}
