package com.sfeir.common.gwt.client.layout;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * With this annotation added to a Place, there are will have no layout for this Place (directly add the corresponding view in the global display)
 * 
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoLayout {
}