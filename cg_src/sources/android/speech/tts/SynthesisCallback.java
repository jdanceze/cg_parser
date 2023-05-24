package android.speech.tts;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/speech/tts/SynthesisCallback.class */
public interface SynthesisCallback {
    int getMaxBufferSize();

    int start(int i, int i2, int i3);

    int audioAvailable(byte[] bArr, int i, int i2);

    int done();

    void error();
}
