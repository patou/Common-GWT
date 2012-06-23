package com.sfeir.common.gwt.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;

public interface MvpFactory {
	public Activity createActivity(Place place);
    public View createView(String viewClass);
}