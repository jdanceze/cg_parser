package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/XATopicConnectionFactory.class */
public interface XATopicConnectionFactory extends XAConnectionFactory, TopicConnectionFactory {
    XATopicConnection createXATopicConnection() throws JMSException;

    XATopicConnection createXATopicConnection(String str, String str2) throws JMSException;
}
