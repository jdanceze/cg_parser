package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/EntityBeanStats.class */
public interface EntityBeanStats extends EJBStats {
    RangeStatistic getReadyCount();

    RangeStatistic getPooledCount();
}
