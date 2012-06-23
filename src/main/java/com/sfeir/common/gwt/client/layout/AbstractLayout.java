package com.sfeir.common.gwt.client.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.sfeir.common.gwt.client.events.ErrorMessageEvent;
import com.sfeir.common.gwt.client.events.LoadingEvent;
import com.sfeir.common.gwt.client.events.UserLoggedEvent;
import com.sfeir.common.gwt.client.events.SetTitleEvent;
import com.sfeir.common.gwt.client.mvp.ActivityGroup;

/**
 * Super class for all Layout Activity
 * 
 * @param <P>
 */
public abstract class AbstractLayout<P extends AbstractLayoutPlace> extends ActivityGroup<P> implements LoadingEvent.Handler, ErrorMessageEvent.Handler, UserLoggedEvent.Handler, SetTitleEvent.Handler {

	protected Place firstPlace;
	int loading = 0;

	public AbstractLayout() {
		super();
	}

	@Override
	public final void start(AcceptsOneWidget panel, EventBus eventBus) {
		super.start(panel, eventBus);
		this.firstPlace = getPlace().getDisplayPlace();
		AbstractLayoutView view = onCreateView();
		createActivityManager(eventBus, view, getClientFactory().getActivityMapper(), getPlace().getDisplayPlace());
		bind(eventBus);
		init();
		panel.setWidget(view);
	}

	/**
	 * Override this method to do some code in the start() method
	 */
	protected void init() {
	}

	/**
	 * Implement this method for created the view
	 * 
	 * @return The view to display, must implement the interface AbstractLayoutView
	 */
	protected abstract AbstractLayoutView onCreateView();

	/**
	 * Automatically bind to global event : - LoadingEvent : notify when loading state change - ErrorMessageEvent : notify when display an error message - UserLoggedEvent : notify
	 * when the user is changed
	 * 
	 * @param eventBus
	 */
	protected void bind(EventBus eventBus) {
		eventBus.addHandler(LoadingEvent.getType(), this);
		eventBus.addHandler(ErrorMessageEvent.getType(), this);
		eventBus.addHandler(UserLoggedEvent.getType(), this);
		eventBus.addHandler(SetTitleEvent.getType(), this);
	}

	/**
	 * Get the first Place to display
	 * 
	 * @return
	 */
	public Place getFirstPlace() {
		return firstPlace;
	}

	@Override
	public void onLoading(LoadingEvent e) {
		if (e.isLoading()) {
			loading++;
		} else {
			loading--;
		}
		if (loading < 0)
			loading = 0;
		onLoading(loading > 0);
	}

	/**
	 * Override this method for display the loading indicator or not (The count of loading are also saved)
	 * 
	 * @param loading
	 *            Display the loading indicator or hide it
	 */
	protected void onLoading(Boolean loading) {

	}

	/**
	 * Override this method to display the error message
	 */
	@Override
	public void onErrorMessage(ErrorMessageEvent e) {
		GWT.log(e.getErrorMessage());
	}

	/**
	 * Override this method to be notified of user login
	 */
	@Override
	public void onUserLogged(UserLoggedEvent e) {
	}

	/**
	 * Override this method to display the title
	 * By default change the page title
	 */
	@Override
	public void onSetTitle(SetTitleEvent e) {
		Window.setTitle(e.getTitle());
	}
}