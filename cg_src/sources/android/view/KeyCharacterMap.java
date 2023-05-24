package android.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.AndroidRuntimeException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/KeyCharacterMap.class */
public class KeyCharacterMap implements Parcelable {
    @Deprecated
    public static final int BUILT_IN_KEYBOARD = 0;
    public static final int VIRTUAL_KEYBOARD = -1;
    public static final int NUMERIC = 1;
    public static final int PREDICTIVE = 2;
    public static final int ALPHA = 3;
    public static final int FULL = 4;
    public static final int SPECIAL_FUNCTION = 5;
    public static final char HEX_INPUT = 61184;
    public static final char PICKER_DIALOG_INPUT = 61185;
    public static final int MODIFIER_BEHAVIOR_CHORDED = 0;
    public static final int MODIFIER_BEHAVIOR_CHORDED_OR_TOGGLED = 1;
    public static final int COMBINING_ACCENT = Integer.MIN_VALUE;
    public static final int COMBINING_ACCENT_MASK = Integer.MAX_VALUE;
    public static final Parcelable.Creator<KeyCharacterMap> CREATOR = null;

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/KeyCharacterMap$KeyData.class */
    public static class KeyData {
        public static final int META_LENGTH = 4;
        public char displayLabel;
        public char number;
        public char[] meta = null;

        public KeyData() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/KeyCharacterMap$UnavailableException.class */
    public static class UnavailableException extends AndroidRuntimeException {
        public UnavailableException(String msg) {
            throw new RuntimeException("Stub!");
        }
    }

    KeyCharacterMap() {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }

    public static KeyCharacterMap load(int deviceId) {
        throw new RuntimeException("Stub!");
    }

    public int get(int keyCode, int metaState) {
        throw new RuntimeException("Stub!");
    }

    public char getNumber(int keyCode) {
        throw new RuntimeException("Stub!");
    }

    public char getMatch(int keyCode, char[] chars) {
        throw new RuntimeException("Stub!");
    }

    public char getMatch(int keyCode, char[] chars, int metaState) {
        throw new RuntimeException("Stub!");
    }

    public char getDisplayLabel(int keyCode) {
        throw new RuntimeException("Stub!");
    }

    public static int getDeadChar(int accent, int c) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean getKeyData(int keyCode, KeyData results) {
        throw new RuntimeException("Stub!");
    }

    public KeyEvent[] getEvents(char[] chars) {
        throw new RuntimeException("Stub!");
    }

    public boolean isPrintingKey(int keyCode) {
        throw new RuntimeException("Stub!");
    }

    public int getKeyboardType() {
        throw new RuntimeException("Stub!");
    }

    public int getModifierBehavior() {
        throw new RuntimeException("Stub!");
    }

    public static boolean deviceHasKey(int keyCode) {
        throw new RuntimeException("Stub!");
    }

    public static boolean[] deviceHasKeys(int[] keyCodes) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }
}
