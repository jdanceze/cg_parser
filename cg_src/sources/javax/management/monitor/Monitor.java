package javax.management.monitor;

import com.sun.jmx.trace.Trace;
import java.util.ArrayList;
import java.util.List;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/monitor/Monitor.class */
public abstract class Monitor extends NotificationBroadcasterSupport implements MonitorMBean, MBeanRegistration {
    protected static final int capacityIncrement = 16;
    protected static final int RESET_FLAGS_ALREADY_NOTIFIED = 0;
    protected static final int OBSERVED_OBJECT_ERROR_NOTIFIED = 1;
    protected static final int OBSERVED_ATTRIBUTE_ERROR_NOTIFIED = 2;
    protected static final int OBSERVED_ATTRIBUTE_TYPE_ERROR_NOTIFIED = 4;
    protected static final int RUNTIME_ERROR_NOTIFIED = 8;
    private List observedObjects = new ArrayList();
    private String observedAttribute = null;
    private long granularityPeriod = 10000;
    protected int elementCount = 0;
    protected int alreadyNotified = 0;
    protected int[] alreadyNotifieds = new int[16];
    protected MBeanServer server = null;
    protected String dbgTag = "Monitor";
    transient boolean isActive = false;
    transient long sequenceNumber = 0;

    public abstract void start();

    public abstract void stop();

    abstract void insertSpecificElementAt(int i);

