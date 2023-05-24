package android.view.inputmethod;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Printer;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/inputmethod/EditorInfo.class */
public class EditorInfo implements InputType, Parcelable {
    public int inputType;
    public static final int IME_MASK_ACTION = 255;
    public static final int IME_ACTION_UNSPECIFIED = 0;
    public static final int IME_ACTION_NONE = 1;
    public static final int IME_ACTION_GO = 2;
    public static final int IME_ACTION_SEARCH = 3;
    public static final int IME_ACTION_SEND = 4;
    public static final int IME_ACTION_NEXT = 5;
    public static final int IME_ACTION_DONE = 6;
    public static final int IME_ACTION_PREVIOUS = 7;
    public static final int IME_FLAG_NO_FULLSCREEN = 33554432;
    public static final int IME_FLAG_NAVIGATE_PREVIOUS = 67108864;
    public static final int IME_FLAG_NAVIGATE_NEXT = 134217728;
    public static final int IME_FLAG_NO_EXTRACT_UI = 268435456;
    public static final int IME_FLAG_NO_ACCESSORY_ACTION = 536870912;
    public static final int IME_FLAG_NO_ENTER_ACTION = 1073741824;
    public static final int IME_FLAG_FORCE_ASCII = Integer.MIN_VALUE;
    public static final int IME_NULL = 0;
    public int imeOptions;
    public String privateImeOptions;
    public CharSequence actionLabel;
    public int actionId;
    public int initialSelStart;
    public int initialSelEnd;
    public int initialCapsMode;
    public CharSequence hintText;
    public CharSequence label;
    public String packageName;
    public int fieldId;
    public String fieldName;
    public Bundle extras;
    public static final Parcelable.Creator<EditorInfo> CREATOR = null;

    public EditorInfo() {
        throw new RuntimeException("Stub!");
    }

    public final void makeCompatible(int targetSdkVersion) {
        throw new RuntimeException("Stub!");
    }

    public void dump(Printer pw, String prefix) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }
}
