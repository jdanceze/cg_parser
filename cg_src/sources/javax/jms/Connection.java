package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/Connection.class */
public interface Connection {
    Session createSession(boolean z, int i) throws JMSException;

    String getClientID() throws JMSException;

    void setClientID(String str) throws JMSException;

    ConnectionMetaData getMetaData() throws JMSException;

    ExceptionListener getExceptionListener() throws JMSException;

    void setExceptionListener(ExceptionListener exceptionListener) throws JMSException;

    void start() throws JMSException;

    void stop() throws JMSException;

    void close() throws JMSException;

    ConnectionConsumer createConnectionConsumer(Destination destination, String str, ServerSessionPool serverSessionPool, int i) throws JMSException;

    ConnectionConsumer createDurableConnectionConsumer(Topic topic, String str, String str2, ServerSessionPool serverSessionPool, int i) throws JMSException;
}
