package soot.jimple.toolkits.annotation.arraycheck;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.G;
import soot.Local;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.JArrayRef;
import soot.jimple.internal.JNewMultiArrayExpr;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;
import soot.options.Options;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/arraycheck/RectangularArrayFinder.class */
public class RectangularArrayFinder extends SceneTransformer {
    private static final Logger logger = LoggerFactory.getLogger(RectangularArrayFinder.class);
    private final ExtendedHashMutableDirectedGraph<Object> agraph = new ExtendedHashMutableDirectedGraph<>();
    private final Set<Object> falseSet = new HashSet();
    private final Set<Object> trueSet = new HashSet();
    private CallGraph cg;

    public RectangularArrayFinder(Singletons.Global g) {
    }

    public static RectangularArrayFinder v() {
        return G.v().soot_jimple_toolkits_annotation_arraycheck_RectangularArrayFinder();
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> opts) {
        Scene sc = Scene.v();
        this.cg = sc.getCallGraph();
        Date start = new Date();
        if (Options.v().verbose()) {
            logger.debug("[ra] Finding rectangular arrays, start on " + start);
        }
        for (SootClass c : sc.getApplicationClasses()) {
            Iterator<SootMethod> methodIt = c.methodIterator();
            while (methodIt.hasNext()) {
                SootMethod method = methodIt.next();
                if (method.isConcrete() && sc.getReachableMethods().contains(method)) {
                    recoverRectArray(method);
                    addInfoFromMethod(method);
                }
            }
        }
        if (this.agraph.containsNode(BoolValue.v(false))) {
            List<Object> startNodes = this.agraph.getSuccsOf(BoolValue.v(false));
            this.falseSet.addAll(startNodes);
            List<Object> changedNodeList = new ArrayList<>(startNodes);
            while (!changedNodeList.isEmpty()) {
                Object node = changedNodeList.remove(0);
                for (Object succ : this.agraph.getSuccsOf(node)) {
                    if (!this.falseSet.contains(succ)) {
                        this.falseSet.add(succ);
                        changedNodeList.add(succ);
                    }
                }
            }
        }
        if (this.agraph.containsNode(BoolValue.v(true))) {
            List<Object> changedNodeList2 = new ArrayList<>();
            for (Object node2 : this.agraph.getSuccsOf(BoolValue.v(true))) {
                if (!this.falseSet.contains(node2)) {
                    changedNodeList2.add(node2);
                    this.trueSet.add(node2);
                }
            }
            while (!changedNodeList2.isEmpty()) {
                Object node3 = changedNodeList2.remove(0);
                for (Object succ2 : this.agraph.getSuccsOf(node3)) {
                    if (!this.falseSet.contains(succ2) && !this.trueSet.contains(succ2)) {
                        this.trueSet.add(succ2);
                        changedNodeList2.add(succ2);
                    }
                }
            }
        }
        if (Options.v().debug()) {
            logger.debug("Rectangular Array :");
            for (Object node4 : this.trueSet) {
                logger.debug(new StringBuilder().append(node4).toString());
            }
            logger.debug("\nNon-rectangular Array :");
            for (Object node5 : this.falseSet) {
                logger.debug(new StringBuilder().append(node5).toString());
            }
        }
        if (Options.v().verbose()) {
            Date finish = new Date();
            long runtime = finish.getTime() - start.getTime();
            long mins = runtime / 60000;
            long secs = (runtime % 60000) / 1000;
            logger.debug("[ra] Rectangular array finder finishes. It took " + mins + " mins and " + secs + " secs.");
        }
    }

