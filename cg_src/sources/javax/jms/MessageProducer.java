package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/MessageProducer.class */
public interface MessageProducer {
    void setDisableMessageID(boolean z) throws JMSException;

    boolean getDisableMessageID() throws JMSException;

    void setDisableMessageTimestamp(boolean z) throws JMSException;

    boolean getDisableMessageTimestamp() throws JMSException;

    void setDeliveryMode(int i) throws JMSException;

    int getDeliveryMode() throws JMSException;

    void setPriority(int i) throws JMSException;

    int getPriority() throws JMSException;

    void setTimeToLive(long j) throws JMSException;

    long getTimeToLive() throws JMSException;

    Destination getDestination() throws JMSException;

    void close() throws JMSException;

    void send(Message message) throws JMSException;

    void send(Message message, int i, int i2, long j) throws JMSException;

    void send(Destination destination, Message message) throws JMSException;

    void send(Destination destination, Message message, int i, int i2, long j) throws JMSException;
}
