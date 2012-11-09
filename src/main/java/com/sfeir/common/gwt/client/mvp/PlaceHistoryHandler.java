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
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.http.client.URL;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.sfeir.common.gwt.client.events.ReplacePlaceEvent;
import com.sfeir.common.gwt.client.history.PlaceHistoryMapper;

/**
 * Monitors {@link PlaceChangeEvent}s and {@link com.google.gwt.user.client.History} events and keep them in sync.
 */
public class PlaceHistoryHandler extends com.google.gwt.place.shared.PlaceHistoryHandler {
	/**
	 * Default implementation of {@link Historian}, based on {@link History}.
	 */
	public static class DefaultHistorian implements Historian {
		public com.google.gwt.event.shared.HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler) {
			return History.addValueChangeHandler(valueChangeHandler);
		}

		public String getToken() {
			return History.getToken();
		}

		public void newItem(String token, boolean issueEvent) {
			History.newItem(token, issueEvent);
		}

		@Override
		public void replaceToken(String token, boolean issueEvent) {
			newItem(token, issueEvent);
		}
	}

	public static class ParameterHistorian implements Historian {

		@Override
		public void newItem(String token, boolean issueEvent) {
			History.newItem(token, issueEvent);
		}

		@Override
		public String getToken() {
			String token = History.getToken();
			if (token.isEmpty()) {
				token = Window.Location.getParameter("token");
				if (token == null) {
					String path = URL.decode(Window.Location.getPath());
					if (path != null && !path.isEmpty() && path.startsWith("/")) {
						token = path.substring(1);
					}
					int posSlash = token.indexOf('/', 1);
					int posDDot = token.indexOf(':', 1);
					if (posSlash > 0 && (posSlash < posDDot || posDDot < 0))
						token = token.replaceFirst("/", ":");
					int posQuestionMark = token.indexOf('?');
					if (posQuestionMark > 0 && token.indexOf('&', posQuestionMark) > 0)
						token = token.substring(0, posQuestionMark) + token.substring(posQuestionMark + 1).replace('&', ';');
				}
			}
			return token;
		}

		@Override
		public com.google.gwt.event.shared.HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler) {
			return History.addValueChangeHandler(valueChangeHandler);
		}

		@Override
		public void replaceToken(String token, boolean issueEvent) {
			newItem(token, issueEvent);
		}
	}

	public static class Html5Historian extends ParameterHistorian implements
	// allows the use of ValueChangeEvent.fire()
			HasValueChangeHandlers<String> {

		private HandlerManager handlers = new HandlerManager(null);

		public Html5Historian() {
			initEvent();
		}

		@Override
		public com.google.gwt.event.shared.HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler) {
			final com.google.gwt.event.shared.HandlerRegistration historyHandler = History.addValueChangeHandler(valueChangeHandler);
			final com.google.gwt.event.shared.HandlerRegistration html5Handler = this.handlers.addHandler(ValueChangeEvent.getType(), valueChangeHandler);
			return new com.google.gwt.event.shared.HandlerRegistration() {
				
				@Override
				public void removeHandler() {
					historyHandler.removeHandler();
					html5Handler.removeHandler();
				}
			};
		}

		@Override
		public void newItem(String token, boolean issueEvent) {
			if (getToken().equals(token)) { // not sure if this is needed, but just in case
				return;
			}
			String newUri = token;
			int posSlash = newUri.indexOf('/', 1);
			int posDDot = newUri.indexOf(':', 1);
			if (posDDot > 0 && (posDDot < posSlash || posSlash < 0))
				newUri = newUri.replaceFirst(":", "/");
			if (newUri.startsWith("!"))
				newUri = newUri.substring(1);
			if (!newUri.startsWith("/"))
				newUri = "/" + newUri;
			int posQuestionMark = newUri.indexOf('?');
			if (posQuestionMark > 0 && newUri.indexOf(';', posQuestionMark) > 0)
				newUri = newUri.substring(0, posQuestionMark) + newUri.substring(posQuestionMark + 1).replace(';', '&');
			if (!GWT.isProdMode()) { //GWT parameters
				String gwtcodesvr = Window.Location.getParameter("gwt.codesvr");
				if (gwtcodesvr != null)
					newUri += ((posQuestionMark > 0) ? "&" : "?") + "gwt.codesvr=" + gwtcodesvr;
			}
			pushState(newUri);
			if (issueEvent) {
				ValueChangeEvent.fire(this, getToken());
			}
		}

		@Override
		public void fireEvent(GwtEvent<?> event) {
			this.handlers.fireEvent(event);
		}

		private native void initEvent() /*-{
			var that = this;
			var oldHandler = $wnd.onpopstate;
			$wnd.onpopstate = $entry(function(e) {
				that.@com.sfeir.common.gwt.client.mvp.PlaceHistoryHandler.Html5Historian::onPopState()();
				if (oldHandler) {
					oldHandler();
				}
			});
		}-*/;

		private void onPopState() {
			ValueChangeEvent.fire(this, getToken());
		}

		private native void pushState(String url) /*-{
			$wnd.history.pushState(null, $doc.title, url);
		}-*/;

		private native void replaceState(String url) /*-{
			$wnd.history.replaceState(null, $doc.title, url);
		}-*/;

		@Override
		public void replaceToken(String token, boolean issueEvent) {
			
		}
	}

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
		this(mapper, (Historian) GWT.create(ParameterHistorian.class));
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
