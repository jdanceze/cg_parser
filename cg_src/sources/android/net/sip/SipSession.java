package android.net.sip;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/sip/SipSession.class */
public final class SipSession {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/sip/SipSession$State.class */
    public static class State {
        public static final int READY_TO_CALL = 0;
        public static final int REGISTERING = 1;
        public static final int DEREGISTERING = 2;
        public static final int INCOMING_CALL = 3;
        public static final int INCOMING_CALL_ANSWERING = 4;
        public static final int OUTGOING_CALL = 5;
        public static final int OUTGOING_CALL_RING_BACK = 6;
        public static final int OUTGOING_CALL_CANCELING = 7;
        public static final int IN_CALL = 8;
        public static final int PINGING = 9;
        public static final int NOT_DEFINED = 101;

        State() {
            throw new RuntimeException("Stub!");
        }

        public static String toString(int state) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/sip/SipSession$Listener.class */
    public static class Listener {
        public Listener() {
            throw new RuntimeException("Stub!");
        }

        public void onCalling(SipSession session) {
            throw new RuntimeException("Stub!");
        }

        public void onRinging(SipSession session, SipProfile caller, String sessionDescription) {
            throw new RuntimeException("Stub!");
        }

        public void onRingingBack(SipSession session) {
            throw new RuntimeException("Stub!");
        }

        public void onCallEstablished(SipSession session, String sessionDescription) {
            throw new RuntimeException("Stub!");
        }

        public void onCallEnded(SipSession session) {
            throw new RuntimeException("Stub!");
        }

        public void onCallBusy(SipSession session) {
            throw new RuntimeException("Stub!");
        }

        public void onError(SipSession session, int errorCode, String errorMessage) {
            throw new RuntimeException("Stub!");
        }

        public void onCallChangeFailed(SipSession session, int errorCode, String errorMessage) {
            throw new RuntimeException("Stub!");
        }

        public void onRegistering(SipSession session) {
            throw new RuntimeException("Stub!");
        }

        public void onRegistrationDone(SipSession session, int duration) {
            throw new RuntimeException("Stub!");
        }

        public void onRegistrationFailed(SipSession session, int errorCode, String errorMessage) {
            throw new RuntimeException("Stub!");
        }

        public void onRegistrationTimeout(SipSession session) {
            throw new RuntimeException("Stub!");
        }
    }

    SipSession() {
        throw new RuntimeException("Stub!");
    }

    public String getLocalIp() {
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

    public boolean isInCall() {
        throw new RuntimeException("Stub!");
    }

    public String getCallId() {
        throw new RuntimeException("Stub!");
    }

    public void setListener(Listener listener) {
        throw new RuntimeException("Stub!");
    }

    public void register(int duration) {
        throw new RuntimeException("Stub!");
    }

    public void unregister() {
        throw new RuntimeException("Stub!");
    }

    public void makeCall(SipProfile callee, String sessionDescription, int timeout) {
        throw new RuntimeException("Stub!");
    }

    public void answerCall(String sessionDescription, int timeout) {
        throw new RuntimeException("Stub!");
    }

    public void endCall() {
        throw new RuntimeException("Stub!");
    }

    public void changeCall(String sessionDescription, int timeout) {
        throw new RuntimeException("Stub!");
    }
}
