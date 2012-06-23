package com.sfeir.common.gwt.client.component.label;

import java.util.Map;

import com.google.gwt.user.client.ui.ListBox;
import com.sfeir.common.gwt.client.component.label.base.LabelListBoxBase;



/**
 * 
 * Liste a choix multiples
 * @param K Type de la clé de la liste de valeur
 * @author sfeir
 */
public class LabelListBox<K> extends LabelListBoxBase<K,K> {
    public LabelListBox() {
        super();
    }
    
    public LabelListBox(String label) {
        super(label);
    }
    
    public LabelListBox(String label, Boolean mandatory) {
        super(label, mandatory);
    }
    
    public LabelListBox(Map<String, Object> config) {
        super(config);
    }
    
    @Override
    public K defaultValue() {
        return null; 
    }
    
    @Override
    public void clearComponent() {
        getComponent().setItemSelected(0, true);
    }
    
    /**
     * Selectionne la valeur donnée    
     */
    @Override
    protected void setComponentValue(K value, boolean fireEvents) {
        if (value != null) {
            int index = searchValue(value.toString());
            if (index >= 0) {
                selectIndex(index);
            }
        }
    }

    /**
     * Retourne la valeur selectionnée
     */
    @Override
    protected K getComponentValue() {
        ListBox component = getComponent();
        int index = component.getSelectedIndex();
        String value = component.getValue(index);
        return getItemValue(value);
    }
}
