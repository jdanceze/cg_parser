package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/TopicRequestor.class */
public class TopicRequestor {
    TopicSession session;
    Topic topic;
    TemporaryTopic tempTopic;
    TopicPublisher publisher;
    TopicSubscriber subscriber;

    public TopicRequestor(TopicSession session, Topic topic) throws JMSException {
        this.session = session;
        this.topic = topic;
        this.tempTopic = session.createTemporaryTopic();
        this.publisher = session.createPublisher(topic);
        this.subscriber = session.createSubscriber(this.tempTopic);
    }

    public Message request(Message message) throws JMSException {
        message.setJMSReplyTo(this.tempTopic);
        this.publisher.publish(message);
        return this.subscriber.receive();
    }

    public void close() throws JMSException {
        this.session.close();
        this.tempTopic.delete();
    }
}
