package soot.JastAddJ;

import beaver.Symbol;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.JastAddJ.Signatures;
import soot.JastAddJ.SimpleSet;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.SootResolver;
import soot.Type;
import soot.Value;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.infoflow.android.source.parsers.xml.XMLConstants;
import soot.jimple.infoflow.results.xml.XmlConstants;
import soot.tagkit.InnerClassAttribute;
import soot.tagkit.InnerClassTag;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/TypeDecl.class */
public abstract class TypeDecl extends ASTNode<ASTNode> implements Cloneable, SimpleSet, Iterator, VariableScope {
    private TypeDecl iterElem;
    private Collection nestedTypes;
    private Collection usedNestedTypes;
    protected String tokenString_ID;
    public int IDstart;
    public int IDend;
    protected Map accessibleFromPackage_String_values;
    protected Map accessibleFromExtend_TypeDecl_values;
    protected Map accessibleFrom_TypeDecl_values;
    protected int dimension_value;
    protected TypeDecl elementType_value;
    protected TypeDecl arrayType_value;
    protected boolean isException_value;
    protected boolean isCheckedException_value;
    protected boolean isUncheckedException_value;
    protected Map mayCatch_TypeDecl_values;
    protected Collection constructors_value;
    protected Map unqualifiedLookupMethod_String_values;
    protected HashMap methodsNameMap_value;
    protected HashMap localMethodsSignatureMap_value;
    protected HashMap methodsSignatureMap_value;
    protected Map ancestorMethods_String_values;
    protected Map localTypeDecls_String_values;
    protected Map memberTypes_String_values;
    protected Map localFields_String_values;
    protected HashMap localFieldsMap_value;
    protected HashMap memberFieldsMap_value;
    protected Map memberFields_String_values;
    protected boolean hasAbstract_value;
    protected Collection unimplementedMethods_value;
    protected boolean isPublic_value;
    protected boolean isStatic_value;
    protected String fullName_value;
    protected String typeName_value;
    protected Map narrowingConversionTo_TypeDecl_values;
    protected Map methodInvocationConversionTo_TypeDecl_values;
    protected Map castingConversionTo_TypeDecl_values;
    protected boolean isString_value;
    protected boolean isObject_value;
    protected Map instanceOf_TypeDecl_values;
    protected boolean isCircular_value;
    protected TypeDecl boxed_value;
    protected TypeDecl unboxed_value;
    protected boolean isIterable_value;
    protected boolean involvesTypeParameters_value;
    protected TypeDecl erasure_value;
    protected HashSet implementedInterfaces_value;
    protected boolean usesTypeVariable_value;
    protected TypeDecl sourceTypeDecl_value;
    protected Map containedIn_TypeDecl_values;
    protected Map sameStructure_TypeDecl_values;
    protected Map subtype_TypeDecl_values;
    protected Collection enclosingVariables_value;
    protected int uniqueIndex_value;
    protected String jvmName_value;
    protected SootClass getSootClassDecl_value;
    protected Type getSootType_value;
    protected SootClass sootClass_value;
    protected boolean needsClinit_value;
    protected Collection innerClassesAttributeEntries_value;
    protected Map getSootField_String_TypeDecl_values;
    protected Map createEnumMethod_TypeDecl_values;
    protected Map createEnumIndex_EnumConstant_values;
    protected Map createEnumArray_TypeDecl_values;
    protected TypeDecl componentType_value;
    protected Map isDAbefore_Variable_values;
    protected Map isDUbefore_Variable_values;
    protected TypeDecl typeException_value;
    protected TypeDecl typeRuntimeException_value;
    protected TypeDecl typeError_value;
    protected Map lookupMethod_String_values;
    protected TypeDecl typeObject_value;
    protected Map lookupType_String_values;
    protected Map lookupVariable_String_values;
    protected String packageName_value;
    protected boolean isAnonymous_value;
    protected TypeDecl unknownType_value;
    protected boolean inExplicitConstructorInvocation_value;
    protected boolean inStaticContext_value;
    public int anonymousIndex = 0;
    public int accessorCounter = 0;
    private HashMap accessorMap = null;
    private boolean addEnclosingVariables = true;
    int uniqueIndexCounter = 1;
    private FieldDeclaration createAssertionsDisabled = null;
    private HashMap createStaticClassField = null;
    private MethodDecl createStaticClassMethod = null;
    public SootMethod clinit = null;
    private HashMap createEnumIndexMap = null;
    protected boolean dimension_computed = false;
    protected boolean elementType_computed = false;
    protected boolean arrayType_computed = false;
    protected boolean isException_computed = false;
    protected boolean isCheckedException_computed = false;
    protected boolean isUncheckedException_computed = false;
    protected boolean constructors_computed = false;
    protected boolean methodsNameMap_computed = false;
    protected boolean localMethodsSignatureMap_computed = false;
    protected boolean methodsSignatureMap_computed = false;
    protected boolean localFieldsMap_computed = false;
    protected boolean memberFieldsMap_computed = false;
    protected boolean hasAbstract_computed = false;
    protected boolean unimplementedMethods_computed = false;
    protected boolean isPublic_computed = false;
    protected boolean isStatic_computed = false;
    protected boolean fullName_computed = false;
    protected boolean typeName_computed = false;
    protected boolean isString_computed = false;
    protected boolean isObject_computed = false;
    protected int isCircular_visited = -1;
    protected boolean isCircular_computed = false;
    protected boolean isCircular_initialized = false;
    protected boolean boxed_computed = false;
    protected boolean unboxed_computed = false;
    protected boolean isIterable_computed = false;
    protected int involvesTypeParameters_visited = -1;
    protected boolean involvesTypeParameters_computed = false;
    protected boolean involvesTypeParameters_initialized = false;
    protected boolean erasure_computed = false;
    protected boolean implementedInterfaces_computed = false;
    protected int usesTypeVariable_visited = -1;
    protected boolean usesTypeVariable_computed = false;
    protected boolean usesTypeVariable_initialized = false;
    protected boolean sourceTypeDecl_computed = false;
    protected boolean enclosingVariables_computed = false;
    protected boolean uniqueIndex_computed = false;
    protected boolean jvmName_computed = false;
    protected boolean getSootClassDecl_computed = false;
    protected boolean getSootType_computed = false;
    protected boolean sootClass_computed = false;
    protected boolean needsClinit_computed = false;
    protected boolean innerClassesAttributeEntries_computed = false;
    protected boolean componentType_computed = false;
    protected boolean typeException_computed = false;
    protected boolean typeRuntimeException_computed = false;
    protected boolean typeError_computed = false;
    protected boolean typeObject_computed = false;
    protected boolean packageName_computed = false;
    protected boolean isAnonymous_computed = false;
    protected boolean unknownType_computed = false;
    protected boolean inExplicitConstructorInvocation_computed = false;
    protected boolean inStaticContext_computed = false;

