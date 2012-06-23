/**
 * 
 */
package com.sfeir.common.gwt.client.dateslider;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

/**
 * @author sfeir
 *
 */
public interface DateSliderClientBundler extends ClientBundle {

	@Source("dateslider.css")
	DateSliderStyle style();

	@ImageOptions(repeatStyle=RepeatStyle.Horizontal)
	ImageResource slidebg();

}
