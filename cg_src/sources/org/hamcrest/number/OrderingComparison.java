package org.hamcrest.number;

import java.lang.Comparable;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/number/OrderingComparison.class */
public class OrderingComparison<T extends Comparable<T>> extends TypeSafeMatcher<T> {
    private static final int LESS_THAN = -1;
    private static final int GREATER_THAN = 1;
    private static final int EQUAL = 0;
    private final T expected;
    private final int minCompare;
    private final int maxCompare;
    private static final String[] comparisonDescriptions = {"less than", "equal to", "greater than"};

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.hamcrest.TypeSafeMatcher
    public /* bridge */ /* synthetic */ void describeMismatchSafely(Object x0, Description x1) {
        describeMismatchSafely((OrderingComparison<T>) ((Comparable) x0), x1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.hamcrest.TypeSafeMatcher
    public /* bridge */ /* synthetic */ boolean matchesSafely(Object x0) {
        return matchesSafely((OrderingComparison<T>) ((Comparable) x0));
    }

    private OrderingComparison(T expected, int minCompare, int maxCompare) {
        this.expected = expected;
        this.minCompare = minCompare;
        this.maxCompare = maxCompare;
    }

    public boolean matchesSafely(T actual) {
        int compare = Integer.signum(actual.compareTo(this.expected));
        return this.minCompare <= compare && compare <= this.maxCompare;
    }

    public void describeMismatchSafely(T actual, Description mismatchDescription) {
        mismatchDescription.appendValue(actual).appendText(" was ").appendText(asText(actual.compareTo(this.expected))).appendText(Instruction.argsep).appendValue(this.expected);
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("a value ").appendText(asText(this.minCompare));
        if (this.minCompare != this.maxCompare) {
            description.appendText(" or ").appendText(asText(this.maxCompare));
        }
        description.appendText(Instruction.argsep).appendValue(this.expected);
    }

    private static String asText(int comparison) {
        return comparisonDescriptions[Integer.signum(comparison) + 1];
    }

    @Factory
    public static <T extends Comparable<T>> Matcher<T> comparesEqualTo(T value) {
        return new OrderingComparison(value, 0, 0);
    }

    @Factory
    public static <T extends Comparable<T>> Matcher<T> greaterThan(T value) {
        return new OrderingComparison(value, 1, 1);
    }

    @Factory
    public static <T extends Comparable<T>> Matcher<T> greaterThanOrEqualTo(T value) {
        return new OrderingComparison(value, 0, 1);
    }

    @Factory
    public static <T extends Comparable<T>> Matcher<T> lessThan(T value) {
        return new OrderingComparison(value, -1, -1);
    }

    @Factory
    public static <T extends Comparable<T>> Matcher<T> lessThanOrEqualTo(T value) {
        return new OrderingComparison(value, -1, 0);
    }
}
