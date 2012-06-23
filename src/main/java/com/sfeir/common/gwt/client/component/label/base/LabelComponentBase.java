package com.sfeir.common.gwt.client.component.label.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.sfeir.common.gwt.client.component.HasConfig;
import com.sfeir.common.gwt.client.component.event.KeyEnterPressEvent;
import com.sfeir.common.gwt.client.component.formator.FormatorEvent;
import com.sfeir.common.gwt.client.component.formator.FormatorHandler;
import com.sfeir.common.gwt.client.component.formator.HasFormatorHandlers;
import com.sfeir.common.gwt.client.component.label.event.HasValidationErrorHandlers;
import com.sfeir.common.gwt.client.component.label.event.ValidationErrorEvent;
import com.sfeir.common.gwt.client.component.label.event.ValidationErrorHandler;
import com.sfeir.common.gwt.client.component.validator.HasValidatorHandlers;
import com.sfeir.common.gwt.client.component.validator.MandatoryValidator;
import com.sfeir.common.gwt.client.component.validator.ValidatorEvent;
import com.sfeir.common.gwt.client.component.validator.ValidatorHandler;

/**
 * Classe de base des composants avec un Label
 * 
 * @author sfeir
 * @param <T>
 *            Type du Composant
 * @param <V>
 *            Type de la valeur du composant
 */

