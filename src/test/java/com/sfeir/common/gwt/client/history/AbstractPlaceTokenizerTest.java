package com.sfeir.common.gwt.client.history;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gwt.place.shared.Place;
import com.sfeir.common.gwt.client.place.AbstractPlaceTokenizer;
import com.sfeir.common.gwt.client.place.PlaceProperty;

public class AbstractPlaceTokenizerTest {
	public enum MyEnum { ENUM1, ENUM2 };
	public class MyPlace extends Place {
		public String value;
		public Integer number;
		public Boolean bool;
		public MyEnum en = null;
	}

	@Test
	public void testGetPlace() {
		AbstractPlaceTokenizer<MyPlace> aPT = new AbstractPlaceTokenizer<MyPlace>() {

			@Override
			public Map<String, String> getPlaceProperties(MyPlace place) {
				fail();
				return ImmutableMap.of("value", place.value, "number", place.number.toString(), "bool", place.bool.toString(), "en", place.en.name());
			}
			
			@Override
			public MyPlace createPlace() {
				return new MyPlace();
			}

			@Override
			public MyPlace createPlaceWithProperties(Map<String, String> properties) {
				MyPlace place = new MyPlace();
				place.value = parseString(properties.get("value"));
				place.number = parseInteger(properties.get("number"));
				place.bool = parseBoolean(properties.get("bool"));
				place.en = parseEnum(properties.get("en"), MyEnum.class);
				return place;
			}

			@Override
			public Map<String, PlaceProperty> getProperties() {
				return ImmutableMap.of("value", new PlaceProperty(true, true, "value", PropertyType.STRING, String.class.getName(), "message"), "number", new PlaceProperty(false, false, "number",
						PropertyType.INTEGER, Integer.class.getName(), "message"), "bool", new PlaceProperty(false, false, "bool", PropertyType.BOOLEAN, Boolean.class.getName(), "message"), "en", new PlaceProperty(false, false, "en", PropertyType.ENUM, MyEnum.class.getName(), "enum"));
			}

			@Override
			public Class<MyPlace> getPlaceType() {
				return MyPlace.class;
			}
		};
		// ---------------------- Tests ---------------------------------
		MyPlace place = aPT.getPlace("val?number=3;bool=true");
		assertEquals("val", place.value);
		assertEquals(new Integer(3), place.number);
		assertEquals(true, place.bool);
		place = aPT.getPlace("value=toto;number=3000;bool=false;en=ENUM1");
		assertEquals(new Integer(3000), place.number);
		assertEquals(false, place.bool);
		assertEquals("toto", place.value);
		assertEquals(MyEnum.ENUM1, place.en);
		place = aPT.getPlace("val");
		assertEquals("val", place.value);
		assertEquals(null, place.bool);
		assertEquals(null, place.number);
		place = aPT.getPlace("v,a%61l%59u%3Fe");
		assertEquals("v,a=l;u?e", place.value);
		assertEquals(null, place.bool);
		assertEquals(null, place.number);
		try {
			place = aPT.getPlace("number=3;bool=true");
			fail();
		} catch (RuntimeException e) {
			assertEquals(
					"In the place com.sfeir.common.gwt.client.history.AbstractPlaceTokenizerTest$MyPlace the property value is required but was not present in the current token number=3;bool=true",
					e.getMessage());
		}
		try {
			place = aPT.getPlace("");
			fail();
		} catch (RuntimeException e) {
			assertEquals(
					"In the place com.sfeir.common.gwt.client.history.AbstractPlaceTokenizerTest$MyPlace the property value is required but was not present in the current token ",
					e.getMessage());
		}
	}

	@Test
	public void testGetToken() {
		AbstractPlaceTokenizer<MyPlace> aPT = new AbstractPlaceTokenizer<MyPlace>() {

			@Override
			public Map<String, String> getPlaceProperties(MyPlace place) {
				Builder<String, String> builder = ImmutableMap.builder();
				if (place.value != null) {
					builder.put("value", place.value);
				}
				if (place.number != null) {
					builder.put("number", place.number.toString());
				}
				if (place.bool != null) {
					builder.put("bool", place.bool.toString());
				}
				if (place.en != null) {
					builder.put("en", place.en.name());
				}
				return builder.build();
			}
			
			@Override
			public MyPlace createPlace() {
				return new MyPlace();
			}

			@Override
			public MyPlace createPlaceWithProperties(Map<String, String> properties) {
				fail();
				MyPlace place = new MyPlace();
				place.value = parseString(properties.get("value"));
				place.number = parseInteger(properties.get("number"));
				place.bool = parseBoolean(properties.get("bool"));
				place.en = parseEnum(properties.get("en"), MyEnum.class);
				return place;
			}

			@Override
			public Map<String, PlaceProperty> getProperties() {
				return ImmutableMap.of("value", new PlaceProperty(true, true, "value", PropertyType.STRING, String.class.getName(), "message"), "number", new PlaceProperty(false, false, "number",
						PropertyType.INTEGER, Integer.class.getName(), "message"), "bool", new PlaceProperty(false, false, "bool", PropertyType.BOOLEAN, Boolean.class.getName(), "message"), "en", new PlaceProperty(false, false, "en", PropertyType.ENUM, MyEnum.class.getName(), "enum"));
			}

			@Override
			public Class<MyPlace> getPlaceType() {
				return MyPlace.class;
			}
		};
		MyPlace place = new MyPlace();
		place.value = "value";
		place.number = 3;
		place.bool = true;
		assertEquals("value?number=3;bool=true", aPT.getToken(place));
		place.value = "v,a=l;u?e";
		place.number = 3789456;
		place.bool = null;
		assertEquals("v,a%61l%59u%3Fe?number=3789456", aPT.getToken(place));
		place = new MyPlace();
		place.value = "value";
		place.number = 3;
		place.bool = true;
		place.en = MyEnum.ENUM1;
		assertEquals("value?number=3;bool=true;en=ENUM1", aPT.getToken(place));
	}

}
