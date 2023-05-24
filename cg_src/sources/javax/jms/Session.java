package javax.jms;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/Session.class */
public interface Session extends Runnable {
    public static final int AUTO_ACKNOWLEDGE = 1;
    public static final int CLIENT_ACKNOWLEDGE = 2;
    public static final int DUPS_OK_ACKNOWLEDGE = 3;
    public static final int SESSION_TRANSACTED = 0;

    BytesMessage createBytesMessage() throws JMSException;

    MapMessage createMapMessage() throws JMSException;

    Message createMessage() throws JMSException;

    ObjectMessage createObjectMessage() throws JMSException;

    ObjectMessage createObjectMessage(Serializable serializable) throws JMSException;

    StreamMessage createStreamMessage() throws JMSException;

    TextMessage createTextMessage() throws JMSException;

    TextMessage createTextMessage(String str) throws JMSException;

    boolean getTransacted() throws JMSException;

    int getAcknowledgeMode() throws JMSException;

    void commit() throws JMSException;

    void rollback() throws JMSException;

    void close() throws JMSException;

    void recover() throws JMSException;

    MessageListener getMessageListener() throws JMSException;

    void setMessageListener(MessageListener messageListener) throws JMSException;

    @Override // java.lang.Runnable
    void run();

    MessageProducer createProducer(Destination destination) throws JMSException;

    MessageConsumer createConsumer(Destination destination) throws JMSException;

    MessageConsumer createConsumer(Destination destination, String str) throws JMSException;

    MessageConsumer createConsumer(Destination destination, String str, boolean z) throws JMSException;

    Queue createQueue(String str) throws JMSException;

    Topic createTopic(String str) throws JMSException;

    TopicSubscriber createDurableSubscriber(Topic topic, String str) throws JMSException;

    TopicSubscriber createDurableSubscriber(Topic topic, String str, String str2, boolean z) throws JMSException;

    QueueBrowser createBrowser(Queue queue) throws JMSException;

    QueueBrowser createBrowser(Queue queue, String str) throws JMSException;

    TemporaryQueue createTemporaryQueue() throws JMSException;

    TemporaryTopic createTemporaryTopic() throws JMSException;

    void unsubscribe(String str) throws JMSException;
}
