package soot.JastAddJ;

import beaver.Symbol;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/RawMethodDecl.class */
public class RawMethodDecl extends ParMethodDecl implements Cloneable {
    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public RawMethodDecl clone() throws CloneNotSupportedException {
        RawMethodDecl node = (RawMethodDecl) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            RawMethodDecl node = clone();
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
    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl, soot.JastAddJ.ASTNode
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

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.Parameterization
    public boolean isRawType() {
        return true;
    }

    public RawMethodDecl() {
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[6];
        setChild(new List(), 2);
        setChild(new List(), 3);
        setChild(new Opt(), 4);
        setChild(new List(), 5);
    }

    public RawMethodDecl(Modifiers p0, Access p1, String p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, List<Access> p6, GenericMethodDecl p7) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
        setChild(p6, 5);
        setGenericMethodDecl(p7);
    }

    public RawMethodDecl(Modifiers p0, Access p1, Symbol p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, List<Access> p6, GenericMethodDecl p7) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
        setChild(p6, 5);
        setGenericMethodDecl(p7);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 6;
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public void setTypeAccess(Access node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public Access getTypeAccess() {
        return (Access) getChild(1);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public Access getTypeAccessNoTransform() {
        return (Access) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public void setParameterList(List<ParameterDeclaration> list) {
        setChild(list, 2);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public int getNumParameter() {
        return getParameterList().getNumChild();
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public int getNumParameterNoTransform() {
        return getParameterListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public ParameterDeclaration getParameter(int i) {
        return getParameterList().getChild(i);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public void addParameter(ParameterDeclaration node) {
        List<ParameterDeclaration> list = (this.parent == null || state == null) ? getParameterListNoTransform() : getParameterList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public void addParameterNoTransform(ParameterDeclaration node) {
        List<ParameterDeclaration> list = getParameterListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public void setParameter(ParameterDeclaration node, int i) {
        List<ParameterDeclaration> list = getParameterList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public List<ParameterDeclaration> getParameters() {
        return getParameterList();
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public List<ParameterDeclaration> getParametersNoTransform() {
        return getParameterListNoTransform();
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public List<ParameterDeclaration> getParameterList() {
        List<ParameterDeclaration> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public List<ParameterDeclaration> getParameterListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public void setExceptionList(List<Access> list) {
        setChild(list, 3);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public int getNumException() {
        return getExceptionList().getNumChild();
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public int getNumExceptionNoTransform() {
        return getExceptionListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public Access getException(int i) {
        return getExceptionList().getChild(i);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public void addException(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getExceptionListNoTransform() : getExceptionList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public void addExceptionNoTransform(Access node) {
        List<Access> list = getExceptionListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public void setException(Access node, int i) {
        List<Access> list = getExceptionList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public List<Access> getExceptions() {
        return getExceptionList();
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public List<Access> getExceptionsNoTransform() {
        return getExceptionListNoTransform();
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public List<Access> getExceptionList() {
        List<Access> list = (List) getChild(3);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public List<Access> getExceptionListNoTransform() {
        return (List) getChildNoTransform(3);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public void setBlockOpt(Opt<Block> opt) {
        setChild(opt, 4);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public boolean hasBlock() {
        return getBlockOpt().getNumChild() != 0;
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public Block getBlock() {
        return getBlockOpt().getChild(0);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public void setBlock(Block node) {
        getBlockOpt().setChild(node, 0);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public Opt<Block> getBlockOpt() {
        return (Opt) getChild(4);
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl
    public Opt<Block> getBlockOptNoTransform() {
        return (Opt) getChildNoTransform(4);
    }

    @Override // soot.JastAddJ.ParMethodDecl
    public void setTypeArgumentList(List<Access> list) {
        setChild(list, 5);
    }

    @Override // soot.JastAddJ.ParMethodDecl
    public int getNumTypeArgument() {
        return getTypeArgumentList().getNumChild();
    }

    @Override // soot.JastAddJ.ParMethodDecl
    public int getNumTypeArgumentNoTransform() {
        return getTypeArgumentListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ParMethodDecl
    public Access getTypeArgument(int i) {
        return getTypeArgumentList().getChild(i);
    }

    @Override // soot.JastAddJ.ParMethodDecl
    public void addTypeArgument(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getTypeArgumentListNoTransform() : getTypeArgumentList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParMethodDecl
    public void addTypeArgumentNoTransform(Access node) {
        List<Access> list = getTypeArgumentListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParMethodDecl
    public void setTypeArgument(Access node, int i) {
        List<Access> list = getTypeArgumentList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ParMethodDecl
    public List<Access> getTypeArguments() {
        return getTypeArgumentList();
    }

    @Override // soot.JastAddJ.ParMethodDecl
    public List<Access> getTypeArgumentsNoTransform() {
        return getTypeArgumentListNoTransform();
    }

    @Override // soot.JastAddJ.ParMethodDecl
    public List<Access> getTypeArgumentList() {
        List<Access> list = (List) getChild(5);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ParMethodDecl
    public List<Access> getTypeArgumentListNoTransform() {
        return (List) getChildNoTransform(5);
    }

    @Override // soot.JastAddJ.ParMethodDecl
    public void setGenericMethodDecl(GenericMethodDecl value) {
        this.tokenGenericMethodDecl_GenericMethodDecl = value;
    }

    @Override // soot.JastAddJ.ParMethodDecl
    public GenericMethodDecl getGenericMethodDecl() {
        return this.tokenGenericMethodDecl_GenericMethodDecl;
    }

    @Override // soot.JastAddJ.ParMethodDecl, soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
