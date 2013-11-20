package com.sfeir.common.gwt.client.component.dialogbox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;

public class ClosableDialogBox extends DialogBox implements ClickHandler {
	// instantiate the caption with the cancel button
	private static DialogBoxCaptionWithCancel caption = new DialogBoxCaptionWithCancel();

	public ClosableDialogBox() {
		super(false, false, caption);
	}
	
	public ClosableDialogBox(Boolean autoHide) {
	    super(autoHide, true, caption);
	}
	
	public ClosableDialogBox(boolean autoHide, boolean modal) {
	    super(autoHide, modal, caption);
	}

	protected void onLoad() {
		super.onLoad();
		caption.addClickHandler(this);
	}

	@Override
	public void onClick(ClickEvent event) {
		this.hide();
	}
}