    abstract void removeSpecificElementAt(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTraceOn() {
        return Trace.isSelected(1, 4);
    }

    void trace(String str, String str2, String str3) {
        Trace.send(1, 4, str, str2, str3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void trace(String str, String str2) {
        trace(this.dbgTag, str, str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDebugOn() {
        return Trace.isSelected(2, 4);
    }

    void debug(String str, String str2, String str3) {
        Trace.send(2, 4, str, str2, str3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void debug(String str, String str2) {
        debug(this.dbgTag, str, str2);
    }

    @Override // javax.management.MBeanRegistration
    public ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        if (isTraceOn()) {
            trace("preRegister", "initialize the reference on the MBean server");
        }
        this.server = mBeanServer;
        return objectName;
    }

    @Override // javax.management.MBeanRegistration
    public void postRegister(Boolean bool) {
    }

    public void preDeregister() throws Exception {
        if (isTraceOn()) {
            trace("preDeregister", "stop the monitor");
        }
        stop();
    }

    @Override // javax.management.MBeanRegistration
    public void postDeregister() {
    }

    @Override // javax.management.monitor.MonitorMBean
    public synchronized ObjectName getObservedObject() {
        return (ObjectName) this.observedObjects.get(0);
    }

    @Override // javax.management.monitor.MonitorMBean
    public synchronized void setObservedObject(ObjectName objectName) throws IllegalArgumentException {
        while (!this.observedObjects.isEmpty()) {
            removeObservedObject((ObjectName) this.observedObjects.get(0));
        }
        addObservedObject(objectName);
    }

    @Override // javax.management.monitor.MonitorMBean
    public void addObservedObject(ObjectName objectName) throws IllegalArgumentException {
        if (objectName == null) {
            throw new IllegalArgumentException("The object to observe cannot be null.");
        }
        if (this.observedObjects.contains(objectName)) {
            return;
        }
        this.observedObjects.add(objectName);
        insertintElementAt(this.alreadyNotifieds, 0 & (-2) & (-3) & (-5), this.elementCount);
        updateDeprecatedAlreadyNotified();
        insertSpecificElementAt(this.elementCount);
        this.elementCount++;
    }

    @Override // javax.management.monitor.MonitorMBean
    public void removeObservedObject(ObjectName objectName) {
        if (this.observedObjects.contains(objectName)) {
            int indexOf = this.observedObjects.indexOf(objectName);
            this.observedObjects.remove(objectName);
            removeintElementAt(this.alreadyNotifieds, indexOf);
            updateDeprecatedAlreadyNotified();
            removeSpecificElementAt(indexOf);
            this.elementCount--;
        }
    }

    @Override // javax.management.monitor.MonitorMBean
    public boolean containsObservedObject(ObjectName objectName) {
        return this.observedObjects.contains(objectName);
    }

    @Override // javax.management.monitor.MonitorMBean
    public ObjectName[] getObservedObjects() {
        ObjectName[] objectNameArr = new ObjectName[this.elementCount];
        for (int i = 0; i < this.elementCount; i++) {
            objectNameArr[i] = (ObjectName) this.observedObjects.get(i);
        }
        return objectNameArr;
    }

    @Override // javax.management.monitor.MonitorMBean
    public synchronized String getObservedAttribute() {
        return this.observedAttribute;
    }

    @Override // javax.management.monitor.MonitorMBean
    public synchronized void setObservedAttribute(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("The attribute to observe cannot be null.");
        }
        this.observedAttribute = str;
        for (int i = 0; i < this.elementCount; i++) {
            resetAlreadyNotified(i, 6);
        }
    }

    @Override // javax.management.monitor.MonitorMBean
    public synchronized long getGranularityPeriod() {
        return this.granularityPeriod;
    }

    public synchronized void setGranularityPeriod(long j) throws IllegalArgumentException {
        if (j <= 0) {
            throw new IllegalArgumentException("The granularity period must be greater than zero.");
        }
        this.granularityPeriod = j;
    }

    @Override // javax.management.monitor.MonitorMBean
    public synchronized boolean isActive() {
        return this.isActive;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ObjectName getObservedObject(int i) throws ArrayIndexOutOfBoundsException {
        return (ObjectName) this.observedObjects.get(i);
    }

    void updateDeprecatedAlreadyNotified() {
        if (this.elementCount > 0) {
            this.alreadyNotified = this.alreadyNotifieds[0];
        } else {
            this.alreadyNotified = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAlreadyNotified(int i, int i2) {
        int[] iArr = this.alreadyNotifieds;
        iArr[i] = iArr[i] | i2;
        if (i == 0) {
            updateDeprecatedAlreadyNotified();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetAlreadyNotified(int i, int i2) {
        int[] iArr = this.alreadyNotifieds;
        iArr[i] = iArr[i] & (i2 ^ (-1));
        if (i == 0) {
            updateDeprecatedAlreadyNotified();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetAllAlreadyNotified(int i) {
        this.alreadyNotifieds[i] = 0;
        if (i == 0) {
            updateDeprecatedAlreadyNotified();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void insertintElementAt(int[] iArr, int i, int i2) {
        ensureintCapacity(iArr, this.elementCount + 1);
        System.arraycopy(iArr, i2, iArr, i2 + 1, this.elementCount - i2);
        iArr[i2] = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void insertlongElementAt(long[] jArr, long j, int i) {
        ensurelongCapacity(jArr, this.elementCount + 1);
        System.arraycopy(jArr, i, jArr, i + 1, this.elementCount - i);
        jArr[i] = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void insertbooleanElementAt(boolean[] zArr, boolean z, int i) {
        ensurebooleanCapacity(zArr, this.elementCount + 1);
        System.arraycopy(zArr, i, zArr, i + 1, this.elementCount - i);
        zArr[i] = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void insertNumberElementAt(Number[] numberArr, Number number, int i) {
        ensureNumberCapacity(numberArr, this.elementCount + 1);
        System.arraycopy(numberArr, i, numberArr, i + 1, this.elementCount - i);
        numberArr[i] = number;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void insertStringElementAt(String[] strArr, String str, int i) {
        ensureStringCapacity(strArr, this.elementCount + 1);
        System.arraycopy(strArr, i, strArr, i + 1, this.elementCount - i);
        strArr[i] = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeintElementAt(int[] iArr, int i) {
        int i2;
        if (i >= 0 && i < this.elementCount && (i2 = (this.elementCount - i) - 1) > 0) {
            System.arraycopy(iArr, i + 1, iArr, i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removelongElementAt(long[] jArr, int i) {
        int i2;
        if (i >= 0 && i < this.elementCount && (i2 = (this.elementCount - i) - 1) > 0) {
            System.arraycopy(jArr, i + 1, jArr, i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removebooleanElementAt(boolean[] zArr, int i) {
        int i2;
        if (i >= 0 && i < this.elementCount && (i2 = (this.elementCount - i) - 1) > 0) {
            System.arraycopy(zArr, i + 1, zArr, i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeNumberElementAt(Number[] numberArr, int i) {
        int i2;
        if (i >= 0 && i < this.elementCount && (i2 = (this.elementCount - i) - 1) > 0) {
            System.arraycopy(numberArr, i + 1, numberArr, i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeStringElementAt(String[] strArr, int i) {
        int i2;
        if (i >= 0 && i < this.elementCount && (i2 = (this.elementCount - i) - 1) > 0) {
            System.arraycopy(strArr, i + 1, strArr, i, i2);
        }
    }

    void ensureintCapacity(int[] iArr, int i) {
        int length = iArr.length;
        if (i > length) {
            int i2 = length + 16;
            if (i2 < i) {
                i2 = i;
            }
            System.arraycopy(iArr, 0, new int[i2], 0, this.elementCount);
        }
    }

    void ensurelongCapacity(long[] jArr, int i) {
        int length = jArr.length;
        if (i > length) {
            int i2 = length + 16;
            if (i2 < i) {
                i2 = i;
            }
            System.arraycopy(jArr, 0, new long[i2], 0, this.elementCount);
        }
    }

    void ensurebooleanCapacity(boolean[] zArr, int i) {
        int length = zArr.length;
        if (i > length) {
            int i2 = length + 16;
            if (i2 < i) {
                i2 = i;
            }
            System.arraycopy(zArr, 0, new boolean[i2], 0, this.elementCount);
        }
    }

    void ensureNumberCapacity(Number[] numberArr, int i) {
        int length = numberArr.length;
        if (i > length) {
            int i2 = length + 16;
            if (i2 < i) {
                i2 = i;
            }
            System.arraycopy(numberArr, 0, new Number[i2], 0, this.elementCount);
        }
    }

    void ensureStringCapacity(String[] strArr, int i) {
        int length = strArr.length;
        if (i > length) {
            int i2 = length + 16;
            if (i2 < i) {
                i2 = i;
            }
            System.arraycopy(strArr, 0, new String[i2], 0, this.elementCount);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int indexOf(ObjectName objectName) {
        return this.observedObjects.indexOf(objectName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendNotification(String str, long j, String str2, Object obj, Object obj2, int i) {
        MonitorNotification monitorNotification;
        synchronized (this) {
            this.sequenceNumber++;
            if (isTraceOn()) {
                trace("sendNotification", new StringBuffer().append("send notification:\n\tNotification observed object = ").append(getObservedObject(i)).append("\n\tNotification observed attribute = ").append(this.observedAttribute).append("\n\tNotification derived gauge = ").append(obj).toString());
            }
            monitorNotification = new MonitorNotification(str, this, this.sequenceNumber, j, str2, getObservedObject(i), this.observedAttribute, obj, obj2);
        }
        sendNotification(monitorNotification);
    }
}
