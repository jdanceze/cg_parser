package net.bytebuddy.matcher;

import java.util.List;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.matcher.ElementMatcher;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/matcher/CollectionOneToOneMatcher.class */
public class CollectionOneToOneMatcher<T> extends ElementMatcher.Junction.AbstractBase<Iterable<? extends T>> {
    private final List<? extends ElementMatcher<? super T>> matchers;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.matchers.equals(((CollectionOneToOneMatcher) obj).matchers);
    }

    public int hashCode() {
        return (17 * 31) + this.matchers.hashCode();
    }

    @Override // net.bytebuddy.matcher.ElementMatcher
    public /* bridge */ /* synthetic */ boolean matches(Object obj) {
        return matches((Iterable) ((Iterable) obj));
    }

    public CollectionOneToOneMatcher(List<? extends ElementMatcher<? super T>> matchers) {
        this.matchers = matchers;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0038  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean matches(java.lang.Iterable<? extends T> r4) {
        /*
            r3 = this;
            r0 = r4
            boolean r0 = r0 instanceof java.util.Collection
            if (r0 == 0) goto L1e
            r0 = r4
            java.util.Collection r0 = (java.util.Collection) r0
            int r0 = r0.size()
            r1 = r3
            java.util.List<? extends net.bytebuddy.matcher.ElementMatcher<? super T>> r1 = r1.matchers
            int r1 = r1.size()
            if (r0 == r1) goto L1e
            r0 = 0
            return r0
        L1e:
            r0 = r3
            java.util.List<? extends net.bytebuddy.matcher.ElementMatcher<? super T>> r0 = r0.matchers
            java.util.Iterator r0 = r0.iterator()
            r5 = r0
            r0 = r4
            java.util.Iterator r0 = r0.iterator()
            r6 = r0
        L2f:
            r0 = r6
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L61
            r0 = r6
            java.lang.Object r0 = r0.next()
            r7 = r0
            r0 = r5
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L5c
            r0 = r5
            java.lang.Object r0 = r0.next()
            net.bytebuddy.matcher.ElementMatcher r0 = (net.bytebuddy.matcher.ElementMatcher) r0
            r1 = r7
            boolean r0 = r0.matches(r1)
            if (r0 != 0) goto L5e
        L5c:
            r0 = 0
            return r0
        L5e:
            goto L2f
        L61:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: net.bytebuddy.matcher.CollectionOneToOneMatcher.matches(java.lang.Iterable):boolean");
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("containing(");
        boolean first = true;
        for (Object value : this.matchers) {
            if (first) {
                first = false;
            } else {
                stringBuilder.append(", ");
            }
            stringBuilder.append(value);
        }
        return stringBuilder.append(')').toString();
    }
}
