package android.app;

import android.content.ComponentName;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Printer;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/ApplicationErrorReport.class */
public class ApplicationErrorReport implements Parcelable {
    public static final int TYPE_NONE = 0;
    public static final int TYPE_CRASH = 1;
    public static final int TYPE_ANR = 2;
    public static final int TYPE_BATTERY = 3;
    public static final int TYPE_RUNNING_SERVICE = 5;
    public int type;
    public String packageName;
    public String installerPackageName;
    public String processName;
    public long time;
    public boolean systemApp;
    public CrashInfo crashInfo;
    public AnrInfo anrInfo;
    public BatteryInfo batteryInfo;
    public RunningServiceInfo runningServiceInfo;
    public static final Parcelable.Creator<ApplicationErrorReport> CREATOR = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/ApplicationErrorReport$CrashInfo.class */
    public static class CrashInfo {
        public String exceptionClassName;
        public String exceptionMessage;
        public String throwFileName;
        public String throwClassName;
        public String throwMethodName;
        public int throwLineNumber;
        public String stackTrace;

        public CrashInfo() {
            throw new RuntimeException("Stub!");
        }

        public CrashInfo(Throwable tr) {
            throw new RuntimeException("Stub!");
        }

        public CrashInfo(Parcel in) {
            throw new RuntimeException("Stub!");
        }

        public void writeToParcel(Parcel dest, int flags) {
            throw new RuntimeException("Stub!");
        }

        public void dump(Printer pw, String prefix) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/ApplicationErrorReport$AnrInfo.class */
    public static class AnrInfo {
        public String activity;
        public String cause;
        public String info;

        public AnrInfo() {
            throw new RuntimeException("Stub!");
        }

        public AnrInfo(Parcel in) {
            throw new RuntimeException("Stub!");
        }

        public void writeToParcel(Parcel dest, int flags) {
            throw new RuntimeException("Stub!");
        }

        public void dump(Printer pw, String prefix) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/ApplicationErrorReport$BatteryInfo.class */
    public static class BatteryInfo {
        public int usagePercent;
        public long durationMicros;
        public String usageDetails;
        public String checkinDetails;

        public BatteryInfo() {
            throw new RuntimeException("Stub!");
        }

        public BatteryInfo(Parcel in) {
            throw new RuntimeException("Stub!");
        }

        public void writeToParcel(Parcel dest, int flags) {
            throw new RuntimeException("Stub!");
        }

        public void dump(Printer pw, String prefix) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/ApplicationErrorReport$RunningServiceInfo.class */
    public static class RunningServiceInfo {
        public long durationMillis;
        public String serviceDetails;

        public RunningServiceInfo() {
            throw new RuntimeException("Stub!");
        }

        public RunningServiceInfo(Parcel in) {
            throw new RuntimeException("Stub!");
        }

        public void writeToParcel(Parcel dest, int flags) {
            throw new RuntimeException("Stub!");
        }

        public void dump(Printer pw, String prefix) {
            throw new RuntimeException("Stub!");
        }
    }

    public ApplicationErrorReport() {
        throw new RuntimeException("Stub!");
    }

    public static ComponentName getErrorReportReceiver(Context context, String packageName, int appFlags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }

    public void readFromParcel(Parcel in) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    public void dump(Printer pw, String prefix) {
        throw new RuntimeException("Stub!");
    }
}
