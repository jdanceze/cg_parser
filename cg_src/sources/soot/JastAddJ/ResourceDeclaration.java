package soot.JastAddJ;

import beaver.Symbol;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ResourceDeclaration.class */
public class ResourceDeclaration extends VariableDeclaration implements Cloneable {
    @Override // soot.JastAddJ.VariableDeclaration, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.VariableDeclaration, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.VariableDeclaration, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public ResourceDeclaration clone() throws CloneNotSupportedException {
        ResourceDeclaration node = (ResourceDeclaration) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.VariableDeclaration, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            ResourceDeclaration node = clone();
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
    @Override // soot.JastAddJ.VariableDeclaration, soot.JastAddJ.ASTNode
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

    @Override // soot.JastAddJ.VariableDeclaration, soot.JastAddJ.ASTNode
    public void typeCheck() {
        TypeDecl typeAutoCloseable = lookupType("java.lang", "AutoCloseable");
        if (typeAutoCloseable == null) {
            error("java.lang.AutoCloseable not found");
        } else if (!getTypeAccess().type().instanceOf(typeAutoCloseable)) {
            error("Resource specification must declare an AutoCloseable resource");
        }
    }

    @Override // soot.JastAddJ.VariableDeclaration, soot.JastAddJ.ASTNode
    public void nameCheck() {
        if (resourcePreviouslyDeclared(name())) {
            error("A resource with the name " + name() + " has already been declared in this try statement.");
        }
        super.nameCheck();
    }

    public ResourceDeclaration() {
    }

    @Override // soot.JastAddJ.VariableDeclaration, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new Opt(), 2);
    }

    public ResourceDeclaration(Modifiers p0, Access p1, String p2, Opt<Expr> p3) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
    }

    public ResourceDeclaration(Modifiers p0, Access p1, Symbol p2, Opt<Expr> p3) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
    }

    @Override // soot.JastAddJ.VariableDeclaration, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.VariableDeclaration, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.VariableDeclaration
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.VariableDeclaration, soot.JastAddJ.Variable
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.VariableDeclaration
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.VariableDeclaration
    public void setTypeAccess(Access node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.VariableDeclaration
    public Access getTypeAccess() {
        return (Access) getChild(1);
    }

    @Override // soot.JastAddJ.VariableDeclaration
    public Access getTypeAccessNoTransform() {
        return (Access) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.VariableDeclaration
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.VariableDeclaration
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.VariableDeclaration
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.VariableDeclaration
    public void setInitOpt(Opt<Expr> opt) {
        setChild(opt, 2);
    }

    @Override // soot.JastAddJ.VariableDeclaration, soot.JastAddJ.Variable
    public boolean hasInit() {
        return getInitOpt().getNumChild() != 0;
    }

    @Override // soot.JastAddJ.VariableDeclaration, soot.JastAddJ.Variable
    public Expr getInit() {
        return getInitOpt().getChild(0);
    }

    @Override // soot.JastAddJ.VariableDeclaration
    public void setInit(Expr node) {
        getInitOpt().setChild(node, 0);
    }

    @Override // soot.JastAddJ.VariableDeclaration
    public Opt<Expr> getInitOpt() {
        return (Opt) getChild(2);
    }

    @Override // soot.JastAddJ.VariableDeclaration
    public Opt<Expr> getInitOptNoTransform() {
        return (Opt) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.Stmt
    public TypeDecl lookupType(String packageName, String typeName) {
        state();
        TypeDecl lookupType_String_String_value = getParent().Define_TypeDecl_lookupType(this, null, packageName, typeName);
        return lookupType_String_String_value;
    }

    @Override // soot.JastAddJ.VariableDeclaration, soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getTypeAccessNoTransform()) {
            return NameType.TYPE_NAME;
        }
        return super.Define_NameType_nameType(caller, child);
    }

    @Override // soot.JastAddJ.VariableDeclaration, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
