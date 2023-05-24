package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/TopicSession.class */
public interface TopicSession extends Session {
    @Override // javax.jms.Session
    Topic createTopic(String str) throws JMSException;

    TopicSubscriber createSubscriber(Topic topic) throws JMSException;

    TopicSubscriber createSubscriber(Topic topic, String str, boolean z) throws JMSException;

    @Override // javax.jms.Session
    TopicSubscriber createDurableSubscriber(Topic topic, String str) throws JMSException;

    @Override // javax.jms.Session
    TopicSubscriber createDurableSubscriber(Topic topic, String str, String str2, boolean z) throws JMSException;

    TopicPublisher createPublisher(Topic topic) throws JMSException;

    @Override // javax.jms.Session
    TemporaryTopic createTemporaryTopic() throws JMSException;

    @Override // javax.jms.Session
    void unsubscribe(String str) throws JMSException;
}
