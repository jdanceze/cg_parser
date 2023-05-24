package com.google.gson.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:gson-2.8.9.jar:com/google/gson/annotations/JsonAdapter.class */
public @interface JsonAdapter {
    Class<?> value();

    boolean nullSafe() default true;
}
