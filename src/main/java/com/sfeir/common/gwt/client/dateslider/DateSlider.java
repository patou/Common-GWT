package com.sfeir.common.gwt.client.dateslider;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;

/**
 * TODO
 * @author sfeir
 *
 */
public class DateSlider extends Composite {

	private static DateSliderUiBinder uiBinder = GWT.create(DateSliderUiBinder.class);

	interface DateSliderUiBinder extends UiBinder<HTMLPanel, DateSlider> {
	}

	@UiField(provided=true)
	Element sliderbar = DOM.createDiv();
	@UiField
	HTMLPanel container;
	@UiField(provided=true)
	Element lefthandle = DOM.createDiv();
	@UiField(provided=true)
	Element righthandle = DOM.createDiv();
	@UiField(provided=true)
	Element shiftpanel = DOM.createDiv();
	@UiField
	DateSliderClientBundler res;

	Date beginDate;
	Date endDate;

	private final Integer beginYear;

	private final Integer endYear;
	private int sliderDayDivWidth = 1; // TODO
	private Date rightDate;
	private Date leftDate;
	Slider leftSlider;
	Slider rightSlider;
	Slider shiftSlider;
	Slider dateSlider;

	@SuppressWarnings("deprecation")
	@UiConstructor
	public DateSlider(Integer beginYear, Integer endYear) {
		this.beginYear = beginYear;
		this.endYear = endYear;
		leftSlider = new Slider(lefthandle);
		rightSlider = new Slider(righthandle);
		shiftSlider = new Slider(shiftpanel);
		dateSlider = new Slider(sliderbar);
		initWidget(uiBinder.createAndBindUi(this));
		res.style().ensureInjected();
		beginDate = new Date(beginYear - 1900, 1, 1, 0, 0, 0);
		endDate = new Date(endYear - 1900, 12, 31, 0, 0, 0);
		createSlider();
		Date now = new Date();
		Date first = new Date();
		CalendarUtil.addMonthsToDate(first, -3);
		centerSlider(now);
		setDates(first, now);
	}

	public void setDates(Date leftDate, Date rightDate) {
		this.leftDate = leftDate;
		this.rightDate = rightDate;
		positionHandler(lefthandle, leftDate);
		positionHandler(righthandle, rightDate);
		int leftDays = CalendarUtil.getDaysBetween(beginDate, leftDate);
		shiftpanel.getStyle().setLeft(sliderDayDivWidth * leftDays, Unit.PX);
		int days = CalendarUtil.getDaysBetween(leftDate, rightDate);
		shiftpanel.getStyle().setWidth(sliderDayDivWidth * days, Unit.PX);
		int offsetWidth = container.getOffsetWidth() / 2;
		sliderbar.getStyle().setLeft(0 - sliderDayDivWidth * (leftDays + days / 2) + offsetWidth, Unit.PX);
	}

	public void centerSlider(Date date) {
		int offsetWidth = container.getOffsetWidth() / 2;
		int days = CalendarUtil.getDaysBetween(beginDate, date);
		sliderbar.getStyle().setLeft(0 - sliderDayDivWidth * days + offsetWidth, Unit.PX);
	}

	private void createSlider() {
		int year = beginYear;
		int totalDays = 365;
		String[] mouths = LocaleInfo.getCurrentLocale().getDateTimeFormatInfo().monthsShort();
		while (year <= endYear) {
			boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
			int nbDays = isLeapYear ? 366 : 365;
			Element slideYear = DOM.createDiv();
			slideYear.setClassName(res.style().slideYear());
			slideYear.getStyle().setWidth(sliderDayDivWidth * nbDays, Unit.PX);
			slideYear.setInnerText(Integer.toString(year));
			int totalDaysMouth = 0;
			for (int i = 0; i < 12; i++) {
				Element slideMouth = DOM.createDiv();
				slideMouth.setClassName(res.style().slideMonth());
				int nbDaysMouth = mouthNdDay[i];
				if (i == 1 && isLeapYear) // February in Leap year
					nbDaysMouth = 29;
				slideMouth.getStyle().setWidth(sliderDayDivWidth * nbDaysMouth, Unit.PX);
				slideMouth.getStyle().setLeft(sliderDayDivWidth * totalDaysMouth, Unit.PX);
				totalDaysMouth += nbDaysMouth;
				slideMouth.setInnerText(mouths[i]);
				slideYear.appendChild(slideMouth);
			}
			sliderbar.appendChild(slideYear);
			totalDays += nbDays;
			year++;
		}
		sliderbar.getStyle().setWidth(sliderDayDivWidth * totalDays, Unit.PX);
	}

	private void positionHandler(Element handle, Date date) {
		int days = CalendarUtil.getDaysBetween(beginDate, date);
		handle.getStyle().setLeft(sliderDayDivWidth * days, Unit.PX);
	}

	private Integer[] mouthNdDay = new Integer[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

}
