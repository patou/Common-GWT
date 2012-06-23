package com.sfeir.common.gwt.client.component.label;

import java.util.Map;

/**
 * 
 * Champs ListBox avec une clé alphabetique
 * @author sfeir
 */

public class LabelListBoxString extends LabelListBox<String> {

    public LabelListBoxString() {
        super();
    }
    
    public LabelListBoxString(String label) {
        super(label);
    }
    
    public LabelListBoxString(String label, Boolean mandatory) {
        super(label, mandatory);
    }
    
    public LabelListBoxString(Map<String, Object> config) {
        super(config);
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
