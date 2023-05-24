package javax.management.monitor;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanNotificationInfo;
import javax.management.ObjectName;
import javax.management.ReflectionException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/monitor/CounterMonitor.class */
public class CounterMonitor extends Monitor implements CounterMonitorMBean {
    private static final int INTEGER = 0;
    private static final int BYTE = 1;
    private static final int SHORT = 2;
    private static final int LONG = 3;
    private static final int THRESHOLD_ERROR_NOTIFIED = 16;
    private Number[] threshold = new Number[16];
    private Number modulus = new Integer(0);
    private Number offset = new Integer(0);
    private boolean notify = false;
    private boolean differenceMode = false;
    private transient Number initThreshold = new Integer(0);
    private transient Number[] derivedGauge = new Number[16];
    private transient long[] derivedGaugeTimestamp = new long[16];
    private transient Number[] previousScanCounter = new Number[16];
    private transient boolean[] modulusExceeded = new boolean[16];
    private transient Number[] derivedGaugeExceeded = new Number[16];
    private transient boolean[] eventAlreadyNotified = new boolean[16];
    private transient int[] type = new int[16];
    private transient Timer timer = null;

    String makeDebugTag() {
        return "CounterMonitor";
    }

    public CounterMonitor() {
        this.dbgTag = makeDebugTag();
    }

    @Override // javax.management.monitor.Monitor, javax.management.MBeanRegistration
    public void preDeregister() throws Exception {
        super.preDeregister();
        if (isTraceOn()) {
            trace("preDeregister", "reset the threshold values");
        }
        for (int i = 0; i < this.elementCount; i++) {
            this.threshold[i] = this.initThreshold;
        }
    }

    @Override // javax.management.monitor.Monitor, javax.management.monitor.MonitorMBean
    public synchronized void start() {
        if (isTraceOn()) {
            trace("start", "start the counter monitor");
        }
        if (!isActive()) {
            this.isActive = true;
            for (int i = 0; i < this.elementCount; i++) {
                this.threshold[i] = this.initThreshold;
                this.modulusExceeded[i] = false;
                this.eventAlreadyNotified[i] = false;
                this.previousScanCounter[i] = null;
            }
            this.timer = new Timer();
            this.timer.schedule(new CounterAlarmClock(this), getGranularityPeriod(), getGranularityPeriod());
        } else if (isTraceOn()) {
            trace("start", "the counter monitor is already activated");
        }
    }

    @Override // javax.management.monitor.Monitor, javax.management.monitor.MonitorMBean
    public void stop() {
        if (isTraceOn()) {
            trace("stop", "stop the counter monitor");
        }
        if (isActive()) {
            if (this.timer != null) {
                this.timer.cancel();
                this.timer = null;
            }
            this.isActive = false;
        } else if (isTraceOn()) {
            trace("stop", "the counter monitor is already deactivated");
        }
    }

