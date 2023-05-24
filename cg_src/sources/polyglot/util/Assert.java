package polyglot.util;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/Assert.class */
public final class Assert {
    public static void check(boolean ok) {
        if (!ok) {
            throw new AssertionFailedError("Assertion failed");
        }
    }

    public static void check(String condition, boolean ok) {
        if (!ok) {
            throw new AssertionFailedError(new StringBuffer().append("Assertion \"").append(condition).append("\" failed.").toString());
        }
    }

    private Assert() {
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/Assert$AssertionFailedError.class */
    private static class AssertionFailedError extends Error {
        public AssertionFailedError() {
        }

        public AssertionFailedError(String s) {
            super(s);
        }
    }
}
