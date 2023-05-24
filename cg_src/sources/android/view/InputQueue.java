package android.view;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/InputQueue.class */
public final class InputQueue {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/InputQueue$Callback.class */
    public interface Callback {
        void onInputQueueCreated(InputQueue inputQueue);

        void onInputQueueDestroyed(InputQueue inputQueue);
    }

    InputQueue() {
        throw new RuntimeException("Stub!");
    }
}
