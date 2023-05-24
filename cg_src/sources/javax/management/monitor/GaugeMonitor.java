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
import soot.JavaBasicTypes;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/monitor/GaugeMonitor.class */
public class GaugeMonitor extends Monitor implements GaugeMonitorMBean {
    private static final int RISING = 0;
    private static final int FALLING = 1;
    private static final int RISING_OR_FALLING = 2;
    private static final int INTEGER = 0;
    private static final int BYTE = 1;
    private static final int SHORT = 2;
    private static final int LONG = 3;
    private static final int FLOAT = 4;
    private static final int DOUBLE = 5;
    private static final int THRESHOLD_ERROR_NOTIFIED = 16;
    private Number highThreshold = new Integer(0);
    private Number lowThreshold = new Integer(0);
    private boolean notifyHigh = false;
    private boolean notifyLow = false;
    private boolean differenceMode = false;
    private transient Number[] derivedGauge = new Number[16];
    private transient long[] derivedGaugeTimestamp = new long[16];
    private transient Number[] previousScanGauge = new Number[16];
    private transient int[] status = new int[16];
    private transient int[] type = new int[16];
    private transient Timer timer = null;

    String makeDebugTag() {
        return "GaugeMonitor";
    }

    public GaugeMonitor() {
        this.dbgTag = makeDebugTag();
    }

    @Override // javax.management.monitor.Monitor, javax.management.monitor.MonitorMBean
    public synchronized void start() {
        if (isTraceOn()) {
            trace("start", "start the gauge monitor");
        }
        if (!isActive()) {
            this.isActive = true;
            for (int i = 0; i < this.elementCount; i++) {
                this.status[i] = 2;
                this.previousScanGauge[i] = null;
            }
            this.timer = new Timer();
            this.timer.schedule(new GaugeAlarmClock(this), getGranularityPeriod(), getGranularityPeriod());
        } else if (isTraceOn()) {
            trace("start", "the gauge monitor is already activated");
        }
    }

