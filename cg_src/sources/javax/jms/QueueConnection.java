package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/QueueConnection.class */
public interface QueueConnection extends Connection {
    QueueSession createQueueSession(boolean z, int i) throws JMSException;

    ConnectionConsumer createConnectionConsumer(Queue queue, String str, ServerSessionPool serverSessionPool, int i) throws JMSException;
}
