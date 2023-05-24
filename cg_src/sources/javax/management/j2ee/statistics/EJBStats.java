package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/EJBStats.class */
public interface EJBStats extends Stats {
    CountStatistic getCreateCount();

    CountStatistic getRemoveCount();
}
