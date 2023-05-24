package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/ConnectionConsumer.class */
public interface ConnectionConsumer {
    ServerSessionPool getServerSessionPool() throws JMSException;

    void close() throws JMSException;
}
