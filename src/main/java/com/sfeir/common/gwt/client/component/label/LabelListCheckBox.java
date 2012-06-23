package com.sfeir.common.gwt.client.component.label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sfeir.common.gwt.client.component.label.base.LabelListBase;


/**
 * Liste de case à cocher
 * @author sfeir
 */
public class LabelListCheckBox<K> extends LabelListBase<K, FlowPanel, List<K>> {

    private List<CheckBox> listCheckBox = new ArrayList<CheckBox>();
    private Map<CheckBox, String> values = new HashMap<CheckBox, String>();
    private Boolean labelCheckBoxHasHtml = false;

    /**
     * 
     */
    public LabelListCheckBox() {
    }

    /**
     * @param text
     */
    public LabelListCheckBox(String text) {
        super(text);
    }

    /**
     * @param text
     * @param mandatory
     */
    public LabelListCheckBox(String text, boolean mandatory) {
        super(text, mandatory);
    }

    /**
     * @param config
     */
    public LabelListCheckBox(Map<String, Object> config) {
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
            for (CheckBox item : listCheckBox) {
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
        CheckBox radio = new CheckBox(item, getLabelCheckBoxHasHtml());
        radio.setName(getName());
        addhandler(radio);
        values.put(radio, value);
        if (index < 0) {
            listCheckBox.add(radio);
            getComponent().add(radio);
        } else {
            listCheckBox.add(index, radio);
            getComponent().insert(radio, index);
        }
    }

    private void addhandler(CheckBox radio) {
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
        for (CheckBox radio : listCheckBox) {
            radio.setValue(false, false);
        }
    }

    @Override
    protected List<K> getComponentValue() {
        List<K> list = new ArrayList<K>();
        for (CheckBox radio : listCheckBox) {
            if (radio.getValue()) {
                list.add(getItemValue(values.get(radio)));
            }
        }
        return list;
    }

    protected void setComponentValue(List<K> value, boolean fireEvents) {
        if (value != null && value.size() > 0) {
            for (Entry<CheckBox, String> item : values.entrySet()) {
                K val = getItemValue(item.getValue());
                if (value.contains(val)) {
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
        flowPanel.setStyleName("list-check-box-panel");
        return flowPanel;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        for (CheckBox radio : listCheckBox) {
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

	public void setLabelCheckBoxHasHtml(Boolean labelCheckBoxHasHtml) {
		this.labelCheckBoxHasHtml = labelCheckBoxHasHtml;
	}

	public Boolean getLabelCheckBoxHasHtml() {
		return labelCheckBoxHasHtml;
	}
}
