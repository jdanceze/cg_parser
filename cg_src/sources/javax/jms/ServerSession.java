package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/ServerSession.class */
public interface ServerSession {
    Session getSession() throws JMSException;

    void start() throws JMSException;
}
