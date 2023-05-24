package javax.management.timer;

import javax.management.Notification;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/timer/TimerNotification.class */
public class TimerNotification extends Notification {
    private static final long serialVersionUID = 1798492029603825750L;
    private Integer notificationID;

    public TimerNotification(String str, Object obj, long j, long j2, String str2, Integer num) {
        super(str, obj, j, j2, str2);
        this.notificationID = num;
    }

    public Integer getNotificationID() {
        return this.notificationID;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object cloneTimerNotification() {
        TimerNotification timerNotification = new TimerNotification(getType(), getSource(), getSequenceNumber(), getTimeStamp(), getMessage(), this.notificationID);
        timerNotification.setUserData(getUserData());
        return timerNotification;
    }
}
