package com.sfeir.common.gwt.client.component.label.base;

import java.util.Map;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBoxBase;

/**
 * 
 * Classe de base pour les composants de type texte (TextBox,TextArea,PasswordBox)
 * @author sfeir
 */
public abstract class LabelTextBoxBase<T extends TextBoxBase,V> extends LabelComponentBase<T, V> {

    public LabelTextBoxBase() {
        super();
    }

    public LabelTextBoxBase(String text) {
        super(text);
    }

    public LabelTextBoxBase(String text, boolean mandatory) {
        super(text, mandatory);
    }

    public LabelTextBoxBase(Map<String,Object> config) {
        super(config);
    }

    @Override
    public void clearComponent() {
        ((HasText) getComponent()).setText("");
    }

    @Override
    public V defaultValue() {
        return null;
    }

    @Override
    public void setConfig(Map<String, Object> config) {
        super.setConfig(config);
        if (config.containsKey("enabled")) {
            setEnabled((Boolean) config.get("enabled"));
        }
        if (config.containsKey("readOnly")) {
            setReadOnly((Boolean) config.get("readOnly"));
        }
        if (config.containsKey("accessKey")) {
            setAccessKey((Character) config.get("accessKey"));
        }
        if (config.containsKey("title")) {
            setTitle((String) config.get("title"));
        }
        if (config.containsKey("tabIndex")) {
            setTabIndex((Integer) config.get("tabIndex"));
        }
    }

    public int getTabIndex() {
        return getComponent().getTabIndex();
    }

    public String getTitle() {
        return getComponent().getTitle();
    }

    public boolean isEnabled() {
        return getComponent().isEnabled();
    }

    public boolean isReadOnly() {
        return getComponent().isReadOnly();
    }

    public void setAccessKey(char key) {
        getComponent().setAccessKey(key);
    }

    public void setEnabled(boolean enabled) {
        getComponent().setEnabled(enabled);
    }

    public void setReadOnly(boolean readOnly) {
        getComponent().setReadOnly(readOnly);
    }

    public void setTabIndex(int index) {
        getComponent().setTabIndex(index);
    }

    public void setTitle(String title) {
        getComponent().setTitle(title);
    }
}
