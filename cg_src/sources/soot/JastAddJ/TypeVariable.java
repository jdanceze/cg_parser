package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/TypeVariable.class */
public class TypeVariable extends ReferenceType implements Cloneable {
    protected TypeDecl toInterface_value;
    protected boolean involvesTypeParameters_value;
    protected Map memberFields_String_values;
    protected Map castingConversionTo_TypeDecl_values;
    protected TypeDecl erasure_value;
    protected String fullName_value;
    protected TypeDecl lubType_value;
    protected boolean usesTypeVariable_value;
    protected Map accessibleFrom_TypeDecl_values;
    protected String typeName_value;
    protected Map sameStructure_TypeDecl_values;
    protected Map subtype_TypeDecl_values;
    protected Map getSubstitutedTypeBound_int_TypeDecl_values;
    protected Map instanceOf_TypeDecl_values;
    protected boolean toInterface_computed = false;
    protected int involvesTypeParameters_visited = -1;
    protected boolean involvesTypeParameters_computed = false;
    protected boolean involvesTypeParameters_initialized = false;
    protected boolean erasure_computed = false;
    protected boolean fullName_computed = false;
    protected boolean lubType_computed = false;
    protected int usesTypeVariable_visited = -1;
    protected boolean usesTypeVariable_computed = false;
    protected boolean usesTypeVariable_initialized = false;
    protected boolean typeName_computed = false;

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.toInterface_computed = false;
        this.toInterface_value = null;
        this.involvesTypeParameters_visited = -1;
        this.involvesTypeParameters_computed = false;
        this.involvesTypeParameters_initialized = false;
        this.memberFields_String_values = null;
        this.castingConversionTo_TypeDecl_values = null;
        this.erasure_computed = false;
        this.erasure_value = null;
        this.fullName_computed = false;
        this.fullName_value = null;
        this.lubType_computed = false;
        this.lubType_value = null;
        this.usesTypeVariable_visited = -1;
        this.usesTypeVariable_computed = false;
        this.usesTypeVariable_initialized = false;
        this.accessibleFrom_TypeDecl_values = null;
        this.typeName_computed = false;
        this.typeName_value = null;
        this.sameStructure_TypeDecl_values = null;
        this.subtype_TypeDecl_values = null;
        this.getSubstitutedTypeBound_int_TypeDecl_values = null;
        this.instanceOf_TypeDecl_values = null;
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public TypeVariable clone() throws CloneNotSupportedException {
        TypeVariable node = (TypeVariable) super.clone();
        node.toInterface_computed = false;
        node.toInterface_value = null;
        node.involvesTypeParameters_visited = -1;
        node.involvesTypeParameters_computed = false;
        node.involvesTypeParameters_initialized = false;
        node.memberFields_String_values = null;
        node.castingConversionTo_TypeDecl_values = null;
        node.erasure_computed = false;
        node.erasure_value = null;
        node.fullName_computed = false;
        node.fullName_value = null;
        node.lubType_computed = false;
        node.lubType_value = null;
        node.usesTypeVariable_visited = -1;
        node.usesTypeVariable_computed = false;
        node.usesTypeVariable_initialized = false;
        node.accessibleFrom_TypeDecl_values = null;
        node.typeName_computed = false;
        node.typeName_value = null;
        node.sameStructure_TypeDecl_values = null;
        node.subtype_TypeDecl_values = null;
        node.getSubstitutedTypeBound_int_TypeDecl_values = null;
        node.instanceOf_TypeDecl_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            TypeVariable node = clone();
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

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void nameCheck() {
        if (extractSingleType(lookupType(name())) != this) {
            error("*** Semantic Error: type variable " + name() + " is multiply declared");
        }
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void typeCheck() {
        TypeDecl type;
        if (!getTypeBound(0).type().isTypeVariable() && !getTypeBound(0).type().isClassDecl() && !getTypeBound(0).type().isInterfaceDecl()) {
            error("the first type bound must be either a type variable, or a class or interface type which " + getTypeBound(0).type().fullName() + " is not");
        }
        for (int i = 1; i < getNumTypeBound(); i++) {
            if (!getTypeBound(i).type().isInterfaceDecl()) {
                error("type bound " + i + " must be an interface type which " + getTypeBound(i).type().fullName() + " is not");
            }
        }
        HashSet typeSet = new HashSet();
        for (int i2 = 0; i2 < getNumTypeBound(); i2++) {
            TypeDecl type2 = getTypeBound(i2).type();
            TypeDecl erasure = type2.erasure();
            if (typeSet.contains(erasure)) {
                if (type2 != erasure) {
                    error("the erasure " + erasure.fullName() + " of typebound " + getTypeBound(i2) + " is multiply declared in " + this);
                } else {
                    error(String.valueOf(type2.fullName()) + " is multiply declared");
                }
            }
            typeSet.add(erasure);
        }
        for (int i3 = 0; i3 < getNumTypeBound(); i3++) {
            Iterator iter = getTypeBound(i3).type().methodsIterator();
            while (iter.hasNext()) {
                MethodDecl m = (MethodDecl) iter.next();
                for (int j = i3 + 1; j < getNumTypeBound(); j++) {
                    TypeDecl destType = getTypeBound(j).type();
                    for (MethodDecl n : destType.memberMethods(m.name())) {
                        if (m.sameSignature(n) && m.type() != n.type()) {
                            error("the two bounds, " + type.name() + " and " + destType.name() + ", in type variable " + name() + " have a method " + m.signature() + " with conflicting return types " + m.type().name() + " and " + n.type().name());
                        }
                    }
                }
            }
        }
    }

    @Override // soot.JastAddJ.TypeDecl
    public Access substitute(Parameterization parTypeDecl) {
        if (parTypeDecl.isRawType()) {
            return erasure().createBoundAccess();
        }
        return parTypeDecl.substitute(this).createBoundAccess();
    }

    @Override // soot.JastAddJ.TypeDecl
    public Access substituteReturnType(Parameterization parTypeDecl) {
        if (parTypeDecl.isRawType()) {
            return erasure().createBoundAccess();
        }
        TypeDecl typeDecl = parTypeDecl.substitute(this);
        if (typeDecl instanceof WildcardType) {
            return createBoundAccess();
        }
        if (typeDecl instanceof WildcardExtendsType) {
            if (typeDecl.instanceOf(this)) {
                return ((WildcardExtendsType) typeDecl).extendsType().createBoundAccess();
            }
            return createBoundAccess();
        } else if (typeDecl instanceof WildcardSuperType) {
            return createBoundAccess();
        } else {
            return typeDecl.createBoundAccess();
        }
    }

    @Override // soot.JastAddJ.TypeDecl
    public Access substituteParameterType(Parameterization parTypeDecl) {
        if (parTypeDecl.isRawType()) {
            return erasure().createBoundAccess();
        }
        TypeDecl typeDecl = parTypeDecl.substitute(this);
        if (typeDecl instanceof WildcardType) {
            return typeNull().createQualifiedAccess();
        }
        if (typeDecl instanceof WildcardExtendsType) {
            return typeNull().createQualifiedAccess();
        }
        if (typeDecl instanceof WildcardSuperType) {
            return ((WildcardSuperType) typeDecl).superType().createBoundAccess();
        }
        return typeDecl.createBoundAccess();
    }

    @Override // soot.JastAddJ.TypeDecl
    public Access createQualifiedAccess() {
        return createBoundAccess();
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(name());
        if (getNumTypeBound() > 0) {
            s.append(" extends ");
            s.append(getTypeBound(0).type().fullName());
            for (int i = 1; i < getNumTypeBound(); i++) {
                s.append(" & ");
                s.append(getTypeBound(i).type().fullName());
            }
        }
    }

    public TypeVariable() {
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new List(), 1);
        setChild(new List(), 2);
    }

    public TypeVariable(Modifiers p0, String p1, List<BodyDecl> p2, List<Access> p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    public TypeVariable(Modifiers p0, Symbol p1, List<BodyDecl> p2, List<Access> p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return true;
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 1);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public int getNumBodyDecl() {
        return getBodyDeclList().getNumChild();
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public int getNumBodyDeclNoTransform() {
        return getBodyDeclListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public BodyDecl getBodyDecl(int i) {
        return getBodyDeclList().getChild(i);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void addBodyDecl(BodyDecl node) {
        List<BodyDecl> list = (this.parent == null || state == null) ? getBodyDeclListNoTransform() : getBodyDeclList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void addBodyDeclNoTransform(BodyDecl node) {
        List<BodyDecl> list = getBodyDeclListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDecl(BodyDecl node, int i) {
        List<BodyDecl> list = getBodyDeclList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDecls() {
        return getBodyDeclList();
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclsNoTransform() {
        return getBodyDeclListNoTransform();
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclList() {
        List<BodyDecl> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    public void setTypeBoundList(List<Access> list) {
        setChild(list, 2);
    }

    public int getNumTypeBound() {
        return getTypeBoundList().getNumChild();
    }

    public int getNumTypeBoundNoTransform() {
        return getTypeBoundListNoTransform().getNumChildNoTransform();
    }

    public Access getTypeBound(int i) {
        return getTypeBoundList().getChild(i);
    }

    public void addTypeBound(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getTypeBoundListNoTransform() : getTypeBoundList();
        list.addChild(node);
    }

    public void addTypeBoundNoTransform(Access node) {
        List<Access> list = getTypeBoundListNoTransform();
        list.addChild(node);
    }

    public void setTypeBound(Access node, int i) {
        List<Access> list = getTypeBoundList();
        list.setChild(node, i);
    }

    public List<Access> getTypeBounds() {
        return getTypeBoundList();
    }

    public List<Access> getTypeBoundsNoTransform() {
        return getTypeBoundListNoTransform();
    }

    public List<Access> getTypeBoundList() {
        List<Access> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    public List<Access> getTypeBoundListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    public TypeDecl toInterface() {
        if (this.toInterface_computed) {
            return this.toInterface_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.toInterface_value = toInterface_compute();
        this.toInterface_value.setParent(this);
        this.toInterface_value.is$Final = true;
        this.toInterface_computed = true;
        return this.toInterface_value;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v11, types: [soot.JastAddJ.MethodDecl, soot.JastAddJ.BodyDecl] */
    private TypeDecl toInterface_compute() {
        InterfaceDecl ITj = new InterfaceDecl();
        ITj.setID("ITj_" + hashCode());
        for (int i = 0; i < getNumTypeBound(); i++) {
            TypeDecl bound = getTypeBound(i).type();
            for (int j = 0; j < bound.getNumBodyDecl(); j++) {
                BodyDecl bd = bound.getBodyDecl(j);
                if (bd instanceof FieldDeclaration) {
                    FieldDeclaration fd = (FieldDeclaration) bd.fullCopy();
                    if (fd.isPublic()) {
                        ITj.addBodyDecl(fd);
                    }
                } else if (bd instanceof MethodDecl) {
                    MethodDecl md = (MethodDecl) bd;
                    if (md.isPublic()) {
                        ITj.addBodyDecl(md.fullCopy());
                    }
                }
            }
        }
        return ITj;
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
        return true;
    }

    public TypeDecl lowerBound() {
        state();
        return getTypeBound(0).type();
    }

    @Override // soot.JastAddJ.TypeDecl
    public Collection memberMethods(String name) {
        state();
        Collection list = new HashSet();
        for (int i = 0; i < getNumTypeBound(); i++) {
            for (MethodDecl decl : getTypeBound(i).type().memberMethods(name)) {
                list.add(decl);
            }
        }
        return list;
    }

    @Override // soot.JastAddJ.TypeDecl
    public SimpleSet memberFields(String name) {
        if (this.memberFields_String_values == null) {
            this.memberFields_String_values = new HashMap(4);
        }
        if (this.memberFields_String_values.containsKey(name)) {
            return (SimpleSet) this.memberFields_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet memberFields_String_value = memberFields_compute(name);
        if (isFinal && num == state().boundariesCrossed) {
            this.memberFields_String_values.put(name, memberFields_String_value);
        }
        return memberFields_String_value;
    }

    private SimpleSet memberFields_compute(String name) {
        SimpleSet set = SimpleSet.emptySet;
        for (int i = 0; i < getNumTypeBound(); i++) {
            for (FieldDeclaration decl : getTypeBound(i).type().memberFields(name)) {
                set = set.add(decl);
            }
        }
        return set;
    }

    @Override // soot.JastAddJ.TypeDecl
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
        if (!type.isReferenceType()) {
            return false;
        }
        if (getNumTypeBound() == 0) {
            return true;
        }
        for (int i = 0; i < getNumTypeBound(); i++) {
            if (getTypeBound(i).type().castingConversionTo(type)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isNestedType() {
        state();
        return false;
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
        return getTypeBound(0).type().erasure();
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
        if (getParent().getParent() instanceof TypeDecl) {
            TypeDecl typeDecl = (TypeDecl) getParent().getParent();
            return String.valueOf(typeDecl.fullName()) + "@" + name();
        }
        return super.fullName();
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ParTypeDecl
    public boolean sameSignature(Access a) {
        state();
        return a.type() == this;
    }

    public TypeDecl lubType() {
        if (this.lubType_computed) {
            return this.lubType_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.lubType_value = lubType_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.lubType_computed = true;
        }
        return this.lubType_value;
    }

    private TypeDecl lubType_compute() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < getNumTypeBound(); i++) {
            list.add(getTypeBound(i).type());
        }
        return lookupLUBType(list);
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
        return true;
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
        return name();
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isTypeVariable() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeWildcard(WildcardType type) {
        state();
        return true;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeWildcardExtends(WildcardExtendsType type) {
        state();
        return type.extendsType().subtype(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeWildcardSuper(WildcardSuperType type) {
        state();
        return type.superType().subtype(this);
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
        if (!(t instanceof TypeVariable)) {
            return false;
        }
        if (t == this) {
            return true;
        }
        TypeVariable type = (TypeVariable) t;
        if (type.getNumTypeBound() != getNumTypeBound()) {
            return false;
        }
        for (int i = 0; i < getNumTypeBound(); i++) {
            boolean found = false;
            for (int j = i; !found && j < getNumTypeBound(); j++) {
                if (getTypeBound(i).type().sameStructure(type.getTypeBound(j).type())) {
                    found = true;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeArrayDecl(ArrayDecl type) {
        state();
        for (int i = 0; i < getNumTypeBound(); i++) {
            if (type.subtype(getTypeBound(i).type())) {
                return true;
            }
        }
        return false;
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
        return type.supertypeTypeVariable(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeTypeVariable(TypeVariable type) {
        state();
        if (type == this) {
            return true;
        }
        for (int i = 0; i < getNumTypeBound(); i++) {
            boolean found = false;
            for (int j = 0; !found && j < type.getNumTypeBound(); j++) {
                if (type.getSubstitutedTypeBound(j, this).type().subtype(getTypeBound(i).type())) {
                    found = true;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    public Access getSubstitutedTypeBound(int i, TypeDecl type) {
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(Integer.valueOf(i));
        arrayList.add(type);
        if (this.getSubstitutedTypeBound_int_TypeDecl_values == null) {
            this.getSubstitutedTypeBound_int_TypeDecl_values = new HashMap(4);
        }
        if (this.getSubstitutedTypeBound_int_TypeDecl_values.containsKey(arrayList)) {
            return (Access) this.getSubstitutedTypeBound_int_TypeDecl_values.get(arrayList);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        Access getSubstitutedTypeBound_int_TypeDecl_value = getSubstitutedTypeBound_compute(i, type);
        if (isFinal && num == state().boundariesCrossed) {
            this.getSubstitutedTypeBound_int_TypeDecl_values.put(arrayList, getSubstitutedTypeBound_int_TypeDecl_value);
        }
        return getSubstitutedTypeBound_int_TypeDecl_value;
    }

    private Access getSubstitutedTypeBound_compute(int i, final TypeDecl type) {
        Access bound = getTypeBound(i);
        if (!bound.type().usesTypeVariable()) {
            return bound;
        }
        Access access = bound.type().substitute(new Parameterization() { // from class: soot.JastAddJ.TypeVariable.1
            @Override // soot.JastAddJ.Parameterization
            public boolean isRawType() {
                return false;
            }

            @Override // soot.JastAddJ.Parameterization
            public TypeDecl substitute(TypeVariable typeVariable) {
                return typeVariable == TypeVariable.this ? type : typeVariable;
            }
        });
        access.setParent(this);
        return access;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeClassDecl(ClassDecl type) {
        state();
        for (int i = 0; i < getNumTypeBound(); i++) {
            if (!type.subtype(getSubstitutedTypeBound(i, type).type())) {
                return false;
            }
        }
        return true;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeInterfaceDecl(InterfaceDecl type) {
        state();
        for (int i = 0; i < getNumTypeBound(); i++) {
            if (!type.subtype(getSubstitutedTypeBound(i, type).type())) {
                return false;
            }
        }
        return true;
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

    @Override // soot.JastAddJ.TypeDecl
    public boolean isReifiable() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl typeObject() {
        state();
        TypeDecl typeObject_value = getParent().Define_TypeDecl_typeObject(this, null);
        return typeObject_value;
    }

    public TypeDecl typeNull() {
        state();
        TypeDecl typeNull_value = getParent().Define_TypeDecl_typeNull(this, null);
        return typeNull_value;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getTypeBoundListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.TYPE_NAME;
        }
        return super.Define_NameType_nameType(caller, child);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        if (getNumTypeBound() == 0) {
            state().duringGenericTypeVariables++;
            ASTNode result = rewriteRule0();
            state().duringGenericTypeVariables--;
            return result;
        }
        return super.rewriteTo();
    }

    private TypeVariable rewriteRule0() {
        addTypeBound(new TypeAccess("java.lang", "Object"));
        return this;
    }
}
