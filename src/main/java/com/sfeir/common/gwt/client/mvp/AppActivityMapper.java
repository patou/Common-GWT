package com.sfeir.common.gwt.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

/**
 * Abstract Activity of the ActivityMapper, used by the generated class
 * implementation
 * 
 * Automatically inject the ClientFactory and the Place to the activity
 * 
 */
public class AppActivityMapper implements ActivityMapper {
	ClientFactory clientFactory;
	private MvpFactory factory;

	void setClientFactory(ClientFactory clientFactory, MvpFactory factory) {
		this.clientFactory = clientFactory;
		this.factory = factory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Activity getActivity(Place place) {
		if (place == null)
			return null;
		Activity newActivity = factory.createActivity(place);
			// If the activity extend the ActivityPresenter, automatically
			// inject the ClientFactory and the Place
		if (newActivity != null
				&& newActivity instanceof ActivityPresenter<?>) {
		    ActivityPresenter<Place> activityPresenter = (ActivityPresenter<Place>) newActivity;
		    activityPresenter.setClientFactory(clientFactory);
            activityPresenter.setPlace(place);
		}			
		return newActivity;
	}
}
