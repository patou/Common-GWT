package com.sfeir.common.gwt.client.component;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.DisclosurePanelImages;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * An {@link ImageBundle} that provides images for {@link CollapsePanel}.
 */
@SuppressWarnings("deprecation")
public interface CollapsePanelImagesRight extends DisclosurePanelImages {
  
  /**
   * An image indicating an open disclosure panel.
   * 
   * @return a prototype of this image
   */
  @Resource("collapsePanelLTR.png")
  AbstractImagePrototype disclosurePanelOpen();
  
  /**
   * An image indicating a closed disclosure panel.
   * 
   * @return a prototype of this image
   */
  @Resource("collapsePanelRTL.png")
  AbstractImagePrototype disclosurePanelClosed();
}