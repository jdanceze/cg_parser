package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ParInterfaceDecl.class */
public class ParInterfaceDecl extends InterfaceDecl implements Cloneable, ParTypeDecl, MemberSubstitutor {
    protected boolean involvesTypeParameters_value;
    protected TypeDecl erasure_value;
    protected List getSuperInterfaceIdList_value;
    protected List getBodyDeclList_value;
    protected Map subtype_TypeDecl_values;
    protected Map sameStructure_TypeDecl_values;
    protected Map instanceOf_TypeDecl_values;
    protected Map sameSignature_ArrayList_values;
    protected boolean usesTypeVariable_value;
    protected TypeDecl sourceTypeDecl_value;
    protected String fullName_value;
    protected String typeName_value;
    protected Collection unimplementedMethods_value;
    protected HashMap localMethodsSignatureMap_value;
    protected Map localFields_String_values;
    protected Map localTypeDecls_String_values;
    protected Collection constructors_value;
    protected TypeDecl genericDecl_value;
    protected int involvesTypeParameters_visited = -1;
    protected boolean involvesTypeParameters_computed = false;
    protected boolean involvesTypeParameters_initialized = false;
    protected boolean erasure_computed = false;
    protected boolean getSuperInterfaceIdList_computed = false;
    protected boolean getBodyDeclList_computed = false;
    protected int usesTypeVariable_visited = -1;
    protected boolean usesTypeVariable_computed = false;
    protected boolean usesTypeVariable_initialized = false;
    protected boolean sourceTypeDecl_computed = false;
    protected boolean fullName_computed = false;
    protected boolean typeName_computed = false;
    protected boolean unimplementedMethods_computed = false;
    protected boolean localMethodsSignatureMap_computed = false;
    protected boolean constructors_computed = false;
    protected boolean genericDecl_computed = false;

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.involvesTypeParameters_visited = -1;
        this.involvesTypeParameters_computed = false;
        this.involvesTypeParameters_initialized = false;
        this.erasure_computed = false;
        this.erasure_value = null;
        this.getSuperInterfaceIdList_computed = false;
        this.getSuperInterfaceIdList_value = null;
        this.getBodyDeclList_computed = false;
        this.getBodyDeclList_value = null;
        this.subtype_TypeDecl_values = null;
        this.sameStructure_TypeDecl_values = null;
        this.instanceOf_TypeDecl_values = null;
        this.sameSignature_ArrayList_values = null;
        this.usesTypeVariable_visited = -1;
        this.usesTypeVariable_computed = false;
        this.usesTypeVariable_initialized = false;
        this.sourceTypeDecl_computed = false;
        this.sourceTypeDecl_value = null;
        this.fullName_computed = false;
        this.fullName_value = null;
        this.typeName_computed = false;
        this.typeName_value = null;
        this.unimplementedMethods_computed = false;
        this.unimplementedMethods_value = null;
        this.localMethodsSignatureMap_computed = false;
        this.localMethodsSignatureMap_value = null;
        this.localFields_String_values = null;
        this.localTypeDecls_String_values = null;
        this.constructors_computed = false;
        this.constructors_value = null;
        this.genericDecl_computed = false;
        this.genericDecl_value = null;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public ParInterfaceDecl clone() throws CloneNotSupportedException {
        ParInterfaceDecl node = (ParInterfaceDecl) super.clone();
        node.involvesTypeParameters_visited = -1;
        node.involvesTypeParameters_computed = false;
        node.involvesTypeParameters_initialized = false;
        node.erasure_computed = false;
        node.erasure_value = null;
        node.getSuperInterfaceIdList_computed = false;
        node.getSuperInterfaceIdList_value = null;
        node.getBodyDeclList_computed = false;
        node.getBodyDeclList_value = null;
        node.subtype_TypeDecl_values = null;
        node.sameStructure_TypeDecl_values = null;
        node.instanceOf_TypeDecl_values = null;
        node.sameSignature_ArrayList_values = null;
        node.usesTypeVariable_visited = -1;
        node.usesTypeVariable_computed = false;
        node.usesTypeVariable_initialized = false;
        node.sourceTypeDecl_computed = false;
        node.sourceTypeDecl_value = null;
        node.fullName_computed = false;
        node.fullName_value = null;
        node.typeName_computed = false;
        node.typeName_value = null;
        node.unimplementedMethods_computed = false;
        node.unimplementedMethods_value = null;
        node.localMethodsSignatureMap_computed = false;
        node.localMethodsSignatureMap_value = null;
        node.localFields_String_values = null;
        node.localTypeDecls_String_values = null;
        node.constructors_computed = false;
        node.constructors_value = null;
        node.genericDecl_computed = false;
        node.genericDecl_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            ParInterfaceDecl node = clone();
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
    /* JADX WARN: Type inference failed for: r0v1, types: [soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.ASTNode<soot.JastAddJ.ASTNode>] */
    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> fullCopy() {
        ?? copy = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                switch (i) {
                    case 3:
                    case 4:
                        copy.children[i] = new List();
                        break;
                    default:
                        ASTNode child = this.children[i];
                        if (child != null) {
                            copy.setChild(child.fullCopy(), i);
                            break;
                        } else {
                            break;
                        }
                }
            }
        }
        return copy;
    }

    @Override // soot.JastAddJ.ASTNode
    public void collectErrors() {
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ParTypeDecl, soot.JastAddJ.Parameterization
    public TypeDecl substitute(TypeVariable typeVariable) {
        for (int i = 0; i < numTypeParameter(); i++) {
            if (typeParameter(i) == typeVariable) {
                return getArgument(i).type();
            }
        }
        return super.substitute(typeVariable);
    }

    @Override // soot.JastAddJ.ParTypeDecl
    public int numTypeParameter() {
        return ((GenericTypeDecl) original()).getNumTypeParameter();
    }

    @Override // soot.JastAddJ.ParTypeDecl
    public TypeVariable typeParameter(int index) {
        return ((GenericTypeDecl) original()).getTypeParameter(index);
    }

    @Override // soot.JastAddJ.TypeDecl
    public Access substitute(Parameterization parTypeDecl) {
        if (parTypeDecl.isRawType()) {
            return ((GenericTypeDecl) genericDecl()).rawType().createBoundAccess();
        }
        if (!usesTypeVariable()) {
            return super.substitute(parTypeDecl);
        }
        List list = new List();
        for (int i = 0; i < getNumArgument(); i++) {
            list.add(getArgument(i).type().substitute(parTypeDecl));
        }
        return new ParTypeAccess(genericDecl().createQualifiedAccess(), list);
    }

    @Override // soot.JastAddJ.TypeDecl
    public Access createQualifiedAccess() {
        List typeArgumentList = new List();
        for (int i = 0; i < getNumArgument(); i++) {
            Access a = getArgument(i);
            if (a instanceof TypeAccess) {
                typeArgumentList.add(a.type().createQualifiedAccess());
            } else {
                typeArgumentList.add(a.fullCopy());
            }
        }
        if (!isTopLevelType()) {
            if (isRawType()) {
                return enclosingType().createQualifiedAccess().qualifiesAccess(new TypeAccess("", getID()));
            }
            return enclosingType().createQualifiedAccess().qualifiesAccess(new ParTypeAccess(new TypeAccess("", getID()), typeArgumentList));
        } else if (isRawType()) {
            return new TypeAccess(packageName(), getID());
        } else {
            return new ParTypeAccess(new TypeAccess(packageName(), getID()), typeArgumentList);
        }
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void transformation() {
    }

    public ParInterfaceDecl() {
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[4];
        setChild(new List(), 1);
        setChild(new List(), 2);
        setChild(new List(), 3);
    }

    public ParInterfaceDecl(Modifiers p0, String p1, List<Access> p2) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
    }

    public ParInterfaceDecl(Modifiers p0, Symbol p1, List<Access> p2) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
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

    public void setArgumentList(List<Access> list) {
        setChild(list, 1);
    }

    @Override // soot.JastAddJ.ParTypeDecl
    public int getNumArgument() {
        return getArgumentList().getNumChild();
    }

    public int getNumArgumentNoTransform() {
        return getArgumentListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ParTypeDecl
    public Access getArgument(int i) {
        return getArgumentList().getChild(i);
    }

    public void addArgument(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getArgumentListNoTransform() : getArgumentList();
        list.addChild(node);
    }

    public void addArgumentNoTransform(Access node) {
        List<Access> list = getArgumentListNoTransform();
        list.addChild(node);
    }

    public void setArgument(Access node, int i) {
        List<Access> list = getArgumentList();
        list.setChild(node, i);
    }

    public List<Access> getArguments() {
        return getArgumentList();
    }

    public List<Access> getArgumentsNoTransform() {
        return getArgumentListNoTransform();
    }

    public List<Access> getArgumentList() {
        List<Access> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    public List<Access> getArgumentListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public void setSuperInterfaceIdList(List<Access> list) {
        setChild(list, 2);
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
        return (Access) getSuperInterfaceIdList().getChild(i);
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
    public List<Access> getSuperInterfaceIdListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    protected int getSuperInterfaceIdListChildPosition() {
        return 2;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 3);
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
        return (BodyDecl) getBodyDeclList().getChild(i);
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
    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(3);
    }

    protected int getBodyDeclListChildPosition() {
        return 3;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean involvesTypeParameters() {
        if (this.involvesTypeParameters_computed) {
            return this.involvesTypeParameters_value;
        }
        ASTNode.State state = state();
        if (!this.involvesTypeParameters_initialized) {
            this.involvesTypeParameters_initialized = true;
            this.involvesTypeParameters_value = false;
        }
        if (!state.IN_CIRCLE) {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = is$Final();
            do {
                this.involvesTypeParameters_visited = state.CIRCLE_INDEX;
                state.CHANGE = false;
                boolean new_involvesTypeParameters_value = involvesTypeParameters_compute();
                if (new_involvesTypeParameters_value != this.involvesTypeParameters_value) {
                    state.CHANGE = true;
                }
                this.involvesTypeParameters_value = new_involvesTypeParameters_value;
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            if (isFinal && num == state().boundariesCrossed) {
                this.involvesTypeParameters_computed = true;
            } else {
                state.RESET_CYCLE = true;
                involvesTypeParameters_compute();
                state.RESET_CYCLE = false;
                this.involvesTypeParameters_computed = false;
                this.involvesTypeParameters_initialized = false;
            }
            state.IN_CIRCLE = false;
            return this.involvesTypeParameters_value;
        } else if (this.involvesTypeParameters_visited != state.CIRCLE_INDEX) {
            this.involvesTypeParameters_visited = state.CIRCLE_INDEX;
            if (state.RESET_CYCLE) {
                this.involvesTypeParameters_computed = false;
                this.involvesTypeParameters_initialized = false;
                this.involvesTypeParameters_visited = -1;
                return this.involvesTypeParameters_value;
            }
            boolean new_involvesTypeParameters_value2 = involvesTypeParameters_compute();
            if (new_involvesTypeParameters_value2 != this.involvesTypeParameters_value) {
                state.CHANGE = true;
            }
            this.involvesTypeParameters_value = new_involvesTypeParameters_value2;
            return this.involvesTypeParameters_value;
        } else {
            return this.involvesTypeParameters_value;
        }
    }

    private boolean involvesTypeParameters_compute() {
        for (int i = 0; i < getNumArgument(); i++) {
            if (getArgument(i).type().involvesTypeParameters()) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl hostType() {
        state();
        return original();
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ParTypeDecl, soot.JastAddJ.Parameterization
    public boolean isRawType() {
        state();
        return isNestedType() && enclosingType().isRawType();
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl erasure() {
        if (this.erasure_computed) {
            return this.erasure_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.erasure_value = erasure_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.erasure_computed = true;
        }
        return this.erasure_value;
    }

    private TypeDecl erasure_compute() {
        return genericDecl();
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public List getSuperInterfaceIdList() {
        if (this.getSuperInterfaceIdList_computed) {
            return (List) getChild(getSuperInterfaceIdListChildPosition());
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.getSuperInterfaceIdList_value = getSuperInterfaceIdList_compute();
        setSuperInterfaceIdList(this.getSuperInterfaceIdList_value);
        if (isFinal && num == state().boundariesCrossed) {
            this.getSuperInterfaceIdList_computed = true;
        }
        return (List) getChild(getSuperInterfaceIdListChildPosition());
    }

    private List getSuperInterfaceIdList_compute() {
        GenericInterfaceDecl decl = (GenericInterfaceDecl) genericDecl();
        List list = decl.getSuperInterfaceIdList().substitute(this);
        return list;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List getBodyDeclList() {
        if (this.getBodyDeclList_computed) {
            return (List) getChild(getBodyDeclListChildPosition());
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.getBodyDeclList_value = getBodyDeclList_compute();
        setBodyDeclList(this.getBodyDeclList_value);
        if (isFinal && num == state().boundariesCrossed) {
            this.getBodyDeclList_computed = true;
        }
        return (List) getChild(getBodyDeclListChildPosition());
    }

    private List getBodyDeclList_compute() {
        return new BodyDeclList();
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeGenericClassDecl(GenericClassDecl type) {
        state();
        return type.subtype(genericDecl().original());
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeGenericInterfaceDecl(GenericInterfaceDecl type) {
        state();
        return type.subtype(genericDecl().original());
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.TypeDecl
    public boolean supertypeClassDecl(ClassDecl type) {
        state();
        return super.supertypeClassDecl(type);
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
        return type.supertypeParInterfaceDecl(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeRawClassDecl(RawClassDecl type) {
        state();
        return type.genericDecl().original().subtype(genericDecl().original());
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeRawInterfaceDecl(RawInterfaceDecl type) {
        state();
        return type.genericDecl().original().subtype(genericDecl().original());
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean sameStructure(TypeDecl t) {
        ASTNode.State.CircularValue _value;
        boolean new_sameStructure_TypeDecl_value;
        if (this.sameStructure_TypeDecl_values == null) {
            this.sameStructure_TypeDecl_values = new HashMap(4);
        }
        if (this.sameStructure_TypeDecl_values.containsKey(t)) {
            Object _o = this.sameStructure_TypeDecl_values.get(t);
            if (!(_o instanceof ASTNode.State.CircularValue)) {
                return ((Boolean) _o).booleanValue();
            }
            _value = (ASTNode.State.CircularValue) _o;
        } else {
            _value = new ASTNode.State.CircularValue();
            this.sameStructure_TypeDecl_values.put(t, _value);
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
                new_sameStructure_TypeDecl_value = sameStructure_compute(t);
                if (new_sameStructure_TypeDecl_value != ((Boolean) _value.value).booleanValue()) {
                    state.CHANGE = true;
                    _value.value = Boolean.valueOf(new_sameStructure_TypeDecl_value);
                }
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            if (!isFinal || num != state().boundariesCrossed) {
                this.sameStructure_TypeDecl_values.remove(t);
                state.RESET_CYCLE = true;
                sameStructure_compute(t);
                state.RESET_CYCLE = false;
            } else {
                this.sameStructure_TypeDecl_values.put(t, Boolean.valueOf(new_sameStructure_TypeDecl_value));
            }
            state.IN_CIRCLE = false;
            return new_sameStructure_TypeDecl_value;
        } else if (!new Integer(state.CIRCLE_INDEX).equals(Integer.valueOf(_value.visited))) {
            _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
            boolean new_sameStructure_TypeDecl_value2 = sameStructure_compute(t);
            if (state.RESET_CYCLE) {
                this.sameStructure_TypeDecl_values.remove(t);
            } else if (new_sameStructure_TypeDecl_value2 != ((Boolean) _value.value).booleanValue()) {
                state.CHANGE = true;
                _value.value = Boolean.valueOf(new_sameStructure_TypeDecl_value2);
            }
            return new_sameStructure_TypeDecl_value2;
        } else {
            return ((Boolean) _value.value).booleanValue();
        }
    }

    private boolean sameStructure_compute(TypeDecl t) {
        if (!(t instanceof ParInterfaceDecl)) {
            return false;
        }
        ParInterfaceDecl type = (ParInterfaceDecl) t;
        if (type.genericDecl().original() == genericDecl().original() && type.getNumArgument() == getNumArgument()) {
            for (int i = 0; i < getNumArgument(); i++) {
                if (!type.getArgument(i).type().sameStructure(getArgument(i).type())) {
                    return false;
                }
            }
            if (isNestedType() && type.isNestedType()) {
                return type.enclosingType().sameStructure(enclosingType());
            }
            return true;
        }
        return false;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeParClassDecl(ParClassDecl type) {
        state();
        if (type.genericDecl().original() == genericDecl().original() && type.getNumArgument() == getNumArgument()) {
            for (int i = 0; i < getNumArgument(); i++) {
                if (!type.getArgument(i).type().containedIn(getArgument(i).type())) {
                    return false;
                }
            }
            if (isNestedType() && type.isNestedType()) {
                return type.enclosingType().subtype(enclosingType());
            }
            return true;
        }
        return supertypeClassDecl(type);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeParInterfaceDecl(ParInterfaceDecl type) {
        state();
        if (type.genericDecl().original() == genericDecl().original() && type.getNumArgument() == getNumArgument()) {
            for (int i = 0; i < getNumArgument(); i++) {
                if (!type.getArgument(i).type().containedIn(getArgument(i).type())) {
                    return false;
                }
            }
            if (isNestedType() && type.isNestedType()) {
                return type.enclosingType().subtype(enclosingType());
            }
            return true;
        }
        return supertypeInterfaceDecl(type);
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

    @Override // soot.JastAddJ.TypeDecl
    public boolean isReifiable() {
        state();
        if (isRawType()) {
            return true;
        }
        for (int i = 0; i < getNumArgument(); i++) {
            if (!getArgument(i).type().isWildcard()) {
                return false;
            }
        }
        return true;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ParTypeDecl
    public boolean isParameterizedType() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.ParTypeDecl
    public boolean sameArgument(ParTypeDecl decl) {
        state();
        if (this == decl) {
            return true;
        }
        if (genericDecl() != decl.genericDecl()) {
            return false;
        }
        for (int i = 0; i < getNumArgument(); i++) {
            TypeDecl t1 = getArgument(i).type();
            TypeDecl t2 = decl.getArgument(i).type();
            if ((t1 instanceof ParTypeDecl) && (t2 instanceof ParTypeDecl)) {
                if (!((ParTypeDecl) t1).sameArgument((ParTypeDecl) t2)) {
                    return false;
                }
            } else if (t1 != t2) {
                return false;
            }
        }
        return true;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ParTypeDecl
    public boolean sameSignature(Access a) {
        state();
        if (a instanceof ParTypeAccess) {
            ParTypeAccess ta = (ParTypeAccess) a;
            if (genericDecl() != ta.genericDecl() || getNumArgument() != ta.getNumTypeArgument()) {
                return false;
            }
            for (int i = 0; i < getNumArgument(); i++) {
                if (!getArgument(i).type().sameSignature(ta.getTypeArgument(i))) {
                    return false;
                }
            }
            return true;
        } else if ((a instanceof TypeAccess) && ((TypeAccess) a).isRaw()) {
            return false;
        } else {
            return super.sameSignature(a);
        }
    }

    @Override // soot.JastAddJ.ParTypeDecl
    public boolean sameSignature(ArrayList list) {
        ASTNode.State.CircularValue _value;
        boolean new_sameSignature_ArrayList_value;
        if (this.sameSignature_ArrayList_values == null) {
            this.sameSignature_ArrayList_values = new HashMap(4);
        }
        if (this.sameSignature_ArrayList_values.containsKey(list)) {
            Object _o = this.sameSignature_ArrayList_values.get(list);
            if (!(_o instanceof ASTNode.State.CircularValue)) {
                return ((Boolean) _o).booleanValue();
            }
            _value = (ASTNode.State.CircularValue) _o;
        } else {
            _value = new ASTNode.State.CircularValue();
            this.sameSignature_ArrayList_values.put(list, _value);
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
                new_sameSignature_ArrayList_value = sameSignature_compute(list);
                if (new_sameSignature_ArrayList_value != ((Boolean) _value.value).booleanValue()) {
                    state.CHANGE = true;
                    _value.value = Boolean.valueOf(new_sameSignature_ArrayList_value);
                }
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            if (!isFinal || num != state().boundariesCrossed) {
                this.sameSignature_ArrayList_values.remove(list);
                state.RESET_CYCLE = true;
                sameSignature_compute(list);
                state.RESET_CYCLE = false;
            } else {
                this.sameSignature_ArrayList_values.put(list, Boolean.valueOf(new_sameSignature_ArrayList_value));
            }
            state.IN_CIRCLE = false;
            return new_sameSignature_ArrayList_value;
        } else if (!new Integer(state.CIRCLE_INDEX).equals(Integer.valueOf(_value.visited))) {
            _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
            boolean new_sameSignature_ArrayList_value2 = sameSignature_compute(list);
            if (state.RESET_CYCLE) {
                this.sameSignature_ArrayList_values.remove(list);
            } else if (new_sameSignature_ArrayList_value2 != ((Boolean) _value.value).booleanValue()) {
                state.CHANGE = true;
                _value.value = Boolean.valueOf(new_sameSignature_ArrayList_value2);
            }
            return new_sameSignature_ArrayList_value2;
        } else {
            return ((Boolean) _value.value).booleanValue();
        }
    }

    private boolean sameSignature_compute(ArrayList list) {
        if (getNumArgument() != list.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (getArgument(i).type() != list.get(i)) {
                return false;
            }
        }
        return true;
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
        if (super.usesTypeVariable()) {
            return true;
        }
        for (int i = 0; i < getNumArgument(); i++) {
            if (getArgument(i).type().usesTypeVariable()) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.MemberSubstitutor
    public TypeDecl original() {
        state();
        return genericDecl().original();
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl sourceTypeDecl() {
        if (this.sourceTypeDecl_computed) {
            return this.sourceTypeDecl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.sourceTypeDecl_value = sourceTypeDecl_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.sourceTypeDecl_computed = true;
        }
        return this.sourceTypeDecl_value;
    }

    private TypeDecl sourceTypeDecl_compute() {
        return genericDecl().original().sourceTypeDecl();
    }

    @Override // soot.JastAddJ.TypeDecl
    public String fullName() {
        if (this.fullName_computed) {
            return this.fullName_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.fullName_value = fullName_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.fullName_computed = true;
        }
        return this.fullName_value;
    }

    private String fullName_compute() {
        if (isNestedType()) {
            return String.valueOf(enclosingType().fullName()) + "." + nameWithArgs();
        }
        String packageName = packageName();
        if (packageName.equals("")) {
            return nameWithArgs();
        }
        return String.valueOf(packageName) + "." + nameWithArgs();
    }

    @Override // soot.JastAddJ.TypeDecl
    public String typeName() {
        if (this.typeName_computed) {
            return this.typeName_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeName_value = typeName_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.typeName_computed = true;
        }
        return this.typeName_value;
    }

    private String typeName_compute() {
        if (isNestedType()) {
            return String.valueOf(enclosingType().typeName()) + "." + nameWithArgs();
        }
        String packageName = packageName();
        if (packageName.equals("") || packageName.equals("@primitive")) {
            return nameWithArgs();
        }
        return String.valueOf(packageName) + "." + nameWithArgs();
    }

    @Override // soot.JastAddJ.ParTypeDecl
    public String nameWithArgs() {
        state();
        StringBuffer s = new StringBuffer();
        s.append(name());
        s.append("<");
        for (int i = 0; i < getNumArgument(); i++) {
            if (i != 0) {
                s.append(", ");
            }
            s.append(getArgument(i).type().fullName());
        }
        s.append(">");
        return s.toString();
    }

    @Override // soot.JastAddJ.TypeDecl
    public Collection unimplementedMethods() {
        if (this.unimplementedMethods_computed) {
            return this.unimplementedMethods_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.unimplementedMethods_value = unimplementedMethods_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.unimplementedMethods_computed = true;
        }
        return this.unimplementedMethods_value;
    }

    private Collection unimplementedMethods_compute() {
        HashSet set = new HashSet();
        HashSet result = new HashSet();
        for (MethodDecl m : genericDecl().unimplementedMethods()) {
            set.add(m.sourceMethodDecl());
        }
        for (MethodDecl m2 : super.unimplementedMethods()) {
            if (set.contains(m2.sourceMethodDecl())) {
                result.add(m2);
            }
        }
        return result;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.MemberSubstitutor
    public HashMap localMethodsSignatureMap() {
        if (this.localMethodsSignatureMap_computed) {
            return this.localMethodsSignatureMap_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.localMethodsSignatureMap_value = localMethodsSignatureMap_compute();
        this.localMethodsSignatureMap_computed = true;
        return this.localMethodsSignatureMap_value;
    }

    private HashMap localMethodsSignatureMap_compute() {
        HashMap map = new HashMap();
        Iterator iter = original().localMethodsIterator();
        while (iter.hasNext()) {
            MethodDecl decl = (MethodDecl) iter.next();
            if (!decl.isStatic() && (decl.usesTypeVariable() || isRawType())) {
                BodyDecl copyDecl = ((BodyDeclList) getBodyDeclList()).localMethodSignatureCopy(decl, this);
                decl = (MethodDecl) copyDecl;
            }
            map.put(decl.signature(), decl);
        }
        return map;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.MemberSubstitutor
    public SimpleSet localFields(String name) {
        if (this.localFields_String_values == null) {
            this.localFields_String_values = new HashMap(4);
        }
        if (this.localFields_String_values.containsKey(name)) {
            return (SimpleSet) this.localFields_String_values.get(name);
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        SimpleSet localFields_String_value = localFields_compute(name);
        this.localFields_String_values.put(name, localFields_String_value);
        return localFields_String_value;
    }

    private SimpleSet localFields_compute(String name) {
        SimpleSet set = SimpleSet.emptySet;
        for (FieldDeclaration f : original().localFields(name)) {
            if (!f.isStatic() && (f.usesTypeVariable() || isRawType())) {
                BodyDecl fCopy = ((BodyDeclList) getBodyDeclList()).localFieldCopy(f, this);
                f = (FieldDeclaration) fCopy;
            }
            set = set.add(f);
        }
        return set;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.MemberSubstitutor
    public SimpleSet localTypeDecls(String name) {
        ASTNode.State.CircularValue _value;
        SimpleSet new_localTypeDecls_String_value;
        if (this.localTypeDecls_String_values == null) {
            this.localTypeDecls_String_values = new HashMap(4);
        }
        if (this.localTypeDecls_String_values.containsKey(name)) {
            Object _o = this.localTypeDecls_String_values.get(name);
            if (!(_o instanceof ASTNode.State.CircularValue)) {
                return (SimpleSet) _o;
            }
            _value = (ASTNode.State.CircularValue) _o;
        } else {
            _value = new ASTNode.State.CircularValue();
            this.localTypeDecls_String_values.put(name, _value);
            _value.value = SimpleSet.emptySet;
        }
        ASTNode.State state = state();
        if (!state.IN_CIRCLE) {
            state.IN_CIRCLE = true;
            int i = state.boundariesCrossed;
            is$Final();
            do {
                _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
                state.CHANGE = false;
                new_localTypeDecls_String_value = localTypeDecls_compute(name);
                if ((new_localTypeDecls_String_value == null && ((SimpleSet) _value.value) != null) || (new_localTypeDecls_String_value != null && !new_localTypeDecls_String_value.equals((SimpleSet) _value.value))) {
                    state.CHANGE = true;
                    _value.value = new_localTypeDecls_String_value;
                }
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            this.localTypeDecls_String_values.put(name, new_localTypeDecls_String_value);
            state.IN_CIRCLE = false;
            return new_localTypeDecls_String_value;
        } else if (!new Integer(state.CIRCLE_INDEX).equals(Integer.valueOf(_value.visited))) {
            _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
            SimpleSet new_localTypeDecls_String_value2 = localTypeDecls_compute(name);
            if (state.RESET_CYCLE) {
                this.localTypeDecls_String_values.remove(name);
            } else if ((new_localTypeDecls_String_value2 == null && ((SimpleSet) _value.value) != null) || (new_localTypeDecls_String_value2 != null && !new_localTypeDecls_String_value2.equals((SimpleSet) _value.value))) {
                state.CHANGE = true;
                _value.value = new_localTypeDecls_String_value2;
            }
            return new_localTypeDecls_String_value2;
        } else {
            return (SimpleSet) _value.value;
        }
    }

    private SimpleSet localTypeDecls_compute(String name) {
        SimpleSet set = SimpleSet.emptySet;
        for (TypeDecl t : original().localTypeDecls(name)) {
            if (t.isStatic()) {
                set = set.add(t);
            } else if (t instanceof ClassDecl) {
                MemberClassDecl copy = ((BodyDeclList) getBodyDeclList()).localClassDeclCopy((ClassDecl) t, this);
                set = set.add(copy.getClassDecl());
            } else if (t instanceof InterfaceDecl) {
                MemberInterfaceDecl copy2 = ((BodyDeclList) getBodyDeclList()).localInterfaceDeclCopy((InterfaceDecl) t, this);
                set = set.add(copy2.getInterfaceDecl());
            }
        }
        return set;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.MemberSubstitutor
    public Collection constructors() {
        if (this.constructors_computed) {
            return this.constructors_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.constructors_value = constructors_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.constructors_computed = true;
        }
        return this.constructors_value;
    }

    private Collection constructors_compute() {
        Collection set = new ArrayList();
        for (ConstructorDecl c : original().constructors()) {
            BodyDecl b = ((BodyDeclList) getBodyDeclList()).constructorCopy(c, this);
            set.add(b);
        }
        return set;
    }

    @Override // soot.JastAddJ.ParTypeDecl
    public TypeDecl genericDecl() {
        if (this.genericDecl_computed) {
            return this.genericDecl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.genericDecl_value = getParent().Define_TypeDecl_genericDecl(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.genericDecl_computed = true;
        }
        return this.genericDecl_value;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getArgumentListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.TYPE_NAME;
        }
        return super.Define_NameType_nameType(caller, child);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_genericDecl(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            int index = caller.getIndexOfChild(child);
            if (getBodyDecl(index) instanceof MemberTypeDecl) {
                MemberTypeDecl m = (MemberTypeDecl) getBodyDecl(index);
                return extractSingleType(genericDecl().memberTypes(m.typeDecl().name()));
            }
            return genericDecl();
        }
        return getParent().Define_TypeDecl_genericDecl(this, caller);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
