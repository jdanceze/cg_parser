package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/JCAConnectionPoolStats.class */
public interface JCAConnectionPoolStats extends JCAConnectionStats {
    CountStatistic getCloseCount();

    CountStatistic getCreateCount();

    BoundedRangeStatistic getFreePoolSize();

    BoundedRangeStatistic getPoolSize();

    RangeStatistic getWaitingThreadCount();
}
