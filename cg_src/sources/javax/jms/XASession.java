package javax.jms;

import javax.transaction.xa.XAResource;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/XASession.class */
public interface XASession extends Session {
    Session getSession() throws JMSException;

    XAResource getXAResource();

    @Override // javax.jms.Session
    boolean getTransacted() throws JMSException;

    @Override // javax.jms.Session
    void commit() throws JMSException;

    @Override // javax.jms.Session
    void rollback() throws JMSException;
}
