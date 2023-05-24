package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ParClassInstanceExpr.class */
public class ParClassInstanceExpr extends ClassInstanceExpr implements Cloneable {
    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public ParClassInstanceExpr clone() throws CloneNotSupportedException {
        ParClassInstanceExpr node = (ParClassInstanceExpr) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            ParClassInstanceExpr node = clone();
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
    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.ASTNode
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

    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.ASTNode
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

    public ParClassInstanceExpr() {
    }

    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[4];
        setChild(new List(), 1);
        setChild(new Opt(), 2);
        setChild(new List(), 3);
    }

    public ParClassInstanceExpr(Access p0, List<Expr> p1, Opt<TypeDecl> p2, List<Access> p3) {
        setChild(p0, 0);
        setChild(p1, 1);
        setChild(p2, 2);
        setChild(p3, 3);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 4;
    }

    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public void setAccess(Access node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public Access getAccess() {
        return (Access) getChild(0);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public Access getAccessNoTransform() {
        return (Access) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public void setArgList(List<Expr> list) {
        setChild(list, 1);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public int getNumArg() {
        return getArgList().getNumChild();
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public int getNumArgNoTransform() {
        return getArgListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public Expr getArg(int i) {
        return getArgList().getChild(i);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public void addArg(Expr node) {
        List<Expr> list = (this.parent == null || state == null) ? getArgListNoTransform() : getArgList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public void addArgNoTransform(Expr node) {
        List<Expr> list = getArgListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public void setArg(Expr node, int i) {
        List<Expr> list = getArgList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public List<Expr> getArgs() {
        return getArgList();
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public List<Expr> getArgsNoTransform() {
        return getArgListNoTransform();
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public List<Expr> getArgList() {
        List<Expr> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public List<Expr> getArgListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public void setTypeDeclOpt(Opt<TypeDecl> opt) {
        setChild(opt, 2);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public boolean hasTypeDecl() {
        return getTypeDeclOpt().getNumChild() != 0;
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public TypeDecl getTypeDecl() {
        return getTypeDeclOpt().getChild(0);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public void setTypeDecl(TypeDecl node) {
        getTypeDeclOpt().setChild(node, 0);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public Opt<TypeDecl> getTypeDeclOpt() {
        return (Opt) getChild(2);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public Opt<TypeDecl> getTypeDeclOptNoTransform() {
        return (Opt) getChildNoTransform(2);
    }

    public void setTypeArgumentList(List<Access> list) {
        setChild(list, 3);
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
        List<Access> list = (List) getChild(3);
        list.getNumChild();
        return list;
    }

    public List<Access> getTypeArgumentListNoTransform() {
        return (List) getChildNoTransform(3);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getTypeArgumentListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.TYPE_NAME;
        }
        return super.Define_NameType_nameType(caller, child);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
        if (caller == getTypeArgumentListNoTransform()) {
            caller.getIndexOfChild(child);
            return unqualifiedScope().lookupType(name);
        }
        return super.Define_SimpleSet_lookupType(caller, child, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isExplicitGenericConstructorAccess(ASTNode caller, ASTNode child) {
        if (caller == getAccessNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_isExplicitGenericConstructorAccess(this, caller);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
