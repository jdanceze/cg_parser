package org.hamcrest.generator.qdox.parser.structs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/parser/structs/MethodDef.class */
public class MethodDef extends LocatedDef {
    public List typeParams;
    public TypeDef returnType;
    public int dimensions;
    public String body;
    public String name = "";
    public Set modifiers = new HashSet();
    public List params = new ArrayList();
    public Set exceptions = new LinkedHashSet();
    public boolean constructor = false;

    /* JADX WARN: Code restructure failed: missing block: B:35:0x00d1, code lost:
        if ((r0.returnType.dimensions + r0.dimensions) != (r5.dimensions + r5.returnType.dimensions)) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00d4, code lost:
        r1 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean equals(java.lang.Object r6) {
        /*
            Method dump skipped, instructions count: 221
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.hamcrest.generator.qdox.parser.structs.MethodDef.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return this.name.hashCode() + (this.returnType != null ? this.returnType.hashCode() : 0) + this.modifiers.hashCode() + this.params.hashCode() + this.params.hashCode() + this.exceptions.hashCode() + this.dimensions + (this.constructor ? 0 : 1);
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(this.modifiers);
        result.append(' ');
        result.append(this.returnType != null ? this.returnType.toString() : "");
        for (int i = 0; i < this.dimensions; i++) {
            result.append("[]");
        }
        result.append(' ');
        result.append(this.name);
        result.append('(');
        result.append(this.params);
        result.append(')');
        result.append(" throws ");
        result.append(this.exceptions);
        result.append(this.body);
        return result.toString();
    }
}
