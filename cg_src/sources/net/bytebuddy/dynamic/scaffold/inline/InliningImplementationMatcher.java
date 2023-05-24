package net.bytebuddy.dynamic.scaffold.inline;

import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.LatentMatcher;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/inline/InliningImplementationMatcher.class */
public class InliningImplementationMatcher implements LatentMatcher<MethodDescription> {
    private final LatentMatcher<? super MethodDescription> ignoredMethods;
    private final ElementMatcher<? super MethodDescription> predefinedMethodSignatures;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.ignoredMethods.equals(((InliningImplementationMatcher) obj).ignoredMethods) && this.predefinedMethodSignatures.equals(((InliningImplementationMatcher) obj).predefinedMethodSignatures);
    }

    public int hashCode() {
        return (((17 * 31) + this.ignoredMethods.hashCode()) * 31) + this.predefinedMethodSignatures.hashCode();
    }

    protected InliningImplementationMatcher(LatentMatcher<? super MethodDescription> ignoredMethods, ElementMatcher<? super MethodDescription> predefinedMethodSignatures) {
        this.ignoredMethods = ignoredMethods;
        this.predefinedMethodSignatures = predefinedMethodSignatures;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static LatentMatcher<MethodDescription> of(LatentMatcher<? super MethodDescription> ignoredMethods, TypeDescription originalType) {
        ElementMatcher.Junction<MethodDescription> named;
        ElementMatcher.Junction<MethodDescription> predefinedMethodSignatures = ElementMatchers.none();
        for (MethodDescription methodDescription : originalType.getDeclaredMethods()) {
            if (methodDescription.isConstructor()) {
                named = ElementMatchers.isConstructor();
            } else {
                named = ElementMatchers.named(methodDescription.getName());
            }
            ElementMatcher.Junction<MethodDescription> signature = named;
            predefinedMethodSignatures = predefinedMethodSignatures.or(signature.and(ElementMatchers.returns(methodDescription.getReturnType().asErasure())).and(ElementMatchers.takesArguments(methodDescription.getParameters().asTypeList().asErasures())));
        }
        return new InliningImplementationMatcher(ignoredMethods, predefinedMethodSignatures);
    }

    @Override // net.bytebuddy.matcher.LatentMatcher
    public ElementMatcher<? super MethodDescription> resolve(TypeDescription typeDescription) {
        return ElementMatchers.not(this.ignoredMethods.resolve(typeDescription)).and(ElementMatchers.isVirtual().and(ElementMatchers.not(ElementMatchers.isFinal())).or(ElementMatchers.isDeclaredBy(typeDescription))).or(ElementMatchers.isDeclaredBy(typeDescription).and(ElementMatchers.not(this.predefinedMethodSignatures)));
    }
}
