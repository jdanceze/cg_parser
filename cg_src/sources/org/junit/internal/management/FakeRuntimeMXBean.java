package org.junit.internal.management;

import java.util.Collections;
import java.util.List;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/management/FakeRuntimeMXBean.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/management/FakeRuntimeMXBean.class */
class FakeRuntimeMXBean implements RuntimeMXBean {
    @Override // org.junit.internal.management.RuntimeMXBean
    public List<String> getInputArguments() {
        return Collections.emptyList();
    }
}
