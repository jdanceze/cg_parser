package org.junit.runners.model;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/model/RunnerScheduler.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/model/RunnerScheduler.class */
public interface RunnerScheduler {
    void schedule(Runnable runnable);

    void finished();
}
