package com.sfeir.common.gwt.client.component.label;


import java.util.Map;

import com.google.gwt.user.client.ui.TextBox;
import com.sfeir.common.gwt.client.component.label.base.LabelTextBoxBase;


/**
 * Champ de saisie de texte avec un Label
 * @author sfeir
 */
public class LabelTextBox extends LabelTextBoxBase<TextBox,String> {
    /**
     * 
     */
    public LabelTextBox() {
    }

    /**
     * @param text
     */
    public LabelTextBox(String text) {
        super(text);
    }

    /**
     * @param text
     * @param mandatory
     */
    public LabelTextBox(String text, boolean mandatory) {
        super(text, mandatory);
    }
    
    public LabelTextBox(Map<String,Object> config) {
        super(config);
    }

    @Override
    public void setConfig(Map<String, Object> config) {
        super.setConfig(config);
        if (config.containsKey("maxLength")) {
            setMaxLength((Integer) config.get("maxLength"));
        }
        if (config.containsKey("visibleLength")) {
            setVisibleLength((Integer) config.get("visibleLength"));
        }
    }
    
    /* (non-Javadoc)
     * @see com.sfeir.gwt.webding.client.component.LabelComponentBase#createComponent()
     */
    @Override
    protected TextBox createComponent() {
        return new TextBox();
    }
    
    public int getMaxLength() {
        return getComponent().getMaxLength();
    }

    public int getVisibleLength() {
        return getComponent().getVisibleLength();
    }

    public void setMaxLength(int length) {
        getComponent().setMaxLength(length);
    }

    public void setVisibleLength(int length) {
        getComponent().setVisibleLength(length);
    }
}
