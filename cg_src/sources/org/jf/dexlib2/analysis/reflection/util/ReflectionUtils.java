package org.jf.dexlib2.analysis.reflection.util;

import com.google.common.collect.ImmutableBiMap;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/analysis/reflection/util/ReflectionUtils.class */
public class ReflectionUtils {
    private static ImmutableBiMap<String, String> primitiveMap = ImmutableBiMap.builder().put((ImmutableBiMap.Builder) "boolean", "Z").put((ImmutableBiMap.Builder) "int", "I").put((ImmutableBiMap.Builder) "long", "J").put((ImmutableBiMap.Builder) "double", "D").put((ImmutableBiMap.Builder) Jimple.VOID, "V").put((ImmutableBiMap.Builder) Jimple.FLOAT, "F").put((ImmutableBiMap.Builder) "char", "C").put((ImmutableBiMap.Builder) "short", "S").put((ImmutableBiMap.Builder) "byte", "B").build();

    public static String javaToDexName(String javaName) {
        if (javaName.charAt(0) == '[') {
            return javaName.replace('.', '/');
        }
        if (primitiveMap.containsKey(javaName)) {
            return primitiveMap.get(javaName);
        }
        return 'L' + javaName.replace('.', '/') + ';';
    }

    public static String dexToJavaName(String dexName) {
        if (dexName.charAt(0) == '[') {
            return dexName.replace('/', '.');
        }
        if (primitiveMap.inverse().containsKey(dexName)) {
            return primitiveMap.inverse().get(dexName);
        }
        return dexName.replace('/', '.').substring(1, dexName.length() - 2);
    }
}
