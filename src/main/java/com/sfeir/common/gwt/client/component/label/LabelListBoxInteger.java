package com.sfeir.common.gwt.client.component.label;

import java.util.Map;

/**
 * 
 * Champs ListBox avec une clé numérique
 * @author sfeir
 */
public class LabelListBoxInteger extends LabelListBox<Integer> {

    public LabelListBoxInteger() {
        super();
    }
    
    public LabelListBoxInteger(String label) {
        super(label);
    }
    
    public LabelListBoxInteger(String label, Boolean mandatory) {
        super(label, mandatory);
    }
    
    public LabelListBoxInteger(Map<String, Object> config) {
        super(config);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void addItem(String item) {
        super.addItem(item);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void insertItem(String item, int index) {
        super.insertItem(item, index);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void setItems(Iterable<String> items) {
        super.setItems(items);
    }
    
    @Override
    protected Integer generateValueFromItem(String item, Integer value, Integer index) {
        return index;
    }
}
