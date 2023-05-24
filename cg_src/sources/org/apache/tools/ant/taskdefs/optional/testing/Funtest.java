package org.apache.tools.ant.taskdefs.optional.testing;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskAdapter;
import org.apache.tools.ant.taskdefs.Parallel;
import org.apache.tools.ant.taskdefs.Sequential;
import org.apache.tools.ant.taskdefs.WaitFor;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.taskdefs.condition.ConditionBase;
import org.apache.tools.ant.util.WorkerAnt;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/testing/Funtest.class */
public class Funtest extends Task {
    public static final String WARN_OVERRIDING = "Overriding previous definition of ";
    public static final String APPLICATION_FORCIBLY_SHUT_DOWN = "Application forcibly shut down";
    public static final String SHUTDOWN_INTERRUPTED = "Shutdown interrupted";
    public static final String SKIPPING_TESTS = "Condition failed -skipping tests";
    public static final String APPLICATION_EXCEPTION = "Application Exception";
    public static final String TEARDOWN_EXCEPTION = "Teardown Exception";
    private NestedCondition condition;
    private Parallel timedTests;
    private Sequential setup;
    private Sequential application;
    private BlockFor block;
    private Sequential tests;
    private Sequential reporting;
    private Sequential teardown;
    private long timeout;
    private String failureProperty;
    private BuildException testException;
    private BuildException teardownException;
    private BuildException applicationException;
    private BuildException taskException;
    private long timeoutUnitMultiplier = 1;
    private long shutdownTime = 10000;
    private long shutdownUnitMultiplier = 1;
    private String failureMessage = "Tests failed";
    private boolean failOnTeardownErrors = true;

    private void logOverride(String name, Object definition) {
        if (definition != null) {
            log("Overriding previous definition of <" + name + '>', 2);
        }
    }

    public ConditionBase createCondition() {
        logOverride("condition", this.condition);
        this.condition = new NestedCondition();
        return this.condition;
    }

    public void addApplication(Sequential sequence) {
        logOverride("application", this.application);
        this.application = sequence;
    }

    public void addSetup(Sequential sequence) {
        logOverride("setup", this.setup);
        this.setup = sequence;
    }

    public void addBlock(BlockFor sequence) {
        logOverride("block", this.block);
        this.block = sequence;
    }

    public void addTests(Sequential sequence) {
        logOverride("tests", this.tests);
        this.tests = sequence;
    }

    public void addReporting(Sequential sequence) {
        logOverride("reporting", this.reporting);
        this.reporting = sequence;
    }

    public void addTeardown(Sequential sequence) {
        logOverride("teardown", this.teardown);
        this.teardown = sequence;
    }

