package com.sfeir.common.gwt.client.component.label;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.ListBox;
import com.sfeir.common.gwt.client.component.label.base.LabelListBoxBase;


/**
 * 
 * Liste a choix multiples
 * @author sfeir
 */
public class LabelMultiListBox<K> extends LabelListBoxBase<K,List<K>> {
    public LabelMultiListBox() {
        super();
    }
    
    public LabelMultiListBox(String label) {
        super(label);
    }
    
    public LabelMultiListBox(String label, Boolean mandatory) {
        super(label, mandatory);
    }
    
    public LabelMultiListBox(Map<String, Object> config) {
        super(config);
    }
    
    
    /**
     * Création du composant avec la possibilité d'avoir des choix multiples
     */
    @Override
    protected ListBox createComponent() {
        ListBox listBox = new ListBox(true);
        listBox.addChangeHandler(this);
        return listBox;
    }
    
    /**
     * Valeur par défaut (liste vide)
     */
    @Override
    public List<K> defaultValue() {
        return new ArrayList<K>();
    }
    
    /**
     * réinistialisation du formulaire
     */
    @Override
    public void clearComponent() {
        setComponentValue(null, false);
    }
    
    /**
     * Selectionne tous les valeurs contenue dans la liste
     */
    @Override
    protected void setComponentValue(List<K> values, boolean fireEvents) {
        ListBox component = getComponent();
        int l = component.getItemCount();
        for (int i = 0; i < l; i++) {
            String value = component.getValue(i);
            component.setItemSelected(i, (values != null && values.contains(getItemValue(value))));
        }
    }

    /**
     * Retourne une liste de toutes les valeurs selectionnées
     */
    @Override
    protected List<K> getComponentValue() {
        List<K> values = new ArrayList<K>();
        ListBox component = getComponent();
        int l = component.getItemCount();
        for (int i = 0; i < l; i++) {
            if (component.isItemSelected(i))
                values.add(getItemValue(component.getValue(i)));
        }
        return values;
    }

    public void insertItem(String item, String value, int index) {
    }
}
