package org.jf.dexlib2.writer;

import android.widget.ExpandableListView;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.annotation.Nonnull;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/DexDataWriter.class */
public class DexDataWriter extends BufferedOutputStream {
    private int filePosition;
    private byte[] tempBuf;
    private byte[] zeroBuf;

    public DexDataWriter(@Nonnull OutputStream output, int filePosition) {
        this(output, filePosition, 262144);
    }

    public DexDataWriter(@Nonnull OutputStream output, int filePosition, int bufferSize) {
        super(output, bufferSize);
        this.tempBuf = new byte[8];
        this.zeroBuf = new byte[3];
        this.filePosition = filePosition;
    }

    @Override // java.io.BufferedOutputStream, java.io.FilterOutputStream, java.io.OutputStream
    public void write(int b) throws IOException {
        this.filePosition++;
        super.write(b);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override // java.io.BufferedOutputStream, java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        this.filePosition += len;
        super.write(b, off, len);
    }

    public void writeLong(long value) throws IOException {
        writeInt((int) value);
        writeInt((int) (value >> 32));
    }

    public static void writeInt(OutputStream out, int value) throws IOException {
        out.write(value);
        out.write(value >> 8);
        out.write(value >> 16);
        out.write(value >> 24);
    }

    public void writeInt(int value) throws IOException {
        writeInt(this, value);
    }

    public void writeShort(int value) throws IOException {
        if (value < -32768 || value > 32767) {
            throw new ExceptionWithContext("Short value out of range: %d", Integer.valueOf(value));
        }
        write(value);
        write(value >> 8);
    }

    public void writeUshort(int value) throws IOException {
        if (value < 0 || value > 65535) {
            throw new ExceptionWithContext("Unsigned short value out of range: %d", Integer.valueOf(value));
        }
        write(value);
        write(value >> 8);
    }

    public void writeUbyte(int value) throws IOException {
        if (value < 0 || value > 255) {
            throw new ExceptionWithContext("Unsigned byte value out of range: %d", Integer.valueOf(value));
        }
        write(value);
    }

    public static void writeUleb128(OutputStream out, int value) throws IOException {
        while ((value & ExpandableListView.PACKED_POSITION_VALUE_NULL) > 127) {
            out.write((value & 127) | 128);
            value >>>= 7;
        }
        out.write(value);
    }

    public void writeUleb128(int value) throws IOException {
        writeUleb128(this, value);
    }

    public static void writeSleb128(OutputStream out, int value) throws IOException {
        if (value >= 0) {
            while (value > 63) {
                out.write((value & 127) | 128);
                value >>>= 7;
            }
            out.write(value & 127);
            return;
        }
        while (value < -64) {
            out.write((value & 127) | 128);
            value >>= 7;
        }
        out.write(value & 127);
    }

    public void writeSleb128(int value) throws IOException {
        writeSleb128(this, value);
    }

    public void writeEncodedValueHeader(int valueType, int valueArg) throws IOException {
        write(valueType | (valueArg << 5));
    }

    public void writeEncodedInt(int valueType, int value) throws IOException {
        int index = 0;
        if (value >= 0) {
            while (value > 127) {
                int i = index;
                index++;
                this.tempBuf[i] = (byte) value;
                value >>= 8;
            }
        } else {
            while (value < -128) {
                int i2 = index;
                index++;
                this.tempBuf[i2] = (byte) value;
                value >>= 8;
            }
        }
        int i3 = index;
        int index2 = index + 1;
        this.tempBuf[i3] = (byte) value;
        writeEncodedValueHeader(valueType, index2 - 1);
        write(this.tempBuf, 0, index2);
    }

    public void writeEncodedLong(int valueType, long value) throws IOException {
        int index = 0;
        if (value >= 0) {
            while (value > 127) {
                int i = index;
                index++;
                this.tempBuf[i] = (byte) value;
                value >>= 8;
            }
        } else {
            while (value < -128) {
                int i2 = index;
                index++;
                this.tempBuf[i2] = (byte) value;
                value >>= 8;
            }
        }
        int i3 = index;
        int index2 = index + 1;
        this.tempBuf[i3] = (byte) value;
        writeEncodedValueHeader(valueType, index2 - 1);
        write(this.tempBuf, 0, index2);
    }

    public void writeEncodedUint(int valueType, int value) throws IOException {
        int index = 0;
        do {
            int i = index;
            index++;
            this.tempBuf[i] = (byte) value;
            value >>>= 8;
        } while (value != 0);
        writeEncodedValueHeader(valueType, index - 1);
        write(this.tempBuf, 0, index);
    }

    public void writeEncodedFloat(int valueType, float value) throws IOException {
        writeRightZeroExtendedInt(valueType, Float.floatToRawIntBits(value));
    }

    protected void writeRightZeroExtendedInt(int valueType, int value) throws IOException {
        int index = 3;
        do {
            int i = index;
            index--;
            this.tempBuf[i] = (byte) ((value & (-16777216)) >>> 24);
            value <<= 8;
        } while (value != 0);
        int firstElement = index + 1;
        int encodedLength = 4 - firstElement;
        writeEncodedValueHeader(valueType, encodedLength - 1);
        write(this.tempBuf, firstElement, encodedLength);
    }

    public void writeEncodedDouble(int valueType, double value) throws IOException {
        writeRightZeroExtendedLong(valueType, Double.doubleToRawLongBits(value));
    }

    protected void writeRightZeroExtendedLong(int valueType, long value) throws IOException {
        int index = 7;
        do {
            int i = index;
            index--;
            this.tempBuf[i] = (byte) ((value & (-72057594037927936L)) >>> 56);
            value <<= 8;
        } while (value != 0);
        int firstElement = index + 1;
        int encodedLength = 8 - firstElement;
        writeEncodedValueHeader(valueType, encodedLength - 1);
        write(this.tempBuf, firstElement, encodedLength);
    }

    public void writeString(String string) throws IOException {
        int len = string.length();
        if (this.tempBuf.length <= string.length() * 3) {
            this.tempBuf = new byte[string.length() * 3];
        }
        byte[] buf = this.tempBuf;
        int bufPos = 0;
        for (int i = 0; i < len; i++) {
            char c = string.charAt(i);
            if (c != 0 && c < 128) {
                int i2 = bufPos;
                bufPos++;
                buf[i2] = (byte) c;
            } else if (c < 2048) {
                int i3 = bufPos;
                int bufPos2 = bufPos + 1;
                buf[i3] = (byte) (((c >> 6) & 31) | 192);
                bufPos = bufPos2 + 1;
                buf[bufPos2] = (byte) ((c & '?') | 128);
            } else {
                int i4 = bufPos;
                int bufPos3 = bufPos + 1;
                buf[i4] = (byte) (((c >> '\f') & 15) | 224);
                int bufPos4 = bufPos3 + 1;
                buf[bufPos3] = (byte) (((c >> 6) & 63) | 128);
                bufPos = bufPos4 + 1;
                buf[bufPos4] = (byte) ((c & '?') | 128);
            }
        }
        write(buf, 0, bufPos);
    }

    public void align() throws IOException {
        int zeros = (-getPosition()) & 3;
        if (zeros > 0) {
            write(this.zeroBuf, 0, zeros);
        }
    }

    public int getPosition() {
        return this.filePosition;
    }
}
