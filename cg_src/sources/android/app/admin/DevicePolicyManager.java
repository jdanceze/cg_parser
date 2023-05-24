package android.app.admin;

import android.content.ComponentName;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/admin/DevicePolicyManager.class */
public class DevicePolicyManager {
    public static final String ACTION_ADD_DEVICE_ADMIN = "android.app.action.ADD_DEVICE_ADMIN";
    public static final String EXTRA_DEVICE_ADMIN = "android.app.extra.DEVICE_ADMIN";
    public static final String EXTRA_ADD_EXPLANATION = "android.app.extra.ADD_EXPLANATION";
    public static final String ACTION_SET_NEW_PASSWORD = "android.app.action.SET_NEW_PASSWORD";
    public static final int PASSWORD_QUALITY_UNSPECIFIED = 0;
    public static final int PASSWORD_QUALITY_BIOMETRIC_WEAK = 32768;
    public static final int PASSWORD_QUALITY_SOMETHING = 65536;
    public static final int PASSWORD_QUALITY_NUMERIC = 131072;
    public static final int PASSWORD_QUALITY_ALPHABETIC = 262144;
    public static final int PASSWORD_QUALITY_ALPHANUMERIC = 327680;
    public static final int PASSWORD_QUALITY_COMPLEX = 393216;
    public static final int RESET_PASSWORD_REQUIRE_ENTRY = 1;
    public static final int WIPE_EXTERNAL_STORAGE = 1;
    public static final int ENCRYPTION_STATUS_UNSUPPORTED = 0;
    public static final int ENCRYPTION_STATUS_INACTIVE = 1;
    public static final int ENCRYPTION_STATUS_ACTIVATING = 2;
    public static final int ENCRYPTION_STATUS_ACTIVE = 3;
    public static final String ACTION_START_ENCRYPTION = "android.app.action.START_ENCRYPTION";

    DevicePolicyManager() {
        throw new RuntimeException("Stub!");
    }

    public boolean isAdminActive(ComponentName who) {
        throw new RuntimeException("Stub!");
    }

    public List<ComponentName> getActiveAdmins() {
        throw new RuntimeException("Stub!");
    }

    public void removeActiveAdmin(ComponentName who) {
        throw new RuntimeException("Stub!");
    }

    public boolean hasGrantedPolicy(ComponentName admin, int usesPolicy) {
        throw new RuntimeException("Stub!");
    }

    public void setPasswordQuality(ComponentName admin, int quality) {
        throw new RuntimeException("Stub!");
    }

    public int getPasswordQuality(ComponentName admin) {
        throw new RuntimeException("Stub!");
    }

    public void setPasswordMinimumLength(ComponentName admin, int length) {
        throw new RuntimeException("Stub!");
    }

    public int getPasswordMinimumLength(ComponentName admin) {
        throw new RuntimeException("Stub!");
    }

    public void setPasswordMinimumUpperCase(ComponentName admin, int length) {
        throw new RuntimeException("Stub!");
    }

    public int getPasswordMinimumUpperCase(ComponentName admin) {
        throw new RuntimeException("Stub!");
    }

    public void setPasswordMinimumLowerCase(ComponentName admin, int length) {
        throw new RuntimeException("Stub!");
    }

    public int getPasswordMinimumLowerCase(ComponentName admin) {
        throw new RuntimeException("Stub!");
    }

    public void setPasswordMinimumLetters(ComponentName admin, int length) {
        throw new RuntimeException("Stub!");
    }

    public int getPasswordMinimumLetters(ComponentName admin) {
        throw new RuntimeException("Stub!");
    }

    public void setPasswordMinimumNumeric(ComponentName admin, int length) {
        throw new RuntimeException("Stub!");
    }

    public int getPasswordMinimumNumeric(ComponentName admin) {
        throw new RuntimeException("Stub!");
    }

    public void setPasswordMinimumSymbols(ComponentName admin, int length) {
        throw new RuntimeException("Stub!");
    }

    public int getPasswordMinimumSymbols(ComponentName admin) {
        throw new RuntimeException("Stub!");
    }

    public void setPasswordMinimumNonLetter(ComponentName admin, int length) {
        throw new RuntimeException("Stub!");
    }

    public int getPasswordMinimumNonLetter(ComponentName admin) {
        throw new RuntimeException("Stub!");
    }

    public void setPasswordHistoryLength(ComponentName admin, int length) {
        throw new RuntimeException("Stub!");
    }

    public void setPasswordExpirationTimeout(ComponentName admin, long timeout) {
        throw new RuntimeException("Stub!");
    }

    public long getPasswordExpirationTimeout(ComponentName admin) {
        throw new RuntimeException("Stub!");
    }

    public long getPasswordExpiration(ComponentName admin) {
        throw new RuntimeException("Stub!");
    }

    public int getPasswordHistoryLength(ComponentName admin) {
        throw new RuntimeException("Stub!");
    }

    public int getPasswordMaximumLength(int quality) {
        throw new RuntimeException("Stub!");
    }

    public boolean isActivePasswordSufficient() {
        throw new RuntimeException("Stub!");
    }

    public int getCurrentFailedPasswordAttempts() {
        throw new RuntimeException("Stub!");
    }

    public void setMaximumFailedPasswordsForWipe(ComponentName admin, int num) {
        throw new RuntimeException("Stub!");
    }

    public int getMaximumFailedPasswordsForWipe(ComponentName admin) {
        throw new RuntimeException("Stub!");
    }

    public boolean resetPassword(String password, int flags) {
        throw new RuntimeException("Stub!");
    }

    public void setMaximumTimeToLock(ComponentName admin, long timeMs) {
        throw new RuntimeException("Stub!");
    }

    public long getMaximumTimeToLock(ComponentName admin) {
        throw new RuntimeException("Stub!");
    }

    public void lockNow() {
        throw new RuntimeException("Stub!");
    }

    public void wipeData(int flags) {
        throw new RuntimeException("Stub!");
    }

    public int setStorageEncryption(ComponentName admin, boolean encrypt) {
        throw new RuntimeException("Stub!");
    }

    public boolean getStorageEncryption(ComponentName admin) {
        throw new RuntimeException("Stub!");
    }

    public int getStorageEncryptionStatus() {
        throw new RuntimeException("Stub!");
    }

    public void setCameraDisabled(ComponentName admin, boolean disabled) {
        throw new RuntimeException("Stub!");
    }

    public boolean getCameraDisabled(ComponentName admin) {
        throw new RuntimeException("Stub!");
    }
}
