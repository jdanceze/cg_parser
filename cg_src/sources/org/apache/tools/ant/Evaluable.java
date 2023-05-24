package org.apache.tools.ant;

import java.util.function.Supplier;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/Evaluable.class */
public interface Evaluable<T> extends Supplier<T> {
    T eval();

    @Override // java.util.function.Supplier
    default T get() {
        return eval();
    }
}
