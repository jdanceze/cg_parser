package com.google.j2objc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
/* loaded from: gencallgraphv3.jar:j2objc-annotations-1.3.jar:com/google/j2objc/annotations/Property.class */
public @interface Property {
    String value() default "";
}
