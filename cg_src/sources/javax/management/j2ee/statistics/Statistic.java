package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/Statistic.class */
public interface Statistic {
    String getName();

    String getUnit();

    String getDescription();

    long getStartTime();

    long getLastSampleTime();
}
