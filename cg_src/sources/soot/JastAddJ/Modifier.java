package soot.JastAddJ;

import beaver.Symbol;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Modifier.class */
public class Modifier extends ASTNode<ASTNode> implements Cloneable {
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
    public Modifier clone() throws CloneNotSupportedException {
        Modifier node = (Modifier) super.mo287clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            Modifier node = clone();
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
    @Override // soot.JastAddJ.ASTNode
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

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(getID());
    }

    public Modifier() {
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    public Modifier(String p0) {
        setID(p0);
    }

    public Modifier(Symbol p0) {
        setID(p0);
    }

    @Override // soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
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

    @Override // soot.JastAddJ.ASTNode
    public String dumpString() {
        state();
        return String.valueOf(getClass().getName()) + " [" + getID() + "]";
    }

    public boolean isRuntimeVisible() {
        state();
        return false;
    }

    public boolean isRuntimeInvisible() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