    @Override // javax.management.monitor.Monitor, javax.management.monitor.MonitorMBean
    public void stop() {
        if (isTraceOn()) {
            trace("stop", "stop the gauge monitor");
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
            this.timer.schedule(new GaugeAlarmClock(this), getGranularityPeriod(), getGranularityPeriod());
        }
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public Number getDerivedGauge(ObjectName objectName) {
        int indexOf = indexOf(objectName);
        if (indexOf != -1) {
            return this.derivedGauge[indexOf];
        }
        return null;
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public long getDerivedGaugeTimeStamp(ObjectName objectName) {
        int indexOf = indexOf(objectName);
        if (indexOf != -1) {
            return this.derivedGaugeTimestamp[indexOf];
        }
        return 0L;
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized Number getDerivedGauge() {
        return this.derivedGauge[0];
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized long getDerivedGaugeTimeStamp() {
        return this.derivedGaugeTimestamp[0];
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized Number getHighThreshold() {
        return this.highThreshold;
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized Number getLowThreshold() {
        return this.lowThreshold;
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized void setThresholds(Number number, Number number2) throws IllegalArgumentException {
        if (number == null || number2 == null) {
            throw new IllegalArgumentException("The threshold values cannot be null.");
        }
        if (number.getClass() != number2.getClass()) {
            throw new IllegalArgumentException("The high and the low thresholds must be of the same type.");
        }
        if (isFirstStrictlyGreaterThanLast(number2, number, number.getClass().getName())) {
            throw new IllegalArgumentException("The threshold high value must be greater than or equal to threshold low value.");
        }
        this.highThreshold = number;
        this.lowThreshold = number2;
        for (int i = 0; i < this.elementCount; i++) {
            resetAlreadyNotified(i, 16);
            this.status[i] = 2;
        }
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized boolean getNotifyHigh() {
        return this.notifyHigh;
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized void setNotifyHigh(boolean z) {
        this.notifyHigh = z;
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized boolean getNotifyLow() {
        return this.notifyLow;
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized void setNotifyLow(boolean z) {
        this.notifyLow = z;
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized boolean getDifferenceMode() {
        return this.differenceMode;
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized void setDifferenceMode(boolean z) {
        this.differenceMode = z;
        for (int i = 0; i < this.elementCount; i++) {
            this.status[i] = 2;
            this.previousScanGauge[i] = null;
        }
    }

    @Override // javax.management.NotificationBroadcasterSupport, javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        return new MBeanNotificationInfo[]{new MBeanNotificationInfo(new String[]{MonitorNotification.RUNTIME_ERROR, MonitorNotification.OBSERVED_OBJECT_ERROR, MonitorNotification.OBSERVED_ATTRIBUTE_ERROR, MonitorNotification.OBSERVED_ATTRIBUTE_TYPE_ERROR, MonitorNotification.THRESHOLD_ERROR, MonitorNotification.THRESHOLD_HIGH_VALUE_EXCEEDED, MonitorNotification.THRESHOLD_LOW_VALUE_EXCEEDED}, "javax.management.monitor.MonitorNotification", "Notifications sent by the GaugeMonitor MBean")};
    }

    private boolean updateDerivedGauge(Object obj, int i) {
        boolean z;
        this.derivedGaugeTimestamp[i] = System.currentTimeMillis();
        if (this.differenceMode) {
            if (this.previousScanGauge[i] != null) {
                setDerivedGaugeWithDifference((Number) obj, i);
                z = true;
            } else {
                z = false;
            }
            this.previousScanGauge[i] = (Number) obj;
        } else {
            this.derivedGauge[i] = (Number) obj;
            z = true;
        }
        return z;
    }

    private void updateNotifications(int i) {
        if (this.status[i] == 2) {
            if (isFirstGreaterThanLast(this.derivedGauge[i], this.highThreshold, this.type[i])) {
                if (this.notifyHigh) {
                    sendNotification(MonitorNotification.THRESHOLD_HIGH_VALUE_EXCEEDED, this.derivedGaugeTimestamp[i], "", this.derivedGauge[i], this.highThreshold, i);
                }
                this.status[i] = 1;
            }
            if (isFirstGreaterThanLast(this.lowThreshold, this.derivedGauge[i], this.type[i])) {
                if (this.notifyLow) {
                    sendNotification(MonitorNotification.THRESHOLD_LOW_VALUE_EXCEEDED, this.derivedGaugeTimestamp[i], "", this.derivedGauge[i], this.lowThreshold, i);
                }
                this.status[i] = 0;
            }
        } else if (this.status[i] == 0) {
            if (isFirstGreaterThanLast(this.derivedGauge[i], this.highThreshold, this.type[i])) {
                if (this.notifyHigh) {
                    sendNotification(MonitorNotification.THRESHOLD_HIGH_VALUE_EXCEEDED, this.derivedGaugeTimestamp[i], "", this.derivedGauge[i], this.highThreshold, i);
                }
                this.status[i] = 1;
            }
        } else if (this.status[i] == 1 && isFirstGreaterThanLast(this.lowThreshold, this.derivedGauge[i], this.type[i])) {
            if (this.notifyLow) {
                sendNotification(MonitorNotification.THRESHOLD_LOW_VALUE_EXCEEDED, this.derivedGaugeTimestamp[i], "", this.derivedGauge[i], this.lowThreshold, i);
            }
            this.status[i] = 0;
        }
    }

    private boolean isThresholdTypeValid(int i) {
        boolean z = false;
        switch (this.type[i]) {
            case 0:
                if ((this.highThreshold instanceof Integer) && (this.lowThreshold instanceof Integer)) {
                    z = true;
                    break;
                }
                break;
            case 1:
                if ((this.highThreshold instanceof Byte) && (this.lowThreshold instanceof Byte)) {
                    z = true;
                    break;
                }
                break;
            case 2:
                if ((this.highThreshold instanceof Short) && (this.lowThreshold instanceof Short)) {
                    z = true;
                    break;
                }
                break;
            case 3:
                if ((this.highThreshold instanceof Long) && (this.lowThreshold instanceof Long)) {
                    z = true;
                    break;
                }
                break;
            case 4:
                if ((this.highThreshold instanceof Float) && (this.lowThreshold instanceof Float)) {
                    z = true;
                    break;
                }
                break;
            case 5:
                if ((this.highThreshold instanceof Double) && (this.lowThreshold instanceof Double)) {
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

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private void setDerivedGaugeWithDifference(Number number, int i) {
        switch (this.type[i]) {
            case 0:
                this.derivedGauge[i] = new Integer(((Integer) number).intValue() - ((Integer) this.previousScanGauge[i]).intValue());
                return;
            case 1:
                this.derivedGauge[i] = new Byte((byte) (((Byte) number).byteValue() - ((Byte) this.previousScanGauge[i]).byteValue()));
                return;
            case 2:
                this.derivedGauge[i] = new Short((short) (((Short) number).shortValue() - ((Short) this.previousScanGauge[i]).shortValue()));
                return;
            case 3:
                this.derivedGauge[i] = new Long(((Long) number).longValue() - ((Long) this.previousScanGauge[i]).longValue());
                return;
            case 4:
                this.derivedGauge[i] = new Float(((Float) number).floatValue() - ((Float) this.previousScanGauge[i]).floatValue());
                return;
            case 5:
                this.derivedGauge[i] = new Double(((Double) number).doubleValue() - ((Double) this.previousScanGauge[i]).doubleValue());
                break;
        }
        if (isDebugOn()) {
            debug("setDerivedGaugeWithDifference", "the threshold type is invalid");
        }
    }

    private boolean isFirstGreaterThanLast(Number number, Number number2, int i) {
        boolean z = false;
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
                if (number.longValue() >= number2.longValue()) {
                    z = true;
                    break;
                }
                break;
            case 4:
            case 5:
                if (number.doubleValue() >= number2.doubleValue()) {
                    z = true;
                    break;
                }
                break;
            default:
                if (isDebugOn()) {
                    debug("isFirstGreaterThanLast", "the threshold type is invalid");
                    break;
                }
                break;
        }
        return z;
    }

    private boolean isFirstStrictlyGreaterThanLast(Number number, Number number2, String str) {
        boolean z = false;
        if (str.equals(JavaBasicTypes.JAVA_LANG_INTEGER) || str.equals(JavaBasicTypes.JAVA_LANG_BYTE) || str.equals(JavaBasicTypes.JAVA_LANG_SHORT) || str.equals(JavaBasicTypes.JAVA_LANG_LONG)) {
            if (number.longValue() > number2.longValue()) {
                z = true;
            }
        } else if (str.equals(JavaBasicTypes.JAVA_LANG_FLOAT) || str.equals(JavaBasicTypes.JAVA_LANG_DOUBLE)) {
            if (number.doubleValue() > number2.doubleValue()) {
                z = true;
            }
        } else if (isDebugOn()) {
            debug("isFirstStrictlyGreaterThanLast", "the threshold type is invalid");
        }
        return z;
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
                                } else if (attribute instanceof Float) {
                                    this.type[i] = 4;
                                } else if (attribute instanceof Double) {
                                    this.type[i] = 5;
                                } else if ((this.alreadyNotifieds[i] & 4) != 0) {
                                    return;
                                } else {
                                    String str = MonitorNotification.OBSERVED_ATTRIBUTE_TYPE_ERROR;
                                    setAlreadyNotified(i, 4);
                                    throw new MonitorSettingException("The observed attribute type must be an integer type or a floating-point type.");
                                }
                                if (!isThresholdTypeValid(i)) {
                                    if ((this.alreadyNotifieds[i] & 16) != 0) {
                                        return;
                                    }
                                    String str2 = MonitorNotification.THRESHOLD_ERROR;
                                    setAlreadyNotified(i, 16);
                                    throw new MonitorSettingException("The threshold high and threshold low must be of the same type as the gauge.");
                                }
                                resetAllAlreadyNotified(i);
                                if (updateDerivedGauge(attribute, i)) {
                                    updateNotifications(i);
                                }
                            } catch (InstanceNotFoundException e) {
                                if ((this.alreadyNotifieds[i] & 1) != 0) {
                                    return;
                                }
                                String str3 = MonitorNotification.OBSERVED_OBJECT_ERROR;
                                setAlreadyNotified(i, 1);
                                throw new MonitorSettingException("The observed object must be registered in the MBean server.");
                            }
                        } catch (NullPointerException e2) {
                            if ((this.alreadyNotifieds[i] & 8) != 0) {
                                return;
                            }
                            String str4 = MonitorNotification.RUNTIME_ERROR;
                            setAlreadyNotified(i, 8);
                            throw new MonitorSettingException("The gauge monitor must be registered in the MBean server.");
                        }
                    } catch (AttributeNotFoundException e3) {
                        if ((this.alreadyNotifieds[i] & 2) != 0) {
                            return;
                        }
                        String str5 = MonitorNotification.OBSERVED_ATTRIBUTE_ERROR;
                        setAlreadyNotified(i, 2);
                        throw new MonitorSettingException("The observed attribute must be accessible in the observed object.");
                    }
                } catch (MBeanException e4) {
                    if ((this.alreadyNotifieds[i] & 8) != 0) {
                        return;
                    }
                    String str6 = MonitorNotification.RUNTIME_ERROR;
                    setAlreadyNotified(i, 8);
                    throw new MonitorSettingException(e4.getMessage());
                } catch (ReflectionException e5) {
                    if ((this.alreadyNotifieds[i] & 2) != 0) {
                        return;
                    }
                    String str7 = MonitorNotification.OBSERVED_ATTRIBUTE_ERROR;
                    setAlreadyNotified(i, 2);
                    throw new MonitorSettingException(e5.getMessage());
                }
            }
        } catch (MonitorSettingException e6) {
            sendNotification(null, this.derivedGaugeTimestamp[i], e6.getMessage(), this.derivedGauge[i], null, i);
            this.status[i] = 2;
            this.previousScanGauge[i] = null;
        }
    }

    @Override // javax.management.monitor.Monitor
    void insertSpecificElementAt(int i) {
        insertNumberElementAt(this.derivedGauge, new Integer(0), i);
        insertNumberElementAt(this.previousScanGauge, null, i);
        insertlongElementAt(this.derivedGaugeTimestamp, new Date().getTime(), i);
        insertintElementAt(this.status, 2, i);
        insertintElementAt(this.type, 0, i);
    }

    @Override // javax.management.monitor.Monitor
    void removeSpecificElementAt(int i) {
        removeNumberElementAt(this.derivedGauge, i);
        removeNumberElementAt(this.previousScanGauge, i);
        removelongElementAt(this.derivedGaugeTimestamp, i);
        removeintElementAt(this.status, i);
        removeintElementAt(this.type, i);
    }

    /* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/monitor/GaugeMonitor$GaugeAlarmClock.class */
    private static class GaugeAlarmClock extends TimerTask {
        GaugeMonitor listener;

        public GaugeAlarmClock(GaugeMonitor gaugeMonitor) {
            this.listener = null;
            this.listener = gaugeMonitor;
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
