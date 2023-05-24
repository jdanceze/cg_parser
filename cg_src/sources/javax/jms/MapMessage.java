package javax.jms;

import java.util.Enumeration;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/MapMessage.class */
public interface MapMessage extends Message {
    boolean getBoolean(String str) throws JMSException;

    byte getByte(String str) throws JMSException;

    short getShort(String str) throws JMSException;

    char getChar(String str) throws JMSException;

    int getInt(String str) throws JMSException;

    long getLong(String str) throws JMSException;

    float getFloat(String str) throws JMSException;

    double getDouble(String str) throws JMSException;

    String getString(String str) throws JMSException;

    byte[] getBytes(String str) throws JMSException;

    Object getObject(String str) throws JMSException;

    Enumeration getMapNames() throws JMSException;

    void setBoolean(String str, boolean z) throws JMSException;

    void setByte(String str, byte b) throws JMSException;

    void setShort(String str, short s) throws JMSException;

    void setChar(String str, char c) throws JMSException;

    void setInt(String str, int i) throws JMSException;

    void setLong(String str, long j) throws JMSException;

    void setFloat(String str, float f) throws JMSException;

    void setDouble(String str, double d) throws JMSException;

    void setString(String str, String str2) throws JMSException;

    void setBytes(String str, byte[] bArr) throws JMSException;

    void setBytes(String str, byte[] bArr, int i, int i2) throws JMSException;

    void setObject(String str, Object obj) throws JMSException;

    boolean itemExists(String str) throws JMSException;
}
