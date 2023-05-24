package android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/TaskStackBuilder.class */
public class TaskStackBuilder {
    TaskStackBuilder() {
        throw new RuntimeException("Stub!");
    }

    public static TaskStackBuilder create(Context context) {
        throw new RuntimeException("Stub!");
    }

    public TaskStackBuilder addNextIntent(Intent nextIntent) {
        throw new RuntimeException("Stub!");
    }

    public TaskStackBuilder addNextIntentWithParentStack(Intent nextIntent) {
        throw new RuntimeException("Stub!");
    }

    public TaskStackBuilder addParentStack(Activity sourceActivity) {
        throw new RuntimeException("Stub!");
    }

    public TaskStackBuilder addParentStack(Class<?> sourceActivityClass) {
        throw new RuntimeException("Stub!");
    }

    public TaskStackBuilder addParentStack(ComponentName sourceActivityName) {
        throw new RuntimeException("Stub!");
    }

    public int getIntentCount() {
        throw new RuntimeException("Stub!");
    }

    public Intent editIntentAt(int index) {
        throw new RuntimeException("Stub!");
    }

    public void startActivities() {
        throw new RuntimeException("Stub!");
    }

    public void startActivities(Bundle options) {
        throw new RuntimeException("Stub!");
    }

    public PendingIntent getPendingIntent(int requestCode, int flags) {
        throw new RuntimeException("Stub!");
    }

    public PendingIntent getPendingIntent(int requestCode, int flags, Bundle options) {
        throw new RuntimeException("Stub!");
    }

    public Intent[] getIntents() {
        throw new RuntimeException("Stub!");
    }
}
