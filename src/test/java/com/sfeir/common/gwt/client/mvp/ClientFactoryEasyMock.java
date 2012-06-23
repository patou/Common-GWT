package com.sfeir.common.gwt.client.mvp;

import static com.google.common.collect.Maps.newHashMap;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.resetToNice;

import java.util.Map;

import org.easymock.EasyMock;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.i18n.client.LocalizableResource;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.RequestTransport;

/**
 * Class helper simplifiant l'utilisation du ClientFactory avec le framework de mock EasyMock
 * 
 * 
 */
public class ClientFactoryEasyMock implements ClientFactory {
	EventBus eventBus = createNiceMock(EventBus.class);
	com.google.gwt.event.shared.EventBus oldEventBus = createNiceMock(com.google.gwt.event.shared.EventBus.class);
	PlaceController placeController = createNiceMock(PlaceController.class);
	ActivityMapper activityMapper = createNiceMock(ActivityMapper.class);
	AppPlaceHistoryMapper appPlaceHistoryMapper = createNiceMock(AppPlaceHistoryMapper.class);
	Map<Class<?>, View> views = newHashMap();
	Map<Class<?>, Object> services = newHashMap();
	Map<Class<?>, LocalizableResource> localizable = newHashMap();
	Map<Class<?>, RequestContext> requests = newHashMap();
	Map<String, Object> datas = newHashMap();
	Object userdata;

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	/**
	 * Return the old com.google.gwt.event.shared.EventBus Use it to give to the start method
	 * 
	 * @return
	 */
	public com.google.gwt.event.shared.EventBus getOldEventBus() {
		return oldEventBus;
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
		return appPlaceHistoryMapper;
	}

	/**
	 * Test implementation of the getView (the production implementation use a generator) Automatically create the mock from the class or use the mock allready create (by other
	 * call to this method or by using the registerMockView)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends View> T getView(Class<T> viewClass) {
		View view = views.get(viewClass);
		if (view == null) {
			view = createNiceMock(viewClass);
			views.put(viewClass, view);
		}
		return (T) view;
	}

	/**
	 * For test, get the view from the cache version method
	 */
	@Override
	public <T extends View> T getNewView(Class<T> viewClass) {
		return getView(viewClass);
	}

	/**
	 * Test implementation of the getService (the production implementation use a generator) Automatically create the async service mock or use the mock allready created (by other
	 * call to this method or by using the registerMockService)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T, V extends RemoteService> T getService(Class<V> serviceClass) {
		Object service = services.get(serviceClass);
		if (service == null) {
			service = createMockService(serviceClass);
		}
		return (T) service;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, V extends RemoteService> T getService(Class<V> serviceClass, Class<T> asyncServiceClass) {
		Object service = services.get(serviceClass);
		if (service == null) {
			service = createMockService(serviceClass);
		}
		return (T) service;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends LocalizableResource> T getMessage(Class<T> messageClass) {
		Object local = localizable.get(messageClass);
		if (local == null) {
			local = createNiceMock(messageClass);
		}
		return (T) local;
	}

	/**
	 * Register a new mock view in the create mock view
	 * 
	 * @param mock
	 * @param viewClass
	 */
	public void registerMockView(View mock, Class<?> viewClass) {
		views.put(viewClass, mock);
	}

	/**
	 * Register a LocalisableResource
	 * 
	 * @param mock
	 * @param localizableResourceClass
	 */
	public void registerMockLocalizableResource(LocalizableResource mock, Class<?> localizableResourceClass) {
		localizable.put(localizableResourceClass, mock);
	}

	/**
	 * Unregister a mock LocalisableResource
	 * 
	 * @param localizableResourceClass
	 */
	public void unregisterMockLocalizableResource(Class<?> localizableResourceClass) {
		localizable.remove(localizableResourceClass);
	}

	/**
	 * Unregister a new mock view in the create mock view
	 * 
	 * @param viewClass
	 */
	public void unregisterMockView(Class<?> viewClass) {
		views.remove(viewClass);
	}

