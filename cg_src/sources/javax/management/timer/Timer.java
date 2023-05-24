package javax.management.timer;

import com.sun.jmx.trace.Trace;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/timer/Timer.class */
public class Timer extends NotificationBroadcasterSupport implements TimerMBean, MBeanRegistration {
    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = 60000;
    public static final long ONE_HOUR = 3600000;
    public static final long ONE_DAY = 86400000;
    public static final long ONE_WEEK = 604800000;
    private static final String dbgTag = "Timer";
    private static final int TIMER_NOTIF_INDEX = 0;
    private static final int TIMER_DATE_INDEX = 1;
    private static final int TIMER_PERIOD_INDEX = 2;
    private static final int TIMER_NB_OCCUR_INDEX = 3;
    private static final int ALARM_CLOCK_INDEX = 4;
    private static final int FIXED_RATE_INDEX = 5;
    private java.util.Timer timer;
    private Hashtable timerTable = new Hashtable();
    private boolean sendPastNotifications = false;
    private transient boolean isActive = false;
    private transient long sequenceNumber = 0;
    private int counterID = 0;

    boolean isTraceOn() {
        return Trace.isSelected(1, 8);
    }

    void trace(String str, String str2, String str3) {
        Trace.send(1, 8, str, str2, str3);
    }

    void trace(String str, String str2) {
        trace(dbgTag, str, str2);
    }

    boolean isDebugOn() {
        return Trace.isSelected(2, 8);
    }

    void debug(String str, String str2, String str3) {
        Trace.send(2, 8, str, str2, str3);
    }

    void debug(String str, String str2) {
        debug(dbgTag, str, str2);
    }

