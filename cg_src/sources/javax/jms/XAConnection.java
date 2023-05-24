package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/XAConnection.class */
public interface XAConnection extends Connection {
    XASession createXASession() throws JMSException;

    @Override // javax.jms.Connection
    Session createSession(boolean z, int i) throws JMSException;
}
