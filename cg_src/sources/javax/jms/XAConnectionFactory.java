package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/XAConnectionFactory.class */
public interface XAConnectionFactory {
    XAConnection createXAConnection() throws JMSException;

    XAConnection createXAConnection(String str, String str2) throws JMSException;
}
