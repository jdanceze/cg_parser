package android.content.pm;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/LabeledIntent.class */
public class LabeledIntent extends Intent {
    public static final Parcelable.Creator<LabeledIntent> CREATOR = null;

    public LabeledIntent(Intent origIntent, String sourcePackage, int labelRes, int icon) {
        throw new RuntimeException("Stub!");
    }

    public LabeledIntent(Intent origIntent, String sourcePackage, CharSequence nonLocalizedLabel, int icon) {
        throw new RuntimeException("Stub!");
    }

    public LabeledIntent(String sourcePackage, int labelRes, int icon) {
        throw new RuntimeException("Stub!");
    }

    public LabeledIntent(String sourcePackage, CharSequence nonLocalizedLabel, int icon) {
        throw new RuntimeException("Stub!");
    }

    public String getSourcePackage() {
        throw new RuntimeException("Stub!");
    }

    public int getLabelResource() {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getNonLocalizedLabel() {
        throw new RuntimeException("Stub!");
    }

    public int getIconResource() {
        throw new RuntimeException("Stub!");
    }

    public CharSequence loadLabel(PackageManager pm) {
        throw new RuntimeException("Stub!");
    }

    public Drawable loadIcon(PackageManager pm) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Intent, android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Intent
    public void readFromParcel(Parcel in) {
        throw new RuntimeException("Stub!");
    }
}
