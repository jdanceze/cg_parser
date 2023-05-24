package soot.JastAddJ;

import beaver.Symbol;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ParConstructorDecl.class */
public class ParConstructorDecl extends ConstructorDecl implements Cloneable {
    protected GenericConstructorDecl genericConstructorDecl_value;
    protected ConstructorDecl sourceConstructorDecl_value;
    protected boolean genericConstructorDecl_computed = false;
    protected boolean sourceConstructorDecl_computed = false;

    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.genericConstructorDecl_computed = false;
        this.genericConstructorDecl_value = null;
        this.sourceConstructorDecl_computed = false;
        this.sourceConstructorDecl_value = null;
    }

    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public ParConstructorDecl clone() throws CloneNotSupportedException {
        ParConstructorDecl node = (ParConstructorDecl) super.clone();
        node.genericConstructorDecl_computed = false;
        node.genericConstructorDecl_value = null;
        node.sourceConstructorDecl_computed = false;
        node.sourceConstructorDecl_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            ParConstructorDecl node = clone();
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
    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.ASTNode
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

    public ParConstructorDecl() {
    }

    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[6];
        setChild(new List(), 1);
        setChild(new List(), 2);
        setChild(new Opt(), 3);
        setChild(new List(), 5);
    }

    public ParConstructorDecl(Modifiers p0, String p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5, List<Access> p6) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
        setChild(p6, 5);
    }

    public ParConstructorDecl(Modifiers p0, Symbol p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5, List<Access> p6) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
        setChild(p6, 5);
    }

    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 6;
    }

    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return true;
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public void setParameterList(List<ParameterDeclaration> list) {
        setChild(list, 1);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public int getNumParameter() {
        return getParameterList().getNumChild();
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public int getNumParameterNoTransform() {
        return getParameterListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public ParameterDeclaration getParameter(int i) {
        return getParameterList().getChild(i);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public void addParameter(ParameterDeclaration node) {
        List<ParameterDeclaration> list = (this.parent == null || state == null) ? getParameterListNoTransform() : getParameterList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public void addParameterNoTransform(ParameterDeclaration node) {
        List<ParameterDeclaration> list = getParameterListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public void setParameter(ParameterDeclaration node, int i) {
        List<ParameterDeclaration> list = getParameterList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public List<ParameterDeclaration> getParameters() {
        return getParameterList();
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public List<ParameterDeclaration> getParametersNoTransform() {
        return getParameterListNoTransform();
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public List<ParameterDeclaration> getParameterList() {
        List<ParameterDeclaration> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public List<ParameterDeclaration> getParameterListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public void setExceptionList(List<Access> list) {
        setChild(list, 2);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public int getNumException() {
        return getExceptionList().getNumChild();
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public int getNumExceptionNoTransform() {
        return getExceptionListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public Access getException(int i) {
        return getExceptionList().getChild(i);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public void addException(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getExceptionListNoTransform() : getExceptionList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public void addExceptionNoTransform(Access node) {
        List<Access> list = getExceptionListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public void setException(Access node, int i) {
        List<Access> list = getExceptionList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public List<Access> getExceptions() {
        return getExceptionList();
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public List<Access> getExceptionsNoTransform() {
        return getExceptionListNoTransform();
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public List<Access> getExceptionList() {
        List<Access> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public List<Access> getExceptionListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public void setConstructorInvocationOpt(Opt<Stmt> opt) {
        setChild(opt, 3);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public boolean hasConstructorInvocation() {
        return getConstructorInvocationOpt().getNumChild() != 0;
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public Stmt getConstructorInvocation() {
        return getConstructorInvocationOpt().getChild(0);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public void setConstructorInvocation(Stmt node) {
        getConstructorInvocationOpt().setChild(node, 0);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public Opt<Stmt> getConstructorInvocationOpt() {
        return (Opt) getChild(3);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public Opt<Stmt> getConstructorInvocationOptNoTransform() {
        return (Opt) getChildNoTransform(3);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public void setBlock(Block node) {
        setChild(node, 4);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public Block getBlock() {
        return (Block) getChild(4);
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public Block getBlockNoTransform() {
        return (Block) getChildNoTransform(4);
    }

    public void setTypeArgumentList(List<Access> list) {
        setChild(list, 5);
    }

    public int getNumTypeArgument() {
        return getTypeArgumentList().getNumChild();
    }

    public int getNumTypeArgumentNoTransform() {
        return getTypeArgumentListNoTransform().getNumChildNoTransform();
    }

    public Access getTypeArgument(int i) {
        return getTypeArgumentList().getChild(i);
    }

    public void addTypeArgument(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getTypeArgumentListNoTransform() : getTypeArgumentList();
        list.addChild(node);
    }

    public void addTypeArgumentNoTransform(Access node) {
        List<Access> list = getTypeArgumentListNoTransform();
        list.addChild(node);
    }

    public void setTypeArgument(Access node, int i) {
        List<Access> list = getTypeArgumentList();
        list.setChild(node, i);
    }

    public List<Access> getTypeArguments() {
        return getTypeArgumentList();
    }

    public List<Access> getTypeArgumentsNoTransform() {
        return getTypeArgumentListNoTransform();
    }

    public List<Access> getTypeArgumentList() {
        List<Access> list = (List) getChild(5);
        list.getNumChild();
        return list;
    }

    public List<Access> getTypeArgumentListNoTransform() {
        return (List) getChildNoTransform(5);
    }

    public GenericConstructorDecl genericConstructorDecl() {
        if (this.genericConstructorDecl_computed) {
            return this.genericConstructorDecl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.genericConstructorDecl_value = genericConstructorDecl_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.genericConstructorDecl_computed = true;
        }
        return this.genericConstructorDecl_value;
    }

    private GenericConstructorDecl genericConstructorDecl_compute() {
        if (getParent() != null && (getParent().getParent() instanceof GenericConstructorDecl)) {
            return (GenericConstructorDecl) getParent().getParent();
        }
        return null;
    }

    @Override // soot.JastAddJ.ConstructorDecl
    public ConstructorDecl sourceConstructorDecl() {
        if (this.sourceConstructorDecl_computed) {
            return this.sourceConstructorDecl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.sourceConstructorDecl_value = sourceConstructorDecl_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.sourceConstructorDecl_computed = true;
        }
        return this.sourceConstructorDecl_value;
    }

    private ConstructorDecl sourceConstructorDecl_compute() {
        return genericConstructorDecl().original().sourceConstructorDecl();
    }

    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
