package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.JastAddJ.Signatures;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/GenericInterfaceDecl.class */
public class GenericInterfaceDecl extends InterfaceDecl implements Cloneable, GenericTypeDecl {
    protected TypeDecl rawType_value;
    protected Map lookupParTypeDecl_ArrayList_values;
    protected List lookupParTypeDecl_ArrayList_list;
    protected boolean usesTypeVariable_value;
    protected Map subtype_TypeDecl_values;
    protected Map instanceOf_TypeDecl_values;
    protected Map lookupParTypeDecl_ParTypeAccess_values;
    protected boolean rawType_computed = false;
    protected int usesTypeVariable_visited = -1;
    protected boolean usesTypeVariable_computed = false;
    protected boolean usesTypeVariable_initialized = false;

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.rawType_computed = false;
        this.rawType_value = null;
        this.lookupParTypeDecl_ArrayList_values = null;
        this.lookupParTypeDecl_ArrayList_list = null;
        this.usesTypeVariable_visited = -1;
        this.usesTypeVariable_computed = false;
        this.usesTypeVariable_initialized = false;
        this.subtype_TypeDecl_values = null;
        this.instanceOf_TypeDecl_values = null;
        this.lookupParTypeDecl_ParTypeAccess_values = null;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public GenericInterfaceDecl clone() throws CloneNotSupportedException {
        GenericInterfaceDecl node = (GenericInterfaceDecl) super.clone();
        node.rawType_computed = false;
        node.rawType_value = null;
        node.lookupParTypeDecl_ArrayList_values = null;
        node.lookupParTypeDecl_ArrayList_list = null;
        node.usesTypeVariable_visited = -1;
        node.usesTypeVariable_computed = false;
        node.usesTypeVariable_initialized = false;
        node.subtype_TypeDecl_values = null;
        node.instanceOf_TypeDecl_values = null;
        node.lookupParTypeDecl_ParTypeAccess_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            GenericInterfaceDecl node = clone();
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
    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ASTNode
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

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void typeCheck() {
        super.typeCheck();
        if (instanceOf(typeThrowable())) {
            error(" generic interface " + typeName() + " may not directly or indirectly inherit java.lang.Throwable");
        }
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [soot.JastAddJ.Modifiers] */
    @Override // soot.JastAddJ.InterfaceDecl
    public InterfaceDecl substitutedInterfaceDecl(Parameterization parTypeDecl) {
        GenericInterfaceDecl c = new GenericInterfaceDeclSubstituted((Modifiers) getModifiers().fullCopy2(), getID(), getSuperInterfaceIdList().substitute(parTypeDecl), new List(), this);
        return c;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        getModifiers().toString(s);
        s.append("interface " + getID());
        s.append('<');
        if (getNumTypeParameter() > 0) {
            getTypeParameter(0).toString(s);
            for (int i = 1; i < getNumTypeParameter(); i++) {
                s.append(", ");
                getTypeParameter(i).toString(s);
            }
        }
        s.append('>');
        if (getNumSuperInterfaceId() > 0) {
            s.append(" extends ");
            getSuperInterfaceId(0).toString(s);
            for (int i2 = 1; i2 < getNumSuperInterfaceId(); i2++) {
                s.append(", ");
                getSuperInterfaceId(i2).toString(s);
            }
        }
        ppBodyDecls(s);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.TypeDecl
    public TypeDecl makeGeneric(Signatures.ClassSignature s) {
        return this;
    }

    @Override // soot.JastAddJ.GenericTypeDecl
    public SimpleSet addTypeVariables(SimpleSet c, String name) {
        GenericTypeDecl original = (GenericTypeDecl) original();
        for (int i = 0; i < original.getNumTypeParameter(); i++) {
            TypeVariable p = original.getTypeParameter(i);
            if (p.name().equals(name)) {
                c = c.add(p);
            }
        }
        return c;
    }

    @Override // soot.JastAddJ.GenericTypeDecl
    public List createArgumentList(ArrayList params) {
        GenericTypeDecl original = (GenericTypeDecl) original();
        List list = new List();
        if (params.isEmpty()) {
            for (int i = 0; i < original.getNumTypeParameter(); i++) {
                list.add(original.getTypeParameter(i).erasure().createBoundAccess());
            }
        } else {
            Iterator iter = params.iterator();
            while (iter.hasNext()) {
                list.add(((TypeDecl) iter.next()).createBoundAccess());
            }
        }
        return list;
    }

    public GenericInterfaceDecl() {
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[4];
        setChild(new List(), 1);
        setChild(new List(), 2);
        setChild(new List(), 3);
    }

    public GenericInterfaceDecl(Modifiers p0, String p1, List<Access> p2, List<BodyDecl> p3, List<TypeVariable> p4) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
        setChild(p4, 3);
    }

    public GenericInterfaceDecl(Modifiers p0, Symbol p1, List<Access> p2, List<BodyDecl> p3, List<TypeVariable> p4) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
        setChild(p4, 3);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 4;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public void setSuperInterfaceIdList(List<Access> list) {
        setChild(list, 1);
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public int getNumSuperInterfaceId() {
        return getSuperInterfaceIdList().getNumChild();
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public int getNumSuperInterfaceIdNoTransform() {
        return getSuperInterfaceIdListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public Access getSuperInterfaceId(int i) {
        return getSuperInterfaceIdList().getChild(i);
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public void addSuperInterfaceId(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getSuperInterfaceIdListNoTransform() : getSuperInterfaceIdList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public void addSuperInterfaceIdNoTransform(Access node) {
        List<Access> list = getSuperInterfaceIdListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public void setSuperInterfaceId(Access node, int i) {
        List<Access> list = getSuperInterfaceIdList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public List<Access> getSuperInterfaceIds() {
        return getSuperInterfaceIdList();
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public List<Access> getSuperInterfaceIdsNoTransform() {
        return getSuperInterfaceIdListNoTransform();
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public List<Access> getSuperInterfaceIdList() {
        List<Access> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public List<Access> getSuperInterfaceIdListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 2);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public int getNumBodyDecl() {
        return getBodyDeclList().getNumChild();
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public int getNumBodyDeclNoTransform() {
        return getBodyDeclListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public BodyDecl getBodyDecl(int i) {
        return getBodyDeclList().getChild(i);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void addBodyDecl(BodyDecl node) {
        List<BodyDecl> list = (this.parent == null || state == null) ? getBodyDeclListNoTransform() : getBodyDeclList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void addBodyDeclNoTransform(BodyDecl node) {
        List<BodyDecl> list = getBodyDeclListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDecl(BodyDecl node, int i) {
        List<BodyDecl> list = getBodyDeclList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDecls() {
        return getBodyDeclList();
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclsNoTransform() {
        return getBodyDeclListNoTransform();
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclList() {
        List<BodyDecl> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    public void setTypeParameterList(List<TypeVariable> list) {
        setChild(list, 3);
    }

    @Override // soot.JastAddJ.GenericTypeDecl
    public int getNumTypeParameter() {
        return getTypeParameterList().getNumChild();
    }

    public int getNumTypeParameterNoTransform() {
        return getTypeParameterListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.GenericTypeDecl
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

    @Override // soot.JastAddJ.GenericTypeDecl
    public List<TypeVariable> getTypeParameterList() {
        List<TypeVariable> list = (List) getChild(3);
        list.getNumChild();
        return list;
    }

    public List<TypeVariable> getTypeParameterListNoTransform() {
        return (List) getChildNoTransform(3);
    }

    @Override // soot.JastAddJ.GenericTypeDecl
    public TypeDecl rawType() {
        if (this.rawType_computed) {
            return this.rawType_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.rawType_value = rawType_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.rawType_computed = true;
        }
        return this.rawType_value;
    }

    private TypeDecl rawType_compute() {
        return lookupParTypeDecl(new ArrayList());
    }

    @Override // soot.JastAddJ.GenericTypeDecl
    public TypeDecl lookupParTypeDecl(ArrayList list) {
        if (this.lookupParTypeDecl_ArrayList_values == null) {
            this.lookupParTypeDecl_ArrayList_values = new HashMap(4);
        }
        if (this.lookupParTypeDecl_ArrayList_values.containsKey(list)) {
            return (TypeDecl) this.lookupParTypeDecl_ArrayList_values.get(list);
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        TypeDecl lookupParTypeDecl_ArrayList_value = lookupParTypeDecl_compute(list);
        if (this.lookupParTypeDecl_ArrayList_list == null) {
            this.lookupParTypeDecl_ArrayList_list = new List();
            this.lookupParTypeDecl_ArrayList_list.is$Final = true;
            this.lookupParTypeDecl_ArrayList_list.setParent(this);
        }
        this.lookupParTypeDecl_ArrayList_list.add(lookupParTypeDecl_ArrayList_value);
        if (lookupParTypeDecl_ArrayList_value != null) {
            lookupParTypeDecl_ArrayList_value.is$Final = true;
        }
        this.lookupParTypeDecl_ArrayList_values.put(list, lookupParTypeDecl_ArrayList_value);
        return lookupParTypeDecl_ArrayList_value;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v3, types: [soot.JastAddJ.Modifiers] */
    /* JADX WARN: Type inference failed for: r6v0, types: [soot.JastAddJ.TypeDecl, soot.JastAddJ.ParInterfaceDecl] */
    private TypeDecl lookupParTypeDecl_compute(ArrayList list) {
        ?? rawInterfaceDecl = list.size() == 0 ? new RawInterfaceDecl() : new ParInterfaceDecl();
        rawInterfaceDecl.setModifiers(getModifiers().fullCopy2());
        rawInterfaceDecl.setID(getID());
        if (!(rawInterfaceDecl instanceof RawInterfaceDecl)) {
            rawInterfaceDecl.setArgumentList(createArgumentList(list));
        }
        return rawInterfaceDecl;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean usesTypeVariable() {
        if (this.usesTypeVariable_computed) {
            return this.usesTypeVariable_value;
        }
        ASTNode.State state = state();
        if (!this.usesTypeVariable_initialized) {
            this.usesTypeVariable_initialized = true;
            this.usesTypeVariable_value = false;
        }
        if (!state.IN_CIRCLE) {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = is$Final();
            do {
                this.usesTypeVariable_visited = state.CIRCLE_INDEX;
                state.CHANGE = false;
                boolean new_usesTypeVariable_value = usesTypeVariable_compute();
                if (new_usesTypeVariable_value != this.usesTypeVariable_value) {
                    state.CHANGE = true;
                }
                this.usesTypeVariable_value = new_usesTypeVariable_value;
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            if (isFinal && num == state().boundariesCrossed) {
                this.usesTypeVariable_computed = true;
            } else {
                state.RESET_CYCLE = true;
                usesTypeVariable_compute();
                state.RESET_CYCLE = false;
                this.usesTypeVariable_computed = false;
                this.usesTypeVariable_initialized = false;
            }
            state.IN_CIRCLE = false;
            return this.usesTypeVariable_value;
        } else if (this.usesTypeVariable_visited != state.CIRCLE_INDEX) {
            this.usesTypeVariable_visited = state.CIRCLE_INDEX;
            if (state.RESET_CYCLE) {
                this.usesTypeVariable_computed = false;
                this.usesTypeVariable_initialized = false;
                this.usesTypeVariable_visited = -1;
                return this.usesTypeVariable_value;
            }
            boolean new_usesTypeVariable_value2 = usesTypeVariable_compute();
            if (new_usesTypeVariable_value2 != this.usesTypeVariable_value) {
                state.CHANGE = true;
            }
            this.usesTypeVariable_value = new_usesTypeVariable_value2;
            return this.usesTypeVariable_value;
        } else {
            return this.usesTypeVariable_value;
        }
    }

    private boolean usesTypeVariable_compute() {
        return true;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.TypeDecl
    public boolean subtype(TypeDecl type) {
        ASTNode.State.CircularValue _value;
        boolean new_subtype_TypeDecl_value;
        if (this.subtype_TypeDecl_values == null) {
            this.subtype_TypeDecl_values = new HashMap(4);
        }
        if (this.subtype_TypeDecl_values.containsKey(type)) {
            Object _o = this.subtype_TypeDecl_values.get(type);
            if (!(_o instanceof ASTNode.State.CircularValue)) {
                return ((Boolean) _o).booleanValue();
            }
            _value = (ASTNode.State.CircularValue) _o;
        } else {
            _value = new ASTNode.State.CircularValue();
            this.subtype_TypeDecl_values.put(type, _value);
            _value.value = true;
        }
        ASTNode.State state = state();
        if (!state.IN_CIRCLE) {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = is$Final();
            do {
                _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
                state.CHANGE = false;
                new_subtype_TypeDecl_value = subtype_compute(type);
                if (new_subtype_TypeDecl_value != ((Boolean) _value.value).booleanValue()) {
                    state.CHANGE = true;
                    _value.value = Boolean.valueOf(new_subtype_TypeDecl_value);
                }
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            if (!isFinal || num != state().boundariesCrossed) {
                this.subtype_TypeDecl_values.remove(type);
                state.RESET_CYCLE = true;
                subtype_compute(type);
                state.RESET_CYCLE = false;
            } else {
                this.subtype_TypeDecl_values.put(type, Boolean.valueOf(new_subtype_TypeDecl_value));
            }
            state.IN_CIRCLE = false;
            return new_subtype_TypeDecl_value;
        } else if (!new Integer(state.CIRCLE_INDEX).equals(Integer.valueOf(_value.visited))) {
            _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
            boolean new_subtype_TypeDecl_value2 = subtype_compute(type);
            if (state.RESET_CYCLE) {
                this.subtype_TypeDecl_values.remove(type);
            } else if (new_subtype_TypeDecl_value2 != ((Boolean) _value.value).booleanValue()) {
                state.CHANGE = true;
                _value.value = Boolean.valueOf(new_subtype_TypeDecl_value2);
            }
            return new_subtype_TypeDecl_value2;
        } else {
            return ((Boolean) _value.value).booleanValue();
        }
    }

    private boolean subtype_compute(TypeDecl type) {
        return type.supertypeGenericInterfaceDecl(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeParClassDecl(ParClassDecl type) {
        state();
        return type.genericDecl().original().subtype(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeParInterfaceDecl(ParInterfaceDecl type) {
        state();
        return type.genericDecl().original().subtype(this);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.TypeDecl
    public boolean instanceOf(TypeDecl type) {
        if (this.instanceOf_TypeDecl_values == null) {
            this.instanceOf_TypeDecl_values = new HashMap(4);
        }
        if (this.instanceOf_TypeDecl_values.containsKey(type)) {
            return ((Boolean) this.instanceOf_TypeDecl_values.get(type)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean instanceOf_TypeDecl_value = instanceOf_compute(type);
        if (isFinal && num == state().boundariesCrossed) {
            this.instanceOf_TypeDecl_values.put(type, Boolean.valueOf(instanceOf_TypeDecl_value));
        }
        return instanceOf_TypeDecl_value;
    }

    private boolean instanceOf_compute(TypeDecl type) {
        return subtype(type);
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.GenericTypeDecl
    public boolean isGenericType() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.GenericTypeDecl
    public TypeDecl lookupParTypeDecl(ParTypeAccess p) {
        if (this.lookupParTypeDecl_ParTypeAccess_values == null) {
            this.lookupParTypeDecl_ParTypeAccess_values = new HashMap(4);
        }
        if (this.lookupParTypeDecl_ParTypeAccess_values.containsKey(p)) {
            return (TypeDecl) this.lookupParTypeDecl_ParTypeAccess_values.get(p);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        TypeDecl lookupParTypeDecl_ParTypeAccess_value = lookupParTypeDecl_compute(p);
        if (isFinal && num == state().boundariesCrossed) {
            this.lookupParTypeDecl_ParTypeAccess_values.put(p, lookupParTypeDecl_ParTypeAccess_value);
        }
        return lookupParTypeDecl_ParTypeAccess_value;
    }

    private TypeDecl lookupParTypeDecl_compute(ParTypeAccess p) {
        ArrayList typeArguments = new ArrayList();
        for (int i = 0; i < p.getNumTypeArgument(); i++) {
            typeArguments.add(p.getTypeArgument(i).type());
        }
        return lookupParTypeDecl(typeArguments);
    }

    public TypeDecl typeThrowable() {
        state();
        TypeDecl typeThrowable_value = getParent().Define_TypeDecl_typeThrowable(this, null);
        return typeThrowable_value;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean Define_boolean_isNestedType(ASTNode caller, ASTNode child) {
        if (caller == getTypeParameterListNoTransform()) {
            caller.getIndexOfChild(child);
            return true;
        }
        return super.Define_boolean_isNestedType(caller, child);
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_enclosingType(ASTNode caller, ASTNode child) {
        if (caller == getTypeParameterListNoTransform()) {
            caller.getIndexOfChild(child);
            return this;
        }
        return super.Define_TypeDecl_enclosingType(caller, child);
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
        if (caller == getBodyDeclListNoTransform()) {
            int index = caller.getIndexOfChild(child);
            SimpleSet c = memberTypes(name);
            if (getBodyDecl(index).visibleTypeParameters()) {
                c = addTypeVariables(c, name);
            }
            if (!c.isEmpty()) {
                return c;
            }
            if (isClassDecl() && isStatic() && !isTopLevelType()) {
                for (TypeDecl d : lookupType(name)) {
                    if (d.isStatic() || (d.enclosingType() != null && instanceOf(d.enclosingType()))) {
                        c = c.add(d);
                    }
                }
            } else {
                c = lookupType(name);
            }
            if (!c.isEmpty()) {
                return c;
            }
            return topLevelType().lookupType(name);
        } else if (caller == getTypeParameterListNoTransform()) {
            caller.getIndexOfChild(child);
            SimpleSet c2 = addTypeVariables(memberTypes(name), name);
            if (c2.isEmpty()) {
                if (isClassDecl() && isStatic() && !isTopLevelType()) {
                    for (TypeDecl d2 : lookupType(name)) {
                        if (d2.isStatic() || (d2.enclosingType() != null && instanceOf(d2.enclosingType()))) {
                            c2 = c2.add(d2);
                        }
                    }
                } else {
                    c2 = lookupType(name);
                }
                if (!c2.isEmpty()) {
                    return c2;
                }
                return topLevelType().lookupType(name);
            }
            return c2;
        } else if (caller == getSuperInterfaceIdListNoTransform()) {
            caller.getIndexOfChild(child);
            SimpleSet c3 = addTypeVariables(SimpleSet.emptySet, name);
            return !c3.isEmpty() ? c3 : lookupType(name);
        } else {
            return super.Define_SimpleSet_lookupType(caller, child, name);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_genericDecl(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return this;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
