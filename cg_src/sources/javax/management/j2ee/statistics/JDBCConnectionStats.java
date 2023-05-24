package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/JDBCConnectionStats.class */
public interface JDBCConnectionStats extends Stats {
    String getJdbcDataSource();

    TimeStatistic getWaitTime();

    TimeStatistic getUseTime();
}
