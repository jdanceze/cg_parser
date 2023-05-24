package org.hamcrest.number;

import java.math.BigDecimal;
import java.math.MathContext;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/number/BigDecimalCloseTo.class */
public class BigDecimalCloseTo extends TypeSafeMatcher<BigDecimal> {
    private final BigDecimal delta;
    private final BigDecimal value;

    public BigDecimalCloseTo(BigDecimal value, BigDecimal error) {
        this.delta = error;
        this.value = value;
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public boolean matchesSafely(BigDecimal item) {
        return actualDelta(item).compareTo(BigDecimal.ZERO) <= 0;
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public void describeMismatchSafely(BigDecimal item, Description mismatchDescription) {
        mismatchDescription.appendValue(item).appendText(" differed by ").appendValue(actualDelta(item));
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("a numeric value within ").appendValue(this.delta).appendText(" of ").appendValue(this.value);
    }

    private BigDecimal actualDelta(BigDecimal item) {
        return item.subtract(this.value, MathContext.DECIMAL128).abs().subtract(this.delta, MathContext.DECIMAL128).stripTrailingZeros();
    }

    @Factory
    public static Matcher<BigDecimal> closeTo(BigDecimal operand, BigDecimal error) {
        return new BigDecimalCloseTo(operand, error);
    }
}
