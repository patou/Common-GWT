package com.sfeir.common.gwt.client.place;

import com.google.gwt.place.shared.Place;

/**
 * Extend this class if you want to display a Place in a dialog box and not in the default layout 
 * 
 * TODO Maybe implement this with the @UseLayout instead a super class
 * TODO find a method to set the caption of the dialog box from the activity
 */
@Deprecated
public abstract class DialogBoxPlace extends Place {
	String width;
	String height;
	String caption;
	Boolean isModal;

	/**
	 * Define the size of the dialog box
	 * @param width width of the dialog box
	 * @param height height of the dialog box
	 */
	public DialogBoxPlace(String width, String height) {
		this(width, height, "", false);
	}

	/**
	 * 
	 * @param width width of the dialog box
	 * @param height height of the dialog box
	 * @param caption Caption of the dialog box
	 */
	public DialogBoxPlace(String width, String height, String caption) {
		this(width, height, caption, false);
	}

	/**
	 * 
	 * @param width width of the dialog box
	 * @param height height of the dialog box
	 * @param caption Caption of the dialog box
	 * @param isModal True if the dialog box must be modal
	 */
	public DialogBoxPlace(String width, String height, String caption, Boolean isModal) {
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
