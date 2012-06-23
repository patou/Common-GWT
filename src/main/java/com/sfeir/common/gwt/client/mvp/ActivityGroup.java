package com.sfeir.common.gwt.client.mvp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.activity.shared.FilteredActivityMapper;
import com.google.gwt.activity.shared.FilteredActivityMapper.Filter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceChangeRequestEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.sfeir.common.gwt.client.place.DialogBoxPlace;
import com.sfeir.common.gwt.client.place.DialogBoxPlaceFactory;
import com.sfeir.common.gwt.client.place.DialogBoxPlaceInfo;
/**
 * This abstract activity manage many other activity
 * It's automatically call all activity methods (start, stop, ...)
 *
 * /!\ Caution to call super.start, super.onStop, ... for good work of the class
 *
 */
@SuppressWarnings("deprecation")
public abstract class ActivityGroup<P extends Place> extends ActivityPresenter<P> implements Activity, PlaceChangeEvent.Handler, PlaceChangeRequestEvent.Handler, Filter {
	List<Activity> managedActivity = new ArrayList<Activity>();
	List<ActivityManager> managedActivityManager = new ArrayList<ActivityManager>();
	Map<Activity, AcceptsOneWidget> activityPanels = new HashMap<Activity, AcceptsOneWidget>();
	Boolean isStarted = false;
	private EventBus eventBus;
	static DialogBoxPlaceFactory factory;
	
	public ActivityGroup() {
	}
	
	/**
	 * Load a new activity from a place
	 * @param place
	 * @return
	 */
	protected Activity loadActivity(Place place) {
		return getClientFactory().getActivity(place);
	}
	
	/**
	 * Load the activity from the given place and attach it to the activity group
	 * @param place
	 * @param panel
	 * @return
	 */
	protected Activity attachActivity(Place place, AcceptsOneWidget panel) {
		Activity activity = loadActivity(place);
		attachActivity(activity, panel);
		return activity;
	}
	
	/**
	 * Attach a activity to the activity group, this activity will be automatically managed by this class
	 * @param activity
	 * @param panel
	 */
	protected void attachActivity(Activity activity, AcceptsOneWidget panel) {
		if (activity != null && panel != null) {
			initActivityPresenter(activity);
			managedActivity.add(activity);
			activityPanels.put(activity, panel);
			if (isStarted) {
				activity.start(panel, eventBus);
			}
		}
	}
	
	void initActivityPresenter(Activity activity) {
		if (activity instanceof ActivityPresenter<?>) {
			@SuppressWarnings("unchecked")
			ActivityPresenter<Place> activityPresenter = (ActivityPresenter<Place>) activity;
			activityPresenter.setClientFactory(getClientFactory());
			if (activityPresenter.getPlace() == null)
				activityPresenter.setPlace(getPlace());
		}
	}
	
	/**
	 * Remove the activity from the managed
	 * @param activity
	 * @param panel
	 */
	protected void unattachActivity(Activity activity) {
		managedActivity.remove(activity);
		activityPanels.remove(activity);
		if (isStarted) {
			activity.onStop();
		}
		else {
			activity.onCancel();
		}
	}

	/**
	 * Activity cycle life : onStart() 
	 * - listen Place Change Events
	 * - start all managed Activity
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = eventBus;
		listenPlaceChange(eventBus);
		for (Activity activity : managedActivity) {
			activity.start(activityPanels.get(activity), eventBus);
		}
		isStarted = true;
	}

	/**
	 * Return the first activity mayStop not null
	 */
	@Override
	public String mayStop() {
		for (Activity activity : managedActivity) {
			String mayStop = activity.mayStop();
			if (mayStop != null) {
				return mayStop;
			}
		}
		return super.mayStop();
	}

	/**
	 * Activity cycle life : onCancel() 
	 * - notify all managed activity
	 * - hide all activityManager (setDisplay(null))
	 */
	@Override
	public void onCancel() {
		super.onCancel();
		for (Activity activity : managedActivity) {
			activity.onCancel();
		}
		for (ActivityManager activityManager : managedActivityManager) {
			activityManager.setDisplay(null);
		}
		isStarted = false;
	}

