package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ParMethodDecl.class */
public class ParMethodDecl extends MethodDecl implements Cloneable, Parameterization {
    protected GenericMethodDecl tokenGenericMethodDecl_GenericMethodDecl;
    protected GenericMethodDecl genericMethodDecl_value;
    protected MethodDecl sourceMethodDecl_value;
    protected Map moreSpecificThan_MethodDecl_values;
    protected boolean genericMethodDecl_computed = false;
    protected boolean sourceMethodDecl_computed = false;

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.genericMethodDecl_computed = false;
        this.genericMethodDecl_value = null;
        this.sourceMethodDecl_computed = false;
        this.sourceMethodDecl_value = null;
        this.moreSpecificThan_MethodDecl_values = null;
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public ParMethodDecl clone() throws CloneNotSupportedException {
        ParMethodDecl node = (ParMethodDecl) super.clone();
        node.genericMethodDecl_computed = false;
        node.genericMethodDecl_value = null;
        node.sourceMethodDecl_computed = false;
        node.sourceMethodDecl_value = null;
        node.moreSpecificThan_MethodDecl_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            ParMethodDecl node = clone();
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

    @Override // soot.JastAddJ.ASTNode
    public void collectErrors() {
    }

    @Override // soot.JastAddJ.Parameterization
    public TypeDecl substitute(TypeVariable typeVariable) {
        for (int i = 0; i < numTypeParameter(); i++) {
            if (typeParameter(i) == typeVariable) {
                return getTypeArgument(i).type();
            }
        }
        return genericMethodDecl().hostType().substitute(typeVariable);
    }

    @Override // soot.JastAddJ.Parameterization
    public boolean isRawType() {
        return false;
    }

    public int numTypeParameter() {
        return genericMethodDecl().original().getNumTypeParameter();
    }

    public TypeVariable typeParameter(int index) {
        return genericMethodDecl().original().getTypeParameter(index);
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.ASTNode
    public void transformation() {
    }

    public ParMethodDecl() {
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[6];
        setChild(new List(), 2);
        setChild(new List(), 3);
        setChild(new Opt(), 4);
        setChild(new List(), 5);
    }

    public ParMethodDecl(Modifiers p0, Access p1, String p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, List<Access> p6, GenericMethodDecl p7) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
        setChild(p6, 5);
        setGenericMethodDecl(p7);
    }

    public ParMethodDecl(Modifiers p0, Access p1, Symbol p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, List<Access> p6, GenericMethodDecl p7) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
        setChild(p6, 5);
        setGenericMethodDecl(p7);
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 6;
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

    public void setGenericMethodDecl(GenericMethodDecl value) {
        this.tokenGenericMethodDecl_GenericMethodDecl = value;
    }

    public GenericMethodDecl getGenericMethodDecl() {
        return this.tokenGenericMethodDecl_GenericMethodDecl;
    }

    public GenericMethodDecl genericMethodDecl() {
        if (this.genericMethodDecl_computed) {
            return this.genericMethodDecl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.genericMethodDecl_value = genericMethodDecl_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.genericMethodDecl_computed = true;
        }
        return this.genericMethodDecl_value;
    }

    private GenericMethodDecl genericMethodDecl_compute() {
        return getGenericMethodDecl();
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
        return genericMethodDecl().original().sourceMethodDecl();
    }

    @Override // soot.JastAddJ.MethodDecl
    public boolean moreSpecificThan(MethodDecl m) {
        if (this.moreSpecificThan_MethodDecl_values == null) {
            this.moreSpecificThan_MethodDecl_values = new HashMap(4);
        }
        if (this.moreSpecificThan_MethodDecl_values.containsKey(m)) {
            return ((Boolean) this.moreSpecificThan_MethodDecl_values.get(m)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean moreSpecificThan_MethodDecl_value = moreSpecificThan_compute(m);
        if (isFinal && num == state().boundariesCrossed) {
            this.moreSpecificThan_MethodDecl_values.put(m, Boolean.valueOf(moreSpecificThan_MethodDecl_value));
        }
        return moreSpecificThan_MethodDecl_value;
    }

    private boolean moreSpecificThan_compute(MethodDecl m) {
        return genericMethodDecl().moreSpecificThan(m instanceof ParMethodDecl ? ((ParMethodDecl) m).genericMethodDecl() : m);
    }

    @Override // soot.JastAddJ.MethodDecl
    public MethodDecl erasedMethod() {
        state();
        return genericMethodDecl().erasedMethod();
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
