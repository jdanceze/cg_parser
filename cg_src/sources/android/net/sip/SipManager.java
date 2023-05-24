package android.net.sip;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipSession;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/sip/SipManager.class */
public class SipManager {
    public static final int INCOMING_CALL_RESULT_CODE = 101;
    public static final String EXTRA_CALL_ID = "android:sipCallID";
    public static final String EXTRA_OFFER_SD = "android:sipOfferSD";

    SipManager() {
        throw new RuntimeException("Stub!");
    }

    public static SipManager newInstance(Context context) {
        throw new RuntimeException("Stub!");
    }

    public static boolean isApiSupported(Context context) {
        throw new RuntimeException("Stub!");
    }

    public static boolean isVoipSupported(Context context) {
        throw new RuntimeException("Stub!");
    }

    public static boolean isSipWifiOnly(Context context) {
        throw new RuntimeException("Stub!");
    }

    public void open(SipProfile localProfile) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public void open(SipProfile localProfile, PendingIntent incomingCallPendingIntent, SipRegistrationListener listener) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public void setRegistrationListener(String localProfileUri, SipRegistrationListener listener) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public void close(String localProfileUri) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public boolean isOpened(String localProfileUri) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public boolean isRegistered(String localProfileUri) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public SipAudioCall makeAudioCall(SipProfile localProfile, SipProfile peerProfile, SipAudioCall.Listener listener, int timeout) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public SipAudioCall makeAudioCall(String localProfileUri, String peerProfileUri, SipAudioCall.Listener listener, int timeout) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public SipAudioCall takeAudioCall(Intent incomingCallIntent, SipAudioCall.Listener listener) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public static boolean isIncomingCallIntent(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    public static String getCallId(Intent incomingCallIntent) {
        throw new RuntimeException("Stub!");
    }

    public static String getOfferSessionDescription(Intent incomingCallIntent) {
        throw new RuntimeException("Stub!");
    }

    public void register(SipProfile localProfile, int expiryTime, SipRegistrationListener listener) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public void unregister(SipProfile localProfile, SipRegistrationListener listener) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public SipSession getSessionFor(Intent incomingCallIntent) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public SipSession createSipSession(SipProfile localProfile, SipSession.Listener listener) throws SipException {
        throw new RuntimeException("Stub!");
    }
}
