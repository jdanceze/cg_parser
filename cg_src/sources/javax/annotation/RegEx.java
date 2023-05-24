package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;
@Syntax("RegEx")
@TypeQualifierNickname
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:jsr305-3.0.2.jar:javax/annotation/RegEx.class */
public @interface RegEx {
    When when() default When.ALWAYS;

    /* loaded from: gencallgraphv3.jar:jsr305-3.0.2.jar:javax/annotation/RegEx$Checker.class */
    public static class Checker implements TypeQualifierValidator<RegEx> {
        @Override // javax.annotation.meta.TypeQualifierValidator
        public When forConstantValue(RegEx annotation, Object value) {
            if (!(value instanceof String)) {
                return When.NEVER;
            }
            try {
                Pattern.compile((String) value);
                return When.ALWAYS;
            } catch (PatternSyntaxException e) {
                return When.NEVER;
            }
        }
    }
}
