package com.sfeir.common.gwt.client.component.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeCloseTabHandler extends EventHandler {
    public void onBeforeCloseTab(BeforeCloseTabEvent event);
}
