package com.sfeir.common.gwt.client.layout;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.activity.shared.FilteredActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.sfeir.common.gwt.client.events.SetTitleEvent;
import com.sfeir.common.gwt.client.mvp.CachingActivityMapper;
import com.sfeir.common.gwt.client.place.DialogBoxPlace;
import com.sfeir.common.gwt.client.place.DialogBoxPlaceFactory;
import com.sfeir.common.gwt.client.place.DialogBoxPlaceInfo;

/**
 * This Activity manager replace the default ActivityManager for display activity in a dialog box It display all activity in a dialog box
 */
@SuppressWarnings("deprecation")
public class DialogBoxActivityManager extends ActivityManager implements SetTitleEvent.Handler {
	DialogBoxDisplay display = new DialogBoxDisplay();
	static DialogBoxPlaceFactory factory = GWT.create(DialogBoxPlaceFactory.class);

	public DialogBoxActivityManager(ActivityMapper mapper, EventBus eventBus) {
		super(new FilteredActivityMapper(new Filter(), new CachingActivityMapper(mapper)), eventBus);
		setDisplay(display);
		eventBus.addHandler(SetTitleEvent.TYPE, this);
	}

	@Override
	public void onPlaceChange(PlaceChangeEvent event) {
		super.onPlaceChange(event);
		Place newPlace = event.getNewPlace();
		DialogBoxPlaceInfo boxPlaceInfo = getDialogBoxInfo(newPlace);
		if (boxPlaceInfo != null) {
			display.setWidth(boxPlaceInfo.getWidth());
			display.setHeight(boxPlaceInfo.getHeight());
			display.setText(boxPlaceInfo.getCaption());
			display.setModal(boxPlaceInfo.getIsModal());
		} else {
			display.hide();
		}
	}

	private static DialogBoxPlaceInfo getDialogBoxInfo(Place newPlace) {
		if (newPlace instanceof DialogBoxPlace) {
			DialogBoxPlace place = (DialogBoxPlace) newPlace;
			return new DialogBoxPlaceInfo(place.getWidth(), place.getHeight(), place.getCaption(), place.getIsModal());
		}
		return factory.getDialogBoxInfo(newPlace);
	}

	static class Filter implements FilteredActivityMapper.Filter {
		@Override
		public Place filter(Place place) {
			DialogBoxPlaceInfo boxPlaceInfo = getDialogBoxInfo(place);
			if (boxPlaceInfo != null) {
				return place;
			}
			return null;
		}
	}

	class DialogBoxDisplay extends DialogBox {
		@Override
		public void setWidget(IsWidget w) {
			super.setWidget(w);
			center();
		}
	}

	@Override
	public void onSetTitle(SetTitleEvent e) {
		if (display.isShowing()) {
			display.setText(e.getTitle());
		}
	}
}