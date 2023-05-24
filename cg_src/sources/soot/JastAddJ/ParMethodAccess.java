package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ParMethodAccess.class */
public class ParMethodAccess extends MethodAccess implements Cloneable {
    protected Map typeArguments_MethodDecl_values;

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.typeArguments_MethodDecl_values = null;
    }

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public ParMethodAccess clone() throws CloneNotSupportedException {
        ParMethodAccess node = (ParMethodAccess) super.clone();
        node.typeArguments_MethodDecl_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            ParMethodAccess node = clone();
            node.parent = null;
            if (this.children != null) {
                node.children = (ASTNode[]) this.children.clone();
            }
            return node;
        } catch (CloneNotSupportedException e) {
            throw new Error("Error: clone not supported for " + getClass().getName());
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> fullCopy() {
        ASTNode<ASTNode> copy = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ASTNode child = this.children[i];
                if (child != null) {
                    copy.setChild(child.fullCopy(), i);
                }
            }
        }
        return copy;
    }

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.ASTNode
    public void typeCheck() {
        super.typeCheck();
        if (!decl().hostType().isUnknown()) {
            if (!(decl() instanceof ParMethodDecl)) {
                error("can not have type parameters on a non generic method");
                return;
            }
            ParMethodDecl m = (ParMethodDecl) decl();
            if (!(m instanceof RawMethodDecl) && m.numTypeParameter() != getNumTypeArgument()) {
                error("generic method " + m.signature() + " requires " + m.numTypeParameter() + " type arguments");
            }
        }
    }

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append("<");
        for (int i = 0; i < getNumTypeArgument(); i++) {
            if (i != 0) {
                s.append(", ");
            }
            getTypeArgument(i).toString(s);
        }
        s.append(">");
        super.toString(s);
    }

    public ParMethodAccess() {
    }

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
        setChild(new List(), 0);
        setChild(new List(), 1);
    }

    public ParMethodAccess(String p0, List<Expr> p1, List<Access> p2) {
        setID(p0);
        setChild(p1, 0);
        setChild(p2, 1);
    }

    public ParMethodAccess(Symbol p0, List<Expr> p1, List<Access> p2) {
        setID(p0);
        setChild(p1, 0);
        setChild(p2, 1);
    }

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.MethodAccess
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.MethodAccess
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.MethodAccess
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.MethodAccess
    public void setArgList(List<Expr> list) {
        setChild(list, 0);
    }

    @Override // soot.JastAddJ.MethodAccess
    public int getNumArg() {
        return getArgList().getNumChild();
    }

    @Override // soot.JastAddJ.MethodAccess
    public int getNumArgNoTransform() {
        return getArgListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.MethodAccess
    public Expr getArg(int i) {
        return getArgList().getChild(i);
    }

    @Override // soot.JastAddJ.MethodAccess
    public void addArg(Expr node) {
        List<Expr> list = (this.parent == null || state == null) ? getArgListNoTransform() : getArgList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.MethodAccess
    public void addArgNoTransform(Expr node) {
        List<Expr> list = getArgListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.MethodAccess
    public void setArg(Expr node, int i) {
        List<Expr> list = getArgList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.MethodAccess
    public List<Expr> getArgs() {
        return getArgList();
    }

    @Override // soot.JastAddJ.MethodAccess
    public List<Expr> getArgsNoTransform() {
        return getArgListNoTransform();
    }

    @Override // soot.JastAddJ.MethodAccess
    public List<Expr> getArgList() {
        List<Expr> list = (List) getChild(0);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.MethodAccess
    public List<Expr> getArgListNoTransform() {
        return (List) getChildNoTransform(0);
    }

    public void setTypeArgumentList(List<Access> list) {
        setChild(list, 1);
    }

    public int getNumTypeArgument() {
        return getTypeArgumentList().getNumChild();
    }

    public int getNumTypeArgumentNoTransform() {
        return getTypeArgumentListNoTransform().getNumChildNoTransform();
    }

    public Access getTypeArgument(int i) {
        return getTypeArgumentList().getChild(i);
    }

    public void addTypeArgument(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getTypeArgumentListNoTransform() : getTypeArgumentList();
        list.addChild(node);
    }

    public void addTypeArgumentNoTransform(Access node) {
        List<Access> list = getTypeArgumentListNoTransform();
        list.addChild(node);
    }

    public void setTypeArgument(Access node, int i) {
        List<Access> list = getTypeArgumentList();
        list.setChild(node, i);
    }

    public List<Access> getTypeArguments() {
        return getTypeArgumentList();
    }

    public List<Access> getTypeArgumentsNoTransform() {
        return getTypeArgumentListNoTransform();
    }

    public List<Access> getTypeArgumentList() {
        List<Access> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    public List<Access> getTypeArgumentListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.MethodAccess
    public ArrayList typeArguments(MethodDecl m) {
        if (this.typeArguments_MethodDecl_values == null) {
            this.typeArguments_MethodDecl_values = new HashMap(4);
        }
        if (this.typeArguments_MethodDecl_values.containsKey(m)) {
            return (ArrayList) this.typeArguments_MethodDecl_values.get(m);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        ArrayList typeArguments_MethodDecl_value = typeArguments_compute(m);
        if (isFinal && num == state().boundariesCrossed) {
            this.typeArguments_MethodDecl_values.put(m, typeArguments_MethodDecl_value);
        }
        return typeArguments_MethodDecl_value;
    }

    private ArrayList typeArguments_compute(MethodDecl m) {
        ArrayList typeArguments = new ArrayList();
        for (int i = 0; i < getNumTypeArgument(); i++) {
            typeArguments.add(getTypeArgument(i).type());
        }
        return typeArguments;
    }

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getTypeArgumentListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.TYPE_NAME;
        }
        return super.Define_NameType_nameType(caller, child);
    }

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
        if (caller == getTypeArgumentListNoTransform()) {
            caller.getIndexOfChild(child);
            return unqualifiedScope().lookupType(name);
        }
        return super.Define_SimpleSet_lookupType(caller, child, name);
    }

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
