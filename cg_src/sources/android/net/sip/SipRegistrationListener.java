package android.net.sip;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/sip/SipRegistrationListener.class */
public interface SipRegistrationListener {
    void onRegistering(String str);

    void onRegistrationDone(String str, long j);

    void onRegistrationFailed(String str, int i, String str2);
}
