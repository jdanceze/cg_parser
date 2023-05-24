package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/WildcardSuperType.class */
public class WildcardSuperType extends AbstractWildcardType implements Cloneable {
    protected boolean involvesTypeParameters_value;
    protected boolean usesTypeVariable_value;
    protected Map subtype_TypeDecl_values;
    protected Map containedIn_TypeDecl_values;
    protected Map sameStructure_TypeDecl_values;
    protected Map instanceOf_TypeDecl_values;
    protected int involvesTypeParameters_visited = -1;
    protected boolean involvesTypeParameters_computed = false;
    protected boolean involvesTypeParameters_initialized = false;
    protected int usesTypeVariable_visited = -1;
    protected boolean usesTypeVariable_computed = false;
    protected boolean usesTypeVariable_initialized = false;

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.involvesTypeParameters_visited = -1;
        this.involvesTypeParameters_computed = false;
        this.involvesTypeParameters_initialized = false;
        this.usesTypeVariable_visited = -1;
        this.usesTypeVariable_computed = false;
        this.usesTypeVariable_initialized = false;
        this.subtype_TypeDecl_values = null;
        this.containedIn_TypeDecl_values = null;
        this.sameStructure_TypeDecl_values = null;
        this.instanceOf_TypeDecl_values = null;
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public WildcardSuperType clone() throws CloneNotSupportedException {
        WildcardSuperType node = (WildcardSuperType) super.clone();
        node.involvesTypeParameters_visited = -1;
        node.involvesTypeParameters_computed = false;
        node.involvesTypeParameters_initialized = false;
        node.usesTypeVariable_visited = -1;
        node.usesTypeVariable_computed = false;
        node.usesTypeVariable_initialized = false;
        node.subtype_TypeDecl_values = null;
        node.containedIn_TypeDecl_values = null;
        node.sameStructure_TypeDecl_values = null;
        node.instanceOf_TypeDecl_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            WildcardSuperType node = clone();
            node.parent = null;
            if (this.children != null) {
                node.children = (ASTNode[]) this.children.clone();
            }
            return node;
        } catch (CloneNotSupportedException e) {
            throw new Error("Error: clone not supported for " + getClass().getName());
        }
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: fullCopy */
    public ASTNode<ASTNode> fullCopy2() {
        ASTNode<ASTNode> copy2 = copy2();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ASTNode child = this.children[i];
                if (child != null) {
                    copy2.setChild(child.fullCopy(), i);
                }
            }
        }
        return copy2;
    }

    @Override // soot.JastAddJ.TypeDecl
    public Access substitute(Parameterization parTypeDecl) {
        if (!usesTypeVariable()) {
            return super.substitute(parTypeDecl);
        }
        return new WildcardSuper(getAccess().type().substitute(parTypeDecl));
    }

    public WildcardSuperType() {
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new List(), 1);
    }

    public WildcardSuperType(Modifiers p0, String p1, List<BodyDecl> p2, Access p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    public WildcardSuperType(Modifiers p0, Symbol p1, List<BodyDecl> p2, Access p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 1);
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public int getNumBodyDecl() {
        return getBodyDeclList().getNumChild();
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public int getNumBodyDeclNoTransform() {
        return getBodyDeclListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public BodyDecl getBodyDecl(int i) {
        return getBodyDeclList().getChild(i);
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public void addBodyDecl(BodyDecl node) {
        List<BodyDecl> list = (this.parent == null || state == null) ? getBodyDeclListNoTransform() : getBodyDeclList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public void addBodyDeclNoTransform(BodyDecl node) {
        List<BodyDecl> list = getBodyDeclListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public void setBodyDecl(BodyDecl node, int i) {
        List<BodyDecl> list = getBodyDeclList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDecls() {
        return getBodyDeclList();
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclsNoTransform() {
        return getBodyDeclListNoTransform();
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclList() {
        List<BodyDecl> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    public void setAccess(Access node) {
        setChild(node, 2);
    }

    public Access getAccess() {
        return (Access) getChild(2);
    }

    public Access getAccessNoTransform() {
        return (Access) getChildNoTransform(2);
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
        return superType().involvesTypeParameters();
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ParTypeDecl
    public boolean sameSignature(Access a) {
        state();
        if (a instanceof WildcardSuper) {
            return getAccess().type().sameSignature(((WildcardSuper) a).getAccess());
        }
        return super.sameSignature(a);
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
        return getAccess().type().usesTypeVariable();
    }

    public TypeDecl superType() {
        state();
        return getAccess().type();
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeWildcard(WildcardType type) {
        state();
        return superType().subtype(typeObject());
    }

    @Override // soot.JastAddJ.TypeDecl
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
        return type.supertypeWildcardSuper(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeWildcardSuper(WildcardSuperType type) {
        state();
        return type.superType().subtype(superType());
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeClassDecl(ClassDecl type) {
        state();
        return superType().subtype(type);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeInterfaceDecl(InterfaceDecl type) {
        state();
        return superType().subtype(type);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeParClassDecl(ParClassDecl type) {
        state();
        return superType().subtype(type);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeParInterfaceDecl(ParInterfaceDecl type) {
        state();
        return superType().subtype(type);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeRawClassDecl(RawClassDecl type) {
        state();
        return superType().subtype(type);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeRawInterfaceDecl(RawInterfaceDecl type) {
        state();
        return superType().subtype(type);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeTypeVariable(TypeVariable type) {
        state();
        return superType().subtype(type);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeArrayDecl(ArrayDecl type) {
        state();
        return superType().subtype(type);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean containedIn(TypeDecl type) {
        ASTNode.State.CircularValue _value;
        boolean new_containedIn_TypeDecl_value;
        if (this.containedIn_TypeDecl_values == null) {
            this.containedIn_TypeDecl_values = new HashMap(4);
        }
        if (this.containedIn_TypeDecl_values.containsKey(type)) {
            Object _o = this.containedIn_TypeDecl_values.get(type);
            if (!(_o instanceof ASTNode.State.CircularValue)) {
                return ((Boolean) _o).booleanValue();
            }
            _value = (ASTNode.State.CircularValue) _o;
        } else {
            _value = new ASTNode.State.CircularValue();
            this.containedIn_TypeDecl_values.put(type, _value);
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
                new_containedIn_TypeDecl_value = containedIn_compute(type);
                if (new_containedIn_TypeDecl_value != ((Boolean) _value.value).booleanValue()) {
                    state.CHANGE = true;
                    _value.value = Boolean.valueOf(new_containedIn_TypeDecl_value);
                }
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            if (!isFinal || num != state().boundariesCrossed) {
                this.containedIn_TypeDecl_values.remove(type);
                state.RESET_CYCLE = true;
                containedIn_compute(type);
                state.RESET_CYCLE = false;
            } else {
                this.containedIn_TypeDecl_values.put(type, Boolean.valueOf(new_containedIn_TypeDecl_value));
            }
            state.IN_CIRCLE = false;
            return new_containedIn_TypeDecl_value;
        } else if (!new Integer(state.CIRCLE_INDEX).equals(Integer.valueOf(_value.visited))) {
            _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
            boolean new_containedIn_TypeDecl_value2 = containedIn_compute(type);
            if (state.RESET_CYCLE) {
                this.containedIn_TypeDecl_values.remove(type);
            } else if (new_containedIn_TypeDecl_value2 != ((Boolean) _value.value).booleanValue()) {
                state.CHANGE = true;
                _value.value = Boolean.valueOf(new_containedIn_TypeDecl_value2);
            }
            return new_containedIn_TypeDecl_value2;
        } else {
            return ((Boolean) _value.value).booleanValue();
        }
    }

    private boolean containedIn_compute(TypeDecl type) {
        if (type == this || (type instanceof WildcardType)) {
            return true;
        }
        if (type instanceof WildcardSuperType) {
            return ((WildcardSuperType) type).superType().subtype(superType());
        }
        return false;
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
        if (super.sameStructure(t)) {
            return true;
        }
        return (t instanceof WildcardSuperType) && ((WildcardSuperType) t).superType().sameStructure(superType());
    }

    @Override // soot.JastAddJ.TypeDecl
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

    @Override // soot.JastAddJ.AbstractWildcardType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
