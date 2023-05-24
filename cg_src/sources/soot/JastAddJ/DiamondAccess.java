package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/DiamondAccess.class */
public class DiamondAccess extends Access implements Cloneable {
    protected boolean type_computed = false;
    protected TypeDecl type_value;
    protected Map typeArguments_MethodDecl_values;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DiamondAccess.class.desiredAssertionStatus();
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.type_computed = false;
        this.type_value = null;
        this.typeArguments_MethodDecl_values = null;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public DiamondAccess clone() throws CloneNotSupportedException {
        DiamondAccess node = (DiamondAccess) super.clone();
        node.type_computed = false;
        node.type_value = null;
        node.typeArguments_MethodDecl_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            DiamondAccess node = clone();
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

    protected static SimpleSet mostSpecific(SimpleSet maxSpecific, MethodDecl decl) {
        if (maxSpecific.isEmpty()) {
            maxSpecific = maxSpecific.add(decl);
        } else if (decl.moreSpecificThan((MethodDecl) maxSpecific.iterator().next())) {
            maxSpecific = SimpleSet.emptySet.add(decl);
        } else if (!((MethodDecl) maxSpecific.iterator().next()).moreSpecificThan(decl)) {
            maxSpecific = maxSpecific.add(decl);
        }
        return maxSpecific;
    }

    protected SimpleSet chooseConstructor() {
        ClassInstanceExpr instanceExpr = getClassInstanceExpr();
        TypeDecl type = getTypeAccess().type();
        if ($assertionsDisabled || instanceExpr != null) {
            if ($assertionsDisabled || (type instanceof ParClassDecl)) {
                GenericClassDecl genericType = (GenericClassDecl) ((ParClassDecl) type).genericDecl();
                List<PlaceholderMethodDecl> placeholderMethods = genericType.getPlaceholderMethodList();
                SimpleSet maxSpecific = SimpleSet.emptySet;
                Collection<MethodDecl> potentiallyApplicable = potentiallyApplicable(placeholderMethods);
                for (MethodDecl candidate : potentiallyApplicable) {
                    if (applicableBySubtyping(instanceExpr, candidate) || applicableByMethodInvocationConversion(instanceExpr, candidate) || applicableByVariableArity(instanceExpr, candidate)) {
                        maxSpecific = mostSpecific(maxSpecific, candidate);
                    }
                }
                return maxSpecific;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    protected Collection<MethodDecl> potentiallyApplicable(List<PlaceholderMethodDecl> candidates) {
        Collection<MethodDecl> potentiallyApplicable = new LinkedList<>();
        Iterator<PlaceholderMethodDecl> it = candidates.iterator();
        while (it.hasNext()) {
            GenericMethodDecl candidate = it.next();
            if (potentiallyApplicable(candidate)) {
                MethodDecl decl = candidate.lookupParMethodDecl(typeArguments(candidate));
                potentiallyApplicable.add(decl);
            }
        }
        return potentiallyApplicable;
    }

    protected boolean potentiallyApplicable(GenericMethodDecl candidate) {
        if (candidate.isVariableArity() && getClassInstanceExpr().arity() < candidate.arity() - 1) {
            return false;
        }
        if (!candidate.isVariableArity() && getClassInstanceExpr().arity() != candidate.arity()) {
            return false;
        }
        java.util.List<TypeDecl> typeArgs = typeArguments(candidate);
        if (typeArgs.size() != 0) {
            if (candidate.getNumTypeParameter() != typeArgs.size()) {
                return false;
            }
            for (int i = 0; i < candidate.getNumTypeParameter(); i++) {
                if (!typeArgs.get(i).subtype(candidate.original().getTypeParameter(i))) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public Collection<TypeDecl> computeConstraints(GenericMethodDecl decl) {
        Constraints c = new Constraints();
        for (int i = 0; i < decl.original().getNumTypeParameter(); i++) {
            c.addTypeVariable(decl.original().getTypeParameter(i));
        }
        ClassInstanceExpr instanceExpr = getClassInstanceExpr();
        int i2 = 0;
        while (i2 < instanceExpr.getNumArg()) {
            TypeDecl A = instanceExpr.getArg(i2).type();
            int index = i2 >= decl.getNumParameter() ? decl.getNumParameter() - 1 : i2;
            TypeDecl F = decl.getParameter(index).type();
            if ((decl.getParameter(index) instanceof VariableArityParameterDeclaration) && (instanceExpr.getNumArg() != decl.getNumParameter() || !A.isArrayDecl())) {
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

    protected boolean applicableBySubtyping(ClassInstanceExpr expr, MethodDecl method) {
        if (method.getNumParameter() != expr.getNumArg()) {
            return false;
        }
        for (int i = 0; i < method.getNumParameter(); i++) {
            if (!expr.getArg(i).type().instanceOf(method.getParameter(i).type())) {
                return false;
            }
        }
        return true;
    }

    protected boolean applicableByMethodInvocationConversion(ClassInstanceExpr expr, MethodDecl method) {
        if (method.getNumParameter() != expr.getNumArg()) {
            return false;
        }
        for (int i = 0; i < method.getNumParameter(); i++) {
            if (!expr.getArg(i).type().methodInvocationConversionTo(method.getParameter(i).type())) {
                return false;
            }
        }
        return true;
    }

    protected boolean applicableByVariableArity(ClassInstanceExpr expr, MethodDecl method) {
        for (int i = 0; i < method.getNumParameter() - 1; i++) {
            if (!expr.getArg(i).type().methodInvocationConversionTo(method.getParameter(i).type())) {
                return false;
            }
        }
        for (int i2 = method.getNumParameter() - 1; i2 < expr.getNumArg(); i2++) {
            if (!expr.getArg(i2).type().methodInvocationConversionTo(method.lastParameter().type().componentType())) {
                return false;
            }
        }
        return true;
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (isAnonymousDecl()) {
            error("the diamond operator can not be used with anonymous classes");
        }
        if (isExplicitGenericConstructorAccess()) {
            error("the diamond operator may not be used with generic constructors with explicit type parameters");
        }
        if (getClassInstanceExpr() == null) {
            error("the diamond operator can only be used in class instance expressions");
        }
        if (!(getTypeAccess().type() instanceof ParClassDecl)) {
            error("the diamond operator can only be used to instantiate generic classes");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer sb) {
        getTypeAccess().toString(sb);
        sb.append("<>");
    }

    public DiamondAccess() {
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public DiamondAccess(Access p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setTypeAccess(Access node) {
        setChild(node, 0);
    }

    public Access getTypeAccess() {
        return (Access) getChild(0);
    }

    public Access getTypeAccessNoTransform() {
        return (Access) getChildNoTransform(0);
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
        TypeDecl accessType = getTypeAccess().type();
        if (isAnonymousDecl()) {
            return accessType;
        }
        if (getClassInstanceExpr() == null) {
            return accessType;
        }
        if (!(accessType instanceof ParClassDecl)) {
            return accessType;
        }
        SimpleSet maxSpecific = chooseConstructor();
        if (maxSpecific.isEmpty()) {
            return getTypeAccess().type();
        }
        MethodDecl constructor = (MethodDecl) maxSpecific.iterator().next();
        return constructor.type();
    }

    @Override // soot.JastAddJ.Access
    public boolean isDiamond() {
        state();
        return true;
    }

    public java.util.List<TypeDecl> typeArguments(MethodDecl decl) {
        if (this.typeArguments_MethodDecl_values == null) {
            this.typeArguments_MethodDecl_values = new HashMap(4);
        }
        if (this.typeArguments_MethodDecl_values.containsKey(decl)) {
            return (java.util.List) this.typeArguments_MethodDecl_values.get(decl);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        java.util.List<TypeDecl> typeArguments_MethodDecl_value = typeArguments_compute(decl);
        if (isFinal && num == state().boundariesCrossed) {
            this.typeArguments_MethodDecl_values.put(decl, typeArguments_MethodDecl_value);
        }
        return typeArguments_MethodDecl_value;
    }

    private java.util.List<TypeDecl> typeArguments_compute(MethodDecl decl) {
        java.util.List<TypeDecl> typeArguments = new LinkedList<>();
        if (decl instanceof GenericMethodDecl) {
            GenericMethodDecl method = (GenericMethodDecl) decl;
            Collection<TypeDecl> arguments = computeConstraints(method);
            if (arguments.isEmpty()) {
                return typeArguments;
            }
            int i = 0;
            for (TypeDecl argument : arguments) {
                if (argument == null) {
                    TypeVariable v = method.original().getTypeParameter(i);
                    if (v.getNumTypeBound() == 0) {
                        argument = typeObject();
                    } else if (v.getNumTypeBound() == 1) {
                        argument = v.getTypeBound(0).type();
                    } else {
                        argument = v.lubType();
                    }
                }
                typeArguments.add(argument);
                i++;
            }
        }
        return typeArguments;
    }

    public ClassInstanceExpr getClassInstanceExpr() {
        state();
        ClassInstanceExpr getClassInstanceExpr_value = getParent().Define_ClassInstanceExpr_getClassInstanceExpr(this, null);
        return getClassInstanceExpr_value;
    }

    public TypeDecl typeObject() {
        state();
        TypeDecl typeObject_value = getParent().Define_TypeDecl_typeObject(this, null);
        return typeObject_value;
    }

    public boolean isAnonymousDecl() {
        state();
        boolean isAnonymousDecl_value = getParent().Define_boolean_isAnonymousDecl(this, null);
        return isAnonymousDecl_value;
    }

    public boolean isExplicitGenericConstructorAccess() {
        state();
        boolean isExplicitGenericConstructorAccess_value = getParent().Define_boolean_isExplicitGenericConstructorAccess(this, null);
        return isExplicitGenericConstructorAccess_value;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
