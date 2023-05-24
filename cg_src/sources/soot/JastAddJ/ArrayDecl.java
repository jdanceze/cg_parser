package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Map;
import soot.ArrayType;
import soot.JastAddJ.ASTNode;
import soot.SootClass;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ArrayDecl.class */
public class ArrayDecl extends ClassDecl implements Cloneable {
    protected Map accessibleFrom_TypeDecl_values;
    protected int dimension_value;
    protected TypeDecl elementType_value;
    protected String fullName_value;
    protected String typeName_value;
    protected Map castingConversionTo_TypeDecl_values;
    protected Map instanceOf_TypeDecl_values;
    protected boolean involvesTypeParameters_value;
    protected TypeDecl erasure_value;
    protected boolean usesTypeVariable_value;
    protected Map subtype_TypeDecl_values;
    protected String jvmName_value;
    protected SootClass getSootClassDecl_value;
    protected Type getSootType_value;
    protected boolean dimension_computed = false;
    protected boolean elementType_computed = false;
    protected boolean fullName_computed = false;
    protected boolean typeName_computed = false;
    protected int involvesTypeParameters_visited = -1;
    protected boolean involvesTypeParameters_computed = false;
    protected boolean involvesTypeParameters_initialized = false;
    protected boolean erasure_computed = false;
    protected int usesTypeVariable_visited = -1;
    protected boolean usesTypeVariable_computed = false;
    protected boolean usesTypeVariable_initialized = false;
    protected boolean jvmName_computed = false;
    protected boolean getSootClassDecl_computed = false;
    protected boolean getSootType_computed = false;

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.accessibleFrom_TypeDecl_values = null;
        this.dimension_computed = false;
        this.elementType_computed = false;
        this.elementType_value = null;
        this.fullName_computed = false;
        this.fullName_value = null;
        this.typeName_computed = false;
        this.typeName_value = null;
        this.castingConversionTo_TypeDecl_values = null;
        this.instanceOf_TypeDecl_values = null;
        this.involvesTypeParameters_visited = -1;
        this.involvesTypeParameters_computed = false;
        this.involvesTypeParameters_initialized = false;
        this.erasure_computed = false;
        this.erasure_value = null;
        this.usesTypeVariable_visited = -1;
        this.usesTypeVariable_computed = false;
        this.usesTypeVariable_initialized = false;
        this.subtype_TypeDecl_values = null;
        this.jvmName_computed = false;
        this.jvmName_value = null;
        this.getSootClassDecl_computed = false;
        this.getSootClassDecl_value = null;
        this.getSootType_computed = false;
        this.getSootType_value = null;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public ArrayDecl clone() throws CloneNotSupportedException {
        ArrayDecl node = (ArrayDecl) super.clone();
        node.accessibleFrom_TypeDecl_values = null;
        node.dimension_computed = false;
        node.elementType_computed = false;
        node.elementType_value = null;
        node.fullName_computed = false;
        node.fullName_value = null;
        node.typeName_computed = false;
        node.typeName_value = null;
        node.castingConversionTo_TypeDecl_values = null;
        node.instanceOf_TypeDecl_values = null;
        node.involvesTypeParameters_visited = -1;
        node.involvesTypeParameters_computed = false;
        node.involvesTypeParameters_initialized = false;
        node.erasure_computed = false;
        node.erasure_value = null;
        node.usesTypeVariable_visited = -1;
        node.usesTypeVariable_computed = false;
        node.usesTypeVariable_initialized = false;
        node.subtype_TypeDecl_values = null;
        node.jvmName_computed = false;
        node.jvmName_value = null;
        node.getSootClassDecl_computed = false;
        node.getSootClassDecl_value = null;
        node.getSootType_computed = false;
        node.getSootType_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            ArrayDecl node = clone();
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
    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ASTNode
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

    @Override // soot.JastAddJ.TypeDecl
    public Access createQualifiedAccess() {
        return new ArrayTypeAccess(componentType().createQualifiedAccess());
    }

    @Override // soot.JastAddJ.TypeDecl
    public Access substitute(Parameterization parTypeDecl) {
        return new ArrayTypeAccess(componentType().substitute(parTypeDecl));
    }

    @Override // soot.JastAddJ.TypeDecl
    public Access substituteReturnType(Parameterization parTypeDecl) {
        return new ArrayTypeAccess(componentType().substituteReturnType(parTypeDecl));
    }

    public ArrayDecl() {
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[4];
        setChild(new Opt(), 1);
        setChild(new List(), 2);
        setChild(new List(), 3);
    }

    public ArrayDecl(Modifiers p0, String p1, Opt<Access> p2, List<Access> p3, List<BodyDecl> p4) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
        setChild(p4, 3);
    }

    public ArrayDecl(Modifiers p0, Symbol p1, Opt<Access> p2, List<Access> p3, List<BodyDecl> p4) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
        setChild(p4, 3);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 4;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.ClassDecl
    public void setSuperClassAccessOpt(Opt<Access> opt) {
        setChild(opt, 1);
    }

