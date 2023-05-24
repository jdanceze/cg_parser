package polyglot.visit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import polyglot.ast.Block;
import polyglot.ast.Local;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.util.InternalCompilerError;
import polyglot.util.UniqueID;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/AlphaRenamer.class */
public class AlphaRenamer extends NodeVisitor {
    protected NodeFactory nf;
    protected Stack setStack = new Stack();
    protected Map renamingMap;
    protected Set freshVars;

    public AlphaRenamer(NodeFactory nf) {
        this.nf = nf;
        this.setStack.push(new HashSet());
        this.renamingMap = new HashMap();
        this.freshVars = new HashSet();
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node n) {
        if (n instanceof Block) {
            this.setStack.push(new HashSet());
        }
        if (n instanceof LocalDecl) {
            LocalDecl l = (LocalDecl) n;
            String name = l.name();
            if (!this.freshVars.contains(name)) {
                String name_ = UniqueID.newID(name);
                this.freshVars.add(name_);
                ((Set) this.setStack.peek()).add(name);
                this.renamingMap.put(name, name_);
            }
        }
        return this;
    }

    @Override // polyglot.visit.NodeVisitor
    public Node leave(Node old, Node n, NodeVisitor v) {
        if (n instanceof Block) {
            Set s = (Set) this.setStack.pop();
            this.renamingMap.keySet().removeAll(s);
            return n;
        } else if (n instanceof Local) {
            Local l = (Local) n;
            String name = l.name();
            if (!this.renamingMap.containsKey(name)) {
                return n;
            }
            return l.name((String) this.renamingMap.get(name));
        } else if (n instanceof LocalDecl) {
            LocalDecl l2 = (LocalDecl) n;
            String name2 = l2.name();
            if (this.freshVars.contains(name2)) {
                return n;
            }
            if (!this.renamingMap.containsKey(name2)) {
                throw new InternalCompilerError("Unexpected error encountered while alpha-renaming.");
            }
            return l2.name((String) this.renamingMap.get(name2));
        } else {
            return n;
        }
    }
}
