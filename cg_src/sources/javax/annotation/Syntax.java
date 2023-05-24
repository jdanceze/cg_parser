package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.When;
@TypeQualifier(applicableTo = CharSequence.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:jsr305-3.0.2.jar:javax/annotation/Syntax.class */
public @interface Syntax {
    String value();

    When when() default When.ALWAYS;
}
