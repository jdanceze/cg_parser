package com.autostart;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
/* loaded from: classes.dex */
public class TimeOutHomeReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("autostart", 0);
            boolean gotoHome = prefs.getBoolean("gotohome", true);
            if (gotoHome) {
                try {
                    Intent startMain = new Intent("android.intent.action.MAIN");
                    startMain.addCategory("android.intent.category.HOME");
                    startMain.setFlags(268435456);
                    context.startActivity(startMain);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            AlarmManager mgr = (AlarmManager) context.getSystemService("alarm");
            Intent i = new Intent(context, TimeOutReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
            mgr.set(2, SystemClock.elapsedRealtime() + 1000, pi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
