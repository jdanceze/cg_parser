package org.objectweb.asm.tree.analysis;

import org.objectweb.asm.tree.AbstractInsnNode;
/* loaded from: gencallgraphv3.jar:asm-analysis-9.4.jar:org/objectweb/asm/tree/analysis/AnalyzerException.class */
public class AnalyzerException extends Exception {
    private static final long serialVersionUID = 3154190448018943333L;
    public final transient AbstractInsnNode node;

    public AnalyzerException(AbstractInsnNode insn, String message) {
        super(message);
        this.node = insn;
    }

    public AnalyzerException(AbstractInsnNode insn, String message, Throwable cause) {
        super(message, cause);
        this.node = insn;
    }

    public AnalyzerException(AbstractInsnNode insn, String message, Object expected, Value actual) {
        super((message == null ? "Expected " : message + ": expected ") + expected + ", but found " + actual);
        this.node = insn;
    }
}
