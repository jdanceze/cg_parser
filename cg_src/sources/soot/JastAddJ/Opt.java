package soot.JastAddJ;

import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Opt.class */
public class Opt<T extends ASTNode> extends ASTNode<T> implements Cloneable {
    @Override // soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ASTNode, beaver.Symbol
    public Opt<T> clone() throws CloneNotSupportedException {
        Opt node = (Opt) super.mo287clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    public Opt<T> copy() {
        try {
            Opt node = clone();
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
    public Opt<T> fullCopy() {
        Opt tree = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ASTNode child = this.children[i];
                if (child != null) {
                    tree.setChild(child.fullCopy(), i);
                }
            }
        }
        return tree;
    }

    public Opt() {
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    public Opt(T opt) {
        setChild(opt, 0);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean definesLabel() {
        state();
        return getParent().definesLabel();
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
