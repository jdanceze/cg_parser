package org.apache.tools.ant.taskdefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ExitStatusException;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.property.LocalProperties;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Parallel.class */
public class Parallel extends Task implements TaskContainer {
    private static final int NUMBER_TRIES = 100;
    private long timeout;
    private volatile boolean stillRunning;
    private boolean timedOut;
    private boolean failOnAny;
    private TaskList daemonTasks;
    private StringBuffer exceptionMessage;
    private Throwable firstException;
    private Location firstLocation;
    private Integer firstExitStatus;
    private Vector<Task> nestedTasks = new Vector<>();
    private final Object semaphore = new Object();
    private int numThreads = 0;
    private int numThreadsPerProcessor = 0;
    private int numExceptions = 0;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Parallel$TaskList.class */
    public static class TaskList implements TaskContainer {
        private List<Task> tasks = new ArrayList();

        @Override // org.apache.tools.ant.TaskContainer
        public void addTask(Task nestedTask) {
            this.tasks.add(nestedTask);
        }
    }

    public void addDaemons(TaskList daemonTasks) {
        if (this.daemonTasks != null) {
            throw new BuildException("Only one daemon group is supported");
        }
        this.daemonTasks = daemonTasks;
    }

    public void setPollInterval(int pollInterval) {
    }

    public void setFailOnAny(boolean failOnAny) {
        this.failOnAny = failOnAny;
    }

    @Override // org.apache.tools.ant.TaskContainer
    public void addTask(Task nestedTask) {
        this.nestedTasks.addElement(nestedTask);
    }

    public void setThreadsPerProcessor(int numThreadsPerProcessor) {
        this.numThreadsPerProcessor = numThreadsPerProcessor;
    }

    public void setThreadCount(int numThreads) {
        this.numThreads = numThreads;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        updateThreadCounts();
        if (this.numThreads == 0) {
            this.numThreads = this.nestedTasks.size();
        }
        spinThreads();
    }

    private void updateThreadCounts() {
        if (this.numThreadsPerProcessor != 0) {
            this.numThreads = Runtime.getRuntime().availableProcessors() * this.numThreadsPerProcessor;
        }
    }

    private void processExceptions(TaskRunnable[] runnables) {
        if (runnables == null) {
            return;
        }
        for (TaskRunnable runnable : runnables) {
            Throwable t = runnable.getException();
            if (t != null) {
                this.numExceptions++;
                if (this.firstException == null) {
                    this.firstException = t;
                }
                if ((t instanceof BuildException) && this.firstLocation == Location.UNKNOWN_LOCATION) {
                    this.firstLocation = ((BuildException) t).getLocation();
                }
                if ((t instanceof ExitStatusException) && this.firstExitStatus == null) {
                    ExitStatusException ex = (ExitStatusException) t;
                    this.firstExitStatus = Integer.valueOf(ex.getStatus());
                    this.firstLocation = ex.getLocation();
                }
                this.exceptionMessage.append(System.lineSeparator());
                this.exceptionMessage.append(t.getMessage());
            }
        }
    }

