package org.apache.tools.ant.util;

import java.lang.ref.WeakReference;
@Deprecated
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/WeakishReference.class */
public class WeakishReference {
    private WeakReference<Object> weakref;

    WeakishReference(Object reference) {
        this.weakref = new WeakReference<>(reference);
    }

    public Object get() {
        return this.weakref.get();
    }

    public static WeakishReference createReference(Object object) {
        return new WeakishReference(object);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/WeakishReference$HardReference.class */
    public static class HardReference extends WeakishReference {
        public HardReference(Object object) {
            super(object);
        }
    }
}
