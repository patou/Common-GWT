package com.sfeir.common.gwt.client.place;

public class DialogBoxPlaceInfo {
	String width;
	String height;
	String caption;
	Boolean isModal;

	/**
	 * 
	 * @param width width of the dialog box
	 * @param height height of the dialog box
	 * @param caption Caption of the dialog box
	 * @param isModal True if the dialog box must be modal
	 */
	public DialogBoxPlaceInfo(String width, String height, String caption, Boolean isModal) {
		super();
		this.width = width;
		this.height = height;
		this.caption = caption;
		this.isModal = isModal;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Boolean getIsModal() {
		return isModal;
	}

	public void setIsModal(Boolean isModal) {
		this.isModal = isModal;
	}
}
