package net.bytebuddy.matcher;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.matcher.ElementMatcher;
@HashCodeAndEqualsPlugin.Enhance(permitSubclassEquality = true)
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/matcher/CachingMatcher.class */
public class CachingMatcher<T> extends ElementMatcher.Junction.AbstractBase<T> {
    private final ElementMatcher<? super T> matcher;
    @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.IGNORE)
    protected final ConcurrentMap<? super T, Boolean> map;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof CachingMatcher) && this.matcher.equals(((CachingMatcher) obj).matcher);
    }

    public int hashCode() {
        return (17 * 31) + this.matcher.hashCode();
    }

    public CachingMatcher(ElementMatcher<? super T> matcher, ConcurrentMap<? super T, Boolean> map) {
        this.matcher = matcher;
        this.map = map;
    }

    @Override // net.bytebuddy.matcher.ElementMatcher
    public boolean matches(T target) {
        Boolean cached = this.map.get(target);
        if (cached == null) {
            cached = Boolean.valueOf(onCacheMiss(target));
        }
        return cached.booleanValue();
    }

    protected boolean onCacheMiss(T target) {
        boolean cached = this.matcher.matches(target);
        this.map.put(target, Boolean.valueOf(cached));
        return cached;
    }

    public String toString() {
        return "cached(" + this.matcher + ")";
    }

    @SuppressFBWarnings(value = {"EQ_DOESNT_OVERRIDE_EQUALS"}, justification = "Equality does not consider eviction size")
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/matcher/CachingMatcher$WithInlineEviction.class */
    public static class WithInlineEviction<S> extends CachingMatcher<S> {
        private final int evictionSize;

        public WithInlineEviction(ElementMatcher<? super S> matcher, ConcurrentMap<? super S, Boolean> map, int evictionSize) {
            super(matcher, map);
            this.evictionSize = evictionSize;
        }

        @Override // net.bytebuddy.matcher.CachingMatcher
        protected boolean onCacheMiss(S target) {
            if (this.map.size() >= this.evictionSize) {
                Iterator<?> iterator = this.map.entrySet().iterator();
                iterator.next();
                iterator.remove();
            }
            return super.onCacheMiss(target);
        }
    }
}
