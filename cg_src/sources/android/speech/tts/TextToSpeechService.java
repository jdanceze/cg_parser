package android.speech.tts;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/speech/tts/TextToSpeechService.class */
public abstract class TextToSpeechService extends Service {
    protected abstract int onIsLanguageAvailable(String str, String str2, String str3);

    protected abstract String[] onGetLanguage();

    protected abstract int onLoadLanguage(String str, String str2, String str3);

    protected abstract void onStop();

    protected abstract void onSynthesizeText(SynthesisRequest synthesisRequest, SynthesisCallback synthesisCallback);

    public TextToSpeechService() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public void onCreate() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public void onDestroy() {
        throw new RuntimeException("Stub!");
    }

    protected Set<String> onGetFeaturesForLanguage(String lang, String country, String variant) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        throw new RuntimeException("Stub!");
    }
}
