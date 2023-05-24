package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.Scene;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.coffi.CoffiMethodSource;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.tagkit.AnnotationTag;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ConstructorDecl.class */
public class ConstructorDecl extends BodyDecl implements Cloneable {
    public SootMethod sootMethod;
    protected String tokenString_ID;
    public int IDstart;
    public int IDend;
    protected Map accessibleFrom_TypeDecl_values;
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected Map throwsException_TypeDecl_values;
    protected String name_value;
    protected String signature_value;
    protected Map sameSignature_ConstructorDecl_values;
    protected Map moreSpecificThan_ConstructorDecl_values;
    protected Map parameterDeclaration_String_values;
    protected Map circularThisInvocation_ConstructorDecl_values;
    protected ConstructorDecl sourceConstructorDecl_value;
    protected SootMethod sootMethod_value;
    protected SootMethodRef sootRef_value;
    protected int localNumOfFirstParameter_value;
    protected int offsetFirstEnclosingVariable_value;
    protected Map handlesException_TypeDecl_values;
    private boolean isDefaultConstructor = false;
    protected boolean addEnclosingVariables = true;
    protected boolean name_computed = false;
    protected boolean signature_computed = false;
    protected boolean sourceConstructorDecl_computed = false;
    protected boolean sootMethod_computed = false;
    protected boolean sootRef_computed = false;
    protected boolean localNumOfFirstParameter_computed = false;
    protected boolean offsetFirstEnclosingVariable_computed = false;

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.accessibleFrom_TypeDecl_values = null;
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.throwsException_TypeDecl_values = null;
        this.name_computed = false;
        this.name_value = null;
        this.signature_computed = false;
        this.signature_value = null;
        this.sameSignature_ConstructorDecl_values = null;
        this.moreSpecificThan_ConstructorDecl_values = null;
        this.parameterDeclaration_String_values = null;
        this.circularThisInvocation_ConstructorDecl_values = null;
        this.sourceConstructorDecl_computed = false;
        this.sourceConstructorDecl_value = null;
        this.sootMethod_computed = false;
        this.sootMethod_value = null;
        this.sootRef_computed = false;
        this.sootRef_value = null;
        this.localNumOfFirstParameter_computed = false;
        this.offsetFirstEnclosingVariable_computed = false;
        this.handlesException_TypeDecl_values = null;
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public ConstructorDecl clone() throws CloneNotSupportedException {
        ConstructorDecl node = (ConstructorDecl) super.clone();
        node.accessibleFrom_TypeDecl_values = null;
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.throwsException_TypeDecl_values = null;
        node.name_computed = false;
        node.name_value = null;
        node.signature_computed = false;
        node.signature_value = null;
        node.sameSignature_ConstructorDecl_values = null;
        node.moreSpecificThan_ConstructorDecl_values = null;
        node.parameterDeclaration_String_values = null;
        node.circularThisInvocation_ConstructorDecl_values = null;
        node.sourceConstructorDecl_computed = false;
        node.sourceConstructorDecl_value = null;
        node.sootMethod_computed = false;
        node.sootMethod_value = null;
        node.sootRef_computed = false;
        node.sootRef_value = null;
        node.localNumOfFirstParameter_computed = false;
        node.offsetFirstEnclosingVariable_computed = false;
        node.handlesException_TypeDecl_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            ConstructorDecl node = clone();
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

    public boolean applicable(List argList) {
        if (getNumParameter() != argList.getNumChild()) {
            return false;
        }
        for (int i = 0; i < getNumParameter(); i++) {
            TypeDecl arg = ((Expr) argList.getChild(i)).type();
            TypeDecl parameter = getParameter(i).type();
            if (!arg.instanceOf(parameter)) {
                return false;
            }
        }
        return true;
    }

    public void setDefaultConstructor() {
        this.isDefaultConstructor = true;
    }

    @Override // soot.JastAddJ.ASTNode
    public void checkModifiers() {
        super.checkModifiers();
    }

    @Override // soot.JastAddJ.ASTNode
    public void nameCheck() {
        super.nameCheck();
        if (!hostType().name().equals(name())) {
            error("constructor " + name() + " does not have the same name as the simple name of the host class " + hostType().name());
        }
        if (hostType().lookupConstructor(this) != this) {
            error("constructor with signature " + signature() + " is multiply declared in type " + hostType().typeName());
        }
        if (circularThisInvocation(this)) {
            error("The constructor " + signature() + " may not directly or indirectly invoke itself");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        if (isDefaultConstructor()) {
            return;
        }
        s.append(indent());
        getModifiers().toString(s);
        s.append(String.valueOf(name()) + "(");
        if (getNumParameter() > 0) {
            getParameter(0).toString(s);
            for (int i = 1; i < getNumParameter(); i++) {
                s.append(", ");
                getParameter(i).toString(s);
            }
        }
        s.append(")");
        if (getNumException() > 0) {
            s.append(" throws ");
            getException(0).toString(s);
            for (int i2 = 1; i2 < getNumException(); i2++) {
                s.append(", ");
                getException(i2).toString(s);
            }
        }
        s.append(" {");
        if (hasConstructorInvocation()) {
            getConstructorInvocation().toString(s);
        }
        for (int i3 = 0; i3 < getBlock().getNumStmt(); i3++) {
            getBlock().getStmt(i3).toString(s);
        }
        s.append(indent());
        s.append("}");
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        TypeDecl exceptionType = typeThrowable();
        for (int i = 0; i < getNumException(); i++) {
            TypeDecl typeDecl = getException(i).type();
            if (!typeDecl.instanceOf(exceptionType)) {
                error(String.valueOf(signature()) + " throws non throwable type " + typeDecl.fullName());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.JastAddJ.ASTNode
    public void transformEnumConstructors() {
        Modifiers newModifiers = new Modifiers(new List());
        for (int i = 0; i < getModifiers().getNumModifier(); i++) {
            String modifier = getModifiers().getModifier(i).getID();
            if (!modifier.equals(Jimple.PUBLIC) && !modifier.equals(Jimple.PRIVATE) && !modifier.equals(Jimple.PROTECTED)) {
                newModifiers.addModifier(new Modifier(modifier));
            }
        }
        newModifiers.addModifier(new Modifier(Jimple.PRIVATE));
        setModifiers(newModifiers);
        if (!hasConstructorInvocation()) {
            setConstructorInvocation(new ExprStmt(new SuperConstructorAccess("super", new List())));
        }
        super.transformEnumConstructors();
        getParameterList().insertChild(new ParameterDeclaration(new TypeAccess("java.lang", "String"), "@p0"), 0);
        getParameterList().insertChild(new ParameterDeclaration(new TypeAccess("int"), "@p1"), 1);
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [soot.JastAddJ.Modifiers] */
    @Override // soot.JastAddJ.BodyDecl
    public BodyDecl substitutedBodyDecl(Parameterization parTypeDecl) {
        ConstructorDecl c = new ConstructorDeclSubstituted((Modifiers) getModifiers().fullCopy2(), getID(), getParameterList().substitute(parTypeDecl), getExceptionList().substitute(parTypeDecl), new Opt(), new Block(), this);
        return c;
    }

    public void addEnclosingVariables() {
        if (!this.addEnclosingVariables) {
            return;
        }
        this.addEnclosingVariables = false;
        hostType().addEnclosingVariables();
        for (Variable v : hostType().enclosingVariables()) {
            getParameterList().add(new ParameterDeclaration(v.type(), "val$" + v.name()));
        }
    }

    public ConstructorDecl createAccessor() {
        ConstructorDecl c = (ConstructorDecl) hostType().getAccessor(this, "constructor");
        if (c != null) {
            return c;
        }
        addEnclosingVariables();
        Modifiers modifiers = new Modifiers(new List());
        modifiers.addModifier(new Modifier("synthetic"));
        modifiers.addModifier(new Modifier(Jimple.PUBLIC));
        List parameters = createAccessorParameters();
        List exceptionList = new List();
        for (int i = 0; i < getNumException(); i++) {
            exceptionList.add(getException(i).type().createQualifiedAccess());
        }
        List args = new List();
        for (int i2 = 0; i2 < parameters.getNumChildNoTransform() - 1; i2++) {
            args.add(new VarAccess(((ParameterDeclaration) parameters.getChildNoTransform(i2)).name()));
        }
        ConstructorAccess access = new ConstructorAccess("this", args);
        access.addEnclosingVariables = false;
        ConstructorDecl c2 = hostType().addConstructor(new ConstructorDecl(modifiers, name(), parameters, exceptionList, new Opt(new ExprStmt(access)), new Block(new List().add(new ReturnStmt(new Opt())))));
        c2.addEnclosingVariables = false;
        hostType().addAccessor(this, "constructor", c2);
        return c2;
    }

    protected List createAccessorParameters() {
        List parameters = new List();
        for (int i = 0; i < getNumParameter(); i++) {
            parameters.add(new ParameterDeclaration(getParameter(i).type(), getParameter(i).name()));
        }
        parameters.add(new ParameterDeclaration(createAnonymousJavaTypeDecl().createBoundAccess(), "p" + getNumParameter()));
        return parameters;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public TypeDecl createAnonymousJavaTypeDecl() {
        ClassDecl classDecl = hostType().addMemberClass(new ClassDecl(new Modifiers(new List().add(new Modifier("synthetic"))), new StringBuilder().append(hostType().nextAnonymousIndex()).toString(), new Opt(), new List(), new List()));
        hostType().addNestedType(classDecl);
        return classDecl;
    }

    @Override // soot.JastAddJ.ASTNode
    public void transformation() {
        addEnclosingVariables();
        super.transformation();
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void jimplify1phase2() {
        ArrayList parameters = new ArrayList();
        ArrayList paramnames = new ArrayList();
        TypeDecl typeDecl = hostType();
        if (typeDecl.needsEnclosing()) {
            parameters.add(typeDecl.enclosingType().getSootType());
        }
        if (typeDecl.needsSuperEnclosing()) {
            TypeDecl superClass = ((ClassDecl) typeDecl).superclass();
            parameters.add(superClass.enclosingType().getSootType());
        }
        for (int i = 0; i < getNumParameter(); i++) {
            parameters.add(getParameter(i).type().getSootType());
            paramnames.add(getParameter(i).name());
        }
        Type returnType = soot.VoidType.v();
        int modifiers = sootTypeModifiers();
        ArrayList throwtypes = new ArrayList();
        for (int i2 = 0; i2 < getNumException(); i2++) {
            throwtypes.add(getException(i2).type().getSootClassDecl());
        }
        String signature = SootMethod.getSubSignature("<init>", parameters, returnType);
        if (!hostType().getSootClassDecl().declaresMethod(signature)) {
            SootMethod m = Scene.v().makeSootMethod("<init>", parameters, returnType, modifiers, throwtypes);
            hostType().getSootClassDecl().addMethod(m);
            m.addTag(new ParamNamesTag(paramnames));
            this.sootMethod = m;
        } else {
            this.sootMethod = hostType().getSootClassDecl().getMethod(signature);
        }
        addAttributes();
    }

    @Override // soot.JastAddJ.ASTNode
    public void addAttributes() {
        super.addAttributes();
        ArrayList c = new ArrayList();
        getModifiers().addRuntimeVisibleAnnotationsAttribute(c);
        getModifiers().addRuntimeInvisibleAnnotationsAttribute(c);
        addRuntimeVisibleParameterAnnotationsAttribute(c);
        addRuntimeInvisibleParameterAnnotationsAttribute(c);
        addSourceLevelParameterAnnotationsAttribute(c);
        getModifiers().addSourceOnlyAnnotations(c);
        Iterator iter = c.iterator();
        while (iter.hasNext()) {
            Tag tag = (Tag) iter.next();
            this.sootMethod.addTag(tag);
        }
    }

    public void addRuntimeVisibleParameterAnnotationsAttribute(Collection c) {
        boolean foundVisibleAnnotations = false;
        Collection<VisibilityAnnotationTag> annotations = new ArrayList(getNumParameter());
        for (int i = 0; i < getNumParameter(); i++) {
            Collection<Annotation> a = getParameter(i).getModifiers().runtimeVisibleAnnotations();
            if (!a.isEmpty()) {
                foundVisibleAnnotations = true;
            }
            VisibilityAnnotationTag tag = new VisibilityAnnotationTag(0);
            for (Annotation annotation : a) {
                ArrayList elements = new ArrayList(1);
                annotation.appendAsAttributeTo(elements);
                tag.addAnnotation((AnnotationTag) elements.get(0));
            }
            annotations.add(tag);
        }
        if (foundVisibleAnnotations) {
            VisibilityParameterAnnotationTag tag2 = new VisibilityParameterAnnotationTag(annotations.size(), 0);
            for (VisibilityAnnotationTag visibilityAnnotationTag : annotations) {
                tag2.addVisibilityAnnotation(visibilityAnnotationTag);
            }
            c.add(tag2);
        }
    }

    public void addRuntimeInvisibleParameterAnnotationsAttribute(Collection c) {
        boolean foundVisibleAnnotations = false;
        Collection<VisibilityAnnotationTag> annotations = new ArrayList(getNumParameter());
        for (int i = 0; i < getNumParameter(); i++) {
            Collection<Annotation> a = getParameter(i).getModifiers().runtimeInvisibleAnnotations();
            if (!a.isEmpty()) {
                foundVisibleAnnotations = true;
            }
            VisibilityAnnotationTag tag = new VisibilityAnnotationTag(1);
            for (Annotation annotation : a) {
                ArrayList elements = new ArrayList(1);
                annotation.appendAsAttributeTo(elements);
                tag.addAnnotation((AnnotationTag) elements.get(0));
            }
            annotations.add(tag);
        }
        if (foundVisibleAnnotations) {
            VisibilityParameterAnnotationTag tag2 = new VisibilityParameterAnnotationTag(annotations.size(), 1);
            for (VisibilityAnnotationTag visibilityAnnotationTag : annotations) {
                tag2.addVisibilityAnnotation(visibilityAnnotationTag);
            }
            c.add(tag2);
        }
    }

    public void addSourceLevelParameterAnnotationsAttribute(Collection c) {
        new ArrayList(getNumParameter());
        for (int i = 0; i < getNumParameter(); i++) {
            getParameter(i).getModifiers().addSourceOnlyAnnotations(c);
        }
    }

    public ConstructorDecl() {
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[5];
        setChild(new List(), 1);
        setChild(new List(), 2);
        setChild(new Opt(), 3);
    }

    public ConstructorDecl(Modifiers p0, String p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
    }

    public ConstructorDecl(Modifiers p0, Symbol p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 5;
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return true;
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

    public void setParameterList(List<ParameterDeclaration> list) {
        setChild(list, 1);
    }

    public int getNumParameter() {
        return getParameterList().getNumChild();
    }

    public int getNumParameterNoTransform() {
        return getParameterListNoTransform().getNumChildNoTransform();
    }

    public ParameterDeclaration getParameter(int i) {
        return getParameterList().getChild(i);
    }

    public void addParameter(ParameterDeclaration node) {
        List<ParameterDeclaration> list = (this.parent == null || state == null) ? getParameterListNoTransform() : getParameterList();
        list.addChild(node);
    }

    public void addParameterNoTransform(ParameterDeclaration node) {
        List<ParameterDeclaration> list = getParameterListNoTransform();
        list.addChild(node);
    }

    public void setParameter(ParameterDeclaration node, int i) {
        List<ParameterDeclaration> list = getParameterList();
        list.setChild(node, i);
    }

    public List<ParameterDeclaration> getParameters() {
        return getParameterList();
    }

    public List<ParameterDeclaration> getParametersNoTransform() {
        return getParameterListNoTransform();
    }

    public List<ParameterDeclaration> getParameterList() {
        List<ParameterDeclaration> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    public List<ParameterDeclaration> getParameterListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    public void setExceptionList(List<Access> list) {
        setChild(list, 2);
    }

    public int getNumException() {
        return getExceptionList().getNumChild();
    }

    public int getNumExceptionNoTransform() {
        return getExceptionListNoTransform().getNumChildNoTransform();
    }

    public Access getException(int i) {
        return getExceptionList().getChild(i);
    }

    public void addException(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getExceptionListNoTransform() : getExceptionList();
        list.addChild(node);
    }

    public void addExceptionNoTransform(Access node) {
        List<Access> list = getExceptionListNoTransform();
        list.addChild(node);
    }

    public void setException(Access node, int i) {
        List<Access> list = getExceptionList();
        list.setChild(node, i);
    }

    public List<Access> getExceptions() {
        return getExceptionList();
    }

    public List<Access> getExceptionsNoTransform() {
        return getExceptionListNoTransform();
    }

    public List<Access> getExceptionList() {
        List<Access> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    public List<Access> getExceptionListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    public void setConstructorInvocationOpt(Opt<Stmt> opt) {
        setChild(opt, 3);
    }

    public boolean hasConstructorInvocation() {
        return getConstructorInvocationOpt().getNumChild() != 0;
    }

    public Stmt getConstructorInvocation() {
        return getConstructorInvocationOpt().getChild(0);
    }

    public void setConstructorInvocation(Stmt node) {
        getConstructorInvocationOpt().setChild(node, 0);
    }

    public Opt<Stmt> getConstructorInvocationOpt() {
        return (Opt) getChild(3);
    }

    public Opt<Stmt> getConstructorInvocationOptNoTransform() {
        return (Opt) getChildNoTransform(3);
    }

    public void setBlock(Block node) {
        setChild(node, 4);
    }

    public Block getBlock() {
        return (Block) getChild(4);
    }

    public Block getBlockNoTransform() {
        return (Block) getChildNoTransform(4);
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void jimplify2() {
        if (generate() && !sootMethod().hasActiveBody()) {
            if (sootMethod().getSource() != null && (sootMethod().getSource() instanceof CoffiMethodSource)) {
                return;
            }
            JimpleBody body = Jimple.v().newBody(sootMethod());
            sootMethod().setActiveBody(body);
            Body b = new Body(hostType(), body, this);
            b.setLine(this);
            for (int i = 0; i < getNumParameter(); i++) {
                getParameter(i).jimplify2(b);
            }
            boolean needsInit = true;
            if (hasConstructorInvocation()) {
                getConstructorInvocation().jimplify2(b);
                Stmt stmt = getConstructorInvocation();
                if (stmt instanceof ExprStmt) {
                    ExprStmt exprStmt = (ExprStmt) stmt;
                    Expr expr = exprStmt.getExpr();
                    if (!expr.isSuperConstructorAccess()) {
                        needsInit = false;
                    }
                }
            }
            if (hostType().needsEnclosing()) {
                TypeDecl type = hostType().enclosingType();
                b.add(Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(b.emitThis(hostType()), hostType().getSootField("this$0", type).makeRef()), asLocal(b, Jimple.v().newParameterRef(type.getSootType(), 0))));
            }
            for (Variable v : hostType().enclosingVariables()) {
                ParameterDeclaration p = (ParameterDeclaration) parameterDeclaration("val$" + v.name()).iterator().next();
                b.add(Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(b.emitThis(hostType()), Scene.v().makeFieldRef(hostType().getSootClassDecl(), "val$" + v.name(), v.type().getSootType(), false)), p.local));
            }
            if (needsInit) {
                TypeDecl typeDecl = hostType();
                for (int i2 = 0; i2 < typeDecl.getNumBodyDecl(); i2++) {
                    BodyDecl bodyDecl = typeDecl.getBodyDecl(i2);
                    if ((bodyDecl instanceof FieldDeclaration) && bodyDecl.generate()) {
                        FieldDeclaration f = (FieldDeclaration) bodyDecl;
                        if (!f.isStatic() && f.hasInit()) {
                            Local base = b.emitThis(hostType());
                            Local l = asLocal(b, f.getInit().type().emitCastTo(b, f.getInit(), f.type()), f.type().getSootType());
                            b.setLine(f);
                            b.add(Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(base, f.sootRef()), l));
                        }
                    } else if ((bodyDecl instanceof InstanceInitializer) && bodyDecl.generate()) {
                        bodyDecl.jimplify2(b);
                    }
                }
            }
            getBlock().jimplify2(b);
            b.add(Jimple.v().newReturnVoidStmt());
        }
    }

    private boolean refined_ConstructorDecl_ConstructorDecl_moreSpecificThan_ConstructorDecl(ConstructorDecl m) {
        for (int i = 0; i < getNumParameter(); i++) {
            if (!getParameter(i).type().instanceOf(m.getParameter(i).type())) {
                return false;
            }
        }
        return true;
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
        if (!hostType().accessibleFrom(type)) {
            return false;
        }
        if (isPublic() || isProtected()) {
            return true;
        }
        if (isPrivate()) {
            return hostType().topLevelType() == type.topLevelType();
        }
        return hostPackage().equals(type.hostPackage());
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
        return getBlock().isDAafter(v) && getBlock().checkReturnDA(v);
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
        return getBlock().isDUafter(v) && getBlock().checkReturnDU(v);
    }

    public boolean throwsException(TypeDecl exceptionType) {
        if (this.throwsException_TypeDecl_values == null) {
            this.throwsException_TypeDecl_values = new HashMap(4);
        }
        if (this.throwsException_TypeDecl_values.containsKey(exceptionType)) {
            return ((Boolean) this.throwsException_TypeDecl_values.get(exceptionType)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean throwsException_TypeDecl_value = throwsException_compute(exceptionType);
        if (isFinal && num == state().boundariesCrossed) {
            this.throwsException_TypeDecl_values.put(exceptionType, Boolean.valueOf(throwsException_TypeDecl_value));
        }
        return throwsException_TypeDecl_value;
    }

    private boolean throwsException_compute(TypeDecl exceptionType) {
        for (int i = 0; i < getNumException(); i++) {
            if (exceptionType.instanceOf(getException(i).type())) {
                return true;
            }
        }
        return false;
    }

    public String name() {
        if (this.name_computed) {
            return this.name_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.name_value = name_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.name_computed = true;
        }
        return this.name_value;
    }

    private String name_compute() {
        return getID();
    }

    public String signature() {
        if (this.signature_computed) {
            return this.signature_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.signature_value = signature_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.signature_computed = true;
        }
        return this.signature_value;
    }

    private String signature_compute() {
        StringBuffer s = new StringBuffer();
        s.append(String.valueOf(name()) + "(");
        for (int i = 0; i < getNumParameter(); i++) {
            s.append(getParameter(i));
            if (i != getNumParameter() - 1) {
                s.append(", ");
            }
        }
        s.append(")");
        return s.toString();
    }

    public boolean sameSignature(ConstructorDecl c) {
        if (this.sameSignature_ConstructorDecl_values == null) {
            this.sameSignature_ConstructorDecl_values = new HashMap(4);
        }
        if (this.sameSignature_ConstructorDecl_values.containsKey(c)) {
            return ((Boolean) this.sameSignature_ConstructorDecl_values.get(c)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean sameSignature_ConstructorDecl_value = sameSignature_compute(c);
        if (isFinal && num == state().boundariesCrossed) {
            this.sameSignature_ConstructorDecl_values.put(c, Boolean.valueOf(sameSignature_ConstructorDecl_value));
        }
        return sameSignature_ConstructorDecl_value;
    }

    private boolean sameSignature_compute(ConstructorDecl c) {
        if (!name().equals(c.name()) || c.getNumParameter() != getNumParameter()) {
            return false;
        }
        for (int i = 0; i < getNumParameter(); i++) {
            if (!c.getParameter(i).type().equals(getParameter(i).type())) {
                return false;
            }
        }
        return true;
    }

    public boolean moreSpecificThan(ConstructorDecl m) {
        if (this.moreSpecificThan_ConstructorDecl_values == null) {
            this.moreSpecificThan_ConstructorDecl_values = new HashMap(4);
        }
        if (this.moreSpecificThan_ConstructorDecl_values.containsKey(m)) {
            return ((Boolean) this.moreSpecificThan_ConstructorDecl_values.get(m)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean moreSpecificThan_ConstructorDecl_value = moreSpecificThan_compute(m);
        if (isFinal && num == state().boundariesCrossed) {
            this.moreSpecificThan_ConstructorDecl_values.put(m, Boolean.valueOf(moreSpecificThan_ConstructorDecl_value));
        }
        return moreSpecificThan_ConstructorDecl_value;
    }

    private boolean moreSpecificThan_compute(ConstructorDecl m) {
        if (!isVariableArity() && !m.isVariableArity()) {
            return refined_ConstructorDecl_ConstructorDecl_moreSpecificThan_ConstructorDecl(m);
        }
        int num = Math.max(getNumParameter(), m.getNumParameter());
        int i = 0;
        while (i < num) {
            TypeDecl t1 = i < getNumParameter() - 1 ? getParameter(i).type() : getParameter(getNumParameter() - 1).type().componentType();
            TypeDecl t2 = i < m.getNumParameter() - 1 ? m.getParameter(i).type() : m.getParameter(m.getNumParameter() - 1).type().componentType();
            if (t1.instanceOf(t2)) {
                i++;
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean isDefaultConstructor() {
        state();
        return this.isDefaultConstructor;
    }

    public SimpleSet parameterDeclaration(String name) {
        if (this.parameterDeclaration_String_values == null) {
            this.parameterDeclaration_String_values = new HashMap(4);
        }
        if (this.parameterDeclaration_String_values.containsKey(name)) {
            return (SimpleSet) this.parameterDeclaration_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet parameterDeclaration_String_value = parameterDeclaration_compute(name);
        if (isFinal && num == state().boundariesCrossed) {
            this.parameterDeclaration_String_values.put(name, parameterDeclaration_String_value);
        }
        return parameterDeclaration_String_value;
    }

    private SimpleSet parameterDeclaration_compute(String name) {
        for (int i = 0; i < getNumParameter(); i++) {
            if (getParameter(i).name().equals(name)) {
                return getParameter(i);
            }
        }
        return SimpleSet.emptySet;
    }

    public boolean isSynthetic() {
        state();
        return getModifiers().isSynthetic();
    }

    public boolean isPublic() {
        state();
        return getModifiers().isPublic();
    }

    public boolean isPrivate() {
        state();
        return getModifiers().isPrivate();
    }

    public boolean isProtected() {
        state();
        return getModifiers().isProtected();
    }

    public boolean circularThisInvocation(ConstructorDecl decl) {
        ASTNode.State.CircularValue _value;
        boolean new_circularThisInvocation_ConstructorDecl_value;
        if (this.circularThisInvocation_ConstructorDecl_values == null) {
            this.circularThisInvocation_ConstructorDecl_values = new HashMap(4);
        }
        if (this.circularThisInvocation_ConstructorDecl_values.containsKey(decl)) {
            Object _o = this.circularThisInvocation_ConstructorDecl_values.get(decl);
            if (!(_o instanceof ASTNode.State.CircularValue)) {
                return ((Boolean) _o).booleanValue();
            }
            _value = (ASTNode.State.CircularValue) _o;
        } else {
            _value = new ASTNode.State.CircularValue();
            this.circularThisInvocation_ConstructorDecl_values.put(decl, _value);
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
                new_circularThisInvocation_ConstructorDecl_value = circularThisInvocation_compute(decl);
                if (new_circularThisInvocation_ConstructorDecl_value != ((Boolean) _value.value).booleanValue()) {
                    state.CHANGE = true;
                    _value.value = Boolean.valueOf(new_circularThisInvocation_ConstructorDecl_value);
                }
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            if (!isFinal || num != state().boundariesCrossed) {
                this.circularThisInvocation_ConstructorDecl_values.remove(decl);
                state.RESET_CYCLE = true;
                circularThisInvocation_compute(decl);
                state.RESET_CYCLE = false;
            } else {
                this.circularThisInvocation_ConstructorDecl_values.put(decl, Boolean.valueOf(new_circularThisInvocation_ConstructorDecl_value));
            }
            state.IN_CIRCLE = false;
            return new_circularThisInvocation_ConstructorDecl_value;
        } else if (!new Integer(state.CIRCLE_INDEX).equals(Integer.valueOf(_value.visited))) {
            _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
            boolean new_circularThisInvocation_ConstructorDecl_value2 = circularThisInvocation_compute(decl);
            if (state.RESET_CYCLE) {
                this.circularThisInvocation_ConstructorDecl_values.remove(decl);
            } else if (new_circularThisInvocation_ConstructorDecl_value2 != ((Boolean) _value.value).booleanValue()) {
                state.CHANGE = true;
                _value.value = Boolean.valueOf(new_circularThisInvocation_ConstructorDecl_value2);
            }
            return new_circularThisInvocation_ConstructorDecl_value2;
        } else {
            return ((Boolean) _value.value).booleanValue();
        }
    }

    private boolean circularThisInvocation_compute(ConstructorDecl decl) {
        if (hasConstructorInvocation()) {
            Expr e = ((ExprStmt) getConstructorInvocation()).getExpr();
            if (e instanceof ConstructorAccess) {
                ConstructorDecl constructorDecl = ((ConstructorAccess) e).decl();
                if (constructorDecl == decl) {
                    return true;
                }
                return constructorDecl.circularThisInvocation(decl);
            }
            return false;
        }
        return false;
    }

    public TypeDecl type() {
        state();
        return unknownType();
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean isVoid() {
        state();
        return true;
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

    public ConstructorDecl sourceConstructorDecl() {
        if (this.sourceConstructorDecl_computed) {
            return this.sourceConstructorDecl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.sourceConstructorDecl_value = sourceConstructorDecl_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.sourceConstructorDecl_computed = true;
        }
        return this.sourceConstructorDecl_value;
    }

    private ConstructorDecl sourceConstructorDecl_compute() {
        return this;
    }

    public boolean applicableBySubtyping(List argList) {
        state();
        if (getNumParameter() != argList.getNumChild()) {
            return false;
        }
        for (int i = 0; i < getNumParameter(); i++) {
            TypeDecl arg = ((Expr) argList.getChild(i)).type();
            if (!arg.instanceOf(getParameter(i).type())) {
                return false;
            }
        }
        return true;
    }

    public boolean applicableByMethodInvocationConversion(List argList) {
        state();
        if (getNumParameter() != argList.getNumChild()) {
            return false;
        }
        for (int i = 0; i < getNumParameter(); i++) {
            TypeDecl arg = ((Expr) argList.getChild(i)).type();
            if (!arg.methodInvocationConversionTo(getParameter(i).type())) {
                return false;
            }
        }
        return true;
    }

    public boolean applicableVariableArity(List argList) {
        state();
        for (int i = 0; i < getNumParameter() - 1; i++) {
            TypeDecl arg = ((Expr) argList.getChild(i)).type();
            if (!arg.methodInvocationConversionTo(getParameter(i).type())) {
                return false;
            }
        }
        for (int i2 = getNumParameter() - 1; i2 < argList.getNumChild(); i2++) {
            TypeDecl arg2 = ((Expr) argList.getChild(i2)).type();
            if (!arg2.methodInvocationConversionTo(lastParameter().type().componentType())) {
                return false;
            }
        }
        return true;
    }

    public boolean potentiallyApplicable(List argList) {
        state();
        if (isVariableArity() && argList.getNumChild() < arity() - 1) {
            return false;
        }
        if (!isVariableArity() && arity() != argList.getNumChild()) {
            return false;
        }
        return true;
    }

    public int arity() {
        state();
        return getNumParameter();
    }

    public boolean isVariableArity() {
        state();
        if (getNumParameter() == 0) {
            return false;
        }
        return getParameter(getNumParameter() - 1).isVariableArity();
    }

    public ParameterDeclaration lastParameter() {
        state();
        return getParameter(getNumParameter() - 1);
    }

    public boolean needsEnclosing() {
        state();
        return hostType().needsEnclosing();
    }

    public boolean needsSuperEnclosing() {
        state();
        return hostType().needsSuperEnclosing();
    }

    public TypeDecl enclosing() {
        state();
        return hostType().enclosing();
    }

    public TypeDecl superEnclosing() {
        state();
        return hostType().superEnclosing();
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
        return result;
    }

    public SootMethod sootMethod() {
        if (this.sootMethod_computed) {
            return this.sootMethod_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.sootMethod_value = sootMethod_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.sootMethod_computed = true;
        }
        return this.sootMethod_value;
    }

    private SootMethod sootMethod_compute() {
        ArrayList list = new ArrayList();
        TypeDecl typeDecl = hostType();
        if (typeDecl.needsEnclosing()) {
            list.add(typeDecl.enclosingType().getSootType());
        }
        if (typeDecl.needsSuperEnclosing()) {
            TypeDecl superClass = ((ClassDecl) typeDecl).superclass();
            list.add(superClass.enclosingType().getSootType());
        }
        for (int i = 0; i < getNumParameter(); i++) {
            list.add(getParameter(i).type().getSootType());
        }
        return hostType().getSootClassDecl().getMethod("<init>", list, soot.VoidType.v());
    }

    public SootMethodRef sootRef() {
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

    private SootMethodRef sootRef_compute() {
        ArrayList parameters = new ArrayList();
        TypeDecl typeDecl = hostType();
        if (typeDecl.needsEnclosing()) {
            parameters.add(typeDecl.enclosingType().getSootType());
        }
        if (typeDecl.needsSuperEnclosing()) {
            TypeDecl superClass = ((ClassDecl) typeDecl).superclass();
            parameters.add(superClass.enclosingType().getSootType());
        }
        for (int i = 0; i < getNumParameter(); i++) {
            parameters.add(getParameter(i).type().getSootType());
        }
        SootMethodRef ref = Scene.v().makeConstructorRef(hostType().getSootClassDecl(), parameters);
        return ref;
    }

    public int localNumOfFirstParameter() {
        if (this.localNumOfFirstParameter_computed) {
            return this.localNumOfFirstParameter_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.localNumOfFirstParameter_value = localNumOfFirstParameter_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.localNumOfFirstParameter_computed = true;
        }
        return this.localNumOfFirstParameter_value;
    }

    private int localNumOfFirstParameter_compute() {
        int i = 0;
        if (hostType().needsEnclosing()) {
            i = 0 + 1;
        }
        if (hostType().needsSuperEnclosing()) {
            i++;
        }
        return i;
    }

    public int offsetFirstEnclosingVariable() {
        if (this.offsetFirstEnclosingVariable_computed) {
            return this.offsetFirstEnclosingVariable_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.offsetFirstEnclosingVariable_value = offsetFirstEnclosingVariable_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.offsetFirstEnclosingVariable_computed = true;
        }
        return this.offsetFirstEnclosingVariable_value;
    }

    private int offsetFirstEnclosingVariable_compute() {
        return getNumParameter() == 0 ? localNumOfFirstParameter() : getParameter(getNumParameter() - 1).localNum() + getParameter(getNumParameter() - 1).type().variableSize();
    }

    public ConstructorDecl erasedConstructor() {
        state();
        return this;
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean hasAnnotationSafeVarargs() {
        state();
        return getModifiers().hasAnnotationSafeVarargs();
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean hasIllegalAnnotationSafeVarargs() {
        state();
        return hasAnnotationSafeVarargs() && !isVariableArity();
    }

    public boolean handlesException(TypeDecl exceptionType) {
        if (this.handlesException_TypeDecl_values == null) {
            this.handlesException_TypeDecl_values = new HashMap(4);
        }
        if (this.handlesException_TypeDecl_values.containsKey(exceptionType)) {
            return ((Boolean) this.handlesException_TypeDecl_values.get(exceptionType)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean handlesException_TypeDecl_value = getParent().Define_boolean_handlesException(this, null, exceptionType);
        if (isFinal && num == state().boundariesCrossed) {
            this.handlesException_TypeDecl_values.put(exceptionType, Boolean.valueOf(handlesException_TypeDecl_value));
        }
        return handlesException_TypeDecl_value;
    }

    public TypeDecl unknownType() {
        state();
        TypeDecl unknownType_value = getParent().Define_TypeDecl_unknownType(this, null);
        return unknownType_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getBlockNoTransform()) {
            return hasConstructorInvocation() ? getConstructorInvocation().isDAafter(v) : isDAbefore(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getBlockNoTransform()) {
            return hasConstructorInvocation() ? getConstructorInvocation().isDUafter(v) : isDUbefore(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
        if (caller == getConstructorInvocationOptNoTransform()) {
            return throwsException(exceptionType) || handlesException(exceptionType);
        } else if (caller == getBlockNoTransform()) {
            return throwsException(exceptionType) || handlesException(exceptionType);
        } else {
            return getParent().Define_boolean_handlesException(this, caller, exceptionType);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
        if (caller == getConstructorInvocationOptNoTransform()) {
            Collection c = new ArrayList();
            for (MethodDecl m : lookupMethod(name)) {
                if (!hostType().memberMethods(name).contains(m) || m.isStatic()) {
                    c.add(m);
                }
            }
            return c;
        }
        return getParent().Define_Collection_lookupMethod(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getParameterListNoTransform()) {
            caller.getIndexOfChild(child);
            return parameterDeclaration(name);
        } else if (caller == getConstructorInvocationOptNoTransform()) {
            SimpleSet set = parameterDeclaration(name);
            if (!set.isEmpty()) {
                return set;
            }
            for (Variable v : lookupVariable(name)) {
                if (!hostType().memberFields(name).contains(v) || v.isStatic()) {
                    set = set.add(v);
                }
            }
            return set;
        } else if (caller == getBlockNoTransform()) {
            SimpleSet set2 = parameterDeclaration(name);
            if (!set2.isEmpty()) {
                return set2;
            }
            return lookupVariable(name);
        } else {
            return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
        }
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
    public ASTNode Define_ASTNode_enclosingBlock(ASTNode caller, ASTNode child) {
        if (caller == getBlockNoTransform()) {
            return this;
        }
        return getParent().Define_ASTNode_enclosingBlock(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getConstructorInvocationOptNoTransform()) {
            return NameType.EXPRESSION_NAME;
        }
        if (caller == getExceptionListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.TYPE_NAME;
        } else if (caller == getParameterListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.TYPE_NAME;
        } else {
            return getParent().Define_NameType_nameType(this, caller);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
        if (caller == getConstructorInvocationOptNoTransform()) {
            return unknownType();
        }
        return getParent().Define_TypeDecl_enclosingInstance(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inExplicitConstructorInvocation(ASTNode caller, ASTNode child) {
        if (caller == getConstructorInvocationOptNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_inExplicitConstructorInvocation(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
        if (caller == getConstructorInvocationOptNoTransform() || caller == getBlockNoTransform()) {
            return false;
        }
        return getParent().Define_boolean_inStaticContext(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
        if (caller == getBlockNoTransform()) {
            if (hasConstructorInvocation()) {
                return getConstructorInvocation().canCompleteNormally();
            }
            return true;
        } else if (caller == getConstructorInvocationOptNoTransform()) {
            return true;
        } else {
            return getParent().Define_boolean_reachable(this, caller);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isMethodParameter(ASTNode caller, ASTNode child) {
        if (caller == getParameterListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_isMethodParameter(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isConstructorParameter(ASTNode caller, ASTNode child) {
        if (caller == getParameterListNoTransform()) {
            caller.getIndexOfChild(child);
            return true;
        }
        return getParent().Define_boolean_isConstructorParameter(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isExceptionHandlerParameter(ASTNode caller, ASTNode child) {
        if (caller == getParameterListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_isExceptionHandlerParameter(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
        if (caller == getModifiersNoTransform()) {
            return name.equals("CONSTRUCTOR");
        }
        return getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_variableArityValid(ASTNode caller, ASTNode child) {
        if (caller == getParameterListNoTransform()) {
            int i = caller.getIndexOfChild(child);
            return i == getNumParameter() - 1;
        }
        return getParent().Define_boolean_variableArityValid(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public int Define_int_localNum(ASTNode caller, ASTNode child) {
        if (caller == getParameterListNoTransform()) {
            int index = caller.getIndexOfChild(child);
            if (index == 0) {
                return localNumOfFirstParameter();
            }
            return getParameter(index - 1).localNum() + getParameter(index - 1).type().variableSize();
        }
        return getParent().Define_int_localNum(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_enclosedByExceptionHandler(ASTNode caller, ASTNode child) {
        if (caller == getBlockNoTransform()) {
            return getNumException() != 0;
        }
        return getParent().Define_boolean_enclosedByExceptionHandler(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inhModifiedInScope(ASTNode caller, ASTNode child, Variable var) {
        if (caller == getParameterListNoTransform()) {
            caller.getIndexOfChild(child);
            return getBlock().modifiedInScope(var);
        }
        return getParent().Define_boolean_inhModifiedInScope(this, caller, var);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isCatchParam(ASTNode caller, ASTNode child) {
        if (caller == getParameterListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_isCatchParam(this, caller);
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        if (!hasConstructorInvocation() && !hostType().isObject()) {
            state().duringImplicitConstructor++;
            ASTNode result = rewriteRule0();
            state().duringImplicitConstructor--;
            return result;
        }
        return super.rewriteTo();
    }

    private ConstructorDecl rewriteRule0() {
        setConstructorInvocation(new ExprStmt(new SuperConstructorAccess("super", new List())));
        return this;
    }
}
