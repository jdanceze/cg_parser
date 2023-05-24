package android.security;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/security/KeyChain.class */
public final class KeyChain {
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_CERTIFICATE = "CERT";
    public static final String EXTRA_PKCS12 = "PKCS12";
    public static final String ACTION_STORAGE_CHANGED = "android.security.STORAGE_CHANGED";

    public KeyChain() {
        throw new RuntimeException("Stub!");
    }

    public static Intent createInstallIntent() {
        throw new RuntimeException("Stub!");
    }

    public static void choosePrivateKeyAlias(Activity activity, KeyChainAliasCallback response, String[] keyTypes, Principal[] issuers, String host, int port, String alias) {
        throw new RuntimeException("Stub!");
    }

    public static PrivateKey getPrivateKey(Context context, String alias) throws KeyChainException, InterruptedException {
        throw new RuntimeException("Stub!");
    }

    public static X509Certificate[] getCertificateChain(Context context, String alias) throws KeyChainException, InterruptedException {
        throw new RuntimeException("Stub!");
    }
}
