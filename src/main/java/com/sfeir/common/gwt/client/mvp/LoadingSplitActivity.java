package com.sfeir.common.gwt.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Label;

@IgnoreActivity
public class LoadingSplitActivity<T extends Place> extends ActivityGroup<T> {
	private AcceptsOneWidget panel;
	private Activity activityLoaded = null;
	
	private LoadingSplitActivity() {
	}
	
	static <T extends Place> LoadingSplitActivity<T> create(T place) {
		return new LoadingSplitActivity<T>();
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.panel = panel;//Save the Panel
		super.start(panel, eventBus);
		if (activityLoaded == null) {
			panel.setWidget(new Label("Loading ..."));
		}
		else { //In developpement mode, the run async is direct and there no loading
			startLoadedActivity(activityLoaded);
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		activityLoaded = null;
	}
	
	@Override
	public void onCancel() {
		super.onCancel();
		activityLoaded = null;
	}
	
	@Override
	public String mayStop() {
		return super.mayStop();
	}

	public void setActivityLoaded(Activity activityLoaded) {
		this.activityLoaded  = activityLoaded;
		startLoadedActivity(activityLoaded);
	}

	void startLoadedActivity(Activity activityLoaded) {
//		if (activityLoaded instanceof ActivityPresenter<?>) {
//			@SuppressWarnings("unchecked")
//			ActivityPresenter<T> activityPresenter = (ActivityPresenter<T>) activityLoaded;
//			activityPresenter.setClientFactory(getClientFactory());
//			activityPresenter.setPlace(getPlace());
//		}
		attachActivity(activityLoaded, panel);
	}
	
	public void setErrorNoActivity(String error) {
		panel.setWidget(new Label(error));
	}
}
