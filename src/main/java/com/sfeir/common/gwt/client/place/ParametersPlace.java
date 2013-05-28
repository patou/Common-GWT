package com.sfeir.common.gwt.client.place;

import java.util.Map;

import com.google.gwt.place.shared.Place;

public abstract class ParametersPlace extends Place {
    Map<String, String> parameters;
    
    public Map<String, String> getParameters() {
        return parameters;
    }
    
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
