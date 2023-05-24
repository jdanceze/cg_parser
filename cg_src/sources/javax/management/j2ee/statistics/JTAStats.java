package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/JTAStats.class */
public interface JTAStats extends Stats {
    CountStatistic getActiveCount();

    CountStatistic getCommittedCount();

    CountStatistic getRolledbackCount();
}
