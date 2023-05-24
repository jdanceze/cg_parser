package soot.JastAddJ;

import beaver.Symbol;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/VariableDecl.class */
public class VariableDecl extends ASTNode<ASTNode> implements Cloneable {
    protected String tokenString_ID;
    public int IDstart;
    public int IDend;

    @Override // soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ASTNode, beaver.Symbol
    public VariableDecl clone() throws CloneNotSupportedException {
        VariableDecl node = (VariableDecl) super.mo287clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            VariableDecl node = clone();
            node.parent = null;
            if (this.children != null) {
                node.children = (ASTNode[]) this.children.clone();
            }
            return node;
        } catch (CloneNotSupportedException e) {
            throw new Error("Error: clone not supported for " + getClass().getName());
        }
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: fullCopy */
    public ASTNode<ASTNode> fullCopy2() {
        ASTNode<ASTNode> copy2 = copy2();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ASTNode child = this.children[i];
                if (child != null) {
                    copy2.setChild(child.fullCopy(), i);
                }
            }
        }
        return copy2;
    }

    public VariableDeclaration createVariableDeclarationFrom(Modifiers modifiers, Access type) {
        VariableDeclaration decl = new VariableDeclaration(modifiers, type.addArrayDims(getDimsList()), getID(), getInitOpt());
        decl.setStart(this.start);
        decl.setEnd(this.end);
        decl.IDstart = this.IDstart;
        decl.IDend = this.IDend;
        return decl;
    }

    public FieldDeclaration createFieldDeclarationFrom(Modifiers modifiers, Access type) {
        FieldDeclaration decl = new FieldDeclaration(modifiers, type.addArrayDims(getDimsList()), getID(), getInitOpt());
        decl.setStart(this.start);
        decl.setEnd(this.end);
        decl.IDstart = this.IDstart;
        decl.IDend = this.IDend;
        return decl;
    }

    public VariableDecl() {
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
        setChild(new List(), 0);
        setChild(new Opt(), 1);
    }

    public VariableDecl(String p0, List<Dims> p1, Opt<Expr> p2) {
        setID(p0);
        setChild(p1, 0);
        setChild(p2, 1);
    }

    public VariableDecl(Symbol p0, List<Dims> p1, Opt<Expr> p2) {
        setID(p0);
        setChild(p1, 0);
        setChild(p2, 1);
    }

    @Override // soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setID(String value) {
        this.tokenString_ID = value;
    }

    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    public void setDimsList(List<Dims> list) {
        setChild(list, 0);
    }

    public int getNumDims() {
        return getDimsList().getNumChild();
    }

    public int getNumDimsNoTransform() {
        return getDimsListNoTransform().getNumChildNoTransform();
    }

    public Dims getDims(int i) {
        return getDimsList().getChild(i);
    }

    public void addDims(Dims node) {
        List<Dims> list = (this.parent == null || state == null) ? getDimsListNoTransform() : getDimsList();
        list.addChild(node);
    }

    public void addDimsNoTransform(Dims node) {
        List<Dims> list = getDimsListNoTransform();
        list.addChild(node);
    }

    public void setDims(Dims node, int i) {
        List<Dims> list = getDimsList();
        list.setChild(node, i);
    }

    public List<Dims> getDimss() {
        return getDimsList();
    }

    public List<Dims> getDimssNoTransform() {
        return getDimsListNoTransform();
    }

    public List<Dims> getDimsList() {
        List<Dims> list = (List) getChild(0);
        list.getNumChild();
        return list;
    }

    public List<Dims> getDimsListNoTransform() {
        return (List) getChildNoTransform(0);
    }

    public void setInitOpt(Opt<Expr> opt) {
        setChild(opt, 1);
    }

    public boolean hasInit() {
        return getInitOpt().getNumChild() != 0;
    }

    public Expr getInit() {
        return getInitOpt().getChild(0);
    }

    public void setInit(Expr node) {
        getInitOpt().setChild(node, 0);
    }

    public Opt<Expr> getInitOpt() {
        return (Opt) getChild(1);
    }

    public Opt<Expr> getInitOptNoTransform() {
        return (Opt) getChildNoTransform(1);
    }

    public String name() {
        state();
        return getID();
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
        if (caller == getInitOptNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_isSource(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_expectedType(ASTNode caller, ASTNode child) {
        if (caller == getInitOptNoTransform()) {
            return null;
        }
        return getParent().Define_TypeDecl_expectedType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
