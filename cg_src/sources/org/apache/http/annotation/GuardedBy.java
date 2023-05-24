package org.apache.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.CLASS)
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/annotation/GuardedBy.class */
public @interface GuardedBy {
    String value();
}
