package com.sfeir.common.gwt.client.history;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.sfeir.common.gwt.client.history.AbstractPlaceHistoryMapperExtend;

public class AbstractPlaceHistoryMapperExtendTest {
    @Test
    public void testGetPlaceAndGetToken() {
	final Place place = new Place() {
	};
	final PlaceTokenizer<Place> pT = new PlaceTokenizer<Place>() {

	    @Override
	    public Place getPlace(String token) {
		assertEquals("token", token);
		return place;
	    }

	    @Override
	    public String getToken(Place newPlace) {
		assertEquals(place, newPlace);
		return "token";
	    }
	};
        AbstractPlaceHistoryMapperExtend pHm = new AbstractPlaceHistoryMapperExtend() {

	    @Override
	    protected PrefixAndToken getPrefixAndToken(Place newPlace) {
		assertEquals(place, newPlace);
		return new PrefixAndToken("place", "token", true);
	    }

	    @Override
	    protected PlaceTokenizer<?> getTokenizer(String prefix) {
		assertEquals("place", prefix);
		return pT;
	    }
        };
        assertEquals(place,pHm.getPlace("!place:token"));
        assertEquals("!place:token",pHm.getToken(place));
    }
}
