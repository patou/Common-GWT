package com.sfeir.common.gwt.client.component.validator;

import java.util.Map;

/**
 * 
 * Valide qu'un champs n'est composé que de caractères numérique
 * @author sfeir
 */
public class NumericValidator extends Validator<String> {
    private String numericErrorText = messages.numericErrorText();
    
    public NumericValidator() {
    }
    
    public NumericValidator(String numericErrorText) {
        super();
        this.numericErrorText = numericErrorText;
    }

    @Override
    public String validate(String value) {
        if (value == null) 
            return null;
        if (!value.matches("^[0-9]*$"))
            return numericErrorText; 
        return null;
    }
    
    @Override
    public void setConfig(Map<String, Object> config) {
        if (config.containsKey("numericErrorText")) {
            setAlphaErrorText((String) config.get("numericErrorText"));
        }
    }

    public void setAlphaErrorText(String mandatoryErrorText) {
        this.numericErrorText = mandatoryErrorText;
    }

    public String getAlphaErrorText() {
        return numericErrorText;
    }

}
