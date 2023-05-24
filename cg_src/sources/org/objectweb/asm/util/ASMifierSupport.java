package org.objectweb.asm.util;

import java.util.Map;
import org.objectweb.asm.Label;
/* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/ASMifierSupport.class */
public interface ASMifierSupport {
    void asmify(StringBuilder sb, String str, Map<Label, String> map);
}