public abstract class LabelComponentBase<T extends Widget, V> extends Composite implements HasText, FocusHandler, BlurHandler, HasValue<V>, ValueChangeHandler<V>,
		HasFormatorHandlers<V>, HasValidatorHandlers<V>, HasConfig, HasValueChangeHandlers<V>, ChangeHandler, HasValidationErrorHandlers, HasAllFocusHandlers, Focusable,
		IsEditor<TakesValueEditor<V>> {

	private static LabelComponentResources resources;
	// private static final String ERROR_DEPENDENT_STYLENAME = "error";
	// private static final String MANDATORY_FIELD = "*";
	// private static final String LABEL_COMPONENT_STYLENAME = "label-component";
	// private static final String LABEL_PANEL_STYLENAME = "label-panel";
	// private static final String ERROR_PANEL_STYLENAME = "error-panel";
	// private static final String ERROR_LABEL_STYLENAME = "error-label";
	// private static final String MANDATORY_FIELD_STYLENAME = "mandatory-field";
	// private static final String ERROR_FIELD_STYLENAME = "error-field";
	// private static final String COMPONENT_PANEL_STYLENAME = "field-panel";

	protected FlowPanel panel = null;
	private Label label = null;
	private T component;
	private V value;
	private V formatedValue;
	private Boolean isMandatory = false;
	// public static String defaultLabelWidth = "200px";
	// public static String defaultLabelHeight = null;
	private String labelWidth/* = defaultLabelWidth */;
	private String labelHeight/* = defaultLabelHeight */;
	private Label mandatoryField = null;
	private FlowPanel errorPanel;
	private List<Label> listeLabelError = new ArrayList<Label>();
	private List<String> listeError = new ArrayList<String>(3);
	private Boolean displayErrorsLabel = true;
	private FlowPanel labelPanel;
	private FlowPanel componentPanel;

	private MandatoryValidator<V> mandatoryValidator = new MandatoryValidator<V>();
	private HandlerRegistration mandatoryHandlerRegistration;
	private V defaultValue = null;
	private String name = null;

	/**
	 * Constructeur vide, crée un label vide et un champ non obligatoire
	 * 
	 * @wbp.parser.constructor
	 */
	public LabelComponentBase() {
		this("", false);
	}

	/**
	 * Constructeur avec le label
	 * 
	 * @param text
	 *            Label texte
	 */
	public LabelComponentBase(String text) {
		this(text, false);
	}

	/**
	 * 
	 * @param text
	 *            Label text
	 * @param mandatory
	 *            si le champ est obligatoire
	 */
	public LabelComponentBase(String text, boolean mandatory) {
		this(text, mandatory, null);
	}

	/**
	 * 
	 * @param text
	 *            Label text
	 * @param mandatory
	 *            si le champ est obligatoire
	 */
	public LabelComponentBase(String text, boolean mandatory, LabelComponentResources resources) {
		setResources(resources);
		this.labelWidth = LabelComponentBase.resources.style().defaultLabelWidth();
		this.labelHeight = LabelComponentBase.resources.style().defaultLabelHeight();
		initWidget(createWidget());
		setText(text);
		setMandatory(mandatory);
	}

	/**
	 * 
	 * @param config
	 *            Map de configuration du composant
	 */
	public LabelComponentBase(Map<String, Object> config) {
		this("", false);
		setConfig(config);
	}

	public static void setResources(LabelComponentResources resources) {
		if (resources != null) {
			resources.style().ensureInjected();
			LabelComponentBase.resources = resources;
		} else {
			if (LabelComponentBase.resources == null) {
				LabelComponentBase.resources = GWT.create(LabelComponentResources.class);
				LabelComponentBase.resources.style().ensureInjected();
			}
		}
	}

	/**
	 * Crée le composant globale (layout du composant) Appellé directement par le constructeur
	 * 
	 * @return Widget
	 */
	protected Widget createWidget() {
		panel = new FlowPanel();
		panel.setStylePrimaryName(resources.style().labelComponent());
		panel.add(createLabelPanel());
		panel.add(createComponentPanel());
		panel.add(createErrorPanel());
		return panel;
	}

	/**
	 * Retroune le label qui indique que le champ est obligatoire
	 * 
	 * @return
	 */
	protected Widget getMandatoryField() {
		if (mandatoryField == null) {
			mandatoryField = new InlineLabel(resources.style().mandatory());
			mandatoryField.setStyleName(resources.style().mandatoryField());
		}
		return mandatoryField;
	}

	/**
	 * Construit le conteneur du label (le label et le label obligatoire si présent) La taille du label est appliqué sur ce conteneur
	 * 
	 * @return
	 */
	protected FlowPanel createLabelPanel() {
		labelPanel = new FlowPanel();
		labelPanel.setStylePrimaryName(resources.style().labelPanel());
		labelPanel.add(getLabel());
		if (isMandatory) {
			labelPanel.add(getMandatoryField());
		}
		applyLabelSize();
		return labelPanel;
	}

	/**
	 * Construit le conteneur du champ
	 * 
	 * @return
	 */
	protected FlowPanel createComponentPanel() {
		componentPanel = new FlowPanel();
		componentPanel.setStylePrimaryName(resources.style().fieldPanel());
		componentPanel.add(getComponent());
		return componentPanel;
	}

	/**
	 * Construit le conteneur d'erreurs
	 * 
	 * @return
	 */
	protected FlowPanel createErrorPanel() {
		errorPanel = new FlowPanel();
		errorPanel.setStylePrimaryName(resources.style().fieldPanelError());
		return errorPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sfeir.gwt.webding.client.component.HasConfig#setConfig(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void setConfig(Map<String, Object> config) {
		if (config.containsKey("text")) {
			setText((String) config.get("text"));
		}
		if (config.containsKey("name")) {
			setName((String) config.get("name"));
		}
		if (config.containsKey("mandatory")) {
			setMandatory((Boolean) config.get("mandatory"));
		}
		if (config.containsKey("value")) {
			setValue((V) config.get("value"), false);
		}
		if (config.containsKey("labelWidth")) {
			setLabelWidth((String) config.get("labelWidth"));
		}
		if (config.containsKey("labelHeight")) {
			setLabelHeight((String) config.get("labelHeight"));
		}
		if (config.containsKey("errors")) {
			setErrors((List<String>) config.get("errors"));
		}
		if (config.containsKey("mandatoryErrorText")) {
			setMandatoryErrorText((String) config.get("mandatoryErrorText"));
		}
	}

	// ---------------- Label management
	/**
	 * Retourne le label du champ Utilise le InlineLabel pour ne pas avoir un retour à la ligne entre le label et le champ
	 */
	public Label getLabel() {
		if (label == null) {
			label = new InlineLabel();
		}
		return label;
	}

	/**
	 * Définie un autre label (supprime le label courant et ajoute le nouveau)
	 * 
	 * @param label
	 */
	public void setLabel(Label label) {
		labelPanel.remove(this.label);
		this.label = label;
		labelPanel.insert(label, 0);
	}

	/**
	 * Définie le texte du label
	 */
	public void setText(String text) {
		Label label = getLabel();
		label.setText(text);
	}

	/**
	 * Retourne le texte du label
	 */
	public String getText() {
		Label label = getLabel();
		return label.getText();
	}

	// ---------------- Error management
	/**
	 * Definie l'erreur à afficher (efface les erreurs existantes)
	 */
	public void setError(String errorText) {
		clearErrors();
		addError(errorText);
	}

	public void setErrors(List<String> errors) {
		clearErrors();
		for (String errorText : errors) {
			addError(errorText);
		}
	}

	/**
	 * Ajoute une erreur dans la liste des erreures Si c'est la première erreur, le champ est indiqué comme non valide
	 * 
	 * @param errorText
	 */
	public void addError(String errorText) {
		if (listeError.size() == 0) {
			getComponent().addStyleName(resources.style().errorField());
			componentPanel.addStyleName(resources.style().fieldPanelError());
		}
		listeError.add(errorText);
		if (displayErrorsLabel) {
			Label errorLabel = createErrorLabel(errorText);
			listeLabelError.add(errorLabel);
			errorPanel.add(errorLabel);
		}
	}

	/**
	 * Indique si le champ est valide
	 * 
	 * @return
	 */
	public boolean isValid() {
		return validate(true);
	}

	/**
	 * Indique si le champ a des erreures
	 * 
	 * @return
	 */
	public boolean hasErrors() {
		return !isValid();
	}

	/**
	 * Efface les erreurs Réinitialise le champs comme valide
	 */
	public void clearErrors() {
		errorPanel.clear();
		listeLabelError.clear();
		listeError.clear();
		getComponent().removeStyleName(resources.style().errorField());
		componentPanel.removeStyleName(resources.style().fieldPanelError());
	}

	public List<String> getErrors() {
		return listeError;
	}

	public HandlerRegistration addValidationErrorHandler(ValidationErrorHandler handler) {
		return addHandler(handler, ValidationErrorEvent.getType());
	}

	/**
	 * Construit le label qui affiche l'erreur
	 * 
	 * @param errorText
	 *            le message d'erreur
	 * @return
	 */
	protected Label createErrorLabel(String errorText) {
		Label label = new Label(errorText);
		label.setStyleName(resources.style().errorLabel());
		return label;
	}

	/**
	 * Définie si le champ est obligatoire ou pas
	 * 
	 * @param isMandatory
	 */
	public void setMandatory(Boolean isMandatory) {
		if (isMandatory) {
			labelPanel.add(getMandatoryField());
			mandatoryHandlerRegistration = addValidator(mandatoryValidator);
		} else if (mandatoryField != null) {
			labelPanel.remove(mandatoryField);
			mandatoryHandlerRegistration.removeHandler();
		}
		this.isMandatory = isMandatory;
	}

	/**
	 * Indique si le champ est obligatoire
	 * 
	 * @return
	 */
	public boolean isMandatory() {
		return isMandatory;
	}

	/**
	 * Réinitialise tous le composant Efface la valeur (remet la valeur par défaut du composant) Efface les erreures
	 */
	public void clear() {
		if (defaultValue == null) {
			value = defaultValue();
		} else {
			value = defaultValue;
		}
		clearComponent();
		clearErrors();
	}

	// ---------- Methode Abstraite à implémenter dans les sous classes

	/**
	 * Crée le composant a afficher
	 * 
	 * @return
	 */
	protected abstract T createComponent();

	/**
	 * Doit remettre à zéro l'état du composant
	 */
	public abstract void clearComponent();

	/**
	 * Valeur par défaut
	 */
	public V defaultValue() {
		return null;
	}

	// HasValue<V> interface
	/**
	 * Retourne la valeur entrée
	 * 
	 * @see HasValue
	 * @return
	 */
	public V getValue() {
		return value;
	}

	/**
	 * Definie la valeur
	 * 
	 * @see HasValue
	 */
	public void setValue(V value) {
		setValue(value, true);
	}

	/**
	 * Retourne la valeur interne du composant
	 * 
	 * @return
	 */
	protected V getInternValue() {
		return value;
	}

	/**
	 * Définie la valeur interne du composant
	 * 
	 * @param value
	 */
	protected void setInternValue(V value) {
		this.value = value;
	}

	/**
	 * @see HasValue
	 */
	public void setValue(V value, boolean fireEvents) {
		final V oldValue = this.value;
		this.value = value;
		this.defaultValue = value;
		this.formatedValue = formatValue(value);
		setComponentValue(this.formatedValue, fireEvents);
		if (fireEvents) {
			ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
		}
	}

	/**
	 * Définie la valeur du composant
	 * 
	 * @param value
	 * @param fireEvents
	 *            indique si il faut lancer l'événement pour indiquer le changement de la valeur
	 */
	@SuppressWarnings("unchecked")
	protected void setComponentValue(V value, boolean fireEvents) {
		T component = getComponent();
		if (component instanceof HasValue) {
			((HasValue<V>) component).setValue(value, fireEvents);
		}
	}

	/**
	 * Retourne la valeur du composant
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected V getComponentValue() {
		T component = getComponent();
		V value = null;
		if (component instanceof HasValue) {
			value = (V) ((HasValue) component).getValue();
		}
		return value;
	}

	/**
	 * Retourne la valeur du composant actuel (non formaté si le composant à le focus, sinon formaté)
	 * 
	 * @return
	 */
	public V getRawValue() {
		return getComponentValue();
	}

	/**
	 * Définie la valeur du composant sans mettre à jour la valeur interne sauvegarder du composant
	 * 
	 * @param value
	 */
	public void setRawValue(V value) {
		setComponentValue(value, true);
	}

	/**
	 * Définie la valeur formaté
	 * 
	 * @param formatedValue
	 */
	public void setFormatedValue(V formatedValue) {
		this.formatedValue = formatedValue;
	}

	/**
	 * Retourne la valeur formaté
	 * 
	 * @return
	 */
	public V getFormatedValue() {
		return formatedValue;
	}

	/**
	 * Retourne le composant
	 * 
	 * @return
	 */
	public T getComponent() {
		if (component == null) {
			component = createComponent();
			addComponentFocusHandler(component);
		}
		return component;
	}

	/**
	 * Ajoute automatiquement les evenements focus et blur Si le composant est une suggestbox, alors applique cela sur le textbox Sinon la validation et le formatage n'est pas
	 * disponible
	 * 
	 * @param component
	 */
	@SuppressWarnings("unchecked")
	private void addComponentFocusHandler(final Widget component) {
		if (component instanceof SuggestBox) {
			addComponentFocusHandler(((SuggestBox) component).getTextBox());
		}
		if (component instanceof DateBox) {
			addComponentFocusHandler(((DateBox) component).getTextBox());
		}
		if (component instanceof HasAllFocusHandlers) {
			HasAllFocusHandlers hasAllFocusHandlers = (HasAllFocusHandlers) component;
			hasAllFocusHandlers.addFocusHandler(this);
			hasAllFocusHandlers.addBlurHandler(this);
		}
		if (component instanceof HasValueChangeHandlers) {
			HasValueChangeHandlers<V> hasValueChangeHandlers = (HasValueChangeHandlers<V>) component;
			if (hasValueChangeHandlers != null) {
				hasValueChangeHandlers.addValueChangeHandler(this);
			}
		}
		if (component instanceof HasChangeHandlers) {
			HasChangeHandlers hasChangeHandlers = (HasChangeHandlers) component;
			hasChangeHandlers.addChangeHandler(this);
		}
		if (component instanceof Focusable) {
			label.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					((Focusable) component).setFocus(true);
				}
			});
		}
		if (component instanceof HasKeyUpHandlers) {
			((HasKeyUpHandlers) component).addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.isAnyModifierKeyDown()) {
						validate();
						fireEvent(new KeyEnterPressEvent());
					}
				}
			});
		}
	}

	/**
	 * Evénement lors de la prise du focus Rétablie la vrai valeur
	 */
	public void onFocus(FocusEvent event) {
		// setComponentValue(value, false);
	}

	/**
	 * Evénement lors de la perte de focus Valide la valeur Formate la valeur
	 */
	public void onBlur(BlurEvent event) {
		validate();
	}

	/**
	 * Valide le champ, et affiche les messages d'erreurs
	 * 
	 * @return true si le champ est valide
	 */
	public Boolean validate() {
		return validate(false);
	}

	/**
	 * Valide le formulaire
	 * 
	 * @param quiet
	 *            Si vrai, n'affiche pas les messages d'erreurs mais l'évènement ValidationError est lancé
	 * @return
	 */
	public Boolean validate(Boolean quiet) {
		V value = getComponentValue();
		clearErrors();
		ValidatorEvent<V> valid = ValidatorEvent.validate(this, value);
		if (valid != null && valid.hasErrors()) {
			if (!quiet) {
				setErrors(valid.getErrors());
				fireEvent(new ValidationErrorEvent(listeError));
			} else {
				fireEvent(new ValidationErrorEvent(valid.getErrors()));
			}
			return false;
		} else if (!quiet) {
			this.value = value;
			this.formatedValue = formatValue(value);
			setComponentValue(this.formatedValue, false);
		}
		return true;
	}

	/**
	 * handler pour les composant HasValueChange
	 */
	public void onValueChange(ValueChangeEvent<V> event) {
		onChange();
	}

	/**
	 * Permet d'envoyer l'évènement ValueChange lors que la selection change
	 */
	public void onChange(ChangeEvent event) {
		onChange();
	}

	/**
	 * Lors qu'il y a un changement dans la valeur du composant
	 */
	protected void onChange() {
		V oldValue = this.getInternValue();
		V newValue = getComponentValue();
		if (oldValue != newValue) {
			setInternValue(newValue);
			ValueChangeEvent.fireIfNotEqual(this, oldValue, newValue);
		}
	}

	/**
	 * Ajoute un formateur
	 */
	public HandlerRegistration addFormator(FormatorHandler<V> handler) {
		return addHandler(handler, FormatorEvent.getType());
	}

	/**
	 * Ajoute un validateur
	 */
	public HandlerRegistration addValidator(ValidatorHandler<V> handler) {
		return addHandler(handler, ValidatorEvent.getType());
	}

	/**
	 * Format la valeur
	 * 
	 * @param value
	 * @return
	 */
	private V formatValue(V value) {
		V formatedValue = FormatorEvent.format(this, value);
		setComponentValue(formatedValue, false);
		return formatedValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.event.logical.shared.HasValueChangeHandlers#addValueChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler)
	 */
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<V> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	/**
	 * Configure le texte à afficher si le champ n'est pas remplis
	 * 
	 * @param mandatoryErrorText
	 */

	public void setMandatoryErrorText(String mandatoryErrorText) {
		mandatoryValidator.setMandatoryErrorText(mandatoryErrorText);
	}

	public String getMandatoryErrorText() {
		return mandatoryValidator.getMandatoryErrorText();
	}

	/**
	 * Donne la largeur du label (s'applique sur le conteneur du label et du label de champ obligatoire "*" )
	 * 
	 * @param labelWidth
	 *            largeur
	 */
	public void setLabelWidth(String labelWidth) {
		this.labelWidth = labelWidth;
		applyLabelSize();
	}

	public String getLabelWidth() {
		return labelWidth;
	}

	/**
	 * Donne la hauteur du label (s'applique sur le conteneur du label et du label de champ obligatoire "*" )
	 * 
	 * @param labelHeight
	 *            largeur
	 */
	public void setLabelHeight(String labelHeight) {
		this.labelHeight = labelHeight;
		applyLabelSize();
	}

	public String getLabelHeight() {
		return labelHeight;
	}

	public void setLabelSize(String labelWidth, String labelHeight) {
		this.labelHeight = labelHeight;
		this.labelWidth = labelWidth;
		applyLabelSize();
	}

	/**
	 * Donne la largeur du Component
	 * 
	 * @param ComponentWidth
	 *            largeur
	 */
	public void setComponentWidth(String ComponentWidth) {
		getComponent().setWidth(ComponentWidth);
	}

	/**
	 * Donne la hauteur du Component
	 * 
	 * @param ComponentHeight
	 *            largeur
	 */
	public void setComponentHeight(String ComponentHeight) {
		getComponent().setHeight(ComponentHeight);
	}

	public void setComponentSize(String ComponentWidth, String ComponentHeight) {
		getComponent().setSize(ComponentWidth, ComponentHeight);
	}

	/**
	 * Definie le nom du champ
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
		T component = getComponent();
		if (component instanceof HasName) {
			((HasName) component).setName(name);
		}
	}

	public String getName() {
		return name;
	}

	/**
	 * Applique les largeurs et hauteurs du champ
	 */
	private void applyLabelSize() {
		if (labelWidth != null) {
			labelPanel.setWidth(labelWidth);
		}
		if (labelHeight != null) {
			labelPanel.setHeight(labelHeight);
		}
	}

	/**
	 * Définie la valeur par un objet en le castant dans le type donnée du composant
	 * 
	 * @param object
	 */
	public void setObjectValue(Object object) {
		setValue((V) value);
	}

	/**
	 * Définie si il faut afficher les messages d'érreurs en dessous
	 * 
	 * @param displayErrorsLabel
	 */
	public void setDisplayErrorsLabel(Boolean displayErrorsLabel) {
		this.displayErrorsLabel = displayErrorsLabel;
		if (displayErrorsLabel) {
			if (panel.getWidgetIndex(errorPanel) < 0) {
				panel.add(createErrorPanel());
			} else {
				panel.remove(errorPanel);
			}
		} else {
			panel.remove(errorPanel);
		}
	}

	public Boolean getDisplayErrorsLabel() {
		return displayErrorsLabel;
	}

	/**
	 * Get the resources style
	 * 
	 * @return
	 */
	protected LabelComponentResources getResources() {
		return resources;
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		if (component instanceof HasFocusHandlers)
			return ((HasFocusHandlers) component).addFocusHandler(handler);
		return null;
	}

	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		if (component instanceof HasBlurHandlers)
			return ((HasBlurHandlers) component).addBlurHandler(handler);
		return null;
	}

	/**
	 * Gets the widget's position in the tab index.
	 * 
	 * @return the widget's tab index
	 */
	public int getTabIndex() {
		if (component instanceof Focusable)
			return ((Focusable) component).getTabIndex();
		return -1;
	}

	/**
	 * Sets the widget's 'access key'. This key is used (in conjunction with a browser-specific modifier key) to automatically focus the widget.
	 * 
	 * @param key
	 *            the widget's access key
	 */
	public void setAccessKey(char key) {
		if (component instanceof Focusable)
			((Focusable) component).setAccessKey(key);
	}

	/**
	 * Explicitly focus/unfocus this widget. Only one widget can have focus at a time, and the widget that does will receive all keyboard events.
	 * 
	 * @param focused
	 *            whether this widget should take focus or release it
	 */
	public void setFocus(boolean focused) {
		if (component instanceof Focusable)
			((Focusable) component).setFocus(focused);
	}

	/**
	 * Sets the widget's position in the tab index. If more than one widget has the same tab index, each such widget will receive focus in an arbitrary order. Setting the tab index
	 * to <code>-1</code> will cause this widget to be removed from the tab order.
	 * 
	 * @param index
	 *            the widget's tab index
	 */
	public void setTabIndex(int index) {
		if (component instanceof Focusable)
			((Focusable) component).setTabIndex(index);
	}

	/**
	 * This event is lauched when the user press the enter key in a component
	 * 
	 * @param handler
	 */
	public HandlerRegistration addKeyEnterPressHandler(KeyEnterPressEvent.Handler handler) {
		return addHandler(handler, KeyEnterPressEvent.getType());
	}
	
	@Override
	public TakesValueEditor<V> asEditor() {
		return TakesValueEditor.of(this);
	}
}
