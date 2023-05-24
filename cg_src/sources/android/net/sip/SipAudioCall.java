package android.net.sip;

import android.content.Context;
import android.os.Message;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/sip/SipAudioCall.class */
public class SipAudioCall {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/sip/SipAudioCall$Listener.class */
    public static class Listener {
        public Listener() {
            throw new RuntimeException("Stub!");
        }

        public void onReadyToCall(SipAudioCall call) {
            throw new RuntimeException("Stub!");
        }

        public void onCalling(SipAudioCall call) {
            throw new RuntimeException("Stub!");
        }

        public void onRinging(SipAudioCall call, SipProfile caller) {
            throw new RuntimeException("Stub!");
        }

        public void onRingingBack(SipAudioCall call) {
            throw new RuntimeException("Stub!");
        }

        public void onCallEstablished(SipAudioCall call) {
            throw new RuntimeException("Stub!");
        }

        public void onCallEnded(SipAudioCall call) {
            throw new RuntimeException("Stub!");
        }

        public void onCallBusy(SipAudioCall call) {
            throw new RuntimeException("Stub!");
        }

        public void onCallHeld(SipAudioCall call) {
            throw new RuntimeException("Stub!");
        }

        public void onError(SipAudioCall call, int errorCode, String errorMessage) {
            throw new RuntimeException("Stub!");
        }

        public void onChanged(SipAudioCall call) {
            throw new RuntimeException("Stub!");
        }
    }

    public SipAudioCall(Context context, SipProfile localProfile) {
        throw new RuntimeException("Stub!");
    }

    public void setListener(Listener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setListener(Listener listener, boolean callbackImmediately) {
        throw new RuntimeException("Stub!");
    }

    public boolean isInCall() {
        throw new RuntimeException("Stub!");
    }

    public boolean isOnHold() {
        throw new RuntimeException("Stub!");
    }

    public void close() {
        throw new RuntimeException("Stub!");
    }

    public SipProfile getLocalProfile() {
        throw new RuntimeException("Stub!");
    }

    public SipProfile getPeerProfile() {
        throw new RuntimeException("Stub!");
    }

    public int getState() {
        throw new RuntimeException("Stub!");
    }

    public void attachCall(SipSession session, String sessionDescription) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public void makeCall(SipProfile peerProfile, SipSession sipSession, int timeout) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public void endCall() throws SipException {
        throw new RuntimeException("Stub!");
    }

    public void holdCall(int timeout) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public void answerCall(int timeout) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public void continueCall(int timeout) throws SipException {
        throw new RuntimeException("Stub!");
    }

    public void toggleMute() {
        throw new RuntimeException("Stub!");
    }

    public boolean isMuted() {
        throw new RuntimeException("Stub!");
    }

    public void setSpeakerMode(boolean speakerMode) {
        throw new RuntimeException("Stub!");
    }

    public void sendDtmf(int code) {
        throw new RuntimeException("Stub!");
    }

    public void sendDtmf(int code, Message result) {
        throw new RuntimeException("Stub!");
    }

    public void startAudio() {
        throw new RuntimeException("Stub!");
    }
}
