package com.google.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.TYPE, ElementType.METHOD})
@GwtCompatible
@Retention(RetentionPolicy.CLASS)
@Documented
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/annotations/GwtCompatible.class */
public @interface GwtCompatible {
    boolean serializable() default false;

    boolean emulated() default false;
}
