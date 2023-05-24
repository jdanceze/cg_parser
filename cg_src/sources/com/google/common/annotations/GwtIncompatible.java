package com.google.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
@GwtCompatible
@Retention(RetentionPolicy.CLASS)
@Documented
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/annotations/GwtIncompatible.class */
public @interface GwtIncompatible {
    String value() default "";
}
