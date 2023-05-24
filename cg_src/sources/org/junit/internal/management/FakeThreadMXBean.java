package org.junit.internal.management;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/management/FakeThreadMXBean.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/management/FakeThreadMXBean.class */
final class FakeThreadMXBean implements ThreadMXBean {
    @Override // org.junit.internal.management.ThreadMXBean
    public long getThreadCpuTime(long id) {
        throw new UnsupportedOperationException();
    }

    @Override // org.junit.internal.management.ThreadMXBean
    public boolean isThreadCpuTimeSupported() {
        return false;
    }
}
