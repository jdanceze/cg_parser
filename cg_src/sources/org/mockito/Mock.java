package org.mockito;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/Mock.class */
public @interface Mock {
    Answers answer() default Answers.RETURNS_DEFAULTS;

    boolean stubOnly() default false;

    String name() default "";

    Class<?>[] extraInterfaces() default {};

    boolean serializable() default false;

    boolean lenient() default false;
}
