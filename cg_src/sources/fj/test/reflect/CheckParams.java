package fj.test.reflect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/reflect/CheckParams.class */
public @interface CheckParams {
    int minSuccessful() default 100;

    int maxDiscarded() default 500;

    int minSize() default 0;

    int maxSize() default 100;
}
