package org.hamcrest.generator.qdox.parser.structs;

import java.util.HashMap;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/parser/structs/AnnoDef.class */
public class AnnoDef extends LocatedDef {
    public String name = "";
    public Map args = new HashMap();
    public AnnoDef tempAnno = null;

    public boolean equals(Object obj) {
        AnnoDef annoDef = (AnnoDef) obj;
        return annoDef.name.equals(this.name) && annoDef.args.equals(this.args);
    }

    public int hashCode() {
        return this.name.hashCode() + this.args.hashCode();
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append('@');
        result.append(this.name);
        result.append('(');
        if (!this.args.isEmpty()) {
            for (Object obj : this.args.entrySet()) {
                result.append(new StringBuffer().append(obj).append(",").toString());
            }
            result.deleteCharAt(result.length() - 1);
        }
        result.append(')');
        return result.toString();
    }
}
