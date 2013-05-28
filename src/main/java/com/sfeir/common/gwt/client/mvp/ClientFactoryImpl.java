package com.sfeir.common.gwt.client.mvp;

import static com.google.common.collect.Maps.newHashMap;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.LocalizableResource;
import com.google.gwt.place.shared.Place;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.RequestTransport;
import com.sfeir.common.gwt.client.events.UserLoggedEvent;
import com.sfeir.common.gwt.client.i18n.I18nFactory;
import com.sfeir.common.gwt.client.layout.LayoutActivityManager;
import com.sfeir.common.gwt.client.mvp.PlaceHistoryHandler.Historian;
import com.sfeir.common.gwt.client.mvp.historian.HistorianTokenFormater;
import com.sfeir.common.gwt.client.utils.Checker;

/**
 * Implementation of the ClientFactory
 * 
 */
public class ClientFactoryImpl implements ClientFactory {
	EventBus eventBus = new SimpleEventBus();
	PlaceController placeController = new PlaceController(eventBus);
	AppActivityMapper activityMapper = GWT.create(AppActivityMapper.class);
	AppPlaceHistoryMapper placeHistoryMapper = GWT.create(AppPlaceHistoryMapper.class);
	ServiceFactory serviceFactory = GWT.create(ServiceFactory.class);
	I18nFactory i18nFactory = GWT.create(I18nFactory.class);
	MvpFactoryAbstract factories = GWT.create(MvpFactory.class);
	HistorianTokenFormater historianTokenFormater = GWT.create(HistorianTokenFormater.class);
	RequestContextFactory requestContextFactory = GWT.create(RequestContextFactory.class);
	Map<Class<?>, View> views = new HashMap<Class<?>, View>();
	Map<Class<? extends LocalizableResource>, LocalizableResource> messages = newHashMap();
	Map<Class<? extends RemoteService>, Object> services = newHashMap();
	Map<String,Object> pageStorage = newHashMap();
	Object userdata;
	HistoryPlaces historyPlaces = new HistoryPlaces(eventBus, placeController);

	public ClientFactoryImpl() {
		activityMapper.setClientFactory(this, factories);
//		factories.setPlaceControler(placeController);
		if (GWT.isClient() && !GWT.isProdMode()) { //In development mode
			Checker checker = GWT.create(Checker.class);
			if (checker.hasErrors()) {
				Window.alert("Caution, there are GWT error, look at the gwt console.");
			}
		}
	}

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public PlaceController getPlaceController() {
		return placeController;
	}

	@Override
	public ActivityMapper getActivityMapper() {
		return activityMapper;
	}

	@Override
	public AppPlaceHistoryMapper getPlaceHistoryMapper() {
		return placeHistoryMapper;
	}
	
	@Override
	public void handleHistory(Place defaultPlace) {
		handleHistory(defaultPlace, null);
	}
	@Override
	public void handleHistory(Place defaultPlace, Historian historian) {
		PlaceHistoryHandler historyHandler = (historian == null) ? new PlaceHistoryHandler(placeHistoryMapper) : new PlaceHistoryHandler(placeHistoryMapper, historian);
		historyHandler.register(placeController, eventBus, defaultPlace);
		historyHandler.handleCurrentHistory();
	}
	
	@Override
	public HistorianTokenFormater getHistorianTokenFormater() {
		if (historianTokenFormater == null) {
			historianTokenFormater = GWT.create(HistorianTokenFormater.class);
		}
		return historianTokenFormater;
	}
	
