package net.bytebuddy.jar.asm.commons;

import java.util.Collections;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/jar/asm/commons/SimpleRemapper.class */
public class SimpleRemapper extends Remapper {
    private final Map<String, String> mapping;

    public SimpleRemapper(Map<String, String> mapping) {
        this.mapping = mapping;
    }

    public SimpleRemapper(String oldName, String newName) {
        this.mapping = Collections.singletonMap(oldName, newName);
    }

    @Override // net.bytebuddy.jar.asm.commons.Remapper
    public String mapMethodName(String owner, String name, String descriptor) {
        String remappedName = map(owner + '.' + name + descriptor);
        return remappedName == null ? name : remappedName;
    }

    @Override // net.bytebuddy.jar.asm.commons.Remapper
    public String mapInvokeDynamicMethodName(String name, String descriptor) {
        String remappedName = map('.' + name + descriptor);
        return remappedName == null ? name : remappedName;
    }

    @Override // net.bytebuddy.jar.asm.commons.Remapper
    public String mapFieldName(String owner, String name, String descriptor) {
        String remappedName = map(owner + '.' + name);
        return remappedName == null ? name : remappedName;
    }

    @Override // net.bytebuddy.jar.asm.commons.Remapper
    public String map(String key) {
        return this.mapping.get(key);
    }
}
