package javax.jms;

import java.util.Enumeration;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/ConnectionMetaData.class */
public interface ConnectionMetaData {
    String getJMSVersion() throws JMSException;

    int getJMSMajorVersion() throws JMSException;

    int getJMSMinorVersion() throws JMSException;

    String getJMSProviderName() throws JMSException;

    String getProviderVersion() throws JMSException;

    int getProviderMajorVersion() throws JMSException;

    int getProviderMinorVersion() throws JMSException;

    Enumeration getJMSXPropertyNames() throws JMSException;
}
