package com.sfeir.common.gwt.theme.twitter;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class TwitterEntryPoint implements EntryPoint {

	@Override
	public void onModuleLoad() {
		Twitter.Instance.getInstance(); //Ensure inject style
		GWT.log("TwitterEntryPoint inject css");
	}

}
