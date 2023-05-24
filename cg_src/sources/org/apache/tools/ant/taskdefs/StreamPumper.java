package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/StreamPumper.class */
public class StreamPumper implements Runnable {
    private static final int SMALL_BUFFER_SIZE = 128;
    private final InputStream is;
    private final OutputStream os;
    private volatile boolean askedToStop;
    private volatile boolean finished;
    private final boolean closeWhenExhausted;
    private boolean autoflush;
    private Exception exception;
    private int bufferSize;
    private boolean started;
    private final boolean useAvailable;
    private PostStopHandle postStopHandle;
    private static final long POLL_INTERVAL = 100;

    public StreamPumper(InputStream is, OutputStream os, boolean closeWhenExhausted) {
        this(is, os, closeWhenExhausted, false);
    }

    public StreamPumper(InputStream is, OutputStream os, boolean closeWhenExhausted, boolean useAvailable) {
        this.autoflush = false;
        this.exception = null;
        this.bufferSize = 128;
        this.started = false;
        this.is = is;
        this.os = os;
        this.closeWhenExhausted = closeWhenExhausted;
        this.useAvailable = useAvailable;
    }

    public StreamPumper(InputStream is, OutputStream os) {
        this(is, os, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAutoflush(boolean autoflush) {
        this.autoflush = autoflush;
    }

    @Override // java.lang.Runnable
    public void run() {
        int length;
        synchronized (this) {
            this.started = true;
        }
        this.finished = false;
        byte[] buf = new byte[this.bufferSize];
        while (!this.askedToStop && !Thread.interrupted()) {
            try {
                try {
                    waitForInput(this.is);
                    if (this.askedToStop || Thread.interrupted() || (length = this.is.read(buf)) < 0) {
                        break;
                    } else if (length > 0) {
                        this.os.write(buf, 0, length);
                        if (this.autoflush) {
                            this.os.flush();
                        }
                    }
                } catch (InterruptedException e) {
                    if (this.closeWhenExhausted) {
                        FileUtils.close(this.os);
                    }
                    this.finished = true;
                    this.askedToStop = false;
                    synchronized (this) {
                        notifyAll();
                        return;
                    }
                } catch (Exception e2) {
                    synchronized (this) {
                        this.exception = e2;
                        if (this.closeWhenExhausted) {
                            FileUtils.close(this.os);
                        }
                        this.finished = true;
                        this.askedToStop = false;
                        synchronized (this) {
                            notifyAll();
                            return;
                        }
                    }
                }
            } catch (Throwable th) {
                if (this.closeWhenExhausted) {
                    FileUtils.close(this.os);
                }
                this.finished = true;
                this.askedToStop = false;
                synchronized (this) {
                    notifyAll();
                    throw th;
                }
            }
        }
        doPostStop();
        if (this.closeWhenExhausted) {
            FileUtils.close(this.os);
        }
        this.finished = true;
        this.askedToStop = false;
        synchronized (this) {
            notifyAll();
        }
    }

    public boolean isFinished() {
        return this.finished;
    }

    public synchronized void waitFor() throws InterruptedException {
        while (!isFinished()) {
            wait();
        }
    }

    public synchronized void setBufferSize(int bufferSize) {
        if (this.started) {
            throw new IllegalStateException("Cannot set buffer size on a running StreamPumper");
        }
        this.bufferSize = bufferSize;
    }

    public synchronized int getBufferSize() {
        return this.bufferSize;
    }

    public synchronized Exception getException() {
        return this.exception;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized PostStopHandle stop() {
        this.askedToStop = true;
        this.postStopHandle = new PostStopHandle();
        notifyAll();
        return this.postStopHandle;
    }

    private void waitForInput(InputStream is) throws IOException, InterruptedException {
        if (this.useAvailable) {
            while (!this.askedToStop && is.available() == 0) {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                synchronized (this) {
                    wait(POLL_INTERVAL);
                }
            }
        }
    }

    private void doPostStop() throws IOException {
        int length;
        try {
            byte[] buf = new byte[this.bufferSize];
            if (this.askedToStop) {
                while (true) {
                    int bytesReadableWithoutBlocking = this.is.available();
                    if (bytesReadableWithoutBlocking <= 0 || (length = this.is.read(buf, 0, Math.min(bytesReadableWithoutBlocking, buf.length))) <= 0) {
                        break;
                    }
                    this.os.write(buf, 0, length);
                }
            }
            this.os.flush();
            if (this.postStopHandle == null) {
                return;
            }
            this.postStopHandle.latch.countDown();
            this.postStopHandle.inPostStopTasks = false;
        } catch (Throwable th) {
            if (this.postStopHandle != null) {
                this.postStopHandle.latch.countDown();
                this.postStopHandle.inPostStopTasks = false;
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/StreamPumper$PostStopHandle.class */
    public final class PostStopHandle {
        private boolean inPostStopTasks = true;
        private final CountDownLatch latch = new CountDownLatch(1);

        PostStopHandle() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isInPostStopTasks() {
            return this.inPostStopTasks;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean awaitPostStopCompletion(long timeout, TimeUnit timeUnit) throws InterruptedException {
            return this.latch.await(timeout, timeUnit);
        }
    }
}
