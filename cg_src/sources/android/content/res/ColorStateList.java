package android.content.res;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/res/ColorStateList.class */
public class ColorStateList implements Parcelable {
    public static final Parcelable.Creator<ColorStateList> CREATOR = null;

    public ColorStateList(int[][] states, int[] colors) {
        throw new RuntimeException("Stub!");
    }

    public static ColorStateList valueOf(int color) {
        throw new RuntimeException("Stub!");
    }

    public static ColorStateList createFromXml(Resources r, XmlPullParser parser) throws XmlPullParserException, IOException {
        throw new RuntimeException("Stub!");
    }

    public ColorStateList withAlpha(int alpha) {
        throw new RuntimeException("Stub!");
    }

    public boolean isStateful() {
        throw new RuntimeException("Stub!");
    }

    public int getColorForState(int[] stateSet, int defaultColor) {
        throw new RuntimeException("Stub!");
    }

    public int getDefaultColor() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
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
}
