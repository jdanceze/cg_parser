package org.mockito.internal.junit;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/StubbingHint.class */
class StubbingHint {
    private final StringBuilder hint;

    /* JADX INFO: Access modifiers changed from: package-private */
    public StubbingHint(String testName) {
        this.hint = new StringBuilder("[MockitoHint] ").append(testName).append(" (see javadoc for MockitoHint):");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void appendLine(Object... elements) {
        this.hint.append("\n[MockitoHint] ");
        for (Object e : elements) {
            this.hint.append(e);
        }
    }

    public String toString() {
        return this.hint.toString() + "\n";
    }
}