    @Override // javax.management.monitor.Monitor, javax.management.monitor.MonitorMBean
    public synchronized void setGranularityPeriod(long j) throws IllegalArgumentException {
        super.setGranularityPeriod(j);
        if (isActive()) {
            this.timer.cancel();
            this.timer = new Timer();
            this.timer.schedule(new CounterAlarmClock(this), getGranularityPeriod(), getGranularityPeriod());
        }
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public Number getDerivedGauge(ObjectName objectName) {
        int indexOf = indexOf(objectName);
        if (indexOf != -1) {
            return this.derivedGauge[indexOf];
        }
        return null;
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public long getDerivedGaugeTimeStamp(ObjectName objectName) {
        int indexOf = indexOf(objectName);
        if (indexOf != -1) {
            return this.derivedGaugeTimestamp[indexOf];
        }
        return 0L;
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public Number getThreshold(ObjectName objectName) {
        int indexOf = indexOf(objectName);
        if (indexOf != -1) {
            return this.threshold[indexOf];
        }
        return null;
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public Number getInitThreshold() {
        return this.initThreshold;
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public void setInitThreshold(Number number) throws IllegalArgumentException {
        if (number == null) {
            throw new IllegalArgumentException("The threshold cannot be null.");
        }
        if (number.longValue() < 0) {
            throw new IllegalArgumentException("The threshold must be greater than or equal to zero.");
        }
        this.initThreshold = number;
        for (int i = 0; i < this.elementCount; i++) {
            this.threshold[i] = number;
            resetAlreadyNotified(i, 16);
            this.modulusExceeded[i] = false;
            this.eventAlreadyNotified[i] = false;
        }
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized Number getDerivedGauge() {
        return this.derivedGauge[0];
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized long getDerivedGaugeTimeStamp() {
        return this.derivedGaugeTimestamp[0];
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized Number getThreshold() {
        return this.threshold[0];
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized void setThreshold(Number number) throws IllegalArgumentException {
        setInitThreshold(number);
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized Number getOffset() {
        return this.offset;
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized void setOffset(Number number) throws IllegalArgumentException {
        if (number == null) {
            throw new IllegalArgumentException("The offset cannot be null.");
        }
        if (number.longValue() < 0) {
            throw new IllegalArgumentException("The offset must be greater than or equal to zero.");
        }
        this.offset = number;
        for (int i = 0; i < this.elementCount; i++) {
            resetAlreadyNotified(i, 16);
        }
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized Number getModulus() {
        return this.modulus;
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized void setModulus(Number number) throws IllegalArgumentException {
        if (number == null) {
            throw new IllegalArgumentException("The modulus cannot be null.");
        }
        if (number.longValue() < 0) {
            throw new IllegalArgumentException("The modulus must be greater than or equal to zero.");
        }
        this.modulus = number;
        for (int i = 0; i < this.elementCount; i++) {
            resetAlreadyNotified(i, 16);
            this.modulusExceeded[i] = false;
        }
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized boolean getNotify() {
        return this.notify;
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized void setNotify(boolean z) {
        this.notify = z;
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized boolean getDifferenceMode() {
        return this.differenceMode;
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized void setDifferenceMode(boolean z) {
        this.differenceMode = z;
        for (int i = 0; i < this.elementCount; i++) {
            this.threshold[i] = this.initThreshold;
            this.modulusExceeded[i] = false;
            this.eventAlreadyNotified[i] = false;
            this.previousScanCounter[i] = null;
        }
    }

    @Override // javax.management.NotificationBroadcasterSupport, javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        return new MBeanNotificationInfo[]{new MBeanNotificationInfo(new String[]{MonitorNotification.RUNTIME_ERROR, MonitorNotification.OBSERVED_OBJECT_ERROR, MonitorNotification.OBSERVED_ATTRIBUTE_ERROR, MonitorNotification.OBSERVED_ATTRIBUTE_TYPE_ERROR, MonitorNotification.THRESHOLD_ERROR, MonitorNotification.THRESHOLD_VALUE_EXCEEDED}, "javax.management.monitor.MonitorNotification", "Notifications sent by the CounterMonitor MBean")};
    }

    private boolean updateDerivedGauge(Object obj, int i) {
        boolean z;
        this.derivedGaugeTimestamp[i] = System.currentTimeMillis();
        if (this.differenceMode) {
            if (this.previousScanCounter[i] != null) {
                setDerivedGaugeWithDifference((Number) obj, null, i);
                if (this.derivedGauge[i].longValue() < 0) {
                    if (this.modulus.longValue() > 0) {
                        setDerivedGaugeWithDifference((Number) obj, this.modulus, i);
                    }
                    this.threshold[i] = this.initThreshold;
                    this.eventAlreadyNotified[i] = false;
                }
                z = true;
            } else {
                z = false;
            }
            this.previousScanCounter[i] = (Number) obj;
        } else {
            this.derivedGauge[i] = (Number) obj;
            z = true;
        }
        return z;
    }

    private void updateNotifications(int i) {
        if (!this.eventAlreadyNotified[i]) {
            if (this.derivedGauge[i].longValue() >= this.threshold[i].longValue()) {
                if (this.notify) {
                    sendNotification(MonitorNotification.THRESHOLD_VALUE_EXCEEDED, this.derivedGaugeTimestamp[i], "", this.derivedGauge[i], this.threshold[i], i);
                }
                this.eventAlreadyNotified[i] = true;
            }
        } else if (isTraceOn()) {
            trace("updateNotifications", new StringBuffer().append("the notification:\n\tNotification observed object = ").append(getObservedObject(i)).append("\n\tNotification observed attribute = ").append(getObservedAttribute()).append("\n\tNotification derived gauge = ").append(this.derivedGauge[i]).append("has already been sent").toString());
        }
    }

    private void updateThreshold(int i) {
        long j;
        if (this.derivedGauge[i].longValue() >= this.threshold[i].longValue()) {
            if (this.offset.longValue() > 0) {
                long longValue = this.threshold[i].longValue();
                while (true) {
                    j = longValue;
                    if (this.derivedGauge[i].longValue() < j) {
                        break;
                    }
                    longValue = j + this.offset.longValue();
                }
                switch (this.type[i]) {
                    case 0:
                        this.threshold[i] = new Integer((int) j);
                        break;
                    case 1:
                        this.threshold[i] = new Byte((byte) j);
                        break;
                    case 2:
                        this.threshold[i] = new Short((short) j);
                        break;
                    case 3:
                        this.threshold[i] = new Long(j);
                        break;
                    default:
                        if (isDebugOn()) {
                            debug("updateThreshold", "the threshold type is invalid");
                            break;
                        }
                        break;
                }
                if (!this.differenceMode && this.modulus.longValue() > 0 && this.threshold[i].longValue() > this.modulus.longValue()) {
                    this.modulusExceeded[i] = true;
                    this.derivedGaugeExceeded[i] = this.derivedGauge[i];
                }
                this.eventAlreadyNotified[i] = false;
                return;
            }
            this.modulusExceeded[i] = true;
            this.derivedGaugeExceeded[i] = this.derivedGauge[i];
        }
    }

    private boolean isThresholdTypeValid(int i) {
        boolean z = false;
        switch (this.type[i]) {
            case 0:
                if ((this.threshold[i] instanceof Integer) && ((this.offset.equals(new Integer(0)) || (this.offset instanceof Integer)) && (this.modulus.equals(new Integer(0)) || (this.modulus instanceof Integer)))) {
                    z = true;
                    break;
                }
                break;
            case 1:
                if ((this.threshold[i] instanceof Byte) && ((this.offset.equals(new Integer(0)) || (this.offset instanceof Byte)) && (this.modulus.equals(new Integer(0)) || (this.modulus instanceof Byte)))) {
                    z = true;
                    break;
                }
                break;
            case 2:
                if ((this.threshold[i] instanceof Short) && ((this.offset.equals(new Integer(0)) || (this.offset instanceof Short)) && (this.modulus.equals(new Integer(0)) || (this.modulus instanceof Short)))) {
                    z = true;
                    break;
                }
                break;
            case 3:
                if ((this.threshold[i] instanceof Long) && ((this.offset.equals(new Integer(0)) || (this.offset instanceof Long)) && (this.modulus.equals(new Integer(0)) || (this.modulus instanceof Long)))) {
                    z = true;
                    break;
                }
                break;
            default:
                if (isDebugOn()) {
                    debug("isThresholdTypeValid", "the threshold type is invalid");
                    break;
                }
                break;
        }
        return z;
    }

    private void setDerivedGaugeWithDifference(Number number, Number number2, int i) {
        switch (this.type[i]) {
            case 0:
                if (number2 == null) {
                    this.derivedGauge[i] = new Integer(((Integer) number).intValue() - ((Integer) this.previousScanCounter[i]).intValue());
                    return;
                } else {
                    this.derivedGauge[i] = new Integer((((Integer) number).intValue() - ((Integer) this.previousScanCounter[i]).intValue()) + ((Integer) this.modulus).intValue());
                    return;
                }
            case 1:
                if (number2 == null) {
                    this.derivedGauge[i] = new Byte((byte) (((Byte) number).byteValue() - ((Byte) this.previousScanCounter[i]).byteValue()));
                    return;
                } else {
                    this.derivedGauge[i] = new Byte((byte) ((((Byte) number).byteValue() - ((Byte) this.previousScanCounter[i]).byteValue()) + ((Byte) this.modulus).byteValue()));
                    return;
                }
            case 2:
                if (number2 == null) {
                    this.derivedGauge[i] = new Short((short) (((Short) number).shortValue() - ((Short) this.previousScanCounter[i]).shortValue()));
                    return;
                } else {
                    this.derivedGauge[i] = new Short((short) ((((Short) number).shortValue() - ((Short) this.previousScanCounter[i]).shortValue()) + ((Short) this.modulus).shortValue()));
                    return;
                }
            case 3:
                if (number2 == null) {
                    this.derivedGauge[i] = new Long(((Long) number).longValue() - ((Long) this.previousScanCounter[i]).longValue());
                    return;
                } else {
                    this.derivedGauge[i] = new Long((((Long) number).longValue() - ((Long) this.previousScanCounter[i]).longValue()) + ((Long) this.modulus).longValue());
                    return;
                }
            default:
                if (isDebugOn()) {
                    debug("setDerivedGaugeWithDifference", "the threshold type is invalid");
                    return;
                }
                return;
        }
    }

    void notifyAlarmClock(int i) {
        try {
            if (isActive()) {
                if (getObservedObject(i) == null || getObservedAttribute() == null) {
                    return;
                }
                try {
                    try {
                        try {
                            Object attribute = this.server.getAttribute(getObservedObject(i), getObservedAttribute());
                            if (attribute == null) {
                                return;
                            }
                            if (attribute instanceof Integer) {
                                this.type[i] = 0;
                            } else if (attribute instanceof Byte) {
                                this.type[i] = 1;
                            } else if (attribute instanceof Short) {
                                this.type[i] = 2;
                            } else if (attribute instanceof Long) {
                                this.type[i] = 3;
                            } else if ((this.alreadyNotifieds[i] & 4) != 0) {
                                return;
                            } else {
                                String str = MonitorNotification.OBSERVED_ATTRIBUTE_TYPE_ERROR;
                                setAlreadyNotified(i, 4);
                                throw new MonitorSettingException("The observed attribute type must be an integer type.");
                            }
                            if (!isThresholdTypeValid(i)) {
                                if ((this.alreadyNotifieds[i] & 16) != 0) {
                                    return;
                                }
                                String str2 = MonitorNotification.THRESHOLD_ERROR;
                                setAlreadyNotified(i, 16);
                                throw new MonitorSettingException("The threshold, offset and modulus must be of the same type as the counter.");
                            }
                            resetAllAlreadyNotified(i);
                            if (this.modulusExceeded[i] && this.derivedGauge[i].longValue() < this.derivedGaugeExceeded[i].longValue()) {
                                this.threshold[i] = this.initThreshold;
                                this.modulusExceeded[i] = false;
                                this.eventAlreadyNotified[i] = false;
                            }
                            if (updateDerivedGauge(attribute, i)) {
                                updateNotifications(i);
                                updateThreshold(i);
                            }
                        } catch (AttributeNotFoundException e) {
                            if ((this.alreadyNotifieds[i] & 2) != 0) {
                                return;
                            }
                            String str3 = MonitorNotification.OBSERVED_ATTRIBUTE_ERROR;
                            setAlreadyNotified(i, 2);
                            throw new MonitorSettingException("The observed attribute must be accessible in the observed object.");
                        }
                    } catch (MBeanException e2) {
                        if ((this.alreadyNotifieds[i] & 8) != 0) {
                            return;
                        }
                        String str4 = MonitorNotification.RUNTIME_ERROR;
                        setAlreadyNotified(i, 8);
                        throw new MonitorSettingException(e2.getMessage());
                    } catch (ReflectionException e3) {
                        if ((this.alreadyNotifieds[i] & 2) != 0) {
                            return;
                        }
                        String str5 = MonitorNotification.OBSERVED_ATTRIBUTE_ERROR;
                        setAlreadyNotified(i, 2);
                        throw new MonitorSettingException(e3.getMessage());
                    }
                } catch (NullPointerException e4) {
                    if ((this.alreadyNotifieds[i] & 8) != 0) {
                        return;
                    }
                    String str6 = MonitorNotification.RUNTIME_ERROR;
                    setAlreadyNotified(i, 8);
                    throw new MonitorSettingException("The counter monitor must be registered in the MBean server.");
                } catch (InstanceNotFoundException e5) {
                    if ((this.alreadyNotifieds[i] & 1) != 0) {
                        return;
                    }
                    String str7 = MonitorNotification.OBSERVED_OBJECT_ERROR;
                    setAlreadyNotified(i, 1);
                    throw new MonitorSettingException("The observed object must be registered in the MBean server.");
                }
            }
        } catch (MonitorSettingException e6) {
            sendNotification(null, this.derivedGaugeTimestamp[i], e6.getMessage(), this.derivedGauge[i], null, i);
            this.modulusExceeded[i] = false;
            this.eventAlreadyNotified[i] = false;
            this.previousScanCounter[i] = null;
        }
    }

    @Override // javax.management.monitor.Monitor
    void insertSpecificElementAt(int i) {
        Integer num = new Integer(0);
        insertNumberElementAt(this.threshold, num, i);
        insertNumberElementAt(this.derivedGauge, num, i);
        insertNumberElementAt(this.previousScanCounter, null, i);
        insertNumberElementAt(this.derivedGaugeExceeded, null, i);
        insertlongElementAt(this.derivedGaugeTimestamp, new Date().getTime(), i);
        insertbooleanElementAt(this.modulusExceeded, false, i);
        insertbooleanElementAt(this.eventAlreadyNotified, false, i);
        insertintElementAt(this.type, 0, i);
    }

    @Override // javax.management.monitor.Monitor
    void removeSpecificElementAt(int i) {
        removeNumberElementAt(this.threshold, i);
        removeNumberElementAt(this.derivedGauge, i);
        removeNumberElementAt(this.previousScanCounter, i);
        removeNumberElementAt(this.derivedGaugeExceeded, i);
        removelongElementAt(this.derivedGaugeTimestamp, i);
        removebooleanElementAt(this.modulusExceeded, i);
        removebooleanElementAt(this.eventAlreadyNotified, i);
        removeintElementAt(this.type, i);
    }

    /* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/monitor/CounterMonitor$CounterAlarmClock.class */
    private static class CounterAlarmClock extends TimerTask {
        CounterMonitor listener;

        public CounterAlarmClock(CounterMonitor counterMonitor) {
            this.listener = null;
            this.listener = counterMonitor;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (this.listener.isActive()) {
                for (int i = 0; i < this.listener.elementCount; i++) {
                    this.listener.notifyAlarmClock(i);
                }
            }
        }
    }
}
