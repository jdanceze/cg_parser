package android.text;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/LoginFilter.class */
public abstract class LoginFilter implements InputFilter {
    public abstract boolean isAllowed(char c);

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/LoginFilter$UsernameFilterGMail.class */
    public static class UsernameFilterGMail extends LoginFilter {
        public UsernameFilterGMail() {
            throw new RuntimeException("Stub!");
        }

        public UsernameFilterGMail(boolean appendInvalid) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.text.LoginFilter
        public boolean isAllowed(char c) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/LoginFilter$UsernameFilterGeneric.class */
    public static class UsernameFilterGeneric extends LoginFilter {
        public UsernameFilterGeneric() {
            throw new RuntimeException("Stub!");
        }

        public UsernameFilterGeneric(boolean appendInvalid) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.text.LoginFilter
        public boolean isAllowed(char c) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/LoginFilter$PasswordFilterGMail.class */
    public static class PasswordFilterGMail extends LoginFilter {
        public PasswordFilterGMail() {
            throw new RuntimeException("Stub!");
        }

        public PasswordFilterGMail(boolean appendInvalid) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.text.LoginFilter
        public boolean isAllowed(char c) {
            throw new RuntimeException("Stub!");
        }
    }

    LoginFilter() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.InputFilter
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        throw new RuntimeException("Stub!");
    }

    public void onStart() {
        throw new RuntimeException("Stub!");
    }

    public void onInvalidCharacter(char c) {
        throw new RuntimeException("Stub!");
    }

    public void onStop() {
        throw new RuntimeException("Stub!");
    }
}
