package org.apache.tools.ant.taskdefs;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.taskdefs.condition.ConditionBase;
import org.apache.tools.ant.types.EnumeratedAttribute;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/WaitFor.class */
public class WaitFor extends ConditionBase {
    public static final long ONE_MILLISECOND = 1;
    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = 60000;
    public static final long ONE_HOUR = 3600000;
    public static final long ONE_DAY = 86400000;
    public static final long ONE_WEEK = 604800000;
    public static final long DEFAULT_MAX_WAIT_MILLIS = 180000;
    public static final long DEFAULT_CHECK_MILLIS = 500;
    private long maxWait;
    private long maxWaitMultiplier;
    private long checkEvery;
    private long checkEveryMultiplier;
    private String timeoutProperty;

    public WaitFor() {
        super("waitfor");
        this.maxWait = DEFAULT_MAX_WAIT_MILLIS;
        this.maxWaitMultiplier = 1L;
        this.checkEvery = 500L;
        this.checkEveryMultiplier = 1L;
    }

    public WaitFor(String taskName) {
        super(taskName);
        this.maxWait = DEFAULT_MAX_WAIT_MILLIS;
        this.maxWaitMultiplier = 1L;
        this.checkEvery = 500L;
        this.checkEveryMultiplier = 1L;
    }

    public void setMaxWait(long time) {
        this.maxWait = time;
    }

    public void setMaxWaitUnit(Unit unit) {
        this.maxWaitMultiplier = unit.getMultiplier();
    }

    public void setCheckEvery(long time) {
        this.checkEvery = time;
    }

    public void setCheckEveryUnit(Unit unit) {
        this.checkEveryMultiplier = unit.getMultiplier();
    }

    public void setTimeoutProperty(String p) {
        this.timeoutProperty = p;
    }

    public void execute() throws BuildException {
        if (countConditions() > 1) {
            throw new BuildException("You must not nest more than one condition into %s", getTaskName());
        }
        if (countConditions() < 1) {
            throw new BuildException("You must nest a condition into %s", getTaskName());
        }
        Condition c = getConditions().nextElement();
        try {
            long maxWaitMillis = calculateMaxWaitMillis();
            long checkEveryMillis = calculateCheckEveryMillis();
            long start = System.currentTimeMillis();
            long end = start + maxWaitMillis;
            while (System.currentTimeMillis() < end) {
                if (c.eval()) {
                    processSuccess();
                    return;
                }
                Thread.sleep(checkEveryMillis);
            }
        } catch (InterruptedException e) {
            log("Task " + getTaskName() + " interrupted, treating as timed out.");
        }
        processTimeout();
    }

    public long calculateCheckEveryMillis() {
        return this.checkEvery * this.checkEveryMultiplier;
    }

    public long calculateMaxWaitMillis() {
        return this.maxWait * this.maxWaitMultiplier;
    }

    protected void processSuccess() {
        log(getTaskName() + ": condition was met", 3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void processTimeout() {
        log(getTaskName() + ": timeout", 3);
        if (this.timeoutProperty != null) {
            getProject().setNewProperty(this.timeoutProperty, "true");
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/WaitFor$Unit.class */
    public static class Unit extends EnumeratedAttribute {
        public static final String MILLISECOND = "millisecond";
        public static final String SECOND = "second";
        public static final String MINUTE = "minute";
        public static final String HOUR = "hour";
        public static final String DAY = "day";
        public static final String WEEK = "week";
        private static final String[] UNITS = {MILLISECOND, SECOND, MINUTE, HOUR, DAY, WEEK};
        private Map<String, Long> timeTable = new HashMap();

        public Unit() {
            this.timeTable.put(MILLISECOND, 1L);
            this.timeTable.put(SECOND, 1000L);
            this.timeTable.put(MINUTE, 60000L);
            this.timeTable.put(HOUR, 3600000L);
            this.timeTable.put(DAY, 86400000L);
            this.timeTable.put(WEEK, 604800000L);
        }

        public long getMultiplier() {
            String key = getValue().toLowerCase(Locale.ENGLISH);
            return this.timeTable.get(key).longValue();
        }

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return UNITS;
        }
    }
}
