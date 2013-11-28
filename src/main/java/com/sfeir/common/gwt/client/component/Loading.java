package com.sfeir.common.gwt.client.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class Loading extends Composite implements HasText {

	private static LoadingUiBinder uiBinder = GWT.create(LoadingUiBinder.class);

	interface LoadingUiBinder extends UiBinder<Widget, Loading> {
	}

	@UiField
	InlineLabel loadingText;
	@UiField
	Image loadingImg;
    private boolean isLoading;

	public Loading() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	public Loading(String text) {
		this();
		setText(text);
	}

	@Override
	public String getText() {
		return loadingText.getText();
	}

	@Override
	public void setText(String text) {
		loadingText.setText(text);
	}
	
	public void setLoading(boolean isLoading) {
	    this.isLoading = isLoading;
        loadingImg.setVisible(isLoading);
	}
	
	public boolean isLoading() {
        return isLoading;
    }
}
