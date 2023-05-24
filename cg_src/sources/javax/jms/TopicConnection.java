package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/TopicConnection.class */
public interface TopicConnection extends Connection {
    TopicSession createTopicSession(boolean z, int i) throws JMSException;

    ConnectionConsumer createConnectionConsumer(Topic topic, String str, ServerSessionPool serverSessionPool, int i) throws JMSException;

    @Override // javax.jms.Connection
    ConnectionConsumer createDurableConnectionConsumer(Topic topic, String str, String str2, ServerSessionPool serverSessionPool, int i) throws JMSException;
}
