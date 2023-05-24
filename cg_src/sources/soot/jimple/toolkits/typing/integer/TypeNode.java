package soot.jimple.toolkits.typing.integer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Type;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/integer/TypeNode.class */
public class TypeNode {
    private static final Logger logger = LoggerFactory.getLogger(TypeNode.class);
    public static final boolean DEBUG = false;
    private final int id;
    private final Type type;

    public TypeNode(int id, Type type) {
        this.id = id;
        this.type = type;
    }

    public int id() {
        return this.id;
    }

    public Type type() {
        return this.type;
    }

    public boolean hasAncestor_1(TypeNode typeNode) {
        if (typeNode == this) {
            return true;
        }
        return ClassHierarchy.v().hasAncestor_1(this.id, typeNode.id);
    }

    public boolean hasAncestor_2(TypeNode typeNode) {
        if (typeNode == this) {
            return true;
        }
        return ClassHierarchy.v().hasAncestor_2(this.id, typeNode.id);
    }

    public TypeNode lca_1(TypeNode typeNode) {
        return ClassHierarchy.v().lca_1(this.id, typeNode.id);
    }

    public TypeNode lca_2(TypeNode typeNode) {
        return ClassHierarchy.v().lca_2(this.id, typeNode.id);
    }

    public TypeNode gcd_1(TypeNode typeNode) {
        return ClassHierarchy.v().gcd_1(this.id, typeNode.id);
    }

    public TypeNode gcd_2(TypeNode typeNode) {
        return ClassHierarchy.v().gcd_2(this.id, typeNode.id);
    }

    public String toString() {
        if (this.type != null) {
            return this.type + "(" + this.id + ")";
        }
        ClassHierarchy classHierarchy = ClassHierarchy.v();
        if (this == classHierarchy.TOP) {
            return "TOP(" + this.id + ")";
        }
        if (this == classHierarchy.R0_1) {
            return "R0_1(" + this.id + ")";
        }
        if (this == classHierarchy.R0_127) {
            return "R0_127(" + this.id + ")";
        }
        if (this == classHierarchy.R0_32767) {
            return "R0_32767(" + this.id + ")";
        }
        return "ERROR!!!!";
    }
}
