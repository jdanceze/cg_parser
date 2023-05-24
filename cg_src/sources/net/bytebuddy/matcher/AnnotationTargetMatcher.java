package net.bytebuddy.matcher;

import java.lang.annotation.ElementType;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.matcher.ElementMatcher;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/matcher/AnnotationTargetMatcher.class */
public class AnnotationTargetMatcher<T extends AnnotationDescription> extends ElementMatcher.Junction.AbstractBase<T> {
    private final ElementType elementType;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.elementType.equals(((AnnotationTargetMatcher) obj).elementType);
    }

    public int hashCode() {
        return (17 * 31) + this.elementType.hashCode();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.bytebuddy.matcher.ElementMatcher
    public /* bridge */ /* synthetic */ boolean matches(Object obj) {
        return matches((AnnotationTargetMatcher<T>) ((AnnotationDescription) obj));
    }

    public AnnotationTargetMatcher(ElementType elementType) {
        this.elementType = elementType;
    }

    public boolean matches(T target) {
        return target.getElementTypes().contains(this.elementType);
    }

    public String toString() {
        return "targetsElement(" + this.elementType + ")";
    }
}
