package com.sfeir.common.gwt.client.component.label.base;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

public interface LabelComponentResources extends ClientBundle {

    @Source("labelcomponentstyle.css")
    @NotStrict
    Style style();

    public interface Style extends CssResource {

	String mandatory();
	
	String defaultLabelWidth();
	
	String defaultLabelHeight();
	
	@ClassName("error-label")
	String errorLabel();

	@ClassName("lazy-tab-panel")
	String lazyTabPanel();

	@ClassName("list-check-box-panel")
	String listCheckBoxPanel();

	@ClassName("form-panel-buttons")
	String formPanelButtons();

	@ClassName("error-field")
	String errorField();

	@ClassName("form-panel-components")
	String formPanelComponents();

	@ClassName("form-panel-root")
	String formPanelRoot();

	@ClassName("field-panel")
	String fieldPanel();

	@ClassName("list-radio-box-panel")
	String listRadioBoxPanel();

	@ClassName("field-panel-error")
	String fieldPanelError();

	@ClassName("mandatory-field")
	String mandatoryField();

	@ClassName("label-panel")
	String labelPanel();

	@ClassName("gwt-Button")
	String gwtButton();

	@ClassName("label-component")
	String labelComponent();

    }

}
