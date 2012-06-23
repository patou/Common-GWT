package com.sfeir.common.gwt.client.component.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * Valide que les valeurs qui sont comprises dans un tableau
 * @author sfeir
 */
public class InArrayValidator<V> extends Validator<V> {
    private String arrayErrorText = messages.arrayErrorText();
    private List<V> array;
    
    public InArrayValidator() {
    }
    
    public InArrayValidator(List<V> array) {
        super();
        setArray(array);
    }
    
    public InArrayValidator(String arrayErrorText) {
        super();
        this.arrayErrorText = arrayErrorText;
    }
    
    public InArrayValidator(List<V> array, String arrayErrorText) {
        super();
        setArray(array);
        this.arrayErrorText = arrayErrorText;
    }

    @Override
    public String validate(V value) {
        if (value == null) 
            return null;
        if (!getArray().contains(value))
            return arrayErrorText;
        return null;
    }
    
    @Override
    public void setConfig(Map<String, Object> config) {
        if (config.containsKey("arrayErrorText")) {
            setArrayErrorText((String) config.get("arrayErrorText"));
        }
    }

    public void setArrayErrorText(String mandatoryErrorText) {
        this.arrayErrorText = mandatoryErrorText;
    }

    public String getArrayErrorText() {
        return arrayErrorText;
    }

    public void setArray(List<V> array) {
        this.array = array;
    }

    public List<V> getArray() {
        if (array == null)
            array = new ArrayList<V>();
        return array;
    }
    
    public void addValue(V value) {
        getArray().add(value);
    }
    
    public void removeValue(V value) {
        getArray().remove(value);
    }

}
