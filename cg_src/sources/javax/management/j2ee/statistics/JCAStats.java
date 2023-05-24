package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/JCAStats.class */
public interface JCAStats extends Stats {
    JCAConnectionStats[] getConnections();

    JCAConnectionPoolStats[] getConnectionPools();
}
