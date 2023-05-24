package org.hamcrest.generator.qdox.parser.structs;

import java.util.HashSet;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/parser/structs/FieldDef.class */
public class FieldDef extends LocatedDef {
    public TypeDef type;
    public int dimensions;
    public boolean isVarArgs;
    public String name = "";
    public Set modifiers = new HashSet();
    public String body = "";

    /* JADX WARN: Code restructure failed: missing block: B:31:0x00b5, code lost:
        if ((r0.type.dimensions + r0.dimensions) != (r5.dimensions + r5.type.dimensions)) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00b8, code lost:
        r1 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean equals(java.lang.Object r6) {
        /*
            r5 = this;
            r0 = r6
            org.hamcrest.generator.qdox.parser.structs.FieldDef r0 = (org.hamcrest.generator.qdox.parser.structs.FieldDef) r0
            r7 = r0
            r0 = r7
            java.lang.String r0 = r0.name
            r1 = r5
            java.lang.String r1 = r1.name
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L30
            r0 = r7
            java.util.Set r0 = r0.modifiers
            r1 = r5
            java.util.Set r1 = r1.modifiers
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L30
            r0 = r7
            boolean r0 = r0.isVarArgs
            r1 = r5
            boolean r1 = r1.isVarArgs
            if (r0 != r1) goto L30
            r0 = 1
            goto L31
        L30:
            r0 = 0
        L31:
            r8 = r0
            r0 = r7
            org.hamcrest.generator.qdox.parser.structs.TypeDef r0 = r0.type
            if (r0 != 0) goto L56
            r0 = r8
            r1 = r5
            org.hamcrest.generator.qdox.parser.structs.TypeDef r1 = r1.type
            if (r1 != 0) goto L50
            r1 = r7
            int r1 = r1.dimensions
            r2 = r5
            int r2 = r2.dimensions
            if (r1 != r2) goto L50
            r1 = 1
            goto L51
        L50:
            r1 = 0
        L51:
            r0 = r0 & r1
            r8 = r0
            goto Lbf
        L56:
            r0 = r8
            r1 = r5
            org.hamcrest.generator.qdox.parser.structs.TypeDef r1 = r1.type
            if (r1 == 0) goto Lbc
            r1 = r7
            org.hamcrest.generator.qdox.parser.structs.TypeDef r1 = r1.type
            java.lang.String r1 = r1.name
            r2 = r5
            org.hamcrest.generator.qdox.parser.structs.TypeDef r2 = r2.type
            java.lang.String r2 = r2.name
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto Lbc
            r1 = r7
            org.hamcrest.generator.qdox.parser.structs.TypeDef r1 = r1.type
            java.util.List r1 = r1.actualArgumentTypes
            if (r1 != 0) goto L89
            r1 = r5
            org.hamcrest.generator.qdox.parser.structs.TypeDef r1 = r1.type
            java.util.List r1 = r1.actualArgumentTypes
            if (r1 != 0) goto Lbc
            goto L9d
        L89:
            r1 = r7
            org.hamcrest.generator.qdox.parser.structs.TypeDef r1 = r1.type
            java.util.List r1 = r1.actualArgumentTypes
            r2 = r5
            org.hamcrest.generator.qdox.parser.structs.TypeDef r2 = r2.type
            java.util.List r2 = r2.actualArgumentTypes
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto Lbc
        L9d:
            r1 = r7
            org.hamcrest.generator.qdox.parser.structs.TypeDef r1 = r1.type
            int r1 = r1.dimensions
            r2 = r7
            int r2 = r2.dimensions
            int r1 = r1 + r2
            r2 = r5
            int r2 = r2.dimensions
            r3 = r5
            org.hamcrest.generator.qdox.parser.structs.TypeDef r3 = r3.type
            int r3 = r3.dimensions
            int r2 = r2 + r3
            if (r1 != r2) goto Lbc
            r1 = 1
            goto Lbd
        Lbc:
            r1 = 0
        Lbd:
            r0 = r0 & r1
            r8 = r0
        Lbf:
            r0 = r8
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.hamcrest.generator.qdox.parser.structs.FieldDef.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return this.name.hashCode() + (this.type != null ? this.type.hashCode() : 0) + this.dimensions + this.modifiers.hashCode() + (this.isVarArgs ? 79769989 : 0);
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(this.modifiers);
        result.append(' ');
        result.append(this.type);
        for (int i = 0; i < this.dimensions; i++) {
            result.append("[]");
        }
        result.append(' ');
        result.append(this.name);
        if (this.body.length() > 0) {
            result.append(" = ").append(this.body);
        }
        return result.toString();
    }
}
