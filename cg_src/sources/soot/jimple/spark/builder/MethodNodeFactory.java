package soot.jimple.spark.builder;

import net.bytebuddy.implementation.auxiliary.TypeProxy;
import soot.ArrayType;
import soot.Local;
import soot.PointsToAnalysis;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.Expr;
import soot.jimple.IdentityRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.spark.internal.ClientAccessibilityOracle;
import soot.jimple.spark.internal.SparkLibraryHelper;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.GlobalVarNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.MethodPAG;
import soot.jimple.spark.pag.NewInstanceNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.Parm;
import soot.jimple.spark.pag.VarNode;
import soot.shimple.AbstractShimpleValueSwitch;
import soot.shimple.PhiExpr;
import soot.toolkits.scalar.Pair;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/builder/MethodNodeFactory.class */
public class MethodNodeFactory extends AbstractShimpleValueSwitch {
    protected final PAG pag;
    protected final MethodPAG mpag;
    protected SootMethod method;
    static final /* synthetic */ boolean $assertionsDisabled;
    protected ClientAccessibilityOracle accessibilityOracle = Scene.v().getClientAccessibilityOracle();
    protected final RefType rtClass = RefType.v("java.lang.Class");
    protected final RefType rtStringType = RefType.v("java.lang.String");
    protected final RefType rtHashSet = RefType.v("java.util.HashSet");
    protected final RefType rtHashMap = RefType.v("java.util.HashMap");
    protected final RefType rtLinkedList = RefType.v("java.util.LinkedList");
    protected final RefType rtHashtableEmptyIterator = RefType.v("java.util.Hashtable$EmptyIterator");
    protected final RefType rtHashtableEmptyEnumerator = RefType.v("java.util.Hashtable$EmptyEnumerator");

    static {
        $assertionsDisabled = !MethodNodeFactory.class.desiredAssertionStatus();
    }

    public MethodNodeFactory(PAG pag, MethodPAG mpag) {
        this.pag = pag;
        this.mpag = mpag;
        setCurrentMethod(mpag.getMethod());
    }

    private void setCurrentMethod(SootMethod m) {
        this.method = m;
        if (!m.isStatic()) {
            SootClass c = m.getDeclaringClass();
            if (c == null) {
                throw new RuntimeException("Method " + m + " has no declaring class");
            }
            caseThis();
        }
        for (int i = 0; i < m.getParameterCount(); i++) {
            if (m.getParameterType(i) instanceof RefLikeType) {
                caseParm(i);
            }
        }
        Type retType = m.getReturnType();
        if (retType instanceof RefLikeType) {
            caseRet();
        }
    }

    public Node getNode(Value v) {
        v.apply(this);
        return getNode();
    }

