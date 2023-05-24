package javax.annotation;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;
@TypeQualifier(applicableTo = Number.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:jsr305-3.0.2.jar:javax/annotation/Nonnegative.class */
public @interface Nonnegative {
    When when() default When.ALWAYS;

    /* loaded from: gencallgraphv3.jar:jsr305-3.0.2.jar:javax/annotation/Nonnegative$Checker.class */
    public static class Checker implements TypeQualifierValidator<Nonnegative> {
        @Override // javax.annotation.meta.TypeQualifierValidator
        public When forConstantValue(Nonnegative annotation, Object v) {
            boolean isNegative;
            if (!(v instanceof Number)) {
                return When.NEVER;
            }
            Number value = (Number) v;
            if (value instanceof Long) {
                isNegative = value.longValue() < 0;
            } else if (value instanceof Double) {
                isNegative = value.doubleValue() < Const.default_value_double;
            } else if (value instanceof Float) {
                isNegative = value.floatValue() < 0.0f;
            } else {
                isNegative = value.intValue() < 0;
            }
            if (isNegative) {
                return When.NEVER;
            }
            return When.ALWAYS;
        }
    }
}
