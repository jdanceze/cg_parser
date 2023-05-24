package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/XAQueueConnection.class */
public interface XAQueueConnection extends XAConnection, QueueConnection {
    XAQueueSession createXAQueueSession() throws JMSException;

    @Override // javax.jms.QueueConnection
    QueueSession createQueueSession(boolean z, int i) throws JMSException;
}
