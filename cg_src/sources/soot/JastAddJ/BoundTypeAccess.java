package soot.JastAddJ;

import beaver.Symbol;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/BoundTypeAccess.class */
public class BoundTypeAccess extends TypeAccess implements Cloneable {
    protected TypeDecl tokenTypeDecl_TypeDecl;
    protected boolean decls_computed = false;
    protected SimpleSet decls_value;

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.decls_computed = false;
        this.decls_value = null;
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public BoundTypeAccess clone() throws CloneNotSupportedException {
        BoundTypeAccess node = (BoundTypeAccess) super.clone();
        node.decls_computed = false;
        node.decls_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            BoundTypeAccess node = clone();
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
    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.ASTNode
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

    @Override // soot.JastAddJ.TypeAccess
    public boolean isRaw() {
        return getTypeDecl().isRawType();
    }

    public BoundTypeAccess() {
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    public BoundTypeAccess(String p0, String p1, TypeDecl p2) {
        setPackage(p0);
        setID(p1);
        setTypeDecl(p2);
    }

    public BoundTypeAccess(Symbol p0, Symbol p1, TypeDecl p2) {
        setPackage(p0);
        setID(p1);
        setTypeDecl(p2);
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.TypeAccess
    public void setPackage(String value) {
        this.tokenString_Package = value;
    }

    @Override // soot.JastAddJ.TypeAccess
    public void setPackage(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setPackage is only valid for String lexemes");
        }
        this.tokenString_Package = (String) symbol.value;
        this.Packagestart = symbol.getStart();
        this.Packageend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.TypeAccess
    public String getPackage() {
        return this.tokenString_Package != null ? this.tokenString_Package : "";
    }

    @Override // soot.JastAddJ.TypeAccess
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.TypeAccess
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.TypeAccess
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    public void setTypeDecl(TypeDecl value) {
        this.tokenTypeDecl_TypeDecl = value;
    }

    public TypeDecl getTypeDecl() {
        return this.tokenTypeDecl_TypeDecl;
    }

    @Override // soot.JastAddJ.TypeAccess
    public SimpleSet decls() {
        if (this.decls_computed) {
            return this.decls_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.decls_value = decls_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.decls_computed = true;
        }
        return this.decls_value;
    }

    private SimpleSet decls_compute() {
        return SimpleSet.emptySet.add(getTypeDecl());
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.ASTNode
    public String dumpString() {
        state();
        return String.valueOf(getClass().getName()) + " [" + getTypeDecl().fullName() + "]";
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
