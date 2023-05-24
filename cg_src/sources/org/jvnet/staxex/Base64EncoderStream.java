package org.jvnet.staxex;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
/* loaded from: gencallgraphv3.jar:stax-ex-1.8.jar:org/jvnet/staxex/Base64EncoderStream.class */
public class Base64EncoderStream extends FilterOutputStream {
    private byte[] buffer;
    private int bufsize;
    private XMLStreamWriter outWriter;
    private static final char[] pem_array = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    public Base64EncoderStream(OutputStream out) {
        super(out);
        this.bufsize = 0;
        this.buffer = new byte[3];
    }

    public Base64EncoderStream(XMLStreamWriter outWriter, OutputStream out) {
        super(out);
        this.bufsize = 0;
        this.buffer = new byte[3];
        this.outWriter = outWriter;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            write(b[off + i]);
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int c) throws IOException {
        byte[] bArr = this.buffer;
        int i = this.bufsize;
        this.bufsize = i + 1;
        bArr[i] = (byte) c;
        if (this.bufsize == 3) {
            encode();
            this.bufsize = 0;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        if (this.bufsize > 0) {
            encode();
            this.bufsize = 0;
        }
        this.out.flush();
        try {
            this.outWriter.flush();
        } catch (XMLStreamException e) {
            Logger.getLogger(Base64EncoderStream.class.getName()).log(Level.SEVERE, (String) null, e);
            throw new IOException(e);
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        flush();
        this.out.close();
    }

    private void encode() throws IOException {
        char[] buf = new char[4];
        if (this.bufsize == 1) {
            byte a = this.buffer[0];
            buf[0] = pem_array[(a >>> 2) & 63];
            buf[1] = pem_array[((a << 4) & 48) + ((0 >>> 4) & 15)];
            buf[2] = '=';
            buf[3] = '=';
        } else if (this.bufsize == 2) {
            byte a2 = this.buffer[0];
            byte b = this.buffer[1];
            buf[0] = pem_array[(a2 >>> 2) & 63];
            buf[1] = pem_array[((a2 << 4) & 48) + ((b >>> 4) & 15)];
            buf[2] = pem_array[((b << 2) & 60) + ((0 >>> 6) & 3)];
            buf[3] = '=';
        } else {
            byte a3 = this.buffer[0];
            byte b2 = this.buffer[1];
            byte c = this.buffer[2];
            buf[0] = pem_array[(a3 >>> 2) & 63];
            buf[1] = pem_array[((a3 << 4) & 48) + ((b2 >>> 4) & 15)];
            buf[2] = pem_array[((b2 << 2) & 60) + ((c >>> 6) & 3)];
            buf[3] = pem_array[c & 63];
        }
        try {
            this.outWriter.writeCharacters(buf, 0, 4);
        } catch (XMLStreamException e) {
            Logger.getLogger(Base64EncoderStream.class.getName()).log(Level.SEVERE, (String) null, e);
            throw new IOException(e);
        }
    }
}
