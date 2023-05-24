package android.telephony;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/telephony/SmsMessage.class */
public class SmsMessage {
    public static final int ENCODING_UNKNOWN = 0;
    public static final int ENCODING_7BIT = 1;
    public static final int ENCODING_8BIT = 2;
    public static final int ENCODING_16BIT = 3;
    public static final int MAX_USER_DATA_BYTES = 140;
    public static final int MAX_USER_DATA_BYTES_WITH_HEADER = 134;
    public static final int MAX_USER_DATA_SEPTETS = 160;
    public static final int MAX_USER_DATA_SEPTETS_WITH_HEADER = 153;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/telephony/SmsMessage$MessageClass.class */
    public enum MessageClass {
        CLASS_0,
        CLASS_1,
        CLASS_2,
        CLASS_3,
        UNKNOWN
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/telephony/SmsMessage$SubmitPdu.class */
    public static class SubmitPdu {
        public byte[] encodedScAddress = null;
        public byte[] encodedMessage = null;

        SubmitPdu() {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }

    SmsMessage() {
        throw new RuntimeException("Stub!");
    }

    public static SmsMessage createFromPdu(byte[] pdu) {
        throw new RuntimeException("Stub!");
    }

    public static int getTPLayerLengthForPDU(String pdu) {
        throw new RuntimeException("Stub!");
    }

    public static int[] calculateLength(CharSequence msgBody, boolean use7bitOnly) {
        throw new RuntimeException("Stub!");
    }

    public static int[] calculateLength(String messageBody, boolean use7bitOnly) {
        throw new RuntimeException("Stub!");
    }

    public static SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, String message, boolean statusReportRequested) {
        throw new RuntimeException("Stub!");
    }

    public static SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, short destinationPort, byte[] data, boolean statusReportRequested) {
        throw new RuntimeException("Stub!");
    }

    public String getServiceCenterAddress() {
        throw new RuntimeException("Stub!");
    }

    public String getOriginatingAddress() {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayOriginatingAddress() {
        throw new RuntimeException("Stub!");
    }

    public String getMessageBody() {
        throw new RuntimeException("Stub!");
    }

    public MessageClass getMessageClass() {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayMessageBody() {
        throw new RuntimeException("Stub!");
    }

    public String getPseudoSubject() {
        throw new RuntimeException("Stub!");
    }

    public long getTimestampMillis() {
        throw new RuntimeException("Stub!");
    }

    public boolean isEmail() {
        throw new RuntimeException("Stub!");
    }

    public String getEmailBody() {
        throw new RuntimeException("Stub!");
    }

    public String getEmailFrom() {
        throw new RuntimeException("Stub!");
    }

    public int getProtocolIdentifier() {
        throw new RuntimeException("Stub!");
    }

    public boolean isReplace() {
        throw new RuntimeException("Stub!");
    }

    public boolean isCphsMwiMessage() {
        throw new RuntimeException("Stub!");
    }

    public boolean isMWIClearMessage() {
        throw new RuntimeException("Stub!");
    }

    public boolean isMWISetMessage() {
        throw new RuntimeException("Stub!");
    }

    public boolean isMwiDontStore() {
        throw new RuntimeException("Stub!");
    }

    public byte[] getUserData() {
        throw new RuntimeException("Stub!");
    }

    public byte[] getPdu() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getStatusOnSim() {
        throw new RuntimeException("Stub!");
    }

    public int getStatusOnIcc() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getIndexOnSim() {
        throw new RuntimeException("Stub!");
    }

    public int getIndexOnIcc() {
        throw new RuntimeException("Stub!");
    }

    public int getStatus() {
        throw new RuntimeException("Stub!");
    }

    public boolean isStatusReportMessage() {
        throw new RuntimeException("Stub!");
    }

    public boolean isReplyPathPresent() {
        throw new RuntimeException("Stub!");
    }
}
