package android.telephony.gsm;

import android.app.PendingIntent;
import java.util.ArrayList;
@Deprecated
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/telephony/gsm/SmsManager.class */
public final class SmsManager {
    @Deprecated
    public static final int STATUS_ON_SIM_FREE = 0;
    @Deprecated
    public static final int STATUS_ON_SIM_READ = 1;
    @Deprecated
    public static final int STATUS_ON_SIM_UNREAD = 3;
    @Deprecated
    public static final int STATUS_ON_SIM_SENT = 5;
    @Deprecated
    public static final int STATUS_ON_SIM_UNSENT = 7;
    @Deprecated
    public static final int RESULT_ERROR_GENERIC_FAILURE = 1;
    @Deprecated
    public static final int RESULT_ERROR_RADIO_OFF = 2;
    @Deprecated
    public static final int RESULT_ERROR_NULL_PDU = 3;
    @Deprecated
    public static final int RESULT_ERROR_NO_SERVICE = 4;

    SmsManager() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static final SmsManager getDefault() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public final void sendTextMessage(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public final ArrayList<String> divideMessage(String text) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public final void sendMultipartTextMessage(String destinationAddress, String scAddress, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public final void sendDataMessage(String destinationAddress, String scAddress, short destinationPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        throw new RuntimeException("Stub!");
    }
}
