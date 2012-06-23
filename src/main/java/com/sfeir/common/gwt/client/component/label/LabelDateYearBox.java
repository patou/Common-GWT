package com.sfeir.common.gwt.client.component.label;

import java.util.Collection;
import java.util.Map;

import com.google.gwt.user.datepicker.client.DateBox;
import com.sfeir.common.gwt.client.component.DateYearBox;


/**
 * 
 * Champs de selection d'une date avec la selection d'une année
 * @author sfeir
 */
public class LabelDateYearBox extends LabelDateBox {

    /**
     * 
     */
    public LabelDateYearBox() {
    }

    /**
     * @param text
     */
    public LabelDateYearBox(String text) {
        super(text);
    }

    /**
     * @param text
     * @param mandatory
     */
    public LabelDateYearBox(String text, boolean mandatory) {
        super(text, mandatory);
    }
    
    @Override
    protected DateBox createComponent() {
        return new DateYearBox();
    }
    
    public DateYearBox getDateYearComponent() {
	return (DateYearBox) getComponent();
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public void setConfig(Map<String, Object> config) {
        super.setConfig(config);
        if (config.containsKey("maxYears")) {
            setMaxYears((Integer) config.get("maxYears"));
        }
        if (config.containsKey("minYears")) {
            setMinYears((Integer) config.get("minYears"));
        }
        if (config.containsKey("yearsList")) {
            setYearsList((Collection<Integer>) config.get("yearsList"));
        }
    }
    
    /**
     * Set the list of years to display
     * You can use the maxYears and minYears to set the bounds of the display list
     * @param liste
     */
    public void setYearsList(Collection<Integer> liste) {
	getDateYearComponent().setYearsList(liste);
    }
    
    /**
     * Set the max years to display in the years list
     * @param maxYears
     */
    public void setMaxYears(Integer maxYears) {
	getDateYearComponent().setMaxYears(maxYears);
    }
    
    /**
     * Set the min years to display in the years list
     * @param minYears
     */
    public void setMinYears(Integer minYears) {
	getDateYearComponent().setMinYears(minYears);
    }
    
    public Integer getMinYears() {
	return getDateYearComponent().getMinYears();
    }
    

    public Integer getMaxYears() {
	return getDateYearComponent().getMaxYears();
    }
}
