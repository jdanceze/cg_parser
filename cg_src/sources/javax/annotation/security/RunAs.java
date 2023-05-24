package javax.annotation.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:javax.annotation-api-1.3.2.jar:javax/annotation/security/RunAs.class */
public @interface RunAs {
    String value();
}
