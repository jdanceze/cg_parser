package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.JastAddJ.Signatures;
import soot.MethodSource;
import soot.Scene;
import soot.SootClass;
import soot.coffi.CoffiMethodSource;
import soot.tagkit.SourceFileTag;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ClassDecl.class */
public class ClassDecl extends ReferenceType implements Cloneable {
    protected HashMap interfacesMethodsSignatureMap_value;
    protected HashMap methodsSignatureMap_value;
    protected Map ancestorMethods_String_values;
    protected Map memberTypes_String_values;
    protected HashMap memberFieldsMap_value;
    protected Map memberFields_String_values;
    protected Collection unimplementedMethods_value;
    protected boolean hasAbstract_value;
    protected Map castingConversionTo_TypeDecl_values;
    protected boolean isString_value;
    protected boolean isObject_value;
    protected Map instanceOf_TypeDecl_values;
    protected boolean isCircular_value;
    protected HashSet implementedInterfaces_value;
    protected Map subtype_TypeDecl_values;
    protected SootClass sootClass_value;
    protected boolean interfacesMethodsSignatureMap_computed = false;
    protected boolean methodsSignatureMap_computed = false;
    protected boolean memberFieldsMap_computed = false;
    protected boolean unimplementedMethods_computed = false;
    protected boolean hasAbstract_computed = false;
    protected boolean isString_computed = false;
    protected boolean isObject_computed = false;
    protected int isCircular_visited = -1;
    protected boolean isCircular_computed = false;
    protected boolean isCircular_initialized = false;
    protected boolean implementedInterfaces_computed = false;
    protected boolean sootClass_computed = false;

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.interfacesMethodsSignatureMap_computed = false;
        this.interfacesMethodsSignatureMap_value = null;
        this.methodsSignatureMap_computed = false;
        this.methodsSignatureMap_value = null;
        this.ancestorMethods_String_values = null;
        this.memberTypes_String_values = null;
        this.memberFieldsMap_computed = false;
        this.memberFieldsMap_value = null;
        this.memberFields_String_values = null;
        this.unimplementedMethods_computed = false;
        this.unimplementedMethods_value = null;
        this.hasAbstract_computed = false;
        this.castingConversionTo_TypeDecl_values = null;
        this.isString_computed = false;
        this.isObject_computed = false;
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
    public ClassDecl clone() throws CloneNotSupportedException {
        ClassDecl node = (ClassDecl) super.clone();
        node.interfacesMethodsSignatureMap_computed = false;
        node.interfacesMethodsSignatureMap_value = null;
        node.methodsSignatureMap_computed = false;
        node.methodsSignatureMap_value = null;
        node.ancestorMethods_String_values = null;
        node.memberTypes_String_values = null;
        node.memberFieldsMap_computed = false;
        node.memberFieldsMap_value = null;
        node.memberFields_String_values = null;
        node.unimplementedMethods_computed = false;
        node.unimplementedMethods_value = null;
        node.hasAbstract_computed = false;
        node.castingConversionTo_TypeDecl_values = null;
        node.isString_computed = false;
        node.isObject_computed = false;
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
            ClassDecl node = clone();
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
        TypeDecl typeDecl = hasSuperclass() ? superclass() : null;
        if (typeDecl != null && !typeDecl.accessibleFromExtend(this)) {
            error("class " + fullName() + " may not extend non accessible type " + typeDecl.fullName());
        }
        if (hasSuperclass() && !superclass().accessibleFrom(this)) {
            error("a superclass must be accessible which " + superclass().name() + " is not");
        }
        for (int i = 0; i < getNumImplements(); i++) {
            TypeDecl decl = getImplements(i).type();
            if (!decl.isCircular() && !decl.accessibleFrom(this)) {
                error("class " + fullName() + " can not implement non accessible type " + decl.fullName());
            }
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void exceptionHandling() {
        constructors();
        super.exceptionHandling();
    }

    public Iterator interfacesMethodsIterator() {
        return new Iterator() { // from class: soot.JastAddJ.ClassDecl.1
            private Iterator outer;
            private Iterator inner = null;

            {
                this.outer = ClassDecl.this.interfacesMethodsSignatureMap().values().iterator();
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

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void checkModifiers() {
        super.checkModifiers();
        TypeDecl typeDecl = hasSuperclass() ? superclass() : null;
        if (typeDecl != null && typeDecl.isFinal()) {
            error("class " + fullName() + " may not extend final class " + typeDecl.fullName());
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        getModifiers().toString(s);
        s.append("class " + name());
        if (hasSuperClassAccess()) {
            s.append(" extends ");
            getSuperClassAccess().toString(s);
        }
        if (getNumImplements() > 0) {
            s.append(" implements ");
            getImplements(0).toString(s);
            for (int i = 1; i < getNumImplements(); i++) {
                s.append(", ");
                getImplements(i).toString(s);
            }
        }
        ppBodyDecls(s);
    }

    public boolean hasSuperclass() {
        return !isObject();
    }

    public ClassDecl superclass() {
        if (isObject()) {
            return null;
        }
        if (hasSuperClassAccess() && !isCircular() && getSuperClassAccess().type().isClassDecl()) {
            return (ClassDecl) getSuperClassAccess().type();
        }
        return (ClassDecl) typeObject();
    }

    public Iterator interfacesIterator() {
        return new Iterator() { // from class: soot.JastAddJ.ClassDecl.2
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
                if (ClassDecl.this.isObject() || ClassDecl.this.isCircular()) {
                    return;
                }
                while (this.index < ClassDecl.this.getNumImplements()) {
                    ClassDecl classDecl = ClassDecl.this;
                    int i = this.index;
                    this.index = i + 1;
                    TypeDecl typeDecl = classDecl.getImplements(i).type();
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
        if (hasSuperClassAccess() && !getSuperClassAccess().type().isClassDecl()) {
            error("class may only inherit a class and not " + getSuperClassAccess().type().typeName());
        }
        if (isObject() && hasSuperClassAccess()) {
            error("class Object may not have superclass");
        }
        if (isObject() && getNumImplements() != 0) {
            error("class Object may not implement interfaces");
        }
        if (isCircular()) {
            error("circular inheritance dependency in " + typeName());
        }
        HashSet set = new HashSet();
        for (int i = 0; i < getNumImplements(); i++) {
            TypeDecl decl = getImplements(i).type();
            if (!decl.isInterfaceDecl() && !decl.isUnknown()) {
                error("type " + fullName() + " tries to implement non interface type " + decl.fullName());
            }
            if (set.contains(decl)) {
                error("type " + decl.fullName() + " mentionened multiple times in implements clause");
            }
            set.add(decl);
        }
        Iterator iter = interfacesMethodsIterator();
        while (iter.hasNext()) {
            MethodDecl m = (MethodDecl) iter.next();
            if (localMethodsSignature(m.signature()).isEmpty()) {
                SimpleSet<MethodDecl> s = superclass().methodsSignature(m.signature());
                for (MethodDecl n : s) {
                    if (n.accessibleFrom(this)) {
                        interfaceMethodCompatibleWithInherited(m, n);
                    }
                }
                if (s.isEmpty()) {
                    for (MethodDecl n2 : interfacesMethodsSignature(m.signature())) {
                        if (!n2.mayOverrideReturn(m) && !m.mayOverrideReturn(n2)) {
                            error("Xthe return type of method " + m.signature() + " in " + m.hostType().typeName() + " does not match the return type of method " + n2.signature() + " in " + n2.hostType().typeName() + " and may thus not be overriden");
                        }
                    }
                }
            }
        }
    }

    private void interfaceMethodCompatibleWithInherited(MethodDecl m, MethodDecl n) {
        if (n.isStatic()) {
            error("Xa static method may not hide an instance method");
        }
        if (!n.isAbstract() && !n.isPublic()) {
            error("Xoverriding access modifier error for " + m.signature() + " in " + m.hostType().typeName() + " and " + n.hostType().typeName());
        }
        if (!n.mayOverrideReturn(m) && !m.mayOverrideReturn(m)) {
            error("Xthe return type of method " + m.signature() + " in " + m.hostType().typeName() + " does not match the return type of method " + n.signature() + " in " + n.hostType().typeName() + " and may thus not be overriden");
        }
        if (!n.isAbstract()) {
            for (int i = 0; i < n.getNumException(); i++) {
                Access e = n.getException(i);
                boolean found = false;
                for (int j = 0; !found && j < m.getNumException(); j++) {
                    if (e.type().instanceOf(m.getException(j).type())) {
                        found = true;
                    }
                }
                if (!found && e.type().isUncheckedException()) {
                    error("X" + n.signature() + " in " + n.hostType().typeName() + " may not throw more checked exceptions than overridden method " + m.signature() + " in " + m.hostType().typeName());
                }
            }
        }
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl makeGeneric(Signatures.ClassSignature s) {
        if (s.hasFormalTypeParameters()) {
            ASTNode node = getParent();
            int index = node.getIndexOfChild(this);
            node.setChild(new GenericClassDecl(getModifiersNoTransform(), getID(), s.hasSuperclassSignature() ? new Opt<>(s.superclassSignature()) : getSuperClassAccessOptNoTransform(), s.hasSuperinterfaceSignature() ? s.superinterfaceSignature() : getImplementsListNoTransform(), getBodyDeclListNoTransform(), s.typeParameters()), index);
            return (TypeDecl) node.getChildNoTransform(index);
        }
        if (s.hasSuperclassSignature()) {
            setSuperClassAccessOpt(new Opt<>(s.superclassSignature()));
        }
        if (s.hasSuperinterfaceSignature()) {
            setImplementsList(s.superinterfaceSignature());
        }
        return this;
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [soot.JastAddJ.Modifiers] */
    public ClassDecl substitutedClassDecl(Parameterization parTypeDecl) {
        ClassDecl c = new ClassDeclSubstituted((Modifiers) getModifiers().fullCopy2(), getID(), hasSuperClassAccess() ? new Opt(getSuperClassAccess().type().substitute(parTypeDecl)) : new Opt(), getImplementsList().substitute(parTypeDecl), this);
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
        if (hasSuperclass()) {
            sc.setSuperclass(superclass().getSootClassDecl());
        }
        Iterator iter = interfacesIterator();
        while (iter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl) iter.next();
            if (!sc.implementsInterface(typeDecl.getSootClassDecl().getName())) {
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

    public ClassDecl() {
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[4];
        setChild(new Opt(), 1);
        setChild(new List(), 2);
        setChild(new List(), 3);
    }

    public ClassDecl(Modifiers p0, String p1, Opt<Access> p2, List<Access> p3, List<BodyDecl> p4) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
        setChild(p4, 3);
    }

    public ClassDecl(Modifiers p0, Symbol p1, Opt<Access> p2, List<Access> p3, List<BodyDecl> p4) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
        setChild(p4, 3);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 4;
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

    public void setSuperClassAccessOpt(Opt<Access> opt) {
        setChild(opt, 1);
    }

    public boolean hasSuperClassAccess() {
        return getSuperClassAccessOpt().getNumChild() != 0;
    }

    public Access getSuperClassAccess() {
        return getSuperClassAccessOpt().getChild(0);
    }

    public void setSuperClassAccess(Access node) {
        getSuperClassAccessOpt().setChild(node, 0);
    }

    public Opt<Access> getSuperClassAccessOpt() {
        return (Opt) getChild(1);
    }

    public Opt<Access> getSuperClassAccessOptNoTransform() {
        return (Opt) getChildNoTransform(1);
    }

    public void setImplementsList(List<Access> list) {
        setChild(list, 2);
    }

    public int getNumImplements() {
        return getImplementsList().getNumChild();
    }

    public int getNumImplementsNoTransform() {
        return getImplementsListNoTransform().getNumChildNoTransform();
    }

    public Access getImplements(int i) {
        return getImplementsList().getChild(i);
    }

    public void addImplements(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getImplementsListNoTransform() : getImplementsList();
        list.addChild(node);
    }

    public void addImplementsNoTransform(Access node) {
        List<Access> list = getImplementsListNoTransform();
        list.addChild(node);
    }

    public void setImplements(Access node, int i) {
        List<Access> list = getImplementsList();
        list.setChild(node, i);
    }

    public List<Access> getImplementss() {
        return getImplementsList();
    }

    public List<Access> getImplementssNoTransform() {
        return getImplementsListNoTransform();
    }

    public List<Access> getImplementsList() {
        List<Access> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    public List<Access> getImplementsListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 3);
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
        List<BodyDecl> list = (List) getChild(3);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(3);
    }

    private boolean refined_TypeConversion_ClassDecl_castingConversionTo_TypeDecl(TypeDecl type) {
        if (type.isArrayDecl()) {
            return isObject();
        }
        if (type.isClassDecl()) {
            return this == type || instanceOf(type) || type.instanceOf(this);
        } else if (type.isInterfaceDecl()) {
            return !isFinal() || instanceOf(type);
        } else {
            return super.castingConversionTo(type);
        }
    }

    private boolean refined_Generics_ClassDecl_castingConversionTo_TypeDecl(TypeDecl type) {
        if (type instanceof TypeVariable) {
            TypeVariable t = (TypeVariable) type;
            if (t.getNumTypeBound() == 0) {
                return true;
            }
            for (int i = 0; i < t.getNumTypeBound(); i++) {
                if (castingConversionTo(t.getTypeBound(i).type())) {
                    return true;
                }
            }
            return false;
        } else if (type.isClassDecl() && (erasure() != this || type.erasure() != type)) {
            return erasure().castingConversionTo(type.erasure());
        } else {
            return refined_TypeConversion_ClassDecl_castingConversionTo_TypeDecl(type);
        }
    }

    private SootClass refined_EmitJimpleRefinements_ClassDecl_sootClass() {
        boolean needAddclass = false;
        SootClass sc = null;
        if (Scene.v().containsClass(jvmName())) {
            SootClass cl = Scene.v().getSootClass(jvmName());
            try {
                MethodSource source = cl.getMethodByName("<clinit>").getSource();
                if (source instanceof CoffiMethodSource) {
                    Scene.v().removeClass(cl);
                    needAddclass = true;
                }
            } catch (RuntimeException e) {
            }
            sc = cl;
        } else {
            needAddclass = true;
        }
        if (needAddclass) {
            if (options().verbose()) {
                System.out.println("Creating from source " + jvmName());
            }
            sc = new SootClass(jvmName());
            sc.setResolvingLevel(0);
            Scene.v().addClass(sc);
        }
        return sc;
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant cast(Constant c) {
        state();
        return Constant.create(c.stringValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant add(Constant c1, Constant c2) {
        state();
        return Constant.create(String.valueOf(c1.stringValue()) + c2.stringValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant questionColon(Constant cond, Constant c1, Constant c2) {
        state();
        return Constant.create(cond.booleanValue() ? c1.stringValue() : c2.stringValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean eqIsTrue(Expr left, Expr right) {
        state();
        return isString() && left.constant().stringValue().equals(right.constant().stringValue());
    }

    @Override // soot.JastAddJ.ASTNode
    public int lineNumber() {
        state();
        return getLine(this.IDstart);
    }

    @Override // soot.JastAddJ.TypeDecl
    public Collection lookupSuperConstructor() {
        state();
        return hasSuperclass() ? superclass().constructors() : Collections.EMPTY_LIST;
    }

    public boolean noConstructor() {
        state();
        if (!compilationUnit().fromSource()) {
            return false;
        }
        for (int i = 0; i < getNumBodyDecl(); i++) {
            if (getBodyDecl(i) instanceof ConstructorDecl) {
                return false;
            }
        }
        return true;
    }

    public SimpleSet interfacesMethodsSignature(String signature) {
        state();
        SimpleSet set = (SimpleSet) interfacesMethodsSignatureMap().get(signature);
        return set != null ? set : SimpleSet.emptySet;
    }

    public HashMap interfacesMethodsSignatureMap() {
        if (this.interfacesMethodsSignatureMap_computed) {
            return this.interfacesMethodsSignatureMap_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.interfacesMethodsSignatureMap_value = interfacesMethodsSignatureMap_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.interfacesMethodsSignatureMap_computed = true;
        }
        return this.interfacesMethodsSignatureMap_value;
    }

    private HashMap interfacesMethodsSignatureMap_compute() {
        HashMap map = new HashMap();
        Iterator iter = interfacesIterator();
        while (iter.hasNext()) {
            TypeDecl typeDecl = (InterfaceDecl) iter.next();
            Iterator i2 = typeDecl.methodsIterator();
            while (i2.hasNext()) {
                MethodDecl m = (MethodDecl) i2.next();
                putSimpleSetElement(map, m.signature(), m);
            }
        }
        return map;
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
        if (hasSuperclass()) {
            Iterator iter = superclass().methodsIterator();
            while (iter.hasNext()) {
                MethodDecl m = (MethodDecl) iter.next();
                if (!m.isPrivate() && m.accessibleFrom(this) && !localMethodsSignatureMap().containsKey(m.signature()) && (!(m instanceof MethodDeclSubstituted) || !localMethodsSignatureMap().containsKey(m.sourceMethodDecl().signature()))) {
                    putSimpleSetElement(map, m.signature(), m);
                }
            }
        }
        Iterator outerIter = interfacesIterator();
        while (outerIter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl) outerIter.next();
            Iterator iter2 = typeDecl.methodsIterator();
            while (iter2.hasNext()) {
                MethodDecl m2 = (MethodDecl) iter2.next();
                if (!m2.isPrivate() && m2.accessibleFrom(this) && !localMethodsSignatureMap().containsKey(m2.signature()) && (!(m2 instanceof MethodDeclSubstituted) || !localMethodsSignatureMap().containsKey(m2.sourceMethodDecl().signature()))) {
                    if (allMethodsAbstract((SimpleSet) map.get(m2.signature())) && (!(m2 instanceof MethodDeclSubstituted) || allMethodsAbstract((SimpleSet) map.get(m2.sourceMethodDecl().signature())))) {
                        putSimpleSetElement(map, m2.signature(), m2);
                    }
                }
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
        if (hasSuperclass()) {
            for (MethodDecl m : superclass().localMethodsSignature(signature)) {
                if (!m.isPrivate()) {
                    set = set.add(m);
                }
            }
        }
        if (set.size() != 1 || ((MethodDecl) set.iterator().next()).isAbstract()) {
            for (MethodDecl m2 : interfacesMethodsSignature(signature)) {
                set = set.add(m2);
            }
        }
        if (hasSuperclass()) {
            if (set.size() == 1) {
                MethodDecl m3 = (MethodDecl) set.iterator().next();
                if (!m3.isAbstract()) {
                    boolean done = true;
                    for (MethodDecl n : superclass().ancestorMethods(signature)) {
                        if (n.isPrivate() || !n.accessibleFrom(m3.hostType())) {
                            done = false;
                        }
                    }
                    if (done) {
                        return set;
                    }
                }
            }
            for (MethodDecl m4 : superclass().ancestorMethods(signature)) {
                set = set.add(m4);
            }
            return set;
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
            Iterator outerIter = interfacesIterator();
            while (outerIter.hasNext()) {
                TypeDecl type = (TypeDecl) outerIter.next();
                for (TypeDecl decl : type.memberTypes(name)) {
                    if (!decl.isPrivate() && decl.accessibleFrom(this)) {
                        set = set.add(decl);
                    }
                }
            }
            if (hasSuperclass()) {
                for (TypeDecl decl2 : superclass().memberTypes(name)) {
                    if (!decl2.isPrivate() && decl2.accessibleFrom(this)) {
                        set = set.add(decl2);
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
        if (hasSuperclass()) {
            Iterator iter = superclass().fieldsIterator();
            while (iter.hasNext()) {
                FieldDeclaration decl = (FieldDeclaration) iter.next();
                if (!decl.isPrivate() && decl.accessibleFrom(this) && !localFieldsMap().containsKey(decl.name())) {
                    putSimpleSetElement(map, decl.name(), decl);
                }
            }
        }
        Iterator outerIter = interfacesIterator();
        while (outerIter.hasNext()) {
            TypeDecl type = (TypeDecl) outerIter.next();
            Iterator iter2 = type.fieldsIterator();
            while (iter2.hasNext()) {
                FieldDeclaration decl2 = (FieldDeclaration) iter2.next();
                if (!decl2.isPrivate() && decl2.accessibleFrom(this) && !localFieldsMap().containsKey(decl2.name())) {
                    putSimpleSetElement(map, decl2.name(), decl2);
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
        if (hasSuperclass()) {
            for (FieldDeclaration decl : superclass().memberFields(name)) {
                if (!decl.isPrivate() && decl.accessibleFrom(this)) {
                    fields = fields.add(decl);
                }
            }
        }
        Iterator outerIter = interfacesIterator();
        while (outerIter.hasNext()) {
            TypeDecl type = (TypeDecl) outerIter.next();
            for (FieldDeclaration decl2 : type.memberFields(name)) {
                if (!decl2.isPrivate() && decl2.accessibleFrom(this)) {
                    fields = fields.add(decl2);
                }
            }
        }
        return fields;
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
        Collection c = new ArrayList();
        Iterator iter = interfacesMethodsIterator();
        while (iter.hasNext()) {
            MethodDecl m = (MethodDecl) iter.next();
            boolean implemented = false;
            SimpleSet set = localMethodsSignature(m.signature());
            if (set.size() == 1 && !((MethodDecl) set.iterator().next()).isAbstract()) {
                implemented = true;
            }
            if (!implemented) {
                Iterator i2 = ancestorMethods(m.signature()).iterator();
                while (true) {
                    if (i2.hasNext()) {
                        if (!((MethodDecl) i2.next()).isAbstract()) {
                            implemented = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            if (!implemented) {
                c.add(m);
            }
        }
        if (hasSuperclass()) {
            for (MethodDecl m2 : superclass().unimplementedMethods()) {
                SimpleSet set2 = localMethodsSignature(m2.signature());
                if (set2.size() == 1) {
                    MethodDecl n = (MethodDecl) set2.iterator().next();
                    if (n.isAbstract() || !n.overrides(m2)) {
                        c.add(m2);
                    }
                } else {
                    c.add(m2);
                }
            }
        }
        Iterator iter2 = localMethodsIterator();
        while (iter2.hasNext()) {
            MethodDecl m3 = (MethodDecl) iter2.next();
            if (m3.isAbstract()) {
                c.add(m3);
            }
        }
        return c;
    }

    @Override // soot.JastAddJ.TypeDecl
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
        return !unimplementedMethods().isEmpty();
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
        if (refined_Generics_ClassDecl_castingConversionTo_TypeDecl(type)) {
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
    public boolean isClassDecl() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.TypeDecl
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
        return fullName().equals("java.lang.String");
    }

    @Override // soot.JastAddJ.TypeDecl
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
        return name().equals("Object") && packageName().equals("java.lang");
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
        return type.hasSuperclass() && type.superclass() != null && type.superclass().instanceOf(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isSupertypeOfInterfaceDecl(InterfaceDecl type) {
        state();
        return isObject();
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isSupertypeOfArrayDecl(ArrayDecl type) {
        state();
        if (super.isSupertypeOfArrayDecl(type)) {
            return true;
        }
        return type.hasSuperclass() && type.superclass() != null && type.superclass().instanceOf(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isInnerClass() {
        state();
        return isNestedType() && !isStatic() && enclosingType().isClassDecl();
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

    /* JADX WARN: Code restructure failed: missing block: B:33:0x007d, code lost:
        r4 = r4 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean isCircular_compute() {
        /*
            r3 = this;
            r0 = r3
            boolean r0 = r0.hasSuperClassAccess()
            if (r0 == 0) goto L3f
            r0 = r3
            soot.JastAddJ.Access r0 = r0.getSuperClassAccess()
            soot.JastAddJ.Access r0 = r0.lastAccess()
            r4 = r0
            goto L3b
        L12:
            r0 = r4
            soot.JastAddJ.TypeDecl r0 = r0.type()
            boolean r0 = r0.isCircular()
            if (r0 == 0) goto L1e
            r0 = 1
            return r0
        L1e:
            r0 = r4
            boolean r0 = r0.isQualified()
            if (r0 == 0) goto L39
            r0 = r4
            soot.JastAddJ.Expr r0 = r0.qualifier()
            boolean r0 = r0.isTypeAccess()
            if (r0 == 0) goto L39
            r0 = r4
            soot.JastAddJ.Expr r0 = r0.qualifier()
            soot.JastAddJ.Access r0 = (soot.JastAddJ.Access) r0
            goto L3a
        L39:
            r0 = 0
        L3a:
            r4 = r0
        L3b:
            r0 = r4
            if (r0 != 0) goto L12
        L3f:
            r0 = 0
            r4 = r0
            goto L80
        L44:
            r0 = r3
            r1 = r4
            soot.JastAddJ.Access r0 = r0.getImplements(r1)
            soot.JastAddJ.Access r0 = r0.lastAccess()
            r5 = r0
            goto L79
        L50:
            r0 = r5
            soot.JastAddJ.TypeDecl r0 = r0.type()
            boolean r0 = r0.isCircular()
            if (r0 == 0) goto L5c
            r0 = 1
            return r0
        L5c:
            r0 = r5
            boolean r0 = r0.isQualified()
            if (r0 == 0) goto L77
            r0 = r5
            soot.JastAddJ.Expr r0 = r0.qualifier()
            boolean r0 = r0.isTypeAccess()
            if (r0 == 0) goto L77
            r0 = r5
            soot.JastAddJ.Expr r0 = r0.qualifier()
            soot.JastAddJ.Access r0 = (soot.JastAddJ.Access) r0
            goto L78
        L77:
            r0 = 0
        L78:
            r5 = r0
        L79:
            r0 = r5
            if (r0 != 0) goto L50
            int r4 = r4 + 1
        L80:
            r0 = r4
            r1 = r3
            int r1 = r1.getNumImplements()
            if (r0 < r1) goto L44
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.JastAddJ.ClassDecl.isCircular_compute():boolean");
    }

    @Override // soot.JastAddJ.TypeDecl
    public Annotation annotation(TypeDecl typeDecl) {
        state();
        Annotation a = super.annotation(typeDecl);
        if (a != null) {
            return a;
        }
        if (hasSuperclass() && typeDecl.annotation(lookupType("java.lang.annotation", "Inherited")) != null) {
            return superclass().annotation(typeDecl);
        }
        return null;
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
        if (hasSuperclass()) {
            set.addAll(superclass().implementedInterfaces());
        }
        Iterator iter = interfacesIterator();
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
        return type.supertypeClassDecl(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeClassDecl(ClassDecl type) {
        state();
        if (super.supertypeClassDecl(type)) {
            return true;
        }
        return type.hasSuperclass() && type.superclass() != null && type.superclass().subtype(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeInterfaceDecl(InterfaceDecl type) {
        state();
        return isObject();
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeArrayDecl(ArrayDecl type) {
        state();
        if (super.supertypeArrayDecl(type)) {
            return true;
        }
        return type.hasSuperclass() && type.superclass() != null && type.superclass().subtype(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl superEnclosing() {
        state();
        return superclass().erasure().enclosing();
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
        if (!Scene.v().isIncrementalBuild()) {
            return refined_EmitJimpleRefinements_ClassDecl_sootClass();
        }
        if (Scene.v().containsClass(jvmName())) {
            Scene.v().removeClass(Scene.v().getSootClass(jvmName()));
        }
        if (options().verbose()) {
            System.out.println("Creating from source " + jvmName());
        }
        SootClass sc = new SootClass(jvmName());
        sc.setResolvingLevel(0);
        Scene.v().addClass(sc);
        return sc;
    }

    @Override // soot.JastAddJ.TypeDecl
    public String typeDescriptor() {
        state();
        return "L" + jvmName().replace('.', '/') + ";";
    }

    @Override // soot.JastAddJ.TypeDecl
    public SimpleSet bridgeCandidates(String signature) {
        state();
        SimpleSet set = ancestorMethods(signature);
        for (Object obj : interfacesMethodsSignature(signature)) {
            set = set.add(obj);
        }
        return set;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return true;
        }
        return super.Define_boolean_mayBeFinal(caller, child);
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getImplementsListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.TYPE_NAME;
        } else if (caller == getSuperClassAccessOptNoTransform()) {
            return NameType.TYPE_NAME;
        } else {
            return super.Define_NameType_nameType(caller, child);
        }
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
        if (caller == getImplementsListNoTransform()) {
            caller.getIndexOfChild(child);
            return hostType();
        } else if (caller == getSuperClassAccessOptNoTransform()) {
            return hostType();
        } else {
            return super.Define_TypeDecl_hostType(caller, child);
        }
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean Define_boolean_withinSuppressWarnings(ASTNode caller, ASTNode child, String s) {
        if (caller == getImplementsListNoTransform()) {
            caller.getIndexOfChild(child);
            return hasAnnotationSuppressWarnings(s) || withinSuppressWarnings(s);
        } else if (caller == getSuperClassAccessOptNoTransform()) {
            return hasAnnotationSuppressWarnings(s) || withinSuppressWarnings(s);
        } else {
            return super.Define_boolean_withinSuppressWarnings(caller, child, s);
        }
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean Define_boolean_withinDeprecatedAnnotation(ASTNode caller, ASTNode child) {
        if (caller == getImplementsListNoTransform()) {
            caller.getIndexOfChild(child);
            return isDeprecated() || withinDeprecatedAnnotation();
        } else if (caller == getSuperClassAccessOptNoTransform()) {
            return isDeprecated() || withinDeprecatedAnnotation();
        } else {
            return super.Define_boolean_withinDeprecatedAnnotation(caller, child);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inExtendsOrImplements(ASTNode caller, ASTNode child) {
        if (caller == getImplementsListNoTransform()) {
            caller.getIndexOfChild(child);
            return true;
        } else if (caller == getSuperClassAccessOptNoTransform()) {
            return true;
        } else {
            return getParent().Define_boolean_inExtendsOrImplements(this, caller);
        }
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
