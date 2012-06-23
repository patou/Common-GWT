package com.sfeir.common.gwt.client.mvp;

import com.google.gwt.place.shared.Place;

public class ClientFactoryTestUtils {
	public static <P extends Place> void setClientFactoryAndPlace(ActivityPresenter<P> activity, P place, ClientFactory clientFactory) {
		activity.setClientFactory(clientFactory);
		activity.setPlace(place);
	}
}
