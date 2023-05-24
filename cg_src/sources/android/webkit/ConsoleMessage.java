package android.webkit;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/ConsoleMessage.class */
public class ConsoleMessage {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/ConsoleMessage$MessageLevel.class */
    public enum MessageLevel {
        DEBUG,
        ERROR,
        LOG,
        TIP,
        WARNING
    }

    public ConsoleMessage(String message, String sourceId, int lineNumber, MessageLevel msgLevel) {
        throw new RuntimeException("Stub!");
    }

    public MessageLevel messageLevel() {
        throw new RuntimeException("Stub!");
    }

    public String message() {
        throw new RuntimeException("Stub!");
    }

    public String sourceId() {
        throw new RuntimeException("Stub!");
    }

    public int lineNumber() {
        throw new RuntimeException("Stub!");
    }
}
