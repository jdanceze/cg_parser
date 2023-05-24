package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:jsr305-3.0.2.jar:javax/annotation/OverridingMethodsMustInvokeSuper.class */
public @interface OverridingMethodsMustInvokeSuper {
}
