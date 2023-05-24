package android.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/nfc/NfcAdapter.class */
public final class NfcAdapter {
    public static final String ACTION_NDEF_DISCOVERED = "android.nfc.action.NDEF_DISCOVERED";
    public static final String ACTION_TECH_DISCOVERED = "android.nfc.action.TECH_DISCOVERED";
    public static final String ACTION_TAG_DISCOVERED = "android.nfc.action.TAG_DISCOVERED";
    public static final String EXTRA_TAG = "android.nfc.extra.TAG";
    public static final String EXTRA_NDEF_MESSAGES = "android.nfc.extra.NDEF_MESSAGES";
    public static final String EXTRA_ID = "android.nfc.extra.ID";

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/nfc/NfcAdapter$CreateBeamUrisCallback.class */
    public interface CreateBeamUrisCallback {
        Uri[] createBeamUris(NfcEvent nfcEvent);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/nfc/NfcAdapter$CreateNdefMessageCallback.class */
    public interface CreateNdefMessageCallback {
        NdefMessage createNdefMessage(NfcEvent nfcEvent);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/nfc/NfcAdapter$OnNdefPushCompleteCallback.class */
    public interface OnNdefPushCompleteCallback {
        void onNdefPushComplete(NfcEvent nfcEvent);
    }

    NfcAdapter() {
        throw new RuntimeException("Stub!");
    }

    public static NfcAdapter getDefaultAdapter(Context context) {
        throw new RuntimeException("Stub!");
    }

    public boolean isEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setBeamPushUris(Uri[] uris, Activity activity) {
        throw new RuntimeException("Stub!");
    }

    public void setBeamPushUrisCallback(CreateBeamUrisCallback callback, Activity activity) {
        throw new RuntimeException("Stub!");
    }

    public void setNdefPushMessage(NdefMessage message, Activity activity, Activity... activities) {
        throw new RuntimeException("Stub!");
    }

    public void setNdefPushMessageCallback(CreateNdefMessageCallback callback, Activity activity, Activity... activities) {
        throw new RuntimeException("Stub!");
    }

    public void setOnNdefPushCompleteCallback(OnNdefPushCompleteCallback callback, Activity activity, Activity... activities) {
        throw new RuntimeException("Stub!");
    }

    public void enableForegroundDispatch(Activity activity, PendingIntent intent, IntentFilter[] filters, String[][] techLists) {
        throw new RuntimeException("Stub!");
    }

    public void disableForegroundDispatch(Activity activity) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void enableForegroundNdefPush(Activity activity, NdefMessage message) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void disableForegroundNdefPush(Activity activity) {
        throw new RuntimeException("Stub!");
    }

    public boolean isNdefPushEnabled() {
        throw new RuntimeException("Stub!");
    }
}
