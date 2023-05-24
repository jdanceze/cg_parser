package org.junit.internal;

import org.junit.Assert;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/InexactComparisonCriteria.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/InexactComparisonCriteria.class */
public class InexactComparisonCriteria extends ComparisonCriteria {
    public Object fDelta;

    public InexactComparisonCriteria(double delta) {
        this.fDelta = Double.valueOf(delta);
    }

    public InexactComparisonCriteria(float delta) {
        this.fDelta = Float.valueOf(delta);
    }

    @Override // org.junit.internal.ComparisonCriteria
    protected void assertElementsEqual(Object expected, Object actual) {
        if (expected instanceof Double) {
            Assert.assertEquals(((Double) expected).doubleValue(), ((Double) actual).doubleValue(), ((Double) this.fDelta).doubleValue());
        } else {
            Assert.assertEquals(((Float) expected).floatValue(), ((Float) actual).floatValue(), ((Float) this.fDelta).floatValue());
        }
    }
}
