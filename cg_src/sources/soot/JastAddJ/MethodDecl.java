package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.JastAddJ.SimpleSet;
import soot.Scene;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.coffi.CoffiMethodSource;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.tagkit.AnnotationTag;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/MethodDecl.class */
public class MethodDecl extends MemberDecl implements Cloneable, SimpleSet, Iterator {
    private MethodDecl iterElem;
    public SootMethod sootMethod;
    protected String tokenString_ID;
    public int IDstart;
    public int IDend;
    protected Map accessibleFrom_TypeDecl_values;
    protected Map throwsException_TypeDecl_values;
    protected String signature_value;
    protected Map moreSpecificThan_MethodDecl_values;
    protected Map overrides_MethodDecl_values;
    protected Map hides_MethodDecl_values;
    protected Map parameterDeclaration_String_values;
    protected TypeDecl type_value;
    protected boolean usesTypeVariable_value;
    protected MethodDecl sourceMethodDecl_value;
    protected SootMethod sootMethod_value;
    protected SootMethodRef sootRef_value;
    protected int offsetBeforeParameters_value;
    protected int offsetAfterParameters_value;
    protected Map handlesException_TypeDecl_values;
    protected boolean signature_computed = false;
    protected boolean type_computed = false;
    protected boolean usesTypeVariable_computed = false;
    protected boolean sourceMethodDecl_computed = false;
    protected boolean sootMethod_computed = false;
    protected boolean sootRef_computed = false;
    protected boolean offsetBeforeParameters_computed = false;
    protected boolean offsetAfterParameters_computed = false;

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.accessibleFrom_TypeDecl_values = null;
        this.throwsException_TypeDecl_values = null;
        this.signature_computed = false;
        this.signature_value = null;
        this.moreSpecificThan_MethodDecl_values = null;
        this.overrides_MethodDecl_values = null;
        this.hides_MethodDecl_values = null;
        this.parameterDeclaration_String_values = null;
        this.type_computed = false;
        this.type_value = null;
        this.usesTypeVariable_computed = false;
        this.sourceMethodDecl_computed = false;
        this.sourceMethodDecl_value = null;
        this.sootMethod_computed = false;
        this.sootMethod_value = null;
        this.sootRef_computed = false;
        this.sootRef_value = null;
        this.offsetBeforeParameters_computed = false;
        this.offsetAfterParameters_computed = false;
        this.handlesException_TypeDecl_values = null;
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public MethodDecl clone() throws CloneNotSupportedException {
        MethodDecl node = (MethodDecl) super.clone();
        node.accessibleFrom_TypeDecl_values = null;
        node.throwsException_TypeDecl_values = null;
        node.signature_computed = false;
        node.signature_value = null;
        node.moreSpecificThan_MethodDecl_values = null;
        node.overrides_MethodDecl_values = null;
        node.hides_MethodDecl_values = null;
        node.parameterDeclaration_String_values = null;
        node.type_computed = false;
        node.type_value = null;
        node.usesTypeVariable_computed = false;
        node.sourceMethodDecl_computed = false;
        node.sourceMethodDecl_value = null;
        node.sootMethod_computed = false;
        node.sootMethod_value = null;
        node.sootRef_computed = false;
        node.sootRef_value = null;
        node.offsetBeforeParameters_computed = false;
        node.offsetAfterParameters_computed = false;
        node.handlesException_TypeDecl_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            MethodDecl node = clone();
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

    public Access createBoundAccess(List args) {
        if (isStatic()) {
            return hostType().createQualifiedAccess().qualifiesAccess(new BoundMethodAccess(name(), args, this));
        }
        return new BoundMethodAccess(name(), args, this);
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
    public void nameCheck() {
        if (!hostType().methodsSignature(signature()).contains(this)) {
            error("method with signature " + signature() + " is multiply declared in type " + hostType().typeName());
        }
        if (isNative() && hasBlock()) {
            error("native methods must have an empty semicolon body");
        }
        if (isAbstract() && hasBlock()) {
            error("abstract methods must have an empty semicolon body");
        }
        if (!hasBlock() && !isNative() && !isAbstract()) {
            error("only abstract and native methods may have an empty semicolon body");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        getModifiers().toString(s);
        getTypeAccess().toString(s);
        s.append(Instruction.argsep + name() + "(");
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
        if (hasBlock()) {
            s.append(Instruction.argsep);
            getBlock().toString(s);
            return;
        }
        s.append(";");
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
        if (!isVoid() && hasBlock() && getBlock().canCompleteNormally()) {
            error("the body of a non void method may not complete normally");
        }
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [soot.JastAddJ.Modifiers] */
    @Override // soot.JastAddJ.BodyDecl
    public BodyDecl substitutedBodyDecl(Parameterization parTypeDecl) {
        MethodDecl m = new MethodDeclSubstituted((Modifiers) getModifiers().fullCopy2(), getTypeAccess().type().substituteReturnType(parTypeDecl), getID(), getParameterList().substitute(parTypeDecl), getExceptionList().substitute(parTypeDecl), substituteBody(parTypeDecl), this);
        return m;
    }

    public Opt substituteBody(Parameterization parTypeDecl) {
        return new Opt();
    }

    public MethodDecl createAccessor(TypeDecl methodQualifier) {
        MethodDecl m = (MethodDecl) methodQualifier.getAccessor(this, "method");
        if (m != null) {
            return m;
        }
        int accessorIndex = methodQualifier.accessorCounter;
        methodQualifier.accessorCounter = accessorIndex + 1;
        List parameterList = new List();
        for (int i = 0; i < getNumParameter(); i++) {
            parameterList.add(new ParameterDeclaration(getParameter(i).type().createQualifiedAccess(), getParameter(i).name()));
        }
        List exceptionList = new List();
        for (int i2 = 0; i2 < getNumException(); i2++) {
            exceptionList.add((Access) getException(i2).fullCopy());
        }
        Modifiers modifiers = new Modifiers(new List());
        if (getModifiers().isStatic()) {
            modifiers.addModifier(new Modifier(Jimple.STATIC));
        }
        modifiers.addModifier(new Modifier("synthetic"));
        modifiers.addModifier(new Modifier(Jimple.PUBLIC));
        MethodDecl m2 = methodQualifier.addMemberMethod(new MethodDecl(modifiers, getTypeAccess().type().createQualifiedAccess(), String.valueOf(name()) + "$access$" + accessorIndex, parameterList, exceptionList, new Opt(new Block(new List().add(createAccessorStmt())))));
        methodQualifier.addAccessor(this, "method", m2);
        return m2;
    }

    private Stmt createAccessorStmt() {
        List argumentList = new List();
        for (int i = 0; i < getNumParameter(); i++) {
            argumentList.add(new VarAccess(getParameter(i).name()));
        }
        Access access = new BoundMethodAccess(name(), argumentList, this);
        if (!isStatic()) {
            access = new ThisAccess("this").qualifiesAccess(access);
        }
        return isVoid() ? new ExprStmt(access) : new ReturnStmt(new Opt(access));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v19, types: [soot.JastAddJ.ExprStmt] */
    public MethodDecl createSuperAccessor(TypeDecl methodQualifier) {
        ReturnStmt returnStmt;
        MethodDecl m = (MethodDecl) methodQualifier.getAccessor(this, "method_super");
        if (m != null) {
            return m;
        }
        int accessorIndex = methodQualifier.accessorCounter;
        methodQualifier.accessorCounter = accessorIndex + 1;
        List parameters = new List();
        List args = new List();
        for (int i = 0; i < getNumParameter(); i++) {
            parameters.add(new ParameterDeclaration(getParameter(i).type(), getParameter(i).name()));
            args.add(new VarAccess(getParameter(i).name()));
        }
        if (type().isVoid()) {
            returnStmt = new ExprStmt(new SuperAccess("super").qualifiesAccess(new MethodAccess(name(), args)));
        } else {
            returnStmt = new ReturnStmt(new Opt(new SuperAccess("super").qualifiesAccess(new MethodAccess(name(), args))));
        }
        MethodDecl m2 = methodQualifier.addMemberMethod(new MethodDecl(new Modifiers(new List().add(new Modifier("synthetic"))), type().createQualifiedAccess(), String.valueOf(name()) + "$access$" + accessorIndex, parameters, new List(), new Opt(new Block(new List().add(returnStmt)))));
        methodQualifier.addAccessor(this, "method_super", m2);
        return m2;
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void jimplify1phase2() {
        String name = name();
        ArrayList parameters = new ArrayList();
        ArrayList paramnames = new ArrayList();
        for (int i = 0; i < getNumParameter(); i++) {
            parameters.add(getParameter(i).type().getSootType());
            paramnames.add(getParameter(i).name());
        }
        Type returnType = type().getSootType();
        int modifiers = sootTypeModifiers();
        ArrayList throwtypes = new ArrayList();
        for (int i2 = 0; i2 < getNumException(); i2++) {
            throwtypes.add(getException(i2).type().getSootClassDecl());
        }
        String signature = SootMethod.getSubSignature(name, parameters, returnType);
        if (!hostType().getSootClassDecl().declaresMethod(signature)) {
            SootMethod m = Scene.v().makeSootMethod(name, parameters, returnType, modifiers, throwtypes);
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

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v53, types: [soot.JastAddJ.ExprStmt] */
    @Override // soot.JastAddJ.ASTNode
    public void transformation() {
        ReturnStmt returnStmt;
        super.transformation();
        HashSet processed = new HashSet();
        for (MethodDecl m : hostType().bridgeCandidates(signature())) {
            if (overrides(m)) {
                MethodDecl erased = m.erasedMethod();
                if (!erased.signature().equals(signature()) || erased.type().erasure() != type().erasure()) {
                    StringBuffer keyBuffer = new StringBuffer();
                    for (int i = 0; i < getNumParameter(); i++) {
                        keyBuffer.append(erased.getParameter(i).type().erasure().fullName());
                    }
                    keyBuffer.append(erased.type().erasure().fullName());
                    String key = keyBuffer.toString();
                    if (!processed.contains(key)) {
                        processed.add(key);
                        List args = new List();
                        List parameters = new List();
                        for (int i2 = 0; i2 < getNumParameter(); i2++) {
                            args.add(new CastExpr(getParameter(i2).type().erasure().createBoundAccess(), new VarAccess("p" + i2)));
                            parameters.add(new ParameterDeclaration(erased.getParameter(i2).type().erasure(), "p" + i2));
                        }
                        if (type().isVoid()) {
                            returnStmt = new ExprStmt(createBoundAccess(args));
                        } else {
                            returnStmt = new ReturnStmt(createBoundAccess(args));
                        }
                        List modifiersList = new List();
                        if (isPublic()) {
                            modifiersList.add(new Modifier(Jimple.PUBLIC));
                        } else if (isProtected()) {
                            modifiersList.add(new Modifier(Jimple.PROTECTED));
                        } else if (isPrivate()) {
                            modifiersList.add(new Modifier(Jimple.PRIVATE));
                        }
                        MethodDecl bridge = new BridgeMethodDecl(new Modifiers(modifiersList), erased.type().erasure().createBoundAccess(), erased.name(), parameters, getExceptionList().fullCopy(), new Opt(new Block(new List().add(returnStmt))));
                        hostType().addBodyDecl(bridge);
                    }
                }
            }
        }
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void checkWarnings() {
        super.checkWarnings();
        if (!suppressWarnings("unchecked") && !hasAnnotationSafeVarargs() && isVariableArity() && !getParameter(getNumParameter() - 1).type().isReifiable()) {
            warning("possible heap pollution for variable arity parameter");
        }
    }

    public MethodDecl() {
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[5];
        setChild(new List(), 2);
        setChild(new List(), 3);
        setChild(new Opt(), 4);
    }

    public MethodDecl(Modifiers p0, Access p1, String p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
    }

    public MethodDecl(Modifiers p0, Access p1, Symbol p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
        setChild(p4, 3);
        setChild(p5, 4);
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 5;
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

    public void setParameterList(List<ParameterDeclaration> list) {
        setChild(list, 2);
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
        List<ParameterDeclaration> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    public List<ParameterDeclaration> getParameterListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    public void setExceptionList(List<Access> list) {
        setChild(list, 3);
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
        List<Access> list = (List) getChild(3);
        list.getNumChild();
        return list;
    }

    public List<Access> getExceptionListNoTransform() {
        return (List) getChildNoTransform(3);
    }

    public void setBlockOpt(Opt<Block> opt) {
        setChild(opt, 4);
    }

    public boolean hasBlock() {
        return getBlockOpt().getNumChild() != 0;
    }

    public Block getBlock() {
        return getBlockOpt().getChild(0);
    }

    public void setBlock(Block node) {
        getBlockOpt().setChild(node, 0);
    }

    public Opt<Block> getBlockOpt() {
        return (Opt) getChild(4);
    }

    public Opt<Block> getBlockOptNoTransform() {
        return (Opt) getChildNoTransform(4);
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.ASTNode
    public void checkModifiers() {
        super.checkModifiers();
        if (hostType().isClassDecl()) {
            if (!hostType().isEnumDecl() && isAbstract() && !hostType().isAbstract()) {
                error("class must be abstract to include abstract methods");
            }
            if (isAbstract() && isPrivate()) {
                error("method may not be abstract and private");
            }
            if (isAbstract() && isStatic()) {
                error("method may not be abstract and static");
            }
            if (isAbstract() && isSynchronized()) {
                error("method may not be abstract and synchronized");
            }
            if (isAbstract() && isNative()) {
                error("method may not be abstract and native");
            }
            if (isAbstract() && isStrictfp()) {
                error("method may not be abstract and strictfp");
            }
            if (isNative() && isStrictfp()) {
                error("method may not be native and strictfp");
            }
        }
        if (hostType().isInterfaceDecl()) {
            if (isStatic()) {
                error("interface method " + signature() + " in " + hostType().typeName() + " may not be static");
            }
            if (isStrictfp()) {
                error("interface method " + signature() + " in " + hostType().typeName() + " may not be strictfp");
            }
            if (isNative()) {
                error("interface method " + signature() + " in " + hostType().typeName() + " may not be native");
            }
            if (isSynchronized()) {
                error("interface method " + signature() + " in " + hostType().typeName() + " may not be synchronized");
            }
            if (isProtected()) {
                error("interface method " + signature() + " in " + hostType().typeName() + " may not be protected");
            }
            if (isPrivate()) {
                error("interface method " + signature() + " in " + hostType().typeName() + " may not be private");
            } else if (isFinal()) {
                error("interface method " + signature() + " in " + hostType().typeName() + " may not be final");
            }
        }
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void jimplify2() {
        if (generate() && !sootMethod().hasActiveBody()) {
            if (sootMethod().getSource() != null && (sootMethod().getSource() instanceof CoffiMethodSource)) {
                return;
            }
            try {
                if (hasBlock() && !hostType().isInterfaceDecl()) {
                    JimpleBody body = Jimple.v().newBody(sootMethod());
                    sootMethod().setActiveBody(body);
                    Body b = new Body(hostType(), body, this);
                    b.setLine(this);
                    for (int i = 0; i < getNumParameter(); i++) {
                        getParameter(i).jimplify2(b);
                    }
                    getBlock().jimplify2(b);
                    if (type() instanceof VoidType) {
                        b.add(Jimple.v().newReturnVoidStmt());
                    }
                }
            } catch (RuntimeException e) {
                System.err.println("Error generating " + hostType().typeName() + ": " + this);
                throw e;
            }
        }
    }

    private boolean refined_MethodDecl_MethodDecl_moreSpecificThan_MethodDecl(MethodDecl m) {
        if (getNumParameter() == 0) {
            return false;
        }
        for (int i = 0; i < getNumParameter(); i++) {
            if (!getParameter(i).type().instanceOf(m.getParameter(i).type())) {
                return false;
            }
        }
        return true;
    }

    private int refined_EmitJimple_MethodDecl_sootTypeModifiers() {
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
        if (isAbstract()) {
            result |= 1024;
        }
        if (isSynchronized()) {
            result |= 32;
        }
        if (isStrictfp()) {
            result |= 2048;
        }
        if (isNative()) {
            result |= 256;
        }
        return result;
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

    @Override // soot.JastAddJ.ASTNode
    public int lineNumber() {
        state();
        return getLine(this.IDstart);
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
        state();
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
            if (i != 0) {
                s.append(", ");
            }
            s.append(getParameter(i).type().erasure().typeName());
        }
        s.append(")");
        return s.toString();
    }

    public boolean sameSignature(MethodDecl m) {
        state();
        return signature().equals(m.signature());
    }

    public boolean moreSpecificThan(MethodDecl m) {
        if (this.moreSpecificThan_MethodDecl_values == null) {
            this.moreSpecificThan_MethodDecl_values = new HashMap(4);
        }
        if (this.moreSpecificThan_MethodDecl_values.containsKey(m)) {
            return ((Boolean) this.moreSpecificThan_MethodDecl_values.get(m)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean moreSpecificThan_MethodDecl_value = moreSpecificThan_compute(m);
        if (isFinal && num == state().boundariesCrossed) {
            this.moreSpecificThan_MethodDecl_values.put(m, Boolean.valueOf(moreSpecificThan_MethodDecl_value));
        }
        return moreSpecificThan_MethodDecl_value;
    }

    private boolean moreSpecificThan_compute(MethodDecl m) {
        if (!isVariableArity() && !m.isVariableArity()) {
            return refined_MethodDecl_MethodDecl_moreSpecificThan_MethodDecl(m);
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

    public boolean overrides(MethodDecl m) {
        if (this.overrides_MethodDecl_values == null) {
            this.overrides_MethodDecl_values = new HashMap(4);
        }
        if (this.overrides_MethodDecl_values.containsKey(m)) {
            return ((Boolean) this.overrides_MethodDecl_values.get(m)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean overrides_MethodDecl_value = overrides_compute(m);
        if (isFinal && num == state().boundariesCrossed) {
            this.overrides_MethodDecl_values.put(m, Boolean.valueOf(overrides_MethodDecl_value));
        }
        return overrides_MethodDecl_value;
    }

    private boolean overrides_compute(MethodDecl m) {
        return !isStatic() && !m.isPrivate() && m.accessibleFrom(hostType()) && hostType().instanceOf(m.hostType()) && m.signature().equals(signature());
    }

    public boolean hides(MethodDecl m) {
        if (this.hides_MethodDecl_values == null) {
            this.hides_MethodDecl_values = new HashMap(4);
        }
        if (this.hides_MethodDecl_values.containsKey(m)) {
            return ((Boolean) this.hides_MethodDecl_values.get(m)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean hides_MethodDecl_value = hides_compute(m);
        if (isFinal && num == state().boundariesCrossed) {
            this.hides_MethodDecl_values.put(m, Boolean.valueOf(hides_MethodDecl_value));
        }
        return hides_MethodDecl_value;
    }

    private boolean hides_compute(MethodDecl m) {
        return isStatic() && !m.isPrivate() && m.accessibleFrom(hostType()) && hostType().instanceOf(m.hostType()) && m.signature().equals(signature());
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

    public boolean isAbstract() {
        state();
        return getModifiers().isAbstract() || hostType().isInterfaceDecl();
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.Variable
    public boolean isStatic() {
        state();
        return getModifiers().isStatic();
    }

    public boolean isFinal() {
        state();
        return getModifiers().isFinal() || hostType().isFinal() || isPrivate();
    }

    public boolean isSynchronized() {
        state();
        return getModifiers().isSynchronized();
    }

    public boolean isNative() {
        state();
        return getModifiers().isNative();
    }

    public boolean isStrictfp() {
        state();
        return getModifiers().isStrictfp();
    }

    @Override // soot.JastAddJ.ASTNode
    public String dumpString() {
        state();
        return String.valueOf(getClass().getName()) + " [" + getID() + "]";
    }

    public TypeDecl type() {
        if (this.type_computed) {
            return this.type_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.type_value = type_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.type_computed = true;
        }
        return this.type_value;
    }

    private TypeDecl type_compute() {
        return getTypeAccess().type();
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean isVoid() {
        state();
        return type().isVoid();
    }

    public boolean mayOverrideReturn(MethodDecl m) {
        state();
        return type().instanceOf(m.type());
    }

    public boolean annotationMethodOverride() {
        state();
        return !hostType().ancestorMethods(signature()).isEmpty();
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
        return getModifiers().usesTypeVariable() || getTypeAccess().usesTypeVariable() || getParameterList().usesTypeVariable() || getExceptionList().usesTypeVariable();
    }

    public MethodDecl sourceMethodDecl() {
        if (this.sourceMethodDecl_computed) {
            return this.sourceMethodDecl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.sourceMethodDecl_value = sourceMethodDecl_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.sourceMethodDecl_computed = true;
        }
        return this.sourceMethodDecl_value;
    }

    private MethodDecl sourceMethodDecl_compute() {
        return this;
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean visibleTypeParameters() {
        state();
        return !isStatic();
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

    public int sootTypeModifiers() {
        state();
        int res = refined_EmitJimple_MethodDecl_sootTypeModifiers();
        if (isVariableArity()) {
            res |= 128;
        }
        return res;
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
        for (int i = 0; i < getNumParameter(); i++) {
            list.add(getParameter(i).type().getSootType());
        }
        if (hostType().isArrayDecl()) {
            return typeObject().getSootClassDecl().getMethod(name(), list, type().getSootType());
        }
        return hostType().getSootClassDecl().getMethod(name(), list, type().getSootType());
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
        for (int i = 0; i < getNumParameter(); i++) {
            parameters.add(getParameter(i).type().getSootType());
        }
        SootMethodRef ref = Scene.v().makeMethodRef(hostType().getSootClassDecl(), name(), parameters, type().getSootType(), isStatic());
        return ref;
    }

    public int offsetBeforeParameters() {
        if (this.offsetBeforeParameters_computed) {
            return this.offsetBeforeParameters_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.offsetBeforeParameters_value = offsetBeforeParameters_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.offsetBeforeParameters_computed = true;
        }
        return this.offsetBeforeParameters_value;
    }

    private int offsetBeforeParameters_compute() {
        return 0;
    }

    public int offsetAfterParameters() {
        if (this.offsetAfterParameters_computed) {
            return this.offsetAfterParameters_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.offsetAfterParameters_value = offsetAfterParameters_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.offsetAfterParameters_computed = true;
        }
        return this.offsetAfterParameters_value;
    }

    private int offsetAfterParameters_compute() {
        if (getNumParameter() == 0) {
            return offsetBeforeParameters();
        }
        return getParameter(getNumParameter() - 1).localNum() + getParameter(getNumParameter() - 1).type().variableSize();
    }

    public MethodDecl erasedMethod() {
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
        if (hasAnnotationSafeVarargs()) {
            if (isVariableArity()) {
                return (isFinal() || isStatic()) ? false : true;
            }
            return true;
        }
        return false;
    }

    public boolean suppressWarnings(String type) {
        state();
        return hasAnnotationSuppressWarnings(type) || withinSuppressWarnings(type);
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

    public MethodDecl unknownMethod() {
        state();
        MethodDecl unknownMethod_value = getParent().Define_MethodDecl_unknownMethod(this, null);
        return unknownMethod_value;
    }

    public TypeDecl typeObject() {
        state();
        TypeDecl typeObject_value = getParent().Define_TypeDecl_typeObject(this, null);
        return typeObject_value;
    }

    public boolean withinSuppressWarnings(String s) {
        state();
        boolean withinSuppressWarnings_String_value = getParent().Define_boolean_withinSuppressWarnings(this, null, s);
        return withinSuppressWarnings_String_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getBlockOptNoTransform()) {
            if (v.isFinal() && (v.isClassVariable() || v.isInstanceVariable())) {
                return true;
            }
            return isDAbefore(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getBlockOptNoTransform()) {
            return (v.isFinal() && (v.isClassVariable() || v.isInstanceVariable())) ? false : true;
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
        if (caller == getBlockOptNoTransform()) {
            return throwsException(exceptionType) || handlesException(exceptionType);
        }
        return getParent().Define_boolean_handlesException(this, caller, exceptionType);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getParameterListNoTransform()) {
            caller.getIndexOfChild(child);
            return parameterDeclaration(name);
        } else if (caller == getBlockOptNoTransform()) {
            SimpleSet set = parameterDeclaration(name);
            if (!set.isEmpty()) {
                return set;
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
    public boolean Define_boolean_mayBeAbstract(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_mayBeAbstract(this, caller);
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
    public boolean Define_boolean_mayBeSynchronized(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_mayBeSynchronized(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeNative(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_mayBeNative(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeStrictfp(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_mayBeStrictfp(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode Define_ASTNode_enclosingBlock(ASTNode caller, ASTNode child) {
        if (caller == getBlockOptNoTransform()) {
            return this;
        }
        return getParent().Define_ASTNode_enclosingBlock(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getExceptionListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.TYPE_NAME;
        } else if (caller == getParameterListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.TYPE_NAME;
        } else if (caller == getTypeAccessNoTransform()) {
            return NameType.TYPE_NAME;
        } else {
            return getParent().Define_NameType_nameType(this, caller);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_returnType(ASTNode caller, ASTNode child) {
        if (caller == getBlockOptNoTransform()) {
            return type();
        }
        return getParent().Define_TypeDecl_returnType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
        if (caller == getBlockOptNoTransform()) {
            return isStatic();
        }
        return getParent().Define_boolean_inStaticContext(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
        if (caller == getBlockOptNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_reachable(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isMethodParameter(ASTNode caller, ASTNode child) {
        if (caller == getParameterListNoTransform()) {
            caller.getIndexOfChild(child);
            return true;
        }
        return getParent().Define_boolean_isMethodParameter(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isConstructorParameter(ASTNode caller, ASTNode child) {
        if (caller == getParameterListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
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
            return name.equals("METHOD");
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
                return offsetBeforeParameters();
            }
            return getParameter(index - 1).localNum() + getParameter(index - 1).type().variableSize();
        }
        return getParent().Define_int_localNum(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_enclosedByExceptionHandler(ASTNode caller, ASTNode child) {
        if (caller == getBlockOptNoTransform()) {
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

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
