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
public class LabelDoubleBox extends LabelTextBoxBase<TextBox, Double> {

	public LabelDoubleBox() {
		super();
	}

	public LabelDoubleBox(String text) {
		super(text);
	}

	public LabelDoubleBox(String text, boolean mandatory) {
		super(text, mandatory);
	}

	public LabelDoubleBox(Map<String, Object> config) {
		super(config);
	}

	@Override
	protected TextBox createComponent() {
		final TextBox textBox = new TextBox();
		textBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
					int index = textBox.getCursorPos();
					String previousText = textBox.getText().replace(',', '.');
					String newText = previousText.replaceAll("([^0-9.-]+)", "");
					if (newText.indexOf(45, 1) > 0) {
						newText = newText.substring(0, 1) + newText.substring(1).replace("-", "");
					}
					if (newText.indexOf(46) != newText.lastIndexOf(46)) {
						newText = newText.substring(0, newText.lastIndexOf(46)).replace(".", "") + newText.substring(newText.lastIndexOf(46));
					}
					if (!previousText.equals(newText)) {
						getComponent().setValue(newText);
						getComponent().setCursorPos(index > 0 ? index  - (previousText.length() - newText.length()) : 0);
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
					newText = newText.replace(',', '.');
					Double.parseDouble(newText);
				} catch (Exception e) {
					if (!event.isControlKeyDown()
							&& !Character.isDigit(charCode)
							&& charCode != 45
							&& charCode != 44
							&& charCode != 46
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
	public Double defaultValue() {
		return .0;
	}

	@Override
	public Double getComponentValue() {
		String value = getComponent().getValue();
		Double val = 0.0;
		try {
			value = value.replace(',', '.');
			val = Double.parseDouble(value);
		} catch (Exception e) {
			val = 0.0;
		}
		return val;
	}

	@Override
	public void setComponentValue(Double value, boolean fire) {
		if (value == null) {
			clear();
		}
		else {
			getComponent().setValue(value.toString(), fire);
		}
	}

}
