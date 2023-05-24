package soot.JastAddJ;

import beaver.Symbol;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ParameterDeclarationSubstituted.class */
public class ParameterDeclarationSubstituted extends ParameterDeclaration implements Cloneable {
    protected ParameterDeclaration tokenParameterDeclaration_Original;
    protected boolean sourceVariableDecl_computed = false;
    protected Variable sourceVariableDecl_value;

    @Override // soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.sourceVariableDecl_computed = false;
        this.sourceVariableDecl_value = null;
    }

    @Override // soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode, beaver.Symbol
    public ParameterDeclarationSubstituted clone() throws CloneNotSupportedException {
        ParameterDeclarationSubstituted node = (ParameterDeclarationSubstituted) super.clone();
        node.sourceVariableDecl_computed = false;
        node.sourceVariableDecl_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            ParameterDeclarationSubstituted node = clone();
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
    @Override // soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode
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

    public ParameterDeclarationSubstituted() {
    }

    @Override // soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public ParameterDeclarationSubstituted(Modifiers p0, Access p1, String p2, ParameterDeclaration p3) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setOriginal(p3);
    }

    public ParameterDeclarationSubstituted(Modifiers p0, Access p1, Symbol p2, ParameterDeclaration p3) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setOriginal(p3);
    }

    @Override // soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.ParameterDeclaration
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.Variable
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.ParameterDeclaration
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ParameterDeclaration
    public void setTypeAccess(Access node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.ParameterDeclaration
    public Access getTypeAccess() {
        return (Access) getChild(1);
    }

    @Override // soot.JastAddJ.ParameterDeclaration
    public Access getTypeAccessNoTransform() {
        return (Access) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.ParameterDeclaration
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.ParameterDeclaration
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.ParameterDeclaration
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    public void setOriginal(ParameterDeclaration value) {
        this.tokenParameterDeclaration_Original = value;
    }

    public ParameterDeclaration getOriginal() {
        return this.tokenParameterDeclaration_Original;
    }

    @Override // soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.Variable
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

    @Override // soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
