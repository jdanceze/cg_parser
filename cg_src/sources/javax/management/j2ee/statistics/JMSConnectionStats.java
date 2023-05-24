package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/JMSConnectionStats.class */
public interface JMSConnectionStats extends Stats {
    JMSSessionStats[] getSessions();

    boolean isTransactional();
}
