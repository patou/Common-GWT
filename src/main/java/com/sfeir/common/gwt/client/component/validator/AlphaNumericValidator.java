package com.sfeir.common.gwt.client.component.validator;

import java.util.Map;

/**
 * 
 * Valide que les caractère alphabetique numérique
 * @author sfeir
 */
public class AlphaNumericValidator extends Validator<String> {
    private String alphaNumericErrorText = messages.alphaNumericErrorText();
    
    public AlphaNumericValidator() {
    }
    
    public AlphaNumericValidator(String alphaNumericErrorText) {
        super();
        this.alphaNumericErrorText = alphaNumericErrorText;
    }

    @Override
    public String validate(String value) {
        if (value == null) 
            return null;
        if (!value.matches("^[a-zA-Z0-9]*$"))
            return alphaNumericErrorText; 
        return null;
    }
    
    @Override
    public void setConfig(Map<String, Object> config) {
        if (config.containsKey("alphaNumericErrorText")) {
            setAlphaNumericErrorText((String) config.get("alphaNumericErrorText"));
        }
    }

    public void setAlphaNumericErrorText(String mandatoryErrorText) {
        this.alphaNumericErrorText = mandatoryErrorText;
    }

    public String getAlphaNumericErrorText() {
        return alphaNumericErrorText;
    }

}