	/**
	 * Activity cycle life : onStop() 
	 * - notify all managed activity
	 * - hide all activityManager (setDisplay(null))
	 */
	@Override
	public void onStop() {
		super.onStop();
		for (Activity activity : managedActivity) {
			activity.onStop();
		}
		for (ActivityManager activityManager : managedActivityManager) {
			activityManager.setDisplay(null);
		}
		isStarted = false;
	}

	/**
	 * Add a Listener to the place change Event, and notify all sub activity of the change
	 * @param eventBus2
	 */
	private void listenPlaceChange(EventBus eventBus) {
		eventBus.addHandler(PlaceChangeRequestEvent.TYPE, this);
		eventBus.addHandler(PlaceChangeEvent.TYPE, this);
	}
	
	/**
	 * When the Place is change, notify all managed Activity if the Activity implement the PlaceChangeEvent.Handler
	 */
	@Override
	public void onPlaceChange(PlaceChangeEvent event) {
		for (Activity activity : managedActivity) {
			if (activity instanceof PlaceChangeEvent.Handler) {
				((PlaceChangeEvent.Handler) activity).onPlaceChange(event);
			}
		}
	}
	
	/**
	 * When the Place is requested to change, notify all managed Activity if the Activity implement the PlaceChangeRequestEvent.Handler
	 */
	@Override
	public void onPlaceChangeRequest(PlaceChangeRequestEvent event) {
		for (Activity activity : managedActivity) {
			if (activity instanceof PlaceChangeRequestEvent.Handler) {
				((PlaceChangeRequestEvent.Handler) activity).onPlaceChangeRequest(event);
			}
		}
	}
	
	/**
	 * Create a new ActivityManager
	 * @param eventBus The eventbus for handle PlaceChangeEvent
	 * @param acceptsOneWidget The panel where activities will add their views.
	 * @param activityMapper The ActivityMapper to use for the ActivityManager
	 * @param firstPlace The first place to open in this ActivityManager
	 */
	protected void createActivityManager(EventBus eventBus, AcceptsOneWidget acceptsOneWidget, ActivityMapper activityMapper, Place firstPlace) {
		ActivityManager content = new ActivityManager(new FilteredActivityMapper(this, new CachingActivityMapper(activityMapper)), eventBus);
		content.setDisplay(acceptsOneWidget);
		content.onPlaceChange(new PlaceChangeEvent(firstPlace));
		managedActivityManager.add(content);
	}
	
	/**
	 * Create a new ActivityManager
	 * @param acceptsOneWidget The panel where activities will add their views.
	 * @param activityMapper The ActivityMapper to use for the ActivityManager
	 * @param firstPlace The first place to open in this ActivityManager
	 */
	protected void createActivityManager(AcceptsOneWidget acceptsOneWidget, ActivityMapper activityMapper, Place firstPlace) {
		ActivityManager content = new ActivityManager(new FilteredActivityMapper(this, new CachingActivityMapper(activityMapper)), eventBus);
		content.setDisplay(acceptsOneWidget);
		content.onPlaceChange(new PlaceChangeEvent(firstPlace));
		managedActivityManager.add(content);
	}
	
	/**
	 * Filter all dialog place to not display
	 * Override this method if you don't want to filter these place
	 */
	@Override
	public Place filter(Place place) {
		if (GWT.isClient()) { // Allow JUnit Test
			if (factory == null) {
				factory = GWT.create(DialogBoxPlaceFactory.class);
			}
			DialogBoxPlaceInfo info = (factory != null) ? factory.getDialogBoxInfo(place) : null;
			if (info != null || place instanceof DialogBoxPlace)
				return null;
		}
		return place;
	}
	
	protected EventBus getEventBus() {
		return eventBus;
	}
}
