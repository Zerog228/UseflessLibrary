package io.github.zerog228.usefless.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking specified methods for using them on
 * auto-conversion from json to custom item
 * */
//TODO Annotate methods from item builder
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JConvert {
    public String key();
}
