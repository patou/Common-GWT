package com.sfeir.common.gwt.client.layout;

import com.google.gwt.place.shared.Place;
import com.sfeir.common.gwt.client.place.IgnorePlace;

@IgnorePlace
public abstract class AbstractLayoutPlace extends Place {
    Place displayPlace;
    
    public AbstractLayoutPlace(Place displayPlace) {
	this.displayPlace = displayPlace;
    }
    
    public Place getDisplayPlace() {
	return displayPlace;
    }

    @Override
    public int hashCode() {
	int result = 1;
	return result;
    }

    /**
     * Two layout place are equals if there are the same class type
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	return true;
    }
    
}
