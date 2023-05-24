package javax.jms;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/ObjectMessage.class */
public interface ObjectMessage extends Message {
    void setObject(Serializable serializable) throws JMSException;

    Serializable getObject() throws JMSException;
}
