package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/OutputStreamFunneler.class */
public class OutputStreamFunneler {
    public static final long DEFAULT_TIMEOUT_MILLIS = 1000;
    private OutputStream out;
    private int count;
    private boolean closed;
    private long timeoutMillis;

    static /* synthetic */ int access$004(OutputStreamFunneler x0) {
        int i = x0.count + 1;
        x0.count = i;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/OutputStreamFunneler$Funnel.class */
    public final class Funnel extends OutputStream {
        private boolean closed;

        private Funnel() {
            this.closed = false;
            synchronized (OutputStreamFunneler.this) {
                OutputStreamFunneler.access$004(OutputStreamFunneler.this);
            }
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            synchronized (OutputStreamFunneler.this) {
                OutputStreamFunneler.this.dieIfClosed();
                OutputStreamFunneler.this.out.flush();
            }
        }

        @Override // java.io.OutputStream
        public void write(int b) throws IOException {
            synchronized (OutputStreamFunneler.this) {
                OutputStreamFunneler.this.dieIfClosed();
                OutputStreamFunneler.this.out.write(b);
            }
        }

        @Override // java.io.OutputStream
        public void write(byte[] b) throws IOException {
            synchronized (OutputStreamFunneler.this) {
                OutputStreamFunneler.this.dieIfClosed();
                OutputStreamFunneler.this.out.write(b);
            }
        }

        @Override // java.io.OutputStream
        public void write(byte[] b, int off, int len) throws IOException {
            synchronized (OutputStreamFunneler.this) {
                OutputStreamFunneler.this.dieIfClosed();
                OutputStreamFunneler.this.out.write(b, off, len);
            }
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            OutputStreamFunneler.this.release(this);
        }
    }

    public OutputStreamFunneler(OutputStream out) {
        this(out, 1000L);
    }

    public OutputStreamFunneler(OutputStream out, long timeoutMillis) {
        this.count = 0;
        if (out == null) {
            throw new IllegalArgumentException("OutputStreamFunneler.<init>:  out == null");
        }
        this.out = out;
        this.closed = false;
        setTimeout(timeoutMillis);
    }

    public synchronized void setTimeout(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public synchronized OutputStream getFunnelInstance() throws IOException {
        dieIfClosed();
        try {
            return new Funnel();
        } finally {
            notifyAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void release(Funnel funnel) throws IOException {
        if (!funnel.closed) {
            try {
                if (this.timeoutMillis > 0) {
                    long start = System.currentTimeMillis();
                    long end = start + this.timeoutMillis;
                    for (long now = System.currentTimeMillis(); now < end; now = System.currentTimeMillis()) {
                        try {
                            wait(end - now);
                        } catch (InterruptedException e) {
                        }
                    }
                }
                int i = this.count - 1;
                this.count = i;
                if (i == 0) {
                    close();
                }
            } finally {
                funnel.closed = true;
            }
        }
    }

    private synchronized void close() throws IOException {
        try {
            dieIfClosed();
            this.out.close();
        } finally {
            this.closed = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dieIfClosed() throws IOException {
        if (this.closed) {
            throw new IOException("The funneled OutputStream has been closed.");
        }
    }
}
