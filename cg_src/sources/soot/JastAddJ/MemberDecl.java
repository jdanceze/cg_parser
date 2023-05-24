package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/MemberDecl.class */
public abstract class MemberDecl extends BodyDecl implements Cloneable {
    public abstract boolean isStatic();

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public MemberDecl clone() throws CloneNotSupportedException {
        MemberDecl node = (MemberDecl) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    public void checkModifiers() {
        if (!isSynthetic()) {
            super.checkModifiers();
            if (isStatic() && hostType().isInnerClass() && !isConstant()) {
                error("*** Inner classes may not declare static members, unless they are compile-time constant fields");
            }
        }
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public boolean isConstant() {
        state();
        return false;
    }

    public boolean isSynthetic() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
