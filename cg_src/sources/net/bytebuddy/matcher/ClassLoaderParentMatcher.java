package net.bytebuddy.matcher;

import java.lang.ClassLoader;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.matcher.ElementMatcher;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/matcher/ClassLoaderParentMatcher.class */
public class ClassLoaderParentMatcher<T extends ClassLoader> extends ElementMatcher.Junction.AbstractBase<T> {
    private final ClassLoader classLoader;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.classLoader.equals(((ClassLoaderParentMatcher) obj).classLoader);
    }

    public int hashCode() {
        return (17 * 31) + this.classLoader.hashCode();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.bytebuddy.matcher.ElementMatcher
    public /* bridge */ /* synthetic */ boolean matches(Object obj) {
        return matches((ClassLoaderParentMatcher<T>) ((ClassLoader) obj));
    }

    public ClassLoaderParentMatcher(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public boolean matches(T target) {
        ClassLoader classLoader = this.classLoader;
        while (true) {
            ClassLoader current = classLoader;
            if (current == null) {
                return target == null;
            } else if (current == target) {
                return true;
            } else {
                classLoader = current.getParent();
            }
        }
    }

    public String toString() {
        return "isParentOf(" + this.classLoader + ')';
    }
}
