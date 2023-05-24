package org.hamcrest.collection;

import java.util.Iterator;
import org.hamcrest.Factory;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/collection/IsIterableWithSize.class */
public class IsIterableWithSize<E> extends FeatureMatcher<Iterable<E>, Integer> {
    @Override // org.hamcrest.FeatureMatcher
    protected /* bridge */ /* synthetic */ Integer featureValueOf(Object x0) {
        return featureValueOf((Iterable) ((Iterable) x0));
    }

    public IsIterableWithSize(Matcher<? super Integer> sizeMatcher) {
        super(sizeMatcher, "an iterable with size", "iterable size");
    }

    protected Integer featureValueOf(Iterable<E> actual) {
        int size = 0;
        Iterator<E> iterator = actual.iterator();
        while (iterator.hasNext()) {
            size++;
            iterator.next();
        }
        return Integer.valueOf(size);
    }

    @Factory
    public static <E> Matcher<Iterable<E>> iterableWithSize(Matcher<? super Integer> sizeMatcher) {
        return new IsIterableWithSize(sizeMatcher);
    }

    @Factory
    public static <E> Matcher<Iterable<E>> iterableWithSize(int size) {
        return iterableWithSize(IsEqual.equalTo(Integer.valueOf(size)));
    }
}
