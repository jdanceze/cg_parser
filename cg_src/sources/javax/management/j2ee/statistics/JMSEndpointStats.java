package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/JMSEndpointStats.class */
public interface JMSEndpointStats extends Stats {
    CountStatistic getMessageCount();

    CountStatistic getPendingMessageCount();

    CountStatistic getExpiredMessageCount();

    TimeStatistic getMessageWaitTime();
}