    private void addInfoFromMethod(SootMethod method) {
        if (Options.v().verbose()) {
            logger.debug("[ra] Operating " + method.getSignature());
        }
        boolean needTransfer = true;
        boolean trackReturn = false;
        Type rtnType = method.getReturnType();
        if ((rtnType instanceof ArrayType) && ((ArrayType) rtnType).numDimensions > 1) {
            trackReturn = true;
            needTransfer = true;
        }
        Body body = method.getActiveBody();
        Set<Object> tmpNode = new HashSet<>();
        Set<Value> arrayLocal = new HashSet<>();
        for (Local local : body.getLocals()) {
            Type type = local.getType();
            if (type instanceof ArrayType) {
                if (((ArrayType) type).numDimensions > 1) {
                    arrayLocal.add(local);
                } else {
                    tmpNode.add(new MethodLocal(method, local));
                }
            }
        }
        ExtendedHashMutableDirectedGraph extendedHashMutableDirectedGraph = new ExtendedHashMutableDirectedGraph();
        Iterator<Unit> unitIt = body.getUnits().snapshotIterator();
        while (unitIt.hasNext()) {
            Stmt s = (Stmt) unitIt.next();
            if (s.containsInvokeExpr()) {
                InvokeExpr iexpr = s.getInvokeExpr();
                int argnum = iexpr.getArgCount();
                for (int i = 0; i < argnum; i++) {
                    Value arg = iexpr.getArg(i);
                    if (arrayLocal.contains(arg)) {
                        needTransfer = true;
                        MethodLocal ml = new MethodLocal(method, (Local) arg);
                        Targets targetIt = new Targets(this.cg.edgesOutOf(s));
                        while (targetIt.hasNext()) {
                            SootMethod target = (SootMethod) targetIt.next();
                            MethodParameter mp = new MethodParameter(target, i);
                            extendedHashMutableDirectedGraph.addMutualEdge(ml, mp);
                        }
                    }
                }
            }
            if (trackReturn && (s instanceof ReturnStmt)) {
                Value op = ((ReturnStmt) s).getOp();
                if (op instanceof Local) {
                    extendedHashMutableDirectedGraph.addMutualEdge(new MethodLocal(method, (Local) op), new MethodReturn(method));
                }
            }
            if (s instanceof DefinitionStmt) {
                Value leftOp = ((DefinitionStmt) s).getLeftOp();
                Value rightOp = ((DefinitionStmt) s).getRightOp();
                if ((leftOp.getType() instanceof ArrayType) || (rightOp.getType() instanceof ArrayType)) {
                    if ((leftOp instanceof Local) && (rightOp instanceof Local)) {
                        if (arrayLocal.contains(leftOp) && arrayLocal.contains(rightOp)) {
                            int leftDims = ((ArrayType) ((Local) leftOp).getType()).numDimensions;
                            int rightDims = ((ArrayType) ((Local) rightOp).getType()).numDimensions;
                            MethodLocal methodLocal = new MethodLocal(method, (Local) leftOp);
                            MethodLocal methodLocal2 = new MethodLocal(method, (Local) rightOp);
                            extendedHashMutableDirectedGraph.addMutualEdge(methodLocal2, methodLocal);
                            if (leftDims != rightDims) {
                                extendedHashMutableDirectedGraph.addEdge(BoolValue.v(false), methodLocal2);
                            }
                        } else if (!arrayLocal.contains(leftOp)) {
                            extendedHashMutableDirectedGraph.addEdge(BoolValue.v(false), new MethodLocal(method, (Local) rightOp));
                        }
                    } else if ((leftOp instanceof Local) && (rightOp instanceof ParameterRef)) {
                        if (arrayLocal.contains(leftOp)) {
                            MethodLocal methodLocal3 = new MethodLocal(method, (Local) leftOp);
                            int index = ((ParameterRef) rightOp).getIndex();
                            extendedHashMutableDirectedGraph.addMutualEdge(new MethodParameter(method, index), methodLocal3);
                            needTransfer = true;
                        }
                    } else if ((leftOp instanceof Local) && (rightOp instanceof ArrayRef)) {
                        Local base = (Local) ((ArrayRef) rightOp).getBase();
                        if (arrayLocal.contains(base)) {
                            ArrayReferenceNode arrayReferenceNode = new ArrayReferenceNode(method, base);
                            extendedHashMutableDirectedGraph.addMutualEdge(new MethodLocal(method, base), arrayReferenceNode);
                            tmpNode.add(arrayReferenceNode);
                            extendedHashMutableDirectedGraph.addMutualEdge(arrayReferenceNode, new MethodLocal(method, (Local) leftOp));
                        }
                    } else if ((leftOp instanceof ArrayRef) && (rightOp instanceof Local)) {
                        Local base2 = (Local) ((ArrayRef) leftOp).getBase();
                        if (arrayLocal.contains(base2)) {
                            MethodLocal methodLocal4 = new MethodLocal(method, (Local) rightOp);
                            boolean addEdge = true;
                            if (extendedHashMutableDirectedGraph.containsNode(methodLocal4)) {
                                Set<Object> neighbor = new HashSet<>();
                                neighbor.addAll(extendedHashMutableDirectedGraph.getSuccsOf(methodLocal4));
                                neighbor.addAll(extendedHashMutableDirectedGraph.getSuccsOf(methodLocal4));
                                if (neighbor.size() == 1) {
                                    Object arrRef = new ArrayReferenceNode(method, base2);
                                    if (arrRef.equals(neighbor.iterator().next())) {
                                        addEdge = false;
                                    }
                                }
                            }
                            if (addEdge) {
                                extendedHashMutableDirectedGraph.addEdge(BoolValue.v(false), new MethodLocal(method, base2));
                            }
                        }
                    } else if ((leftOp instanceof Local) && (rightOp instanceof InvokeExpr)) {
                        if (arrayLocal.contains(leftOp)) {
                            MethodLocal methodLocal5 = new MethodLocal(method, (Local) leftOp);
                            Targets targetIt2 = new Targets(this.cg.edgesOutOf(s));
                            while (targetIt2.hasNext()) {
                                SootMethod target2 = (SootMethod) targetIt2.next();
                                extendedHashMutableDirectedGraph.addMutualEdge(new MethodReturn(target2), methodLocal5);
                            }
                        }
                    } else if ((leftOp instanceof FieldRef) && (rightOp instanceof Local)) {
                        if (arrayLocal.contains(rightOp)) {
                            SootField field = ((FieldRef) leftOp).getField();
                            extendedHashMutableDirectedGraph.addMutualEdge(new MethodLocal(method, (Local) rightOp), field);
                            Type ftype = ((FieldRef) leftOp).getType();
                            Type ltype = ((Local) rightOp).getType();
                            if (!ftype.equals(ltype)) {
                                extendedHashMutableDirectedGraph.addEdge(BoolValue.v(false), field);
                            }
                            needTransfer = true;
                        }
                    } else if ((leftOp instanceof Local) && (rightOp instanceof FieldRef)) {
                        if (arrayLocal.contains(leftOp)) {
                            MethodLocal methodLocal6 = new MethodLocal(method, (Local) leftOp);
                            extendedHashMutableDirectedGraph.addMutualEdge(((FieldRef) rightOp).getField(), methodLocal6);
                            Type ftype2 = ((FieldRef) rightOp).getType();
                            Type ltype2 = ((Local) leftOp).getType();
                            if (!ftype2.equals(ltype2)) {
                                extendedHashMutableDirectedGraph.addEdge(BoolValue.v(false), methodLocal6);
                            }
                            needTransfer = true;
                        }
                    } else if ((leftOp instanceof Local) && ((rightOp instanceof NewArrayExpr) || (rightOp instanceof NewMultiArrayExpr))) {
                        if (arrayLocal.contains(leftOp)) {
                            extendedHashMutableDirectedGraph.addEdge(BoolValue.v(true), new MethodLocal(method, (Local) leftOp));
                        }
                    } else if ((leftOp instanceof Local) && (rightOp instanceof CastExpr)) {
                        Local rOp = (Local) ((CastExpr) rightOp).getOp();
                        MethodLocal methodLocal7 = new MethodLocal(method, (Local) leftOp);
                        MethodLocal methodLocal8 = new MethodLocal(method, rOp);
                        if (arrayLocal.contains(leftOp) && arrayLocal.contains(rOp)) {
                            ArrayType lat = (ArrayType) leftOp.getType();
                            ArrayType rat = (ArrayType) rOp.getType();
                            if (lat.numDimensions == rat.numDimensions) {
                                extendedHashMutableDirectedGraph.addMutualEdge(methodLocal8, methodLocal7);
                            } else {
                                extendedHashMutableDirectedGraph.addEdge(BoolValue.v(false), methodLocal8);
                                extendedHashMutableDirectedGraph.addEdge(BoolValue.v(false), methodLocal7);
                            }
                        } else if (arrayLocal.contains(leftOp)) {
                            extendedHashMutableDirectedGraph.addEdge(BoolValue.v(false), methodLocal7);
                        } else if (arrayLocal.contains(rOp)) {
                            extendedHashMutableDirectedGraph.addEdge(BoolValue.v(false), methodLocal8);
                        }
                    }
                }
            }
        }
        if (needTransfer) {
            for (Object next : tmpNode) {
                extendedHashMutableDirectedGraph.skipNode(next);
            }
            this.agraph.mergeWith(extendedHashMutableDirectedGraph);
        }
    }

