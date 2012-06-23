package com.sfeir.common.gwt.client.dateslider;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

class Slider extends Widget {
	private boolean mouseDown;
	private int offset;
	private int absoluteLeft;

	public Slider(Element element) {
		setElement(element);
		sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEMOVE);
	}
	
	public void setAbsoluteLeft(int absoluteLeft) {
		this.absoluteLeft = absoluteLeft;
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (event.getTypeInt()) {
		case Event.ONMOUSEDOWN:
			mouseDown = true;

			/*
			 * Resize glassElem to take up the entire scrollable window area, which is the greater of the scroll size and the client size.
			 */

			offset = event.getClientX() - getAbsoluteLeft();
			Event.setCapture(getElement());
			event.preventDefault();
			break;

		case Event.ONMOUSEUP:
			mouseDown = false;
			Event.releaseCapture(getElement());
			event.preventDefault();
			break;

		case Event.ONMOUSEMOVE:
			if (mouseDown) {
				int left = absoluteLeft + event.getClientX() - offset;
				getElement().getStyle().setLeft(left, Unit.PX);
				event.preventDefault();
			}
			break;
		}
	}

}