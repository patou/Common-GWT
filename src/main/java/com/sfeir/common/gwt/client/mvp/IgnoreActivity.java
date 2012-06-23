package com.sfeir.common.gwt.client.mvp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to add to a Activity type to ignore the Activity from the Activity mapper
 * 
 */
@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreActivity {

}