    private void recoverRectArray(SootMethod method) {
        int firstdim;
        Body body = method.getActiveBody();
        HashSet<Value> malocal = new HashSet<>();
        for (Local local : body.getLocals()) {
            Type type = local.getType();
            if ((type instanceof ArrayType) && ((ArrayType) type).numDimensions == 2) {
                malocal.add(local);
            }
        }
        if (malocal.isEmpty()) {
            return;
        }
        Chain<Unit> units = body.getUnits();
        Unit first = units.getFirst();
        while (true) {
            Stmt stmt = (Stmt) first;
            if (stmt != null && stmt.fallsThrough()) {
                if (stmt instanceof AssignStmt) {
                    Value leftOp = ((AssignStmt) stmt).getLeftOp();
                    Value rightOp = ((AssignStmt) stmt).getRightOp();
                    if (malocal.contains(leftOp) && (rightOp instanceof NewArrayExpr)) {
                        Local local2 = (Local) leftOp;
                        NewArrayExpr naexpr = (NewArrayExpr) rightOp;
                        Value size = naexpr.getSize();
                        if ((size instanceof IntConstant) && (firstdim = ((IntConstant) size).value) <= 100) {
                            ArrayType localtype = (ArrayType) local2.getType();
                            Type basetype = localtype.baseType;
                            Local[] tmplocals = new Local[firstdim];
                            int seconddim = lookforPattern(units, stmt, firstdim, local2, basetype, tmplocals);
                            if (seconddim >= 0) {
                                transferPattern(units, stmt, firstdim, seconddim, local2, basetype, tmplocals);
                            }
                        }
                    }
                }
                first = units.getSuccOf(stmt);
            } else {
                return;
            }
        }
    }

