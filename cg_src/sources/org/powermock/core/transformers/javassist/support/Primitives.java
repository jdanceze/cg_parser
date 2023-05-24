package org.powermock.core.transformers.javassist.support;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import javassist.CtClass;
import javassist.CtPrimitiveType;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/support/Primitives.class */
public class Primitives {
    private static final Map<CtPrimitiveType, Class<?>> ct2primitiveClass = lookupMappings();

    /* JADX WARN: Multi-variable type inference failed */
    private static Map<CtPrimitiveType, Class<?>> lookupMappings() {
        Object[] objArr;
        Map<CtPrimitiveType, Class<?>> mappings = new IdentityHashMap<>(10);
        for (Object[] each : new Object[]{new Object[]{CtClass.booleanType, Boolean.TYPE}, new Object[]{CtClass.byteType, Byte.TYPE}, new Object[]{CtClass.charType, Character.TYPE}, new Object[]{CtClass.doubleType, Double.TYPE}, new Object[]{CtClass.floatType, Float.TYPE}, new Object[]{CtClass.intType, Integer.TYPE}, new Object[]{CtClass.longType, Long.TYPE}, new Object[]{CtClass.shortType, Short.TYPE}, new Object[]{CtClass.voidType, Void.TYPE}}) {
            mappings.put((CtPrimitiveType) each[0], (Class) each[1]);
        }
        return Collections.unmodifiableMap(mappings);
    }

    public static Class<?> getClassFor(CtPrimitiveType ctPrimitiveType) {
        return ct2primitiveClass.get(ctPrimitiveType);
    }
}
