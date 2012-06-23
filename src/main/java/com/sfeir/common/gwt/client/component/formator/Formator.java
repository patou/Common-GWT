package com.sfeir.common.gwt.client.component.formator;

import java.util.Map;

import com.sfeir.common.gwt.client.component.HasConfig;


/**
 * 
 * Classe Abstraite pour créer un formateur
 * Il faut réécrire une des trois méthodes format
 * @author sfeir
 */
public abstract class Formator<V> implements FormatorHandler<V>, HasConfig {
    /**
     * Methode format de base
     * @param event Evénement du formateur (avec la methode getValue et setValue pour récuppérer la valeur)
     */
    public void format(FormatorEvent<V> event) {
        event.setValue(format(event.getValue(), event));
    }
    
    /**
     * Méthode qui prend la valeur en paramètre avec l'évenement
     * @param value
     * @param event
     * @return
     */
    public V format(V value, FormatorEvent<V> event) {
        return format(value);
    }
    
    /**
     * Méthode qui prend la valeur en paramètre et retourne la valeur formaté
     * @param value
     * @return
     */
    public V format(V value) {
        return value;
    }
    
    /**
     * Configuration du Formateur
     */
    public void setConfig(Map<String, Object> config) {
        
    }
    
    public Formator() {
    }
    
    public Formator(Map<String,Object> config) {
        setConfig(config);
    }
}
