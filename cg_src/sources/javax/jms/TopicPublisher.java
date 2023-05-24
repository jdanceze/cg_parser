package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/TopicPublisher.class */
public interface TopicPublisher extends MessageProducer {
    Topic getTopic() throws JMSException;

    void publish(Message message) throws JMSException;

    void publish(Message message, int i, int i2, long j) throws JMSException;

    void publish(Topic topic, Message message) throws JMSException;

    void publish(Topic topic, Message message, int i, int i2, long j) throws JMSException;
}
