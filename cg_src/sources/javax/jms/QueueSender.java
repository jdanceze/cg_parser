package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/QueueSender.class */
public interface QueueSender extends MessageProducer {
    Queue getQueue() throws JMSException;

    @Override // javax.jms.MessageProducer
    void send(Message message) throws JMSException;

    @Override // javax.jms.MessageProducer
    void send(Message message, int i, int i2, long j) throws JMSException;

    void send(Queue queue, Message message) throws JMSException;

    void send(Queue queue, Message message, int i, int i2, long j) throws JMSException;
}
