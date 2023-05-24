package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/XATopicConnection.class */
public interface XATopicConnection extends XAConnection, TopicConnection {
    XATopicSession createXATopicSession() throws JMSException;

    @Override // javax.jms.TopicConnection
    TopicSession createTopicSession(boolean z, int i) throws JMSException;
}
