package org.hamcrest.number;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/number/IsCloseTo.class */
public class IsCloseTo extends TypeSafeMatcher<Double> {
    private final double delta;
    private final double value;

    public IsCloseTo(double value, double error) {
        this.delta = error;
        this.value = value;
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public boolean matchesSafely(Double item) {
        return actualDelta(item) <= Const.default_value_double;
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public void describeMismatchSafely(Double item, Description mismatchDescription) {
        mismatchDescription.appendValue(item).appendText(" differed by ").appendValue(Double.valueOf(actualDelta(item)));
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("a numeric value within ").appendValue(Double.valueOf(this.delta)).appendText(" of ").appendValue(Double.valueOf(this.value));
    }

    private double actualDelta(Double item) {
        return Math.abs(item.doubleValue() - this.value) - this.delta;
    }

    @Factory
    public static Matcher<Double> closeTo(double operand, double error) {
        return new IsCloseTo(operand, error);
    }
}
