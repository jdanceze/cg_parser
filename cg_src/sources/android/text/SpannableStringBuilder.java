package android.text;

import android.graphics.Paint;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/SpannableStringBuilder.class */
public class SpannableStringBuilder implements CharSequence, GetChars, Spannable, Editable, Appendable {
    public SpannableStringBuilder() {
        throw new RuntimeException("Stub!");
    }

    public SpannableStringBuilder(CharSequence text) {
        throw new RuntimeException("Stub!");
    }

    public SpannableStringBuilder(CharSequence text, int start, int end) {
        throw new RuntimeException("Stub!");
    }

    public static SpannableStringBuilder valueOf(CharSequence source) {
        throw new RuntimeException("Stub!");
    }

    @Override // java.lang.CharSequence
    public char charAt(int where) {
        throw new RuntimeException("Stub!");
    }

    @Override // java.lang.CharSequence
    public int length() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Editable
    public SpannableStringBuilder insert(int where, CharSequence tb, int start, int end) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Editable
    public SpannableStringBuilder insert(int where, CharSequence tb) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Editable
    public SpannableStringBuilder delete(int start, int end) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Editable
    public void clear() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Editable
    public void clearSpans() {
        throw new RuntimeException("Stub!");
    }

    @Override // java.lang.Appendable
    public SpannableStringBuilder append(CharSequence text) {
        throw new RuntimeException("Stub!");
    }

    @Override // java.lang.Appendable
    public SpannableStringBuilder append(CharSequence text, int start, int end) {
        throw new RuntimeException("Stub!");
    }

    @Override // java.lang.Appendable
    public SpannableStringBuilder append(char text) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Editable
    public SpannableStringBuilder replace(int start, int end, CharSequence tb) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Editable
    public SpannableStringBuilder replace(int start, int end, CharSequence tb, int tbstart, int tbend) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Spannable
    public void setSpan(Object what, int start, int end, int flags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Spannable
    public void removeSpan(Object what) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Spanned
    public int getSpanStart(Object what) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Spanned
    public int getSpanEnd(Object what) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Spanned
    public int getSpanFlags(Object what) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Spanned
    public <T> T[] getSpans(int queryStart, int queryEnd, Class<T> kind) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Spanned
    public int nextSpanTransition(int start, int limit, Class kind) {
        throw new RuntimeException("Stub!");
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.GetChars
    public void getChars(int start, int end, char[] dest, int destoff) {
        throw new RuntimeException("Stub!");
    }

    @Override // java.lang.CharSequence
    public String toString() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getTextRunCursor(int contextStart, int contextEnd, int flags, int offset, int cursorOpt, Paint p) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Editable
    public void setFilters(InputFilter[] filters) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Editable
    public InputFilter[] getFilters() {
        throw new RuntimeException("Stub!");
    }
}
