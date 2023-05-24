package android.os;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/StrictMode.class */
public final class StrictMode {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/StrictMode$ThreadPolicy.class */
    public static final class ThreadPolicy {
        public static final ThreadPolicy LAX = null;

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/StrictMode$ThreadPolicy$Builder.class */
        public static final class Builder {
            public Builder() {
                throw new RuntimeException("Stub!");
            }

            public Builder(ThreadPolicy policy) {
                throw new RuntimeException("Stub!");
            }

            public Builder detectAll() {
                throw new RuntimeException("Stub!");
            }

            public Builder permitAll() {
                throw new RuntimeException("Stub!");
            }

            public Builder detectNetwork() {
                throw new RuntimeException("Stub!");
            }

            public Builder permitNetwork() {
                throw new RuntimeException("Stub!");
            }

            public Builder detectDiskReads() {
                throw new RuntimeException("Stub!");
            }

            public Builder permitDiskReads() {
                throw new RuntimeException("Stub!");
            }

            public Builder detectCustomSlowCalls() {
                throw new RuntimeException("Stub!");
            }

            public Builder permitCustomSlowCalls() {
                throw new RuntimeException("Stub!");
            }

            public Builder detectDiskWrites() {
                throw new RuntimeException("Stub!");
            }

            public Builder permitDiskWrites() {
                throw new RuntimeException("Stub!");
            }

            public Builder penaltyDialog() {
                throw new RuntimeException("Stub!");
            }

            public Builder penaltyDeath() {
                throw new RuntimeException("Stub!");
            }

            public Builder penaltyDeathOnNetwork() {
                throw new RuntimeException("Stub!");
            }

            public Builder penaltyFlashScreen() {
                throw new RuntimeException("Stub!");
            }

            public Builder penaltyLog() {
                throw new RuntimeException("Stub!");
            }

            public Builder penaltyDropBox() {
                throw new RuntimeException("Stub!");
            }

            public ThreadPolicy build() {
                throw new RuntimeException("Stub!");
            }
        }

        ThreadPolicy() {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/StrictMode$VmPolicy.class */
    public static final class VmPolicy {
        public static final VmPolicy LAX = null;

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/StrictMode$VmPolicy$Builder.class */
        public static final class Builder {
            public Builder() {
                throw new RuntimeException("Stub!");
            }

            public Builder(VmPolicy base) {
                throw new RuntimeException("Stub!");
            }

            public Builder setClassInstanceLimit(Class klass, int instanceLimit) {
                throw new RuntimeException("Stub!");
            }

            public Builder detectActivityLeaks() {
                throw new RuntimeException("Stub!");
            }

            public Builder detectAll() {
                throw new RuntimeException("Stub!");
            }

            public Builder detectLeakedSqlLiteObjects() {
                throw new RuntimeException("Stub!");
            }

            public Builder detectLeakedClosableObjects() {
                throw new RuntimeException("Stub!");
            }

            public Builder detectLeakedRegistrationObjects() {
                throw new RuntimeException("Stub!");
            }

            public Builder penaltyDeath() {
                throw new RuntimeException("Stub!");
            }

            public Builder penaltyLog() {
                throw new RuntimeException("Stub!");
            }

            public Builder penaltyDropBox() {
                throw new RuntimeException("Stub!");
            }

            public VmPolicy build() {
                throw new RuntimeException("Stub!");
            }
        }

        VmPolicy() {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }

    StrictMode() {
        throw new RuntimeException("Stub!");
    }

    public static void setThreadPolicy(ThreadPolicy policy) {
        throw new RuntimeException("Stub!");
    }

    public static ThreadPolicy getThreadPolicy() {
        throw new RuntimeException("Stub!");
    }

    public static ThreadPolicy allowThreadDiskWrites() {
        throw new RuntimeException("Stub!");
    }

    public static ThreadPolicy allowThreadDiskReads() {
        throw new RuntimeException("Stub!");
    }

    public static void setVmPolicy(VmPolicy policy) {
        throw new RuntimeException("Stub!");
    }

    public static VmPolicy getVmPolicy() {
        throw new RuntimeException("Stub!");
    }

    public static void enableDefaults() {
        throw new RuntimeException("Stub!");
    }

    public static void noteSlowCall(String name) {
        throw new RuntimeException("Stub!");
    }
}
