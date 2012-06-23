package com.sfeir.common.gwt.client.component.validator;

import java.util.Date;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;

/**
 * 
 * Valide que la date est bien inférieur à la date demandé
 * @author sfeir
 */
public class DateBeforeValidator extends Validator<Date> {
    private String dateBeforeErrorText = messages.dateBeforeErrorText();
    private Date before = new Date();
    
    public DateBeforeValidator() {
    }
    
    public DateBeforeValidator(Date before) {
	this.before = before;
    }
    
    public DateBeforeValidator(String dateBeforeErrorText) {
        super();
        this.dateBeforeErrorText = dateBeforeErrorText;
    }
    
    public DateBeforeValidator(Date before, String dateBeforeErrorText) {
        super();
        this.before = before;
        this.dateBeforeErrorText = dateBeforeErrorText;
    }

    @Override
    public String validate(Date value) {
        if (value == null) 
            return null;
        if (!value.before(before))
            return dateBeforeErrorText.replace("{0}", DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT).format(before)); 
        return null;
    }
    
    @Override
    public void setConfig(Map<String, Object> config) {
        if (config.containsKey("dateBeforeErrorText")) {
            setDateBeforeErrorText((String) config.get("dateBeforeErrorText"));
        }
    }

    public void setDateBeforeErrorText(String dateBeforeErrorText) {
        this.dateBeforeErrorText = dateBeforeErrorText;
    }

    public String getDateBeforeErrorText() {
        return dateBeforeErrorText;
    }

}
