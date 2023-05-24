package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/TextMessage.class */
public interface TextMessage extends Message {
    void setText(String str) throws JMSException;

    String getText() throws JMSException;
}
