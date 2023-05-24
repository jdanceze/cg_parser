package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.TimeoutObserver;
import org.apache.tools.ant.util.Watchdog;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/ExecuteWatchdog.class */
public class ExecuteWatchdog implements TimeoutObserver {
    private Process process;
    private volatile boolean watch;
    private Exception caught;
    private volatile boolean killedProcess;
    private Watchdog watchdog;

    public ExecuteWatchdog(long timeout) {
        this.watch = false;
        this.caught = null;
        this.killedProcess = false;
        this.watchdog = new Watchdog(timeout);
        this.watchdog.addTimeoutObserver(this);
    }

    @Deprecated
    public ExecuteWatchdog(int timeout) {
        this(timeout);
    }

    public synchronized void start(Process process) {
        if (process == null) {
            throw new NullPointerException("process is null.");
        }
        if (this.process != null) {
            throw new IllegalStateException("Already running.");
        }
        this.caught = null;
        this.killedProcess = false;
        this.watch = true;
        this.process = process;
        this.watchdog.start();
    }

    public synchronized void stop() {
        this.watchdog.stop();
        cleanUp();
    }

    @Override // org.apache.tools.ant.util.TimeoutObserver
    public synchronized void timeoutOccured(Watchdog w) {
        try {
            try {
                this.process.exitValue();
            } catch (Exception e) {
                this.caught = e;
            } finally {
                cleanUp();
            }
        } catch (IllegalThreadStateException e2) {
            if (this.watch) {
                this.killedProcess = true;
                this.process.destroy();
            }
        }
    }

    protected synchronized void cleanUp() {
        this.watch = false;
        this.process = null;
    }

    public synchronized void checkException() throws BuildException {
        if (this.caught != null) {
            throw new BuildException("Exception in ExecuteWatchdog.run: " + this.caught.getMessage(), this.caught);
        }
    }

    public boolean isWatching() {
        return this.watch;
    }

    public boolean killedProcess() {
        return this.killedProcess;
    }
}
