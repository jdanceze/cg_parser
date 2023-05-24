package android.renderscript;

import android.content.Context;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/RenderScript.class */
public class RenderScript {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/RenderScript$Priority.class */
    public enum Priority {
        LOW,
        NORMAL
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/RenderScript$RSMessageHandler.class */
    public static class RSMessageHandler implements Runnable {
        protected int[] mData = null;
        protected int mID;
        protected int mLength;

        public RSMessageHandler() {
            throw new RuntimeException("Stub!");
        }

        @Override // java.lang.Runnable
        public void run() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/RenderScript$RSErrorHandler.class */
    public static class RSErrorHandler implements Runnable {
        protected String mErrorMessage;
        protected int mErrorNum;

        public RSErrorHandler() {
            throw new RuntimeException("Stub!");
        }

        @Override // java.lang.Runnable
        public void run() {
            throw new RuntimeException("Stub!");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RenderScript() {
        throw new RuntimeException("Stub!");
    }

    public void setMessageHandler(RSMessageHandler msg) {
        throw new RuntimeException("Stub!");
    }

    public RSMessageHandler getMessageHandler() {
        throw new RuntimeException("Stub!");
    }

    public void setErrorHandler(RSErrorHandler msg) {
        throw new RuntimeException("Stub!");
    }

    public RSErrorHandler getErrorHandler() {
        throw new RuntimeException("Stub!");
    }

    public void setPriority(Priority p) {
        throw new RuntimeException("Stub!");
    }

    public final Context getApplicationContext() {
        throw new RuntimeException("Stub!");
    }

    public static RenderScript create(Context ctx) {
        throw new RuntimeException("Stub!");
    }

    public void contextDump() {
        throw new RuntimeException("Stub!");
    }

    public void finish() {
        throw new RuntimeException("Stub!");
    }

    public void destroy() {
        throw new RuntimeException("Stub!");
    }
}
