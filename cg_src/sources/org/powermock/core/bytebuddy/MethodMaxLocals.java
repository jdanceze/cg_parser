package org.powermock.core.bytebuddy;

import java.util.HashMap;
import java.util.Map;
import net.bytebuddy.description.method.MethodDescription;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/bytebuddy/MethodMaxLocals.class */
public class MethodMaxLocals {
    private final Map<String, Integer> methodMaxLocals = new HashMap();

    public void add(String name, String signature, int maxLocals) {
        this.methodMaxLocals.put(name + signature, Integer.valueOf(maxLocals));
    }

    public int getMethodMaxLocal(MethodDescription instrumentedMethod) {
        String key = instrumentedMethod.getInternalName() + instrumentedMethod.getDescriptor();
        Integer maxLocals = this.methodMaxLocals.get(key);
        if (maxLocals == null) {
            return 0;
        }
        return maxLocals.intValue();
    }
}
