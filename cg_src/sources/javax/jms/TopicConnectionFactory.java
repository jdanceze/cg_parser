package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/TopicConnectionFactory.class */
public interface TopicConnectionFactory extends ConnectionFactory {
    TopicConnection createTopicConnection() throws JMSException;

    TopicConnection createTopicConnection(String str, String str2) throws JMSException;
}
