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
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/monitor/StringMonitor.class */
public class StringMonitor extends Monitor implements StringMonitorMBean {
    private static final String[] types = {MonitorNotification.RUNTIME_ERROR, MonitorNotification.OBSERVED_OBJECT_ERROR, MonitorNotification.OBSERVED_ATTRIBUTE_ERROR, MonitorNotification.OBSERVED_ATTRIBUTE_TYPE_ERROR, MonitorNotification.STRING_TO_COMPARE_VALUE_MATCHED, MonitorNotification.STRING_TO_COMPARE_VALUE_DIFFERED};
    private static final MBeanNotificationInfo[] notifsInfo = {new MBeanNotificationInfo(types, "javax.management.monitor.MonitorNotification", "Notifications sent by the StringMonitor MBean")};
    private static final int MATCHING = 0;
    private static final int DIFFERING = 1;
    private static final int MATCHING_OR_DIFFERING = 2;
    private String stringToCompare = "";
    private boolean notifyMatch = false;
    private boolean notifyDiffer = false;
    private transient String[] derivedGauge = new String[16];
    private transient long[] derivedGaugeTimestamp = new long[16];
    private transient int[] status = new int[16];
    private transient Timer timer = null;

    String makeDebugTag() {
        return "StringMonitor";
    }

    public StringMonitor() {
        this.dbgTag = makeDebugTag();
    }

    @Override // javax.management.monitor.Monitor, javax.management.monitor.MonitorMBean
    public synchronized void start() {
        if (isTraceOn()) {
            trace("start", "start the string monitor");
        }
        if (!isActive()) {
            this.isActive = true;
            for (int i = 0; i < this.elementCount; i++) {
                this.status[i] = 2;
            }
            this.timer = new Timer();
            this.timer.schedule(new StringAlarmClock(this), getGranularityPeriod(), getGranularityPeriod());
        } else if (isTraceOn()) {
            trace("start", "the string monitor is already activated");
        }
    }

    @Override // javax.management.monitor.Monitor, javax.management.monitor.MonitorMBean
    public void stop() {
        if (isTraceOn()) {
            trace("stop", "stop the string monitor");
        }
        if (isActive()) {
            if (this.timer != null) {
                this.timer.cancel();
                this.timer = null;
            }
            this.isActive = false;
        } else if (isTraceOn()) {
            trace("stop", "the string monitor is already deactivated");
        }
    }

    @Override // javax.management.monitor.Monitor, javax.management.monitor.MonitorMBean
    public synchronized void setGranularityPeriod(long j) throws IllegalArgumentException {
        super.setGranularityPeriod(j);
        if (isActive()) {
            this.timer.cancel();
            this.timer = new Timer();
            this.timer.schedule(new StringAlarmClock(this), getGranularityPeriod(), getGranularityPeriod());
        }
    }

    @Override // javax.management.monitor.StringMonitorMBean
    public String getDerivedGauge(ObjectName objectName) {
        int indexOf = indexOf(objectName);
        if (indexOf != -1) {
            return this.derivedGauge[indexOf];
        }
        return null;
    }

    @Override // javax.management.monitor.StringMonitorMBean
    public long getDerivedGaugeTimeStamp(ObjectName objectName) {
        int indexOf = indexOf(objectName);
        if (indexOf != -1) {
            return this.derivedGaugeTimestamp[indexOf];
        }
        return 0L;
    }

    @Override // javax.management.monitor.StringMonitorMBean
    public synchronized String getDerivedGauge() {
        return this.derivedGauge[0];
    }

    @Override // javax.management.monitor.StringMonitorMBean
    public synchronized long getDerivedGaugeTimeStamp() {
        return this.derivedGaugeTimestamp[0];
    }

    @Override // javax.management.monitor.StringMonitorMBean
    public synchronized String getStringToCompare() {
        return this.stringToCompare;
    }

