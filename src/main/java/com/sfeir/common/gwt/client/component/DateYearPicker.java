package com.sfeir.common.gwt.client.component;

import java.util.Collection;

import com.google.gwt.user.datepicker.client.CalendarModel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DefaultCalendarView;
import com.google.gwt.user.datepicker.client.MonthYearSelector;

/**
 * This {@link DatePicker} allow to easily change the year with to button next and previous year and a drop down menu to select directly a year.
 * @see DatePicker
 *
 */
public class DateYearPicker extends DatePicker {
    public DateYearPicker() {
	super(new MonthYearSelector(), new DefaultCalendarView(), new CalendarModel());
    }
    
    /**
     * Set the list of years to display
     * You can use the maxYears and minYears to set the bounds of the display list
     * @param liste
     */
    public void setYearsList(Collection<Integer> liste) {
	getMonthYearSelector().setYearsList(liste);
    }
    
    /**
     * Set the max years to display in the years list
     * @param maxYears
     */
    public void setMaxYears(Integer maxYears) {
	getMonthYearSelector().setMaxYears(maxYears);
    }
    
    /**
     * Set the min years to display in the years list
     * @param minYears
     */
    public void setMinYears(Integer minYears) {
	getMonthYearSelector().setMinYears(minYears);
    }
    
    public Integer getMinYears() {
	return getMonthYearSelector().getMinYears();
    }
    

    public Integer getMaxYears() {
	return getMonthYearSelector().getMaxYears();
    }
    
    protected MonthYearSelector getMonthYearSelector() {
	return (MonthYearSelector) getMonthSelector();
    }
}
