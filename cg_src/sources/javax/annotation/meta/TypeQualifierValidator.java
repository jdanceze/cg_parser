package javax.annotation.meta;

import java.lang.annotation.Annotation;
import javax.annotation.Nonnull;
/* loaded from: gencallgraphv3.jar:jsr305-3.0.2.jar:javax/annotation/meta/TypeQualifierValidator.class */
public interface TypeQualifierValidator<A extends Annotation> {
    @Nonnull
    When forConstantValue(@Nonnull A a, Object obj);
}
