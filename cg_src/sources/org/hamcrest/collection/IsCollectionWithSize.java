package org.hamcrest.collection;

import java.util.Collection;
import org.hamcrest.Factory;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/collection/IsCollectionWithSize.class */
public class IsCollectionWithSize<E> extends FeatureMatcher<Collection<? extends E>, Integer> {
    @Override // org.hamcrest.FeatureMatcher
    protected /* bridge */ /* synthetic */ Integer featureValueOf(Object x0) {
        return featureValueOf((Collection) ((Collection) x0));
    }

    public IsCollectionWithSize(Matcher<? super Integer> sizeMatcher) {
        super(sizeMatcher, "a collection with size", "collection size");
    }

    protected Integer featureValueOf(Collection<? extends E> actual) {
        return Integer.valueOf(actual.size());
    }

    @Factory
    public static <E> Matcher<Collection<? extends E>> hasSize(Matcher<? super Integer> sizeMatcher) {
        return new IsCollectionWithSize(sizeMatcher);
    }

    @Factory
    public static <E> Matcher<Collection<? extends E>> hasSize(int size) {
        Matcher<? super Integer> matcher = IsEqual.equalTo(Integer.valueOf(size));
        return hasSize(matcher);
    }
}