    public void setFailOnTeardownErrors(boolean failOnTeardownErrors) {
        this.failOnTeardownErrors = failOnTeardownErrors;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public void setFailureProperty(String failureProperty) {
        this.failureProperty = failureProperty;
    }

    public void setShutdownTime(long shutdownTime) {
        this.shutdownTime = shutdownTime;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setTimeoutUnit(WaitFor.Unit unit) {
        this.timeoutUnitMultiplier = unit.getMultiplier();
    }

    public void setShutdownUnit(WaitFor.Unit unit) {
        this.shutdownUnitMultiplier = unit.getMultiplier();
    }

    public BuildException getApplicationException() {
        return this.applicationException;
    }

    public BuildException getTeardownException() {
        return this.teardownException;
    }

    public BuildException getTestException() {
        return this.testException;
    }

    public BuildException getTaskException() {
        return this.taskException;
    }

    private void bind(Task task) {
        task.bindToOwner(this);
        task.init();
    }

    private Parallel newParallel(long parallelTimeout) {
        Parallel par = new Parallel();
        bind(par);
        par.setFailOnAny(true);
        par.setTimeout(parallelTimeout);
        return par;
    }

    private Parallel newParallel(long parallelTimeout, Task child) {
        Parallel par = newParallel(parallelTimeout);
        par.addTask(child);
        return par;
    }

    private void validateTask(Task task, String role) {
        if (task != null && task.getProject() == null) {
            throw new BuildException("%s task is not bound to the project %s", role, task);
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        validateTask(this.setup, "setup");
        validateTask(this.application, "application");
        validateTask(this.tests, "tests");
        validateTask(this.reporting, "reporting");
        validateTask(this.teardown, "teardown");
        if (this.condition != null && !this.condition.eval()) {
            log(SKIPPING_TESTS);
            return;
        }
        long timeoutMillis = this.timeout * this.timeoutUnitMultiplier;
        Parallel applicationRun = newParallel(timeoutMillis);
        WorkerAnt worker = new WorkerAnt(applicationRun, null);
        if (this.application != null) {
            applicationRun.addTask(this.application);
        }
        long testRunTimeout = 0;
        Sequential testRun = new Sequential();
        bind(testRun);
        if (this.block != null) {
            TaskAdapter ta = new TaskAdapter(this.block);
            ta.bindToOwner(this);
            validateTask(ta, "block");
            testRun.addTask(ta);
            testRunTimeout = this.block.calculateMaxWaitMillis();
        }
        if (this.tests != null) {
            testRun.addTask(this.tests);
            testRunTimeout += timeoutMillis;
        }
        if (this.reporting != null) {
            testRun.addTask(this.reporting);
            testRunTimeout += timeoutMillis;
        }
        this.timedTests = newParallel(testRunTimeout, testRun);
        try {
            try {
                if (this.setup != null) {
                    Parallel setupRun = newParallel(timeoutMillis, this.setup);
                    setupRun.execute();
                }
                worker.start();
                this.timedTests.execute();
                if (this.teardown != null) {
                    try {
                        Parallel teardownRun = newParallel(timeoutMillis, this.teardown);
                        teardownRun.execute();
                    } catch (BuildException e) {
                        this.teardownException = e;
                    }
                }
            } catch (Throwable th) {
                if (this.teardown != null) {
                    try {
                        Parallel teardownRun2 = newParallel(timeoutMillis, this.teardown);
                        teardownRun2.execute();
                    } catch (BuildException e2) {
                        this.teardownException = e2;
                    }
                }
                throw th;
            }
        } catch (BuildException e3) {
            this.testException = e3;
            if (this.teardown != null) {
                try {
                    Parallel teardownRun3 = newParallel(timeoutMillis, this.teardown);
                    teardownRun3.execute();
                } catch (BuildException e4) {
                    this.teardownException = e4;
                }
            }
        }
        try {
            long shutdownTimeMillis = this.shutdownTime * this.shutdownUnitMultiplier;
            worker.waitUntilFinished(shutdownTimeMillis);
            if (worker.isAlive()) {
                log(APPLICATION_FORCIBLY_SHUT_DOWN, 1);
                worker.interrupt();
                worker.waitUntilFinished(shutdownTimeMillis);
            }
        } catch (InterruptedException e5) {
            log(SHUTDOWN_INTERRUPTED, e5, 3);
        }
        this.applicationException = worker.getBuildException();
        processExceptions();
    }

    protected void processExceptions() {
        this.taskException = this.testException;
        if (this.applicationException != null) {
            if (this.taskException == null || (this.taskException instanceof BuildTimeoutException)) {
                this.taskException = this.applicationException;
            } else {
                ignoringThrowable(APPLICATION_EXCEPTION, this.applicationException);
            }
        }
        if (this.teardownException != null) {
            if (this.taskException == null && this.failOnTeardownErrors) {
                this.taskException = this.teardownException;
            } else {
                ignoringThrowable(TEARDOWN_EXCEPTION, this.teardownException);
            }
        }
        if (this.failureProperty != null && getProject().getProperty(this.failureProperty) != null) {
            log(this.failureMessage);
            if (this.taskException == null) {
                this.taskException = new BuildException(this.failureMessage);
            }
        }
        if (this.taskException != null) {
            throw this.taskException;
        }
    }

    protected void ignoringThrowable(String type, Throwable thrown) {
        log(type + ": " + thrown.toString(), thrown, 1);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/testing/Funtest$NestedCondition.class */
    private static class NestedCondition extends ConditionBase implements Condition {
        private NestedCondition() {
        }

        @Override // org.apache.tools.ant.taskdefs.condition.Condition
        public boolean eval() {
            if (countConditions() != 1) {
                throw new BuildException("A single nested condition is required.");
            }
            return getConditions().nextElement().eval();
        }
    }
}
