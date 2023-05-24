package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/JVMStats.class */
public interface JVMStats extends Stats {
    CountStatistic getUpTime();

    BoundedRangeStatistic getHeapSize();
}
