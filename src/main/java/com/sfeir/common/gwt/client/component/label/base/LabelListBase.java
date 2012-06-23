package com.sfeir.common.gwt.client.component.label.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.ui.Widget;
import com.sfeir.common.gwt.client.component.label.listitem.ListItem;

/**
 * 
 * Classe de base pour les composants gérant une liste de donnée (ListBox,SuggestBox,ListCheckBox,ListRadioButton)
 * @param <K> Type de la clé de la liste
 * @author sfeir
 */
public abstract class LabelListBase<K,T extends Widget,V> extends LabelComponentBase<T, V> {

    private static final int INSERT_AT_END = -1;
    private boolean displayValue = false;
    private String displayValueSeparator = ": ";
    private Map<String,K> itemsValue = new HashMap<String, K>();
    private List<String> defaultValueList = new ArrayList<String>();

    public LabelListBase() {
        super();
    }

    public LabelListBase(String text) {
        super(text);
    }

    public LabelListBase(String text, boolean mandatory) {
        super(text, mandatory);
    }

    public LabelListBase(Map<String, Object> config) {
        super(config);
    }
    
    @SuppressWarnings({ "deprecation", "unchecked" })
    @Override
    public void setConfig(Map<String, Object> config) {
        super.setConfig(config);
        if (config.containsKey("items")) {
            Object items = config.get("items");
            if (items instanceof Map) {
                setItems((Map<K,String>) items);
            }
            if (items instanceof Iterable) {
                setItems((Iterable<String>) items);
            }
        }
        if (config.containsKey("defaultItem")) {
            addDefaultItem((String) config.get("defaultItem"));
        }
    }

    /**
     * Ajoute une valeur par défaut au début de la liste
     */
    public void addDefaultItem() {
        addDefaultItem("Veuillez sélectionner ...");
    }

    /**
     * Ajoute une valeur par défaut personnalisé au début de la liste
     * @param text
     */
    public void addDefaultItem(String text) {
        addIndefaultvalueList(text);
        insertItemValue(text, text, 0);
    }

    protected void addIndefaultvalueList(String text) {
        getDefaultValueList().add(text);
    }

    public void addItem(String item, K value) {
        insertItem(item, value, INSERT_AT_END);
    }
    
    public void add(ListItem<K> item) {
    	addItem(item.getText(),item.getValue());
    }

    /**
     * Si aucune valeur n'est définie, il ne sera pas possible de connaitre la valeur de l'item
     * @deprecated Utilisez addItem(item, value)
     * @param item
     * @param index
     */
    @Deprecated
    public void addItem(String item) {
        insertItem(item, null, INSERT_AT_END);
    }

    /**
     * Si aucune valeur n'est définie, il ne sera pas possible de connaitre la valeur de l'item
     * @deprecated Utilisez insertItem(item, value, index)
     * @param item
     * @param index
     */
    @Deprecated
    public void insertItem(String item, int index) {
        insertItem(item, null, index);
    }

    
    public void insertItem(String item, K value, Integer index) {
        if (displayValue) {
            item = value + getDisplayValueSeparator() + item;
        }
        String itemValue;
        if (value != null) {
            itemValue = value.toString();
        }
        else {
            itemValue = item;
        }
        itemsValue.put(itemValue, value);
        insertItemValue(item, itemValue, index);
    }

    /**
     * Si l'item n'a pas de valeur par défaut, essaye d'en créer une
     * @param item 
     * @param value Valeur par défaut
     * @param index Index courant de l'item
     * @return
     */
    protected K generateValueFromItem(String item, K value, Integer index) {
        return null;
    }
    
    public abstract Integer getItemCount(); 
    
    protected abstract void insertItemValue(String item, String value, int index);
    
    @Deprecated
    public void setItems(Iterable<String> items) {
        removeAll();
        addItems(items);
    }

    public void setItems(Map<K,String> items) {
        removeAll();
        addItems(items);
    }

    public void addItems(Iterable<String> items) {
        for (String item : items) {
            insertItem(item, null, INSERT_AT_END);
        }
    }

    public void addItems(Map<K,String> items) {
        for (Entry<K, String> item : items.entrySet()) {
            insertItem(item.getValue(), item.getKey(), INSERT_AT_END);
        }
    }

    public void removeAll() {
        itemsValue.clear();
    }
    
    protected K getItemValue(String value) {
        if (getDefaultValueList().contains(value)) { // Valeur par défaut retourne null
            return null;
        }
        if (itemsValue.containsKey(value)) { //Si la valeur existe
            return itemsValue.get(value); 
        }
        return generateValueFromItem(value, null, getItemIndex(value));
    }
    
    protected abstract Integer getItemIndex(String value);
    
    /**
     * Affiche la valeur devant libellée
     * Définir avant d'ajouter les éléments dans la liste
     * @param displayValue
     */
    public void setDisplayValue(boolean displayValue) {
        this.displayValue = displayValue;
    }

    public boolean isDisplayValue() {
        return displayValue;
    }

    public void setDisplayValueSeparator(String displayValueSeparator) {
        this.displayValueSeparator = displayValueSeparator;
    }

    public String getDisplayValueSeparator() {
        return displayValueSeparator;
    }

    public void setDefaultValueList(List<String> defaultValueList) {
        this.defaultValueList = defaultValueList;
    }

    public List<String> getDefaultValueList() {
        return defaultValueList;
    }

}