	@Override
	public void createActivityLayoutManager() {
		AcceptsOneWidget appWidget = new AcceptsOneWidget() {
			IsWidget lastWidget = null;
			@Override
			public void setWidget(IsWidget w) {
				if (lastWidget != null)
					RootPanel.get().remove(lastWidget);
				lastWidget = w;
				if (w != null)
					RootPanel.get().add(w);
			}
		};
		ActivityManager activityManager = new LayoutActivityManager(activityMapper, eventBus);
		activityManager.setDisplay(appWidget);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends View> T getView(Class<T> viewClass) {
		View view = views.get(viewClass);
		if (view == null) {
			view = factories.createView(viewClass.getName());
			if (view == null)
				throw new RuntimeException(viewClass.getName() + " not exist");
			views.put(viewClass, view);
		}
		return (T) view;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends View> T getNewView(Class<T> viewClass) {
		return (T) factories.createView(viewClass.getName());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, V extends RemoteService> T getService(Class<V> serviceClass) {
		Object service = services.get(serviceClass);
		if (service == null) {
			service = serviceFactory.createService(serviceClass.getName());
			if (service == null)
				throw new RuntimeException(serviceClass.getName()
						+ " not exist");
			services.put(serviceClass, service);
		}
		return (T) service;
	}

	@Override
	public <T, V extends RemoteService> T getService(Class<V> serviceClass,
			Class<T> asyncServiceClass) {
		return getService(serviceClass);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends RequestContext> T getRequest(Class<T> requestContextClass) {
		RequestContext requestContext = requestContextFactory.createRequestContext(requestContextClass.getName());
		if (requestContext == null) {
			throw new RuntimeException("The request context " + requestContextClass.getName() + " not exist or the RequestFactory isn't initialised");
		}
		return (T) requestContext;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends RequestFactory> T getRequestFactory(Class<T> requestFactoryClass) {
		return (T) requestContextFactory.createRequestFactory(requestFactoryClass.getName());
	}
	
	@Override
	public <T extends RequestFactory> T initializeRequestFactory(Class<T> requestFactoryClass) {
		T rf = getRequestFactory(requestFactoryClass);
		rf.initialize(eventBus);
		return rf;
	}
	
	@Override
	public <T extends RequestFactory> T initializeRequestFactory(Class<T> requestFactoryClass, RequestTransport transport) {
		T rf = getRequestFactory(requestFactoryClass);
		rf.initialize(eventBus, transport);
		return rf;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends LocalizableResource> T getMessage(Class<T> messageClass) {
		LocalizableResource message = messages.get(messageClass);
		if (message == null) {
			message = i18nFactory.createI18n(messageClass.getName());
			if (message == null)
				throw new RuntimeException(messageClass.getName()
						+ " not exist");
			messages.put(messageClass, message);
		}
		return (T) message;
	}

	@Override
	public void setUserData(Object userdata) {
		this.userdata = userdata;
		eventBus.fireEvent(new UserLoggedEvent(userdata));
	}

	@Override
	public Object getUserData() {
		return userdata;
	}

	@Override
	public Object getData(String key) {
		if (pageStorage.containsKey(key))
			return pageStorage.get(key);
		if (Storage.isSessionStorageSupported())
			return Storage.getSessionStorageIfSupported().getItem(key);
		if (Storage.isLocalStorageSupported())
			return Storage.getLocalStorageIfSupported().getItem(key);
		return null;
	}

	@Override
	public void setData(String key, Object data) {
		setData(key, data, StorageData.PAGE);
	}

	/**
	 * TODO object convert to string
	 */
	@Override
	public void setData(String key, Object data, StorageData storage) {
		switch (storage) {
		case SESSION:
			Storage session = Storage.getSessionStorageIfSupported();
			if (session != null) {
				session.setItem(key, data.toString());
				break;
			}
		case LOCALE:
			Storage locale = Storage.getSessionStorageIfSupported();
			if (locale != null) {
				locale.setItem(key, data.toString());
				break;
			}
		default:
			pageStorage.put(key, data);
			break;
		}
	}

	@Override
	public Activity getActivity(Place place) {
		return activityMapper.getActivity(place);
	}

	@Override
	public void getActivity(Place place, AsyncCallback<Activity> callback) {
		factories.getActivity(place, callback);
	}
	
	@Override
	public HistoryPlaces getHistoryPlaces() {
	    return historyPlaces;
	}
}
