package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.Scene;
import soot.SootMethodRef;
import soot.Type;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/MethodAccess.class */
public class MethodAccess extends Access implements Cloneable {
    protected String tokenString_ID;
    public int IDstart;
    public int IDend;
    protected Map computeDAbefore_int_Variable_values;
    protected boolean exceptionCollection_computed;
    protected Collection exceptionCollection_value;
    protected boolean decls_computed;
    protected SimpleSet decls_value;
    protected boolean decl_computed;
    protected MethodDecl decl_value;
    protected boolean type_computed;
    protected TypeDecl type_value;
    protected Map typeArguments_MethodDecl_values;

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.computeDAbefore_int_Variable_values = null;
        this.exceptionCollection_computed = false;
        this.exceptionCollection_value = null;
        this.decls_computed = false;
        this.decls_value = null;
        this.decl_computed = false;
        this.decl_value = null;
        this.type_computed = false;
        this.type_value = null;
        this.typeArguments_MethodDecl_values = null;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public MethodAccess clone() throws CloneNotSupportedException {
        MethodAccess node = (MethodAccess) super.clone();
        node.computeDAbefore_int_Variable_values = null;
        node.exceptionCollection_computed = false;
        node.exceptionCollection_value = null;
        node.decls_computed = false;
        node.decls_value = null;
        node.decl_computed = false;
        node.decl_value = null;
        node.type_computed = false;
        node.type_value = null;
        node.typeArguments_MethodDecl_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            MethodAccess node = clone();
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.JastAddJ.ASTNode
    public void collectExceptions(Collection c, ASTNode target) {
        super.collectExceptions(c, target);
        for (int i = 0; i < decl().getNumException(); i++) {
            c.add(decl().getException(i).type());
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void exceptionHandling() {
        for (TypeDecl exceptionType : exceptionCollection()) {
            if (!handlesException(exceptionType)) {
                error(decl().hostType().fullName() + "." + this + " invoked in " + hostType().fullName() + " may throw uncaught exception " + exceptionType.fullName());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.JastAddJ.ASTNode
    public boolean reachedException(TypeDecl catchType) {
        for (TypeDecl exceptionType : exceptionCollection()) {
            if (catchType.mayCatch(exceptionType)) {
                return true;
            }
        }
        return super.reachedException(catchType);
    }

    private static SimpleSet removeInstanceMethods(SimpleSet c) {
        SimpleSet set = SimpleSet.emptySet;
        Iterator iter = c.iterator();
        while (iter.hasNext()) {
            MethodDecl m = (MethodDecl) iter.next();
            if (m.isStatic()) {
                set = set.add(m);
            }
        }
        return set;
    }

    public boolean applicable(MethodDecl decl) {
        if (getNumArg() != decl.getNumParameter() || !name().equals(decl.name())) {
            return false;
        }
        for (int i = 0; i < getNumArg(); i++) {
            if (!getArg(i).type().instanceOf(decl.getParameter(i).type())) {
                return false;
            }
        }
        return true;
    }

    public MethodAccess(String name, List args, int start, int end) {
        this(name, args);
        setStart(start);
        setEnd(end);
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(name());
        s.append("(");
        if (getNumArg() > 0) {
            getArg(0).toString(s);
            for (int i = 1; i < getNumArg(); i++) {
                s.append(", ");
                getArg(i).toString(s);
            }
        }
        s.append(")");
    }

    @Override // soot.JastAddJ.ASTNode
    public void nameCheck() {
        if (isQualified() && qualifier().isPackageAccess() && !qualifier().isUnknown()) {
            error("The method " + decl().signature() + " can not be qualified by a package name.");
        }
        if (isQualified() && decl().isAbstract() && qualifier().isSuperAccess()) {
            error("may not access abstract methods in superclass");
        }
        if (decls().isEmpty() && (!isQualified() || !qualifier().isUnknown())) {
            StringBuffer s = new StringBuffer();
            s.append("no method named " + name());
            s.append("(");
            for (int i = 0; i < getNumArg(); i++) {
                if (i != 0) {
                    s.append(", ");
                }
                s.append(getArg(i).type().typeName());
            }
            s.append(") in " + methodHost() + " matches.");
            if (singleCandidateDecl() != null) {
                s.append(" However, there is a method " + singleCandidateDecl().signature());
            }
            error(s.toString());
        }
        if (decls().size() > 1) {
            boolean allAbstract = true;
            Iterator iter = decls().iterator();
            while (iter.hasNext() && allAbstract) {
                MethodDecl m = (MethodDecl) iter.next();
                if (!m.isAbstract() && !m.hostType().isObject()) {
                    allAbstract = false;
                }
            }
            if (!allAbstract && validArgs()) {
                StringBuffer s2 = new StringBuffer();
                s2.append("several most specific methods for " + this + "\n");
                for (MethodDecl m2 : decls()) {
                    s2.append(soot.dava.internal.AST.ASTNode.TAB + m2.signature() + " in " + m2.hostType().typeName() + "\n");
                }
                error(s2.toString());
            }
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void checkModifiers() {
        if (decl().isDeprecated() && !withinDeprecatedAnnotation() && hostType().topLevelType() != decl().hostType().topLevelType() && !withinSuppressWarnings("deprecation")) {
            warning(String.valueOf(decl().signature()) + " in " + decl().hostType().typeName() + " has been deprecated");
        }
    }

    public Collection computeConstraints(GenericMethodDecl decl) {
        Constraints c = new Constraints();
        for (int i = 0; i < decl.original().getNumTypeParameter(); i++) {
            c.addTypeVariable(decl.original().getTypeParameter(i));
        }
        int i2 = 0;
        while (i2 < getNumArg()) {
            TypeDecl A = getArg(i2).type();
            int index = i2 >= decl.getNumParameter() ? decl.getNumParameter() - 1 : i2;
            TypeDecl F = decl.getParameter(index).type();
            if ((decl.getParameter(index) instanceof VariableArityParameterDeclaration) && (getNumArg() != decl.getNumParameter() || !A.isArrayDecl())) {
                F = F.componentType();
            }
            c.convertibleTo(A, F);
            i2++;
        }
        if (c.rawAccess) {
            return new ArrayList();
        }
        c.resolveEqualityConstraints();
        c.resolveSupertypeConstraints();
        if (c.unresolvedTypeArguments()) {
            TypeDecl S = assignConvertedType();
            if (S.isUnboxedPrimitive()) {
                S = S.boxed();
            }
            TypeDecl R = decl.type();
            if (R.isVoid()) {
                R = typeObject();
            }
            c.convertibleFrom(S, R);
            c.resolveEqualityConstraints();
            c.resolveSupertypeConstraints();
            c.resolveSubtypeConstraints();
        }
        return c.typeArguments();
    }

    protected SimpleSet potentiallyApplicable(Collection candidates) {
        SimpleSet potentiallyApplicable = SimpleSet.emptySet;
        Iterator iter = candidates.iterator();
        while (iter.hasNext()) {
            MethodDecl decl = (MethodDecl) iter.next();
            if (potentiallyApplicable(decl) && accessible(decl)) {
                if (decl instanceof GenericMethodDecl) {
                    decl = ((GenericMethodDecl) decl).lookupParMethodDecl(typeArguments(decl));
                }
                potentiallyApplicable = potentiallyApplicable.add(decl);
            }
        }
        return potentiallyApplicable;
    }

    protected SimpleSet applicableBySubtyping(SimpleSet potentiallyApplicable) {
        SimpleSet maxSpecific = SimpleSet.emptySet;
        Iterator iter = potentiallyApplicable.iterator();
        while (iter.hasNext()) {
            MethodDecl decl = (MethodDecl) iter.next();
            if (applicableBySubtyping(decl)) {
                maxSpecific = mostSpecific(maxSpecific, decl);
            }
        }
        return maxSpecific;
    }

    protected SimpleSet applicableByMethodInvocationConversion(SimpleSet potentiallyApplicable, SimpleSet maxSpecific) {
        if (maxSpecific.isEmpty()) {
            Iterator iter = potentiallyApplicable.iterator();
            while (iter.hasNext()) {
                MethodDecl decl = (MethodDecl) iter.next();
                if (applicableByMethodInvocationConversion(decl)) {
                    maxSpecific = mostSpecific(maxSpecific, decl);
                }
            }
        }
        return maxSpecific;
    }

    protected SimpleSet applicableVariableArity(SimpleSet potentiallyApplicable, SimpleSet maxSpecific) {
        if (maxSpecific.isEmpty()) {
            Iterator iter = potentiallyApplicable.iterator();
            while (iter.hasNext()) {
                MethodDecl decl = (MethodDecl) iter.next();
                if (decl.isVariableArity() && applicableVariableArity(decl)) {
                    maxSpecific = mostSpecific(maxSpecific, decl);
                }
            }
        }
        return maxSpecific;
    }

    private static SimpleSet mostSpecific(SimpleSet maxSpecific, MethodDecl decl) {
        if (maxSpecific.isEmpty()) {
            maxSpecific = maxSpecific.add(decl);
        } else if (decl.moreSpecificThan((MethodDecl) maxSpecific.iterator().next())) {
            maxSpecific = SimpleSet.emptySet.add(decl);
        } else if (!((MethodDecl) maxSpecific.iterator().next()).moreSpecificThan(decl)) {
            maxSpecific = maxSpecific.add(decl);
        }
        return maxSpecific;
    }

    private TypeDecl refined_InnerClasses_MethodAccess_methodQualifierType() {
        TypeDecl typeDecl;
        if (hasPrevExpr()) {
            return prevExpr().type();
        }
        TypeDecl hostType = hostType();
        while (true) {
            typeDecl = hostType;
            if (typeDecl == null || typeDecl.hasMethod(name())) {
                break;
            }
            hostType = typeDecl.enclosingType();
        }
        if (typeDecl != null) {
            return typeDecl;
        }
        return decl().hostType();
    }

    public TypeDecl superAccessorTarget() {
        TypeDecl targetDecl = prevExpr().type();
        TypeDecl enclosing = hostType();
        do {
            enclosing = enclosing.enclosingType();
        } while (!enclosing.instanceOf(targetDecl));
        return enclosing;
    }

    public void refined_Transformations_MethodAccess_transformation() {
        MethodDecl m = decl();
        if (requiresAccessor()) {
            super.transformation();
            replace(this).with(decl().createAccessor(methodQualifierType()).createBoundAccess(getArgList()));
            return;
        }
        if (!m.isStatic() && isQualified() && prevExpr().isSuperAccess() && !hostType().instanceOf(prevExpr().type())) {
            decl().createSuperAccessor(superAccessorTarget());
        }
        super.transformation();
    }

    @Override // soot.JastAddJ.ASTNode
    public void checkWarnings() {
        MethodDecl decl = decl();
        if (decl.getNumParameter() != 0 && decl.getNumParameter() <= getNumArg()) {
            ParameterDeclaration param = decl.getParameter(decl.getNumParameter() - 1);
            if (!withinSuppressWarnings("unchecked") && !decl.hasAnnotationSafeVarargs() && param.isVariableArity() && !param.type().isReifiable()) {
                warning("unchecked array creation for variable arity parameter of " + decl().name());
            }
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void collectTypesToSignatures(Collection<Type> set) {
        super.collectTypesToSignatures(set);
        addDependencyIfNeeded(set, methodQualifierType());
    }

    public MethodAccess() {
        this.exceptionCollection_computed = false;
        this.decls_computed = false;
        this.decl_computed = false;
        this.type_computed = false;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
        setChild(new List(), 0);
    }

    public MethodAccess(String p0, List<Expr> p1) {
        this.exceptionCollection_computed = false;
        this.decls_computed = false;
        this.decl_computed = false;
        this.type_computed = false;
        setID(p0);
        setChild(p1, 0);
    }

    public MethodAccess(Symbol p0, List<Expr> p1) {
        this.exceptionCollection_computed = false;
        this.decls_computed = false;
        this.decl_computed = false;
        this.type_computed = false;
        setID(p0);
        setChild(p1, 0);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
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

    public void setArgList(List<Expr> list) {
        setChild(list, 0);
    }

    public int getNumArg() {
        return getArgList().getNumChild();
    }

    public int getNumArgNoTransform() {
        return getArgListNoTransform().getNumChildNoTransform();
    }

    public Expr getArg(int i) {
        return getArgList().getChild(i);
    }

    public void addArg(Expr node) {
        List<Expr> list = (this.parent == null || state == null) ? getArgListNoTransform() : getArgList();
        list.addChild(node);
    }

    public void addArgNoTransform(Expr node) {
        List<Expr> list = getArgListNoTransform();
        list.addChild(node);
    }

    public void setArg(Expr node, int i) {
        List<Expr> list = getArgList();
        list.setChild(node, i);
    }

    public List<Expr> getArgs() {
        return getArgList();
    }

    public List<Expr> getArgsNoTransform() {
        return getArgListNoTransform();
    }

    public List<Expr> getArgList() {
        List<Expr> list = (List) getChild(0);
        list.getNumChild();
        return list;
    }

    public List<Expr> getArgListNoTransform() {
        return (List) getChildNoTransform(0);
    }

    protected SimpleSet maxSpecific(Collection candidates) {
        SimpleSet potentiallyApplicable = potentiallyApplicable(candidates);
        SimpleSet maxSpecific = applicableBySubtyping(potentiallyApplicable);
        return applicableVariableArity(potentiallyApplicable, applicableByMethodInvocationConversion(potentiallyApplicable, maxSpecific));
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (isQualified() && decl().isAbstract() && qualifier().isSuperAccess()) {
            error("may not access abstract methods in superclass");
        }
        if (!decl().isVariableArity() || invokesVariableArityAsArray()) {
            for (int i = 0; i < decl().getNumParameter(); i++) {
                TypeDecl exprType = getArg(i).type();
                TypeDecl parmType = decl().getParameter(i).type();
                if (!exprType.methodInvocationConversionTo(parmType) && !exprType.isUnknown() && !parmType.isUnknown()) {
                    error("#The type " + exprType.typeName() + " of expr " + getArg(i) + " is not compatible with the method parameter " + decl().getParameter(i));
                }
            }
        }
    }

    protected TypeDecl refined_GenericsCodegen_MethodAccess_methodQualifierType() {
        TypeDecl typeDecl = refined_InnerClasses_MethodAccess_methodQualifierType();
        if (typeDecl == null) {
            return null;
        }
        TypeDecl typeDecl2 = typeDecl.erasure();
        MethodDecl m = decl().sourceMethodDecl();
        Collection methods = typeDecl2.memberMethods(m.name());
        if (!methods.contains(decl()) && !methods.contains(m)) {
            return m.hostType();
        }
        return typeDecl2.erasure();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.JastAddJ.ASTNode
    public void transformation() {
        if (decl().isVariableArity() && !invokesVariableArityAsArray()) {
            List list = new List();
            for (int i = 0; i < decl().getNumParameter() - 1; i++) {
                list.add(getArg(i).fullCopy());
            }
            List last = new List();
            for (int i2 = decl().getNumParameter() - 1; i2 < getNumArg(); i2++) {
                last.add(getArg(i2).fullCopy());
            }
            Access typeAccess = decl().lastParameter().type().elementType().createQualifiedAccess();
            for (int i3 = 0; i3 < decl().lastParameter().type().dimension(); i3++) {
                typeAccess = new ArrayTypeAccess(typeAccess);
            }
            list.add(new ArrayCreationExpr(typeAccess, new Opt(new ArrayInit(last))));
            setArgList(list);
        }
        refined_Transformations_MethodAccess_transformation();
    }

    private ArrayList buildArgList(Body b) {
        ArrayList list = new ArrayList();
        for (int i = 0; i < getNumArg(); i++) {
            list.add(asImmediate(b, getArg(i).type().emitCastTo(b, getArg(i), decl().getParameter(i).type())));
        }
        return list;
    }

    @Override // soot.JastAddJ.Expr
    public Value eval(Body b) {
        Value result;
        Value result2;
        MethodDecl decl = decl().erasedMethod();
        if (!decl().isStatic() && isQualified() && prevExpr().isSuperAccess()) {
            Local left = asLocal(b, createLoadQualifier(b));
            ArrayList list = buildArgList(b);
            if (!hostType().instanceOf(prevExpr().type())) {
                MethodDecl m = decl.createSuperAccessor(superAccessorTarget());
                if (methodQualifierType().isInterfaceDecl()) {
                    result2 = b.newInterfaceInvokeExpr(left, m.sootRef(), list, this);
                } else {
                    result2 = b.newVirtualInvokeExpr(left, m.sootRef(), list, this);
                }
            } else {
                result2 = b.newSpecialInvokeExpr(left, sootRef(), list, this);
            }
            if (decl.type() != decl().type()) {
                result2 = decl.type().emitCastTo(b, result2, decl().type(), this);
            }
            return type().isVoid() ? result2 : asLocal(b, result2);
        }
        if (!decl().isStatic()) {
            Local left2 = asLocal(b, createLoadQualifier(b));
            ArrayList list2 = buildArgList(b);
            if (methodQualifierType().isInterfaceDecl()) {
                result = b.newInterfaceInvokeExpr(left2, sootRef(), list2, this);
            } else {
                result = b.newVirtualInvokeExpr(left2, sootRef(), list2, this);
            }
        } else {
            if (isQualified() && !qualifier().isTypeAccess()) {
                b.newTemp(qualifier().eval(b));
            }
            result = b.newStaticInvokeExpr(sootRef(), buildArgList(b), this);
        }
        if (decl.type() != decl().type()) {
            result = decl.type().emitCastTo(b, result, decl().type(), this);
        }
        return type().isVoid() ? result : asLocal(b, result);
    }

    private SootMethodRef sootRef() {
        MethodDecl decl = decl().erasedMethod();
        ArrayList parameters = new ArrayList();
        for (int i = 0; i < decl.getNumParameter(); i++) {
            parameters.add(decl.getParameter(i).type().getSootType());
        }
        SootMethodRef ref = Scene.v().makeMethodRef(methodQualifierType().getSootClassDecl(), decl.name(), parameters, decl.type().getSootType(), decl.isStatic());
        return ref;
    }

    private Value createLoadQualifier(Body b) {
        MethodDecl m = decl().erasedMethod();
        if (hasPrevExpr()) {
            Value v = prevExpr().eval(b);
            if (v == null) {
                throw new Error("Problems evaluating " + prevExpr().getClass().getName());
            }
            Local qualifier = asLocal(b, v);
            return qualifier;
        } else if (!m.isStatic()) {
            return emitThis(b, methodQualifierType());
        } else {
            throw new Error("createLoadQualifier not supported for " + m.getClass().getName());
        }
    }

    protected TypeDecl methodQualifierType() {
        TypeDecl typeDecl = refined_GenericsCodegen_MethodAccess_methodQualifierType();
        if (typeDecl != null) {
            return typeDecl;
        }
        return decl().hostType();
    }

    private TypeDecl refined_TypeAnalysis_MethodAccess_type() {
        return decl().type();
    }

    public boolean computeDAbefore(int i, Variable v) {
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(Integer.valueOf(i));
        arrayList.add(v);
        if (this.computeDAbefore_int_Variable_values == null) {
            this.computeDAbefore_int_Variable_values = new HashMap(4);
        }
        if (this.computeDAbefore_int_Variable_values.containsKey(arrayList)) {
            return ((Boolean) this.computeDAbefore_int_Variable_values.get(arrayList)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean computeDAbefore_int_Variable_value = computeDAbefore_compute(i, v);
        if (isFinal && num == state().boundariesCrossed) {
            this.computeDAbefore_int_Variable_values.put(arrayList, Boolean.valueOf(computeDAbefore_int_Variable_value));
        }
        return computeDAbefore_int_Variable_value;
    }

    private boolean computeDAbefore_compute(int i, Variable v) {
        return i == 0 ? isDAbefore(v) : getArg(i - 1).isDAafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafter(Variable v) {
        state();
        return getNumArg() == 0 ? isDAbefore(v) : getArg(getNumArg() - 1).isDAafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafterTrue(Variable v) {
        state();
        if (getNumArg() == 0) {
            if (isDAbefore(v)) {
                return true;
            }
        } else if (getArg(getNumArg() - 1).isDAafter(v)) {
            return true;
        }
        return isFalse();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafterFalse(Variable v) {
        state();
        if (getNumArg() == 0) {
            if (isDAbefore(v)) {
                return true;
            }
        } else if (getArg(getNumArg() - 1).isDAafter(v)) {
            return true;
        }
        return isTrue();
    }

    public Collection exceptionCollection() {
        if (this.exceptionCollection_computed) {
            return this.exceptionCollection_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.exceptionCollection_value = exceptionCollection_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.exceptionCollection_computed = true;
        }
        return this.exceptionCollection_value;
    }

    private Collection exceptionCollection_compute() {
        HashSet set = new HashSet();
        Iterator iter = decls().iterator();
        if (!iter.hasNext()) {
            return set;
        }
        MethodDecl m = (MethodDecl) iter.next();
        for (int i = 0; i < m.getNumException(); i++) {
            TypeDecl exceptionType = m.getException(i).type();
            set.add(exceptionType);
        }
        while (iter.hasNext()) {
            HashSet first = new HashSet();
            first.addAll(set);
            HashSet second = new HashSet();
            MethodDecl m2 = (MethodDecl) iter.next();
            for (int i2 = 0; i2 < m2.getNumException(); i2++) {
                TypeDecl exceptionType2 = m2.getException(i2).type();
                second.add(exceptionType2);
            }
            set = new HashSet();
            Iterator i1 = first.iterator();
            while (i1.hasNext()) {
                TypeDecl firstType = (TypeDecl) i1.next();
                Iterator i22 = second.iterator();
                while (i22.hasNext()) {
                    TypeDecl secondType = (TypeDecl) i22.next();
                    if (firstType.instanceOf(secondType)) {
                        set.add(firstType);
                    } else if (secondType.instanceOf(firstType)) {
                        set.add(secondType);
                    }
                }
            }
        }
        return set;
    }

    public MethodDecl singleCandidateDecl() {
        state();
        MethodDecl result = null;
        for (MethodDecl m : lookupMethod(name())) {
            if (result == null) {
                result = m;
            } else if (m.getNumParameter() == getNumArg() && result.getNumParameter() != getNumArg()) {
                result = m;
            }
        }
        return result;
    }

    public SimpleSet decls() {
        if (this.decls_computed) {
            return this.decls_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.decls_value = decls_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.decls_computed = true;
        }
        return this.decls_value;
    }

    private SimpleSet decls_compute() {
        SimpleSet maxSpecific = maxSpecific(lookupMethod(name()));
        if (!isQualified() ? inStaticContext() : qualifier().staticContextQualifier()) {
            maxSpecific = removeInstanceMethods(maxSpecific);
        }
        return maxSpecific;
    }

    public MethodDecl decl() {
        if (this.decl_computed) {
            return this.decl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.decl_value = decl_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.decl_computed = true;
        }
        return this.decl_value;
    }

    private MethodDecl decl_compute() {
        SimpleSet decls = decls();
        if (decls.size() == 1) {
            return (MethodDecl) decls.iterator().next();
        }
        boolean allAbstract = true;
        Iterator iter = decls.iterator();
        while (iter.hasNext() && allAbstract) {
            MethodDecl m = (MethodDecl) iter.next();
            if (!m.isAbstract() && !m.hostType().isObject()) {
                allAbstract = false;
            }
        }
        if (decls.size() > 1 && allAbstract) {
            return (MethodDecl) decls.iterator().next();
        }
        return unknownMethod();
    }

    public boolean accessible(MethodDecl m) {
        state();
        if (!isQualified()) {
            return true;
        }
        if (!m.accessibleFrom(hostType()) || !qualifier().type().accessibleFrom(hostType())) {
            return false;
        }
        if (m.isProtected() && !m.hostPackage().equals(hostPackage()) && !m.isStatic() && !qualifier().isSuperAccess()) {
            return hostType().mayAccess(this, m);
        }
        return true;
    }

    public boolean validArgs() {
        state();
        for (int i = 0; i < getNumArg(); i++) {
            if (getArg(i).type().isUnknown()) {
                return false;
            }
        }
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

    @Override // soot.JastAddJ.Expr
    public boolean isMethodAccess() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.Access
    public NameType predNameType() {
        state();
        return NameType.AMBIGUOUS_NAME;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr
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
        if (getNumArg() == 0 && name().equals("getClass") && decl().hostType().isObject()) {
            TypeDecl bound = isQualified() ? qualifier().type() : hostType();
            ArrayList args = new ArrayList();
            args.add(bound.erasure().asWildcardExtends());
            return ((GenericClassDecl) lookupType("java.lang", "Class")).lookupParTypeDecl(args);
        }
        return refined_TypeAnalysis_MethodAccess_type();
    }

    public boolean applicableBySubtyping(MethodDecl m) {
        state();
        if (m.getNumParameter() != getNumArg()) {
            return false;
        }
        for (int i = 0; i < m.getNumParameter(); i++) {
            if (!getArg(i).type().instanceOf(m.getParameter(i).type())) {
                return false;
            }
        }
        return true;
    }

    public boolean applicableByMethodInvocationConversion(MethodDecl m) {
        state();
        if (m.getNumParameter() != getNumArg()) {
            return false;
        }
        for (int i = 0; i < m.getNumParameter(); i++) {
            if (!getArg(i).type().methodInvocationConversionTo(m.getParameter(i).type())) {
                return false;
            }
        }
        return true;
    }

    public boolean applicableVariableArity(MethodDecl m) {
        state();
        for (int i = 0; i < m.getNumParameter() - 1; i++) {
            if (!getArg(i).type().methodInvocationConversionTo(m.getParameter(i).type())) {
                return false;
            }
        }
        for (int i2 = m.getNumParameter() - 1; i2 < getNumArg(); i2++) {
            if (!getArg(i2).type().methodInvocationConversionTo(m.lastParameter().type().componentType())) {
                return false;
            }
        }
        return true;
    }

    public boolean potentiallyApplicable(MethodDecl m) {
        state();
        if (!m.name().equals(name()) || !m.accessibleFrom(hostType())) {
            return false;
        }
        if (m.isVariableArity() && arity() < m.arity() - 1) {
            return false;
        }
        if (!m.isVariableArity() && m.arity() != arity()) {
            return false;
        }
        if (m instanceof GenericMethodDecl) {
            GenericMethodDecl gm = (GenericMethodDecl) m;
            ArrayList list = typeArguments(m);
            if (list.size() != 0) {
                if (gm.getNumTypeParameter() != list.size()) {
                    return false;
                }
                for (int i = 0; i < gm.getNumTypeParameter(); i++) {
                    if (!((TypeDecl) list.get(i)).subtype(gm.original().getTypeParameter(i))) {
                        return false;
                    }
                }
                return true;
            }
            return true;
        }
        return true;
    }

    public int arity() {
        state();
        return getNumArg();
    }

    public ArrayList typeArguments(MethodDecl m) {
        if (this.typeArguments_MethodDecl_values == null) {
            this.typeArguments_MethodDecl_values = new HashMap(4);
        }
        if (this.typeArguments_MethodDecl_values.containsKey(m)) {
            return (ArrayList) this.typeArguments_MethodDecl_values.get(m);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        ArrayList typeArguments_MethodDecl_value = typeArguments_compute(m);
        if (isFinal && num == state().boundariesCrossed) {
            this.typeArguments_MethodDecl_values.put(m, typeArguments_MethodDecl_value);
        }
        return typeArguments_MethodDecl_value;
    }

    private ArrayList typeArguments_compute(MethodDecl m) {
        ArrayList typeArguments = new ArrayList();
        if (m instanceof GenericMethodDecl) {
            GenericMethodDecl g = (GenericMethodDecl) m;
            Collection<TypeDecl> arguments = computeConstraints(g);
            if (arguments.isEmpty()) {
                return typeArguments;
            }
            int i = 0;
            for (TypeDecl typeDecl : arguments) {
                if (typeDecl == null) {
                    TypeVariable v = g.original().getTypeParameter(i);
                    if (v.getNumTypeBound() == 0) {
                        typeDecl = typeObject();
                    } else if (v.getNumTypeBound() == 1) {
                        typeDecl = v.getTypeBound(0).type();
                    } else {
                        typeDecl = v.lubType();
                    }
                }
                typeArguments.add(typeDecl);
                i++;
            }
        }
        return typeArguments;
    }

    public boolean invokesVariableArityAsArray() {
        state();
        if (!decl().isVariableArity() || arity() != decl().arity()) {
            return false;
        }
        return getArg(getNumArg() - 1).type().methodInvocationConversionTo(decl().lastParameter().type());
    }

    public boolean requiresAccessor() {
        state();
        MethodDecl m = decl();
        if (m.isPrivate() && m.hostType() != hostType()) {
            return true;
        }
        if (m.isProtected() && !m.hostPackage().equals(hostPackage()) && !hostType().hasMethod(m.name())) {
            return true;
        }
        return false;
    }

    public boolean handlesException(TypeDecl exceptionType) {
        state();
        boolean handlesException_TypeDecl_value = getParent().Define_boolean_handlesException(this, null, exceptionType);
        return handlesException_TypeDecl_value;
    }

    public MethodDecl unknownMethod() {
        state();
        MethodDecl unknownMethod_value = getParent().Define_MethodDecl_unknownMethod(this, null);
        return unknownMethod_value;
    }

    @Override // soot.JastAddJ.Access
    public boolean inExplicitConstructorInvocation() {
        state();
        boolean inExplicitConstructorInvocation_value = getParent().Define_boolean_inExplicitConstructorInvocation(this, null);
        return inExplicitConstructorInvocation_value;
    }

    public TypeDecl typeObject() {
        state();
        TypeDecl typeObject_value = getParent().Define_TypeDecl_typeObject(this, null);
        return typeObject_value;
    }

    @Override // soot.JastAddJ.Access
    public boolean withinSuppressWarnings(String s) {
        state();
        boolean withinSuppressWarnings_String_value = getParent().Define_boolean_withinSuppressWarnings(this, null, s);
        return withinSuppressWarnings_String_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getArgListNoTransform()) {
            int i = caller.getIndexOfChild(child);
            return computeDAbefore(i, v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
        if (caller == getArgListNoTransform()) {
            caller.getIndexOfChild(child);
            return unqualifiedScope().lookupMethod(name);
        }
        return getParent().Define_Collection_lookupMethod(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
        if (caller == getArgListNoTransform()) {
            caller.getIndexOfChild(child);
            return unqualifiedScope().hasPackage(packageName);
        }
        return getParent().Define_boolean_hasPackage(this, caller, packageName);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
        if (caller == getArgListNoTransform()) {
            caller.getIndexOfChild(child);
            return unqualifiedScope().lookupType(name);
        }
        return getParent().Define_SimpleSet_lookupType(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getArgListNoTransform()) {
            caller.getIndexOfChild(child);
            return unqualifiedScope().lookupVariable(name);
        }
        return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getArgListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.EXPRESSION_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public String Define_String_methodHost(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return unqualifiedScope().methodHost();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
        if (caller == getArgListNoTransform()) {
            caller.getIndexOfChild(child);
            return typeObject();
        }
        return getParent().Define_TypeDecl_assignConvertedType(this, caller);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
