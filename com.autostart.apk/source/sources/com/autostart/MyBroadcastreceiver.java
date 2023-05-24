package com.autostart;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
/* loaded from: classes.dex */
public class MyBroadcastreceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("autostart", 0);
        int timeout = prefs.getInt("startdelay", 0);
        AlarmManager mgr = (AlarmManager) context.getSystemService("alarm");
        Intent i = new Intent(context, TimeOutReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        mgr.set(2, SystemClock.elapsedRealtime() + (timeout * 1000) + 300, pi);
    }
}
