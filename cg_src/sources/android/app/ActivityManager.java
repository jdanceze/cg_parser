package android.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Debug;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/ActivityManager.class */
public class ActivityManager {
    public static final int RECENT_WITH_EXCLUDED = 1;
    public static final int RECENT_IGNORE_UNAVAILABLE = 2;
    public static final int MOVE_TASK_WITH_HOME = 1;
    public static final int MOVE_TASK_NO_USER_ACTION = 2;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/ActivityManager$RecentTaskInfo.class */
    public static class RecentTaskInfo implements Parcelable {
        public int id;
        public int persistentId;
        public Intent baseIntent;
        public ComponentName origActivity;
        public CharSequence description;
        public static final Parcelable.Creator<RecentTaskInfo> CREATOR = null;

        public RecentTaskInfo() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            throw new RuntimeException("Stub!");
        }

        public void readFromParcel(Parcel source) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/ActivityManager$RunningTaskInfo.class */
    public static class RunningTaskInfo implements Parcelable {
        public int id;
        public ComponentName baseActivity;
        public ComponentName topActivity;
        public Bitmap thumbnail;
        public CharSequence description;
        public int numActivities;
        public int numRunning;
        public static final Parcelable.Creator<RunningTaskInfo> CREATOR = null;

        public RunningTaskInfo() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            throw new RuntimeException("Stub!");
        }

        public void readFromParcel(Parcel source) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/ActivityManager$RunningServiceInfo.class */
    public static class RunningServiceInfo implements Parcelable {
        public ComponentName service;
        public int pid;
        public int uid;
        public String process;
        public boolean foreground;
        public long activeSince;
        public boolean started;
        public int clientCount;
        public int crashCount;
        public long lastActivityTime;
        public long restarting;
        public static final int FLAG_STARTED = 1;
        public static final int FLAG_FOREGROUND = 2;
        public static final int FLAG_SYSTEM_PROCESS = 4;
        public static final int FLAG_PERSISTENT_PROCESS = 8;
        public int flags;
        public String clientPackage;
        public int clientLabel;
        public static final Parcelable.Creator<RunningServiceInfo> CREATOR = null;

        public RunningServiceInfo() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            throw new RuntimeException("Stub!");
        }

        public void readFromParcel(Parcel source) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/ActivityManager$MemoryInfo.class */
    public static class MemoryInfo implements Parcelable {
        public long availMem;
        public long totalMem;
        public long threshold;
        public boolean lowMemory;
        public static final Parcelable.Creator<MemoryInfo> CREATOR = null;

        public MemoryInfo() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            throw new RuntimeException("Stub!");
        }

        public void readFromParcel(Parcel source) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/ActivityManager$ProcessErrorStateInfo.class */
    public static class ProcessErrorStateInfo implements Parcelable {
        public static final int NO_ERROR = 0;
        public static final int CRASHED = 1;
        public static final int NOT_RESPONDING = 2;
        public int condition;
        public String processName;
        public int pid;
        public int uid;
        public String tag;
        public String shortMsg;
        public String longMsg;
        public String stackTrace;
        public byte[] crashData = null;
        public static final Parcelable.Creator<ProcessErrorStateInfo> CREATOR = null;

        public ProcessErrorStateInfo() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            throw new RuntimeException("Stub!");
        }

        public void readFromParcel(Parcel source) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/ActivityManager$RunningAppProcessInfo.class */
    public static class RunningAppProcessInfo implements Parcelable {
        public String processName;
        public int pid;
        public int uid;
        public String[] pkgList = null;
        public int lastTrimLevel;
        public static final int IMPORTANCE_FOREGROUND = 100;
        public static final int IMPORTANCE_VISIBLE = 200;
        public static final int IMPORTANCE_PERCEPTIBLE = 130;
        public static final int IMPORTANCE_SERVICE = 300;
        public static final int IMPORTANCE_BACKGROUND = 400;
        public static final int IMPORTANCE_EMPTY = 500;
        public int importance;
        public int lru;
        public static final int REASON_UNKNOWN = 0;
        public static final int REASON_PROVIDER_IN_USE = 1;
        public static final int REASON_SERVICE_IN_USE = 2;
        public int importanceReasonCode;
        public int importanceReasonPid;
        public ComponentName importanceReasonComponent;
        public static final Parcelable.Creator<RunningAppProcessInfo> CREATOR = null;

        public RunningAppProcessInfo() {
            throw new RuntimeException("Stub!");
        }

        public RunningAppProcessInfo(String pProcessName, int pPid, String[] pArr) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            throw new RuntimeException("Stub!");
        }

        public void readFromParcel(Parcel source) {
            throw new RuntimeException("Stub!");
        }
    }

    ActivityManager() {
        throw new RuntimeException("Stub!");
    }

    public int getMemoryClass() {
        throw new RuntimeException("Stub!");
    }

    public int getLargeMemoryClass() {
        throw new RuntimeException("Stub!");
    }

    public List<RecentTaskInfo> getRecentTasks(int maxNum, int flags) throws SecurityException {
        throw new RuntimeException("Stub!");
    }

    public List<RunningTaskInfo> getRunningTasks(int maxNum) throws SecurityException {
        throw new RuntimeException("Stub!");
    }

    public void moveTaskToFront(int taskId, int flags) {
        throw new RuntimeException("Stub!");
    }

    public void moveTaskToFront(int taskId, int flags, Bundle options) {
        throw new RuntimeException("Stub!");
    }

    public List<RunningServiceInfo> getRunningServices(int maxNum) throws SecurityException {
        throw new RuntimeException("Stub!");
    }

    public PendingIntent getRunningServiceControlPanel(ComponentName service) throws SecurityException {
        throw new RuntimeException("Stub!");
    }

    public void getMemoryInfo(MemoryInfo outInfo) {
        throw new RuntimeException("Stub!");
    }

    public List<ProcessErrorStateInfo> getProcessesInErrorState() {
        throw new RuntimeException("Stub!");
    }

    public List<RunningAppProcessInfo> getRunningAppProcesses() {
        throw new RuntimeException("Stub!");
    }

    public static void getMyMemoryState(RunningAppProcessInfo outState) {
        throw new RuntimeException("Stub!");
    }

    public Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void restartPackage(String packageName) {
        throw new RuntimeException("Stub!");
    }

    public void killBackgroundProcesses(String packageName) {
        throw new RuntimeException("Stub!");
    }

    public ConfigurationInfo getDeviceConfigurationInfo() {
        throw new RuntimeException("Stub!");
    }

    public int getLauncherLargeIconDensity() {
        throw new RuntimeException("Stub!");
    }

    public int getLauncherLargeIconSize() {
        throw new RuntimeException("Stub!");
    }

    public static boolean isUserAMonkey() {
        throw new RuntimeException("Stub!");
    }

    public static boolean isRunningInTestHarness() {
        throw new RuntimeException("Stub!");
    }
}
