package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/GenericMethodDecl.class */
public class GenericMethodDecl extends MethodDecl implements Cloneable {
    public GenericMethodDecl original;
    protected boolean rawMethodDecl_computed = false;
    protected MethodDecl rawMethodDecl_value;
    protected Map lookupParMethodDecl_java_util_List_values;
    protected List lookupParMethodDecl_java_util_List_list;

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.rawMethodDecl_computed = false;
        this.rawMethodDecl_value = null;
        this.lookupParMethodDecl_java_util_List_values = null;
        this.lookupParMethodDecl_java_util_List_list = null;
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public GenericMethodDecl clone() throws CloneNotSupportedException {
        GenericMethodDecl node = (GenericMethodDecl) super.clone();
        node.rawMethodDecl_computed = false;
        node.rawMethodDecl_value = null;
        node.lookupParMethodDecl_java_util_List_values = null;
        node.lookupParMethodDecl_java_util_List_list = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            GenericMethodDecl node = clone();
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

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v10, types: [soot.JastAddJ.Modifiers] */
    /* JADX WARN: Type inference failed for: r6v0, types: [soot.JastAddJ.ParMethodDecl, soot.JastAddJ.Parameterization] */
    public ParMethodDecl newParMethodDecl(java.util.List typeArguments) {
        ?? rawMethodDecl = typeArguments.isEmpty() ? new RawMethodDecl() : new ParMethodDecl();
        rawMethodDecl.setGenericMethodDecl(this);
        List list = new List();
        if (typeArguments.isEmpty()) {
            GenericMethodDecl original = original();
            for (int i = 0; i < original.getNumTypeParameter(); i++) {
                list.add(original.getTypeParameter(i).erasure().createBoundAccess());
            }
        } else {
            Iterator iter = typeArguments.iterator();
            while (iter.hasNext()) {
                list.add(((TypeDecl) iter.next()).createBoundAccess());
            }
        }
        rawMethodDecl.setTypeArgumentList(list);
        rawMethodDecl.setModifiers(getModifiers().fullCopy2());
        rawMethodDecl.setTypeAccess(getTypeAccess().type().substituteReturnType(rawMethodDecl));
        rawMethodDecl.setID(getID());
        rawMethodDecl.setParameterList(getParameterList().substitute(rawMethodDecl));
        rawMethodDecl.setExceptionList(getExceptionList().substitute(rawMethodDecl));
        return rawMethodDecl;
    }

    private void ppTypeParameters(StringBuffer s) {
        s.append(" <");
        for (int i = 0; i < getNumTypeParameter(); i++) {
            if (i != 0) {
                s.append(", ");
            }
            original().getTypeParameter(i).toString(s);
        }
        s.append("> ");
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        getModifiers().toString(s);
        ppTypeParameters(s);
        getTypeAccess().toString(s);
        s.append(Instruction.argsep + getID());
        s.append("(");
        if (getNumParameter() > 0) {
            getParameter(0).toString(s);
            for (int i = 1; i < getNumParameter(); i++) {
                s.append(", ");
                getParameter(i).toString(s);
            }
        }
        s.append(")");
        if (getNumException() > 0) {
            s.append(" throws ");
            getException(0).toString(s);
            for (int i2 = 1; i2 < getNumException(); i2++) {
                s.append(", ");
                getException(i2).toString(s);
            }
        }
        if (hasBlock()) {
            s.append(Instruction.argsep);
            getBlock().toString(s);
            return;
        }
        s.append(";\n");
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [soot.JastAddJ.Modifiers] */
    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.BodyDecl
    public BodyDecl substitutedBodyDecl(Parameterization parTypeDecl) {
        GenericMethodDecl m = new GenericMethodDecl((Modifiers) getModifiers().fullCopy2(), getTypeAccess().type().substituteReturnType(parTypeDecl), getID(), getParameterList().substitute(parTypeDecl), getExceptionList().substitute(parTypeDecl), new Opt(), getTypeParameterList().fullCopy());
        m.original = this;
        return m;
    }

    public GenericMethodDecl() {
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[6];
        setChild(new List(), 2);
        setChild(new List(), 3);
        setChild(new Opt(), 4);
        setChild(new List(), 5);
    }

    public GenericMethodDecl(Modifiers p0, Access p1, String p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, List<TypeVariable> p6) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
        setChild(p6, 5);
    }

    public GenericMethodDecl(Modifiers p0, Access p1, Symbol p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, List<TypeVariable> p6) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
        setChild(p6, 5);
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

    public MethodDecl rawMethodDecl() {
        if (this.rawMethodDecl_computed) {
            return this.rawMethodDecl_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.rawMethodDecl_value = rawMethodDecl_compute();
        this.rawMethodDecl_computed = true;
        return this.rawMethodDecl_value;
    }

    private MethodDecl rawMethodDecl_compute() {
        return lookupParMethodDecl(new ArrayList());
    }

    public MethodDecl lookupParMethodDecl(java.util.List typeArguments) {
        if (this.lookupParMethodDecl_java_util_List_values == null) {
            this.lookupParMethodDecl_java_util_List_values = new HashMap(4);
        }
        if (this.lookupParMethodDecl_java_util_List_values.containsKey(typeArguments)) {
            return (MethodDecl) this.lookupParMethodDecl_java_util_List_values.get(typeArguments);
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        MethodDecl lookupParMethodDecl_java_util_List_value = lookupParMethodDecl_compute(typeArguments);
        if (this.lookupParMethodDecl_java_util_List_list == null) {
            this.lookupParMethodDecl_java_util_List_list = new List();
            this.lookupParMethodDecl_java_util_List_list.is$Final = true;
            this.lookupParMethodDecl_java_util_List_list.setParent(this);
        }
        this.lookupParMethodDecl_java_util_List_list.add(lookupParMethodDecl_java_util_List_value);
        if (lookupParMethodDecl_java_util_List_value != null) {
            lookupParMethodDecl_java_util_List_value.is$Final = true;
        }
        this.lookupParMethodDecl_java_util_List_values.put(typeArguments, lookupParMethodDecl_java_util_List_value);
        return lookupParMethodDecl_java_util_List_value;
    }

    private MethodDecl lookupParMethodDecl_compute(java.util.List typeArguments) {
        return newParMethodDecl(typeArguments);
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

    public GenericMethodDecl original() {
        state();
        return this.original != null ? this.original : this;
    }

    @Override // soot.JastAddJ.BodyDecl
    public SimpleSet lookupType(String name) {
        state();
        SimpleSet lookupType_String_value = getParent().Define_SimpleSet_lookupType(this, null, name);
        return lookupType_String_value;
    }

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.ASTNode
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

    @Override // soot.JastAddJ.MethodDecl, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
