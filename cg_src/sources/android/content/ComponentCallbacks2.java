package android.content;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/ComponentCallbacks2.class */
public interface ComponentCallbacks2 extends ComponentCallbacks {
    public static final int TRIM_MEMORY_COMPLETE = 80;
    public static final int TRIM_MEMORY_MODERATE = 60;
    public static final int TRIM_MEMORY_BACKGROUND = 40;
    public static final int TRIM_MEMORY_UI_HIDDEN = 20;
    public static final int TRIM_MEMORY_RUNNING_CRITICAL = 15;
    public static final int TRIM_MEMORY_RUNNING_LOW = 10;
    public static final int TRIM_MEMORY_RUNNING_MODERATE = 5;

    void onTrimMemory(int i);
}