	/**
	 * Create the async service version from the service class
	 * 
	 * @param serviceClass
	 * @return
	 */
	public <T, V extends RemoteService> T createMockService(Class<V> serviceClass) {
		try {
			@SuppressWarnings({ "static-access", "unchecked" })
			// The GWT compiler allready check that the Async corresponding
			// interface exist
			Class<T> serviceAsyncClass = (Class<T>) serviceClass.forName(serviceClass.getName().concat("Async"));
			T service = createNiceMock(serviceAsyncClass);
			registerMockService(service, serviceClass);
			return service;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Register a new Service
	 * 
	 * @param serviceAsync
	 * @param serviceClass
	 */
	public <T, V extends RemoteService> void registerMockService(T serviceAsync, Class<V> serviceClass) {
		services.put(serviceClass, serviceAsync);
	}

	/**
	 * Clear the cache of mocking view to be restart to fresh instance
	 * 
	 * Call this method in the @Before method
	 */
	public void reset() {
		views.clear();
		services.clear();
		resetToNice(eventBus, placeController, activityMapper, appPlaceHistoryMapper);
		for (View view : views.values()) {
			resetToNice(view);
		}
		for (Object view : services.values()) {
			resetToNice(view);
		}
		for (LocalizableResource local : localizable.values()) {
			resetToNice(local);
		}
	}

	/**
	 * Call replay on all mock on the clientFactory
	 * 
	 * Call this after configuring all expect() on mock vue and before call the testing method
	 */
	public void replay() {
		EasyMock.replay(eventBus, placeController, activityMapper, appPlaceHistoryMapper);
		for (View view : views.values()) {
			EasyMock.replay(view);
		}
		for (Object view : services.values()) {
			EasyMock.replay(view);
		}
		for (LocalizableResource local : localizable.values()) {
			EasyMock.replay(local);
		}
	}

	/**
	 * Call verify on all mock on the ClientFactory
	 * 
	 * Call this method after calling the tested method, for verify that all defined method call has been doing
	 */
	public void verify() {
		EasyMock.verify(eventBus, placeController, activityMapper, appPlaceHistoryMapper);
		for (View view : views.values()) {
			EasyMock.verify(view);
		}
		for (Object view : services.values()) {
			EasyMock.verify(view);
		}
		for (LocalizableResource local : localizable.values()) {
			EasyMock.verify(local);
		}
	}

	/**
	 * Helper to set the Place and the ClientFactory to an Activity Use the package access method to do the work
	 * 
	 * @param activity
	 * @param place
	 */
	public <P extends Place> void setClientFactoryAndPlace(ActivityPresenter<P> activity, P place) {
		ClientFactoryTestUtils.setClientFactoryAndPlace(activity, place, this);
	}

	@Override
	public Object getUserData() {
		return userdata;
	}

	@Override
	public void setUserData(Object userdata) {
		this.userdata = userdata;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends RequestContext> T getRequest(Class<T> requestContextClass) {
		RequestContext req = requests.get(requestContextClass);
		if (req == null) {
			req = createNiceMock(requestContextClass);
			requests.put(requestContextClass, req);
		}
		return (T) req;
	}

	@Override
	public Object getData(String key) {
		return datas.get(key);
	}

	@Override
	public void setData(String key, Object data) {
		datas.put(key, data);
	}

	/*
	 * Not save for test in Session or Locale storage
	 */
	@Override
	public void setData(String key, Object data, StorageData storage) {
		datas.put(key, data);
	}

	/**
	 * Not Implemented, in test, no init of the requestfactory needed !
	 */
	@Override
	public <T extends RequestFactory> T getRequestFactory(Class<T> requestFactoryClass) {
		return createNiceMock(requestFactoryClass);
	}

	@Override
	public void handleHistory(Place defaultPlace) {
		
	}

	@Override
	public void createActivityLayoutManager() {
		
	}

	@Override
	public <T extends RequestFactory> T initializeRequestFactory(Class<T> requestFactoryClass) {
		return createNiceMock(requestFactoryClass);
	}
	
	@Override
	public <T extends RequestFactory> T initializeRequestFactory(Class<T> requestFactoryClass, RequestTransport transport) {
		return createNiceMock(requestFactoryClass);
	}

	@Override
	public Activity getActivity(Place place) {
		return null;
	}

	@Override
	public void getActivity(Place place, AsyncCallback<Activity> callback) {
		
	}

}
