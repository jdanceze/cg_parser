package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/RangeStatistic.class */
public interface RangeStatistic extends Statistic {
    long getHighWaterMark();

    long getLowWaterMark();

    long getCurrent();
}
