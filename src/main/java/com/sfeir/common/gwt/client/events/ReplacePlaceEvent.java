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
package com.sfeir.common.gwt.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.place.shared.Place;

/**
 * Event thrown when the user has reached a new location in the app.
 */
public class ReplacePlaceEvent extends GwtEvent<ReplacePlaceEvent.Handler> {

  /**
   * Implemented by handlers of PlaceChangeEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link ReplacePlaceEvent} is fired.
     *
     * @param event the {@link ReplacePlaceEvent}
     */
    void onPlaceReplace(ReplacePlaceEvent event);
  }

  /**
   * A singleton instance of Type&lt;Handler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();

  private final Place newPlace;

  /**
   * Constructs a PlaceChangeEvent for the given {@link Place}.
   *
   * @param newPlace a {@link Place} instance
   */
  public ReplacePlaceEvent(Place newPlace) {
    this.newPlace = newPlace;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  /**
   * Return the new {@link Place}.
   *
   * @return a {@link Place} instance
   */
  public Place getNewPlace() {
    return newPlace;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onPlaceReplace(this);
  }
}
