package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/QueueSession.class */
public interface QueueSession extends Session {
    @Override // javax.jms.Session
    Queue createQueue(String str) throws JMSException;

    QueueReceiver createReceiver(Queue queue) throws JMSException;

    QueueReceiver createReceiver(Queue queue, String str) throws JMSException;

    QueueSender createSender(Queue queue) throws JMSException;

    @Override // javax.jms.Session
    QueueBrowser createBrowser(Queue queue) throws JMSException;

    @Override // javax.jms.Session
    QueueBrowser createBrowser(Queue queue, String str) throws JMSException;

    @Override // javax.jms.Session
    TemporaryQueue createTemporaryQueue() throws JMSException;
}
