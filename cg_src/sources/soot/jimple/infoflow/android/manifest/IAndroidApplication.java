package soot.jimple.infoflow.android.manifest;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/manifest/IAndroidApplication.class */
public interface IAndroidApplication {
    boolean isEnabled();

    boolean isDebuggable();

    boolean isAllowBackup();

    String getName();

    Boolean isUsesCleartextTraffic();
}
