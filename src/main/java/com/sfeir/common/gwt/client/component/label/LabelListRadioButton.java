package com.sfeir.common.gwt.client.component.label;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.sfeir.common.gwt.client.component.label.base.LabelListBase;


/**
 * Liste de boutons radio
 * @author sfeir
 */
public class LabelListRadioButton<K> extends LabelListBase<K, FlowPanel, K> {

    private List<RadioButton> listRadio = new ArrayList<RadioButton>();
    private Map<RadioButton, String> values = new LinkedHashMap<RadioButton, String>();
    private Boolean labelRadioButtonHasHtml = false;

    /**
     * 
     */
    public LabelListRadioButton() {
    }

    /**
     * @param text
     */
    public LabelListRadioButton(String text) {
        super(text);
    }

    /**
     * @param text
     * @param mandatory
     */
    public LabelListRadioButton(String text, boolean mandatory) {
        super(text, mandatory);
    }

    /**
     * @param config
     */
    public LabelListRadioButton(Map<String, Object> config) {
        super(config);
    }

    /*
     * (non-Javadoc)
     * @see com.sfeir.gwt.webding.client.component.label.base.LabelListBase#getItemCount()
     */
    @Override
    public Integer getItemCount() {
        return values.size();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sfeir.gwt.webding.client.component.label.base.LabelListBase#getItemIndex(java.lang.String)
     */
    @Override
    protected Integer getItemIndex(String value) {
        if (value != null) {
            Integer i = 0;
            for (RadioButton item : listRadio) {
                String v = values.get(item);
                if (value.equals(v)) {
                    return i;
                }
                ++i;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sfeir.gwt.webding.client.component.label.base.LabelListBase#insertItemValue(java.lang.String
     * , java.lang.String, int)
     */
    @Override
    protected void insertItemValue(String item, String value, int index) {
        RadioButton radio = new RadioButton(getName(), item, labelRadioButtonHasHtml);
        addhandler(radio);
        values.put(radio, value);
        if (index < 0) {
            listRadio.add(radio);
            getComponent().add(radio);
        } else {
            listRadio.add(index, radio);
            getComponent().insert(radio, index);
        }
    }

    private void addhandler(RadioButton radio) {
        radio.addFocusHandler(this);
        radio.addBlurHandler(this);
        radio.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                onChange();
            }
        });
    }

    /*
     * (non-Javadoc)
     * @see com.sfeir.gwt.webding.client.component.label.base.LabelComponentBase#clearComponent()
     */
    @Override
    public void clearComponent() {
        for (RadioButton radio : listRadio) {
            radio.setValue(false, false);
        }
    }

    @Override
    protected K getComponentValue() {
        for (RadioButton radio : listRadio) {
            if (radio.getValue()) {
                return getItemValue(values.get(radio));
            }
        }
        return null;
    }

    protected void setComponentValue(K value, boolean fireEvents) {
        if (value != null) {
            String v = value.toString();
            for (Entry<RadioButton, String> item : values.entrySet()) {
                if (v.equals(item.getValue())) {
                    item.getKey().setValue(true, fireEvents);
                }
            }
        }
    };

    /*
     * (non-Javadoc)
     * @see com.sfeir.gwt.webding.client.component.label.base.LabelComponentBase#createComponent()
     */
    @Override
    protected FlowPanel createComponent() {
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.setStyleName("list-radio-box-panel");
        return flowPanel;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        for (RadioButton radio : listRadio) {
            radio.setName(name);
        }
    }

    @Override
    public String getName() {
        String name = super.getName();
        if (name == null) {
            name = "checkbox" + Math.round(Math.random() * 1000);
            setName(name);
        }
        return name;
    }

	public void setLabelRadioButtonHasHtml(Boolean labelRadioButtonHasHtml) {
		this.labelRadioButtonHasHtml = labelRadioButtonHasHtml;
	}

	public Boolean getLabelRadioButtonHasHtml() {
		return labelRadioButtonHasHtml;
	}
}
