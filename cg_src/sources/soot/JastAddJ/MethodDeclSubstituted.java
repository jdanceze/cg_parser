package soot.JastAddJ;

import beaver.Symbol;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/MethodDeclSubstituted.class */
public class MethodDeclSubstituted extends MethodDecl implements Cloneable {
    protected MethodDecl tokenMethodDecl_Original;
    protected boolean sourceMethodDecl_computed = false;
    protected MethodDecl sourceMethodDecl_value;

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.sourceMethodDecl_computed = false;
        this.sourceMethodDecl_value = null;
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public MethodDeclSubstituted clone() throws CloneNotSupportedException {
        MethodDeclSubstituted node = (MethodDeclSubstituted) super.clone();
        node.sourceMethodDecl_computed = false;
        node.sourceMethodDecl_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            MethodDeclSubstituted node = clone();
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
    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.ASTNode
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

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.ASTNode
    public void transformation() {
    }

    public MethodDeclSubstituted() {
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[5];
        setChild(new List(), 2);
        setChild(new List(), 3);
        setChild(new Opt(), 4);
    }

    public MethodDeclSubstituted(Modifiers p0, Access p1, String p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, MethodDecl p6) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
        setOriginal(p6);
    }

    public MethodDeclSubstituted(Modifiers p0, Access p1, Symbol p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, MethodDecl p6) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
        setOriginal(p6);
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 5;
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.MethodDecl
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.MethodDecl
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.MethodDecl
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.MethodDecl
    public void setTypeAccess(Access node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.MethodDecl
    public Access getTypeAccess() {
        return (Access) getChild(1);
    }

    @Override // soot.JastAddJ.MethodDecl
    public Access getTypeAccessNoTransform() {
        return (Access) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.MethodDecl
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.MethodDecl
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.MethodDecl
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.MethodDecl
    public void setParameterList(List<ParameterDeclaration> list) {
        setChild(list, 2);
    }

    @Override // soot.JastAddJ.MethodDecl
    public int getNumParameter() {
        return getParameterList().getNumChild();
    }

    @Override // soot.JastAddJ.MethodDecl
    public int getNumParameterNoTransform() {
        return getParameterListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.MethodDecl
    public ParameterDeclaration getParameter(int i) {
        return getParameterList().getChild(i);
    }

    @Override // soot.JastAddJ.MethodDecl
    public void addParameter(ParameterDeclaration node) {
        List<ParameterDeclaration> list = (this.parent == null || state == null) ? getParameterListNoTransform() : getParameterList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.MethodDecl
    public void addParameterNoTransform(ParameterDeclaration node) {
        List<ParameterDeclaration> list = getParameterListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.MethodDecl
    public void setParameter(ParameterDeclaration node, int i) {
        List<ParameterDeclaration> list = getParameterList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.MethodDecl
    public List<ParameterDeclaration> getParameters() {
        return getParameterList();
    }

    @Override // soot.JastAddJ.MethodDecl
    public List<ParameterDeclaration> getParametersNoTransform() {
        return getParameterListNoTransform();
    }

    @Override // soot.JastAddJ.MethodDecl
    public List<ParameterDeclaration> getParameterList() {
        List<ParameterDeclaration> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.MethodDecl
    public List<ParameterDeclaration> getParameterListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.MethodDecl
    public void setExceptionList(List<Access> list) {
        setChild(list, 3);
    }

    @Override // soot.JastAddJ.MethodDecl
    public int getNumException() {
        return getExceptionList().getNumChild();
    }

    @Override // soot.JastAddJ.MethodDecl
    public int getNumExceptionNoTransform() {
        return getExceptionListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.MethodDecl
    public Access getException(int i) {
        return getExceptionList().getChild(i);
    }

    @Override // soot.JastAddJ.MethodDecl
    public void addException(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getExceptionListNoTransform() : getExceptionList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.MethodDecl
    public void addExceptionNoTransform(Access node) {
        List<Access> list = getExceptionListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.MethodDecl
    public void setException(Access node, int i) {
        List<Access> list = getExceptionList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.MethodDecl
    public List<Access> getExceptions() {
        return getExceptionList();
    }

    @Override // soot.JastAddJ.MethodDecl
    public List<Access> getExceptionsNoTransform() {
        return getExceptionListNoTransform();
    }

    @Override // soot.JastAddJ.MethodDecl
    public List<Access> getExceptionList() {
        List<Access> list = (List) getChild(3);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.MethodDecl
    public List<Access> getExceptionListNoTransform() {
        return (List) getChildNoTransform(3);
    }

    @Override // soot.JastAddJ.MethodDecl
    public void setBlockOpt(Opt<Block> opt) {
        setChild(opt, 4);
    }

    @Override // soot.JastAddJ.MethodDecl
    public boolean hasBlock() {
        return getBlockOpt().getNumChild() != 0;
    }

    @Override // soot.JastAddJ.MethodDecl
    public Block getBlock() {
        return getBlockOpt().getChild(0);
    }

    @Override // soot.JastAddJ.MethodDecl
    public void setBlock(Block node) {
        getBlockOpt().setChild(node, 0);
    }

    @Override // soot.JastAddJ.MethodDecl
    public Opt<Block> getBlockOpt() {
        return (Opt) getChild(4);
    }

    @Override // soot.JastAddJ.MethodDecl
    public Opt<Block> getBlockOptNoTransform() {
        return (Opt) getChildNoTransform(4);
    }

    public void setOriginal(MethodDecl value) {
        this.tokenMethodDecl_Original = value;
    }

    public MethodDecl getOriginal() {
        return this.tokenMethodDecl_Original;
    }

    @Override // soot.JastAddJ.MethodDecl
    public MethodDecl sourceMethodDecl() {
        if (this.sourceMethodDecl_computed) {
            return this.sourceMethodDecl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.sourceMethodDecl_value = sourceMethodDecl_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.sourceMethodDecl_computed = true;
        }
        return this.sourceMethodDecl_value;
    }

    private MethodDecl sourceMethodDecl_compute() {
        return getOriginal().sourceMethodDecl();
    }

    @Override // soot.JastAddJ.MethodDecl
    public MethodDecl erasedMethod() {
        state();
        return getOriginal().erasedMethod();
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
