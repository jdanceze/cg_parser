package org.hamcrest.internal;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/internal/SelfDescribingValue.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/internal/SelfDescribingValue.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/internal/SelfDescribingValue.class */
public class SelfDescribingValue<T> implements SelfDescribing {
    private T value;

    public SelfDescribingValue(T value) {
        this.value = value;
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendValue(this.value);
    }
}