    private void spinThreads() throws BuildException {
        this.stillRunning = true;
        this.timedOut = false;
        boolean interrupted = false;
        TaskRunnable[] runnables = (TaskRunnable[]) this.nestedTasks.stream().map(x$0 -> {
            return new TaskRunnable(x$0);
        }).toArray(x$02 -> {
            return new TaskRunnable[x$02];
        });
        int numTasks = this.nestedTasks.size();
        int maxRunning = numTasks < this.numThreads ? numTasks : this.numThreads;
        TaskRunnable[] running = new TaskRunnable[maxRunning];
        ThreadGroup group = new ThreadGroup("parallel");
        TaskRunnable[] daemons = null;
        if (this.daemonTasks != null && !this.daemonTasks.tasks.isEmpty()) {
            daemons = new TaskRunnable[this.daemonTasks.tasks.size()];
        }
        synchronized (this.semaphore) {
        }
        synchronized (this.semaphore) {
            if (daemons != null) {
                for (int i = 0; i < daemons.length; i++) {
                    daemons[i] = new TaskRunnable((Task) this.daemonTasks.tasks.get(i));
                    Thread daemonThread = new Thread(group, daemons[i]);
                    daemonThread.setDaemon(true);
                    daemonThread.start();
                }
            }
            int threadNumber = 0;
            for (int i2 = 0; i2 < maxRunning; i2++) {
                int i3 = threadNumber;
                threadNumber++;
                running[i2] = runnables[i3];
                Thread thread = new Thread(group, running[i2]);
                thread.start();
            }
            if (this.timeout != 0) {
                Thread timeoutThread = new Thread() { // from class: org.apache.tools.ant.taskdefs.Parallel.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public synchronized void run() {
                        try {
                            long start = System.currentTimeMillis();
                            long end = start + Parallel.this.timeout;
                            for (long now = System.currentTimeMillis(); now < end; now = System.currentTimeMillis()) {
                                wait(end - now);
                            }
                            synchronized (Parallel.this.semaphore) {
                                Parallel.this.stillRunning = false;
                                Parallel.this.timedOut = true;
                                Parallel.this.semaphore.notifyAll();
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                };
                timeoutThread.start();
            }
            while (threadNumber < numTasks) {
                try {
                    if (!this.stillRunning) {
                        break;
                    }
                    for (int i4 = 0; i4 < maxRunning; i4++) {
                        if (running[i4] == null || running[i4].isFinished()) {
                            int i5 = threadNumber;
                            threadNumber++;
                            running[i4] = runnables[i5];
                            Thread thread2 = new Thread(group, running[i4]);
                            thread2.start();
                            break;
                        }
                    }
                    this.semaphore.wait();
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
            while (this.stillRunning) {
                int i6 = 0;
                while (true) {
                    if (i6 < maxRunning) {
                        if (running[i6] == null || running[i6].isFinished()) {
                            i6++;
                        } else {
                            this.semaphore.wait();
                            break;
                        }
                    } else {
                        this.stillRunning = false;
                        break;
                    }
                }
            }
            if (!this.timedOut && !this.failOnAny) {
                killAll(running);
            }
        }
        if (interrupted) {
            throw new BuildException("Parallel execution interrupted.");
        }
        if (this.timedOut) {
            throw new BuildException("Parallel execution timed out");
        }
        this.exceptionMessage = new StringBuffer();
        this.numExceptions = 0;
        this.firstException = null;
        this.firstExitStatus = null;
        this.firstLocation = Location.UNKNOWN_LOCATION;
        processExceptions(daemons);
        processExceptions(runnables);
        if (this.numExceptions == 1) {
            if (this.firstException instanceof BuildException) {
                throw ((BuildException) this.firstException);
            }
            throw new BuildException(this.firstException);
        } else if (this.numExceptions > 1) {
            if (this.firstExitStatus == null) {
                throw new BuildException(this.exceptionMessage.toString(), this.firstLocation);
            }
            throw new ExitStatusException(this.exceptionMessage.toString(), this.firstExitStatus.intValue(), this.firstLocation);
        }
    }

    private void killAll(TaskRunnable[] running) {
        int tries = 0;
        do {
            boolean oneAlive = false;
            for (TaskRunnable runnable : running) {
                if (runnable != null && !runnable.isFinished()) {
                    runnable.interrupt();
                    Thread.yield();
                    oneAlive = true;
                }
            }
            if (oneAlive) {
                tries++;
                Thread.yield();
            }
            if (!oneAlive) {
                return;
            }
        } while (tries < 100);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Parallel$TaskRunnable.class */
    public class TaskRunnable implements Runnable {
        private Throwable exception;
        private Task task;
        private boolean finished;
        private volatile Thread thread;

        TaskRunnable(Task task) {
            this.task = task;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                LocalProperties.get(Parallel.this.getProject()).copy();
                this.thread = Thread.currentThread();
                this.task.perform();
                synchronized (Parallel.this.semaphore) {
                    this.finished = true;
                    Parallel.this.semaphore.notifyAll();
                }
            } catch (Throwable t) {
                try {
                    this.exception = t;
                    if (Parallel.this.failOnAny) {
                        Parallel.this.stillRunning = false;
                    }
                    synchronized (Parallel.this.semaphore) {
                        this.finished = true;
                        Parallel.this.semaphore.notifyAll();
                    }
                } catch (Throwable th) {
                    synchronized (Parallel.this.semaphore) {
                        this.finished = true;
                        Parallel.this.semaphore.notifyAll();
                        throw th;
                    }
                }
            }
        }

        public Throwable getException() {
            return this.exception;
        }

        boolean isFinished() {
            return this.finished;
        }

        void interrupt() {
            this.thread.interrupt();
        }
    }
}
