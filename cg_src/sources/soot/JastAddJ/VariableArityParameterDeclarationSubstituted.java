package soot.JastAddJ;

import beaver.Symbol;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/VariableArityParameterDeclarationSubstituted.class */
public class VariableArityParameterDeclarationSubstituted extends VariableArityParameterDeclaration implements Cloneable {
    protected VariableArityParameterDeclaration tokenVariableArityParameterDeclaration_Original;

    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode, beaver.Symbol
    public VariableArityParameterDeclarationSubstituted clone() throws CloneNotSupportedException {
        VariableArityParameterDeclarationSubstituted node = (VariableArityParameterDeclarationSubstituted) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            VariableArityParameterDeclarationSubstituted node = clone();
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
    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode
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

    public VariableArityParameterDeclarationSubstituted() {
    }

    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public VariableArityParameterDeclarationSubstituted(Modifiers p0, Access p1, String p2, VariableArityParameterDeclaration p3) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setOriginal(p3);
    }

    public VariableArityParameterDeclarationSubstituted(Modifiers p0, Access p1, Symbol p2, VariableArityParameterDeclaration p3) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setOriginal(p3);
    }

    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.Variable
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration
    public void setTypeAccess(Access node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration
    public Access getTypeAccess() {
        return (Access) getChild(1);
    }

    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration
    public Access getTypeAccessNoTransform() {
        return (Access) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    public void setOriginal(VariableArityParameterDeclaration value) {
        this.tokenVariableArityParameterDeclaration_Original = value;
    }

    public VariableArityParameterDeclaration getOriginal() {
        return this.tokenVariableArityParameterDeclaration_Original;
    }

    @Override // soot.JastAddJ.VariableArityParameterDeclaration, soot.JastAddJ.ParameterDeclaration, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
