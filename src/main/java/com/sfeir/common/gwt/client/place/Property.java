package com.sfeir.common.gwt.client.place;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
	/**
	 * Description of the use of property
	 * @return
	 */
	String value() default "";
	/**
	 * If this property is required
	 * If it's false, the property will display on the token only if the value isn't null.
	 * @return
	 */
	boolean required() default false;

	/**
	 * If this property is the default token (history compatibility) and have a more compact token
	 * @return
	 */
	boolean defaultToken() default false;
}
