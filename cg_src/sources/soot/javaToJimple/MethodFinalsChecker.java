package soot.javaToJimple;

import java.util.ArrayList;
import java.util.HashMap;
import polyglot.ast.ClassBody;
import polyglot.ast.ConstructorCall;
import polyglot.ast.Formal;
import polyglot.ast.LocalClassDecl;
import polyglot.ast.LocalDecl;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.util.IdentityKey;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/MethodFinalsChecker.class */
public class MethodFinalsChecker extends NodeVisitor {
    private final ArrayList<IdentityKey> finalLocals = new ArrayList<>();
    private final ArrayList<IdentityKey> inners = new ArrayList<>();
    private final ArrayList<Node> ccallList = new ArrayList<>();
    private final HashMap<IdentityKey, ArrayList<IdentityKey>> typeToLocalsUsed = new HashMap<>();

    public HashMap<IdentityKey, ArrayList<IdentityKey>> typeToLocalsUsed() {
        return this.typeToLocalsUsed;
    }

    public ArrayList<IdentityKey> finalLocals() {
        return this.finalLocals;
    }

    public ArrayList<IdentityKey> inners() {
        return this.inners;
    }

    public ArrayList<Node> ccallList() {
        return this.ccallList;
    }

    @Override // polyglot.visit.NodeVisitor
    public Node override(Node parent, Node n) {
        if (n instanceof LocalClassDecl) {
            this.inners.add(new IdentityKey(((LocalClassDecl) n).decl().type()));
            ClassBody localClassBody = ((LocalClassDecl) n).decl().body();
            LocalUsesChecker luc = new LocalUsesChecker();
            localClassBody.visit(luc);
            this.typeToLocalsUsed.put(new IdentityKey(((LocalClassDecl) n).decl().type()), luc.getLocals());
            return n;
        } else if ((n instanceof New) && ((New) n).anonType() != null) {
            this.inners.add(new IdentityKey(((New) n).anonType()));
            ClassBody anonClassBody = ((New) n).body();
            LocalUsesChecker luc2 = new LocalUsesChecker();
            anonClassBody.visit(luc2);
            this.typeToLocalsUsed.put(new IdentityKey(((New) n).anonType()), luc2.getLocals());
            return n;
        } else {
            return null;
        }
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node parent, Node n) {
        if (n instanceof LocalDecl) {
            LocalDecl ld = (LocalDecl) n;
            if (ld.flags().isFinal() && !this.finalLocals.contains(new IdentityKey(ld.localInstance()))) {
                this.finalLocals.add(new IdentityKey(ld.localInstance()));
            }
        }
        if (n instanceof Formal) {
            Formal ld2 = (Formal) n;
            if (ld2.flags().isFinal() && !this.finalLocals.contains(new IdentityKey(ld2.localInstance()))) {
                this.finalLocals.add(new IdentityKey(ld2.localInstance()));
            }
        }
        if (n instanceof ConstructorCall) {
            this.ccallList.add(n);
        }
        return enter(n);
    }
}
