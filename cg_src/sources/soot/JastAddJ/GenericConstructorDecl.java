package soot.JastAddJ;

import beaver.Symbol;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/GenericConstructorDecl.class */
public class GenericConstructorDecl extends ConstructorDecl implements Cloneable {
    public GenericConstructorDecl original;

    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public GenericConstructorDecl clone() throws CloneNotSupportedException {
        GenericConstructorDecl node = (GenericConstructorDecl) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            GenericConstructorDecl node = clone();
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

    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        getModifiers().toString(s);
        s.append(" <");
        for (int i = 0; i < getNumTypeParameter(); i++) {
            if (i != 0) {
                s.append(", ");
            }
            original().getTypeParameter(i).toString(s);
        }
        s.append("> ");
        s.append(String.valueOf(getID()) + "(");
        if (getNumParameter() > 0) {
            getParameter(0).toString(s);
            for (int i2 = 1; i2 < getNumParameter(); i2++) {
                s.append(", ");
                getParameter(i2).toString(s);
            }
        }
        s.append(")");
        if (getNumException() > 0) {
            s.append(" throws ");
            getException(0).toString(s);
            for (int i3 = 1; i3 < getNumException(); i3++) {
                s.append(", ");
                getException(i3).toString(s);
            }
        }
        s.append(" {");
        if (hasConstructorInvocation()) {
            s.append(indent());
            getConstructorInvocation().toString(s);
        }
        for (int i4 = 0; i4 < getBlock().getNumStmt(); i4++) {
            s.append(indent());
            getBlock().getStmt(i4).toString(s);
        }
        s.append(indent());
        s.append("}");
    }

    public GenericConstructorDecl() {
    }

    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[6];
        setChild(new List(), 1);
        setChild(new List(), 2);
        setChild(new Opt(), 3);
        setChild(new List(), 5);
    }

    public GenericConstructorDecl(Modifiers p0, String p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5, List<TypeVariable> p6) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
        setChild(p6, 5);
    }

    public GenericConstructorDecl(Modifiers p0, Symbol p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5, List<TypeVariable> p6) {
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

    public void setTypeParameterList(List<TypeVariable> list) {
        setChild(list, 5);
    }

    public int getNumTypeParameter() {
        return getTypeParameterList().getNumChild();
    }

    public int getNumTypeParameterNoTransform() {
        return getTypeParameterListNoTransform().getNumChildNoTransform();
    }

    public TypeVariable getTypeParameter(int i) {
        return getTypeParameterList().getChild(i);
    }

    public void addTypeParameter(TypeVariable node) {
        List<TypeVariable> list = (this.parent == null || state == null) ? getTypeParameterListNoTransform() : getTypeParameterList();
        list.addChild(node);
    }

    public void addTypeParameterNoTransform(TypeVariable node) {
        List<TypeVariable> list = getTypeParameterListNoTransform();
        list.addChild(node);
    }

    public void setTypeParameter(TypeVariable node, int i) {
        List<TypeVariable> list = getTypeParameterList();
        list.setChild(node, i);
    }

    public List<TypeVariable> getTypeParameters() {
        return getTypeParameterList();
    }

    public List<TypeVariable> getTypeParametersNoTransform() {
        return getTypeParameterListNoTransform();
    }

    public List<TypeVariable> getTypeParameterList() {
        List<TypeVariable> list = (List) getChild(5);
        list.getNumChild();
        return list;
    }

    public List<TypeVariable> getTypeParameterListNoTransform() {
        return (List) getChildNoTransform(5);
    }

    public SimpleSet localLookupType(String name) {
        state();
        for (int i = 0; i < getNumTypeParameter(); i++) {
            if (original().getTypeParameter(i).name().equals(name)) {
                return SimpleSet.emptySet.add(original().getTypeParameter(i));
            }
        }
        return SimpleSet.emptySet;
    }

    public GenericConstructorDecl original() {
        state();
        return this.original != null ? this.original : this;
    }

    @Override // soot.JastAddJ.BodyDecl
    public SimpleSet lookupType(String name) {
        state();
        SimpleSet lookupType_String_value = getParent().Define_SimpleSet_lookupType(this, null, name);
        return lookupType_String_value;
    }

    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getTypeParameterListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.TYPE_NAME;
        }
        return super.Define_NameType_nameType(caller, child);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
        getIndexOfChild(caller);
        return localLookupType(name).isEmpty() ? lookupType(name) : localLookupType(name);
    }

    @Override // soot.JastAddJ.ConstructorDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