    @Override // soot.JastAddJ.ClassDecl
    public boolean hasSuperClassAccess() {
        return getSuperClassAccessOpt().getNumChild() != 0;
    }

    @Override // soot.JastAddJ.ClassDecl
    public Access getSuperClassAccess() {
        return getSuperClassAccessOpt().getChild(0);
    }

    @Override // soot.JastAddJ.ClassDecl
    public void setSuperClassAccess(Access node) {
        getSuperClassAccessOpt().setChild(node, 0);
    }

    @Override // soot.JastAddJ.ClassDecl
    public Opt<Access> getSuperClassAccessOpt() {
        return (Opt) getChild(1);
    }

    @Override // soot.JastAddJ.ClassDecl
    public Opt<Access> getSuperClassAccessOptNoTransform() {
        return (Opt) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.ClassDecl
    public void setImplementsList(List<Access> list) {
        setChild(list, 2);
    }

    @Override // soot.JastAddJ.ClassDecl
    public int getNumImplements() {
        return getImplementsList().getNumChild();
    }

    @Override // soot.JastAddJ.ClassDecl
    public int getNumImplementsNoTransform() {
        return getImplementsListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ClassDecl
    public Access getImplements(int i) {
        return getImplementsList().getChild(i);
    }

    @Override // soot.JastAddJ.ClassDecl
    public void addImplements(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getImplementsListNoTransform() : getImplementsList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ClassDecl
    public void addImplementsNoTransform(Access node) {
        List<Access> list = getImplementsListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ClassDecl
    public void setImplements(Access node, int i) {
        List<Access> list = getImplementsList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ClassDecl
    public List<Access> getImplementss() {
        return getImplementsList();
    }

    @Override // soot.JastAddJ.ClassDecl
    public List<Access> getImplementssNoTransform() {
        return getImplementsListNoTransform();
    }

    @Override // soot.JastAddJ.ClassDecl
    public List<Access> getImplementsList() {
        List<Access> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ClassDecl
    public List<Access> getImplementsListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 3);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public int getNumBodyDecl() {
        return getBodyDeclList().getNumChild();
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public int getNumBodyDeclNoTransform() {
        return getBodyDeclListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public BodyDecl getBodyDecl(int i) {
        return getBodyDeclList().getChild(i);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void addBodyDecl(BodyDecl node) {
        List<BodyDecl> list = (this.parent == null || state == null) ? getBodyDeclListNoTransform() : getBodyDeclList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void addBodyDeclNoTransform(BodyDecl node) {
        List<BodyDecl> list = getBodyDeclListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDecl(BodyDecl node, int i) {
        List<BodyDecl> list = getBodyDeclList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDecls() {
        return getBodyDeclList();
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclsNoTransform() {
        return getBodyDeclListNoTransform();
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclList() {
        List<BodyDecl> list = (List) getChild(3);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(3);
    }

    private boolean refined_TypeConversion_ArrayDecl_castingConversionTo_TypeDecl(TypeDecl type) {
        if (type.isArrayDecl()) {
            TypeDecl SC = componentType();
            TypeDecl TC = type.componentType();
            if (SC.isPrimitiveType() && TC.isPrimitiveType() && SC == TC) {
                return true;
            }
            if (SC.isReferenceType() && TC.isReferenceType()) {
                return SC.castingConversionTo(TC);
            }
            return false;
        } else if (type.isClassDecl()) {
            return type.isObject();
        } else {
            if (type.isInterfaceDecl()) {
                return type == typeSerializable() || type == typeCloneable();
            }
            return super.castingConversionTo(type);
        }
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean accessibleFrom(TypeDecl type) {
        if (this.accessibleFrom_TypeDecl_values == null) {
            this.accessibleFrom_TypeDecl_values = new HashMap(4);
        }
        if (this.accessibleFrom_TypeDecl_values.containsKey(type)) {
            return ((Boolean) this.accessibleFrom_TypeDecl_values.get(type)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean accessibleFrom_TypeDecl_value = accessibleFrom_compute(type);
        if (isFinal && num == state().boundariesCrossed) {
            this.accessibleFrom_TypeDecl_values.put(type, Boolean.valueOf(accessibleFrom_TypeDecl_value));
        }
        return accessibleFrom_TypeDecl_value;
    }

    private boolean accessibleFrom_compute(TypeDecl type) {
        return elementType().accessibleFrom(type);
    }

    @Override // soot.JastAddJ.TypeDecl
    public int dimension() {
        if (this.dimension_computed) {
            return this.dimension_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.dimension_value = dimension_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.dimension_computed = true;
        }
        return this.dimension_value;
    }

    private int dimension_compute() {
        return componentType().dimension() + 1;
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl elementType() {
        if (this.elementType_computed) {
            return this.elementType_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.elementType_value = elementType_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.elementType_computed = true;
        }
        return this.elementType_value;
    }

    private TypeDecl elementType_compute() {
        return componentType().elementType();
    }

    @Override // soot.JastAddJ.TypeDecl
    public String name() {
        state();
        return fullName();
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
        return getID();
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
        return String.valueOf(componentType().typeName()) + "[]";
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.TypeDecl
    public boolean castingConversionTo(TypeDecl type) {
        if (this.castingConversionTo_TypeDecl_values == null) {
            this.castingConversionTo_TypeDecl_values = new HashMap(4);
        }
        if (this.castingConversionTo_TypeDecl_values.containsKey(type)) {
            return ((Boolean) this.castingConversionTo_TypeDecl_values.get(type)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean castingConversionTo_TypeDecl_value = castingConversionTo_compute(type);
        if (isFinal && num == state().boundariesCrossed) {
            this.castingConversionTo_TypeDecl_values.put(type, Boolean.valueOf(castingConversionTo_TypeDecl_value));
        }
        return castingConversionTo_TypeDecl_value;
    }

    private boolean castingConversionTo_compute(TypeDecl type) {
        if (type instanceof TypeVariable) {
            TypeVariable t = (TypeVariable) type;
            if (!type.isReferenceType()) {
                return false;
            }
            if (t.getNumTypeBound() == 0) {
                return true;
            }
            for (int i = 0; i < t.getNumTypeBound(); i++) {
                TypeDecl bound = t.getTypeBound(i).type();
                if (bound.isObject() || bound == typeSerializable() || bound == typeCloneable()) {
                    return true;
                }
                if (bound.isTypeVariable() && castingConversionTo(bound)) {
                    return true;
                }
                if (bound.isArrayDecl() && castingConversionTo(bound)) {
                    return true;
                }
            }
            return false;
        }
        return refined_TypeConversion_ArrayDecl_castingConversionTo_TypeDecl(type);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isArrayDecl() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.TypeDecl
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

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.TypeDecl
    public boolean isSupertypeOfArrayDecl(ArrayDecl type) {
        state();
        if (type.elementType().isPrimitive() && elementType().isPrimitive()) {
            return type.dimension() == dimension() && type.elementType() == elementType();
        }
        return type.componentType().instanceOf(componentType());
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public boolean isValidAnnotationMethodReturnType() {
        state();
        return componentType().isValidAnnotationMethodReturnType();
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean commensurateWith(ElementValue value) {
        state();
        return value.commensurateWithArrayDecl(this);
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
        return componentType().involvesTypeParameters();
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
        return componentType().erasure().arrayType();
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
        return elementType().usesTypeVariable();
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.TypeDecl
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
        return type.supertypeArrayDecl(this);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.TypeDecl
    public boolean supertypeArrayDecl(ArrayDecl type) {
        state();
        if (type.elementType().isPrimitive() && elementType().isPrimitive()) {
            return type.dimension() == dimension() && type.elementType() == elementType();
        }
        return type.componentType().subtype(componentType());
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public String jvmName() {
        if (this.jvmName_computed) {
            return this.jvmName_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.jvmName_value = jvmName_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.jvmName_computed = true;
        }
        return this.jvmName_value;
    }

    private String jvmName_compute() {
        StringBuffer dim = new StringBuffer();
        for (int i = 0; i < dimension(); i++) {
            dim.append("[");
        }
        if (elementType().isReferenceType()) {
            return String.valueOf(dim.toString()) + "L" + elementType().jvmName() + ";";
        }
        return String.valueOf(dim.toString()) + elementType().jvmName();
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public String referenceClassFieldName() {
        state();
        return "array" + jvmName().replace('[', '$').replace('.', '$').replace(';', ' ').trim();
    }

    @Override // soot.JastAddJ.TypeDecl
    public SootClass getSootClassDecl() {
        if (this.getSootClassDecl_computed) {
            return this.getSootClassDecl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.getSootClassDecl_value = getSootClassDecl_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.getSootClassDecl_computed = true;
        }
        return this.getSootClassDecl_value;
    }

    private SootClass getSootClassDecl_compute() {
        return typeObject().getSootClassDecl();
    }

    @Override // soot.JastAddJ.TypeDecl
    public Type getSootType() {
        if (this.getSootType_computed) {
            return this.getSootType_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.getSootType_value = getSootType_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.getSootType_computed = true;
        }
        return this.getSootType_value;
    }

    private Type getSootType_compute() {
        return ArrayType.v(elementType().getSootType(), dimension());
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isReifiable() {
        state();
        return elementType().isReifiable();
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl typeSerializable() {
        state();
        TypeDecl typeSerializable_value = getParent().Define_TypeDecl_typeSerializable(this, null);
        return typeSerializable_value;
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl typeCloneable() {
        state();
        TypeDecl typeCloneable_value = getParent().Define_TypeDecl_typeCloneable(this, null);
        return typeCloneable_value;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
