package com.sfeir.common.gwt.client.component.label;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.sfeir.common.gwt.client.component.label.base.LabelSuggestBoxBase;


/**
 * Champ à valeur multiple séparer par une virgule avec suggestions de valeurs
 * @author sfeir
 */
public class LabelMultiSuggestBox extends LabelSuggestBoxBase<String, List<String>> {
    protected String separator = ",";

    /**
     * 
     */
    public LabelMultiSuggestBox() {
    }

    /**
     * @param label
     */
    public LabelMultiSuggestBox(String label) {
        super(label);
    }

    /**
     * @param label
     * @param mandatory
     */
    public LabelMultiSuggestBox(String label, boolean mandatory) {
        super(label, mandatory);
    }

    public LabelMultiSuggestBox(Map<String, Object> config) {
        super(config);
    }
    
    @Override
    public void setConfig(Map<String, Object> config) {
        super.setConfig(config);
        if (config.containsKey("separator")) {
            setSeparator((String) config.get("separator"));
        }
    }

    @Override
    protected SuggestBox createComponent() {
        SuggestBox suggestBox = new SuggestBox(new SuggestBoxOracle(), new MultipleTextBox());
        suggestBox.addSelectionHandler(this);
        return suggestBox;
    }

    @Override
    protected void setComponentValue(List<String> list, boolean fireEvents) {
        String text = "";
        for (String value : list) {
            text += value + separator + " ";
        }
        getTextBox().setValue(text);
    }

    @Override
    protected List<String> getComponentValue() {
        String text = getTextBox().getValue();
        List<String> liste = new ArrayList<String>();
        String[] values = text.trim().split(separator);
        for (String value : values) {
            value = value.trim();
            if (value.length() > 0) {
                liste.add(value);
            }
        }
        return liste;
    }

    @Override
    public MultipleTextBox getTextBox() {
        return (MultipleTextBox) super.getTextBox();
    }

    /**
     * Séparateur des différentes valeurs du champs (Par défaut, c'est une virgule)
     * @param separator
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }

    public class MultipleTextBox extends TextBoxBase {
        /**
         * Creates an empty multiple text box.
         */
        public MultipleTextBox() {
            this(Document.get().createTextInputElement(), "gwt-TextBox");
        }

        /**
         * This constructor may be used by subclasses to explicitly use an existing element. This
         * element must be an <input> element whose type is 'text'.
         * @param element
         *            the element to be used
         */
        protected MultipleTextBox(InputElement element) {
            super(element);
        }

        MultipleTextBox(InputElement inputElement, String styleName) {
            super(inputElement);
            if (styleName != null) {
                setStyleName(styleName);
            }
        }

        @Override
        public String getText() {
            String wholeString = super.getText();
            String lastString = wholeString;
            if (wholeString != null && !wholeString.trim().equals("")) {
                int lastComma = wholeString.trim().lastIndexOf(separator);
                if (lastComma > 0) {
                    lastString = wholeString.trim().substring(lastComma + 1);
                }
            }
            return lastString;
        }

        @Override
        public void setText(String text) {
            String wholeString = super.getText();
            if (text != null && text.equals("")) {
                super.setText(text);
            } else {
                // Clean last text, to replace with new value, for example, if new
                // text is v.zaprudnevd@gmail.com:
                // "manuel@we-r-you.com, v" need to be replaced with:
                // "manuel@we-r-you.com, v.zaprudnevd@gmail.com, "

                if (wholeString != null) {
                    int lastComma = wholeString.trim().lastIndexOf(separator);
                    if (lastComma > 0) {
                        wholeString = wholeString.trim().substring(0, lastComma);
                    } else {
                        wholeString = "";
                    }

                    if (!wholeString.trim().endsWith(separator) && !wholeString.trim().equals("")) {
                        wholeString = wholeString + separator + " ";
                    }

                    wholeString = wholeString + text + separator + " ";
                    super.setText(wholeString);
                }
            }
        }

        public String getValue() {
            return super.getText();
        }

        public void setValue(String text) {
            super.setText(text);
        }
    }
}
