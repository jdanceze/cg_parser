package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/JDBCStats.class */
public interface JDBCStats extends Stats {
    JDBCConnectionStats[] getConnections();

    JDBCConnectionPoolStats[] getConnectionPools();
}
