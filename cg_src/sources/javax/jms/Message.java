package javax.jms;

import java.util.Enumeration;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/Message.class */
public interface Message {
    public static final int DEFAULT_DELIVERY_MODE = 2;
    public static final int DEFAULT_PRIORITY = 4;
    public static final long DEFAULT_TIME_TO_LIVE = 0;

    String getJMSMessageID() throws JMSException;

    void setJMSMessageID(String str) throws JMSException;

    long getJMSTimestamp() throws JMSException;

    void setJMSTimestamp(long j) throws JMSException;

    byte[] getJMSCorrelationIDAsBytes() throws JMSException;

    void setJMSCorrelationIDAsBytes(byte[] bArr) throws JMSException;

    void setJMSCorrelationID(String str) throws JMSException;

    String getJMSCorrelationID() throws JMSException;

    Destination getJMSReplyTo() throws JMSException;

    void setJMSReplyTo(Destination destination) throws JMSException;

    Destination getJMSDestination() throws JMSException;

    void setJMSDestination(Destination destination) throws JMSException;

    int getJMSDeliveryMode() throws JMSException;

    void setJMSDeliveryMode(int i) throws JMSException;

    boolean getJMSRedelivered() throws JMSException;

    void setJMSRedelivered(boolean z) throws JMSException;

    String getJMSType() throws JMSException;

    void setJMSType(String str) throws JMSException;

    long getJMSExpiration() throws JMSException;

    void setJMSExpiration(long j) throws JMSException;

    int getJMSPriority() throws JMSException;

    void setJMSPriority(int i) throws JMSException;

    void clearProperties() throws JMSException;

    boolean propertyExists(String str) throws JMSException;

    boolean getBooleanProperty(String str) throws JMSException;

    byte getByteProperty(String str) throws JMSException;

    short getShortProperty(String str) throws JMSException;

    int getIntProperty(String str) throws JMSException;

    long getLongProperty(String str) throws JMSException;

    float getFloatProperty(String str) throws JMSException;

    double getDoubleProperty(String str) throws JMSException;

    String getStringProperty(String str) throws JMSException;

    Object getObjectProperty(String str) throws JMSException;

    Enumeration getPropertyNames() throws JMSException;

    void setBooleanProperty(String str, boolean z) throws JMSException;

    void setByteProperty(String str, byte b) throws JMSException;

    void setShortProperty(String str, short s) throws JMSException;

    void setIntProperty(String str, int i) throws JMSException;

    void setLongProperty(String str, long j) throws JMSException;

    void setFloatProperty(String str, float f) throws JMSException;

    void setDoubleProperty(String str, double d) throws JMSException;

    void setStringProperty(String str, String str2) throws JMSException;

    void setObjectProperty(String str, Object obj) throws JMSException;

    void acknowledge() throws JMSException;

    void clearBody() throws JMSException;
}
