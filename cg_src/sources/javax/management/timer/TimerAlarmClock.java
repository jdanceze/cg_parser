package javax.management.timer;

import java.util.Date;
import java.util.TimerTask;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Timer.java */
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/timer/TimerAlarmClock.class */
public class TimerAlarmClock extends TimerTask {
    Timer listener;
    long timeout;
    Date next;

    public TimerAlarmClock(Timer timer, long j) {
        this.listener = null;
        this.timeout = 10000L;
        this.next = null;
        this.listener = timer;
        this.timeout = Math.max(0L, j);
    }

    public TimerAlarmClock(Timer timer, Date date) {
        this.listener = null;
        this.timeout = 10000L;
        this.next = null;
        this.listener = timer;
        this.next = date;
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public void run() {
        try {
            this.listener.notifyAlarmClock(new TimerAlarmClockNotification(this));
        } catch (Exception e) {
        }
    }
}
