package polyglot.visit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import polyglot.ast.Node;
import polyglot.util.Copy;
import polyglot.util.InternalCompilerError;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/HaltingVisitor.class */
public abstract class HaltingVisitor extends NodeVisitor implements Copy {
    Node bypassParent;
    Collection bypass;

    public HaltingVisitor bypassChildren(Node n) {
        HaltingVisitor v = (HaltingVisitor) copy();
        v.bypassParent = n;
        return v;
    }

    public HaltingVisitor visitChildren() {
        HaltingVisitor v = (HaltingVisitor) copy();
        v.bypassParent = null;
        v.bypass = null;
        return v;
    }

    public HaltingVisitor bypass(Node n) {
        if (n == null) {
            return this;
        }
        HaltingVisitor v = (HaltingVisitor) copy();
        if (this.bypass == null) {
            v.bypass = Collections.singleton(n);
        } else {
            v.bypass = new ArrayList(this.bypass.size() + 1);
            v.bypass.addAll(this.bypass);
            v.bypass.add(n);
        }
        return v;
    }

    public HaltingVisitor bypass(Collection c) {
        if (c == null) {
            return this;
        }
        HaltingVisitor v = (HaltingVisitor) copy();
        if (this.bypass == null) {
            v.bypass = new ArrayList(c);
        } else {
            v.bypass = new ArrayList(this.bypass.size() + c.size());
            v.bypass.addAll(this.bypass);
            v.bypass.addAll(c);
        }
        return v;
    }

    @Override // polyglot.visit.NodeVisitor
    public final Node override(Node parent, Node n) {
        if (this.bypassParent != null && this.bypassParent == parent) {
            return n;
        }
        if (this.bypass != null) {
            for (Object obj : this.bypass) {
                if (obj == n) {
                    return n;
                }
            }
            return null;
        }
        return null;
    }

    @Override // polyglot.util.Copy
    public Object copy() {
        try {
            HaltingVisitor v = (HaltingVisitor) super.clone();
            return v;
        } catch (CloneNotSupportedException e) {
            throw new InternalCompilerError("Java clone() weirdness.");
        }
    }
}
