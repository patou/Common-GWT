package com.google.gwt.user.datepicker.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ValueListBox;

/**
 * A MonthSelector with a first line with a Year selector.
 * It's a sub class using by the component DateYearPicker
 * 
 * It was in the google package because of limitation with package visibility
 */
@SuppressWarnings(/*Use of deprecated date*/"deprecation")
public final class MonthYearSelector extends MonthSelector {

    private Integer maxYears = new Date().getYear() + 1900;
    private Integer minYears = new Date().getYear() - 110 + 1900;
    private PushButton backwards;
    private PushButton forwards;
    private PushButton yearBackwards;
    private PushButton yearForwards;
    private ValueListBox<Integer> years = new ValueListBox<Integer>(new AbstractRenderer<Integer>() {

	@Override
	public String render(Integer object) {
	    if (object == null)
		return "";
	    return object.toString();
	}
    });
    private Grid grid;
    private DateTimeFormat format = DateTimeFormat.getFormat("MMMM");

    /**
     * Constructor.
     */
    public MonthYearSelector() {
    }

    @Override
    protected void refresh() {
	Date currentMonth = getModel().getCurrentMonth();
	years.setValue(currentMonth.getYear() + 1900);
	String formattedMonth = format.format(currentMonth);
	grid.setText(1, 1, formattedMonth);
    }

    @Override
    protected void setup() {
	// Set up backwards.
	backwards = new PushButton();
	backwards.addClickHandler(new ClickHandler() {
	    public void onClick(ClickEvent event) {
		addMonths(-1);
	    }
	});
	backwards.getUpFace().setHTML("&laquo;");
	backwards.setStyleName(css().previousButton());

	yearBackwards = new PushButton();
	yearBackwards.getUpFace().setHTML("&laquo;&laquo;");
	yearBackwards.setStyleName(css().previousButton());
	yearBackwards.addClickHandler(new ClickHandler() {
	    public void onClick(ClickEvent event) {
		addMonths(-12);
	    }
	});

	forwards = new PushButton();
	forwards.getUpFace().setHTML("&raquo;");
	forwards.setStyleName(css().nextButton());
	forwards.addClickHandler(new ClickHandler() {
	    public void onClick(ClickEvent event) {
		addMonths(+1);
	    }
	});

	yearForwards = new PushButton();
	yearForwards.getUpFace().setHTML("&raquo;&raquo;");
	yearForwards.setStyleName(css().nextButton());
	yearForwards.addClickHandler(new ClickHandler() {
	    public void onClick(ClickEvent event) {
		addMonths(+12);
	    }
	});

	createYearsList();
	years.addValueChangeHandler(new ValueChangeHandler<Integer>() {

	    @Override
	    public void onValueChange(ValueChangeEvent<Integer> event) {
		Date date = getModel().getCurrentMonth();
		date.setYear(event.getValue() - 1900);
		getModel().setCurrentMonth(date);
		refreshAll();
	    }
	});
//	years.setWidth("100%");
	years.getElement().getStyle().setBorderStyle(BorderStyle.NONE);
	years.addStyleName(css().month());

	// Set up grid.
	grid = new Grid(2, 3);
	grid.setWidget(0, 0, yearBackwards);
	grid.setWidget(0, 1, years);
	grid.setWidget(0, 2, yearForwards);
	grid.setWidget(1, 0, backwards);
	grid.setWidget(1, 2, forwards);

	CellFormatter formatter = grid.getCellFormatter();
	formatter.setStyleName(0, 1, css().month());
	formatter.setWidth(0, 0, "1");
	formatter.setWidth(0, 1, "100%");
	formatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
	formatter.setWidth(0, 2, "1");
	formatter.setWidth(1, 0, "1");
	formatter.setWidth(1, 1, "100%");
	formatter.setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_CENTER);
	formatter.setWidth(1, 2, "1");
	grid.setStyleName(css().monthSelector());
	initWidget(grid);
    }

    private void createYearsList() {
	ArrayList<Integer> yearsList = new ArrayList<Integer>(110);
	for (Integer cy = maxYears; cy > minYears; cy--) {
	    yearsList.add(cy);
	}
	years.setAcceptableValues(yearsList);
    }
    
    public void setYearsList(Collection<Integer> liste) {
	years.setAcceptableValues(liste);
	refreshAll();
    }
    
    /**
     * Set the max years to display in the years list
     * @param maxYears
     */
    public void setMaxYears(Integer maxYears) {
	this.maxYears = maxYears;
	createYearsList();
    }
    
    /**
     * Set the min years to display in the years list
     * @param minYears
     */
    public void setMinYears(Integer minYears) {
	this.minYears = minYears;
	createYearsList();
    }
    
    public Integer getMinYears() {
	return minYears;
    }
    
    public Integer getMaxYears() {
	return maxYears;
    }

}
