package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/StaticImportOnDemandDecl.class */
public class StaticImportOnDemandDecl extends StaticImportDecl implements Cloneable {
    @Override // soot.JastAddJ.StaticImportDecl, soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.StaticImportDecl, soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.StaticImportDecl, soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public StaticImportOnDemandDecl clone() throws CloneNotSupportedException {
        StaticImportOnDemandDecl node = (StaticImportOnDemandDecl) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            StaticImportOnDemandDecl node = clone();
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
    public void toString(StringBuffer s) {
        s.append("import static ");
        getAccess().toString(s);
        s.append(".*;\n");
    }

    public StaticImportOnDemandDecl() {
    }

    @Override // soot.JastAddJ.StaticImportDecl, soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public StaticImportOnDemandDecl(Access p0) {
        setChild(p0, 0);
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

    @Override // soot.JastAddJ.StaticImportDecl
    public TypeDecl type() {
        state();
        return getAccess().type();
    }

    @Override // soot.JastAddJ.ImportDecl
    public boolean isOnDemand() {
        state();
        return true;
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
