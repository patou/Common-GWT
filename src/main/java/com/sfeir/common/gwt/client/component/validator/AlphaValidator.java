package com.sfeir.common.gwt.client.component.validator;

import java.util.Map;

/**
 * 
 * Valide que les caractères alphabétique
 * @author sfeir
 */
public class AlphaValidator extends Validator<String> {
    private String alphaErrorText = messages.alphaErrorText();
    
    public AlphaValidator() {
    }
    
    public AlphaValidator(String alphaErrorText) {
        super();
        this.alphaErrorText = alphaErrorText;
    }

    @Override
    public String validate(String value) {
        if (value == null) 
            return null;
        if (!value.matches("^[a-zA-Z]*$"))
            return alphaErrorText; 
        return null;
    }
    
    @Override
    public void setConfig(Map<String, Object> config) {
        if (config.containsKey("alphaErrorText")) {
            setAlphaErrorText((String) config.get("alphaErrorText"));
        }
    }

    public void setAlphaErrorText(String mandatoryErrorText) {
        this.alphaErrorText = mandatoryErrorText;
    }

    public String getAlphaErrorText() {
        return alphaErrorText;
    }

}
