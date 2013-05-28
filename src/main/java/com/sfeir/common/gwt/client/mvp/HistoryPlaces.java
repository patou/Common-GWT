package com.sfeir.common.gwt.client.mvp;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.web.bindery.event.shared.EventBus;

public class HistoryPlaces {
    List<Place> places = new ArrayList<Place>();
    public HistoryPlaces(EventBus eventBus, PlaceController placeController) {
    }
    
    public void registerHandler(EventBus eventBus) {
        
    }
}
