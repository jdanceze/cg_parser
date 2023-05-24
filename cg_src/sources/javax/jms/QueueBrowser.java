package javax.jms;

import java.util.Enumeration;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/QueueBrowser.class */
public interface QueueBrowser {
    Queue getQueue() throws JMSException;

    String getMessageSelector() throws JMSException;

    Enumeration getEnumeration() throws JMSException;

    void close() throws JMSException;
}
