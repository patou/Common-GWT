package com.sfeir.common.gwt.client.component.label;

import java.util.Date;
import java.util.Map;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.sfeir.common.gwt.client.component.label.base.LabelComponentBase;


/**
 * 
 * Champs de selection d'une date
 * @author sfeir
 */
public class LabelDateBox extends LabelComponentBase<DateBox, Date> {

    /**
     * 
     */
    public LabelDateBox() {
    }

    /**
     * @param text
     */
    public LabelDateBox(String text) {
        super(text);
    }

    /**
     * @param text
     * @param mandatory
     */
    public LabelDateBox(String text, boolean mandatory) {
        super(text, mandatory);
    }
    
    public LabelDateBox(Map<String,Object> config) {
        super(config);
    }
    
    @Override
    public void clearComponent() {
        getComponent().setValue(defaultValue());
    }

    @Override
    public Date defaultValue() {
        return new Date();
    }
    
    @Override
    protected DateBox createComponent() {
        return new DateBox();
    }
    
    
    @Override
    public void setConfig(Map<String, Object> config) {
        super.setConfig(config);
        if (config.containsKey("enabled")) {
            setEnabled((Boolean) config.get("enabled"));
        }
        if (config.containsKey("title")) {
            setTitle((String) config.get("title"));
        }
        if (config.containsKey("tabIndex")) {
            setTabIndex((Integer) config.get("tabIndex"));
        }
        if (config.containsKey("format")) {
            setFormat((Format) config.get("format"));
        }
    }

    /**
     * @return
     * @see com.google.gwt.user.datepicker.client.DateBox#getDatePicker()
     */
    public DatePicker getDatePicker() {
        return getComponent().getDatePicker();
    }

    /**
     * @return
     * @see com.google.gwt.user.datepicker.client.DateBox#getFormat()
     */
    public Format getFormat() {
        return getComponent().getFormat();
    }

    /**
     * @return
     * @see com.google.gwt.user.datepicker.client.DateBox#getTabIndex()
     */
    public int getTabIndex() {
        return getComponent().getTabIndex();
    }

    /**
     * @return
     * @see com.google.gwt.user.datepicker.client.DateBox#getTextBox()
     */
    public TextBox getTextBox() {
        return getComponent().getTextBox();
    }

    /**
     * @return
     * @see com.google.gwt.user.client.ui.UIObject#getTitle()
     */
    public String getTitle() {
        return getComponent().getTitle();
    }

    /**
     * 
     * @see com.google.gwt.user.datepicker.client.DateBox#hideDatePicker()
     */
    public void hideDatePicker() {
        getComponent().hideDatePicker();
    }

    /**
     * @return
     * @see com.google.gwt.user.datepicker.client.DateBox#isDatePickerShowing()
     */
    public boolean isDatePickerShowing() {
        return getComponent().isDatePickerShowing();
    }

    /**
     * @param enabled
     * @see com.google.gwt.user.datepicker.client.DateBox#setEnabled(boolean)
     */
    public void setEnabled(boolean enabled) {
        getComponent().setEnabled(enabled);
    }

    /**
     * @param format
     * @see com.google.gwt.user.datepicker.client.DateBox#setFormat(com.google.gwt.user.datepicker.client.DateBox.Format)
     */
    public void setFormat(Format format) {
        getComponent().setFormat(format);
    }

    /**
     * @param index
     * @see com.google.gwt.user.datepicker.client.DateBox#setTabIndex(int)
     */
    public void setTabIndex(int index) {
        getComponent().setTabIndex(index);
    }

    /**
     * @param title
     * @see com.google.gwt.user.client.ui.UIObject#setTitle(java.lang.String)
     */
    public void setTitle(String title) {
        getComponent().setTitle(title);
    }

    /**
     * 
     * @see com.google.gwt.user.datepicker.client.DateBox#showDatePicker()
     */
    public void showDatePicker() {
        getComponent().showDatePicker();
    }
    
    

}
