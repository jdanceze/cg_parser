package android.speech;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/speech/SpeechRecognizer.class */
public class SpeechRecognizer {
    public static final String RESULTS_RECOGNITION = "results_recognition";
    public static final String CONFIDENCE_SCORES = "confidence_scores";
    public static final int ERROR_NETWORK_TIMEOUT = 1;
    public static final int ERROR_NETWORK = 2;
    public static final int ERROR_AUDIO = 3;
    public static final int ERROR_SERVER = 4;
    public static final int ERROR_CLIENT = 5;
    public static final int ERROR_SPEECH_TIMEOUT = 6;
    public static final int ERROR_NO_MATCH = 7;
    public static final int ERROR_RECOGNIZER_BUSY = 8;
    public static final int ERROR_INSUFFICIENT_PERMISSIONS = 9;

    SpeechRecognizer() {
        throw new RuntimeException("Stub!");
    }

    public static boolean isRecognitionAvailable(Context context) {
        throw new RuntimeException("Stub!");
    }

    public static SpeechRecognizer createSpeechRecognizer(Context context) {
        throw new RuntimeException("Stub!");
    }

    public static SpeechRecognizer createSpeechRecognizer(Context context, ComponentName serviceComponent) {
        throw new RuntimeException("Stub!");
    }

    public void setRecognitionListener(RecognitionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void startListening(Intent recognizerIntent) {
        throw new RuntimeException("Stub!");
    }

    public void stopListening() {
        throw new RuntimeException("Stub!");
    }

    public void cancel() {
        throw new RuntimeException("Stub!");
    }

    public void destroy() {
        throw new RuntimeException("Stub!");
    }
}
