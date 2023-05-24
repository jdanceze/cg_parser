package org.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/Rule.class
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/Rule.class */
public @interface Rule {
    public static final int DEFAULT_ORDER = -1;

    int order() default -1;
}
