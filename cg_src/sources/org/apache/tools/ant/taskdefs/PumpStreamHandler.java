package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import org.apache.tools.ant.taskdefs.StreamPumper;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/PumpStreamHandler.class */
public class PumpStreamHandler implements ExecuteStreamHandler {
    private Thread outputThread;
    private Thread errorThread;
    private Thread inputThread;
    private OutputStream out;
    private OutputStream err;
    private InputStream input;
    private final boolean nonBlockingRead;
    private static final long JOIN_TIMEOUT = 200;

    public PumpStreamHandler(OutputStream out, OutputStream err, InputStream input, boolean nonBlockingRead) {
        if (out == null) {
            throw new NullPointerException("out must not be null");
        }
        if (err == null) {
            throw new NullPointerException("err must not be null");
        }
        this.out = out;
        this.err = err;
        this.input = input;
        this.nonBlockingRead = nonBlockingRead;
    }

    public PumpStreamHandler(OutputStream out, OutputStream err, InputStream input) {
        this(out, err, input, false);
    }

    public PumpStreamHandler(OutputStream out, OutputStream err) {
        this(out, err, null);
    }

    public PumpStreamHandler(OutputStream outAndErr) {
        this(outAndErr, outAndErr);
    }

    public PumpStreamHandler() {
        this(System.out, System.err);
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void setProcessOutputStream(InputStream is) {
        createProcessOutputPump(is, this.out);
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void setProcessErrorStream(InputStream is) {
        createProcessErrorPump(is, this.err);
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void setProcessInputStream(OutputStream os) {
        if (this.input != null) {
            this.inputThread = createPump(this.input, os, true, this.nonBlockingRead);
        } else {
            FileUtils.close(os);
        }
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void start() {
        start(this.outputThread);
        start(this.errorThread);
        start(this.inputThread);
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void stop() {
        finish(this.inputThread);
        try {
            this.err.flush();
        } catch (IOException e) {
        }
        try {
            this.out.flush();
        } catch (IOException e2) {
        }
        finish(this.outputThread);
        finish(this.errorThread);
    }

    private void start(Thread t) {
        if (t != null) {
            t.start();
        }
    }

    protected final void finish(Thread t) {
        if (t == null) {
            return;
        }
        try {
            StreamPumper s = null;
            if (t instanceof ThreadWithPumper) {
                s = ((ThreadWithPumper) t).getPumper();
            }
            if ((s != null && s.isFinished()) || !t.isAlive()) {
                return;
            }
            StreamPumper.PostStopHandle postStopHandle = null;
            if (s != null && !s.isFinished()) {
                postStopHandle = s.stop();
            }
            if (postStopHandle != null && postStopHandle.isInPostStopTasks()) {
                postStopHandle.awaitPostStopCompletion(2L, TimeUnit.SECONDS);
            }
            while (true) {
                if (s != null) {
                    if (s.isFinished()) {
                        break;
                    }
                }
                if (!t.isAlive()) {
                    break;
                }
                t.interrupt();
                t.join(JOIN_TIMEOUT);
            }
        } catch (InterruptedException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public OutputStream getErr() {
        return this.err;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public OutputStream getOut() {
        return this.out;
    }

    protected void createProcessOutputPump(InputStream is, OutputStream os) {
        this.outputThread = createPump(is, os);
    }

    protected void createProcessErrorPump(InputStream is, OutputStream os) {
        this.errorThread = createPump(is, os);
    }

    protected Thread createPump(InputStream is, OutputStream os) {
        return createPump(is, os, false);
    }

    protected Thread createPump(InputStream is, OutputStream os, boolean closeWhenExhausted) {
        return createPump(is, os, closeWhenExhausted, true);
    }

    protected Thread createPump(InputStream is, OutputStream os, boolean closeWhenExhausted, boolean nonBlockingIO) {
        StreamPumper pumper = new StreamPumper(is, os, closeWhenExhausted, nonBlockingIO);
        pumper.setAutoflush(true);
        Thread result = new ThreadWithPumper(pumper);
        result.setDaemon(true);
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/PumpStreamHandler$ThreadWithPumper.class */
    public static class ThreadWithPumper extends Thread {
        private final StreamPumper pumper;

        public ThreadWithPumper(StreamPumper p) {
            super(p);
            this.pumper = p;
        }

        protected StreamPumper getPumper() {
            return this.pumper;
        }
    }
}
