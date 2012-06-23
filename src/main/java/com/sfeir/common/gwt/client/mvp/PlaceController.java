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

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceChangeRequestEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.sfeir.common.gwt.client.events.ReplacePlaceEvent;

/**
 * In charge of the user's location in the app.
 */
public class PlaceController extends com.google.gwt.place.shared.PlaceController {

  private static final Logger log = Logger.getLogger(PlaceController.class.getName());

  private final EventBus eventBus;

  private final Delegate delegate;
  private Place where = Place.NOWHERE;

  /**
   * Create a new PlaceController with a {@link DefaultDelegate}. The
   * DefaultDelegate is created via a call to GWT.create(), so an alternative
   * default implementation can be provided through &lt;replace-with&gt; rules
   * in a {@code .gwt.xml} file.
   * 
   * @param eventBus the {@link EventBus}
   */
  public PlaceController(EventBus eventBus) {
    this(eventBus, (Delegate) GWT.create(DefaultDelegate.class));
  }

  /**
   * Create a new PlaceController.
   * 
   * @param eventBus the {@link EventBus}
   * @param delegate the {@link Delegate} in charge of Window-related events
   */
  public PlaceController(EventBus eventBus, Delegate delegate) {
	super(eventBus);
    this.eventBus = eventBus;
    this.delegate = delegate;
    delegate.addWindowClosingHandler(new ClosingHandler() {
      public void onWindowClosing(ClosingEvent event) {
        String warning = maybeGoTo(Place.NOWHERE);
        if (warning != null) {
          event.setMessage(warning);
        }
      }
    });
  }

  /**
   * Returns the current place.
   * 
   * @return a {@link Place} instance
   */
  public Place getWhere() {
    return where;
  }

  /**
   * Request a change to a new place. It is not a given that we'll actually get
   * there. First a {@link PlaceChangeRequestEvent} will be posted to the event
   * bus. If any receivers post a warning message to that event, it will be
   * presented to the user via {@link Delegate#confirm(String)} (which is
   * typically a call to {@link Window#confirm(String)}). If she cancels, the
   * current location will not change. Otherwise, the location changes and a
   * {@link PlaceChangeEvent} is posted announcing the new place.
   * 
   * @param newPlace a {@link Place} instance
   */
  public void goTo(Place newPlace) {
    log.fine("goTo: " + newPlace);

    if (getWhere().equals(newPlace)) {
      log.fine("Asked to return to the same place: " + newPlace);
      return;
    }

    String warning = maybeGoTo(newPlace);
    if (warning == null || delegate.confirm(warning)) {
      where = newPlace;
      eventBus.fireEvent(new PlaceChangeEvent(newPlace));
    }
  }
  
  /**
   * Request a replace to the current place. It is not a given that we'll actually get
   * there. First a {@link PlaceChangeRequestEvent} will be posted to the event
   * bus. If any receivers post a warning message to that event, it will be
   * presented to the user via {@link Delegate#confirm(String)} (which is
   * typically a call to {@link Window#confirm(String)}). If she cancels, the
   * current location will not change. Otherwise, the location changes and a
   * {@link PlaceChangeEvent} is posted announcing the new place.
   * 
   * @param newPlace a {@link Place} instance
   */
  public void replace(Place newPlace) {
	  where = newPlace;
      eventBus.fireEvent(new ReplacePlaceEvent(newPlace));
  }

  private String maybeGoTo(Place newPlace) {
    PlaceChangeRequestEvent willChange = new PlaceChangeRequestEvent(newPlace);
    eventBus.fireEvent(willChange);
    String warning = willChange.getWarning();
    return warning;
  }
}