    @Override // javax.management.MBeanRegistration
    public ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        return objectName;
    }

    @Override // javax.management.MBeanRegistration
    public void postRegister(Boolean bool) {
    }

    @Override // javax.management.MBeanRegistration
    public void preDeregister() throws Exception {
        if (isTraceOn()) {
            trace("preDeregister", "stop the timer");
        }
        stop();
    }

    @Override // javax.management.MBeanRegistration
    public void postDeregister() {
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized void start() {
        if (isTraceOn()) {
            trace("start", "starting the timer");
        }
        if (!this.isActive) {
            this.timer = new java.util.Timer();
            Date date = new Date();
            sendPastNotifications(date, this.sendPastNotifications);
            Enumeration elements = this.timerTable.elements();
            while (elements.hasMoreElements()) {
                Object[] objArr = (Object[]) elements.nextElement();
                Date date2 = (Date) objArr[1];
                if (((Boolean) objArr[5]).booleanValue()) {
                    TimerAlarmClock timerAlarmClock = new TimerAlarmClock(this, date2);
                    objArr[4] = timerAlarmClock;
                    this.timer.schedule(timerAlarmClock, timerAlarmClock.next);
                } else {
                    TimerAlarmClock timerAlarmClock2 = new TimerAlarmClock(this, date2.getTime() - date.getTime());
                    objArr[4] = timerAlarmClock2;
                    this.timer.schedule(timerAlarmClock2, timerAlarmClock2.timeout);
                }
            }
            this.isActive = true;
            if (isTraceOn()) {
                trace("start", "timer started");
            }
        } else if (isTraceOn()) {
            trace("start", "the timer is already activated");
        }
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized void stop() {
        if (isTraceOn()) {
            trace("stop", "stoping the timer");
        }
        if (this.isActive) {
            Enumeration elements = this.timerTable.elements();
            while (elements.hasMoreElements()) {
                TimerAlarmClock timerAlarmClock = (TimerAlarmClock) ((Object[]) elements.nextElement())[4];
                if (timerAlarmClock != null) {
                    timerAlarmClock.cancel();
                }
            }
            this.timer.cancel();
            this.isActive = false;
            if (isTraceOn()) {
                trace("stop", "timer stopped");
            }
        } else if (isTraceOn()) {
            trace("stop", "the timer is already deactivated");
        }
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Integer addNotification(String str, String str2, Object obj, Date date, long j, long j2, boolean z) throws IllegalArgumentException {
        TimerAlarmClock timerAlarmClock;
        if (date == null) {
            throw new IllegalArgumentException("Timer notification date cannot be null.");
        }
        if (j < 0 || j2 < 0) {
            throw new IllegalArgumentException("Negative values for the periodicity");
        }
        Date date2 = new Date();
        if (date2.after(date)) {
            date.setTime(date2.getTime());
            if (isTraceOn()) {
                trace("addNotification", new StringBuffer().append("update timer notification to add with:\n\tNotification date = ").append(date).toString());
            }
        }
        int i = this.counterID + 1;
        this.counterID = i;
        Integer num = new Integer(i);
        TimerNotification timerNotification = new TimerNotification(str, this, 0L, 0L, str2, num);
        timerNotification.setUserData(obj);
        Object[] objArr = new Object[6];
        if (z) {
            timerAlarmClock = new TimerAlarmClock(this, date);
        } else {
            timerAlarmClock = new TimerAlarmClock(this, date.getTime() - date2.getTime());
        }
        Date date3 = new Date(date.getTime());
        objArr[0] = timerNotification;
        objArr[1] = date3;
        objArr[2] = new Long(j);
        objArr[3] = new Long(j2);
        objArr[4] = timerAlarmClock;
        objArr[5] = new Boolean(z);
        if (isTraceOn()) {
            trace("addNotification", new StringBuffer().append("adding timer notification:\n\tNotification source = ").append(timerNotification.getSource()).append("\n\tNotification type = ").append(timerNotification.getType()).append("\n\tNotification ID = ").append(num).append("\n\tNotification date = ").append(date3).append("\n\tNotification period = ").append(j).append("\n\tNotification nb of occurences = ").append(j2).append("\n\tNotification executes at fixed rate = ").append(z).toString());
        }
        this.timerTable.put(num, objArr);
        if (this.isActive) {
            if (z) {
                this.timer.schedule(timerAlarmClock, timerAlarmClock.next);
            } else {
                this.timer.schedule(timerAlarmClock, timerAlarmClock.timeout);
            }
        }
        if (isTraceOn()) {
            trace("addNotification", "timer notification added");
        }
        return num;
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Integer addNotification(String str, String str2, Object obj, Date date, long j, long j2) throws IllegalArgumentException {
        return addNotification(str, str2, obj, date, j, j2, false);
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Integer addNotification(String str, String str2, Object obj, Date date, long j) throws IllegalArgumentException {
        return addNotification(str, str2, obj, date, j, 0L);
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Integer addNotification(String str, String str2, Object obj, Date date) throws IllegalArgumentException {
        return addNotification(str, str2, obj, date, 0L, 0L);
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized void removeNotification(Integer num) throws InstanceNotFoundException {
        if (!this.timerTable.containsKey(num)) {
            throw new InstanceNotFoundException("Timer notification to remove not in the list of notifications");
        }
        Object[] objArr = (Object[]) this.timerTable.get(num);
        TimerAlarmClock timerAlarmClock = (TimerAlarmClock) objArr[4];
        if (timerAlarmClock != null) {
            timerAlarmClock.cancel();
        }
        if (isTraceOn()) {
            trace("removeNotification", new StringBuffer().append("removing timer notification:\n\tNotification source = ").append(((TimerNotification) objArr[0]).getSource()).append("\n\tNotification type = ").append(((TimerNotification) objArr[0]).getType()).append("\n\tNotification ID = ").append(((TimerNotification) objArr[0]).getNotificationID()).append("\n\tNotification date = ").append(objArr[1]).append("\n\tNotification period = ").append(objArr[2]).append("\n\tNotification nb of occurences = ").append(objArr[3]).append("\n\tNotification executes at fixed rate = ").append(objArr[5]).toString());
        }
        this.timerTable.remove(num);
        if (isTraceOn()) {
            trace("removeNotification", "timer notification removed");
        }
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized void removeNotifications(String str) throws InstanceNotFoundException {
        Vector notificationIDs = getNotificationIDs(str);
        if (notificationIDs.isEmpty()) {
            throw new InstanceNotFoundException("Timer notifications to remove not in the list of notifications");
        }
        Enumeration elements = notificationIDs.elements();
        while (elements.hasMoreElements()) {
            Integer notificationID = ((TimerNotification) elements.nextElement()).getNotificationID();
            Object[] objArr = (Object[]) this.timerTable.get(notificationID);
            this.timerTable.remove(notificationID);
            TimerAlarmClock timerAlarmClock = (TimerAlarmClock) objArr[4];
            if (timerAlarmClock != null) {
                timerAlarmClock.cancel();
            }
        }
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized void removeAllNotifications() {
        Enumeration elements = this.timerTable.elements();
        while (elements.hasMoreElements()) {
            ((TimerAlarmClock) ((Object[]) elements.nextElement())[4]).cancel();
        }
        if (isTraceOn()) {
            trace("removeAllNotifications", "removing all timer notifications");
        }
        this.timerTable.clear();
        if (isTraceOn()) {
            trace("removeAllNotifications", "all timer notifications removed");
        }
        this.counterID = 0;
        if (isTraceOn()) {
            trace("removeAllNotifications", "timer notification counter ID resetted");
        }
    }

    @Override // javax.management.timer.TimerMBean
    public int getNbNotifications() {
        return this.timerTable.size();
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Vector getAllNotificationIDs() {
        Vector vector = new Vector();
        Enumeration keys = this.timerTable.keys();
        while (keys.hasMoreElements()) {
            vector.addElement((Integer) keys.nextElement());
        }
        return vector;
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Vector getNotificationIDs(String str) {
        Vector vector = new Vector();
        Enumeration elements = this.timerTable.elements();
        if (str == null) {
            while (elements.hasMoreElements()) {
                Object[] objArr = (Object[]) elements.nextElement();
                if (((TimerNotification) objArr[0]).getType() == null) {
                    vector.addElement((TimerNotification) objArr[0]);
                }
            }
        } else {
            while (elements.hasMoreElements()) {
                Object[] objArr2 = (Object[]) elements.nextElement();
                if (str.equals(((TimerNotification) objArr2[0]).getType())) {
                    vector.addElement((TimerNotification) objArr2[0]);
                }
            }
        }
        return vector;
    }

    @Override // javax.management.timer.TimerMBean
    public String getNotificationType(Integer num) {
        Object[] objArr = (Object[]) this.timerTable.get(num);
        if (objArr != null) {
            return ((TimerNotification) objArr[0]).getType();
        }
        return null;
    }

    @Override // javax.management.timer.TimerMBean
    public String getNotificationMessage(Integer num) {
        Object[] objArr = (Object[]) this.timerTable.get(num);
        if (objArr != null) {
            return ((TimerNotification) objArr[0]).getMessage();
        }
        return null;
    }

    @Override // javax.management.timer.TimerMBean
    public Object getNotificationUserData(Integer num) {
        Object[] objArr = (Object[]) this.timerTable.get(num);
        if (objArr != null) {
            return ((TimerNotification) objArr[0]).getUserData();
        }
        return null;
    }

    @Override // javax.management.timer.TimerMBean
    public Date getDate(Integer num) {
        Object[] objArr = (Object[]) this.timerTable.get(num);
        if (objArr != null) {
            return new Date(((Date) objArr[1]).getTime());
        }
        return null;
    }

    @Override // javax.management.timer.TimerMBean
    public Long getPeriod(Integer num) {
        Object[] objArr = (Object[]) this.timerTable.get(num);
        if (objArr != null) {
            return new Long(((Long) objArr[2]).longValue());
        }
        return null;
    }

    @Override // javax.management.timer.TimerMBean
    public Long getNbOccurences(Integer num) {
        Object[] objArr = (Object[]) this.timerTable.get(num);
        if (objArr != null) {
            return new Long(((Long) objArr[3]).longValue());
        }
        return null;
    }

    @Override // javax.management.timer.TimerMBean
    public Boolean getFixedRate(Integer num) {
        Object[] objArr = (Object[]) this.timerTable.get(num);
        if (objArr != null) {
            return new Boolean(((Boolean) objArr[5]).booleanValue());
        }
        return null;
    }

    @Override // javax.management.timer.TimerMBean
    public boolean getSendPastNotifications() {
        return this.sendPastNotifications;
    }

    @Override // javax.management.timer.TimerMBean
    public void setSendPastNotifications(boolean z) {
        this.sendPastNotifications = z;
    }

    @Override // javax.management.timer.TimerMBean
    public boolean isActive() {
        return this.isActive;
    }

    @Override // javax.management.timer.TimerMBean
    public boolean isEmpty() {
        return this.timerTable.isEmpty();
    }

    private synchronized void sendPastNotifications(Date date, boolean z) {
        Enumeration elements = this.timerTable.elements();
        while (elements.hasMoreElements()) {
            Object[] objArr = (Object[]) elements.nextElement();
            TimerNotification timerNotification = (TimerNotification) objArr[0];
            Integer notificationID = timerNotification.getNotificationID();
            Date date2 = (Date) objArr[1];
            while (date.after(date2) && this.timerTable.containsKey(notificationID)) {
                if (z) {
                    if (isTraceOn()) {
                        trace("sendPastNotifications", new StringBuffer().append("sending past timer notification:\n\tNotification source = ").append(timerNotification.getSource()).append("\n\tNotification type = ").append(timerNotification.getType()).append("\n\tNotification ID = ").append(timerNotification.getNotificationID()).append("\n\tNotification date = ").append(date2).append("\n\tNotification period = ").append(objArr[2]).append("\n\tNotification nb of occurences = ").append(objArr[3]).append("\n\tNotification executes at fixed rate = ").append(objArr[5]).toString());
                    }
                    sendNotification(date2, timerNotification);
                    if (isTraceOn()) {
                        trace("sendPastNotifications", "past timer notification sent");
                    }
                }
                updateTimerTable(timerNotification.getNotificationID());
            }
        }
    }

    private synchronized void updateTimerTable(Integer num) {
        Object[] objArr = (Object[]) this.timerTable.get(num);
        Date date = (Date) objArr[1];
        Long l = (Long) objArr[2];
        Long l2 = (Long) objArr[3];
        Boolean bool = (Boolean) objArr[5];
        TimerAlarmClock timerAlarmClock = (TimerAlarmClock) objArr[4];
        if (l.longValue() != 0) {
            if (l2.longValue() == 0 || l2.longValue() > 1) {
                date.setTime(date.getTime() + l.longValue());
                objArr[3] = new Long(Math.max(0L, l2.longValue() - 1));
                Long l3 = (Long) objArr[3];
                if (this.isActive) {
                    if (bool.booleanValue()) {
                        TimerAlarmClock timerAlarmClock2 = new TimerAlarmClock(this, date);
                        objArr[4] = timerAlarmClock2;
                        this.timer.schedule(timerAlarmClock2, timerAlarmClock2.next);
                    } else {
                        TimerAlarmClock timerAlarmClock3 = new TimerAlarmClock(this, l.longValue());
                        objArr[4] = timerAlarmClock3;
                        this.timer.schedule(timerAlarmClock3, timerAlarmClock3.timeout);
                    }
                }
                if (isTraceOn()) {
                    TimerNotification timerNotification = (TimerNotification) objArr[0];
                    trace("updateTimerTable", new StringBuffer().append("update timer notification with:\n\tNotification source = ").append(timerNotification.getSource()).append("\n\tNotification type = ").append(timerNotification.getType()).append("\n\tNotification ID = ").append(num).append("\n\tNotification date = ").append(date).append("\n\tNotification period = ").append(l).append("\n\tNotification nb of occurences = ").append(l3).append("\n\tNotification executes at fixed rate = ").append(bool).toString());
                    return;
                }
                return;
            }
            if (timerAlarmClock != null) {
                timerAlarmClock.cancel();
            }
            this.timerTable.remove(num);
            return;
        }
        if (timerAlarmClock != null) {
            timerAlarmClock.cancel();
        }
        this.timerTable.remove(num);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyAlarmClock(TimerAlarmClockNotification timerAlarmClockNotification) {
        TimerNotification timerNotification = null;
        Date date = null;
        TimerAlarmClock timerAlarmClock = (TimerAlarmClock) timerAlarmClockNotification.getSource();
        Enumeration elements = this.timerTable.elements();
        while (true) {
            if (!elements.hasMoreElements()) {
                break;
            }
            Object[] objArr = (Object[]) elements.nextElement();
            if (objArr[4] == timerAlarmClock) {
                timerNotification = (TimerNotification) objArr[0];
                date = (Date) objArr[1];
                break;
            }
        }
        sendNotification(date, timerNotification);
        updateTimerTable(timerNotification.getNotificationID());
    }

    void sendNotification(Date date, TimerNotification timerNotification) {
        long j;
        if (isTraceOn()) {
            trace("sendNotification", new StringBuffer().append("sending timer notification:\n\tNotification source = ").append(timerNotification.getSource()).append("\n\tNotification type = ").append(timerNotification.getType()).append("\n\tNotification ID = ").append(timerNotification.getNotificationID()).append("\n\tNotification date = ").append(date).toString());
        }
        synchronized (this) {
            this.sequenceNumber++;
            j = this.sequenceNumber;
        }
        synchronized (timerNotification) {
            timerNotification.setTimeStamp(date.getTime());
            timerNotification.setSequenceNumber(j);
            sendNotification((TimerNotification) timerNotification.cloneTimerNotification());
        }
        if (isTraceOn()) {
            trace("sendNotification", "timer notification sent");
        }
    }
}
