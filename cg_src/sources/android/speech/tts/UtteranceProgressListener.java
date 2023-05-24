package android.speech.tts;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/speech/tts/UtteranceProgressListener.class */
public abstract class UtteranceProgressListener {
    public abstract void onStart(String str);

    public abstract void onDone(String str);

    public abstract void onError(String str);

    public UtteranceProgressListener() {
        throw new RuntimeException("Stub!");
    }
}