    private int lookforPattern(Chain<Unit> units, Stmt startpoint, int firstdim, Local local, Type baseTy, Local[] tmplocals) {
        int seconddim = -1;
        int curdim = 0;
        Local local2 = local;
        Stmt curstmt = startpoint;
        int state = 1;
        while (true) {
            curstmt = (Stmt) units.getSuccOf(curstmt);
            if (curstmt == null || !(curstmt instanceof AssignStmt)) {
                return -1;
            }
            Value leftOp = ((AssignStmt) curstmt).getLeftOp();
            Value rightOp = ((AssignStmt) curstmt).getRightOp();
            switch (state) {
                case 0:
                    break;
                case 1:
                    state = 99;
                    if (!(rightOp instanceof NewArrayExpr)) {
                        break;
                    } else {
                        NewArrayExpr naexpr = (NewArrayExpr) rightOp;
                        Type type = naexpr.getBaseType();
                        Value size = naexpr.getSize();
                        if (type.equals(baseTy) && (size instanceof IntConstant)) {
                            if (curdim == 0) {
                                seconddim = ((IntConstant) size).value;
                            } else if (((IntConstant) size).value != seconddim) {
                                break;
                            }
                            local2 = leftOp;
                            state = 2;
                            break;
                        }
                    }
                    break;
                case 2:
                    state = 99;
                    if (!(leftOp instanceof ArrayRef)) {
                        break;
                    } else {
                        Value base = ((ArrayRef) leftOp).getBase();
                        Value idx = ((ArrayRef) leftOp).getIndex();
                        if (base.equals(local2)) {
                            state = 2;
                            break;
                        } else if (base.equals(local) && (idx instanceof IntConstant) && curdim == ((IntConstant) idx).value && rightOp.equals(local2)) {
                            tmplocals[curdim] = local2;
                            curdim++;
                            if (curdim >= firstdim) {
                                state = 3;
                                break;
                            } else {
                                state = 1;
                                break;
                            }
                        }
                    }
                    break;
                case 3:
                    return seconddim;
                default:
                    return -1;
            }
        }
    }

    private void transferPattern(Chain<Unit> units, Stmt startpoint, int firstdim, int seconddim, Local local, Type baseTy, Local[] tmplocals) {
        List<Value> sizes = new ArrayList<>(2);
        sizes.add(IntConstant.v(firstdim));
        sizes.add(IntConstant.v(seconddim));
        Value nmexpr = new JNewMultiArrayExpr((ArrayType) local.getType(), sizes);
        ((AssignStmt) startpoint).setRightOp(nmexpr);
        int curdim = 0;
        Local tmpcur = local;
        Stmt curstmt = (Stmt) units.getSuccOf(startpoint);
        while (curdim < firstdim) {
            Value leftOp = ((AssignStmt) curstmt).getLeftOp();
            Value rightOp = ((AssignStmt) curstmt).getRightOp();
            if (tmplocals[curdim].equals(leftOp) && (rightOp instanceof NewArrayExpr)) {
                ArrayRef arexpr = new JArrayRef(local, IntConstant.v(curdim));
                ((AssignStmt) curstmt).setRightOp(arexpr);
                tmpcur = (Local) leftOp;
            } else if ((leftOp instanceof ArrayRef) && rightOp.equals(tmpcur)) {
                Stmt tmpstmt = curstmt;
                curstmt = (Stmt) units.getSuccOf(curstmt);
                units.remove(tmpstmt);
                curdim++;
            } else {
                curstmt = (Stmt) units.getSuccOf(curstmt);
            }
        }
    }

    public boolean isRectangular(Object obj) {
        return this.trueSet.contains(obj);
    }
}