    @Override // javax.management.monitor.StringMonitorMBean
    public synchronized void setStringToCompare(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("The string to compare cannot be null.");
        }
        this.stringToCompare = str;
        for (int i = 0; i < this.elementCount; i++) {
            this.status[i] = 2;
        }
    }

    @Override // javax.management.monitor.StringMonitorMBean
    public synchronized boolean getNotifyMatch() {
        return this.notifyMatch;
    }

    @Override // javax.management.monitor.StringMonitorMBean
    public synchronized void setNotifyMatch(boolean z) {
        this.notifyMatch = z;
    }

    @Override // javax.management.monitor.StringMonitorMBean
    public synchronized boolean getNotifyDiffer() {
        return this.notifyDiffer;
    }

    @Override // javax.management.monitor.StringMonitorMBean
    public synchronized void setNotifyDiffer(boolean z) {
        this.notifyDiffer = z;
    }

    @Override // javax.management.NotificationBroadcasterSupport, javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        return notifsInfo;
    }

    private void updateDerivedGauge(Object obj, int i) {
        this.derivedGaugeTimestamp[i] = new Date().getTime();
        this.derivedGauge[i] = (String) obj;
    }

    private void updateNotifications(int i) {
        if (this.status[i] == 2) {
            if (this.derivedGauge[i].equals(this.stringToCompare)) {
                if (this.notifyMatch) {
                    sendNotification(MonitorNotification.STRING_TO_COMPARE_VALUE_MATCHED, this.derivedGaugeTimestamp[i], "", this.derivedGauge[i], this.stringToCompare, i);
                }
                this.status[i] = 1;
                return;
            }
            if (this.notifyDiffer) {
                sendNotification(MonitorNotification.STRING_TO_COMPARE_VALUE_DIFFERED, this.derivedGaugeTimestamp[i], "", this.derivedGauge[i], this.stringToCompare, i);
            }
            this.status[i] = 0;
        } else if (this.status[i] == 0) {
            if (this.derivedGauge[i].equals(this.stringToCompare)) {
                if (this.notifyMatch) {
                    sendNotification(MonitorNotification.STRING_TO_COMPARE_VALUE_MATCHED, this.derivedGaugeTimestamp[i], "", this.derivedGauge[i], this.stringToCompare, i);
                }
                this.status[i] = 1;
            }
        } else if (this.status[i] == 1 && !this.derivedGauge[i].equals(this.stringToCompare)) {
            if (this.notifyDiffer) {
                sendNotification(MonitorNotification.STRING_TO_COMPARE_VALUE_DIFFERED, this.derivedGaugeTimestamp[i], "", this.derivedGauge[i], this.stringToCompare, i);
            }
            this.status[i] = 0;
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
                            try {
                                Object attribute = this.server.getAttribute(getObservedObject(i), getObservedAttribute());
                                if (attribute == null) {
                                    return;
                                }
                                if (!(attribute instanceof String)) {
                                    if ((this.alreadyNotifieds[i] & 4) != 0) {
                                        return;
                                    }
                                    String str = MonitorNotification.OBSERVED_ATTRIBUTE_TYPE_ERROR;
                                    setAlreadyNotified(i, 4);
                                    throw new MonitorSettingException("The observed attribute type must be a string type.");
                                }
                                resetAllAlreadyNotified(i);
                                updateDerivedGauge(attribute, i);
                                updateNotifications(i);
                            } catch (InstanceNotFoundException e) {
                                if ((this.alreadyNotifieds[i] & 1) != 0) {
                                    return;
                                }
                                String str2 = MonitorNotification.OBSERVED_OBJECT_ERROR;
                                setAlreadyNotified(i, 1);
                                throw new MonitorSettingException("The observed object must be registered in the MBean server.");
                            }
                        } catch (NullPointerException e2) {
                            if ((this.alreadyNotifieds[i] & 8) != 0) {
                                return;
                            }
                            String str3 = MonitorNotification.RUNTIME_ERROR;
                            setAlreadyNotified(i, 8);
                            throw new MonitorSettingException("The string monitor must be registered in the MBean server.");
                        }
                    } catch (AttributeNotFoundException e3) {
                        if ((this.alreadyNotifieds[i] & 2) != 0) {
                            return;
                        }
                        String str4 = MonitorNotification.OBSERVED_ATTRIBUTE_ERROR;
                        setAlreadyNotified(i, 2);
                        throw new MonitorSettingException("The observed attribute must be accessible in the observed object.");
                    }
                } catch (MBeanException e4) {
                    if ((this.alreadyNotifieds[i] & 8) != 0) {
                        return;
                    }
                    String str5 = MonitorNotification.RUNTIME_ERROR;
                    setAlreadyNotified(i, 8);
                    throw new MonitorSettingException(e4.getMessage());
                } catch (ReflectionException e5) {
                    if ((this.alreadyNotifieds[i] & 2) != 0) {
                        return;
                    }
                    String str6 = MonitorNotification.OBSERVED_ATTRIBUTE_ERROR;
                    setAlreadyNotified(i, 2);
                    throw new MonitorSettingException(e5.getMessage());
                }
            }
        } catch (MonitorSettingException e6) {
            sendNotification(null, this.derivedGaugeTimestamp[i], e6.getMessage(), this.derivedGauge[i], null, i);
            this.status[i] = 2;
        }
    }

    @Override // javax.management.monitor.Monitor
    void insertSpecificElementAt(int i) {
        insertStringElementAt(this.derivedGauge, "", i);
        insertlongElementAt(this.derivedGaugeTimestamp, new Date().getTime(), i);
        insertintElementAt(this.status, 2, i);
    }

    @Override // javax.management.monitor.Monitor
    void removeSpecificElementAt(int i) {
        removeStringElementAt(this.derivedGauge, i);
        removelongElementAt(this.derivedGaugeTimestamp, i);
        removeintElementAt(this.status, i);
    }

    /* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/monitor/StringMonitor$StringAlarmClock.class */
    private static class StringAlarmClock extends TimerTask {
        StringMonitor listener;

        public StringAlarmClock(StringMonitor stringMonitor) {
            this.listener = null;
            this.listener = stringMonitor;
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
