package com.sfeir.common.gwt.client.component.label;

import java.util.Map;

import com.google.gwt.user.client.ui.SuggestBox;
import com.sfeir.common.gwt.client.component.label.base.LabelSuggestBoxBase;


/**
 * Champ a valeur multiple avec un champ de recherche 
 * @author sfeir
 */
public class LabelComboBox<K> extends LabelSuggestBoxBase<K,K> {
    /**
     * 
     */
    public LabelComboBox() {
    }

    /**
     * @param label
     */
    public LabelComboBox(String label) {
        super(label);
    }

    /**
     * @param label
     * @param mandatory
     */
    public LabelComboBox(String label, boolean mandatory) {
        super(label, mandatory);
    }

    public LabelComboBox(Map<String, Object> config) {
        super(config);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected SuggestBox createComponent() {
        SuggestBox createComponent = super.createComponent();
        ((SuggestBoxOracle) createComponent.getSuggestOracle()).setDefaultSuggestionList(true);
        return createComponent;
    }
    
    @Override
    protected K getComponentValue() {
        String value = getSuggestOracle().getItemValue(getTextBox().getValue());
        return getItemValue(value);
    }
}
