package com.sfeir.common.gwt.client.place;

import com.google.gwt.place.shared.Place;

/**x
 * Place launched when there are a NotLoggingException
 * 
 * This place was ignored from the history
 */
@IgnorePlace
public class DefaultPlace extends Place {
    /**
     * The next place to go when the login was successful
     */
    private Place successLoginPlace;
    
    public DefaultPlace(Place successLoginPlace) {
	this.successLoginPlace = successLoginPlace;
    }
    
    public Place getSuccessLoginPlace() {
	return successLoginPlace;
    }
}
