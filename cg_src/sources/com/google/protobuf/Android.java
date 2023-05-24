package com.google.protobuf;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/Android.class */
final class Android {
    private static boolean ASSUME_ANDROID;
    private static final Class<?> MEMORY_CLASS = getClassForName("libcore.io.Memory");
    private static final boolean IS_ROBOLECTRIC;

    private Android() {
    }

    static {
        IS_ROBOLECTRIC = (ASSUME_ANDROID || getClassForName("org.robolectric.Robolectric") == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isOnAndroidDevice() {
        return ASSUME_ANDROID || !(MEMORY_CLASS == null || IS_ROBOLECTRIC);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Class<?> getMemoryClass() {
        return MEMORY_CLASS;
    }

    private static <T> Class<T> getClassForName(String name) {
        try {
            return (Class<T>) Class.forName(name);
        } catch (Throwable th) {
            return null;
        }
    }
}
