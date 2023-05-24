package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/MemberClassDecl.class */
public class MemberClassDecl extends MemberTypeDecl implements Cloneable {
    @Override // soot.JastAddJ.MemberTypeDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.MemberTypeDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.MemberTypeDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public MemberClassDecl clone() throws CloneNotSupportedException {
        MemberClassDecl node = (MemberClassDecl) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            MemberClassDecl node = clone();
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
        s.append(indent());
        getClassDecl().toString(s);
    }

    public MemberClassDecl() {
    }

    @Override // soot.JastAddJ.MemberTypeDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public MemberClassDecl(ClassDecl p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.MemberTypeDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.MemberTypeDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setClassDecl(ClassDecl node) {
        setChild(node, 0);
    }

    public ClassDecl getClassDecl() {
        return (ClassDecl) getChild(0);
    }

    public ClassDecl getClassDeclNoTransform() {
        return (ClassDecl) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.MemberTypeDecl
    public TypeDecl typeDecl() {
        state();
        return getClassDecl();
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isMemberType(ASTNode caller, ASTNode child) {
        if (caller == getClassDeclNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_isMemberType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
        if (caller == getClassDeclNoTransform()) {
            return false;
        }
        return getParent().Define_boolean_inStaticContext(this, caller);
    }

    @Override // soot.JastAddJ.MemberTypeDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
