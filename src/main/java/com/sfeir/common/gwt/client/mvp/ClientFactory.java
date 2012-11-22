package com.sfeir.common.gwt.client.mvp;

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
import com.sfeir.common.gwt.client.mvp.PlaceHistoryHandler.Historian;

/**
 * This Interface allow to access all MVP elements.
 * 
 */
public interface ClientFactory {
    /**
     * The Event Bus is used to communicate with other Activity
     * 
     * @return
     */
    public EventBus getEventBus();

    /**
     * The PlaceController allow to navigate to a new Place
     * @return
     */
    public PlaceController getPlaceController();

    /**
     * The ActivityMapper map a Place with an Activity
     * @return
     */
    public ActivityMapper getActivityMapper();
    
    /**
     * Returns the activity to run for the given {@link Place}, or null.
     *
     * @param place a Place object
     */
    public Activity getActivity(Place place);
    
    /**
     * Returns the activity to run for the given {@link Place}, or null.
     *
     * @param place a Place object
     */
    public void getActivity(Place place, AsyncCallback<Activity> callback);

    /**
     * The PlaceHistoryMapper is used to have the history token from a place and build the place from the token
     * This class use all Tokenizer defined in the Place.
     * @return
     */
    public AppPlaceHistoryMapper getPlaceHistoryMapper();
    
    /**
     * HandleHistory,
     * Create the PlaceHistoryHandler, register the PlaceControler, the EventBus and the defaultPlace, and call the handlerHistory
     * @param defaultPlace
     * @return
     */
    public void handleHistory(Place defaultPlace);
    /**
     * HandleHistory,
     * Create the PlaceHistoryHandler, register the PlaceControler, the EventBus and the defaultPlace, and call the handlerHistory
     * @param defaultPlace
     * @param historian The Historian to use with the PlaceHistoryHandler
     * @return
     */
    public void handleHistory(Place defaultPlace, Historian historian);
    
    /**
     * Create the ActivityLayoutManager
     */
    public void createActivityLayoutManager();

    /**
     * Get the view implementation of the view Interface given
     * @param viewClass The interface class
     * @return
     */
    public <T extends View> T getView(Class<T> viewClass);

    /**
     * Create a new view implementation, not keep it in the cache
     * @param viewClass The Interface class
     * @return allways return a new instance of the class implementation
     */
    public <T extends View> T getNewView(Class<T> viewClass);
    
    /**
     * Get the async service
     * @param serviceClass
     * @param asyncServiceClass Used for good cast class
     * @return
     */
    public <T, V extends RemoteService> T getService(Class<V> serviceClass, Class<T> asyncServiceClass);
    
    /**
     * Get the async service
     * @param serviceAsyncClass
     * @return
     */
    public <T, V extends RemoteService> T getService(Class<V> serviceClass);
    
    /**
     * Get a new Request Context
     * /!\ allways rebuild a new request context, the request factory must be initialised before call any request
     * @param requestContextClass
     * @return
     */
    public <T extends RequestContext> T getRequest(Class<T> requestContextClass);
    
    /**
     * Get a new Request Factory
     * @param requestContextClass
     * @return
     */
    public <T extends RequestFactory> T getRequestFactory(Class<T> requestFactoryClass);
    
    /**
     * Get a new Request Factory
     * @param requestContextClass
     * @return
     */
    public <T extends RequestFactory> T initializeRequestFactory(Class<T> requestFactoryClass);
    
    /**
     * Get a new Request Factory
     * @param requestContextClass
     * @return
     */
    public <T extends RequestFactory> T initializeRequestFactory(Class<T> requestFactoryClass, RequestTransport transport);
    
    /**
     * Get a LocalizableResource, a Message or Constants ressouces
     * @param messageClass
     * @return
     */
    public <T extends LocalizableResource> T getMessage(Class<T> messageClass);

    /**
     * Get the login user
     * @return
     */
    public Object getUserData();

    /**
     * Set the logged the user
     * @param utilisateur
     */
    public void setUserData(Object utilisateur);
    
    /**
     * Get data from page, session, locale storage
     * If data come from session or locale storage, the data type is string
     * @param key
     * @return
     */
    public Object getData(String key);

    /**
     * Set data in a page cache
     * @param key
     * @param data
     */
    public void setData(String key, Object data);
    
    /**
     * Set data in a page cache
     * /!\ If you use session or locale storage, the type of data will be converted in String with toData();
     * @param key
     * @param data
     * @param storage 
     */
    public void setData(String key, Object data, StorageData storage);
    
    public enum StorageData {
    	PAGE, SESSION, LOCALE
    }

}
