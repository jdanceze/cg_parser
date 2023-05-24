package org.objectweb.asm.tree.analysis;

import java.util.List;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.analysis.Value;
/* loaded from: gencallgraphv3.jar:asm-analysis-9.4.jar:org/objectweb/asm/tree/analysis/Interpreter.class */
public abstract class Interpreter<V extends Value> {
    protected final int api;

    public abstract V newValue(Type type);

    public abstract V newOperation(AbstractInsnNode abstractInsnNode) throws AnalyzerException;

    public abstract V copyOperation(AbstractInsnNode abstractInsnNode, V v) throws AnalyzerException;

    public abstract V unaryOperation(AbstractInsnNode abstractInsnNode, V v) throws AnalyzerException;

    public abstract V binaryOperation(AbstractInsnNode abstractInsnNode, V v, V v2) throws AnalyzerException;

    public abstract V ternaryOperation(AbstractInsnNode abstractInsnNode, V v, V v2, V v3) throws AnalyzerException;

    public abstract V naryOperation(AbstractInsnNode abstractInsnNode, List<? extends V> list) throws AnalyzerException;

    public abstract void returnOperation(AbstractInsnNode abstractInsnNode, V v, V v2) throws AnalyzerException;

    public abstract V merge(V v, V v2);

    /* JADX INFO: Access modifiers changed from: protected */
    public Interpreter(int api) {
        this.api = api;
    }

    public V newParameterValue(boolean isInstanceMethod, int local, Type type) {
        return newValue(type);
    }

    public V newReturnTypeValue(Type type) {
        return newValue(type);
    }

    public V newEmptyValue(int local) {
        return newValue(null);
    }

    public V newExceptionValue(TryCatchBlockNode tryCatchBlockNode, Frame<V> handlerFrame, Type exceptionType) {
        return newValue(exceptionType);
    }
}
