package com.sfeir.common.gwt.client.component.formator;

import java.util.Map;


/**
 * 
 * Replace le texte par une regex
 * @author sfeir
 */
public class RegexFormator extends Formator<String> {
    private String regex;
    private String replacement;

    
    public RegexFormator(String regex, String replacement) {
        super();
        this.regex = regex;
        this.replacement = replacement;
    }

    @Override
    public String format(String value) {
        return value.replaceAll(getRegex(),getReplacement());
    }
    
    @Override
    public void setConfig(Map<String, Object> config) {
        super.setConfig(config);
        if (config.containsKey("regex"))
            setRegex((String) config.get("regex"));
        if (config.containsKey("replacement"))
            setReplacement((String) config.get("replacement"));
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
