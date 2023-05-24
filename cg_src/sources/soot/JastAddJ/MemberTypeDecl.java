package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/MemberTypeDecl.class */
public abstract class MemberTypeDecl extends MemberDecl implements Cloneable {
    public abstract TypeDecl typeDecl();

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public MemberTypeDecl clone() throws CloneNotSupportedException {
        MemberTypeDecl node = (MemberTypeDecl) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean declaresType(String name) {
        state();
        return typeDecl().name().equals(name);
    }

    @Override // soot.JastAddJ.BodyDecl
    public TypeDecl type(String name) {
        state();
        if (declaresType(name)) {
            return typeDecl();
        }
        return null;
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.Variable
    public boolean isStatic() {
        state();
        return typeDecl().isStatic();
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public boolean addsIndentationLevel() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean hasAnnotationSuppressWarnings(String s) {
        state();
        return typeDecl().hasAnnotationSuppressWarnings(s);
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean isDeprecated() {
        state();
        return typeDecl().isDeprecated();
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean visibleTypeParameters() {
        state();
        return !isStatic();
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean hasAnnotationSafeVarargs() {
        state();
        return typeDecl().hasAnnotationSafeVarargs();
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
