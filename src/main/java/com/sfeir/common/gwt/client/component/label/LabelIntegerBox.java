package com.sfeir.common.gwt.client.component.label;

import java.util.Map;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.sfeir.common.gwt.client.component.label.base.LabelTextBoxBase;

/**
 * Champs text pour entrer un entier
 */
public class LabelIntegerBox extends LabelTextBoxBase<TextBox, Integer> {

	public LabelIntegerBox() {
		super();
	}

	public LabelIntegerBox(String text) {
		super(text);
	}

	public LabelIntegerBox(String text, boolean mandatory) {
		super(text, mandatory);
	}

	public LabelIntegerBox(Map<String, Object> config) {
		super(config);
	}

	@Override
	protected TextBox createComponent() {
		final TextBox textBox = new TextBox();
		textBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				int index = textBox.getCursorPos();
				String previousText = textBox.getText();
				String newText = previousText.replaceAll("([^0-9-]+)", "");
				if (newText.indexOf(45, 1) > 0) {
					newText = newText.substring(0, 1) + newText.substring(1).replace("-", "");
				}
				if (!previousText.equals(newText)) {
					getComponent().setValue(newText);
					getComponent().setCursorPos(index > 0 ? index - (previousText.length() - newText.length()) : 0);
				}
			}
		});
		textBox.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				int index = textBox.getCursorPos();
				String previousText = textBox.getText();
				String newText;
				char charCode = event.getCharCode();
				if (textBox.getSelectionLength() > 0) {
					newText = previousText.substring(0, textBox.getCursorPos())
							+ charCode
							+ previousText.substring(textBox.getCursorPos()
									+ textBox.getSelectionLength(),
									previousText.length());
				} else {
					newText = previousText.substring(0, index)
							+ charCode
							+ previousText.substring(index, previousText
									.length());
				}
				try {
					Integer.parseInt(newText);
				} catch (Exception e) {
					if (!event.isControlKeyDown()
							&& !Character.isDigit(charCode)
							&& charCode != 45
							&& charCode != KeyCodes.KEY_BACKSPACE
							&& charCode != KeyCodes.KEY_DELETE
							&& charCode != KeyCodes.KEY_LEFT
							&& charCode != KeyCodes.KEY_RIGHT
							&& charCode != KeyCodes.KEY_UP
							&& charCode != KeyCodes.KEY_DOWN
							&& charCode != KeyCodes.KEY_HOME
							&& charCode != KeyCodes.KEY_END
							&& charCode != KeyCodes.KEY_ENTER
							&& charCode != KeyCodes.KEY_ALT
							&& charCode != KeyCodes.KEY_CTRL
							&& charCode != KeyCodes.KEY_SHIFT
							&& charCode != KeyCodes.KEY_TAB) {
						getComponent().cancelKey();
					}
				}
			}
		});
		return textBox;
	}

	@Override
	public Integer defaultValue() {
		return 0;
	}
	
	@Override
	public Integer getComponentValue() {
		String value = getComponent().getValue();
		Integer val = 0;
		try {
			val = Integer.parseInt(value);
		} catch (Exception e) {
			val = 0;
		}
		return val;
	}

	@Override
	public void setComponentValue(Integer value, boolean fire) {
		if (value == null) {
			clear();
		}
		else {
			getComponent().setValue(value.toString(), fire);
		}
	}
}
