package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/Stats.class */
public interface Stats {
    Statistic getStatistic(String str);

    String[] getStatisticNames();

    Statistic[] getStatistics();
}
