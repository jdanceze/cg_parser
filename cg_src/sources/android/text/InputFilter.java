package android.text;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/InputFilter.class */
public interface InputFilter {
    CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4);

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/InputFilter$AllCaps.class */
    public static class AllCaps implements InputFilter {
        public AllCaps() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.text.InputFilter
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/InputFilter$LengthFilter.class */
    public static class LengthFilter implements InputFilter {
        public LengthFilter(int max) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.text.InputFilter
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            throw new RuntimeException("Stub!");
        }
    }
}
