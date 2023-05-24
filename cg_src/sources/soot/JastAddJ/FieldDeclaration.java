package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.JastAddJ.SimpleSet;
import soot.Scene;
import soot.SootField;
import soot.SootFieldRef;
import soot.Type;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/FieldDeclaration.class */
public class FieldDeclaration extends MemberDecl implements Cloneable, SimpleSet, Iterator, Variable {
    private FieldDeclaration iterElem;
    private FieldDecl fieldDecl;
    public SootField sootField;
    protected String tokenString_ID;
    public int IDstart;
    public int IDend;
    protected Map accessibleFrom_TypeDecl_values;
    protected boolean exceptions_computed;
    protected Collection exceptions_value;
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected boolean constant_computed;
    protected Constant constant_value;
    protected boolean usesTypeVariable_computed;
    protected boolean usesTypeVariable_value;
    protected boolean sourceVariableDecl_computed;
    protected Variable sourceVariableDecl_value;
    protected boolean sootRef_computed;
    protected SootFieldRef sootRef_value;
    protected boolean throwTypes_computed;
    protected Collection<TypeDecl> throwTypes_value;

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.accessibleFrom_TypeDecl_values = null;
        this.exceptions_computed = false;
        this.exceptions_value = null;
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.constant_computed = false;
        this.constant_value = null;
        this.usesTypeVariable_computed = false;
        this.sourceVariableDecl_computed = false;
        this.sourceVariableDecl_value = null;
        this.sootRef_computed = false;
        this.sootRef_value = null;
        this.throwTypes_computed = false;
        this.throwTypes_value = null;
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public FieldDeclaration clone() throws CloneNotSupportedException {
        FieldDeclaration node = (FieldDeclaration) super.clone();
        node.accessibleFrom_TypeDecl_values = null;
        node.exceptions_computed = false;
        node.exceptions_value = null;
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.constant_computed = false;
        node.constant_value = null;
        node.usesTypeVariable_computed = false;
        node.sourceVariableDecl_computed = false;
        node.sourceVariableDecl_value = null;
        node.sootRef_computed = false;
        node.sootRef_value = null;
        node.throwTypes_computed = false;
        node.throwTypes_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            FieldDeclaration node = clone();
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

    public Access createQualifiedBoundAccess() {
        if (isStatic()) {
            return hostType().createQualifiedAccess().qualifiesAccess(new BoundFieldAccess(this));
        }
        return new ThisAccess("this").qualifiesAccess(new BoundFieldAccess(this));
    }

    public Access createBoundFieldAccess() {
        return createQualifiedBoundAccess();
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

    @Override // soot.JastAddJ.ASTNode
    public void definiteAssignment() {
        super.definiteAssignment();
        if (isBlank() && isFinal() && isClassVariable()) {
            boolean found = false;
            TypeDecl typeDecl = hostType();
            for (int i = 0; i < typeDecl.getNumBodyDecl(); i++) {
                if (typeDecl.getBodyDecl(i) instanceof StaticInitializer) {
                    StaticInitializer s = (StaticInitializer) typeDecl.getBodyDecl(i);
                    if (s.isDAafter(this)) {
                        found = true;
                    }
                } else if (typeDecl.getBodyDecl(i) instanceof FieldDeclaration) {
                    FieldDeclaration f = (FieldDeclaration) typeDecl.getBodyDecl(i);
                    if (f.isStatic() && f.isDAafter(this)) {
                        found = true;
                    }
                }
            }
            if (!found) {
                error("blank final class variable " + name() + " in " + hostType().typeName() + " is not definitely assigned in static initializer");
            }
        }
        if (isBlank() && isFinal() && isInstanceVariable()) {
            TypeDecl typeDecl2 = hostType();
            boolean found2 = false;
            for (int i2 = 0; !found2 && i2 < typeDecl2.getNumBodyDecl(); i2++) {
                if (typeDecl2.getBodyDecl(i2) instanceof FieldDeclaration) {
                    FieldDeclaration f2 = (FieldDeclaration) typeDecl2.getBodyDecl(i2);
                    if (!f2.isStatic() && f2.isDAafter(this)) {
                        found2 = true;
                    }
                } else if (typeDecl2.getBodyDecl(i2) instanceof InstanceInitializer) {
                    InstanceInitializer ii = (InstanceInitializer) typeDecl2.getBodyDecl(i2);
                    if (ii.getBlock().isDAafter(this)) {
                        found2 = true;
                    }
                }
            }
            Iterator iter = typeDecl2.constructors().iterator();
            while (!found2 && iter.hasNext()) {
                ConstructorDecl c = (ConstructorDecl) iter.next();
                if (!c.isDAafter(this)) {
                    error("blank final instance variable " + name() + " in " + hostType().typeName() + " is not definitely assigned after " + c.signature());
                }
            }
        }
        if (isBlank() && hostType().isInterfaceDecl()) {
            error("variable  " + name() + " in " + hostType().typeName() + " which is an interface must have an initializer");
        }
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.ASTNode
    public void checkModifiers() {
        super.checkModifiers();
        if (hostType().isInterfaceDecl()) {
            if (isProtected()) {
                error("an interface field may not be protected");
            }
            if (isPrivate()) {
                error("an interface field may not be private");
            }
            if (isTransient()) {
                error("an interface field may not be transient");
            }
            if (isVolatile()) {
                error("an interface field may not be volatile");
            }
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void nameCheck() {
        super.nameCheck();
        for (Variable v : hostType().memberFields(name())) {
            if (v != this && v.hostType() == hostType()) {
                error("field named " + name() + " is multiply declared in type " + hostType().typeName());
            }
        }
    }

    public FieldDeclaration(Modifiers m, Access type, String name) {
        this(m, type, name, new Opt());
    }

    public FieldDeclaration(Modifiers m, Access type, String name, Expr init) {
        this(m, type, name, new Opt(init));
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        getModifiers().toString(s);
        getTypeAccess().toString(s);
        s.append(Instruction.argsep + name());
        if (hasInit()) {
            s.append(" = ");
            getInit().toString(s);
        }
        s.append(";");
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (hasInit()) {
            TypeDecl source = getInit().type();
            TypeDecl dest = type();
            if (!source.assignConversionTo(dest, getInit())) {
                error("can not assign field " + name() + " of type " + dest.typeName() + " a value of type " + source.typeName());
            }
        }
    }

    public FieldDecl getFieldDecl() {
        return this.fieldDecl;
    }

    public void setFieldDecl(FieldDecl fieldDecl) {
        this.fieldDecl = fieldDecl;
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [soot.JastAddJ.Modifiers] */
    @Override // soot.JastAddJ.BodyDecl
    public BodyDecl substitutedBodyDecl(Parameterization parTypeDecl) {
        FieldDeclaration f = new FieldDeclarationSubstituted((Modifiers) getModifiers().fullCopy2(), getTypeAccess().type().substituteReturnType(parTypeDecl), getID(), new Opt(), this);
        return f;
    }

    public MethodDecl createAccessor(TypeDecl fieldQualifier) {
        MethodDecl m = (MethodDecl) fieldQualifier.getAccessor(this, "field_read");
        if (m != null) {
            return m;
        }
        int accessorIndex = fieldQualifier.accessorCounter;
        fieldQualifier.accessorCounter = accessorIndex + 1;
        Modifiers modifiers = new Modifiers(new List());
        modifiers.addModifier(new Modifier(Jimple.STATIC));
        modifiers.addModifier(new Modifier("synthetic"));
        modifiers.addModifier(new Modifier(Jimple.PUBLIC));
        List parameters = new List();
        if (!isStatic()) {
            parameters.add(new ParameterDeclaration(fieldQualifier.createQualifiedAccess(), "that"));
        }
        MethodDecl m2 = fieldQualifier.addMemberMethod(new MethodDecl(modifiers, type().createQualifiedAccess(), "get$" + name() + "$access$" + accessorIndex, parameters, new List(), new Opt(new Block(new List().add(new ReturnStmt(createAccess()))))));
        fieldQualifier.addAccessor(this, "field_read", m2);
        return m2;
    }

    public MethodDecl createAccessorWrite(TypeDecl fieldQualifier) {
        MethodDecl m = (MethodDecl) fieldQualifier.getAccessor(this, "field_write");
        if (m != null) {
            return m;
        }
        int accessorIndex = fieldQualifier.accessorCounter;
        fieldQualifier.accessorCounter = accessorIndex + 1;
        Modifiers modifiers = new Modifiers(new List());
        modifiers.addModifier(new Modifier(Jimple.STATIC));
        modifiers.addModifier(new Modifier("synthetic"));
        modifiers.addModifier(new Modifier(Jimple.PUBLIC));
        List parameters = new List();
        if (!isStatic()) {
            parameters.add(new ParameterDeclaration(fieldQualifier.createQualifiedAccess(), "that"));
        }
        parameters.add(new ParameterDeclaration(type().createQualifiedAccess(), "value"));
        MethodDecl m2 = fieldQualifier.addMemberMethod(new MethodDecl(modifiers, type().createQualifiedAccess(), "set$" + name() + "$access$" + accessorIndex, parameters, new List(), new Opt(new Block(new List().add(new ExprStmt(new AssignSimpleExpr(createAccess(), new VarAccess("value")))).add(new ReturnStmt(new Opt(new VarAccess("value"))))))));
        fieldQualifier.addAccessor(this, "field_write", m2);
        return m2;
    }

    private Access createAccess() {
        Access fieldAccess = new BoundFieldAccess(this);
        return isStatic() ? fieldAccess : new VarAccess("that").qualifiesAccess(fieldAccess);
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void jimplify1phase2() {
        String name = name();
        Type type = type().getSootType();
        int modifiers = sootTypeModifiers();
        if (!hostType().getSootClassDecl().declaresFieldByName(name)) {
            SootField f = Scene.v().makeSootField(name, type, modifiers);
            hostType().getSootClassDecl().addField(f);
            if (isStatic() && isFinal() && isConstant() && (type().isPrimitive() || type().isString())) {
                if (type().isString()) {
                    f.addTag(new StringConstantValueTag(constant().stringValue()));
                } else if (type().isLong()) {
                    f.addTag(new LongConstantValueTag(constant().longValue()));
                } else if (type().isDouble()) {
                    f.addTag(new DoubleConstantValueTag(constant().doubleValue()));
                } else if (type().isFloat()) {
                    f.addTag(new FloatConstantValueTag(constant().floatValue()));
                } else if (type().isIntegralType()) {
                    f.addTag(new IntegerConstantValueTag(constant().intValue()));
                }
            }
            this.sootField = f;
        } else {
            this.sootField = hostType().getSootClassDecl().getFieldByName(name);
        }
        addAttributes();
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
            this.sootField.addTag(tag);
        }
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void checkWarnings() {
        if (hasInit() && !suppressWarnings("unchecked")) {
            checkUncheckedConversion(getInit().type(), type());
        }
    }

    public FieldDeclaration() {
        this.fieldDecl = null;
        this.exceptions_computed = false;
        this.constant_computed = false;
        this.usesTypeVariable_computed = false;
        this.sourceVariableDecl_computed = false;
        this.sootRef_computed = false;
        this.throwTypes_computed = false;
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new Opt(), 2);
    }

    public FieldDeclaration(Modifiers p0, Access p1, String p2, Opt<Expr> p3) {
        this.fieldDecl = null;
        this.exceptions_computed = false;
        this.constant_computed = false;
        this.usesTypeVariable_computed = false;
        this.sourceVariableDecl_computed = false;
        this.sootRef_computed = false;
        this.throwTypes_computed = false;
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
    }

    public FieldDeclaration(Modifiers p0, Access p1, Symbol p2, Opt<Expr> p3) {
        this.fieldDecl = null;
        this.exceptions_computed = false;
        this.constant_computed = false;
        this.usesTypeVariable_computed = false;
        this.sourceVariableDecl_computed = false;
        this.sootRef_computed = false;
        this.throwTypes_computed = false;
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
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

    public void setTypeAccess(Access node) {
        setChild(node, 1);
    }

    public Access getTypeAccess() {
        return (Access) getChild(1);
    }

    public Access getTypeAccessNoTransform() {
        return (Access) getChildNoTransform(1);
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

    public void setInitOpt(Opt<Expr> opt) {
        setChild(opt, 2);
    }

    public boolean hasInit() {
        return getInitOpt().getNumChild() != 0;
    }

    public Expr getInit() {
        return getInitOpt().getChild(0);
    }

    public void setInit(Expr node) {
        getInitOpt().setChild(node, 0);
    }

    public Opt<Expr> getInitOpt() {
        return (Opt) getChild(2);
    }

    public Opt<Expr> getInitOptNoTransform() {
        return (Opt) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.MemberDecl
    public boolean isConstant() {
        state();
        if (isFinal() && hasInit() && getInit().isConstant()) {
            return (type() instanceof PrimitiveType) || type().isString();
        }
        return false;
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
        if (isPublic()) {
            return true;
        }
        if (isProtected()) {
            if (hostPackage().equals(type.hostPackage()) || type.withinBodyThatSubclasses(hostType()) != null) {
                return true;
            }
            return false;
        } else if (isPrivate()) {
            return hostType().topLevelType() == type.topLevelType();
        } else {
            return hostPackage().equals(type.hostPackage());
        }
    }

    public Collection exceptions() {
        if (this.exceptions_computed) {
            return this.exceptions_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.exceptions_value = exceptions_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.exceptions_computed = true;
        }
        return this.exceptions_value;
    }

    private Collection exceptions_compute() {
        HashSet set = new HashSet();
        if (isInstanceVariable() && hasInit()) {
            collectExceptions(set, this);
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                TypeDecl typeDecl = (TypeDecl) iter.next();
                if (!getInit().reachedException(typeDecl)) {
                    iter.remove();
                }
            }
        }
        return set;
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

    @Override // soot.JastAddJ.BodyDecl
    public boolean isDAafter(Variable v) {
        if (this.isDAafter_Variable_values == null) {
            this.isDAafter_Variable_values = new HashMap(4);
        }
        if (this.isDAafter_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDAafter_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDAafter_Variable_value = isDAafter_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDAafter_Variable_values.put(v, Boolean.valueOf(isDAafter_Variable_value));
        }
        return isDAafter_Variable_value;
    }

    private boolean isDAafter_compute(Variable v) {
        if (v == this) {
            return hasInit();
        }
        return hasInit() ? getInit().isDAafter(v) : isDAbefore(v);
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean isDUafter(Variable v) {
        if (this.isDUafter_Variable_values == null) {
            this.isDUafter_Variable_values = new HashMap(4);
        }
        if (this.isDUafter_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDUafter_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDUafter_Variable_value = isDUafter_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDUafter_Variable_values.put(v, Boolean.valueOf(isDUafter_Variable_value));
        }
        return isDUafter_Variable_value;
    }

    private boolean isDUafter_compute(Variable v) {
        return v == this ? !hasInit() : hasInit() ? getInit().isDUafter(v) : isDUbefore(v);
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.Variable
    public boolean isSynthetic() {
        state();
        return getModifiers().isSynthetic();
    }

    public boolean isPublic() {
        state();
        return getModifiers().isPublic() || hostType().isInterfaceDecl();
    }

    public boolean isPrivate() {
        state();
        return getModifiers().isPrivate();
    }

    public boolean isProtected() {
        state();
        return getModifiers().isProtected();
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.Variable
    public boolean isStatic() {
        state();
        return getModifiers().isStatic() || hostType().isInterfaceDecl();
    }

    public boolean isFinal() {
        state();
        return getModifiers().isFinal() || hostType().isInterfaceDecl();
    }

    public boolean isTransient() {
        state();
        return getModifiers().isTransient();
    }

    @Override // soot.JastAddJ.Variable
    public boolean isVolatile() {
        state();
        return getModifiers().isVolatile();
    }

    @Override // soot.JastAddJ.ASTNode
    public String dumpString() {
        state();
        return String.valueOf(getClass().getName()) + " [" + getID() + "]";
    }

    @Override // soot.JastAddJ.Variable
    public TypeDecl type() {
        state();
        return getTypeAccess().type();
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean isVoid() {
        state();
        return type().isVoid();
    }

    @Override // soot.JastAddJ.Variable
    public boolean isParameter() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isClassVariable() {
        state();
        return isStatic() || hostType().isInterfaceDecl();
    }

    @Override // soot.JastAddJ.Variable
    public boolean isInstanceVariable() {
        state();
        return (hostType().isClassDecl() || hostType().isAnonymous()) && !isStatic();
    }

    @Override // soot.JastAddJ.Variable
    public boolean isMethodParameter() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isConstructorParameter() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isExceptionHandlerParameter() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isLocalVariable() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isBlank() {
        state();
        return !hasInit();
    }

    @Override // soot.JastAddJ.Variable
    public String name() {
        state();
        return getID();
    }

    @Override // soot.JastAddJ.Variable
    public Constant constant() {
        if (this.constant_computed) {
            return this.constant_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.constant_value = constant_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.constant_computed = true;
        }
        return this.constant_value;
    }

    private Constant constant_compute() {
        return type().cast(getInit().constant());
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean hasAnnotationSuppressWarnings(String s) {
        state();
        return getModifiers().hasAnnotationSuppressWarnings(s);
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean isDeprecated() {
        state();
        return getModifiers().hasDeprecatedAnnotation();
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean usesTypeVariable() {
        if (this.usesTypeVariable_computed) {
            return this.usesTypeVariable_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.usesTypeVariable_value = usesTypeVariable_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.usesTypeVariable_computed = true;
        }
        return this.usesTypeVariable_value;
    }

    private boolean usesTypeVariable_compute() {
        return getTypeAccess().usesTypeVariable();
    }

    @Override // soot.JastAddJ.Variable
    public Variable sourceVariableDecl() {
        if (this.sourceVariableDecl_computed) {
            return this.sourceVariableDecl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.sourceVariableDecl_value = sourceVariableDecl_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.sourceVariableDecl_computed = true;
        }
        return this.sourceVariableDecl_value;
    }

    private Variable sourceVariableDecl_compute() {
        return this;
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean visibleTypeParameters() {
        state();
        return !isStatic();
    }

    public int sootTypeModifiers() {
        state();
        int result = 0;
        if (isPublic()) {
            result = 0 | 1;
        }
        if (isProtected()) {
            result |= 4;
        }
        if (isPrivate()) {
            result |= 2;
        }
        if (isFinal()) {
            result |= 16;
        }
        if (isStatic()) {
            result |= 8;
        }
        return result;
    }

    public SootFieldRef sootRef() {
        if (this.sootRef_computed) {
            return this.sootRef_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.sootRef_value = sootRef_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.sootRef_computed = true;
        }
        return this.sootRef_value;
    }

    private SootFieldRef sootRef_compute() {
        return Scene.v().makeFieldRef(hostType().getSootClassDecl(), name(), type().getSootType(), isStatic());
    }

    public FieldDeclaration erasedField() {
        state();
        return this;
    }

    @Override // soot.JastAddJ.Variable
    public Collection<TypeDecl> throwTypes() {
        if (this.throwTypes_computed) {
            return this.throwTypes_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.throwTypes_value = throwTypes_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.throwTypes_computed = true;
        }
        return this.throwTypes_value;
    }

    private Collection<TypeDecl> throwTypes_compute() {
        Collection<TypeDecl> tts = new LinkedList<>();
        tts.add(type());
        return tts;
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean hasAnnotationSafeVarargs() {
        state();
        return getModifiers().hasAnnotationSafeVarargs();
    }

    public boolean suppressWarnings(String type) {
        state();
        return hasAnnotationSuppressWarnings(type) || withinSuppressWarnings(type);
    }

    public boolean handlesException(TypeDecl exceptionType) {
        state();
        boolean handlesException_TypeDecl_value = getParent().Define_boolean_handlesException(this, null, exceptionType);
        return handlesException_TypeDecl_value;
    }

    public boolean withinSuppressWarnings(String s) {
        state();
        boolean withinSuppressWarnings_String_value = getParent().Define_boolean_withinSuppressWarnings(this, null, s);
        return withinSuppressWarnings_String_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
        if (caller == getInitOptNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_isSource(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getInitOptNoTransform()) {
            return isDAbefore(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
        if (caller == getInitOptNoTransform()) {
            if (hostType().isAnonymous() || !exceptionType.isUncheckedException()) {
                return true;
            }
            for (ConstructorDecl decl : hostType().constructors()) {
                if (!decl.throwsException(exceptionType)) {
                    return false;
                }
            }
            return true;
        }
        return getParent().Define_boolean_handlesException(this, caller, exceptionType);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBePublic(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_mayBePublic(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeProtected(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_mayBeProtected(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBePrivate(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_mayBePrivate(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeStatic(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_mayBeStatic(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_mayBeFinal(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeTransient(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_mayBeTransient(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeVolatile(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_mayBeVolatile(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getTypeAccessNoTransform()) {
            return NameType.TYPE_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
        if (caller == getInitOptNoTransform()) {
            return type();
        }
        return getParent().Define_TypeDecl_declType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
        if (caller == getInitOptNoTransform()) {
            return isStatic() || hostType().isInterfaceDecl();
        }
        return getParent().Define_boolean_inStaticContext(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
        if (caller == getModifiersNoTransform()) {
            return name.equals("FIELD");
        }
        return getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
        if (caller == getInitOptNoTransform()) {
            return type();
        }
        return getParent().Define_TypeDecl_assignConvertedType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_expectedType(ASTNode caller, ASTNode child) {
        if (caller == getInitOptNoTransform()) {
            return type().componentType();
        }
        return getParent().Define_TypeDecl_expectedType(this, caller);
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
