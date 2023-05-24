package org.objectweb.asm.util;

import java.util.Map;
import org.objectweb.asm.Label;
/* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/TextifierSupport.class */
public interface TextifierSupport {
    void textify(StringBuilder sb, Map<Label, String> map);
}
