package com.sfeir.common.gwt.client.place;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add this anotation on a Place to indicate that the current place must be display in a DialoguBox
 * 
 *
 */
@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Dialog {
	/**
	 * The width of the DialogBox
	 * @return
	 */
	String width();
	/**
	 * The height of the DialogBox
	 * @return
	 */
	String height();
	/**
	 * The title of the caption
	 * @return
	 */
	String caption() default "";
	/**
	 * If the 
	 * @return
	 */
	boolean isModal() default false;
}
