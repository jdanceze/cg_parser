package org.hamcrest;

import org.hamcrest.core.IsEqual;
import org.hamcrest.integration.EasyMock2Adapter;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/EasyMock2Matchers.class */
public class EasyMock2Matchers {
    public static String equalTo(String string) {
        EasyMock2Adapter.adapt(IsEqual.equalTo(string));
        return null;
    }
}
