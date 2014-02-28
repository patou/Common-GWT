package com.sfeir.common.gwt.client.component.dialogbox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;

public class ClosableDialogBox extends DialogBox implements ClickHandler {

	public ClosableDialogBox() {
		super(false, false, createCaption());
	}
	
	public ClosableDialogBox(Boolean autoHide) {
	    super(autoHide, true, createCaption());
	}
	
	public ClosableDialogBox(boolean autoHide, boolean modal) {
	    super(autoHide, modal, createCaption());
	}

	protected void onLoad() {
		super.onLoad();
		((DialogBoxCaptionWithCancel)getCaption()).addClickHandler(this);
	}

	@Override
	public void onClick(ClickEvent event) {
		this.hide();
	}
	
	public static DialogBoxCaptionWithCancel createCaption() {
		return new DialogBoxCaptionWithCancel();
	}
}