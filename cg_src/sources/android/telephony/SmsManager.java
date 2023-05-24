package android.telephony;

import android.app.PendingIntent;
import java.util.ArrayList;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/telephony/SmsManager.class */
public final class SmsManager {
    public static final int STATUS_ON_ICC_FREE = 0;
    public static final int STATUS_ON_ICC_READ = 1;
    public static final int STATUS_ON_ICC_UNREAD = 3;
    public static final int STATUS_ON_ICC_SENT = 5;
    public static final int STATUS_ON_ICC_UNSENT = 7;
    public static final int RESULT_ERROR_GENERIC_FAILURE = 1;
    public static final int RESULT_ERROR_RADIO_OFF = 2;
    public static final int RESULT_ERROR_NULL_PDU = 3;
    public static final int RESULT_ERROR_NO_SERVICE = 4;

    SmsManager() {
        throw new RuntimeException("Stub!");
    }

    public void sendTextMessage(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        throw new RuntimeException("Stub!");
    }

    public ArrayList<String> divideMessage(String text) {
        throw new RuntimeException("Stub!");
    }

    public void sendMultipartTextMessage(String destinationAddress, String scAddress, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents) {
        throw new RuntimeException("Stub!");
    }

    public void sendDataMessage(String destinationAddress, String scAddress, short destinationPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        throw new RuntimeException("Stub!");
    }

    public static SmsManager getDefault() {
        throw new RuntimeException("Stub!");
    }
}
