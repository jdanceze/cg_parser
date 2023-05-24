package soot.jimple.spark.ondemand.pautil;

import java.util.HashSet;
import java.util.Set;
import soot.AnySubType;
import soot.ArrayType;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.Type;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.ondemand.genericutil.ImmutableStack;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.VirtualCalls;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/pautil/AllocationSiteHandler.class */
public interface AllocationSiteHandler {
    boolean handleAllocationSite(AllocNode allocNode, ImmutableStack<Integer> immutableStack);

    void resetState();

    boolean shouldHandle(VarNode varNode);

    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/pautil/AllocationSiteHandler$PointsToSetHandler.class */
    public static class PointsToSetHandler implements AllocationSiteHandler {
        private PointsToSetInternal p2set;

        @Override // soot.jimple.spark.ondemand.pautil.AllocationSiteHandler
        public boolean handleAllocationSite(AllocNode allocNode, ImmutableStack<Integer> callStack) {
            this.p2set.add(allocNode);
            return false;
        }

        public PointsToSetInternal getP2set() {
            return this.p2set;
        }

        public void setP2set(PointsToSetInternal p2set) {
            this.p2set = p2set;
        }

        @Override // soot.jimple.spark.ondemand.pautil.AllocationSiteHandler
        public void resetState() {
            throw new RuntimeException();
        }

        @Override // soot.jimple.spark.ondemand.pautil.AllocationSiteHandler
        public boolean shouldHandle(VarNode dst) {
            return false;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/pautil/AllocationSiteHandler$CastCheckHandler.class */
    public static class CastCheckHandler implements AllocationSiteHandler {
        private Type type;
        private TypeManager manager;
        private boolean castFailed = false;

        @Override // soot.jimple.spark.ondemand.pautil.AllocationSiteHandler
        public boolean handleAllocationSite(AllocNode allocNode, ImmutableStack<Integer> callStack) {
            this.castFailed = !this.manager.castNeverFails(allocNode.getType(), this.type);
            return this.castFailed;
        }

        public void setManager(TypeManager manager) {
            this.manager = manager;
        }

        public void setType(Type type) {
            this.type = type;
        }

        @Override // soot.jimple.spark.ondemand.pautil.AllocationSiteHandler
        public void resetState() {
            throw new RuntimeException();
        }

        @Override // soot.jimple.spark.ondemand.pautil.AllocationSiteHandler
        public boolean shouldHandle(VarNode dst) {
            P2SetVisitor v = new P2SetVisitor() { // from class: soot.jimple.spark.ondemand.pautil.AllocationSiteHandler.CastCheckHandler.1
                @Override // soot.jimple.spark.sets.P2SetVisitor
                public void visit(Node n) {
                    if (!this.returnValue) {
                        this.returnValue = !CastCheckHandler.this.manager.castNeverFails(n.getType(), CastCheckHandler.this.type);
                    }
                }
            };
            dst.getP2Set().forall(v);
            return v.getReturnValue();
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/pautil/AllocationSiteHandler$VirtualCallHandler.class */
    public static class VirtualCallHandler implements AllocationSiteHandler {
        public PAG pag;
        public Type receiverType;
        public SootMethod callee;
        public Set<SootMethod> possibleMethods = new HashSet();

        public VirtualCallHandler(PAG pag, Type receiverType, SootMethod callee) {
            this.pag = pag;
            this.receiverType = receiverType;
            this.callee = callee;
        }

        @Override // soot.jimple.spark.ondemand.pautil.AllocationSiteHandler
        public boolean handleAllocationSite(AllocNode allocNode, ImmutableStack<Integer> callStack) {
            Type type = allocNode.getType();
            if (!this.pag.getTypeManager().castNeverFails(type, this.receiverType)) {
                return false;
            }
            if (type instanceof AnySubType) {
                AnySubType any = (AnySubType) type;
                RefType refType = any.getBase();
                if (this.pag.getTypeManager().getFastHierarchy().canStoreType(this.receiverType, refType) || this.pag.getTypeManager().getFastHierarchy().canStoreType(refType, this.receiverType)) {
                    return true;
                }
                return false;
            }
            if (type instanceof ArrayType) {
                type = Scene.v().getSootClass(Scene.v().getObjectType().toString()).getType();
            }
            SootMethod targetMethod = VirtualCalls.v().resolveNonSpecial((RefType) type, this.callee.makeRef());
            if (!this.possibleMethods.contains(targetMethod)) {
                this.possibleMethods.add(targetMethod);
                if (this.possibleMethods.size() > 1) {
                    return true;
                }
                return false;
            }
            return false;
        }

        @Override // soot.jimple.spark.ondemand.pautil.AllocationSiteHandler
        public void resetState() {
            this.possibleMethods.clear();
        }

        @Override // soot.jimple.spark.ondemand.pautil.AllocationSiteHandler
        public boolean shouldHandle(VarNode dst) {
            return false;
        }
    }
}
