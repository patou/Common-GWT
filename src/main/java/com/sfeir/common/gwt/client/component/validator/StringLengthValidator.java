package com.sfeir.common.gwt.client.component.validator;

import java.util.Map;

/**
 * 
 * Valide la longeur d'une chaine de caractère
 * @author sfeir
 */
public class StringLengthValidator extends Validator<String> {
    private String minLengthErrorText = messages.minLengthErrorText();
    private String maxLengthErrorText = messages.maxLengthErrorText();
    private Integer minLength = null;
    private Integer maxLength = null;

    public StringLengthValidator(Integer maxLength) {
        setMaxLength(maxLength);
    }

    public StringLengthValidator(Integer maxLength, String maxLengthErrorText) {
        setMaxLength(maxLength);
        setMaxLengthErrorText(maxLengthErrorText);
    }

    public StringLengthValidator(Integer minLength, Integer maxLength) {
        setMaxLength(maxLength);
        setMinLength(minLength);
    }
    
    public StringLengthValidator(Integer minLength, Integer maxLength, String minLengthErrorText, String maxLengthErrorText) {
        setMaxLength(maxLength);
        setMinLength(minLength);
        setMaxLengthErrorText(maxLengthErrorText);
        setMinLengthErrorText(minLengthErrorText);
    }

    @Override
    public void validate(ValidatorEvent<String> event) {
        String value = event.getValue();
        if (value != null && value.length() > 0) {
            if (minLength != null && value.length() < minLength)
                event.addError(minLengthErrorText.replace("{0}", minLength.toString()));
            if (maxLength != null && value.length() > maxLength)
                event.addError(maxLengthErrorText.replace("{0}", maxLength.toString()));
        }
    }

    @Override
    public void setConfig(Map<String, Object> config) {
        if (config.containsKey("maxLength")) {
            setMaxLength((Integer) config.get("maxLength"));
        }
        if (config.containsKey("minLength")) {
            setMinLength((Integer) config.get("minLength"));
        }
        if (config.containsKey("maxLengthErrorText")) {
            setMaxLengthErrorText((String) config.get("maxLengthErrorText"));
        }
        if (config.containsKey("minLengthErrorText")) {
            setMinLengthErrorText((String) config.get("minLengthErrorText"));
        }
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLengthErrorText(String maxLengthErrorText) {
        this.maxLengthErrorText = maxLengthErrorText;
    }

    public String getMaxLengthErrorText() {
        return maxLengthErrorText;
    }

    public void setMinLengthErrorText(String minLengthErrorText) {
        this.minLengthErrorText = minLengthErrorText;
    }

    public String getMinLengthErrorText() {
        return minLengthErrorText;
    }

}
