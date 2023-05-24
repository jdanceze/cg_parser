package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/MessageConsumer.class */
public interface MessageConsumer {
    String getMessageSelector() throws JMSException;

    MessageListener getMessageListener() throws JMSException;

    void setMessageListener(MessageListener messageListener) throws JMSException;

    Message receive() throws JMSException;

    Message receive(long j) throws JMSException;

    Message receiveNoWait() throws JMSException;

    void close() throws JMSException;
}
