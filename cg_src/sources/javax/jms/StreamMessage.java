package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/StreamMessage.class */
public interface StreamMessage extends Message {
    boolean readBoolean() throws JMSException;

    byte readByte() throws JMSException;

    short readShort() throws JMSException;

    char readChar() throws JMSException;

    int readInt() throws JMSException;

    long readLong() throws JMSException;

    float readFloat() throws JMSException;

    double readDouble() throws JMSException;

    String readString() throws JMSException;

    int readBytes(byte[] bArr) throws JMSException;

    Object readObject() throws JMSException;

    void writeBoolean(boolean z) throws JMSException;

    void writeByte(byte b) throws JMSException;

    void writeShort(short s) throws JMSException;

    void writeChar(char c) throws JMSException;

    void writeInt(int i) throws JMSException;

    void writeLong(long j) throws JMSException;

    void writeFloat(float f) throws JMSException;

    void writeDouble(double d) throws JMSException;

    void writeString(String str) throws JMSException;

    void writeBytes(byte[] bArr) throws JMSException;

    void writeBytes(byte[] bArr, int i, int i2) throws JMSException;

    void writeObject(Object obj) throws JMSException;

    void reset() throws JMSException;
}
