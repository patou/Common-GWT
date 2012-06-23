package com.sfeir.common.gwt.client.mvp;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sfeir.common.gwt.client.events.ErrorMessageEvent;
import com.sfeir.common.gwt.client.place.DefaultPlace;
import com.sfeir.common.gwt.shared.exceptions.NotAccessAllowedException;
import com.sfeir.common.gwt.shared.exceptions.NotLoginException;

/**
 * A Callback wrapper that catch in the onFailure all exception from authentification purpose
 * 
 * @param <T>
 */
public class ServiceCallback<T> implements AsyncCallback<T> {
	ClientFactory clientFactory;
	AsyncCallback<T> wrapped;
	ActivityPresenter<?> activity;

	ServiceCallback(ActivityPresenter<?> activity, AsyncCallback<T> wrapped) {
		super();
		this.activity = activity;
		this.clientFactory = activity.getClientFactory();
		this.wrapped = wrapped;
	}

	@Override
	public void onFailure(Throwable caught) {
		if (checkIslive())
			if (caught != null) {
				if (caught instanceof NotAccessAllowedException) {
					Window.alert("Your are not allowed to access to this part of the application");
					clientFactory.getPlaceController().goTo(new DefaultPlace(clientFactory.getPlaceController().getWhere()));
				}
				if (caught instanceof NotLoginException) {
					clientFactory.getPlaceController().goTo(new DefaultPlace(clientFactory.getPlaceController().getWhere()));
				}
				wrapped.onFailure(caught);
				clientFactory.getEventBus().fireEvent(new ErrorMessageEvent(caught.getMessage()));
			}
			else {
				throw new RuntimeException("null Exception in the onFailure (maybe the server exception can't be deserialized)");
			}
	}

	@Override
	public void onSuccess(T result) {
		if (checkIslive())
			wrapped.onSuccess(result);
	}

	/**
	 * Check if the activity is allready alive
	 * If the activity is
	 * @return True if we must call the onSuccess or onFailure
	 */
	private boolean checkIslive() {
		if (activity == null || !activity.isLive()) {
			activity = null; //Remove reference to the activity for the GC
		}
		return true;
	}
}
