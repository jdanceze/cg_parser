package android.view.textservice;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/textservice/SpellCheckerSession.class */
public class SpellCheckerSession {
    public static final String SERVICE_META_DATA = "android.view.textservice.scs";

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/textservice/SpellCheckerSession$SpellCheckerSessionListener.class */
    public interface SpellCheckerSessionListener {
        void onGetSuggestions(SuggestionsInfo[] suggestionsInfoArr);

        void onGetSentenceSuggestions(SentenceSuggestionsInfo[] sentenceSuggestionsInfoArr);
    }

    SpellCheckerSession() {
        throw new RuntimeException("Stub!");
    }

    public boolean isSessionDisconnected() {
        throw new RuntimeException("Stub!");
    }

    public SpellCheckerInfo getSpellChecker() {
        throw new RuntimeException("Stub!");
    }

    public void cancel() {
        throw new RuntimeException("Stub!");
    }

    public void close() {
        throw new RuntimeException("Stub!");
    }

    public void getSentenceSuggestions(TextInfo[] textInfos, int suggestionsLimit) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void getSuggestions(TextInfo textInfo, int suggestionsLimit) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void getSuggestions(TextInfo[] textInfos, int suggestionsLimit, boolean sequentialWords) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
}
