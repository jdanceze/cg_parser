package android.speech;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/speech/RecognitionService.class */
public abstract class RecognitionService extends Service {
    public static final String SERVICE_INTERFACE = "android.speech.RecognitionService";
    public static final String SERVICE_META_DATA = "android.speech";

    protected abstract void onStartListening(Intent intent, Callback callback);

    protected abstract void onCancel(Callback callback);

    protected abstract void onStopListening(Callback callback);

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/speech/RecognitionService$Callback.class */
    public class Callback {
        Callback() {
            throw new RuntimeException("Stub!");
        }

        public void beginningOfSpeech() throws RemoteException {
            throw new RuntimeException("Stub!");
        }

        public void bufferReceived(byte[] buffer) throws RemoteException {
            throw new RuntimeException("Stub!");
        }

        public void endOfSpeech() throws RemoteException {
            throw new RuntimeException("Stub!");
        }

        public void error(int error) throws RemoteException {
            throw new RuntimeException("Stub!");
        }

        public void partialResults(Bundle partialResults) throws RemoteException {
            throw new RuntimeException("Stub!");
        }

        public void readyForSpeech(Bundle params) throws RemoteException {
            throw new RuntimeException("Stub!");
        }

        public void results(Bundle results) throws RemoteException {
            throw new RuntimeException("Stub!");
        }

        public void rmsChanged(float rmsdB) throws RemoteException {
            throw new RuntimeException("Stub!");
        }
    }

    public RecognitionService() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public void onDestroy() {
        throw new RuntimeException("Stub!");
    }
}
