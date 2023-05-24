package soot.JastAddJ;

import beaver.Symbol;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/FieldDeclarationSubstituted.class */
public class FieldDeclarationSubstituted extends FieldDeclaration implements Cloneable {
    protected FieldDeclaration tokenFieldDeclaration_Original;
    protected boolean sourceVariableDecl_computed = false;
    protected Variable sourceVariableDecl_value;

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.sourceVariableDecl_computed = false;
        this.sourceVariableDecl_value = null;
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public FieldDeclarationSubstituted clone() throws CloneNotSupportedException {
        FieldDeclarationSubstituted node = (FieldDeclarationSubstituted) super.clone();
        node.sourceVariableDecl_computed = false;
        node.sourceVariableDecl_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            FieldDeclarationSubstituted node = clone();
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
    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.ASTNode
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

    public FieldDeclarationSubstituted() {
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new Opt(), 2);
    }

    public FieldDeclarationSubstituted(Modifiers p0, Access p1, String p2, Opt<Expr> p3, FieldDeclaration p4) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
        setOriginal(p4);
    }

    public FieldDeclarationSubstituted(Modifiers p0, Access p1, Symbol p2, Opt<Expr> p3, FieldDeclaration p4) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
        setOriginal(p4);
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.Variable
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public void setTypeAccess(Access node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public Access getTypeAccess() {
        return (Access) getChild(1);
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public Access getTypeAccessNoTransform() {
        return (Access) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public void setInitOpt(Opt<Expr> opt) {
        setChild(opt, 2);
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.Variable
    public boolean hasInit() {
        return getInitOpt().getNumChild() != 0;
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.Variable
    public Expr getInit() {
        return getInitOpt().getChild(0);
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public void setInit(Expr node) {
        getInitOpt().setChild(node, 0);
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public Opt<Expr> getInitOpt() {
        return (Opt) getChild(2);
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public Opt<Expr> getInitOptNoTransform() {
        return (Opt) getChildNoTransform(2);
    }

    public void setOriginal(FieldDeclaration value) {
        this.tokenFieldDeclaration_Original = value;
    }

    public FieldDeclaration getOriginal() {
        return this.tokenFieldDeclaration_Original;
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.Variable
    public Variable sourceVariableDecl() {
        if (this.sourceVariableDecl_computed) {
            return this.sourceVariableDecl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.sourceVariableDecl_value = sourceVariableDecl_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.sourceVariableDecl_computed = true;
        }
        return this.sourceVariableDecl_value;
    }

    private Variable sourceVariableDecl_compute() {
        return getOriginal().sourceVariableDecl();
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public FieldDeclaration erasedField() {
        state();
        return getOriginal().erasedField();
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
