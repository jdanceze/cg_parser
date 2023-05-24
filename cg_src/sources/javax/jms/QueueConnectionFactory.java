package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/QueueConnectionFactory.class */
public interface QueueConnectionFactory extends ConnectionFactory {
    QueueConnection createQueueConnection() throws JMSException;

    QueueConnection createQueueConnection(String str, String str2) throws JMSException;
}
