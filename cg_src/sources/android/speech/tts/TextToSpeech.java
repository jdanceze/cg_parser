package android.speech.tts;

import android.content.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/speech/tts/TextToSpeech.class */
public class TextToSpeech {
    public static final int SUCCESS = 0;
    public static final int ERROR = -1;
    public static final int QUEUE_FLUSH = 0;
    public static final int QUEUE_ADD = 1;
    public static final int LANG_COUNTRY_VAR_AVAILABLE = 2;
    public static final int LANG_COUNTRY_AVAILABLE = 1;
    public static final int LANG_AVAILABLE = 0;
    public static final int LANG_MISSING_DATA = -1;
    public static final int LANG_NOT_SUPPORTED = -2;
    public static final String ACTION_TTS_QUEUE_PROCESSING_COMPLETED = "android.speech.tts.TTS_QUEUE_PROCESSING_COMPLETED";

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/speech/tts/TextToSpeech$OnInitListener.class */
    public interface OnInitListener {
        void onInit(int i);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/speech/tts/TextToSpeech$OnUtteranceCompletedListener.class */
    public interface OnUtteranceCompletedListener {
        void onUtteranceCompleted(String str);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/speech/tts/TextToSpeech$Engine.class */
    public class Engine {
        public static final int DEFAULT_STREAM = 3;
        public static final int CHECK_VOICE_DATA_PASS = 1;
        public static final int CHECK_VOICE_DATA_FAIL = 0;
        public static final int CHECK_VOICE_DATA_BAD_DATA = -1;
        public static final int CHECK_VOICE_DATA_MISSING_DATA = -2;
        public static final int CHECK_VOICE_DATA_MISSING_VOLUME = -3;
        public static final String INTENT_ACTION_TTS_SERVICE = "android.intent.action.TTS_SERVICE";
        public static final String SERVICE_META_DATA = "android.speech.tts";
        public static final String ACTION_INSTALL_TTS_DATA = "android.speech.tts.engine.INSTALL_TTS_DATA";
        public static final String ACTION_TTS_DATA_INSTALLED = "android.speech.tts.engine.TTS_DATA_INSTALLED";
        public static final String ACTION_CHECK_TTS_DATA = "android.speech.tts.engine.CHECK_TTS_DATA";
        public static final String EXTRA_VOICE_DATA_ROOT_DIRECTORY = "dataRoot";
        public static final String EXTRA_VOICE_DATA_FILES = "dataFiles";
        public static final String EXTRA_VOICE_DATA_FILES_INFO = "dataFilesInfo";
        public static final String EXTRA_AVAILABLE_VOICES = "availableVoices";
        public static final String EXTRA_UNAVAILABLE_VOICES = "unavailableVoices";
        public static final String EXTRA_CHECK_VOICE_DATA_FOR = "checkVoiceDataFor";
        public static final String EXTRA_TTS_DATA_INSTALLED = "dataInstalled";
        public static final String KEY_PARAM_STREAM = "streamType";
        public static final String KEY_PARAM_UTTERANCE_ID = "utteranceId";
        public static final String KEY_PARAM_VOLUME = "volume";
        public static final String KEY_PARAM_PAN = "pan";
        public static final String KEY_FEATURE_NETWORK_SYNTHESIS = "networkTts";
        public static final String KEY_FEATURE_EMBEDDED_SYNTHESIS = "embeddedTts";

        public Engine() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/speech/tts/TextToSpeech$EngineInfo.class */
    public static class EngineInfo {
        public String name;
        public String label;
        public int icon;

        public EngineInfo() {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }

    public TextToSpeech(Context context, OnInitListener listener) {
        throw new RuntimeException("Stub!");
    }

    public TextToSpeech(Context context, OnInitListener listener, String engine) {
        throw new RuntimeException("Stub!");
    }

    public void shutdown() {
        throw new RuntimeException("Stub!");
    }

    public int addSpeech(String text, String packagename, int resourceId) {
        throw new RuntimeException("Stub!");
    }

    public int addSpeech(String text, String filename) {
        throw new RuntimeException("Stub!");
    }

    public int addEarcon(String earcon, String packagename, int resourceId) {
        throw new RuntimeException("Stub!");
    }

    public int addEarcon(String earcon, String filename) {
        throw new RuntimeException("Stub!");
    }

    public int speak(String text, int queueMode, HashMap<String, String> params) {
        throw new RuntimeException("Stub!");
    }

    public int playEarcon(String earcon, int queueMode, HashMap<String, String> params) {
        throw new RuntimeException("Stub!");
    }

    public int playSilence(long durationInMs, int queueMode, HashMap<String, String> params) {
        throw new RuntimeException("Stub!");
    }

    public Set<String> getFeatures(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public boolean isSpeaking() {
        throw new RuntimeException("Stub!");
    }

    public int stop() {
        throw new RuntimeException("Stub!");
    }

    public int setSpeechRate(float speechRate) {
        throw new RuntimeException("Stub!");
    }

    public int setPitch(float pitch) {
        throw new RuntimeException("Stub!");
    }

    public int setLanguage(Locale loc) {
        throw new RuntimeException("Stub!");
    }

    public Locale getLanguage() {
        throw new RuntimeException("Stub!");
    }

    public int isLanguageAvailable(Locale loc) {
        throw new RuntimeException("Stub!");
    }

    public int synthesizeToFile(String text, HashMap<String, String> params, String filename) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int setOnUtteranceCompletedListener(OnUtteranceCompletedListener listener) {
        throw new RuntimeException("Stub!");
    }

    public int setOnUtteranceProgressListener(UtteranceProgressListener listener) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int setEngineByPackageName(String enginePackageName) {
        throw new RuntimeException("Stub!");
    }

    public String getDefaultEngine() {
        throw new RuntimeException("Stub!");
    }

    public boolean areDefaultsEnforced() {
        throw new RuntimeException("Stub!");
    }

    public List<EngineInfo> getEngines() {
        throw new RuntimeException("Stub!");
    }
}