    public final void handleStmt(Stmt s) {
        if (s.containsInvokeExpr()) {
            if (!this.pag.getCGOpts().types_for_invoke()) {
                return;
            }
            InvokeExpr iexpr = s.getInvokeExpr();
            if (iexpr instanceof VirtualInvokeExpr) {
                if (!isReflectionNewInstance(iexpr)) {
                    return;
                }
            } else if (!(iexpr instanceof StaticInvokeExpr)) {
                return;
            }
        }
        s.apply(new AbstractStmtSwitch() { // from class: soot.jimple.spark.builder.MethodNodeFactory.1
            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public final void caseAssignStmt(AssignStmt as) {
                Value l = as.getLeftOp();
                Value r = as.getRightOp();
                if (!(l.getType() instanceof RefLikeType)) {
                    return;
                }
                if (!MethodNodeFactory.$assertionsDisabled && !(r.getType() instanceof RefLikeType)) {
                    throw new AssertionError("Type mismatch in assignment " + as + " in method " + MethodNodeFactory.this.method.getSignature());
                }
                l.apply(MethodNodeFactory.this);
                Node dest = MethodNodeFactory.this.getNode();
                r.apply(MethodNodeFactory.this);
                Node src = MethodNodeFactory.this.getNode();
                if (l instanceof InstanceFieldRef) {
                    ((InstanceFieldRef) l).getBase().apply(MethodNodeFactory.this);
                    MethodNodeFactory.this.pag.addDereference((VarNode) MethodNodeFactory.this.getNode());
                }
                if (r instanceof InstanceFieldRef) {
                    ((InstanceFieldRef) r).getBase().apply(MethodNodeFactory.this);
                    MethodNodeFactory.this.pag.addDereference((VarNode) MethodNodeFactory.this.getNode());
                } else if (r instanceof StaticFieldRef) {
                    StaticFieldRef sfr = (StaticFieldRef) r;
                    SootFieldRef s2 = sfr.getFieldRef();
                    if (MethodNodeFactory.this.pag.getOpts().empties_as_allocs()) {
                        if (s2.declaringClass().getName().equals("java.util.Collections")) {
                            if (s2.name().equals("EMPTY_SET")) {
                                src = MethodNodeFactory.this.pag.makeAllocNode(MethodNodeFactory.this.rtHashSet, MethodNodeFactory.this.rtHashSet, MethodNodeFactory.this.method);
                            } else if (s2.name().equals("EMPTY_MAP")) {
                                src = MethodNodeFactory.this.pag.makeAllocNode(MethodNodeFactory.this.rtHashMap, MethodNodeFactory.this.rtHashMap, MethodNodeFactory.this.method);
                            } else if (s2.name().equals("EMPTY_LIST")) {
                                src = MethodNodeFactory.this.pag.makeAllocNode(MethodNodeFactory.this.rtLinkedList, MethodNodeFactory.this.rtLinkedList, MethodNodeFactory.this.method);
                            }
                        } else if (s2.declaringClass().getName().equals("java.util.Hashtable")) {
                            if (s2.name().equals("emptyIterator")) {
                                src = MethodNodeFactory.this.pag.makeAllocNode(MethodNodeFactory.this.rtHashtableEmptyIterator, MethodNodeFactory.this.rtHashtableEmptyIterator, MethodNodeFactory.this.method);
                            } else if (s2.name().equals("emptyEnumerator")) {
                                src = MethodNodeFactory.this.pag.makeAllocNode(MethodNodeFactory.this.rtHashtableEmptyEnumerator, MethodNodeFactory.this.rtHashtableEmptyEnumerator, MethodNodeFactory.this.method);
                            }
                        }
                    }
                }
                MethodNodeFactory.this.mpag.addInternalEdge(src, dest);
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public final void caseReturnStmt(ReturnStmt rs) {
                if (!(rs.getOp().getType() instanceof RefLikeType)) {
                    return;
                }
                rs.getOp().apply(MethodNodeFactory.this);
                Node retNode = MethodNodeFactory.this.getNode();
                MethodNodeFactory.this.mpag.addInternalEdge(retNode, MethodNodeFactory.this.caseRet());
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public final void caseIdentityStmt(IdentityStmt is) {
                if (!(is.getLeftOp().getType() instanceof RefLikeType)) {
                    return;
                }
                Value leftOp = is.getLeftOp();
                Value rightOp = is.getRightOp();
                leftOp.apply(MethodNodeFactory.this);
                Node dest = MethodNodeFactory.this.getNode();
                rightOp.apply(MethodNodeFactory.this);
                Node src = MethodNodeFactory.this.getNode();
                MethodNodeFactory.this.mpag.addInternalEdge(src, dest);
                int libOption = MethodNodeFactory.this.pag.getCGOpts().library();
                if (libOption != 1 && MethodNodeFactory.this.accessibilityOracle.isAccessible(MethodNodeFactory.this.method) && (rightOp instanceof IdentityRef)) {
                    Type rt = rightOp.getType();
                    rt.apply(new SparkLibraryHelper(MethodNodeFactory.this.pag, src, MethodNodeFactory.this.method));
                }
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public final void caseThrowStmt(ThrowStmt ts) {
                ts.getOp().apply(MethodNodeFactory.this);
                MethodNodeFactory.this.mpag.addOutEdge(MethodNodeFactory.this.getNode(), MethodNodeFactory.this.pag.nodeFactory().caseThrow());
            }
        });
    }

    private boolean isReflectionNewInstance(InvokeExpr iexpr) {
        if (iexpr instanceof VirtualInvokeExpr) {
            VirtualInvokeExpr vie = (VirtualInvokeExpr) iexpr;
            if (vie.getBase().getType() instanceof RefType) {
                RefType rt = (RefType) vie.getBase().getType();
                if (rt.getSootClass().getName().equals("java.lang.Class") && vie.getMethodRef().name().equals(TypeProxy.SilentConstruction.Appender.NEW_INSTANCE_METHOD_NAME) && vie.getMethodRef().parameterTypes().size() == 0) {
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public final Node getNode() {
        return (Node) getResult();
    }

    public final Node caseThis() {
        VarNode ret = this.pag.makeLocalVarNode(new Pair(this.method, PointsToAnalysis.THIS_NODE), this.method.getDeclaringClass().getType(), this.method);
        ret.setInterProcTarget();
        return ret;
    }

    public final Node caseParm(int index) {
        if (this.method.getParameterCount() < index + 1) {
            return null;
        }
        VarNode ret = this.pag.makeLocalVarNode(new Pair(this.method, new Integer(index)), this.method.getParameterType(index), this.method);
        ret.setInterProcTarget();
        return ret;
    }

    @Override // soot.shimple.AbstractShimpleValueSwitch, soot.shimple.ShimpleExprSwitch
    public final void casePhiExpr(PhiExpr e) {
        Pair<Expr, String> phiPair = new Pair<>(e, PointsToAnalysis.PHI_NODE);
        LocalVarNode makeLocalVarNode = this.pag.makeLocalVarNode(phiPair, e.getType(), this.method);
        for (Value op : e.getValues()) {
            op.apply(this);
            Node opNode = getNode();
            this.mpag.addInternalEdge(opNode, makeLocalVarNode);
        }
        setResult(makeLocalVarNode);
    }

    public final Node caseRet() {
        VarNode ret = this.pag.makeLocalVarNode(Parm.v(this.method, -2), this.method.getReturnType(), this.method);
        ret.setInterProcSource();
        return ret;
    }

    public final Node caseArray(VarNode base) {
        return this.pag.makeFieldRefNode(base, ArrayElement.v());
    }

    @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
    public final void caseArrayRef(ArrayRef ar) {
        caseLocal((Local) ar.getBase());
        setResult(caseArray((VarNode) getNode()));
    }

    @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
    public final void caseCastExpr(CastExpr ce) {
        Pair<Expr, String> castPair = new Pair<>(ce, PointsToAnalysis.CAST_NODE);
        ce.getOp().apply(this);
        Node opNode = getNode();
        LocalVarNode makeLocalVarNode = this.pag.makeLocalVarNode(castPair, ce.getCastType(), this.method);
        this.mpag.addInternalEdge(opNode, makeLocalVarNode);
        setResult(makeLocalVarNode);
    }

    @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
    public final void caseCaughtExceptionRef(CaughtExceptionRef cer) {
        setResult(this.pag.nodeFactory().caseThrow());
    }

    @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
    public final void caseInstanceFieldRef(InstanceFieldRef ifr) {
        if (this.pag.getOpts().field_based() || this.pag.getOpts().vta()) {
            setResult(this.pag.makeGlobalVarNode(ifr.getField(), ifr.getField().getType()));
        } else {
            setResult(this.pag.makeLocalFieldRefNode(ifr.getBase(), ifr.getBase().getType(), ifr.getField(), this.method));
        }
    }

    @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ImmediateSwitch
    public final void caseLocal(Local l) {
        setResult(this.pag.makeLocalVarNode(l, l.getType(), this.method));
    }

    @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
    public final void caseNewArrayExpr(NewArrayExpr nae) {
        setResult(this.pag.makeAllocNode(nae, nae.getType(), this.method));
    }

    private boolean isStringBuffer(Type t) {
        if (!(t instanceof RefType)) {
            return false;
        }
        RefType rt = (RefType) t;
        String s = rt.toString();
        if (s.equals("java.lang.StringBuffer") || s.equals("java.lang.StringBuilder")) {
            return true;
        }
        return false;
    }

    @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
    public final void caseNewExpr(NewExpr ne) {
        if (this.pag.getOpts().merge_stringbuffer() && isStringBuffer(ne.getType())) {
            setResult(this.pag.makeAllocNode(ne.getType(), ne.getType(), null));
        } else {
            setResult(this.pag.makeAllocNode(ne, ne.getType(), this.method));
        }
    }

    @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
    public final void caseNewMultiArrayExpr(NewMultiArrayExpr nmae) {
        ArrayType type = (ArrayType) nmae.getType();
        AllocNode prevAn = this.pag.makeAllocNode(new Pair(nmae, new Integer(type.numDimensions)), type, this.method);
        VarNode prevVn = this.pag.makeLocalVarNode(prevAn, prevAn.getType(), this.method);
        this.mpag.addInternalEdge(prevAn, prevVn);
        setResult(prevAn);
        while (true) {
            Type t = type.getElementType();
            if (t instanceof ArrayType) {
                type = (ArrayType) t;
                AllocNode an = this.pag.makeAllocNode(new Pair(nmae, new Integer(type.numDimensions)), type, this.method);
                VarNode vn = this.pag.makeLocalVarNode(an, an.getType(), this.method);
                this.mpag.addInternalEdge(an, vn);
                this.mpag.addInternalEdge(vn, this.pag.makeFieldRefNode(prevVn, ArrayElement.v()));
                prevVn = vn;
            } else {
                return;
            }
        }
    }

    @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
    public final void caseParameterRef(ParameterRef pr) {
        setResult(caseParm(pr.getIndex()));
    }

    @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
    public final void caseStaticFieldRef(StaticFieldRef sfr) {
        setResult(this.pag.makeGlobalVarNode(sfr.getField(), sfr.getField().getType()));
    }

    @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ConstantSwitch
    public final void caseStringConstant(StringConstant sc) {
        AllocNode stringConstant;
        if (this.pag.getOpts().string_constants() || Scene.v().containsClass(sc.value) || (sc.value.length() > 0 && sc.value.charAt(0) == '[')) {
            stringConstant = this.pag.makeStringConstantNode(sc.value);
        } else {
            stringConstant = this.pag.makeAllocNode(PointsToAnalysis.STRING_NODE, this.rtStringType, null);
        }
        GlobalVarNode makeGlobalVarNode = this.pag.makeGlobalVarNode(stringConstant, this.rtStringType);
        this.pag.addEdge(stringConstant, makeGlobalVarNode);
        setResult(makeGlobalVarNode);
    }

    @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
    public final void caseThisRef(ThisRef tr) {
        setResult(caseThis());
    }

    @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ConstantSwitch
    public final void caseNullConstant(NullConstant nr) {
        setResult(null);
    }

    @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ConstantSwitch
    public final void caseClassConstant(ClassConstant cc) {
        AllocNode classConstant = this.pag.makeClassConstantNode(cc);
        GlobalVarNode makeGlobalVarNode = this.pag.makeGlobalVarNode(classConstant, this.rtClass);
        this.pag.addEdge(classConstant, makeGlobalVarNode);
        setResult(makeGlobalVarNode);
    }

    @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch, soot.jimple.ConstantSwitch
    public final void defaultCase(Object v) {
        throw new RuntimeException("failed to handle " + v);
    }

    @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
    public void caseStaticInvokeExpr(StaticInvokeExpr v) {
        SootMethodRef ref = v.getMethodRef();
        if (v.getArgCount() == 1 && (v.getArg(0) instanceof StringConstant) && ref.name().equals("forName") && ref.declaringClass().getName().equals("java.lang.Class") && ref.parameterTypes().size() == 1) {
            StringConstant classNameConst = (StringConstant) v.getArg(0);
            caseClassConstant(ClassConstant.v("L" + classNameConst.value.replaceAll("\\.", "/") + ";"));
        }
    }

    @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
    public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
        if (isReflectionNewInstance(v)) {
            NewInstanceNode newInstanceNode = this.pag.makeNewInstanceNode(v, Scene.v().getObjectType(), this.method);
            v.getBase().apply(this);
            Node srcNode = getNode();
            this.mpag.addInternalEdge(srcNode, newInstanceNode);
            setResult(newInstanceNode);
            return;
        }
        throw new RuntimeException("Unhandled case of VirtualInvokeExpr");
    }
}
