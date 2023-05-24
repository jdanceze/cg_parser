package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/JCAConnectionStats.class */
public interface JCAConnectionStats extends Stats {
    String getConnectionFactory();

    String getManagedConnectionFactory();

    TimeStatistic getWaitTime();

    TimeStatistic getUseTime();
}
