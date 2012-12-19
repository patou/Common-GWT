/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.sfeir.common.gwt.client.mvp;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.History;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.sfeir.common.gwt.client.events.ReplacePlaceEvent;
import com.sfeir.common.gwt.client.history.PlaceHistoryMapper;

/**
 * Monitors {@link PlaceChangeEvent}s and {@link com.google.gwt.user.client.History} events and keep them in sync.
 */
public class PlaceHistoryHandler extends com.google.gwt.place.shared.PlaceHistoryHandler {
	/**
	 * Optional delegate in charge of History related events. Provides nice isolation for unit testing, and allows pre- or post-processing of tokens. Methods correspond to the like
	 * named methods on {@link History}.
	 */
	public interface Historian extends com.google.gwt.place.shared.PlaceHistoryHandler.Historian {
		/**
		 * Replace the token {@link ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)} to be called as well.
		 */
		void replaceToken(String token, boolean issueEvent);
	}

	private final Historian historian;

	private final PlaceHistoryMapper mapper;

	private Place defaultPlace = Place.NOWHERE;

	private PlaceController placeController;

	/**
	 * Create a new PlaceHistoryHandler with a {@link DefaultHistorian}. The DefaultHistorian is created via a call to GWT.create(), so an alternative default implementation can be
	 * provided through &lt;replace-with&gt; rules in a {@code gwt.xml} file.
	 * 
	 * @param mapper
	 *            a {@link PlaceHistoryMapper} instance
	 */
	public PlaceHistoryHandler(PlaceHistoryMapper mapper) {
		this(mapper, (Historian) GWT.create(Historian.class));
	}

	/**
	 * Create a new PlaceHistoryHandler.
	 * 
	 * @param mapper
	 *            a {@link PlaceHistoryMapper} instance
	 * @param historian
	 *            a {@link Historian} instance
	 */
	public PlaceHistoryHandler(PlaceHistoryMapper mapper, Historian historian) {
		super(mapper, historian);
		this.mapper = mapper;
		this.historian = historian;
	}

	/**
	 * Initialize this place history handler.
	 * 
	 * @return a registration object to de-register the handler
	 */
	public HandlerRegistration register(PlaceController placeController, EventBus eventBus, Place defaultPlace) {
		this.defaultPlace = defaultPlace;
		this.placeController = placeController;

		final HandlerRegistration placeReg = eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				Place newPlace = event.getNewPlace();
				String token = tokenForPlace(newPlace);
				if (token != null)
					historian.newItem(token, false);
			}
		});

		final HandlerRegistration historyReg = historian.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				String token = event.getValue();
				handleHistoryToken(token);
			}
		});

		final HandlerRegistration replaceReg = eventBus.addHandler(ReplacePlaceEvent.TYPE, new ReplacePlaceEvent.Handler() {
			@Override
			public void onPlaceReplace(ReplacePlaceEvent event) {
				Place newPlace = event.getNewPlace();
				String token = tokenForPlace(newPlace);
				if (token != null)
					historian.replaceToken(token, false);
			}
		});

		return new HandlerRegistration() {
			public void removeHandler() {
				PlaceHistoryHandler.this.defaultPlace = Place.NOWHERE;
				PlaceHistoryHandler.this.placeController = null;
				placeReg.removeHandler();
				historyReg.removeHandler();
				replaceReg.removeHandler();
			}
		};
	}

	/**
	 * Handle the current history token. Typically called at application start, to ensure bookmark launches work.
	 */
	public void handleCurrentHistory() {
		handleHistoryToken(historian.getToken());
	}
	
	public Historian getHistorian() {
		return historian;
	}

	private void handleHistoryToken(String token) {

		Place newPlace = null;

		if ("".equals(token)) {
			newPlace = defaultPlace;
		}

		if (newPlace == null) {
			newPlace = mapper.getPlace(token);
		}

		if (newPlace == null) {
			newPlace = defaultPlace;
		}

		placeController.goTo(newPlace);
	}

	private String tokenForPlace(Place newPlace) {
		if (defaultPlace.equals(newPlace)) {
			return "";
		}

		String token = mapper.getToken(newPlace);
		if (Strings.isNullOrEmpty(token))
			return null;
		return token;
	}
}
