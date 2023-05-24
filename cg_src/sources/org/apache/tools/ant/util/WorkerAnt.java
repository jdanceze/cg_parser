package org.apache.tools.ant.util;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/WorkerAnt.class */
public class WorkerAnt extends Thread {
    private Task task;
    private Object notify;
    private volatile boolean finished;
    private volatile BuildException buildException;
    private volatile Throwable exception;
    public static final String ERROR_NO_TASK = "No task defined";

    public WorkerAnt(Task task, Object notify) {
        this.finished = false;
        this.task = task;
        this.notify = notify != null ? notify : this;
    }

    public WorkerAnt(Task task) {
        this(task, null);
    }

    public synchronized BuildException getBuildException() {
        return this.buildException;
    }

    public synchronized Throwable getException() {
        return this.exception;
    }

    public Task getTask() {
        return this.task;
    }

    public synchronized boolean isFinished() {
        return this.finished;
    }

    public void waitUntilFinished(long timeout) throws InterruptedException {
        long start = System.currentTimeMillis();
        long end = start + timeout;
        synchronized (this.notify) {
            for (long now = System.currentTimeMillis(); !this.finished && now < end; now = System.currentTimeMillis()) {
                this.notify.wait(end - now);
            }
        }
    }

    public void rethrowAnyBuildException() {
        BuildException ex = getBuildException();
        if (ex != null) {
            throw ex;
        }
    }

    private synchronized void caught(Throwable thrown) {
        BuildException buildException;
        this.exception = thrown;
        if (thrown instanceof BuildException) {
            buildException = (BuildException) thrown;
        } else {
            buildException = new BuildException(thrown);
        }
        this.buildException = buildException;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            if (this.task != null) {
                this.task.execute();
            }
            synchronized (this.notify) {
                this.finished = true;
                this.notify.notifyAll();
            }
        } catch (Throwable thrown) {
            try {
                caught(thrown);
                synchronized (this.notify) {
                    this.finished = true;
                    this.notify.notifyAll();
                }
            } catch (Throwable th) {
                synchronized (this.notify) {
                    this.finished = true;
                    this.notify.notifyAll();
                    throw th;
                }
            }
        }
    }
}
