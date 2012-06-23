package com.sfeir.common.gwt.client.component.label;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sfeir.common.gwt.client.component.HasConfig;
import com.sfeir.common.gwt.client.component.label.base.LabelComponentBase;


/**
 * Formulaire Affichant une liste de LabelComponent
 * @author sfeir
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class LabelForm extends FormPanel implements HasConfig, ValueChangeHandler {
    private String primaryName = "";
    private Integer nameCounter = 0;
    private List<Button> listSubmitButton = new ArrayList<Button>(1);
    private Boolean formSubmitNative = false;
    private Boolean autoEnabledButton = true;
    private FlowPanel rootPanel = new FlowPanel();
    private FlowPanel buttonPanel = new FlowPanel();
    private FlowPanel panel = new FlowPanel();
    private List<LabelComponentBase<?, ?>> listeComponent = new ArrayList<LabelComponentBase<?, ?>>();

    /**
     * Cree un nouveau conteneur d'élément de formulaire avec un Label
     */
    public LabelForm() {
        super();
        createWidget();
    }

    /**
     * Crée le composant de base
     */
    private void createWidget() {
        setSize("100%", "100%");
        rootPanel.setSize("100%", "100%");

        rootPanel.setStyleName("form-panel-root");
        panel.setStyleName("form-panel-components");
        buttonPanel.setStyleName("form-panel-buttons");
        rootPanel.add(panel);
        // rootPanel.setCellHeight(panel, "100%");
        // rootPanel.setCellWidth(panel, "100%");
        // rootPanel.setCellVerticalAlignment(panel, HasVerticalAlignment.ALIGN_TOP);
        rootPanel.add(buttonPanel);
        // rootPanel.setCellWidth(buttonPanel, "100%");
        // rootPanel.setCellVerticalAlignment(buttonPanel, HasVerticalAlignment.ALIGN_BOTTOM);
        // rootPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
        super.add(rootPanel);
    }

    /**
     * Ajoute un composant label
     * @param component
     */
    public void add(LabelComponentBase<?, ?> component) {
        panel.add(component);
        listeComponent.add(component);
        isFileUpload(component);
        setComponentName(component);
        addHandler(component);
    }
    
    /**
     * Add another widget
     */
    public void add(Widget widget) {
        panel.add(widget);
    }

    /**
     * Insert un composant Label
     * @param component
     * @param beforeIndex
     */
    public void insert(LabelComponentBase<?, ?> component, int beforeIndex) {
        panel.insert(component, beforeIndex);
        listeComponent.add(beforeIndex, component);
        isFileUpload(component);
        setComponentName(component);
        addHandler(component);
    }

    /**
     * Supprime un label composant
     * @param component
     */
    public void remove(LabelComponentBase<?, ?> component) {
        panel.remove(component);
        listeComponent.remove(component);
    }

    /**
     * Supprime tous les composants
     */
    public void removeAll() {
        panel.clear();
        listeComponent.clear();
    }

    /**
     * Ajoute un bouton qui envoie le formulaire
     * @param text
     *            Texte du bouton
     */
    public void addSubmitButton(String text) {
        addSubmitButton(new Button(text));
    }

    /**
     * Ajoute un bouton qui envoie le formulaire
     * @param button
     *            bouton à ajouter
     */
    public void addSubmitButton(Button button) {
        buttonPanel.add(button);
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                submit();
            }
        });
        listSubmitButton.add(button);
        button.setEnabled(!autoEnabledButton);
    }

    /**
     * Ajoute un bouton qui remet à zéro le formulaire
     * @param text
     *            Texte du bouton
     */
    public void addResetButton(String text) {
        addResetButton(new Button(text));
    }

    /**
     * Ajoute un bouton qui remet à zéro le formulaire
     * @param button
     *            bouton à ajouter
     */
    public void addResetButton(Button button) {
        buttonPanel.add(button);
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                reset();
            }
        });
    }

    /**
     * Ajoute un bouton qui valide simplement le formulaire
     * @param text
     *            Texte du bouton
     */
    public void addValidateButton(String text) {
        addValidateButton(new Button(text));
    }

    /**
     * Ajoute un bouton qui valide simplement le formulaire
     * @param button
     *            bouton à ajouter
     */
    public void addValidateButton(Button button) {
        buttonPanel.add(button);
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                validate();
            }
        });
    }

    /**
     * Ajoute un bouton sans action spécifique
     * @param text
     *            Texte du bouton
     */
    public void addButton(String text) {
        addValidateButton(new Button(text));
    }

    /**
     * Ajoute un bouton sans action spécifique
     * @param button
     *            bouton à ajouter
     */
    public void addButton(Button button) {
        buttonPanel.add(button);
    }

    /**
     * Supprime un bouton
     * @param button
     */
    public void removeButton(Button button) {
        buttonPanel.remove(button);
    }

    /**
     * Supprime tous les boutons
     */
    public void removeAllButton() {
        buttonPanel.clear();
    }

    /**
     * Retourne une map avec toutes les valeurs des composants du formulaires
     * @return
     */
    public Map<String, Object> getValues() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        for (LabelComponentBase<?, ?> element : listeComponent) {
            map.put(element.getName(), element.getValue());
        }
        return map;
    }

    /**
     * Définie les valeures des composants du formulaires
     * @param values
     */
    public void setValues(Map<String, Object> values) {
        for (LabelComponentBase<?, ?> element : listeComponent) {
            String name = element.getName();
            if (values.containsKey(name)) {
                element.setObjectValue(values.get(name));
            }
        }
    }
    
    /**
     * Réinitialise le formulaire
     */
    @Override
    public void reset() {
        super.reset();
        for (LabelComponentBase<?, ?> element : listeComponent) {
            element.clear();
        }
        checkValid();
    }

    /**
     * Envoie le formulaire
     */
    @Override
    public void submit() {
        if (validate()) {
            if (formSubmitNative) {
                super.submit();
            } else {
                FormPanel.SubmitEvent event = new FormPanel.SubmitEvent();
                fireEvent(event);
            }
        }
    }

    /**
     * Valide le formulaire
     * @return
     */
    public Boolean validate() {
        Boolean valid = true;
        for (LabelComponentBase<?, ?> element : listeComponent) {
            if (!element.validate())
                valid = false;
        }
        return valid;
    }

    /**
     * Si le formulaire est valid ou pas
     * @return
     */
    public Boolean isValid() {
        Boolean valid = true;
        for (LabelComponentBase<?, ?> element : listeComponent) {
            if (!element.isValid())
                valid = false;
        }
        return valid;
    }

    private void checkValid() {
        Boolean isValid = isValid();
        for (Button button : listSubmitButton) {
            button.setEnabled(isValid);
        }
    }

    /**
     * Si le composant n'a pas ne nom, il est automatiquement générer
     * @param component
     */
    private void setComponentName(LabelComponentBase<?, ?> component) {
        String name = component.getName();
        if (name == null) {
            name = primaryName + ++nameCounter;
            component.setName(name);
        }
    }

    /**
     * Si le composant contient un champ FileUpload, automatiquement change l'encodage et la méthode
     * du formulaire
     * @param component
     */
    private void isFileUpload(LabelComponentBase<?, ?> component) {
        if (component.getComponent() instanceof FileUpload) {
            setEncoding(FormPanel.ENCODING_MULTIPART);
            setMethod(FormPanel.METHOD_POST);
            formSubmitNative = true;
        }
    }

    private void addHandler(LabelComponentBase<?, ?> component) {
        component.addValueChangeHandler(this);
    }

    /**
     * @param primaryName
     *            Nom du formulaire, utilisé comme base pour nommer les composants n'ayant pas de
     *            nom
     */
    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    /**
     * @return primaryName
     */
    public String getPrimaryName() {
        return primaryName;
    }

    /**
     * @param formSubmitNative
     *            formSubmitNative à alimenter
     */
    public void setFormSubmitNative(Boolean formSubmitNative) {
        this.formSubmitNative = formSubmitNative;
    }

    /**
     * @return formSubmitNative
     */
    public Boolean getFormSubmitNative() {
        return formSubmitNative;
    }

    public void setConfig(Map<String, Object> config) {
        // TODO Auto-generated method stub

    }

    /**
     * @param autoEnabledButton
     *            autoEnabledButton à alimenter
     */
    public void setAutoEnabledButton(Boolean autoEnabledButton) {
        this.autoEnabledButton = autoEnabledButton;
    }

    /**
     * @return autoEnabledButton
     */
    public Boolean getAutoEnabledButton() {
        return autoEnabledButton;
    }

    public void onValueChange(ValueChangeEvent event) {
        checkValid();
    }
}
