package org.junit;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/ComparisonFailure.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/ComparisonFailure.class */
public class ComparisonFailure extends AssertionError {
    private static final int MAX_CONTEXT_LENGTH = 20;
    private static final long serialVersionUID = 1;
    private String fExpected;
    private String fActual;

    public ComparisonFailure(String message, String expected, String actual) {
        super(message);
        this.fExpected = expected;
        this.fActual = actual;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return new ComparisonCompactor(20, this.fExpected, this.fActual).compact(super.getMessage());
    }

    public String getActual() {
        return this.fActual;
    }

    public String getExpected() {
        return this.fExpected;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/ComparisonFailure$ComparisonCompactor.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/ComparisonFailure$ComparisonCompactor.class */
    public static class ComparisonCompactor {
        private static final String ELLIPSIS = "...";
        private static final String DIFF_END = "]";
        private static final String DIFF_START = "[";
        private final int contextLength;
        private final String expected;
        private final String actual;

        public ComparisonCompactor(int contextLength, String expected, String actual) {
            this.contextLength = contextLength;
            this.expected = expected;
            this.actual = actual;
        }

        public String compact(String message) {
            if (this.expected == null || this.actual == null || this.expected.equals(this.actual)) {
                return Assert.format(message, this.expected, this.actual);
            }
            DiffExtractor extractor = new DiffExtractor();
            String compactedPrefix = extractor.compactPrefix();
            String compactedSuffix = extractor.compactSuffix();
            return Assert.format(message, compactedPrefix + extractor.expectedDiff() + compactedSuffix, compactedPrefix + extractor.actualDiff() + compactedSuffix);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String sharedPrefix() {
            int end = Math.min(this.expected.length(), this.actual.length());
            for (int i = 0; i < end; i++) {
                if (this.expected.charAt(i) != this.actual.charAt(i)) {
                    return this.expected.substring(0, i);
                }
            }
            return this.expected.substring(0, end);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String sharedSuffix(String prefix) {
            int suffixLength = 0;
            int maxSuffixLength = Math.min(this.expected.length() - prefix.length(), this.actual.length() - prefix.length()) - 1;
            while (suffixLength <= maxSuffixLength && this.expected.charAt((this.expected.length() - 1) - suffixLength) == this.actual.charAt((this.actual.length() - 1) - suffixLength)) {
                suffixLength++;
            }
            return this.expected.substring(this.expected.length() - suffixLength);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Classes with same name are omitted:
          gencallgraphv3.jar:junit-4.13.2.jar:org/junit/ComparisonFailure$ComparisonCompactor$DiffExtractor.class
         */
        /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/ComparisonFailure$ComparisonCompactor$DiffExtractor.class */
        public class DiffExtractor {
            private final String sharedPrefix;
            private final String sharedSuffix;

            private DiffExtractor() {
                this.sharedPrefix = ComparisonCompactor.this.sharedPrefix();
                this.sharedSuffix = ComparisonCompactor.this.sharedSuffix(this.sharedPrefix);
            }

            public String expectedDiff() {
                return extractDiff(ComparisonCompactor.this.expected);
            }

            public String actualDiff() {
                return extractDiff(ComparisonCompactor.this.actual);
            }

            public String compactPrefix() {
                if (this.sharedPrefix.length() > ComparisonCompactor.this.contextLength) {
                    return "..." + this.sharedPrefix.substring(this.sharedPrefix.length() - ComparisonCompactor.this.contextLength);
                }
                return this.sharedPrefix;
            }

            public String compactSuffix() {
                if (this.sharedSuffix.length() > ComparisonCompactor.this.contextLength) {
                    return this.sharedSuffix.substring(0, ComparisonCompactor.this.contextLength) + "...";
                }
                return this.sharedSuffix;
            }

            private String extractDiff(String source) {
                return "[" + source.substring(this.sharedPrefix.length(), source.length() - this.sharedSuffix.length()) + "]";
            }
        }
    }
}
