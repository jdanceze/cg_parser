package com.google.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@GwtCompatible
@Retention(RetentionPolicy.CLASS)
@Documented
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/annotations/Beta.class */
public @interface Beta {
}
