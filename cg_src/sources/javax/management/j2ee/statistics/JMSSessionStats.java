package javax.management.j2ee.statistics;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/statistics/JMSSessionStats.class */
public interface JMSSessionStats extends Stats {
    JMSProducerStats[] getProducers();

    JMSConsumerStats[] getConsumers();

    CountStatistic getMessageCount();

    CountStatistic getPendingMessageCount();

    CountStatistic getExpiredMessageCount();

    TimeStatistic getMessageWaitTime();

    CountStatistic getDurableSubscriptionCount();
}
