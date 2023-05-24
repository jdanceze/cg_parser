package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/JDBCConnectionPoolStats.class */
public interface JDBCConnectionPoolStats extends JDBCConnectionStats {
    CountStatistic getCreateCount();

    CountStatistic getCloseCount();

    BoundedRangeStatistic getPoolSize();

    BoundedRangeStatistic getFreePoolSize();

    RangeStatistic getWaitingThreadCount();
}
