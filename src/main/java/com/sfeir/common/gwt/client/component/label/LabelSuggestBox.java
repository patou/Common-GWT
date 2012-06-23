package com.sfeir.common.gwt.client.component.label;

import java.util.Map;

import com.sfeir.common.gwt.client.component.label.base.LabelSuggestBoxBase;


/**
 * Champ de saisie de texte avec aide à la saisie
 * @author sfeir
 */
public class LabelSuggestBox extends LabelSuggestBoxBase<String,String> {
    /**
     * 
     */
    public LabelSuggestBox() {
    }

    /**
     * @param label
     */
    public LabelSuggestBox(String label) {
        super(label);
    }

    /**
     * @param label
     * @param mandatory
     */
    public LabelSuggestBox(String label, boolean mandatory) {
        super(label, mandatory);
    }

    public LabelSuggestBox(Map<String, Object> config) {
        super(config);
    }

    /**
     * Selectionne la valeur donnée
     */
    @Override
    protected void setComponentValue(String value, boolean fireEvents) {
        getTextBox().setValue(value, fireEvents);
    }

    /**
     * Retourne la valeur selectionnée
     */
    @Override
    protected String getComponentValue() {
        return getTextBox().getValue();
    }

    @Override
    public void addItem(String item) {
        super.addItem(item, item);
    }

    @Override
    public void insertItem(String item, int index) {
        super.insertItem(item, item, index);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setItems(Iterable<String> items) {
        super.setItems(items);
    }

    @Override
    protected String generateValueFromItem(String item, String value, Integer index) {
        return item;
    }
}
