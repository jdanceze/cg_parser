package android.text.style;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/TextAppearanceSpan.class */
public class TextAppearanceSpan extends MetricAffectingSpan implements ParcelableSpan {
    public TextAppearanceSpan(Context context, int appearance) {
        throw new RuntimeException("Stub!");
    }

    public TextAppearanceSpan(Context context, int appearance, int colorList) {
        throw new RuntimeException("Stub!");
    }

    public TextAppearanceSpan(String family, int style, int size, ColorStateList color, ColorStateList linkColor) {
        throw new RuntimeException("Stub!");
    }

    public TextAppearanceSpan(Parcel src) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.ParcelableSpan
    public int getSpanTypeId() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }

    public String getFamily() {
        throw new RuntimeException("Stub!");
    }

    public ColorStateList getTextColor() {
        throw new RuntimeException("Stub!");
    }

    public ColorStateList getLinkTextColor() {
        throw new RuntimeException("Stub!");
    }

    public int getTextSize() {
        throw new RuntimeException("Stub!");
    }

    public int getTextStyle() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.CharacterStyle
    public void updateDrawState(TextPaint ds) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.MetricAffectingSpan
    public void updateMeasureState(TextPaint ds) {
        throw new RuntimeException("Stub!");
    }
}
