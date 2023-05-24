package javax.management.timer;

import javax.management.Notification;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/timer/TimerAlarmClockNotification.class */
public class TimerAlarmClockNotification extends Notification {
    private static final long serialVersionUID = -4841061275673620641L;

    public TimerAlarmClockNotification(TimerAlarmClock timerAlarmClock) {
        super("", timerAlarmClock, 0L);
    }
}
