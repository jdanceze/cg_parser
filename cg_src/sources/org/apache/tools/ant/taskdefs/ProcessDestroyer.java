package org.apache.tools.ant.taskdefs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/ProcessDestroyer.class */
class ProcessDestroyer implements Runnable {
    private static final int THREAD_DIE_TIMEOUT = 20000;
    private Method addShutdownHookMethod;
    private Method removeShutdownHookMethod;
    private final Set<Process> processes = new HashSet();
    private ProcessDestroyerImpl destroyProcessThread = null;
    private boolean added = false;
    private boolean running = false;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/ProcessDestroyer$ProcessDestroyerImpl.class */
    public class ProcessDestroyerImpl extends Thread {
        private boolean shouldDestroy;

        public ProcessDestroyerImpl() {
            super("ProcessDestroyer Shutdown Hook");
            this.shouldDestroy = true;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            if (this.shouldDestroy) {
                ProcessDestroyer.this.run();
            }
        }

        public void setShouldDestroy(boolean shouldDestroy) {
            this.shouldDestroy = shouldDestroy;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProcessDestroyer() {
        try {
            this.addShutdownHookMethod = Runtime.class.getMethod("addShutdownHook", Thread.class);
            this.removeShutdownHookMethod = Runtime.class.getMethod("removeShutdownHook", Thread.class);
        } catch (NoSuchMethodException e) {
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void addShutdownHook() {
        if (this.addShutdownHookMethod != null && !this.running) {
            this.destroyProcessThread = new ProcessDestroyerImpl();
            try {
                this.addShutdownHookMethod.invoke(Runtime.getRuntime(), this.destroyProcessThread);
                this.added = true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e2) {
                Throwable t = e2.getTargetException();
                if (t != null && t.getClass() == IllegalStateException.class) {
                    this.running = true;
                } else {
                    e2.printStackTrace();
                }
            }
        }
    }

    private void removeShutdownHook() {
        if (this.removeShutdownHookMethod != null && this.added && !this.running) {
            try {
                if (!Boolean.TRUE.equals(this.removeShutdownHookMethod.invoke(Runtime.getRuntime(), this.destroyProcessThread))) {
                    System.err.println("Could not remove shutdown hook");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e2) {
                Throwable t = e2.getTargetException();
                if (t != null && t.getClass() == IllegalStateException.class) {
                    this.running = true;
                } else {
                    e2.printStackTrace();
                }
            }
            this.destroyProcessThread.setShouldDestroy(false);
            if (!this.destroyProcessThread.getThreadGroup().isDestroyed()) {
                this.destroyProcessThread.start();
            }
            try {
                this.destroyProcessThread.join(20000L);
            } catch (InterruptedException e3) {
            }
            this.destroyProcessThread = null;
            this.added = false;
        }
    }

    public boolean isAddedAsShutdownHook() {
        return this.added;
    }

    public boolean add(Process process) {
        boolean add;
        synchronized (this.processes) {
            if (this.processes.isEmpty()) {
                addShutdownHook();
            }
            add = this.processes.add(process);
        }
        return add;
    }

    public boolean remove(Process process) {
        boolean processRemoved;
        synchronized (this.processes) {
            processRemoved = this.processes.remove(process);
            if (processRemoved && this.processes.isEmpty()) {
                removeShutdownHook();
            }
        }
        return processRemoved;
    }

    @Override // java.lang.Runnable
    public void run() {
        synchronized (this.processes) {
            this.running = true;
            this.processes.forEach((v0) -> {
                v0.destroy();
            });
        }
    }
}
