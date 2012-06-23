package com.sfeir.common.gwt.client.layout;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.activity.shared.FilteredActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.web.bindery.event.shared.EventBus;
import com.sfeir.common.gwt.client.mvp.CachingActivityMapper;
import com.sfeir.common.gwt.client.place.DialogBoxPlace;
import com.sfeir.common.gwt.client.place.DialogBoxPlaceFactory;

/**
 * This Activity manager replace the default ActivityManager by filtering all place with a LayoutFilter automatically generated
 * This allow to automatically display a new layout for the given Place.
 * The layout defined is configured by an annotation @UseLayout or @NoLayout (for have no layout for the given Place) 
 * or use the default Layout configured in the gwt.xml file :
 * <set-configuration-property name="layout.defaultlayoutplace" value="com.sfeir.common.gwt.sample.protogwt.client.layout.LayoutPlace" />
 * 
 * The last layout activity is also cached and not restarted if the Layout is the same
 */
@SuppressWarnings("deprecation")
public class LayoutActivityManager extends ActivityManager {
	static DialogBoxPlaceFactory factory = GWT.create(DialogBoxPlaceFactory.class);
    public LayoutActivityManager(ActivityMapper mapper, EventBus eventBus) {
        super(new FilteredActivityMapper(new Filter(), new CachingActivityMapper(mapper)), eventBus);
        new DialogBoxActivityManager(mapper, eventBus);
    }
    
    interface LayoutFilter extends FilteredActivityMapper.Filter {
    }

    static class Filter implements FilteredActivityMapper.Filter {
    	LayoutFilter filter = GWT.create(LayoutFilter.class);
		@Override
		public Place filter(Place place) {
			if (!(place instanceof DialogBoxPlace) && factory.getDialogBoxInfo(place) == null) {
				return filter.filter(place);
			}
			return null;
		}
    }
}