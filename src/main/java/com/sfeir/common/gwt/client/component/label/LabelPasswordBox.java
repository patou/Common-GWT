package com.sfeir.common.gwt.client.component.label;

import java.util.Map;

import com.google.gwt.user.client.ui.PasswordTextBox;
import com.sfeir.common.gwt.client.component.label.base.LabelTextBoxBase;


/**
 * Champ de saisie de mot de passe avec un label
 * @author sfeir
 */
public class LabelPasswordBox extends LabelTextBoxBase<PasswordTextBox, String> {
    /**
     * 
     */
    public LabelPasswordBox() {
    }

    /**
     * @param label Label
     */
    public LabelPasswordBox(String label) {
        super(label);
    }

    /**
     * @param label
     * @param mandatory
     */
    public LabelPasswordBox(String label, boolean mandatory) {
        super(label, mandatory);
    }

    public LabelPasswordBox(Map<String, Object> config) {
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

    /*
     * (non-Javadoc)
     * @see com.sfeir.gwt.webding.client.component.LabelComponentBase#createComponent()
     */
    @Override
    protected PasswordTextBox createComponent() {
        return new PasswordTextBox();
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