    @Override // soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.accessibleFromPackage_String_values = null;
        this.accessibleFromExtend_TypeDecl_values = null;
        this.accessibleFrom_TypeDecl_values = null;
        this.dimension_computed = false;
        this.elementType_computed = false;
        this.elementType_value = null;
        this.arrayType_computed = false;
        this.arrayType_value = null;
        this.isException_computed = false;
        this.isCheckedException_computed = false;
        this.isUncheckedException_computed = false;
        this.mayCatch_TypeDecl_values = null;
        this.constructors_computed = false;
        this.constructors_value = null;
        this.unqualifiedLookupMethod_String_values = null;
        this.methodsNameMap_computed = false;
        this.methodsNameMap_value = null;
        this.localMethodsSignatureMap_computed = false;
        this.localMethodsSignatureMap_value = null;
        this.methodsSignatureMap_computed = false;
        this.methodsSignatureMap_value = null;
        this.ancestorMethods_String_values = null;
        this.localTypeDecls_String_values = null;
        this.memberTypes_String_values = null;
        this.localFields_String_values = null;
        this.localFieldsMap_computed = false;
        this.localFieldsMap_value = null;
        this.memberFieldsMap_computed = false;
        this.memberFieldsMap_value = null;
        this.memberFields_String_values = null;
        this.hasAbstract_computed = false;
        this.unimplementedMethods_computed = false;
        this.unimplementedMethods_value = null;
        this.isPublic_computed = false;
        this.isStatic_computed = false;
        this.fullName_computed = false;
        this.fullName_value = null;
        this.typeName_computed = false;
        this.typeName_value = null;
        this.narrowingConversionTo_TypeDecl_values = null;
        this.methodInvocationConversionTo_TypeDecl_values = null;
        this.castingConversionTo_TypeDecl_values = null;
        this.isString_computed = false;
        this.isObject_computed = false;
        this.instanceOf_TypeDecl_values = null;
        this.isCircular_visited = -1;
        this.isCircular_computed = false;
        this.isCircular_initialized = false;
        this.boxed_computed = false;
        this.boxed_value = null;
        this.unboxed_computed = false;
        this.unboxed_value = null;
        this.isIterable_computed = false;
        this.involvesTypeParameters_visited = -1;
        this.involvesTypeParameters_computed = false;
        this.involvesTypeParameters_initialized = false;
        this.erasure_computed = false;
        this.erasure_value = null;
        this.implementedInterfaces_computed = false;
        this.implementedInterfaces_value = null;
        this.usesTypeVariable_visited = -1;
        this.usesTypeVariable_computed = false;
        this.usesTypeVariable_initialized = false;
        this.sourceTypeDecl_computed = false;
        this.sourceTypeDecl_value = null;
        this.containedIn_TypeDecl_values = null;
        this.sameStructure_TypeDecl_values = null;
        this.subtype_TypeDecl_values = null;
        this.enclosingVariables_computed = false;
        this.enclosingVariables_value = null;
        this.uniqueIndex_computed = false;
        this.jvmName_computed = false;
        this.jvmName_value = null;
        this.getSootClassDecl_computed = false;
        this.getSootClassDecl_value = null;
        this.getSootType_computed = false;
        this.getSootType_value = null;
        this.sootClass_computed = false;
        this.sootClass_value = null;
        this.needsClinit_computed = false;
        this.innerClassesAttributeEntries_computed = false;
        this.innerClassesAttributeEntries_value = null;
        this.getSootField_String_TypeDecl_values = null;
        this.createEnumMethod_TypeDecl_values = null;
        this.createEnumIndex_EnumConstant_values = null;
        this.createEnumArray_TypeDecl_values = null;
        this.componentType_computed = false;
        this.componentType_value = null;
        this.isDAbefore_Variable_values = null;
        this.isDUbefore_Variable_values = null;
        this.typeException_computed = false;
        this.typeException_value = null;
        this.typeRuntimeException_computed = false;
        this.typeRuntimeException_value = null;
        this.typeError_computed = false;
        this.typeError_value = null;
        this.lookupMethod_String_values = null;
        this.typeObject_computed = false;
        this.typeObject_value = null;
        this.lookupType_String_values = null;
        this.lookupVariable_String_values = null;
        this.packageName_computed = false;
        this.packageName_value = null;
        this.isAnonymous_computed = false;
        this.unknownType_computed = false;
        this.unknownType_value = null;
        this.inExplicitConstructorInvocation_computed = false;
        this.inStaticContext_computed = false;
    }

    @Override // soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ASTNode, beaver.Symbol
    public TypeDecl clone() throws CloneNotSupportedException {
        TypeDecl node = (TypeDecl) super.mo287clone();
        node.accessibleFromPackage_String_values = null;
        node.accessibleFromExtend_TypeDecl_values = null;
        node.accessibleFrom_TypeDecl_values = null;
        node.dimension_computed = false;
        node.elementType_computed = false;
        node.elementType_value = null;
        node.arrayType_computed = false;
        node.arrayType_value = null;
        node.isException_computed = false;
        node.isCheckedException_computed = false;
        node.isUncheckedException_computed = false;
        node.mayCatch_TypeDecl_values = null;
        node.constructors_computed = false;
        node.constructors_value = null;
        node.unqualifiedLookupMethod_String_values = null;
        node.methodsNameMap_computed = false;
        node.methodsNameMap_value = null;
        node.localMethodsSignatureMap_computed = false;
        node.localMethodsSignatureMap_value = null;
        node.methodsSignatureMap_computed = false;
        node.methodsSignatureMap_value = null;
        node.ancestorMethods_String_values = null;
        node.localTypeDecls_String_values = null;
        node.memberTypes_String_values = null;
        node.localFields_String_values = null;
        node.localFieldsMap_computed = false;
        node.localFieldsMap_value = null;
        node.memberFieldsMap_computed = false;
        node.memberFieldsMap_value = null;
        node.memberFields_String_values = null;
        node.hasAbstract_computed = false;
        node.unimplementedMethods_computed = false;
        node.unimplementedMethods_value = null;
        node.isPublic_computed = false;
        node.isStatic_computed = false;
        node.fullName_computed = false;
        node.fullName_value = null;
        node.typeName_computed = false;
        node.typeName_value = null;
        node.narrowingConversionTo_TypeDecl_values = null;
        node.methodInvocationConversionTo_TypeDecl_values = null;
        node.castingConversionTo_TypeDecl_values = null;
        node.isString_computed = false;
        node.isObject_computed = false;
        node.instanceOf_TypeDecl_values = null;
        node.isCircular_visited = -1;
        node.isCircular_computed = false;
        node.isCircular_initialized = false;
        node.boxed_computed = false;
        node.boxed_value = null;
        node.unboxed_computed = false;
        node.unboxed_value = null;
        node.isIterable_computed = false;
        node.involvesTypeParameters_visited = -1;
        node.involvesTypeParameters_computed = false;
        node.involvesTypeParameters_initialized = false;
        node.erasure_computed = false;
        node.erasure_value = null;
        node.implementedInterfaces_computed = false;
        node.implementedInterfaces_value = null;
        node.usesTypeVariable_visited = -1;
        node.usesTypeVariable_computed = false;
        node.usesTypeVariable_initialized = false;
        node.sourceTypeDecl_computed = false;
        node.sourceTypeDecl_value = null;
        node.containedIn_TypeDecl_values = null;
        node.sameStructure_TypeDecl_values = null;
        node.subtype_TypeDecl_values = null;
        node.enclosingVariables_computed = false;
        node.enclosingVariables_value = null;
        node.uniqueIndex_computed = false;
        node.jvmName_computed = false;
        node.jvmName_value = null;
        node.getSootClassDecl_computed = false;
        node.getSootClassDecl_value = null;
        node.getSootType_computed = false;
        node.getSootType_value = null;
        node.sootClass_computed = false;
        node.sootClass_value = null;
        node.needsClinit_computed = false;
        node.innerClassesAttributeEntries_computed = false;
        node.innerClassesAttributeEntries_value = null;
        node.getSootField_String_TypeDecl_values = null;
        node.createEnumMethod_TypeDecl_values = null;
        node.createEnumIndex_EnumConstant_values = null;
        node.createEnumArray_TypeDecl_values = null;
        node.componentType_computed = false;
        node.componentType_value = null;
        node.isDAbefore_Variable_values = null;
        node.isDUbefore_Variable_values = null;
        node.typeException_computed = false;
        node.typeException_value = null;
        node.typeRuntimeException_computed = false;
        node.typeRuntimeException_value = null;
        node.typeError_computed = false;
        node.typeError_value = null;
        node.lookupMethod_String_values = null;
        node.typeObject_computed = false;
        node.typeObject_value = null;
        node.lookupType_String_values = null;
        node.lookupVariable_String_values = null;
        node.packageName_computed = false;
        node.packageName_value = null;
        node.isAnonymous_computed = false;
        node.unknownType_computed = false;
        node.unknownType_value = null;
        node.inExplicitConstructorInvocation_computed = false;
        node.inStaticContext_computed = false;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    public int nextAnonymousIndex() {
        if (isNestedType()) {
            return enclosingType().nextAnonymousIndex();
        }
        int i = this.anonymousIndex;
        this.anonymousIndex = i + 1;
        return i;
    }

    public MethodDecl addMemberMethod(MethodDecl m) {
        addBodyDecl(m);
        return (MethodDecl) getBodyDecl(getNumBodyDecl() - 1);
    }

    public ConstructorDecl addConstructor(ConstructorDecl c) {
        addBodyDecl(c);
        return (ConstructorDecl) getBodyDecl(getNumBodyDecl() - 1);
    }

    public ClassDecl addMemberClass(ClassDecl c) {
        addBodyDecl(new MemberClassDecl(c));
        return ((MemberClassDecl) getBodyDecl(getNumBodyDecl() - 1)).getClassDecl();
    }

    public FieldDeclaration addMemberField(FieldDeclaration f) {
        addBodyDecl(f);
        return (FieldDeclaration) getBodyDecl(getNumBodyDecl() - 1);
    }

    public TypeAccess createBoundAccess() {
        return new BoundTypeAccess("", name(), this);
    }

    @Override // soot.JastAddJ.SimpleSet
    public SimpleSet add(Object o) {
        return new SimpleSet.SimpleSetImpl().add(this).add(o);
    }

    @Override // soot.JastAddJ.SimpleSet
    public boolean isSingleton() {
        return true;
    }

    @Override // soot.JastAddJ.SimpleSet
    public boolean isSingleton(Object o) {
        return contains(o);
    }

    @Override // soot.JastAddJ.ASTNode, java.lang.Iterable
    public Iterator iterator() {
        this.iterElem = this;
        return this;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.iterElem != null;
    }

    @Override // java.util.Iterator
    public Object next() {
        Object o = this.iterElem;
        this.iterElem = null;
        return o;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean declaredBeforeUse(Variable decl, ASTNode use) {
        int indexDecl = ((ASTNode) decl).varChildIndex(this);
        int indexUse = use.varChildIndex(this);
        return indexDecl < indexUse;
    }

    public boolean declaredBeforeUse(Variable decl, int indexUse) {
        int indexDecl = ((ASTNode) decl).varChildIndex(this);
        return indexDecl < indexUse;
    }

    public ConstructorDecl lookupConstructor(ConstructorDecl signature) {
        for (ConstructorDecl decl : constructors()) {
            if (decl.sameSignature(signature)) {
                return decl;
            }
        }
        return null;
    }

    public boolean mayAccess(MethodAccess access, MethodDecl method) {
        if (instanceOf(method.hostType()) && access.qualifier().type().instanceOf(this)) {
            return true;
        }
        if (isNestedType()) {
            return enclosingType().mayAccess(access, method);
        }
        return false;
    }

    public Iterator localMethodsIterator() {
        return new Iterator() { // from class: soot.JastAddJ.TypeDecl.1
            private Iterator outer;
            private Iterator inner = null;

            {
                this.outer = TypeDecl.this.localMethodsSignatureMap().values().iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if ((this.inner == null || !this.inner.hasNext()) && this.outer.hasNext()) {
                    this.inner = ((SimpleSet) this.outer.next()).iterator();
                }
                if (this.inner == null) {
                    return false;
                }
                return this.inner.hasNext();
            }

            @Override // java.util.Iterator
            public Object next() {
                return this.inner.next();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public Iterator methodsIterator() {
        return new Iterator() { // from class: soot.JastAddJ.TypeDecl.2
            private Iterator outer;
            private Iterator inner = null;

            {
                this.outer = TypeDecl.this.methodsSignatureMap().values().iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if ((this.inner == null || !this.inner.hasNext()) && this.outer.hasNext()) {
                    this.inner = ((SimpleSet) this.outer.next()).iterator();
                }
                if (this.inner != null) {
                    return this.inner.hasNext();
                }
                return false;
            }

            @Override // java.util.Iterator
            public Object next() {
                return this.inner.next();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean allMethodsAbstract(SimpleSet set) {
        if (set == null) {
            return true;
        }
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            MethodDecl m = (MethodDecl) iter.next();
            if (!m.isAbstract()) {
                return false;
            }
        }
        return true;
    }

    public boolean mayAccess(Expr expr, FieldDeclaration field) {
        if (instanceOf(field.hostType()) && (!field.isInstanceVariable() || expr.isSuperAccess() || expr.type().instanceOf(this))) {
            return true;
        }
        if (isNestedType()) {
            return enclosingType().mayAccess(expr, field);
        }
        return false;
    }

    public Iterator fieldsIterator() {
        return new Iterator() { // from class: soot.JastAddJ.TypeDecl.3
            private Iterator outer;
            private Iterator inner = null;

            {
                this.outer = TypeDecl.this.memberFieldsMap().values().iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if ((this.inner == null || !this.inner.hasNext()) && this.outer.hasNext()) {
                    this.inner = ((SimpleSet) this.outer.next()).iterator();
                }
                if (this.inner != null) {
                    return this.inner.hasNext();
                }
                return false;
            }

            @Override // java.util.Iterator
            public Object next() {
                return this.inner.next();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // soot.JastAddJ.ASTNode
    public void checkModifiers() {
        super.checkModifiers();
        if (isPublic() && !isTopLevelType() && !isMemberType()) {
            error("public pertains only to top level types and member types");
        }
        if ((isProtected() || isPrivate()) && (!isMemberType() || !enclosingType().isClassDecl())) {
            error("protected and private may only be used on member types within a directly enclosing class declaration");
        }
        if (isStatic() && !isMemberType()) {
            error("static pertains only to member types");
        }
        if (!isAbstract() && hasAbstract()) {
            StringBuffer s = new StringBuffer();
            s.append(name() + " is not declared abstract but contains abstract members: \n");
            for (MethodDecl m : unimplementedMethods()) {
                s.append("  " + m.signature() + " in " + m.hostType().typeName() + "\n");
            }
            error(s.toString());
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void nameCheck() {
        TypeDecl typeDecl;
        if (isTopLevelType() && lookupType(packageName(), name()) != this) {
            error("duplicate type " + name() + " in package " + packageName());
        }
        if (!isTopLevelType() && !isAnonymous() && !isLocalClass() && extractSingleType(enclosingType().memberTypes(name())) != this) {
            error("duplicate member type " + name() + " in type " + enclosingType().typeName());
        }
        if (isLocalClass() && (typeDecl = extractSingleType(lookupType(name()))) != null && typeDecl != this && typeDecl.isLocalClass() && enclosingBlock() == typeDecl.enclosingBlock()) {
            error("local class named " + name() + " may not be redeclared as a local class in the same block");
        }
        if (!packageName().equals("") && hasPackage(fullName())) {
            error("type name conflicts with a package using the same name: " + name());
        }
        if (hasEnclosingTypeDecl(name())) {
            error("type may not have the same simple name as an enclosing type declaration");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void ppBodyDecls(StringBuffer s) {
        s.append(" {");
        for (int i = 0; i < getNumBodyDecl(); i++) {
            getBodyDecl(i).toString(s);
        }
        s.append(String.valueOf(indent()) + "}");
    }

    public Access createQualifiedAccess() {
        if (isLocalClass() || isAnonymous()) {
            return new TypeAccess(name());
        }
        if (!isTopLevelType()) {
            return enclosingType().createQualifiedAccess().qualifiesAccess(new TypeAccess(name()));
        }
        return new TypeAccess(packageName(), name());
    }

    public FieldDeclaration findSingleVariable(String name) {
        return (FieldDeclaration) memberFields(name).iterator().next();
    }

    public void refined_TypeHierarchyCheck_TypeDecl_typeCheck() {
        Iterator iter1 = localMethodsIterator();
        while (iter1.hasNext()) {
            MethodDecl m = (MethodDecl) iter1.next();
            ASTNode target = m.hostType() == this ? m : this;
            for (MethodDecl decl : ancestorMethods(m.signature())) {
                if (m.overrides(decl)) {
                    if (!m.isStatic() && decl.isStatic()) {
                        target.error("an instance method may not override a static method");
                    }
                    if (!m.mayOverrideReturn(decl)) {
                        target.error("the return type of method " + m.signature() + " in " + m.hostType().typeName() + " does not match the return type of method " + decl.signature() + " in " + decl.hostType().typeName() + " and may thus not be overriden");
                    }
                    for (int i = 0; i < m.getNumException(); i++) {
                        Access e = m.getException(i);
                        boolean found = false;
                        for (int j = 0; !found && j < decl.getNumException(); j++) {
                            if (e.type().instanceOf(decl.getException(j).type())) {
                                found = true;
                            }
                        }
                        if (!found && e.type().isUncheckedException()) {
                            target.error(String.valueOf(m.signature()) + " in " + m.hostType().typeName() + " may not throw more checked exceptions than overridden method " + decl.signature() + " in " + decl.hostType().typeName());
                        }
                    }
                    if (decl.isPublic() && !m.isPublic()) {
                        target.error("overriding access modifier error");
                    }
                    if (decl.isProtected() && !m.isPublic() && !m.isProtected()) {
                        target.error("overriding access modifier error");
                    }
                    if (!decl.isPrivate() && !decl.isProtected() && !decl.isPublic() && m.isPrivate()) {
                        target.error("overriding access modifier error");
                    }
                    if (decl.isFinal()) {
                        target.error("method " + m.signature() + " in " + hostType().typeName() + " can not override final method " + decl.signature() + " in " + decl.hostType().typeName());
                    }
                }
                if (m.hides(decl)) {
                    if (m.isStatic() && !decl.isStatic()) {
                        target.error("a static method may not hide an instance method");
                    }
                    if (!m.mayOverrideReturn(decl)) {
                        target.error("can not hide a method with a different return type");
                    }
                    for (int i2 = 0; i2 < m.getNumException(); i2++) {
                        Access e2 = m.getException(i2);
                        boolean found2 = false;
                        for (int j2 = 0; !found2 && j2 < decl.getNumException(); j2++) {
                            if (e2.type().instanceOf(decl.getException(j2).type())) {
                                found2 = true;
                            }
                        }
                        if (!found2) {
                            target.error("may not throw more checked exceptions than hidden method");
                        }
                    }
                    if (decl.isPublic() && !m.isPublic()) {
                        target.error("hiding access modifier error: public method " + decl.signature() + " in " + decl.hostType().typeName() + " is hidden by non public method " + m.signature() + " in " + m.hostType().typeName());
                    }
                    if (decl.isProtected() && !m.isPublic() && !m.isProtected()) {
                        target.error("hiding access modifier error: protected method " + decl.signature() + " in " + decl.hostType().typeName() + " is hidden by non (public|protected) method " + m.signature() + " in " + m.hostType().typeName());
                    }
                    if (!decl.isPrivate() && !decl.isProtected() && !decl.isPublic() && m.isPrivate()) {
                        target.error("hiding access modifier error: default method " + decl.signature() + " in " + decl.hostType().typeName() + " is hidden by private method " + m.signature() + " in " + m.hostType().typeName());
                    }
                    if (decl.isFinal()) {
                        target.error("method " + m.signature() + " in " + hostType().typeName() + " can not hide final method " + decl.signature() + " in " + decl.hostType().typeName());
                    }
                }
            }
        }
    }

    public TypeDecl makeGeneric(Signatures.ClassSignature s) {
        return this;
    }

    public TypeDecl substitute(TypeVariable typeVariable) {
        if (isTopLevelType()) {
            return typeVariable;
        }
        return enclosingType().substitute(typeVariable);
    }

    public Access substitute(Parameterization parTypeDecl) {
        if ((parTypeDecl instanceof ParTypeDecl) && ((ParTypeDecl) parTypeDecl).genericDecl() == this) {
            return ((TypeDecl) parTypeDecl).createBoundAccess();
        }
        if (isTopLevelType()) {
            return createBoundAccess();
        }
        return enclosingType().substitute(parTypeDecl).qualifiesAccess(new TypeAccess(name()));
    }

    public Access substituteReturnType(Parameterization parTypeDecl) {
        return substitute(parTypeDecl);
    }

    public Access substituteParameterType(Parameterization parTypeDecl) {
        return substitute(parTypeDecl);
    }

    public boolean hasField(String name) {
        if (!memberFields(name).isEmpty()) {
            return true;
        }
        for (int i = 0; i < getNumBodyDecl(); i++) {
            if (getBodyDecl(i) instanceof FieldDeclaration) {
                FieldDeclaration decl = (FieldDeclaration) getBodyDecl(i);
                if (decl.name().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasMethod(String id) {
        if (!memberMethods(id).isEmpty()) {
            return true;
        }
        for (int i = 0; i < getNumBodyDecl(); i++) {
            if (getBodyDecl(i) instanceof MethodDecl) {
                MethodDecl decl = (MethodDecl) getBodyDecl(i);
                if (decl.name().equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Collection nestedTypes() {
        return this.nestedTypes != null ? this.nestedTypes : new HashSet();
    }

    public void addNestedType(TypeDecl typeDecl) {
        if (this.nestedTypes == null) {
            this.nestedTypes = new HashSet();
        }
        if (typeDecl != this) {
            this.nestedTypes.add(typeDecl);
        }
    }

    public Collection usedNestedTypes() {
        return this.usedNestedTypes != null ? this.usedNestedTypes : new HashSet();
    }

    public void addUsedNestedType(TypeDecl typeDecl) {
        if (this.usedNestedTypes == null) {
            this.usedNestedTypes = new HashSet();
        }
        this.usedNestedTypes.add(typeDecl);
    }

    public ASTNode getAccessor(ASTNode source, String name) {
        ArrayList key = new ArrayList(2);
        key.add(source);
        key.add(name);
        if (this.accessorMap == null || !this.accessorMap.containsKey(key)) {
            return null;
        }
        return (ASTNode) this.accessorMap.get(key);
    }

    public void addAccessor(ASTNode source, String name, ASTNode accessor) {
        ArrayList key = new ArrayList(2);
        key.add(source);
        key.add(name);
        if (this.accessorMap == null) {
            this.accessorMap = new HashMap();
        }
        this.accessorMap.put(key, accessor);
    }

    public ASTNode getAccessorSource(ASTNode accessor) {
        for (Map.Entry entry : this.accessorMap.entrySet()) {
            if (entry.getValue() == accessor) {
                return (ASTNode) ((ArrayList) entry.getKey()).get(0);
            }
        }
        return null;
    }

    public void addEnclosingVariables() {
        if (!this.addEnclosingVariables) {
            return;
        }
        this.addEnclosingVariables = false;
        for (Variable v : enclosingVariables()) {
            Modifiers m = new Modifiers();
            m.addModifier(new Modifier(Jimple.PUBLIC));
            m.addModifier(new Modifier("synthetic"));
            m.addModifier(new Modifier(Jimple.FINAL));
            addMemberField(new FieldDeclaration(m, v.type().createQualifiedAccess(), "val$" + v.name(), new Opt()));
        }
    }

    public FieldDeclaration createAssertionsDisabled() {
        if (this.createAssertionsDisabled != null) {
            return this.createAssertionsDisabled;
        }
        this.createAssertionsDisabled = new FieldDeclaration(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC)).add(new Modifier(Jimple.STATIC)).add(new Modifier(Jimple.FINAL))), new PrimitiveTypeAccess("boolean"), "$assertionsDisabled", new Opt(new LogNotExpr(topLevelType().createQualifiedAccess().qualifiesAccess(new ClassAccess().qualifiesAccess(new MethodAccess("desiredAssertionStatus", new List()))))));
        getBodyDeclList().insertChild(this.createAssertionsDisabled, 0);
        this.createAssertionsDisabled = (FieldDeclaration) getBodyDeclList().getChild(0);
        this.createAssertionsDisabled.transformation();
        return this.createAssertionsDisabled;
    }

    public FieldDeclaration createStaticClassField(String name) {
        if (this.createStaticClassField == null) {
            this.createStaticClassField = new HashMap();
        }
        if (this.createStaticClassField.containsKey(name)) {
            return (FieldDeclaration) this.createStaticClassField.get(name);
        }
        FieldDeclaration f = new FieldDeclaration(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC)).add(new Modifier(Jimple.STATIC))), lookupType("java.lang", "Class").createQualifiedAccess(), name, new Opt()) { // from class: soot.JastAddJ.TypeDecl.4
            @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.MemberDecl
            public boolean isConstant() {
                return true;
            }
        };
        this.createStaticClassField.put(name, f);
        return addMemberField(f);
    }

    public MethodDecl createStaticClassMethod() {
        if (this.createStaticClassMethod != null) {
            return this.createStaticClassMethod;
        }
        this.createStaticClassMethod = new MethodDecl(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC)).add(new Modifier(Jimple.STATIC))), lookupType("java.lang", "Class").createQualifiedAccess(), "class$", new List().add(new ParameterDeclaration(new Modifiers(new List()), lookupType("java.lang", "String").createQualifiedAccess(), "name")), new List(), new Opt(new Block(new List().add(new TryStmt(new Block(new List().add(new ReturnStmt(new Opt(lookupType("java.lang", "Class").createQualifiedAccess().qualifiesAccess(new MethodAccess("forName", new List().add(new VarAccess("name")))))))), new List().add(new BasicCatch(new ParameterDeclaration(new Modifiers(new List()), lookupType("java.lang", "ClassNotFoundException").createQualifiedAccess(), "e"), new Block(new List().add(new ThrowStmt(new ClassInstanceExpr(lookupType("java.lang", "NoClassDefFoundError").createQualifiedAccess(), new List().add(new VarAccess("e").qualifiesAccess(new MethodAccess("getMessage", new List()))), new Opt())))))), new Opt()))))) { // from class: soot.JastAddJ.TypeDecl.5
            @Override // soot.JastAddJ.MemberDecl
            public boolean isConstant() {
                return true;
            }
        };
        return addMemberMethod(this.createStaticClassMethod);
    }

    @Override // soot.JastAddJ.ASTNode
    public void transformation() {
        addEnclosingVariables();
        super.transformation();
        if (isNestedType()) {
            enclosingType().addNestedType(this);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void jimplify1phase2() {
        if (needsClinit() && !getSootClassDecl().declaresMethod("<clinit>", new ArrayList())) {
            this.clinit = Scene.v().makeSootMethod("<clinit>", new ArrayList(), soot.VoidType.v(), 8, new ArrayList());
            getSootClassDecl().addMethod(this.clinit);
        }
        for (TypeDecl typeDecl : nestedTypes()) {
            typeDecl.jimplify1phase2();
        }
        for (int i = 0; i < getNumBodyDecl(); i++) {
            if (getBodyDecl(i).generate()) {
                getBodyDecl(i).jimplify1phase2();
            }
        }
        addAttributes();
    }

    public Value emitCastTo(Body b, Value v, TypeDecl type, ASTNode location) {
        if (this == type) {
            return v;
        }
        if (isReferenceType() && type.isReferenceType() && instanceOf(type)) {
            return v;
        }
        if ((isLong() || (this instanceof FloatingPointType)) && type.isIntegralType()) {
            return typeInt().emitCastTo(b, b.newCastExpr(asImmediate(b, v), typeInt().getSootType(), location), type, location);
        }
        return b.newCastExpr(asImmediate(b, v), type.getSootType(), location);
    }

    public void jimplify2clinit() {
        SootMethod m = this.clinit;
        JimpleBody body = Jimple.v().newBody(m);
        m.setActiveBody(body);
        Body b = new Body(this, body, this);
        for (int i = 0; i < getNumBodyDecl(); i++) {
            BodyDecl bodyDecl = getBodyDecl(i);
            if ((bodyDecl instanceof FieldDeclaration) && bodyDecl.generate()) {
                FieldDeclaration f = (FieldDeclaration) bodyDecl;
                if (f.isStatic() && f.hasInit()) {
                    Local l = asLocal(b, f.getInit().type().emitCastTo(b, f.getInit(), f.type()), f.type().getSootType());
                    b.setLine(f);
                    b.add(b.newAssignStmt(b.newStaticFieldRef(f.sootRef(), f), l, f));
                }
            } else if ((bodyDecl instanceof StaticInitializer) && bodyDecl.generate()) {
                bodyDecl.jimplify2(b);
            }
        }
        b.add(b.newReturnVoidStmt(null));
    }

    @Override // soot.JastAddJ.ASTNode
    public void jimplify2() {
        super.jimplify2();
        if (this.clinit != null) {
            jimplify2clinit();
        }
        for (TypeDecl typeDecl : nestedTypes()) {
            typeDecl.jimplify2();
        }
        ArrayList tags = new ArrayList();
        for (TypeDecl type : innerClassesAttributeEntries()) {
            tags.add(new InnerClassTag(type.jvmName().replace('.', '/'), type.isMemberType() ? type.enclosingType().jvmName().replace('.', '/') : null, type.isAnonymous() ? null : type.name(), type.sootTypeModifiers()));
        }
        if (!tags.isEmpty()) {
            getSootClassDecl().addTag(new InnerClassAttribute(tags));
        }
        addAttributes();
        getSootClassDecl().setResolvingLevel(3);
    }

    @Override // soot.JastAddJ.ASTNode
    public void addAttributes() {
        super.addAttributes();
        ArrayList c = new ArrayList();
        getModifiers().addRuntimeVisibleAnnotationsAttribute(c);
        getModifiers().addRuntimeInvisibleAnnotationsAttribute(c);
        getModifiers().addSourceOnlyAnnotations(c);
        Iterator iter = c.iterator();
        while (iter.hasNext()) {
            Tag tag = (Tag) iter.next();
            getSootClassDecl().addTag(tag);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Value emitBoxingOperation(Body b, Value v, ASTNode location) {
        ArrayList parameters = new ArrayList();
        parameters.add(unboxed().getSootType());
        SootMethodRef ref = Scene.v().makeMethodRef(getSootClassDecl(), "valueOf", parameters, getSootType(), true);
        ArrayList args = new ArrayList();
        args.add(asLocal(b, v));
        return b.newStaticInvokeExpr(ref, args, location);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Value emitUnboxingOperation(Body b, Value v, ASTNode location) {
        SootMethodRef ref = Scene.v().makeMethodRef(getSootClassDecl(), String.valueOf(unboxed().name()) + XmlConstants.Attributes.value, new ArrayList(), unboxed().getSootType(), false);
        return b.newVirtualInvokeExpr(asLocal(b, v), ref, new ArrayList(), location);
    }

    public TypeDecl() {
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
        setChild(new List(), 1);
    }

    public TypeDecl(Modifiers p0, String p1, List<BodyDecl> p2) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
    }

    public TypeDecl(Modifiers p0, Symbol p1, List<BodyDecl> p2) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
    }

    @Override // soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    public void setID(String value) {
        this.tokenString_ID = value;
    }

    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 1);
    }

    public int getNumBodyDecl() {
        return getBodyDeclList().getNumChild();
    }

    public int getNumBodyDeclNoTransform() {
        return getBodyDeclListNoTransform().getNumChildNoTransform();
    }

    public BodyDecl getBodyDecl(int i) {
        return getBodyDeclList().getChild(i);
    }

    public void addBodyDecl(BodyDecl node) {
        List<BodyDecl> list = (this.parent == null || state == null) ? getBodyDeclListNoTransform() : getBodyDeclList();
        list.addChild(node);
    }

    public void addBodyDeclNoTransform(BodyDecl node) {
        List<BodyDecl> list = getBodyDeclListNoTransform();
        list.addChild(node);
    }

    public void setBodyDecl(BodyDecl node, int i) {
        List<BodyDecl> list = getBodyDeclList();
        list.setChild(node, i);
    }

    public List<BodyDecl> getBodyDecls() {
        return getBodyDeclList();
    }

    public List<BodyDecl> getBodyDeclsNoTransform() {
        return getBodyDeclListNoTransform();
    }

    public List<BodyDecl> getBodyDeclList() {
        List<BodyDecl> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        ParInterfaceDecl q;
        refined_TypeHierarchyCheck_TypeDecl_typeCheck();
        ArrayList list = new ArrayList();
        list.addAll(implementedInterfaces());
        for (int i = 0; i < list.size(); i++) {
            InterfaceDecl decl = (InterfaceDecl) list.get(i);
            if (decl instanceof ParInterfaceDecl) {
                ParInterfaceDecl p = (ParInterfaceDecl) decl;
                Iterator i2 = list.listIterator(i);
                while (i2.hasNext()) {
                    InterfaceDecl decl2 = (InterfaceDecl) i2.next();
                    if ((decl2 instanceof ParInterfaceDecl) && p != (q = (ParInterfaceDecl) decl2) && p.genericDecl() == q.genericDecl() && !p.sameArgument(q)) {
                        error(String.valueOf(p.genericDecl().name()) + " cannot be inherited with different arguments: " + p.typeName() + " and " + q.typeName());
                    }
                }
            }
        }
    }

    public Value emitCastTo(Body b, Expr expr, TypeDecl type) {
        if ((type instanceof LUBType) || (type instanceof GLBType) || (type instanceof AbstractWildcardType)) {
            type = typeObject();
        } else if (expr.isConstant() && isPrimitive() && type.isReferenceType()) {
            return boxed().emitBoxingOperation(b, emitConstant(cast(expr.constant())), expr);
        } else {
            if (expr.isConstant() && !expr.type().isEnumDecl()) {
                if (type.isPrimitive()) {
                    return emitConstant(type.cast(expr.constant()));
                }
                return emitConstant(expr.constant());
            }
        }
        return emitCastTo(b, expr.eval(b), type, expr);
    }

    private boolean refined_TypeConversion_TypeDecl_assignConversionTo_TypeDecl_Expr(TypeDecl type, Expr expr) {
        boolean sourceIsConstant = expr != null ? expr.isConstant() : false;
        if (identityConversionTo(type) || wideningConversionTo(type)) {
            return true;
        }
        if (sourceIsConstant) {
            if (isInt() || isChar() || isShort() || isByte()) {
                if ((type.isByte() || type.isShort() || type.isChar()) && narrowingConversionTo(type) && expr.representableIn(type)) {
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private boolean refined_TypeConversion_TypeDecl_methodInvocationConversionTo_TypeDecl(TypeDecl type) {
        return identityConversionTo(type) || wideningConversionTo(type);
    }

    private boolean refined_TypeConversion_TypeDecl_castingConversionTo_TypeDecl(TypeDecl type) {
        return identityConversionTo(type) || wideningConversionTo(type) || narrowingConversionTo(type);
    }

    private SootClass refined_EmitJimple_TypeDecl_getSootClassDecl() {
        if (compilationUnit().fromSource()) {
            return sootClass();
        }
        if (options().verbose()) {
            System.out.println("Loading .class file " + jvmName());
        }
        SootClass sc = Scene.v().loadClass(jvmName(), 2);
        sc.setLibraryClass();
        return sc;
    }

    private Type refined_EmitJimple_TypeDecl_getSootType() {
        return getSootClassDecl().getType();
    }

    private SootClass refined_EmitJimple_TypeDecl_sootClass() {
        return null;
    }

    public Constant cast(Constant c) {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation cast not supported for type " + getClass().getName());
    }

    public Constant plus(Constant c) {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation plus not supported for type " + getClass().getName());
    }

    public Constant minus(Constant c) {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation minus not supported for type " + getClass().getName());
    }

    public Constant bitNot(Constant c) {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation bitNot not supported for type " + getClass().getName());
    }

    public Constant mul(Constant c1, Constant c2) {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation mul not supported for type " + getClass().getName());
    }

    public Constant div(Constant c1, Constant c2) {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation div not supported for type " + getClass().getName());
    }

    public Constant mod(Constant c1, Constant c2) {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation mod not supported for type " + getClass().getName());
    }

    public Constant add(Constant c1, Constant c2) {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation add not supported for type " + getClass().getName());
    }

    public Constant sub(Constant c1, Constant c2) {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation sub not supported for type " + getClass().getName());
    }

    public Constant lshift(Constant c1, Constant c2) {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation lshift not supported for type " + getClass().getName());
    }

    public Constant rshift(Constant c1, Constant c2) {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation rshift not supported for type " + getClass().getName());
    }

    public Constant urshift(Constant c1, Constant c2) {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation urshift not supported for type " + getClass().getName());
    }

    public Constant andBitwise(Constant c1, Constant c2) {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation andBitwise not supported for type " + getClass().getName());
    }

    public Constant xorBitwise(Constant c1, Constant c2) {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation xorBitwise not supported for type " + getClass().getName());
    }

    public Constant orBitwise(Constant c1, Constant c2) {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation orBitwise not supported for type " + getClass().getName());
    }

    public Constant questionColon(Constant cond, Constant c1, Constant c2) {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation questionColon not supported for type " + getClass().getName());
    }

    public boolean eqIsTrue(Expr left, Expr right) {
        state();
        System.err.println("Evaluation eqIsTrue for unknown type: " + getClass().getName());
        return false;
    }

    public boolean ltIsTrue(Expr left, Expr right) {
        state();
        return false;
    }

    public boolean leIsTrue(Expr left, Expr right) {
        state();
        return false;
    }

    public boolean accessibleFromPackage(String packageName) {
        if (this.accessibleFromPackage_String_values == null) {
            this.accessibleFromPackage_String_values = new HashMap(4);
        }
        if (this.accessibleFromPackage_String_values.containsKey(packageName)) {
            return ((Boolean) this.accessibleFromPackage_String_values.get(packageName)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean accessibleFromPackage_String_value = accessibleFromPackage_compute(packageName);
        if (isFinal && num == state().boundariesCrossed) {
            this.accessibleFromPackage_String_values.put(packageName, Boolean.valueOf(accessibleFromPackage_String_value));
        }
        return accessibleFromPackage_String_value;
    }

    private boolean accessibleFromPackage_compute(String packageName) {
        if (isPrivate()) {
            return false;
        }
        return isPublic() || hostPackage().equals(packageName);
    }

    public boolean accessibleFromExtend(TypeDecl type) {
        if (this.accessibleFromExtend_TypeDecl_values == null) {
            this.accessibleFromExtend_TypeDecl_values = new HashMap(4);
        }
        if (this.accessibleFromExtend_TypeDecl_values.containsKey(type)) {
            return ((Boolean) this.accessibleFromExtend_TypeDecl_values.get(type)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean accessibleFromExtend_TypeDecl_value = accessibleFromExtend_compute(type);
        if (isFinal && num == state().boundariesCrossed) {
            this.accessibleFromExtend_TypeDecl_values.put(type, Boolean.valueOf(accessibleFromExtend_TypeDecl_value));
        }
        return accessibleFromExtend_TypeDecl_value;
    }

    private boolean accessibleFromExtend_compute(TypeDecl type) {
        if (type == this) {
            return true;
        }
        if (isInnerType() && !enclosingType().accessibleFrom(type)) {
            return false;
        }
        if (isPublic()) {
            return true;
        }
        if (isProtected()) {
            if (hostPackage().equals(type.hostPackage())) {
                return true;
            }
            if (type.isNestedType() && type.enclosingType().withinBodyThatSubclasses(enclosingType()) != null) {
                return true;
            }
            return false;
        } else if (isPrivate()) {
            return topLevelType() == type.topLevelType();
        } else {
            return hostPackage().equals(type.hostPackage());
        }
    }

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
        TypeDecl typeDecl;
        if (type == this) {
            return true;
        }
        if (isInnerType() && !enclosingType().accessibleFrom(type)) {
            return false;
        }
        if (isPublic()) {
            return true;
        }
        if (isProtected()) {
            if (hostPackage().equals(type.hostPackage())) {
                return true;
            }
            if (isMemberType()) {
                TypeDecl typeDecl2 = type;
                while (true) {
                    typeDecl = typeDecl2;
                    if (typeDecl == null || typeDecl.instanceOf(enclosingType())) {
                        break;
                    }
                    typeDecl2 = typeDecl.enclosingType();
                }
                if (typeDecl != null) {
                    return true;
                }
                return false;
            }
            return false;
        } else if (isPrivate()) {
            return topLevelType() == type.topLevelType();
        } else {
            return hostPackage().equals(type.hostPackage());
        }
    }

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
        return 0;
    }

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
        return this;
    }

    public TypeDecl arrayType() {
        if (this.arrayType_computed) {
            return this.arrayType_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.arrayType_value = arrayType_compute();
        this.arrayType_value.setParent(this);
        this.arrayType_value.is$Final = true;
        this.arrayType_computed = true;
        return this.arrayType_value;
    }

    private TypeDecl arrayType_compute() {
        String name = String.valueOf(name()) + "[]";
        List body = new List();
        body.add(new FieldDeclaration(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC)).add(new Modifier(Jimple.FINAL))), new PrimitiveTypeAccess("int"), XMLConstants.LENGTH_ATTRIBUTE, new Opt()));
        MethodDecl clone = null;
        TypeDecl typeObject = typeObject();
        for (int i = 0; clone == null && i < typeObject.getNumBodyDecl(); i++) {
            if (typeObject.getBodyDecl(i) instanceof MethodDecl) {
                MethodDecl m = (MethodDecl) typeObject.getBodyDecl(i);
                if (m.name().equals("clone")) {
                    clone = m;
                }
            }
        }
        if (clone != null) {
            body.add(new MethodDeclSubstituted(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))), new ArrayTypeAccess(createQualifiedAccess()), "clone", new List(), new List(), new Opt(new Block()), (MethodDecl) typeObject().memberMethods("clone").iterator().next()));
        }
        TypeDecl typeDecl = new ArrayDecl(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))), name, new Opt(typeObject().createQualifiedAccess()), new List().add(typeCloneable().createQualifiedAccess()).add(typeSerializable().createQualifiedAccess()), body);
        return typeDecl;
    }

    @Override // soot.JastAddJ.SimpleSet
    public int size() {
        state();
        return 1;
    }

    @Override // soot.JastAddJ.SimpleSet
    public boolean isEmpty() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.SimpleSet
    public boolean contains(Object o) {
        state();
        return this == o;
    }

    public boolean isException() {
        if (this.isException_computed) {
            return this.isException_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isException_value = isException_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isException_computed = true;
        }
        return this.isException_value;
    }

    private boolean isException_compute() {
        return instanceOf(typeException());
    }

    public boolean isCheckedException() {
        if (this.isCheckedException_computed) {
            return this.isCheckedException_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isCheckedException_value = isCheckedException_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isCheckedException_computed = true;
        }
        return this.isCheckedException_value;
    }

    private boolean isCheckedException_compute() {
        if (isException()) {
            return instanceOf(typeRuntimeException()) || instanceOf(typeError());
        }
        return false;
    }

    public boolean isUncheckedException() {
        if (this.isUncheckedException_computed) {
            return this.isUncheckedException_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isUncheckedException_value = isUncheckedException_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isUncheckedException_computed = true;
        }
        return this.isUncheckedException_value;
    }

    private boolean isUncheckedException_compute() {
        return isException() && !isCheckedException();
    }

    public boolean mayCatch(TypeDecl thrownType) {
        if (this.mayCatch_TypeDecl_values == null) {
            this.mayCatch_TypeDecl_values = new HashMap(4);
        }
        if (this.mayCatch_TypeDecl_values.containsKey(thrownType)) {
            return ((Boolean) this.mayCatch_TypeDecl_values.get(thrownType)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean mayCatch_TypeDecl_value = mayCatch_compute(thrownType);
        if (isFinal && num == state().boundariesCrossed) {
            this.mayCatch_TypeDecl_values.put(thrownType, Boolean.valueOf(mayCatch_TypeDecl_value));
        }
        return mayCatch_TypeDecl_value;
    }

    private boolean mayCatch_compute(TypeDecl thrownType) {
        return thrownType.instanceOf(this) || instanceOf(thrownType);
    }

    public Collection lookupSuperConstructor() {
        state();
        return Collections.EMPTY_LIST;
    }

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
        Collection c = new ArrayList();
        for (int i = 0; i < getNumBodyDecl(); i++) {
            if (getBodyDecl(i) instanceof ConstructorDecl) {
                c.add(getBodyDecl(i));
            }
        }
        return c;
    }

    public Collection unqualifiedLookupMethod(String name) {
        if (this.unqualifiedLookupMethod_String_values == null) {
            this.unqualifiedLookupMethod_String_values = new HashMap(4);
        }
        if (this.unqualifiedLookupMethod_String_values.containsKey(name)) {
            return (Collection) this.unqualifiedLookupMethod_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        Collection unqualifiedLookupMethod_String_value = unqualifiedLookupMethod_compute(name);
        if (isFinal && num == state().boundariesCrossed) {
            this.unqualifiedLookupMethod_String_values.put(name, unqualifiedLookupMethod_String_value);
        }
        return unqualifiedLookupMethod_String_value;
    }

    private Collection unqualifiedLookupMethod_compute(String name) {
        Collection c = memberMethods(name);
        if (!c.isEmpty()) {
            return c;
        }
        if (isInnerType()) {
            return lookupMethod(name);
        }
        return removeInstanceMethods(lookupMethod(name));
    }

    public Collection memberMethods(String name) {
        state();
        Collection c = (Collection) methodsNameMap().get(name);
        if (c != null) {
            return c;
        }
        return Collections.EMPTY_LIST;
    }

    public HashMap methodsNameMap() {
        if (this.methodsNameMap_computed) {
            return this.methodsNameMap_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.methodsNameMap_value = methodsNameMap_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.methodsNameMap_computed = true;
        }
        return this.methodsNameMap_value;
    }

    private HashMap methodsNameMap_compute() {
        HashMap map = new HashMap();
        Iterator iter = methodsIterator();
        while (iter.hasNext()) {
            MethodDecl m = (MethodDecl) iter.next();
            ArrayList list = (ArrayList) map.get(m.name());
            if (list == null) {
                list = new ArrayList(4);
                map.put(m.name(), list);
            }
            list.add(m);
        }
        return map;
    }

    public SimpleSet localMethodsSignature(String signature) {
        state();
        SimpleSet set = (SimpleSet) localMethodsSignatureMap().get(signature);
        if (set != null) {
            return set;
        }
        return SimpleSet.emptySet;
    }

    public HashMap localMethodsSignatureMap() {
        if (this.localMethodsSignatureMap_computed) {
            return this.localMethodsSignatureMap_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.localMethodsSignatureMap_value = localMethodsSignatureMap_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.localMethodsSignatureMap_computed = true;
        }
        return this.localMethodsSignatureMap_value;
    }

    private HashMap localMethodsSignatureMap_compute() {
        HashMap map = new HashMap(getNumBodyDecl());
        for (int i = 0; i < getNumBodyDecl(); i++) {
            if (getBodyDecl(i) instanceof MethodDecl) {
                MethodDecl decl = (MethodDecl) getBodyDecl(i);
                map.put(decl.signature(), decl);
            }
        }
        return map;
    }

    public SimpleSet methodsSignature(String signature) {
        state();
        SimpleSet set = (SimpleSet) methodsSignatureMap().get(signature);
        if (set != null) {
            return set;
        }
        return SimpleSet.emptySet;
    }

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
        return localMethodsSignatureMap();
    }

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
        return SimpleSet.emptySet;
    }

    public boolean hasType(String name) {
        state();
        return !memberTypes(name).isEmpty();
    }

    public SimpleSet localTypeDecls(String name) {
        if (this.localTypeDecls_String_values == null) {
            this.localTypeDecls_String_values = new HashMap(4);
        }
        if (this.localTypeDecls_String_values.containsKey(name)) {
            return (SimpleSet) this.localTypeDecls_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet localTypeDecls_String_value = localTypeDecls_compute(name);
        if (isFinal && num == state().boundariesCrossed) {
            this.localTypeDecls_String_values.put(name, localTypeDecls_String_value);
        }
        return localTypeDecls_String_value;
    }

    private SimpleSet localTypeDecls_compute(String name) {
        SimpleSet set = SimpleSet.emptySet;
        for (int i = 0; i < getNumBodyDecl(); i++) {
            if (getBodyDecl(i).declaresType(name)) {
                set = set.add(getBodyDecl(i).type(name));
            }
        }
        return set;
    }

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
        return SimpleSet.emptySet;
    }

    public SimpleSet localFields(String name) {
        if (this.localFields_String_values == null) {
            this.localFields_String_values = new HashMap(4);
        }
        if (this.localFields_String_values.containsKey(name)) {
            return (SimpleSet) this.localFields_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet localFields_String_value = localFields_compute(name);
        if (isFinal && num == state().boundariesCrossed) {
            this.localFields_String_values.put(name, localFields_String_value);
        }
        return localFields_String_value;
    }

    private SimpleSet localFields_compute(String name) {
        return localFieldsMap().containsKey(name) ? (SimpleSet) localFieldsMap().get(name) : SimpleSet.emptySet;
    }

    public HashMap localFieldsMap() {
        if (this.localFieldsMap_computed) {
            return this.localFieldsMap_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.localFieldsMap_value = localFieldsMap_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.localFieldsMap_computed = true;
        }
        return this.localFieldsMap_value;
    }

    private HashMap localFieldsMap_compute() {
        HashMap map = new HashMap();
        for (int i = 0; i < getNumBodyDecl(); i++) {
            if (getBodyDecl(i) instanceof FieldDeclaration) {
                FieldDeclaration decl = (FieldDeclaration) getBodyDecl(i);
                SimpleSet fields = (SimpleSet) map.get(decl.name());
                if (fields == null) {
                    fields = SimpleSet.emptySet;
                }
                map.put(decl.name(), fields.add(decl));
            }
        }
        return map;
    }

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
        return localFieldsMap();
    }

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
        return localFields(name);
    }

    public boolean hasAbstract() {
        if (this.hasAbstract_computed) {
            return this.hasAbstract_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.hasAbstract_value = hasAbstract_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.hasAbstract_computed = true;
        }
        return this.hasAbstract_value;
    }

    private boolean hasAbstract_compute() {
        return false;
    }

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
        return Collections.EMPTY_LIST;
    }

    public boolean isPublic() {
        if (this.isPublic_computed) {
            return this.isPublic_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isPublic_value = isPublic_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isPublic_computed = true;
        }
        return this.isPublic_value;
    }

    private boolean isPublic_compute() {
        if (getModifiers().isPublic()) {
            return true;
        }
        return isMemberType() && enclosingType().isInterfaceDecl();
    }

    public boolean isPrivate() {
        state();
        return getModifiers().isPrivate();
    }

    public boolean isProtected() {
        state();
        return getModifiers().isProtected();
    }

    public boolean isAbstract() {
        state();
        return getModifiers().isAbstract();
    }

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
        if (getModifiers().isStatic()) {
            return true;
        }
        return isMemberType() && enclosingType().isInterfaceDecl();
    }

    public boolean isFinal() {
        state();
        return getModifiers().isFinal();
    }

    public boolean isStrictfp() {
        state();
        return getModifiers().isStrictfp();
    }

    public boolean isSynthetic() {
        state();
        return getModifiers().isSynthetic();
    }

    public boolean hasEnclosingTypeDecl(String name) {
        state();
        TypeDecl enclosingType = enclosingType();
        if (enclosingType != null) {
            return enclosingType.name().equals(name) || enclosingType.hasEnclosingTypeDecl(name);
        }
        return false;
    }

    public boolean assignableToInt() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean addsIndentationLevel() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.ASTNode
    public String dumpString() {
        state();
        return String.valueOf(getClass().getName()) + " [" + getID() + "]";
    }

    public String name() {
        state();
        return getID();
    }

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
            return String.valueOf(enclosingType().fullName()) + "." + name();
        }
        String packageName = packageName();
        if (packageName.equals("")) {
            return name();
        }
        return String.valueOf(packageName) + "." + name();
    }

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
            return String.valueOf(enclosingType().typeName()) + "." + name();
        }
        String packageName = packageName();
        if (packageName.equals("") || packageName.equals("@primitive")) {
            return name();
        }
        return String.valueOf(packageName) + "." + name();
    }

    public boolean identityConversionTo(TypeDecl type) {
        state();
        return this == type;
    }

    public boolean wideningConversionTo(TypeDecl type) {
        state();
        return instanceOf(type);
    }

    public boolean narrowingConversionTo(TypeDecl type) {
        if (this.narrowingConversionTo_TypeDecl_values == null) {
            this.narrowingConversionTo_TypeDecl_values = new HashMap(4);
        }
        if (this.narrowingConversionTo_TypeDecl_values.containsKey(type)) {
            return ((Boolean) this.narrowingConversionTo_TypeDecl_values.get(type)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean narrowingConversionTo_TypeDecl_value = narrowingConversionTo_compute(type);
        if (isFinal && num == state().boundariesCrossed) {
            this.narrowingConversionTo_TypeDecl_values.put(type, Boolean.valueOf(narrowingConversionTo_TypeDecl_value));
        }
        return narrowingConversionTo_TypeDecl_value;
    }

    private boolean narrowingConversionTo_compute(TypeDecl type) {
        return instanceOf(type);
    }

    public boolean stringConversion() {
        state();
        return true;
    }

    public boolean assignConversionTo(TypeDecl type, Expr expr) {
        state();
        if (refined_TypeConversion_TypeDecl_assignConversionTo_TypeDecl_Expr(type, expr)) {
            return true;
        }
        boolean canBoxThis = this instanceof PrimitiveType;
        boolean canBoxType = type instanceof PrimitiveType;
        boolean canUnboxThis = !unboxed().isUnknown();
        boolean canUnboxType = !type.unboxed().isUnknown();
        TypeDecl t = (canUnboxThis || !canUnboxType) ? type : type.unboxed();
        boolean sourceIsConstant = expr != null ? expr.isConstant() : false;
        if (sourceIsConstant && ((isInt() || isChar() || isShort() || isByte()) && ((t.isByte() || t.isShort() || t.isChar()) && narrowingConversionTo(t) && expr.representableIn(t)))) {
            return true;
        }
        if (canBoxThis && !canBoxType && boxed().wideningConversionTo(type)) {
            return true;
        }
        if (canUnboxThis && !canUnboxType && unboxed().wideningConversionTo(type)) {
            return true;
        }
        return false;
    }

    public boolean methodInvocationConversionTo(TypeDecl type) {
        if (this.methodInvocationConversionTo_TypeDecl_values == null) {
            this.methodInvocationConversionTo_TypeDecl_values = new HashMap(4);
        }
        if (this.methodInvocationConversionTo_TypeDecl_values.containsKey(type)) {
            return ((Boolean) this.methodInvocationConversionTo_TypeDecl_values.get(type)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean methodInvocationConversionTo_TypeDecl_value = methodInvocationConversionTo_compute(type);
        if (isFinal && num == state().boundariesCrossed) {
            this.methodInvocationConversionTo_TypeDecl_values.put(type, Boolean.valueOf(methodInvocationConversionTo_TypeDecl_value));
        }
        return methodInvocationConversionTo_TypeDecl_value;
    }

    private boolean methodInvocationConversionTo_compute(TypeDecl type) {
        if (refined_TypeConversion_TypeDecl_methodInvocationConversionTo_TypeDecl(type)) {
            return true;
        }
        boolean canBoxThis = this instanceof PrimitiveType;
        boolean canBoxType = type instanceof PrimitiveType;
        boolean canUnboxThis = !unboxed().isUnknown();
        boolean canUnboxType = !type.unboxed().isUnknown();
        if (canBoxThis && !canBoxType) {
            return boxed().wideningConversionTo(type);
        }
        if (canUnboxThis && !canUnboxType) {
            return unboxed().wideningConversionTo(type);
        }
        return false;
    }

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
        if (refined_TypeConversion_TypeDecl_castingConversionTo_TypeDecl(type)) {
            return true;
        }
        boolean canBoxThis = this instanceof PrimitiveType;
        boolean canBoxType = type instanceof PrimitiveType;
        boolean canUnboxThis = !unboxed().isUnknown();
        boolean canUnboxType = !type.unboxed().isUnknown();
        if (canBoxThis && !canBoxType) {
            return boxed().wideningConversionTo(type);
        }
        if (canUnboxThis && !canUnboxType) {
            return unboxed().wideningConversionTo(type);
        }
        return false;
    }

    public TypeDecl unaryNumericPromotion() {
        state();
        return this;
    }

    public TypeDecl binaryNumericPromotion(TypeDecl type) {
        state();
        return unknownType();
    }

    public boolean isReferenceType() {
        state();
        return false;
    }

    public boolean isPrimitiveType() {
        state();
        return false;
    }

    public boolean isNumericType() {
        state();
        return false;
    }

    public boolean isIntegralType() {
        state();
        return false;
    }

    public boolean isBoolean() {
        state();
        return false;
    }

    public boolean isByte() {
        state();
        return false;
    }

    public boolean isChar() {
        state();
        return false;
    }

    public boolean isShort() {
        state();
        return false;
    }

    public boolean isInt() {
        state();
        return false;
    }

    public boolean isFloat() {
        state();
        return false;
    }

    public boolean isLong() {
        state();
        return false;
    }

    public boolean isDouble() {
        state();
        return false;
    }

    public boolean isVoid() {
        state();
        return false;
    }

    public boolean isNull() {
        state();
        return false;
    }

    public boolean isClassDecl() {
        state();
        return false;
    }

    public boolean isInterfaceDecl() {
        state();
        return false;
    }

    public boolean isArrayDecl() {
        state();
        return false;
    }

    public boolean isPrimitive() {
        state();
        return false;
    }

    public boolean isString() {
        if (this.isString_computed) {
            return this.isString_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isString_value = isString_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isString_computed = true;
        }
        return this.isString_value;
    }

    private boolean isString_compute() {
        return false;
    }

    public boolean isObject() {
        if (this.isObject_computed) {
            return this.isObject_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isObject_value = isObject_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isObject_computed = true;
        }
        return this.isObject_value;
    }

    private boolean isObject_compute() {
        return false;
    }

    public boolean isUnknown() {
        state();
        return false;
    }

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

    public boolean isSupertypeOfClassDecl(ClassDecl type) {
        state();
        return type == this;
    }

    public boolean isSupertypeOfInterfaceDecl(InterfaceDecl type) {
        state();
        return type == this;
    }

    public boolean isSupertypeOfArrayDecl(ArrayDecl type) {
        state();
        return this == type;
    }

    public boolean isSupertypeOfPrimitiveType(PrimitiveType type) {
        state();
        return type == this;
    }

    public boolean isSupertypeOfNullType(NullType type) {
        state();
        return false;
    }

    public boolean isSupertypeOfVoidType(VoidType type) {
        state();
        return false;
    }

    public TypeDecl topLevelType() {
        state();
        if (isTopLevelType()) {
            return this;
        }
        return enclosingType().topLevelType();
    }

    public boolean isTopLevelType() {
        state();
        return !isNestedType();
    }

    public boolean isInnerClass() {
        state();
        return false;
    }

    public boolean isInnerType() {
        state();
        return (isLocalClass() || isAnonymous() || (isMemberType() && !isStatic())) && !inStaticContext();
    }

    public boolean isInnerTypeOf(TypeDecl typeDecl) {
        state();
        if (typeDecl != this) {
            return isInnerType() && enclosingType().isInnerTypeOf(typeDecl);
        }
        return true;
    }

    public TypeDecl withinBodyThatSubclasses(TypeDecl type) {
        state();
        if (instanceOf(type)) {
            return this;
        }
        if (!isTopLevelType()) {
            return enclosingType().withinBodyThatSubclasses(type);
        }
        return null;
    }

    public boolean encloses(TypeDecl type) {
        state();
        return type.enclosedBy(this);
    }

    public boolean enclosedBy(TypeDecl type) {
        state();
        if (this == type) {
            return true;
        }
        if (isTopLevelType()) {
            return false;
        }
        return enclosingType().enclosedBy(type);
    }

    public TypeDecl hostType() {
        state();
        return this;
    }

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

    private boolean isCircular_compute() {
        return false;
    }

    public boolean isValidAnnotationMethodReturnType() {
        state();
        return false;
    }

    public Annotation annotation(TypeDecl typeDecl) {
        state();
        return getModifiers().annotation(typeDecl);
    }

    public boolean hasAnnotationSuppressWarnings(String s) {
        state();
        return getModifiers().hasAnnotationSuppressWarnings(s);
    }

    public boolean isDeprecated() {
        state();
        return getModifiers().hasDeprecatedAnnotation();
    }

    public boolean commensurateWith(ElementValue value) {
        state();
        return value.commensurateWithTypeDecl(this);
    }

    public boolean isAnnotationDecl() {
        state();
        return false;
    }

    public boolean boxingConversionTo(TypeDecl typeDecl) {
        state();
        return false;
    }

    public TypeDecl boxed() {
        if (this.boxed_computed) {
            return this.boxed_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.boxed_value = boxed_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.boxed_computed = true;
        }
        return this.boxed_value;
    }

    private TypeDecl boxed_compute() {
        return unknownType();
    }

    public boolean unboxingConversionTo(TypeDecl typeDecl) {
        state();
        return false;
    }

    public TypeDecl unboxed() {
        if (this.unboxed_computed) {
            return this.unboxed_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.unboxed_value = unboxed_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.unboxed_computed = true;
        }
        return this.unboxed_value;
    }

    private TypeDecl unboxed_compute() {
        return unknownType();
    }

    public boolean isIterable() {
        if (this.isIterable_computed) {
            return this.isIterable_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isIterable_value = isIterable_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isIterable_computed = true;
        }
        return this.isIterable_value;
    }

    private boolean isIterable_compute() {
        return instanceOf(lookupType("java.lang", "Iterable"));
    }

    public boolean isEnumDecl() {
        state();
        return false;
    }

    public boolean isUnboxedPrimitive() {
        state();
        return (this instanceof PrimitiveType) && isPrimitive();
    }

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
        return false;
    }

    public boolean isGenericType() {
        state();
        return false;
    }

    public boolean isParameterizedType() {
        state();
        return false;
    }

    public boolean isRawType() {
        state();
        return isNestedType() && enclosingType().isRawType();
    }

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
        if (isAnonymous() || isLocalClass()) {
            return this;
        }
        if (!isNestedType()) {
            return this;
        }
        return extractSingleType(enclosingType().erasure().memberTypes(name()));
    }

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
        return new HashSet();
    }

    public boolean sameSignature(Access a) {
        state();
        return ((a instanceof ParTypeAccess) || (a instanceof AbstractWildcard) || this != a.type()) ? false : true;
    }

    @Override // soot.JastAddJ.ASTNode
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
        return isNestedType() && enclosingType().usesTypeVariable();
    }

    public TypeDecl original() {
        state();
        return this;
    }

    public TypeDecl asWildcardExtends() {
        state();
        return lookupWildcardExtends(this);
    }

    public TypeDecl asWildcardSuper() {
        state();
        return lookupWildcardSuper(this);
    }

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
        return this;
    }

    public boolean isTypeVariable() {
        state();
        return false;
    }

    public boolean supertypeGenericClassDecl(GenericClassDecl type) {
        state();
        return supertypeClassDecl(type);
    }

    public boolean supertypeGenericInterfaceDecl(GenericInterfaceDecl type) {
        state();
        return this == type || supertypeInterfaceDecl(type);
    }

    public boolean supertypeRawClassDecl(RawClassDecl type) {
        state();
        return supertypeParClassDecl(type);
    }

    public boolean supertypeRawInterfaceDecl(RawInterfaceDecl type) {
        state();
        return supertypeParInterfaceDecl(type);
    }

    public boolean supertypeWildcard(WildcardType type) {
        state();
        return false;
    }

    public boolean supertypeWildcardExtends(WildcardExtendsType type) {
        state();
        return false;
    }

    public boolean supertypeWildcardSuper(WildcardSuperType type) {
        state();
        return false;
    }

    public boolean isWildcard() {
        state();
        return false;
    }

    public boolean supertypeParClassDecl(ParClassDecl type) {
        state();
        return supertypeClassDecl(type);
    }

    public boolean supertypeParInterfaceDecl(ParInterfaceDecl type) {
        state();
        return supertypeInterfaceDecl(type);
    }

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
        if (type instanceof WildcardExtendsType) {
            return subtype(((WildcardExtendsType) type).extendsType());
        }
        if (type instanceof WildcardSuperType) {
            return ((WildcardSuperType) type).superType().subtype(this);
        }
        if (type instanceof TypeVariable) {
            return subtype(type);
        }
        return sameStructure(type);
    }

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
        return t == this;
    }

    public boolean supertypeTypeVariable(TypeVariable type) {
        state();
        if (type == this) {
            return true;
        }
        for (int i = 0; i < type.getNumTypeBound(); i++) {
            if (type.getTypeBound(i).type().subtype(this)) {
                return true;
            }
        }
        return false;
    }

    public boolean supertypeLUBType(LUBType type) {
        state();
        for (int i = 0; i < type.getNumTypeBound(); i++) {
            if (!type.getTypeBound(i).type().subtype(this)) {
                return false;
            }
        }
        return true;
    }

    public boolean supertypeGLBType(GLBType type) {
        state();
        for (int i = 0; i < type.getNumTypeBound(); i++) {
            if (type.getTypeBound(i).type().subtype(this)) {
                return true;
            }
        }
        return false;
    }

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
        return type == this;
    }

    public boolean supertypeClassDecl(ClassDecl type) {
        state();
        return type == this;
    }

    public boolean supertypeInterfaceDecl(InterfaceDecl type) {
        state();
        return type == this;
    }

    public boolean supertypeArrayDecl(ArrayDecl type) {
        state();
        return this == type;
    }

    public boolean supertypePrimitiveType(PrimitiveType type) {
        state();
        return type == this;
    }

    public boolean supertypeNullType(NullType type) {
        state();
        return false;
    }

    public boolean supertypeVoidType(VoidType type) {
        state();
        return false;
    }

    public boolean supertypeClassDeclSubstituted(ClassDeclSubstituted type) {
        state();
        return type.original() == this || supertypeClassDecl(type);
    }

    public boolean supertypeInterfaceDeclSubstituted(InterfaceDeclSubstituted type) {
        state();
        return type.original() == this || supertypeInterfaceDecl(type);
    }

    public boolean supertypeGenericClassDeclSubstituted(GenericClassDeclSubstituted type) {
        state();
        return type.original() == this || supertypeGenericClassDecl(type);
    }

    public boolean supertypeGenericInterfaceDeclSubstituted(GenericInterfaceDeclSubstituted type) {
        state();
        return type.original() == this || supertypeGenericInterfaceDecl(type);
    }

    public TypeDecl stringPromotion() {
        state();
        return this;
    }

    public MethodDecl methodWithArgs(String name, TypeDecl[] args) {
        state();
        for (MethodDecl m : memberMethods(name)) {
            if (m.getNumParameter() == args.length) {
                for (int i = 0; i < args.length; i++) {
                    if (m.getParameter(i).type() == args[i]) {
                        return m;
                    }
                }
                continue;
            }
        }
        return null;
    }

    public Collection enclosingVariables() {
        if (this.enclosingVariables_computed) {
            return this.enclosingVariables_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.enclosingVariables_value = enclosingVariables_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.enclosingVariables_computed = true;
        }
        return this.enclosingVariables_value;
    }

    private Collection enclosingVariables_compute() {
        HashSet set = new HashSet();
        TypeDecl typeDecl = this;
        while (true) {
            TypeDecl e = typeDecl;
            if (e == null) {
                break;
            }
            if (e.isLocalClass() || e.isAnonymous()) {
                collectEnclosingVariables(set, e.enclosingType());
            }
            typeDecl = e.enclosingType();
        }
        if (isClassDecl()) {
            ClassDecl classDecl = (ClassDecl) this;
            if (classDecl.isNestedType() && classDecl.hasSuperclass()) {
                set.addAll(classDecl.superclass().enclosingVariables());
            }
        }
        return set;
    }

    public boolean isAnonymousInNonStaticContext() {
        state();
        if (!isAnonymous() || ((ClassInstanceExpr) getParent().getParent()).unqualifiedScope().inStaticContext()) {
            return false;
        }
        return !inExplicitConstructorInvocation() || enclosingBodyDecl().hostType().isInnerType();
    }

    public boolean needsEnclosing() {
        state();
        if (isAnonymous()) {
            return isAnonymousInNonStaticContext();
        }
        if (isLocalClass()) {
            return !inStaticContext();
        } else if (isInnerType()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean needsSuperEnclosing() {
        state();
        if (!isAnonymous()) {
            return false;
        }
        TypeDecl superClass = ((ClassDecl) this).superclass();
        if (superClass.isLocalClass()) {
            return !superClass.inStaticContext();
        } else if (superClass.isInnerType()) {
            return true;
        } else {
            if (!needsEnclosing() || enclosing() == superEnclosing()) {
                return false;
            }
            return false;
        }
    }

    public TypeDecl enclosing() {
        state();
        if (!needsEnclosing()) {
            return null;
        }
        TypeDecl typeDecl = enclosingType();
        if (isAnonymous() && inExplicitConstructorInvocation()) {
            typeDecl = typeDecl.enclosingType();
        }
        return typeDecl;
    }

    public TypeDecl superEnclosing() {
        state();
        return null;
    }

    public int uniqueIndex() {
        if (this.uniqueIndex_computed) {
            return this.uniqueIndex_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.uniqueIndex_value = uniqueIndex_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.uniqueIndex_computed = true;
        }
        return this.uniqueIndex_value;
    }

    private int uniqueIndex_compute() {
        TypeDecl typeDecl = topLevelType();
        int i = typeDecl.uniqueIndexCounter;
        typeDecl.uniqueIndexCounter = i + 1;
        return i;
    }

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
        throw new Error("Jvm name only supported for reference types and not " + getClass().getName());
    }

    public String primitiveClassName() {
        state();
        throw new Error("primitiveClassName not supported for " + name() + " of type " + getClass().getName());
    }

    public String referenceClassFieldName() {
        state();
        throw new Error("referenceClassFieldName not supported for " + name() + " of type " + getClass().getName());
    }

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
        if (erasure() != this) {
            return erasure().getSootClassDecl();
        }
        if (compilationUnit().fromSource()) {
            return sootClass();
        }
        if (options().verbose()) {
            System.out.println("Loading .class file " + jvmName());
        }
        return SootResolver.v().makeClassRef(jvmName());
    }

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
        return RefType.v(erasure().jvmName());
    }

    public RefType sootRef() {
        state();
        return (RefType) getSootType();
    }

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
        return erasure() != this ? erasure().sootClass() : refined_EmitJimple_TypeDecl_sootClass();
    }

    public String sourceNameWithoutPath() {
        state();
        String s = sourceFile();
        return s != null ? s.substring(s.lastIndexOf(File.separatorChar) + 1) : "Unknown";
    }

    public int sootTypeModifiers() {
        state();
        int result = 0;
        if (isNestedType()) {
            result = 0 | 1;
        } else {
            if (isPublic()) {
                result = 0 | 1;
            }
            if (isProtected()) {
                result |= 4;
            }
            if (isPrivate()) {
                result |= 2;
            }
        }
        if (isFinal()) {
            result |= 16;
        }
        if (isStatic()) {
            result |= 8;
        }
        if (isAbstract()) {
            result |= 1024;
        }
        return result;
    }

    public boolean needsClinit() {
        if (this.needsClinit_computed) {
            return this.needsClinit_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.needsClinit_value = needsClinit_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.needsClinit_computed = true;
        }
        return this.needsClinit_value;
    }

    private boolean needsClinit_compute() {
        for (int i = 0; i < getNumBodyDecl(); i++) {
            BodyDecl b = getBodyDecl(i);
            if (b instanceof FieldDeclaration) {
                FieldDeclaration f = (FieldDeclaration) b;
                if (f.isStatic() && f.hasInit() && f.generate()) {
                    return true;
                }
            } else if ((b instanceof StaticInitializer) && b.generate()) {
                return true;
            }
        }
        return false;
    }

    public Collection innerClassesAttributeEntries() {
        if (this.innerClassesAttributeEntries_computed) {
            return this.innerClassesAttributeEntries_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.innerClassesAttributeEntries_value = innerClassesAttributeEntries_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.innerClassesAttributeEntries_computed = true;
        }
        return this.innerClassesAttributeEntries_value;
    }

    private Collection innerClassesAttributeEntries_compute() {
        HashSet list = new HashSet();
        if (isNestedType()) {
            list.add(this);
        }
        for (Object obj : nestedTypes()) {
            list.add(obj);
        }
        for (Object obj2 : usedNestedTypes()) {
            list.add(obj2);
        }
        return list;
    }

    public SootField getSootField(String name, TypeDecl type) {
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(name);
        arrayList.add(type);
        if (this.getSootField_String_TypeDecl_values == null) {
            this.getSootField_String_TypeDecl_values = new HashMap(4);
        }
        if (this.getSootField_String_TypeDecl_values.containsKey(arrayList)) {
            return (SootField) this.getSootField_String_TypeDecl_values.get(arrayList);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SootField getSootField_String_TypeDecl_value = getSootField_compute(name, type);
        if (isFinal && num == state().boundariesCrossed) {
            this.getSootField_String_TypeDecl_values.put(arrayList, getSootField_String_TypeDecl_value);
        }
        return getSootField_String_TypeDecl_value;
    }

    private SootField getSootField_compute(String name, TypeDecl type) {
        SootField f = Scene.v().makeSootField(name, type.getSootType(), 0);
        getSootClassDecl().addField(f);
        return f;
    }

    public int variableSize() {
        state();
        return 1;
    }

    public String typeDescriptor() {
        state();
        return jvmName();
    }

    public MethodDecl createEnumMethod(TypeDecl enumDecl) {
        if (this.createEnumMethod_TypeDecl_values == null) {
            this.createEnumMethod_TypeDecl_values = new HashMap(4);
        }
        if (this.createEnumMethod_TypeDecl_values.containsKey(enumDecl)) {
            return (MethodDecl) this.createEnumMethod_TypeDecl_values.get(enumDecl);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        MethodDecl createEnumMethod_TypeDecl_value = createEnumMethod_compute(enumDecl);
        if (isFinal && num == state().boundariesCrossed) {
            this.createEnumMethod_TypeDecl_values.put(enumDecl, createEnumMethod_TypeDecl_value);
        }
        return createEnumMethod_TypeDecl_value;
    }

    private MethodDecl createEnumMethod_compute(TypeDecl enumDecl) {
        MethodDecl m = new MethodDecl(new Modifiers(new List().add(new Modifier(Jimple.STATIC)).add(new Modifier(Jimple.FINAL)).add(new Modifier(Jimple.PRIVATE))), typeInt().arrayType().createQualifiedAccess(), "$SwitchMap$" + enumDecl.fullName().replace('.', '$'), new List(), new List(), new Opt(new Block(new List().add(new IfStmt(new EQExpr(createEnumArray(enumDecl).createBoundFieldAccess(), new NullLiteral(Jimple.NULL)), AssignExpr.asStmt(createEnumArray(enumDecl).createBoundFieldAccess(), new ArrayCreationExpr(new ArrayTypeWithSizeAccess(typeInt().createQualifiedAccess(), enumDecl.createQualifiedAccess().qualifiesAccess(new MethodAccess("values", new List())).qualifiesAccess(new VarAccess(XMLConstants.LENGTH_ATTRIBUTE))), new Opt())), new Opt())).add(new ReturnStmt(createEnumArray(enumDecl).createBoundFieldAccess())))));
        getBodyDeclList().insertChild(m, 1);
        return (MethodDecl) getBodyDeclList().getChild(1);
    }

    public int createEnumIndex(EnumConstant e) {
        if (this.createEnumIndex_EnumConstant_values == null) {
            this.createEnumIndex_EnumConstant_values = new HashMap(4);
        }
        if (this.createEnumIndex_EnumConstant_values.containsKey(e)) {
            return ((Integer) this.createEnumIndex_EnumConstant_values.get(e)).intValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        int createEnumIndex_EnumConstant_value = createEnumIndex_compute(e);
        if (isFinal && num == state().boundariesCrossed) {
            this.createEnumIndex_EnumConstant_values.put(e, Integer.valueOf(createEnumIndex_EnumConstant_value));
        }
        return createEnumIndex_EnumConstant_value;
    }

    private int createEnumIndex_compute(EnumConstant e) {
        if (this.createEnumIndexMap == null) {
            this.createEnumIndexMap = new HashMap();
        }
        if (!this.createEnumIndexMap.containsKey(e.hostType())) {
            this.createEnumIndexMap.put(e.hostType(), new Integer(0));
        }
        Integer i = new Integer(((Integer) this.createEnumIndexMap.get(e.hostType())).intValue() + 1);
        this.createEnumIndexMap.put(e.hostType(), i);
        MethodDecl m = createEnumMethod(e.hostType());
        List list = m.getBlock().getStmtList();
        list.insertChild(new TryStmt(new Block(new List().add(AssignExpr.asStmt(createEnumArray(e.hostType()).createBoundFieldAccess().qualifiesAccess(new ArrayAccess(e.createBoundFieldAccess().qualifiesAccess(new MethodAccess("ordinal", new List())))), new IntegerLiteral(i.toString())))), new List().add(new BasicCatch(new ParameterDeclaration(lookupType("java.lang", "NoSuchFieldError").createQualifiedAccess(), "e"), new Block(new List()))), new Opt()), list.getNumChild() - 1);
        return i.intValue();
    }

    public FieldDeclaration createEnumArray(TypeDecl enumDecl) {
        if (this.createEnumArray_TypeDecl_values == null) {
            this.createEnumArray_TypeDecl_values = new HashMap(4);
        }
        if (this.createEnumArray_TypeDecl_values.containsKey(enumDecl)) {
            return (FieldDeclaration) this.createEnumArray_TypeDecl_values.get(enumDecl);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        FieldDeclaration createEnumArray_TypeDecl_value = createEnumArray_compute(enumDecl);
        if (isFinal && num == state().boundariesCrossed) {
            this.createEnumArray_TypeDecl_values.put(enumDecl, createEnumArray_TypeDecl_value);
        }
        return createEnumArray_TypeDecl_value;
    }

    private FieldDeclaration createEnumArray_compute(TypeDecl enumDecl) {
        FieldDeclaration f = new FieldDeclaration(new Modifiers(new List().add(new Modifier(Jimple.STATIC)).add(new Modifier(Jimple.FINAL)).add(new Modifier(Jimple.PRIVATE))), typeInt().arrayType().createQualifiedAccess(), "$SwitchMap$" + enumDecl.fullName().replace('.', '$'), new Opt());
        getBodyDeclList().insertChild(f, 0);
        return (FieldDeclaration) getBodyDeclList().getChild(0);
    }

    public SimpleSet bridgeCandidates(String signature) {
        state();
        return SimpleSet.emptySet;
    }

    public boolean hasAnnotationSafeVarargs() {
        state();
        return getModifiers().hasAnnotationSafeVarargs();
    }

    public boolean isReifiable() {
        state();
        return true;
    }

    public boolean isUncheckedConversionTo(TypeDecl dest) {
        state();
        return !dest.isRawType() && isRawType();
    }

    public TypeDecl componentType() {
        if (this.componentType_computed) {
            return this.componentType_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.componentType_value = getParent().Define_TypeDecl_componentType(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.componentType_computed = true;
        }
        return this.componentType_value;
    }

    public TypeDecl typeCloneable() {
        state();
        TypeDecl typeCloneable_value = getParent().Define_TypeDecl_typeCloneable(this, null);
        return typeCloneable_value;
    }

    public TypeDecl typeSerializable() {
        state();
        TypeDecl typeSerializable_value = getParent().Define_TypeDecl_typeSerializable(this, null);
        return typeSerializable_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public CompilationUnit compilationUnit() {
        state();
        CompilationUnit compilationUnit_value = getParent().Define_CompilationUnit_compilationUnit(this, null);
        return compilationUnit_value;
    }

    public boolean isDAbefore(Variable v) {
        if (this.isDAbefore_Variable_values == null) {
            this.isDAbefore_Variable_values = new HashMap(4);
        }
        if (this.isDAbefore_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDAbefore_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDAbefore_Variable_value = getParent().Define_boolean_isDAbefore(this, null, v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDAbefore_Variable_values.put(v, Boolean.valueOf(isDAbefore_Variable_value));
        }
        return isDAbefore_Variable_value;
    }

    public boolean isDUbefore(Variable v) {
        if (this.isDUbefore_Variable_values == null) {
            this.isDUbefore_Variable_values = new HashMap(4);
        }
        if (this.isDUbefore_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDUbefore_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDUbefore_Variable_value = getParent().Define_boolean_isDUbefore(this, null, v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDUbefore_Variable_values.put(v, Boolean.valueOf(isDUbefore_Variable_value));
        }
        return isDUbefore_Variable_value;
    }

    public TypeDecl typeException() {
        if (this.typeException_computed) {
            return this.typeException_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeException_value = getParent().Define_TypeDecl_typeException(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.typeException_computed = true;
        }
        return this.typeException_value;
    }

    public TypeDecl typeRuntimeException() {
        if (this.typeRuntimeException_computed) {
            return this.typeRuntimeException_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeRuntimeException_value = getParent().Define_TypeDecl_typeRuntimeException(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.typeRuntimeException_computed = true;
        }
        return this.typeRuntimeException_value;
    }

    public TypeDecl typeError() {
        if (this.typeError_computed) {
            return this.typeError_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeError_value = getParent().Define_TypeDecl_typeError(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.typeError_computed = true;
        }
        return this.typeError_value;
    }

    public Collection lookupMethod(String name) {
        if (this.lookupMethod_String_values == null) {
            this.lookupMethod_String_values = new HashMap(4);
        }
        if (this.lookupMethod_String_values.containsKey(name)) {
            return (Collection) this.lookupMethod_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        Collection lookupMethod_String_value = getParent().Define_Collection_lookupMethod(this, null, name);
        if (isFinal && num == state().boundariesCrossed) {
            this.lookupMethod_String_values.put(name, lookupMethod_String_value);
        }
        return lookupMethod_String_value;
    }

    public TypeDecl typeInt() {
        state();
        TypeDecl typeInt_value = getParent().Define_TypeDecl_typeInt(this, null);
        return typeInt_value;
    }

    public TypeDecl typeObject() {
        if (this.typeObject_computed) {
            return this.typeObject_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeObject_value = getParent().Define_TypeDecl_typeObject(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.typeObject_computed = true;
        }
        return this.typeObject_value;
    }

    public TypeDecl lookupType(String packageName, String typeName) {
        state();
        TypeDecl lookupType_String_String_value = getParent().Define_TypeDecl_lookupType(this, null, packageName, typeName);
        return lookupType_String_String_value;
    }

    public SimpleSet lookupType(String name) {
        if (this.lookupType_String_values == null) {
            this.lookupType_String_values = new HashMap(4);
        }
        if (this.lookupType_String_values.containsKey(name)) {
            return (SimpleSet) this.lookupType_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet lookupType_String_value = getParent().Define_SimpleSet_lookupType(this, null, name);
        if (isFinal && num == state().boundariesCrossed) {
            this.lookupType_String_values.put(name, lookupType_String_value);
        }
        return lookupType_String_value;
    }

    @Override // soot.JastAddJ.VariableScope
    public SimpleSet lookupVariable(String name) {
        if (this.lookupVariable_String_values == null) {
            this.lookupVariable_String_values = new HashMap(4);
        }
        if (this.lookupVariable_String_values.containsKey(name)) {
            return (SimpleSet) this.lookupVariable_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet lookupVariable_String_value = getParent().Define_SimpleSet_lookupVariable(this, null, name);
        if (isFinal && num == state().boundariesCrossed) {
            this.lookupVariable_String_values.put(name, lookupVariable_String_value);
        }
        return lookupVariable_String_value;
    }

    public boolean hasPackage(String packageName) {
        state();
        boolean hasPackage_String_value = getParent().Define_boolean_hasPackage(this, null, packageName);
        return hasPackage_String_value;
    }

    public ASTNode enclosingBlock() {
        state();
        ASTNode enclosingBlock_value = getParent().Define_ASTNode_enclosingBlock(this, null);
        return enclosingBlock_value;
    }

    public String packageName() {
        if (this.packageName_computed) {
            return this.packageName_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.packageName_value = getParent().Define_String_packageName(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.packageName_computed = true;
        }
        return this.packageName_value;
    }

    public boolean isAnonymous() {
        if (this.isAnonymous_computed) {
            return this.isAnonymous_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isAnonymous_value = getParent().Define_boolean_isAnonymous(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.isAnonymous_computed = true;
        }
        return this.isAnonymous_value;
    }

    public TypeDecl enclosingType() {
        state();
        TypeDecl enclosingType_value = getParent().Define_TypeDecl_enclosingType(this, null);
        return enclosingType_value;
    }

    public BodyDecl enclosingBodyDecl() {
        state();
        BodyDecl enclosingBodyDecl_value = getParent().Define_BodyDecl_enclosingBodyDecl(this, null);
        return enclosingBodyDecl_value;
    }

    public boolean isNestedType() {
        state();
        boolean isNestedType_value = getParent().Define_boolean_isNestedType(this, null);
        return isNestedType_value;
    }

    public boolean isMemberType() {
        state();
        boolean isMemberType_value = getParent().Define_boolean_isMemberType(this, null);
        return isMemberType_value;
    }

    public boolean isLocalClass() {
        state();
        boolean isLocalClass_value = getParent().Define_boolean_isLocalClass(this, null);
        return isLocalClass_value;
    }

    public String hostPackage() {
        state();
        String hostPackage_value = getParent().Define_String_hostPackage(this, null);
        return hostPackage_value;
    }

    public TypeDecl unknownType() {
        if (this.unknownType_computed) {
            return this.unknownType_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.unknownType_value = getParent().Define_TypeDecl_unknownType(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.unknownType_computed = true;
        }
        return this.unknownType_value;
    }

    public TypeDecl typeVoid() {
        state();
        TypeDecl typeVoid_value = getParent().Define_TypeDecl_typeVoid(this, null);
        return typeVoid_value;
    }

    public TypeDecl enclosingInstance() {
        state();
        TypeDecl enclosingInstance_value = getParent().Define_TypeDecl_enclosingInstance(this, null);
        return enclosingInstance_value;
    }

    public boolean inExplicitConstructorInvocation() {
        if (this.inExplicitConstructorInvocation_computed) {
            return this.inExplicitConstructorInvocation_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.inExplicitConstructorInvocation_value = getParent().Define_boolean_inExplicitConstructorInvocation(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.inExplicitConstructorInvocation_computed = true;
        }
        return this.inExplicitConstructorInvocation_value;
    }

    public boolean inStaticContext() {
        if (this.inStaticContext_computed) {
            return this.inStaticContext_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.inStaticContext_value = getParent().Define_boolean_inStaticContext(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.inStaticContext_computed = true;
        }
        return this.inStaticContext_value;
    }

    public boolean withinSuppressWarnings(String s) {
        state();
        boolean withinSuppressWarnings_String_value = getParent().Define_boolean_withinSuppressWarnings(this, null, s);
        return withinSuppressWarnings_String_value;
    }

    public boolean withinDeprecatedAnnotation() {
        state();
        boolean withinDeprecatedAnnotation_value = getParent().Define_boolean_withinDeprecatedAnnotation(this, null);
        return withinDeprecatedAnnotation_value;
    }

    public TypeDecl typeWildcard() {
        state();
        TypeDecl typeWildcard_value = getParent().Define_TypeDecl_typeWildcard(this, null);
        return typeWildcard_value;
    }

    public TypeDecl lookupWildcardExtends(TypeDecl typeDecl) {
        state();
        TypeDecl lookupWildcardExtends_TypeDecl_value = getParent().Define_TypeDecl_lookupWildcardExtends(this, null, typeDecl);
        return lookupWildcardExtends_TypeDecl_value;
    }

    public TypeDecl lookupWildcardSuper(TypeDecl typeDecl) {
        state();
        TypeDecl lookupWildcardSuper_TypeDecl_value = getParent().Define_TypeDecl_lookupWildcardSuper(this, null, typeDecl);
        return lookupWildcardSuper_TypeDecl_value;
    }

    public LUBType lookupLUBType(Collection bounds) {
        state();
        LUBType lookupLUBType_Collection_value = getParent().Define_LUBType_lookupLUBType(this, null, bounds);
        return lookupLUBType_Collection_value;
    }

    public GLBType lookupGLBType(ArrayList bounds) {
        state();
        GLBType lookupGLBType_ArrayList_value = getParent().Define_GLBType_lookupGLBType(this, null, bounds);
        return lookupGLBType_ArrayList_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_componentType(ASTNode caller, ASTNode child) {
        if (caller == this.arrayType_value) {
            return this;
        }
        return getParent().Define_TypeDecl_componentType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_isDest(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return true;
        }
        return getParent().Define_boolean_isSource(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getBodyDeclListNoTransform()) {
            int childIndex = caller.getIndexOfChild(child);
            BodyDecl b = getBodyDecl(childIndex);
            if (!v.isInstanceVariable() && !v.isClassVariable()) {
                if (v.hostType() != this) {
                    return isDAbefore(v);
                }
                return false;
            } else if (((b instanceof FieldDeclaration) && !((FieldDeclaration) b).isStatic() && v.isClassVariable()) || (b instanceof MethodDecl)) {
                return true;
            } else {
                if ((b instanceof MemberTypeDecl) && v.isBlank() && v.isFinal() && v.hostType() == this) {
                    return true;
                }
                if (v.isClassVariable() || v.isInstanceVariable()) {
                    if (v.isFinal() && v.hostType() != this && instanceOf(v.hostType())) {
                        return true;
                    }
                    int index = childIndex - 1;
                    if (b instanceof ConstructorDecl) {
                        index = getNumBodyDecl() - 1;
                    }
                    for (int i = index; i >= 0; i--) {
                        BodyDecl b2 = getBodyDecl(i);
                        if (b2 instanceof FieldDeclaration) {
                            FieldDeclaration f = (FieldDeclaration) b2;
                            if ((v.isClassVariable() && f.isStatic()) || (v.isInstanceVariable() && !f.isStatic())) {
                                boolean c = f.isDAafter(v);
                                return c;
                            }
                        } else if ((b2 instanceof StaticInitializer) && v.isClassVariable()) {
                            StaticInitializer si = (StaticInitializer) b2;
                            return si.isDAafter(v);
                        } else if ((b2 instanceof InstanceInitializer) && v.isInstanceVariable()) {
                            InstanceInitializer ii = (InstanceInitializer) b2;
                            return ii.isDAafter(v);
                        }
                    }
                }
                return isDAbefore(v);
            }
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getBodyDeclListNoTransform()) {
            int childIndex = caller.getIndexOfChild(child);
            BodyDecl b = getBodyDecl(childIndex);
            if ((b instanceof MethodDecl) || (b instanceof MemberTypeDecl)) {
                return false;
            }
            if (v.isClassVariable() || v.isInstanceVariable()) {
                int index = childIndex - 1;
                if (b instanceof ConstructorDecl) {
                    index = getNumBodyDecl() - 1;
                }
                for (int i = index; i >= 0; i--) {
                    BodyDecl b2 = getBodyDecl(i);
                    if (b2 instanceof FieldDeclaration) {
                        FieldDeclaration f = (FieldDeclaration) b2;
                        if (f == v) {
                            return !f.hasInit();
                        } else if ((v.isClassVariable() && f.isStatic()) || (v.isInstanceVariable() && !f.isStatic())) {
                            return f.isDUafter(v);
                        }
                    } else if ((b2 instanceof StaticInitializer) && v.isClassVariable()) {
                        StaticInitializer si = (StaticInitializer) b2;
                        return si.isDUafter(v);
                    } else if ((b2 instanceof InstanceInitializer) && v.isInstanceVariable()) {
                        InstanceInitializer ii = (InstanceInitializer) b2;
                        return ii.isDUafter(v);
                    }
                }
            }
            return isDUbefore(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public Collection Define_Collection_lookupConstructor(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return constructors();
        }
        return getParent().Define_Collection_lookupConstructor(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public Collection Define_Collection_lookupSuperConstructor(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return lookupSuperConstructor();
        }
        return getParent().Define_Collection_lookupSuperConstructor(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return unqualifiedLookupMethod(name);
        }
        return getParent().Define_Collection_lookupMethod(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            SimpleSet c = memberTypes(name);
            if (!c.isEmpty()) {
                return c;
            }
            if (name().equals(name)) {
                return SimpleSet.emptySet.add(this);
            }
            SimpleSet<TypeDecl> c2 = lookupType(name);
            if (isClassDecl() && isStatic() && !isTopLevelType()) {
                SimpleSet newSet = SimpleSet.emptySet;
                for (TypeDecl d : c2) {
                    newSet = newSet.add(d);
                }
                c2 = newSet;
            }
            return c2;
        }
        return getParent().Define_SimpleSet_lookupType(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            SimpleSet list = memberFields(name);
            if (!list.isEmpty()) {
                return list;
            }
            SimpleSet list2 = lookupVariable(name);
            if (inStaticContext() || isStatic()) {
                list2 = removeInstanceVariables(list2);
            }
            return list2;
        }
        return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBePublic(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        } else if (caller == getModifiersNoTransform()) {
            return true;
        } else {
            return getParent().Define_boolean_mayBePublic(this, caller);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeProtected(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        } else if (caller == getModifiersNoTransform()) {
            return true;
        } else {
            return getParent().Define_boolean_mayBeProtected(this, caller);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBePrivate(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        } else if (caller == getModifiersNoTransform()) {
            return true;
        } else {
            return getParent().Define_boolean_mayBePrivate(this, caller);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeAbstract(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        } else if (caller == getModifiersNoTransform()) {
            return true;
        } else {
            return getParent().Define_boolean_mayBeAbstract(this, caller);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeStatic(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        } else if (caller == getModifiersNoTransform()) {
            return true;
        } else {
            return getParent().Define_boolean_mayBeStatic(this, caller);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeStrictfp(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        } else if (caller == getModifiersNoTransform()) {
            return true;
        } else {
            return getParent().Define_boolean_mayBeStrictfp(this, caller);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_mayBeFinal(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeVolatile(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_mayBeVolatile(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeTransient(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_mayBeTransient(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeSynchronized(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_mayBeSynchronized(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeNative(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_mayBeNative(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return this;
        }
        return getParent().Define_VariableScope_outerScope(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_insideLoop(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_insideLoop(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_insideSwitch(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_insideSwitch(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.EXPRESSION_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isAnonymous(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_isAnonymous(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_enclosingType(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return this;
        }
        return getParent().Define_TypeDecl_enclosingType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isNestedType(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return true;
        }
        return getParent().Define_boolean_isNestedType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isLocalClass(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_isLocalClass(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return hostType();
        }
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return hostType();
        }
        getIndexOfChild(caller);
        return hostType();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_returnType(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return typeVoid();
        }
        return getParent().Define_TypeDecl_returnType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            int childIndex = caller.getIndexOfChild(child);
            if ((getBodyDecl(childIndex) instanceof MemberTypeDecl) && !((MemberTypeDecl) getBodyDecl(childIndex)).typeDecl().isInnerType()) {
                return null;
            }
            if (getBodyDecl(childIndex) instanceof ConstructorDecl) {
                return enclosingInstance();
            }
            return this;
        }
        return getParent().Define_TypeDecl_enclosingInstance(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public String Define_String_methodHost(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return typeName();
        }
        return getParent().Define_String_methodHost(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return isStatic() || inStaticContext();
        }
        return getParent().Define_boolean_inStaticContext(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return true;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
        if (caller == getModifiersNoTransform()) {
            return name.equals("TYPE");
        }
        return getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_withinSuppressWarnings(ASTNode caller, ASTNode child, String s) {
        if (caller == getBodyDeclListNoTransform()) {
            int i = caller.getIndexOfChild(child);
            return getBodyDecl(i).hasAnnotationSuppressWarnings(s) || hasAnnotationSuppressWarnings(s) || withinSuppressWarnings(s);
        }
        return getParent().Define_boolean_withinSuppressWarnings(this, caller, s);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_withinDeprecatedAnnotation(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            int i = caller.getIndexOfChild(child);
            return getBodyDecl(i).isDeprecated() || isDeprecated() || withinDeprecatedAnnotation();
        }
        return getParent().Define_boolean_withinDeprecatedAnnotation(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_enclosedByExceptionHandler(ASTNode caller, ASTNode child) {
        if (caller == getBodyDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_enclosedByExceptionHandler(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
