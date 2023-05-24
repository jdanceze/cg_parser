package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/TopicSubscriber.class */
public interface TopicSubscriber extends MessageConsumer {
    Topic getTopic() throws JMSException;

    boolean getNoLocal() throws JMSException;
}
