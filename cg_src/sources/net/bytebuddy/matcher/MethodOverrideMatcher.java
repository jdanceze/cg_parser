package net.bytebuddy.matcher;

import java.util.List;
import java.util.Set;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/matcher/MethodOverrideMatcher.class */
public class MethodOverrideMatcher<T extends MethodDescription> extends ElementMatcher.Junction.AbstractBase<T> {
    private final ElementMatcher<? super TypeDescription.Generic> matcher;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.matcher.equals(((MethodOverrideMatcher) obj).matcher);
    }

    public int hashCode() {
        return (17 * 31) + this.matcher.hashCode();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.bytebuddy.matcher.ElementMatcher
    public /* bridge */ /* synthetic */ boolean matches(Object obj) {
        return matches((MethodOverrideMatcher<T>) ((MethodDescription) obj));
    }

    public MethodOverrideMatcher(ElementMatcher<? super TypeDescription.Generic> matcher) {
        this.matcher = matcher;
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x001d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean matches(T r6) {
        /*
            r5 = this;
            java.util.HashSet r0 = new java.util.HashSet
            r1 = r0
            r1.<init>()
            r7 = r0
            r0 = r6
            net.bytebuddy.description.type.TypeDefinition r0 = r0.getDeclaringType()
            java.util.Iterator r0 = r0.iterator()
            r8 = r0
        L14:
            r0 = r8
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L47
            r0 = r8
            java.lang.Object r0 = r0.next()
            net.bytebuddy.description.type.TypeDefinition r0 = (net.bytebuddy.description.type.TypeDefinition) r0
            r9 = r0
            r0 = r5
            r1 = r6
            r2 = r9
            boolean r0 = r0.matches(r1, r2)
            if (r0 != 0) goto L42
            r0 = r5
            r1 = r6
            r2 = r9
            net.bytebuddy.description.type.TypeList$Generic r2 = r2.getInterfaces()
            r3 = r7
            boolean r0 = r0.matches(r1, r2, r3)
            if (r0 == 0) goto L44
        L42:
            r0 = 1
            return r0
        L44:
            goto L14
        L47:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: net.bytebuddy.matcher.MethodOverrideMatcher.matches(net.bytebuddy.description.method.MethodDescription):boolean");
    }

    private boolean matches(MethodDescription target, List<? extends TypeDefinition> typeDefinitions, Set<TypeDescription> duplicates) {
        for (TypeDefinition anInterface : typeDefinitions) {
            if (duplicates.add(anInterface.asErasure()) && (matches(target, anInterface) || matches(target, anInterface.getInterfaces(), duplicates))) {
                return true;
            }
        }
        return false;
    }

    private boolean matches(MethodDescription target, TypeDefinition typeDefinition) {
        for (MethodDescription methodDescription : typeDefinition.getDeclaredMethods().filter(ElementMatchers.isVirtual())) {
            if (methodDescription.asSignatureToken().equals(target.asSignatureToken())) {
                if (this.matcher.matches(typeDefinition.asGenericType())) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public String toString() {
        return "isOverriddenFrom(" + this.matcher + ")";
    }
}
