package com.sfeir.common.gwt.client.component.validator;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.sfeir.common.gwt.client.component.HasConfig;
import com.sfeir.common.gwt.client.component.message.ValidatorConstants;

/**
 * 
 * Classe Abstraite pour créer un validateur Il faut réimplémenter une des trois méthodes validate
 * 
 * @author sfeir
 */
public class Validator<V> implements ValidatorHandler<V>, HasConfig {
    protected static ValidatorConstants messages = GWT.create(ValidatorConstants.class);

    public Validator() {
    }

    /**
     * Constructeur avec une map de configuration
     * 
     * @param config
     */
    public Validator(Map<String, Object> config) {
	setConfig(config);
    }

    /**
     * Valide une valeur, ajoute une erreur avec la methode event.addError();
     * 
     * @param event
     *            évènement de validation (getValue et setValue)
     */
    public void validate(ValidatorEvent<V> event) {
	event.addError(validate(event.getValue(), event));
    }

    /**
     * Valide une valeur et retourne le message d'erreur ou null sinon
     * 
     * @param value
     * @param event
     * @return
     */
    public String validate(V value, ValidatorEvent<V> event) {
	return validate(value);
    }

    /**
     * Valide une valeur et retourne le message d'erreur ou null sinon
     * 
     * @param value
     * @return
     */
    public String validate(V value) {
	return null;
    }

    /**
     * Configuration du Validateur
     */
    public void setConfig(Map<String, Object> config) {

    }
}
