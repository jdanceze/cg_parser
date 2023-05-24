package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/InterfaceDeclSubstituted.class */
public class InterfaceDeclSubstituted extends InterfaceDecl implements Cloneable, MemberSubstitutor {
    protected TypeDecl tokenTypeDecl_Original;
    protected List getBodyDeclList_value;
    protected TypeDecl sourceTypeDecl_value;
    protected Map instanceOf_TypeDecl_values;
    protected Map subtype_TypeDecl_values;
    protected HashMap localMethodsSignatureMap_value;
    protected Map localFields_String_values;
    protected Map localTypeDecls_String_values;
    protected Collection constructors_value;
    protected boolean getBodyDeclList_computed = false;
    protected boolean sourceTypeDecl_computed = false;
    protected boolean localMethodsSignatureMap_computed = false;
    protected boolean constructors_computed = false;

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.getBodyDeclList_computed = false;
        this.getBodyDeclList_value = null;
        this.sourceTypeDecl_computed = false;
        this.sourceTypeDecl_value = null;
        this.instanceOf_TypeDecl_values = null;
        this.subtype_TypeDecl_values = null;
        this.localMethodsSignatureMap_computed = false;
        this.localMethodsSignatureMap_value = null;
        this.localFields_String_values = null;
        this.localTypeDecls_String_values = null;
        this.constructors_computed = false;
        this.constructors_value = null;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public InterfaceDeclSubstituted clone() throws CloneNotSupportedException {
        InterfaceDeclSubstituted node = (InterfaceDeclSubstituted) super.clone();
        node.getBodyDeclList_computed = false;
        node.getBodyDeclList_value = null;
        node.sourceTypeDecl_computed = false;
        node.sourceTypeDecl_value = null;
        node.instanceOf_TypeDecl_values = null;
        node.subtype_TypeDecl_values = null;
        node.localMethodsSignatureMap_computed = false;
        node.localMethodsSignatureMap_value = null;
        node.localFields_String_values = null;
        node.localTypeDecls_String_values = null;
        node.constructors_computed = false;
        node.constructors_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            InterfaceDeclSubstituted node = clone();
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
    /* JADX WARN: Type inference failed for: r0v1, types: [soot.JastAddJ.ASTNode<soot.JastAddJ.ASTNode>, soot.JastAddJ.InterfaceDeclSubstituted] */
    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> fullCopy() {
        ?? copy = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                switch (i) {
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

    public InterfaceDeclSubstituted() {
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new List(), 1);
        setChild(new List(), 2);
    }

    public InterfaceDeclSubstituted(Modifiers p0, String p1, List<Access> p2, TypeDecl p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setOriginal(p3);
    }

    public InterfaceDeclSubstituted(Modifiers p0, Symbol p1, List<Access> p2, TypeDecl p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setOriginal(p3);
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

    public void setOriginal(TypeDecl value) {
        this.tokenTypeDecl_Original = value;
    }

    public TypeDecl getOriginal() {
        return this.tokenTypeDecl_Original;
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
        return (List) getChildNoTransform(2);
    }

    protected int getBodyDeclListChildPosition() {
        return 2;
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl hostType() {
        state();
        return getOriginal();
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

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.MemberSubstitutor
    public TypeDecl original() {
        state();
        return getOriginal().original();
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
        return original().sourceTypeDecl();
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
        return type.supertypeInterfaceDeclSubstituted(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeInterfaceDeclSubstituted(InterfaceDeclSubstituted type) {
        state();
        return (original() == type.original() && type.enclosingType().subtype(enclosingType())) || super.supertypeInterfaceDeclSubstituted(type);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.TypeDecl
    public boolean supertypeInterfaceDecl(InterfaceDecl type) {
        state();
        return super.supertypeInterfaceDecl(type) || original().supertypeInterfaceDecl(type);
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

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
