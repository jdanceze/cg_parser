package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedBytes;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/io/ReaderInputStream.class */
final class ReaderInputStream extends InputStream {
    private final Reader reader;
    private final CharsetEncoder encoder;
    private final byte[] singleByte;
    private CharBuffer charBuffer;
    private ByteBuffer byteBuffer;
    private boolean endOfInput;
    private boolean draining;
    private boolean doneFlushing;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReaderInputStream(Reader reader, Charset charset, int bufferSize) {
        this(reader, charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE), bufferSize);
    }

    ReaderInputStream(Reader reader, CharsetEncoder encoder, int bufferSize) {
        this.singleByte = new byte[1];
        this.reader = (Reader) Preconditions.checkNotNull(reader);
        this.encoder = (CharsetEncoder) Preconditions.checkNotNull(encoder);
        Preconditions.checkArgument(bufferSize > 0, "bufferSize must be positive: %s", bufferSize);
        encoder.reset();
        this.charBuffer = CharBuffer.allocate(bufferSize);
        this.charBuffer.flip();
        this.byteBuffer = ByteBuffer.allocate(bufferSize);
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.reader.close();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (read(this.singleByte) == 1) {
            return UnsignedBytes.toInt(this.singleByte[0]);
        }
        return -1;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        CoderResult result;
        Preconditions.checkPositionIndexes(off, off + len, b.length);
        if (len == 0) {
            return 0;
        }
        int totalBytesRead = 0;
        boolean doneEncoding = this.endOfInput;
        while (true) {
            if (this.draining) {
                totalBytesRead += drain(b, off + totalBytesRead, len - totalBytesRead);
                if (totalBytesRead == len || this.doneFlushing) {
                    break;
                }
                this.draining = false;
                this.byteBuffer.clear();
            }
            while (true) {
                if (this.doneFlushing) {
                    result = CoderResult.UNDERFLOW;
                } else if (doneEncoding) {
                    result = this.encoder.flush(this.byteBuffer);
                } else {
                    result = this.encoder.encode(this.charBuffer, this.byteBuffer, this.endOfInput);
                }
                if (result.isOverflow()) {
                    startDraining(true);
                    break;
                } else if (result.isUnderflow()) {
                    if (doneEncoding) {
                        this.doneFlushing = true;
                        startDraining(false);
                        break;
                    } else if (this.endOfInput) {
                        doneEncoding = true;
                    } else {
                        readMoreChars();
                    }
                } else if (result.isError()) {
                    result.throwException();
                    return 0;
                }
            }
        }
        if (totalBytesRead > 0) {
            return totalBytesRead;
        }
        return -1;
    }

    private static CharBuffer grow(CharBuffer buf) {
        char[] copy = Arrays.copyOf(buf.array(), buf.capacity() * 2);
        CharBuffer bigger = CharBuffer.wrap(copy);
        bigger.position(buf.position());
        bigger.limit(buf.limit());
        return bigger;
    }

    private void readMoreChars() throws IOException {
        if (availableCapacity(this.charBuffer) == 0) {
            if (this.charBuffer.position() > 0) {
                this.charBuffer.compact().flip();
            } else {
                this.charBuffer = grow(this.charBuffer);
            }
        }
        int limit = this.charBuffer.limit();
        int numChars = this.reader.read(this.charBuffer.array(), limit, availableCapacity(this.charBuffer));
        if (numChars == -1) {
            this.endOfInput = true;
        } else {
            this.charBuffer.limit(limit + numChars);
        }
    }

    private static int availableCapacity(Buffer buffer) {
        return buffer.capacity() - buffer.limit();
    }

    private void startDraining(boolean overflow) {
        this.byteBuffer.flip();
        if (overflow && this.byteBuffer.remaining() == 0) {
            this.byteBuffer = ByteBuffer.allocate(this.byteBuffer.capacity() * 2);
        } else {
            this.draining = true;
        }
    }

    private int drain(byte[] b, int off, int len) {
        int remaining = Math.min(len, this.byteBuffer.remaining());
        this.byteBuffer.get(b, off, remaining);
        return remaining;
    }
}
