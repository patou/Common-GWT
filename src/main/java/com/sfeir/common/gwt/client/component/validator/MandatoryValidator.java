package com.sfeir.common.gwt.client.component.validator;

import java.util.Collection;
import java.util.Map;

/**
 * 
 * Valide qu'un champs a une valeur
 * @author sfeir
 */
public class MandatoryValidator<V> extends Validator<V> {
    private String mandatoryErrorText = messages.mandatoryErrorText();
    
    public MandatoryValidator() {
    }
    
    public MandatoryValidator(String mandatoryErrorText) {
        super();
        this.mandatoryErrorText = mandatoryErrorText;
    }

    @Override
    public String validate(V value) {
        if (value == null) 
            return mandatoryErrorText;
        if (value instanceof String && ((String) value).length() == 0)
            return mandatoryErrorText;
        if (value instanceof Integer && ((Integer) value) == 0)
            return mandatoryErrorText;
        if (value instanceof Boolean && !((Boolean) value))
            return mandatoryErrorText;
        if (value instanceof Collection<?> && ((Collection<?>) value).size() == 0)
            return mandatoryErrorText;
        return null;
    }
    
    @Override
    public void setConfig(Map<String, Object> config) {
        if (config.containsKey("mandatoryErrorText")) {
            setMandatoryErrorText((String) config.get("mandatoryErrorText"));
        }
    }

    public void setMandatoryErrorText(String mandatoryErrorText) {
        this.mandatoryErrorText = mandatoryErrorText;
    }

    public String getMandatoryErrorText() {
        return mandatoryErrorText;
    }

}
