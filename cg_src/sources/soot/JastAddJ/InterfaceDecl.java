package soot.JastAddJ;

import beaver.Symbol;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.JastAddJ.Signatures;
import soot.SootClass;
import soot.SootResolver;
import soot.tagkit.SourceFileTag;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/InterfaceDecl.class */
public class InterfaceDecl extends ReferenceType implements Cloneable {
    protected HashMap methodsSignatureMap_value;
    protected Map ancestorMethods_String_values;
    protected Map memberTypes_String_values;
    protected HashMap memberFieldsMap_value;
    protected Map memberFields_String_values;
    protected boolean isStatic_value;
    protected Map castingConversionTo_TypeDecl_values;
    protected Map instanceOf_TypeDecl_values;
    protected boolean isCircular_value;
    protected HashSet implementedInterfaces_value;
    protected Map subtype_TypeDecl_values;
    protected SootClass sootClass_value;
    private TypeDecl methodHolder = null;
    protected boolean methodsSignatureMap_computed = false;
    protected boolean memberFieldsMap_computed = false;
    protected boolean isStatic_computed = false;
    protected int isCircular_visited = -1;
    protected boolean isCircular_computed = false;
    protected boolean isCircular_initialized = false;
    protected boolean implementedInterfaces_computed = false;
    protected boolean sootClass_computed = false;

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.methodsSignatureMap_computed = false;
        this.methodsSignatureMap_value = null;
        this.ancestorMethods_String_values = null;
        this.memberTypes_String_values = null;
        this.memberFieldsMap_computed = false;
        this.memberFieldsMap_value = null;
        this.memberFields_String_values = null;
        this.isStatic_computed = false;
        this.castingConversionTo_TypeDecl_values = null;
        this.instanceOf_TypeDecl_values = null;
        this.isCircular_visited = -1;
        this.isCircular_computed = false;
        this.isCircular_initialized = false;
        this.implementedInterfaces_computed = false;
        this.implementedInterfaces_value = null;
        this.subtype_TypeDecl_values = null;
        this.sootClass_computed = false;
        this.sootClass_value = null;
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public InterfaceDecl clone() throws CloneNotSupportedException {
        InterfaceDecl node = (InterfaceDecl) super.clone();
        node.methodsSignatureMap_computed = false;
        node.methodsSignatureMap_value = null;
        node.ancestorMethods_String_values = null;
        node.memberTypes_String_values = null;
        node.memberFieldsMap_computed = false;
        node.memberFieldsMap_value = null;
        node.memberFields_String_values = null;
        node.isStatic_computed = false;
        node.castingConversionTo_TypeDecl_values = null;
        node.instanceOf_TypeDecl_values = null;
        node.isCircular_visited = -1;
        node.isCircular_computed = false;
        node.isCircular_initialized = false;
        node.implementedInterfaces_computed = false;
        node.implementedInterfaces_value = null;
        node.subtype_TypeDecl_values = null;
        node.sootClass_computed = false;
        node.sootClass_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            InterfaceDecl node = clone();
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
    @Override // soot.JastAddJ.ASTNode
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
    public void accessControl() {
        super.accessControl();
        if (!isCircular()) {
            HashSet set = new HashSet();
            for (int i = 0; i < getNumSuperInterfaceId(); i++) {
                TypeDecl decl = getSuperInterfaceId(i).type();
                if (!decl.isInterfaceDecl() && !decl.isUnknown()) {
                    error("interface " + fullName() + " tries to extend non interface type " + decl.fullName());
                }
                if (!decl.isCircular() && !decl.accessibleFrom(this)) {
                    error("interface " + fullName() + " can not extend non accessible type " + decl.fullName());
                }
                if (set.contains(decl)) {
                    error("extended interface " + decl.fullName() + " mentionened multiple times in extends clause");
                }
                set.add(decl);
            }
        }
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void checkModifiers() {
        super.checkModifiers();
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        getModifiers().toString(s);
        s.append("interface " + name());
        if (getNumSuperInterfaceId() > 0) {
            s.append(" extends ");
            getSuperInterfaceId(0).toString(s);
            for (int i = 1; i < getNumSuperInterfaceId(); i++) {
                s.append(", ");
                getSuperInterfaceId(i).toString(s);
            }
        }
        ppBodyDecls(s);
    }

    public Iterator superinterfacesIterator() {
        return new Iterator() { // from class: soot.JastAddJ.InterfaceDecl.1
            private int index = 0;
            private TypeDecl current = null;

            @Override // java.util.Iterator
            public boolean hasNext() {
                computeNextCurrent();
                return this.current != null;
            }

            @Override // java.util.Iterator
            public Object next() {
                return this.current;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private void computeNextCurrent() {
                this.current = null;
                if (InterfaceDecl.this.isCircular()) {
                    return;
                }
                while (this.index < InterfaceDecl.this.getNumSuperInterfaceId()) {
                    InterfaceDecl interfaceDecl = InterfaceDecl.this;
                    int i = this.index;
                    this.index = i + 1;
                    TypeDecl typeDecl = interfaceDecl.getSuperInterfaceId(i).type();
                    if (!typeDecl.isCircular() && typeDecl.isInterfaceDecl()) {
                        this.current = typeDecl;
                        return;
                    }
                }
            }
        };
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void nameCheck() {
        super.nameCheck();
        if (isCircular()) {
            error("circular inheritance dependency in " + typeName());
        } else {
            for (int i = 0; i < getNumSuperInterfaceId(); i++) {
                TypeDecl typeDecl = getSuperInterfaceId(i).type();
                if (typeDecl.isCircular()) {
                    error("circular inheritance dependency in " + typeName());
                }
            }
        }
        for (SimpleSet set : methodsSignatureMap().values()) {
            if (set.size() > 1) {
                Iterator i2 = set.iterator();
                MethodDecl m = (MethodDecl) i2.next();
                while (i2.hasNext()) {
                    MethodDecl n = (MethodDecl) i2.next();
                    if (!n.mayOverrideReturn(m) && !m.mayOverrideReturn(n)) {
                        error("multiply inherited methods with the same signature must have the same return type");
                    }
                }
            }
        }
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl makeGeneric(Signatures.ClassSignature s) {
        if (s.hasFormalTypeParameters()) {
            ASTNode node = getParent();
            int index = node.getIndexOfChild(this);
            node.setChild(new GenericInterfaceDecl(getModifiersNoTransform(), getID(), s.hasSuperinterfaceSignature() ? s.superinterfaceSignature() : getSuperInterfaceIdListNoTransform(), getBodyDeclListNoTransform(), s.typeParameters()), index);
            return (TypeDecl) node.getChildNoTransform(index);
        }
        if (s.hasSuperinterfaceSignature()) {
            setSuperInterfaceIdList(s.superinterfaceSignature());
        }
        return this;
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [soot.JastAddJ.Modifiers] */
    public InterfaceDecl substitutedInterfaceDecl(Parameterization parTypeDecl) {
        InterfaceDecl c = new InterfaceDeclSubstituted((Modifiers) getModifiers().fullCopy2(), getID(), getSuperInterfaceIdList().substitute(parTypeDecl), this);
        return c;
    }

    @Override // soot.JastAddJ.TypeDecl
    public FieldDeclaration createStaticClassField(String name) {
        return methodHolder().createStaticClassField(name);
    }

    @Override // soot.JastAddJ.TypeDecl
    public MethodDecl createStaticClassMethod() {
        return methodHolder().createStaticClassMethod();
    }

    public TypeDecl methodHolder() {
        if (this.methodHolder != null) {
            return this.methodHolder;
        }
        String name = "$" + nextAnonymousIndex();
        ClassDecl c = addMemberClass(new ClassDecl(new Modifiers(new List()), name, new Opt(), new List(), new List()));
        this.methodHolder = c;
        return c;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void jimplify1phase2() {
        SootClass sc = getSootClassDecl();
        sc.setResolvingLevel(0);
        sc.setModifiers(sootTypeModifiers());
        sc.setApplicationClass();
        SourceFileTag st = new SourceFileTag(sourceNameWithoutPath());
        st.setAbsolutePath(compilationUnit().pathName());
        sc.addTag(st);
        sc.setSuperclass(typeObject().getSootClassDecl());
        Iterator iter = superinterfacesIterator();
        while (iter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl) iter.next();
            if (typeDecl != typeObject() && !sc.implementsInterface(typeDecl.getSootClassDecl().getName())) {
                sc.addInterface(typeDecl.getSootClassDecl());
            }
        }
        if (isNestedType()) {
            sc.setOuterClass(enclosingType().getSootClassDecl());
        }
        sc.setResolvingLevel(1);
        super.jimplify1phase2();
        sc.setResolvingLevel(2);
    }

    public InterfaceDecl() {
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new List(), 1);
        setChild(new List(), 2);
    }

    public InterfaceDecl(Modifiers p0, String p1, List<Access> p2, List<BodyDecl> p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    public InterfaceDecl(Modifiers p0, Symbol p1, List<Access> p2, List<BodyDecl> p3) {
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
        return false;
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

    public void setSuperInterfaceIdList(List<Access> list) {
        setChild(list, 1);
    }

    public int getNumSuperInterfaceId() {
        return getSuperInterfaceIdList().getNumChild();
    }

    public int getNumSuperInterfaceIdNoTransform() {
        return getSuperInterfaceIdListNoTransform().getNumChildNoTransform();
    }

    public Access getSuperInterfaceId(int i) {
        return getSuperInterfaceIdList().getChild(i);
    }

    public void addSuperInterfaceId(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getSuperInterfaceIdListNoTransform() : getSuperInterfaceIdList();
        list.addChild(node);
    }

    public void addSuperInterfaceIdNoTransform(Access node) {
        List<Access> list = getSuperInterfaceIdListNoTransform();
        list.addChild(node);
    }

    public void setSuperInterfaceId(Access node, int i) {
        List<Access> list = getSuperInterfaceIdList();
        list.setChild(node, i);
    }

    public List<Access> getSuperInterfaceIds() {
        return getSuperInterfaceIdList();
    }

    public List<Access> getSuperInterfaceIdsNoTransform() {
        return getSuperInterfaceIdListNoTransform();
    }

    public List<Access> getSuperInterfaceIdList() {
        List<Access> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    public List<Access> getSuperInterfaceIdListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 2);
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
        List<BodyDecl> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    private boolean refined_Generics_InterfaceDecl_castingConversionTo_TypeDecl(TypeDecl type) {
        if (type.isArrayDecl()) {
            return type.instanceOf(this);
        }
        if (type.isReferenceType() && !type.isFinal()) {
            return true;
        }
        return type.instanceOf(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public Collection lookupSuperConstructor() {
        state();
        return typeObject().constructors();
    }

    @Override // soot.JastAddJ.TypeDecl
    public HashMap methodsSignatureMap() {
        if (this.methodsSignatureMap_computed) {
            return this.methodsSignatureMap_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.methodsSignatureMap_value = methodsSignatureMap_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.methodsSignatureMap_computed = true;
        }
        return this.methodsSignatureMap_value;
    }

    private HashMap methodsSignatureMap_compute() {
        HashMap map = new HashMap(localMethodsSignatureMap());
        Iterator outerIter = superinterfacesIterator();
        while (outerIter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl) outerIter.next();
            Iterator iter = typeDecl.methodsIterator();
            while (iter.hasNext()) {
                MethodDecl m = (MethodDecl) iter.next();
                if (!m.isPrivate() && m.accessibleFrom(this) && !localMethodsSignatureMap().containsKey(m.signature()) && (!(m instanceof MethodDeclSubstituted) || !localMethodsSignatureMap().containsKey(m.sourceMethodDecl().signature()))) {
                    putSimpleSetElement(map, m.signature(), m);
                }
            }
        }
        Iterator iter2 = typeObject().methodsIterator();
        while (iter2.hasNext()) {
            MethodDecl m2 = (MethodDecl) iter2.next();
            if (m2.isPublic() && !map.containsKey(m2.signature())) {
                putSimpleSetElement(map, m2.signature(), m2);
            }
        }
        return map;
    }

    @Override // soot.JastAddJ.TypeDecl
    public SimpleSet ancestorMethods(String signature) {
        if (this.ancestorMethods_String_values == null) {
            this.ancestorMethods_String_values = new HashMap(4);
        }
        if (this.ancestorMethods_String_values.containsKey(signature)) {
            return (SimpleSet) this.ancestorMethods_String_values.get(signature);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet ancestorMethods_String_value = ancestorMethods_compute(signature);
        if (isFinal && num == state().boundariesCrossed) {
            this.ancestorMethods_String_values.put(signature, ancestorMethods_String_value);
        }
        return ancestorMethods_String_value;
    }

    private SimpleSet ancestorMethods_compute(String signature) {
        SimpleSet set = SimpleSet.emptySet;
        Iterator outerIter = superinterfacesIterator();
        while (outerIter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl) outerIter.next();
            for (MethodDecl m : typeDecl.methodsSignature(signature)) {
                set = set.add(m);
            }
        }
        if (!superinterfacesIterator().hasNext()) {
            for (MethodDecl m2 : typeObject().methodsSignature(signature)) {
                if (m2.isPublic()) {
                    set = set.add(m2);
                }
            }
        }
        return set;
    }

    @Override // soot.JastAddJ.TypeDecl
    public SimpleSet memberTypes(String name) {
        if (this.memberTypes_String_values == null) {
            this.memberTypes_String_values = new HashMap(4);
        }
        if (this.memberTypes_String_values.containsKey(name)) {
            return (SimpleSet) this.memberTypes_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet memberTypes_String_value = memberTypes_compute(name);
        if (isFinal && num == state().boundariesCrossed) {
            this.memberTypes_String_values.put(name, memberTypes_String_value);
        }
        return memberTypes_String_value;
    }

    private SimpleSet memberTypes_compute(String name) {
        SimpleSet set = localTypeDecls(name);
        if (set.isEmpty()) {
            Iterator outerIter = superinterfacesIterator();
            while (outerIter.hasNext()) {
                TypeDecl typeDecl = (TypeDecl) outerIter.next();
                for (TypeDecl decl : typeDecl.memberTypes(name)) {
                    if (!decl.isPrivate()) {
                        set = set.add(decl);
                    }
                }
            }
            return set;
        }
        return set;
    }

    @Override // soot.JastAddJ.TypeDecl
    public HashMap memberFieldsMap() {
        if (this.memberFieldsMap_computed) {
            return this.memberFieldsMap_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.memberFieldsMap_value = memberFieldsMap_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.memberFieldsMap_computed = true;
        }
        return this.memberFieldsMap_value;
    }

    private HashMap memberFieldsMap_compute() {
        HashMap map = new HashMap(localFieldsMap());
        Iterator outerIter = superinterfacesIterator();
        while (outerIter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl) outerIter.next();
            Iterator iter = typeDecl.fieldsIterator();
            while (iter.hasNext()) {
                FieldDeclaration f = (FieldDeclaration) iter.next();
                if (f.accessibleFrom(this) && !f.isPrivate() && !localFieldsMap().containsKey(f.name())) {
                    putSimpleSetElement(map, f.name(), f);
                }
            }
        }
        return map;
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
        SimpleSet fields = localFields(name);
        if (!fields.isEmpty()) {
            return fields;
        }
        Iterator outerIter = superinterfacesIterator();
        while (outerIter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl) outerIter.next();
            for (FieldDeclaration f : typeDecl.memberFields(name)) {
                if (f.accessibleFrom(this) && !f.isPrivate()) {
                    fields = fields.add(f);
                }
            }
        }
        return fields;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isAbstract() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isStatic() {
        if (this.isStatic_computed) {
            return this.isStatic_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isStatic_value = isStatic_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isStatic_computed = true;
        }
        return this.isStatic_value;
    }

    private boolean isStatic_compute() {
        return getModifiers().isStatic() || isMemberType();
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
        if (refined_Generics_InterfaceDecl_castingConversionTo_TypeDecl(type)) {
            return true;
        }
        boolean canUnboxThis = !unboxed().isUnknown();
        boolean canUnboxType = !type.unboxed().isUnknown();
        if (canUnboxThis && !canUnboxType) {
            return unboxed().wideningConversionTo(type);
        }
        return false;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isInterfaceDecl() {
        state();
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
    public boolean isSupertypeOfClassDecl(ClassDecl type) {
        state();
        if (super.isSupertypeOfClassDecl(type)) {
            return true;
        }
        Iterator iter = type.interfacesIterator();
        while (iter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl) iter.next();
            if (typeDecl.instanceOf(this)) {
                return true;
            }
        }
        return type.hasSuperclass() && type.superclass() != null && type.superclass().instanceOf(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isSupertypeOfInterfaceDecl(InterfaceDecl type) {
        state();
        if (super.isSupertypeOfInterfaceDecl(type)) {
            return true;
        }
        Iterator iter = type.superinterfacesIterator();
        while (iter.hasNext()) {
            TypeDecl superinterface = (TypeDecl) iter.next();
            if (superinterface.instanceOf(this)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isSupertypeOfArrayDecl(ArrayDecl type) {
        state();
        if (super.isSupertypeOfArrayDecl(type)) {
            return true;
        }
        Iterator iter = type.interfacesIterator();
        while (iter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl) iter.next();
            if (typeDecl.instanceOf(this)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isCircular() {
        if (this.isCircular_computed) {
            return this.isCircular_value;
        }
        ASTNode.State state = state();
        if (!this.isCircular_initialized) {
            this.isCircular_initialized = true;
            this.isCircular_value = true;
        }
        if (!state.IN_CIRCLE) {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = is$Final();
            do {
                this.isCircular_visited = state.CIRCLE_INDEX;
                state.CHANGE = false;
                boolean new_isCircular_value = isCircular_compute();
                if (new_isCircular_value != this.isCircular_value) {
                    state.CHANGE = true;
                }
                this.isCircular_value = new_isCircular_value;
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            if (isFinal && num == state().boundariesCrossed) {
                this.isCircular_computed = true;
            } else {
                state.RESET_CYCLE = true;
                isCircular_compute();
                state.RESET_CYCLE = false;
                this.isCircular_computed = false;
                this.isCircular_initialized = false;
            }
            state.IN_CIRCLE = false;
            return this.isCircular_value;
        } else if (this.isCircular_visited != state.CIRCLE_INDEX) {
            this.isCircular_visited = state.CIRCLE_INDEX;
            if (state.RESET_CYCLE) {
                this.isCircular_computed = false;
                this.isCircular_initialized = false;
                this.isCircular_visited = -1;
                return this.isCircular_value;
            }
            boolean new_isCircular_value2 = isCircular_compute();
            if (new_isCircular_value2 != this.isCircular_value) {
                state.CHANGE = true;
            }
            this.isCircular_value = new_isCircular_value2;
            return this.isCircular_value;
        } else {
            return this.isCircular_value;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x003e, code lost:
        r4 = r4 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean isCircular_compute() {
        /*
            r3 = this;
            r0 = 0
            r4 = r0
            goto L41
        L5:
            r0 = r3
            r1 = r4
            soot.JastAddJ.Access r0 = r0.getSuperInterfaceId(r1)
            soot.JastAddJ.Access r0 = r0.lastAccess()
            r5 = r0
            goto L3a
        L11:
            r0 = r5
            soot.JastAddJ.TypeDecl r0 = r0.type()
            boolean r0 = r0.isCircular()
            if (r0 == 0) goto L1d
            r0 = 1
            return r0
        L1d:
            r0 = r5
            boolean r0 = r0.isQualified()
            if (r0 == 0) goto L38
            r0 = r5
            soot.JastAddJ.Expr r0 = r0.qualifier()
            boolean r0 = r0.isTypeAccess()
            if (r0 == 0) goto L38
            r0 = r5
            soot.JastAddJ.Expr r0 = r0.qualifier()
            soot.JastAddJ.Access r0 = (soot.JastAddJ.Access) r0
            goto L39
        L38:
            r0 = 0
        L39:
            r5 = r0
        L3a:
            r0 = r5
            if (r0 != 0) goto L11
            int r4 = r4 + 1
        L41:
            r0 = r4
            r1 = r3
            int r1 = r1.getNumSuperInterfaceId()
            if (r0 < r1) goto L5
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.JastAddJ.InterfaceDecl.isCircular_compute():boolean");
    }

    @Override // soot.JastAddJ.TypeDecl
    public HashSet implementedInterfaces() {
        if (this.implementedInterfaces_computed) {
            return this.implementedInterfaces_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.implementedInterfaces_value = implementedInterfaces_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.implementedInterfaces_computed = true;
        }
        return this.implementedInterfaces_value;
    }

    private HashSet implementedInterfaces_compute() {
        HashSet set = new HashSet();
        set.addAll(typeObject().implementedInterfaces());
        Iterator iter = superinterfacesIterator();
        while (iter.hasNext()) {
            InterfaceDecl decl = (InterfaceDecl) iter.next();
            set.add(decl);
            set.addAll(decl.implementedInterfaces());
        }
        return set;
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
        return type.supertypeInterfaceDecl(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeClassDecl(ClassDecl type) {
        state();
        if (super.supertypeClassDecl(type)) {
            return true;
        }
        Iterator iter = type.interfacesIterator();
        while (iter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl) iter.next();
            if (typeDecl.subtype(this)) {
                return true;
            }
        }
        return type.hasSuperclass() && type.superclass() != null && type.superclass().subtype(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeInterfaceDecl(InterfaceDecl type) {
        state();
        if (super.supertypeInterfaceDecl(type)) {
            return true;
        }
        Iterator iter = type.superinterfacesIterator();
        while (iter.hasNext()) {
            TypeDecl superinterface = (TypeDecl) iter.next();
            if (superinterface.subtype(this)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeArrayDecl(ArrayDecl type) {
        state();
        if (super.supertypeArrayDecl(type)) {
            return true;
        }
        Iterator iter = type.interfacesIterator();
        while (iter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl) iter.next();
            if (typeDecl.subtype(this)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.JastAddJ.TypeDecl
    public SootClass sootClass() {
        if (this.sootClass_computed) {
            return this.sootClass_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.sootClass_value = sootClass_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.sootClass_computed = true;
        }
        return this.sootClass_value;
    }

    private SootClass sootClass_compute() {
        if (options().verbose()) {
            System.out.println("Creating from source " + jvmName());
        }
        SootClass sc = SootResolver.v().makeClassRef(jvmName());
        sc.setModifiers(sootTypeModifiers());
        return sc;
    }

    @Override // soot.JastAddJ.TypeDecl
    public int sootTypeModifiers() {
        state();
        return super.sootTypeModifiers() | 512;
    }

    @Override // soot.JastAddJ.TypeDecl
    public String typeDescriptor() {
        state();
        return "L" + jvmName().replace('.', '/') + ";";
    }

    @Override // soot.JastAddJ.TypeDecl
    public SimpleSet bridgeCandidates(String signature) {
        state();
        return ancestorMethods(signature);
    }

    public MethodDecl unknownMethod() {
        state();
        MethodDecl unknownMethod_value = getParent().Define_MethodDecl_unknownMethod(this, null);
        return unknownMethod_value;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getSuperInterfaceIdListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.TYPE_NAME;
        }
        return super.Define_NameType_nameType(caller, child);
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
        if (caller == getSuperInterfaceIdListNoTransform()) {
            caller.getIndexOfChild(child);
            return hostType();
        }
        return super.Define_TypeDecl_hostType(caller, child);
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean Define_boolean_withinSuppressWarnings(ASTNode caller, ASTNode child, String s) {
        if (caller == getSuperInterfaceIdListNoTransform()) {
            caller.getIndexOfChild(child);
            return hasAnnotationSuppressWarnings(s) || withinSuppressWarnings(s);
        }
        return super.Define_boolean_withinSuppressWarnings(caller, child, s);
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean Define_boolean_withinDeprecatedAnnotation(ASTNode caller, ASTNode child) {
        if (caller == getSuperInterfaceIdListNoTransform()) {
            caller.getIndexOfChild(child);
            return isDeprecated() || withinDeprecatedAnnotation();
        }
        return super.Define_boolean_withinDeprecatedAnnotation(caller, child);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inExtendsOrImplements(ASTNode caller, ASTNode child) {
        if (caller == getSuperInterfaceIdListNoTransform()) {
            caller.getIndexOfChild(child);
            return true;
        }
        return getParent().Define_boolean_inExtendsOrImplements(this, caller);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
