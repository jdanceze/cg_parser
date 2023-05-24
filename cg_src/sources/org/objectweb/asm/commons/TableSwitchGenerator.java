package org.objectweb.asm.commons;

import org.objectweb.asm.Label;
/* loaded from: gencallgraphv3.jar:asm-commons-9.4.jar:org/objectweb/asm/commons/TableSwitchGenerator.class */
public interface TableSwitchGenerator {
    void generateCase(int i, Label label);

    void generateDefault();
}
