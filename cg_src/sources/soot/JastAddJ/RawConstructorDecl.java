package soot.JastAddJ;

import beaver.Symbol;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/RawConstructorDecl.class */
public class RawConstructorDecl extends ParConstructorDecl implements Cloneable {
    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public RawConstructorDecl clone() throws CloneNotSupportedException {
        RawConstructorDecl node = (RawConstructorDecl) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            RawConstructorDecl node = clone();
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
    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl, soot.JastAddJ.ASTNode
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

    public RawConstructorDecl() {
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[6];
        setChild(new List(), 1);
        setChild(new List(), 2);
        setChild(new Opt(), 3);
        setChild(new List(), 5);
    }

    public RawConstructorDecl(Modifiers p0, String p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5, List<Access> p6) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
        setChild(p6, 5);
    }

    public RawConstructorDecl(Modifiers p0, Symbol p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5, List<Access> p6) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
        setChild(p6, 5);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 6;
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return true;
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public void setParameterList(List<ParameterDeclaration> list) {
        setChild(list, 1);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public int getNumParameter() {
        return getParameterList().getNumChild();
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public int getNumParameterNoTransform() {
        return getParameterListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public ParameterDeclaration getParameter(int i) {
        return getParameterList().getChild(i);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public void addParameter(ParameterDeclaration node) {
        List<ParameterDeclaration> list = (this.parent == null || state == null) ? getParameterListNoTransform() : getParameterList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public void addParameterNoTransform(ParameterDeclaration node) {
        List<ParameterDeclaration> list = getParameterListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public void setParameter(ParameterDeclaration node, int i) {
        List<ParameterDeclaration> list = getParameterList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public List<ParameterDeclaration> getParameters() {
        return getParameterList();
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public List<ParameterDeclaration> getParametersNoTransform() {
        return getParameterListNoTransform();
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public List<ParameterDeclaration> getParameterList() {
        List<ParameterDeclaration> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public List<ParameterDeclaration> getParameterListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public void setExceptionList(List<Access> list) {
        setChild(list, 2);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public int getNumException() {
        return getExceptionList().getNumChild();
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public int getNumExceptionNoTransform() {
        return getExceptionListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public Access getException(int i) {
        return getExceptionList().getChild(i);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public void addException(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getExceptionListNoTransform() : getExceptionList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public void addExceptionNoTransform(Access node) {
        List<Access> list = getExceptionListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public void setException(Access node, int i) {
        List<Access> list = getExceptionList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public List<Access> getExceptions() {
        return getExceptionList();
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public List<Access> getExceptionsNoTransform() {
        return getExceptionListNoTransform();
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public List<Access> getExceptionList() {
        List<Access> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public List<Access> getExceptionListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public void setConstructorInvocationOpt(Opt<Stmt> opt) {
        setChild(opt, 3);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public boolean hasConstructorInvocation() {
        return getConstructorInvocationOpt().getNumChild() != 0;
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public Stmt getConstructorInvocation() {
        return getConstructorInvocationOpt().getChild(0);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public void setConstructorInvocation(Stmt node) {
        getConstructorInvocationOpt().setChild(node, 0);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public Opt<Stmt> getConstructorInvocationOpt() {
        return (Opt) getChild(3);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public Opt<Stmt> getConstructorInvocationOptNoTransform() {
        return (Opt) getChildNoTransform(3);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public void setBlock(Block node) {
        setChild(node, 4);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public Block getBlock() {
        return (Block) getChild(4);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl
    public Block getBlockNoTransform() {
        return (Block) getChildNoTransform(4);
    }

    @Override // soot.JastAddJ.ParConstructorDecl
    public void setTypeArgumentList(List<Access> list) {
        setChild(list, 5);
    }

    @Override // soot.JastAddJ.ParConstructorDecl
    public int getNumTypeArgument() {
        return getTypeArgumentList().getNumChild();
    }

    @Override // soot.JastAddJ.ParConstructorDecl
    public int getNumTypeArgumentNoTransform() {
        return getTypeArgumentListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ParConstructorDecl
    public Access getTypeArgument(int i) {
        return getTypeArgumentList().getChild(i);
    }

    @Override // soot.JastAddJ.ParConstructorDecl
    public void addTypeArgument(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getTypeArgumentListNoTransform() : getTypeArgumentList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParConstructorDecl
    public void addTypeArgumentNoTransform(Access node) {
        List<Access> list = getTypeArgumentListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParConstructorDecl
    public void setTypeArgument(Access node, int i) {
        List<Access> list = getTypeArgumentList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ParConstructorDecl
    public List<Access> getTypeArguments() {
        return getTypeArgumentList();
    }

    @Override // soot.JastAddJ.ParConstructorDecl
    public List<Access> getTypeArgumentsNoTransform() {
        return getTypeArgumentListNoTransform();
    }

    @Override // soot.JastAddJ.ParConstructorDecl
    public List<Access> getTypeArgumentList() {
        List<Access> list = (List) getChild(5);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ParConstructorDecl
    public List<Access> getTypeArgumentListNoTransform() {
        return (List) getChildNoTransform(5);
    }

    @Override // soot.JastAddJ.ParConstructorDecl, soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
