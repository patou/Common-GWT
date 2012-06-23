package com.sfeir.common.gwt.client.component.label;

import java.util.Map;

import com.google.gwt.user.client.ui.FileUpload;
import com.sfeir.common.gwt.client.component.label.base.LabelComponentBase;


/**
 * 
 * Champ permettant d'envoyer un fichier
 * @author sfeir
 */
public class LabelFileUpload extends LabelComponentBase<FileUpload, String> {

    public LabelFileUpload() {
        super();
    }

    public LabelFileUpload(String text) {
        super(text);
    }

    public LabelFileUpload(String text, boolean mandatory) {
        super(text, mandatory);
    }

    public LabelFileUpload(Map<String, Object> config) {
        super(config);
    }

    @Override
    protected FileUpload createComponent() {
        return new FileUpload();
    }

    public void setConfig(Map<String, Object> config) {
        super.setConfig(config);
    }

    @Override
    public void clearComponent() {
    }

    @Override
    protected void setComponentValue(String value, boolean fireEvents) {
        // Non disponible pour se composant
    }

    @Override
    protected String getComponentValue() {
        return getComponent().getFilename();
    }
}
