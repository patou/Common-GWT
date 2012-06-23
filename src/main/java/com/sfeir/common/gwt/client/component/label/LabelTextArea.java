package com.sfeir.common.gwt.client.component.label;

import java.util.Map;

import com.google.gwt.user.client.ui.TextArea;
import com.sfeir.common.gwt.client.component.label.base.LabelTextBoxBase;


/**
 * Champs de text multiligne (TextArea) avec un label
 * 
 * @author sfeir
 */
public class LabelTextArea extends LabelTextBoxBase<TextArea,String> {
    /**
     * 
     */
    public LabelTextArea() {
    }

    /**
     * @param text
     */
    public LabelTextArea(String text) {
        super(text);
    }

    /**
     * @param text
     * @param mandatory
     */
    public LabelTextArea(String text, boolean mandatory) {
        super(text, mandatory);
    }
    
    public LabelTextArea(Map<String,Object> config) {
        super(config);
    }

    /* (non-Javadoc)
     * @see com.sfeir.gwt.webding.client.component.LabelComponentBase#clearComponent()
     */
    @Override
    public void clearComponent() {
        getComponent().setText("");
    }
    
    @Override
    public String defaultValue() {
        return "";
    }

    @Override
    public void setConfig(Map<String, Object> config) {
        super.setConfig(config);
        if (config.containsKey("characterWidth")) {
            setCharacterWidth((Integer) config.get("characterWidth"));
        }
        if (config.containsKey("visibleLines")) {
            setVisibleLines((Integer) config.get("visibleLines"));
        }
    }
    
    /* (non-Javadoc)
     * @see com.sfeir.gwt.webding.client.component.LabelComponentBase#createComponent()
     */
    @Override
    protected TextArea createComponent() {
        return new TextArea();
    }
    
    /**
     * Gets the requested width of the text box (this is not an exact value, as
     * not all characters are created equal).
     * 
     * @return the requested width, in characters
     */
    public int getCharacterWidth() {
      return getComponent().getCharacterWidth();
    }

    /**
     * Gets the number of text lines that are visible.
     * 
     * @return the number of visible lines
     */
    public int getVisibleLines() {
      return getComponent().getVisibleLines();
    }

    /**
     * Sets the requested width of the text box (this is not an exact value, as
     * not all characters are created equal).
     * 
     * @param width the requested width, in characters
     */
    public void setCharacterWidth(int width) {
      getComponent().setCharacterWidth(width);
    }

    /**
     * Sets the number of text lines that are visible.
     * 
     * @param lines the number of visible lines
     */
    public void setVisibleLines(int lines) {
      getComponent().setVisibleLines(lines);
    }


}
