package org.junit.internal.management;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/management/ThreadMXBean.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/management/ThreadMXBean.class */
public interface ThreadMXBean {
    long getThreadCpuTime(long j);

    boolean isThreadCpuTimeSupported();
}
