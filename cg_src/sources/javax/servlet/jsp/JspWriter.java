package javax.servlet.jsp;

import java.io.IOException;
import java.io.Writer;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/JspWriter.class */
public abstract class JspWriter extends Writer {
    public static final int NO_BUFFER = 0;
    public static final int DEFAULT_BUFFER = -1;
    public static final int UNBOUNDED_BUFFER = -2;
    protected int bufferSize;
    protected boolean autoFlush;

    public abstract void newLine() throws IOException;

    public abstract void print(boolean z) throws IOException;

    public abstract void print(char c) throws IOException;

    public abstract void print(int i) throws IOException;

    public abstract void print(long j) throws IOException;

    public abstract void print(float f) throws IOException;

    public abstract void print(double d) throws IOException;

    public abstract void print(char[] cArr) throws IOException;

    public abstract void print(String str) throws IOException;

    public abstract void print(Object obj) throws IOException;

    public abstract void println() throws IOException;

    public abstract void println(boolean z) throws IOException;

    public abstract void println(char c) throws IOException;

    public abstract void println(int i) throws IOException;

    public abstract void println(long j) throws IOException;

    public abstract void println(float f) throws IOException;

    public abstract void println(double d) throws IOException;

    public abstract void println(char[] cArr) throws IOException;

    public abstract void println(String str) throws IOException;

    public abstract void println(Object obj) throws IOException;

    public abstract void clear() throws IOException;

    public abstract void clearBuffer() throws IOException;

    @Override // java.io.Writer, java.io.Flushable
    public abstract void flush() throws IOException;

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public abstract void close() throws IOException;

    public abstract int getRemaining();

    /* JADX INFO: Access modifiers changed from: protected */
    public JspWriter(int bufferSize, boolean autoFlush) {
        this.bufferSize = bufferSize;
        this.autoFlush = autoFlush;
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public boolean isAutoFlush() {
        return this.autoFlush;
    }
}
