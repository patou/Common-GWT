package com.sfeir.common.gwt.client.mvp;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

/**
 * Wraps another {@link ActivityMapper} and caches the last activity it
 * returned, to be re-used if we see the same place twice.
 */
public class CachingActivityMapper implements ActivityMapper {

  private final ActivityMapper wrapped;

  private Place lastPlace;
  private Activity lastActivity;

  /**
   * Constructs a CachingActivityMapper object.
   *
   * @param wrapped an ActivityMapper object
   */
  public CachingActivityMapper(ActivityMapper wrapped) {
    this.wrapped = wrapped;
  }

  public Activity getActivity(Place place) {
	if (place == null)
		return lastActivity;
    if (!place.equals(lastPlace)) {
        lastActivity = wrapped.getActivity(place);
        if (lastActivity instanceof ActivityPresenter<?>) {
            ((ActivityPresenter<?>) lastActivity).setOldPlace(lastPlace);
        }
        lastPlace = place;
    }

    return lastActivity;
  }
}