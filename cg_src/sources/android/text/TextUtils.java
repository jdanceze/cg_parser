package android.text;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Printer;
import java.util.Iterator;
import java.util.regex.Pattern;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/TextUtils.class */
public class TextUtils {
    public static final Parcelable.Creator<CharSequence> CHAR_SEQUENCE_CREATOR = null;
    public static final int CAP_MODE_CHARACTERS = 4096;
    public static final int CAP_MODE_WORDS = 8192;
    public static final int CAP_MODE_SENTENCES = 16384;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/TextUtils$EllipsizeCallback.class */
    public interface EllipsizeCallback {
        void ellipsized(int i, int i2);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/TextUtils$StringSplitter.class */
    public interface StringSplitter extends Iterable<String> {
        void setString(String str);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/TextUtils$TruncateAt.class */
    public enum TruncateAt {
        END,
        MARQUEE,
        MIDDLE,
        START
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/TextUtils$SimpleStringSplitter.class */
    public static class SimpleStringSplitter implements StringSplitter, Iterator<String> {
        public SimpleStringSplitter(char delimiter) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.text.TextUtils.StringSplitter
        public void setString(String string) {
            throw new RuntimeException("Stub!");
        }

        @Override // java.lang.Iterable
        public Iterator<String> iterator() {
            throw new RuntimeException("Stub!");
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            throw new RuntimeException("Stub!");
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public String next() {
            throw new RuntimeException("Stub!");
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new RuntimeException("Stub!");
        }
    }

    TextUtils() {
        throw new RuntimeException("Stub!");
    }

    public static void getChars(CharSequence s, int start, int end, char[] dest, int destoff) {
        throw new RuntimeException("Stub!");
    }

    public static int indexOf(CharSequence s, char ch) {
        throw new RuntimeException("Stub!");
    }

    public static int indexOf(CharSequence s, char ch, int start) {
        throw new RuntimeException("Stub!");
    }

    public static int indexOf(CharSequence s, char ch, int start, int end) {
        throw new RuntimeException("Stub!");
    }

    public static int lastIndexOf(CharSequence s, char ch) {
        throw new RuntimeException("Stub!");
    }

    public static int lastIndexOf(CharSequence s, char ch, int last) {
        throw new RuntimeException("Stub!");
    }

    public static int lastIndexOf(CharSequence s, char ch, int start, int last) {
        throw new RuntimeException("Stub!");
    }

    public static int indexOf(CharSequence s, CharSequence needle) {
        throw new RuntimeException("Stub!");
    }

    public static int indexOf(CharSequence s, CharSequence needle, int start) {
        throw new RuntimeException("Stub!");
    }

    public static int indexOf(CharSequence s, CharSequence needle, int start, int end) {
        throw new RuntimeException("Stub!");
    }

    public static boolean regionMatches(CharSequence one, int toffset, CharSequence two, int ooffset, int len) {
        throw new RuntimeException("Stub!");
    }

    public static String substring(CharSequence source, int start, int end) {
        throw new RuntimeException("Stub!");
    }

    public static String join(CharSequence delimiter, Object[] tokens) {
        throw new RuntimeException("Stub!");
    }

    public static String join(CharSequence delimiter, Iterable tokens) {
        throw new RuntimeException("Stub!");
    }

    public static String[] split(String text, String expression) {
        throw new RuntimeException("Stub!");
    }

    public static String[] split(String text, Pattern pattern) {
        throw new RuntimeException("Stub!");
    }

    public static CharSequence stringOrSpannedString(CharSequence source) {
        throw new RuntimeException("Stub!");
    }

    public static boolean isEmpty(CharSequence str) {
        throw new RuntimeException("Stub!");
    }

    public static int getTrimmedLength(CharSequence s) {
        throw new RuntimeException("Stub!");
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        throw new RuntimeException("Stub!");
    }

    public static CharSequence getReverse(CharSequence source, int start, int end) {
        throw new RuntimeException("Stub!");
    }

    public static void writeToParcel(CharSequence cs, Parcel p, int parcelableFlags) {
        throw new RuntimeException("Stub!");
    }

    public static void dumpSpans(CharSequence cs, Printer printer, String prefix) {
        throw new RuntimeException("Stub!");
    }

    public static CharSequence replace(CharSequence template, String[] sources, CharSequence[] destinations) {
        throw new RuntimeException("Stub!");
    }

    public static CharSequence expandTemplate(CharSequence template, CharSequence... values) {
        throw new RuntimeException("Stub!");
    }

    public static int getOffsetBefore(CharSequence text, int offset) {
        throw new RuntimeException("Stub!");
    }

    public static int getOffsetAfter(CharSequence text, int offset) {
        throw new RuntimeException("Stub!");
    }

    public static void copySpansFrom(Spanned source, int start, int end, Class kind, Spannable dest, int destoff) {
        throw new RuntimeException("Stub!");
    }

    public static CharSequence ellipsize(CharSequence text, TextPaint p, float avail, TruncateAt where) {
        throw new RuntimeException("Stub!");
    }

    public static CharSequence ellipsize(CharSequence text, TextPaint paint, float avail, TruncateAt where, boolean preserveLength, EllipsizeCallback callback) {
        throw new RuntimeException("Stub!");
    }

    public static CharSequence commaEllipsize(CharSequence text, TextPaint p, float avail, String oneMore, String more) {
        throw new RuntimeException("Stub!");
    }

    public static String htmlEncode(String s) {
        throw new RuntimeException("Stub!");
    }

    public static CharSequence concat(CharSequence... text) {
        throw new RuntimeException("Stub!");
    }

    public static boolean isGraphic(CharSequence str) {
        throw new RuntimeException("Stub!");
    }

    public static boolean isGraphic(char c) {
        throw new RuntimeException("Stub!");
    }

    public static boolean isDigitsOnly(CharSequence str) {
        throw new RuntimeException("Stub!");
    }

    public static int getCapsMode(CharSequence cs, int off, int reqModes) {
        throw new RuntimeException("Stub!");
    }
}
