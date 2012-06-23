package com.sfeir.common.gwt.client.component.label.base;

import java.util.Map;

import com.google.gwt.i18n.client.HasDirection;
import com.google.gwt.user.client.ui.ListBox;


/**
 * 
 * @author sfeir
 */
public abstract class LabelListBoxBase<K,V> extends LabelListBase<K,ListBox,V> implements HasDirection {
    public LabelListBoxBase() {
        super();
    }
    
    public LabelListBoxBase(String label) {
        super(label);
    }
    
    public LabelListBoxBase(String label, Boolean mandatory) {
        super(label, mandatory);
    }
    
    public LabelListBoxBase(Map<String, Object> config) {
        super(config);
    }
    
    /**
     * @see com.sfeir.gwt.webding.client.component.LabelComponentBase#clearComponent()
     */
    @Override
    public void clearComponent() {
        getComponent().clear();
    }

    /**
     * @see com.sfeir.gwt.webding.client.component.LabelComponentBase#createComponent()
     */
    @Override
    protected ListBox createComponent() {
        ListBox listBox = new ListBox();
        return listBox;
    }

    @Override
    public void setConfig(Map<String, Object> config) {
        super.setConfig(config);
        if (config.containsKey("direction")) {
            setDirection((Direction) config.get("direction"));
        }
        if (config.containsKey("tabIndex")) {
            setTabIndex((Integer) config.get("tabIndex"));
        }
        if (config.containsKey("visibleItemCount")) {
            setVisibleItemCount((Integer) config.get("visibleItemCount"));
        }
        if (config.containsKey("enabled")) {
            setEnabled((Boolean) config.get("enabled"));
        }
        if (config.containsKey("displayValue")) {
            setDisplayValue((Boolean) config.get("displayValue"));
        }
        if (config.containsKey("displayValueSeparator")) {
            setDisplayValueSeparator((String) config.get("displayValueSeparator"));
        }
        if (config.containsKey("accessKey")) {
            setAccessKey((Character) config.get("accessKey"));
        }
        if (config.containsKey("title")) {
            setTitle((String) config.get("title"));
        }
    }
    
    /**
     * Sélectionne une options par son index
     * @param index
     */
    public void selectIndex(int index) {
        ListBox component = getComponent();
        component.setItemSelected(index, true);
    }
    
    /**
     * Désélectionne une options par son index 
     * @param index
     */
    public void unselectIndex(int index) {
        ListBox component = getComponent();
        component.setItemSelected(index, false);
    }

    /**
     * Search a Value in the ListBox
     * @param value to search
     * @return The value index or -1
     */
    protected Integer searchValue(String value) {
        if (value != null) {
            ListBox component = getComponent();
            int l = component.getItemCount();
            for (int i = 0; i < l; i++) {
                String val = component.getValue(i);
                if (value.equals(val)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    @Override
    public Integer getItemIndex(String value) {
        return searchValue(value);
    }
    
    @Override
    protected void insertItemValue(String item, String value, int index) {
        getComponent().insertItem(item, value, index);
    }

    @Override
    public void removeAll() {
        super.removeAll();
        getComponent().clear();
    }
    
    @Override
    public Integer getItemCount() {
        return getComponent().getItemCount();
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#getItemText()
     */
    public String getItemText(int index) {
        return getComponent().getItemText(index);
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#getSelectedIndex()
     */
    public int getSelectedIndex() {
        return getComponent().getSelectedIndex();
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#getTabIndex()
     */
    public int getTabIndex() {
        return getComponent().getTabIndex();
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#getTitle()
     */
    public String getTitle() {
        return getComponent().getTitle();
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#getVisibleItemCount()
     */
    public int getVisibleItemCount() {
        return getComponent().getVisibleItemCount();
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#isEnabled()
     */
    public boolean isEnabled() {
        return getComponent().isEnabled();
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#isItemSelected()
     */
    public boolean isItemSelected(int index) {
        return getComponent().isItemSelected(index);
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#isMultipleSelect()
     */
    public boolean isMultipleSelect() {
        return getComponent().isMultipleSelect();
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#isVisible()
     */
    public boolean isVisible() {
        return getComponent().isVisible();
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#setAccessKey()
     */
    public void setAccessKey(char key) {
        getComponent().setAccessKey(key);
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#setEnabled()
     */
    public void setEnabled(boolean enabled) {
        getComponent().setEnabled(enabled);
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#setItemSelected()
     */
    public void setItemSelected(int index, boolean selected) {
        getComponent().setItemSelected(index, selected);
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#setItemText()
     */
    public void setItemText(int index, String text) {
        getComponent().setItemText(index, text);
    }
    
    /**
     * @see com.google.gwt.user.client.ui.ListBox#setSelectedIndex()
     */
    public void setSelectedIndex(int index) {
        getComponent().setSelectedIndex(index);
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#setTabIndex()
     */
    public void setTabIndex(int index) {
        getComponent().setTabIndex(index);
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#setTitle()
     */
    public void setTitle(String title) {
        getComponent().setTitle(title);
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#setVisible()
     */
    public void setVisible(boolean visible) {
        getComponent().setVisible(visible);
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#setVisibleItemCount()
     */
    public void setVisibleItemCount(int visibleItems) {
        getComponent().setVisibleItemCount(visibleItems);
    }

    /**
     * @see com.google.gwt.user.client.ui.ListBox#getDirection()
     */
    public Direction getDirection() {
        return ((HasDirection) getComponent()).getDirection();
    }
    
    /**
     * @see com.google.gwt.user.client.ui.ListBox#setDirection()
     */
    public void setDirection(Direction direction) {
        ((HasDirection) getComponent()).setDirection(direction);
    }
    
}
