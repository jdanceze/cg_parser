package org.hamcrest;

import org.hamcrest.core.IsEqual;
import org.hamcrest.integration.JMock1Adapter;
import org.jmock.core.Constraint;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/JMock1Matchers.class */
public class JMock1Matchers {
    public static Constraint equalTo(String string) {
        return JMock1Adapter.adapt(IsEqual.equalTo(string));
    }
}
