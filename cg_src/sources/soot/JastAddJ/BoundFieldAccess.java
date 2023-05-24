package soot.JastAddJ;

import beaver.Symbol;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/BoundFieldAccess.class */
public class BoundFieldAccess extends VarAccess implements Cloneable {
    protected FieldDeclaration tokenFieldDeclaration_FieldDeclaration;
    protected boolean decl_computed;
    protected Variable decl_value;

    @Override // soot.JastAddJ.VarAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.decl_computed = false;
        this.decl_value = null;
    }

    @Override // soot.JastAddJ.VarAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.VarAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public BoundFieldAccess clone() throws CloneNotSupportedException {
        BoundFieldAccess node = (BoundFieldAccess) super.clone();
        node.decl_computed = false;
        node.decl_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.VarAccess, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            BoundFieldAccess node = clone();
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
    @Override // soot.JastAddJ.VarAccess, soot.JastAddJ.ASTNode
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

    public BoundFieldAccess(FieldDeclaration f) {
        this(f.name(), f);
    }

    public boolean isExactVarAccess() {
        return false;
    }

    public BoundFieldAccess() {
        this.decl_computed = false;
    }

    @Override // soot.JastAddJ.VarAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    public BoundFieldAccess(String p0, FieldDeclaration p1) {
        this.decl_computed = false;
        setID(p0);
        setFieldDeclaration(p1);
    }

    public BoundFieldAccess(Symbol p0, FieldDeclaration p1) {
        this.decl_computed = false;
        setID(p0);
        setFieldDeclaration(p1);
    }

    @Override // soot.JastAddJ.VarAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.VarAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.VarAccess
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.VarAccess
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.VarAccess
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    public void setFieldDeclaration(FieldDeclaration value) {
        this.tokenFieldDeclaration_FieldDeclaration = value;
    }

    public FieldDeclaration getFieldDeclaration() {
        return this.tokenFieldDeclaration_FieldDeclaration;
    }

    @Override // soot.JastAddJ.VarAccess
    public Variable decl() {
        if (this.decl_computed) {
            return this.decl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.decl_value = decl_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.decl_computed = true;
        }
        return this.decl_value;
    }

    private Variable decl_compute() {
        return getFieldDeclaration();
    }

    @Override // soot.JastAddJ.VarAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
