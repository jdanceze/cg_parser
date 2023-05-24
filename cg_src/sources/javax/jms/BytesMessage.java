package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/BytesMessage.class */
public interface BytesMessage extends Message {
    long getBodyLength() throws JMSException;

    boolean readBoolean() throws JMSException;

    byte readByte() throws JMSException;

    int readUnsignedByte() throws JMSException;

    short readShort() throws JMSException;

    int readUnsignedShort() throws JMSException;

    char readChar() throws JMSException;

    int readInt() throws JMSException;

    long readLong() throws JMSException;

    float readFloat() throws JMSException;

    double readDouble() throws JMSException;

    String readUTF() throws JMSException;

    int readBytes(byte[] bArr) throws JMSException;

    int readBytes(byte[] bArr, int i) throws JMSException;

    void writeBoolean(boolean z) throws JMSException;

    void writeByte(byte b) throws JMSException;

    void writeShort(short s) throws JMSException;

    void writeChar(char c) throws JMSException;

    void writeInt(int i) throws JMSException;

    void writeLong(long j) throws JMSException;

    void writeFloat(float f) throws JMSException;

    void writeDouble(double d) throws JMSException;

    void writeUTF(String str) throws JMSException;

    void writeBytes(byte[] bArr) throws JMSException;

    void writeBytes(byte[] bArr, int i, int i2) throws JMSException;

    void writeObject(Object obj) throws JMSException;

    void reset() throws JMSException;
}
