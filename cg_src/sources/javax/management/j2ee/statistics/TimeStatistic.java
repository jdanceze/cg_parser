package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/TimeStatistic.class */
public interface TimeStatistic extends Statistic {
    long getCount();

    long getMaxTime();

    long getMinTime();

    long getTotalTime();
}
