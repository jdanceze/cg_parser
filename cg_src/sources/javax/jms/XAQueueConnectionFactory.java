package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/XAQueueConnectionFactory.class */
public interface XAQueueConnectionFactory extends XAConnectionFactory, QueueConnectionFactory {
    XAQueueConnection createXAQueueConnection() throws JMSException;

    XAQueueConnection createXAQueueConnection(String str, String str2) throws JMSException;
}
