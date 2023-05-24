package android.telephony.gsm;
@Deprecated
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/telephony/gsm/SmsMessage.class */
public class SmsMessage {
    @Deprecated
    public static final int ENCODING_UNKNOWN = 0;
    @Deprecated
    public static final int ENCODING_7BIT = 1;
    @Deprecated
    public static final int ENCODING_8BIT = 2;
    @Deprecated
    public static final int ENCODING_16BIT = 3;
    @Deprecated
    public static final int MAX_USER_DATA_BYTES = 140;
    @Deprecated
    public static final int MAX_USER_DATA_SEPTETS = 160;
    @Deprecated
    public static final int MAX_USER_DATA_SEPTETS_WITH_HEADER = 153;

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/telephony/gsm/SmsMessage$MessageClass.class */
    public enum MessageClass {
        CLASS_0,
        CLASS_1,
        CLASS_2,
        CLASS_3,
        UNKNOWN
    }

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/telephony/gsm/SmsMessage$SubmitPdu.class */
    public static class SubmitPdu {
        @Deprecated
        public byte[] encodedScAddress = null;
        @Deprecated
        public byte[] encodedMessage = null;

        @Deprecated
        public SubmitPdu() {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }

    @Deprecated
    public SmsMessage() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static SmsMessage createFromPdu(byte[] pdu) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static int getTPLayerLengthForPDU(String pdu) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static int[] calculateLength(CharSequence messageBody, boolean use7bitOnly) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static int[] calculateLength(String messageBody, boolean use7bitOnly) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, String message, boolean statusReportRequested) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, short destinationPort, byte[] data, boolean statusReportRequested) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public String getServiceCenterAddress() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public String getOriginatingAddress() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public String getDisplayOriginatingAddress() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public String getMessageBody() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public MessageClass getMessageClass() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public String getDisplayMessageBody() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public String getPseudoSubject() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public long getTimestampMillis() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isEmail() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public String getEmailBody() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public String getEmailFrom() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getProtocolIdentifier() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isReplace() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isCphsMwiMessage() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isMWIClearMessage() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isMWISetMessage() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isMwiDontStore() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public byte[] getUserData() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public byte[] getPdu() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getStatusOnSim() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getIndexOnSim() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getStatus() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isStatusReportMessage() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isReplyPathPresent() {
        throw new RuntimeException("Stub!");
    }
}
