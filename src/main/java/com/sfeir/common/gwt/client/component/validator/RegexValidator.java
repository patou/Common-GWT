package com.sfeir.common.gwt.client.component.validator;

import java.util.Map;

/**
 * 
 * Valide un champs selon une regex
 * @author sfeir
 */
public class RegexValidator extends Validator<String> {
    private String regexErrorText = messages.regexErrorText();
    private String regex;
    public RegexValidator(String regex) {
        setRegex(regex);
    }
    
    public RegexValidator(String regex, String regexErrorText) {
        this(regex);
        this.regexErrorText = regexErrorText;
    }

    @Override
    public String validate(String value) {
        if (value == null) 
            return null;
        if (!value.matches(regex))
            return regexErrorText; 
        return null;
    }
    
    @Override
    public void setConfig(Map<String, Object> config) {
        if (config.containsKey("regexErrorText")) {
            setRegexErrorText((String) config.get("regexErrorText"));
        }
        if (config.containsKey("regex")) {
            setRegex((String) config.get("regex"));
        }
    }

    public void setRegexErrorText(String mandatoryErrorText) {
        this.regexErrorText = mandatoryErrorText;
    }

    public String getRegexErrorText() {
        return regexErrorText;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

}
