package soot.JastAddJ;

import beaver.Symbol;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/SingleStaticImportDecl.class */
public class SingleStaticImportDecl extends StaticImportDecl implements Cloneable {
    protected String tokenString_ID;
    public int IDstart;
    public int IDend;

    @Override // soot.JastAddJ.StaticImportDecl, soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.StaticImportDecl, soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.StaticImportDecl, soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public SingleStaticImportDecl clone() throws CloneNotSupportedException {
        SingleStaticImportDecl node = (SingleStaticImportDecl) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            SingleStaticImportDecl node = clone();
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

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (!getAccess().type().typeName().equals(typeName()) && !getAccess().type().isUnknown()) {
            error("Single-type import " + typeName() + " is not the canonical name of type " + getAccess().type().typeName());
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void nameCheck() {
        if (importedFields(name()).isEmpty() && importedMethods(name()).isEmpty() && importedTypes(name()).isEmpty() && !getAccess().type().isUnknown()) {
            error("Semantic Error: At least one static member named " + name() + " must be available in static imported type " + type().fullName());
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append("import static ");
        getAccess().toString(s);
        s.append("." + getID());
        s.append(";\n");
    }

    public SingleStaticImportDecl() {
    }

    @Override // soot.JastAddJ.StaticImportDecl, soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public SingleStaticImportDecl(Access p0, String p1) {
        setChild(p0, 0);
        setID(p1);
    }

    public SingleStaticImportDecl(Access p0, Symbol p1) {
        setChild(p0, 0);
        setID(p1);
    }

    @Override // soot.JastAddJ.StaticImportDecl, soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.StaticImportDecl, soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.StaticImportDecl, soot.JastAddJ.ImportDecl
    public void setAccess(Access node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.StaticImportDecl, soot.JastAddJ.ImportDecl
    public Access getAccess() {
        return (Access) getChild(0);
    }

    @Override // soot.JastAddJ.StaticImportDecl, soot.JastAddJ.ImportDecl
    public Access getAccessNoTransform() {
        return (Access) getChildNoTransform(0);
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

    @Override // soot.JastAddJ.StaticImportDecl
    public TypeDecl type() {
        state();
        return getAccess().type();
    }

    public String name() {
        state();
        return getID();
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getAccessNoTransform()) {
            return NameType.TYPE_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.StaticImportDecl, soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
