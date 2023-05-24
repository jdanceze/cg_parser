package com.autostart;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.SystemClock;
/* loaded from: classes.dex */
public class TimeOutReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("autostart", 0);
        String packageNames = prefs.getString("package", "");
        String classNames = prefs.getString("class", "");
        int timeout = prefs.getInt("inbetweendelay", 3);
        boolean enabled = prefs.getBoolean("enabled", false);
        boolean notificaton = prefs.getBoolean("noti", true);
        int pos = prefs.getInt("iteration", 0);
        if (enabled && !packageNames.equalsIgnoreCase("") && !classNames.equalsIgnoreCase("")) {
            try {
                String[] splitPackages = packageNames.split(";;");
                String[] splitClassNames = classNames.split(";;");
                if (splitClassNames.length > pos && splitPackages.length > pos) {
                    String packageName = splitPackages[pos];
                    String className = splitClassNames[pos];
                    if (notificaton) {
                        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService("notification");
                        CharSequence contentText = "Started application: " + className;
                        Intent notificationIntent = new Intent(context, AutoStartActivity.class);
                        notificationIntent.setFlags(603979776);
                        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
                        Notification mNotification = new Notification(R.drawable.icon, contentText, System.currentTimeMillis());
                        mNotification.setLatestEventInfo(context, "Auto Start", contentText, contentIntent);
                        mNotificationManager.notify(3461, mNotification);
                    }
                    PackageManager pm = context.getPackageManager();
                    Intent i = pm.getLaunchIntentForPackage(packageName);
                    i.setFlags(268435456);
                    context.startActivity(i);
                    int pos2 = pos + 1;
                    SharedPreferences.Editor editor = prefs.edit();
                    if (pos2 == splitPackages.length) {
                        editor.putInt("iteration", 0);
                    } else {
                        editor.putInt("iteration", pos2);
                        AlarmManager mgr = (AlarmManager) context.getSystemService("alarm");
                        Intent ii = new Intent(context, TimeOutHomeReceiver.class);
                        PendingIntent pi = PendingIntent.getBroadcast(context, 0, ii, 0);
                        mgr.set(2, SystemClock.elapsedRealtime() + (timeout * 1000), pi);
                    }
                    editor.commit();
                }
            } catch (Exception e) {
            }
        }
    }
}
