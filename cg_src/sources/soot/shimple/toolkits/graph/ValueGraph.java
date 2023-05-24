package soot.shimple.toolkits.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.coffi.Instruction;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.DivExpr;
import soot.jimple.EqExpr;
import soot.jimple.Expr;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.RemExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.ThisRef;
import soot.jimple.UnopExpr;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;
import soot.shimple.AbstractShimpleValueSwitch;
import soot.shimple.PhiExpr;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.CompleteBlockGraph;
import soot.toolkits.graph.Orderer;
import soot.toolkits.graph.PseudoTopologicalOrderer;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/shimple/toolkits/graph/ValueGraph.class */
public class ValueGraph {
    protected Map<Value, Node> localToNode;
    protected Map<Node, Value> nodeToLocal;
    protected List<Node> nodeList;
    protected int currentNodeNumber;

    public ValueGraph(BlockGraph cfg) {
        if (!(cfg.getBody() instanceof ShimpleBody)) {
            throw new RuntimeException("ValueGraph requires SSA form");
        }
        this.localToNode = new HashMap();
        this.nodeToLocal = new HashMap();
        this.nodeList = new ArrayList();
        this.currentNodeNumber = 0;
        Orderer<Block> pto = new PseudoTopologicalOrderer<>();
        List<Block> blocks = pto.newList(cfg, false);
        for (Block block : blocks) {
            Iterator<Unit> blockIt = block.iterator();
            while (blockIt.hasNext()) {
                handleStmt((Stmt) blockIt.next());
            }
        }
        for (Node node : this.nodeList) {
            node.patchStubs();
        }
    }

    protected void handleStmt(Stmt stmt) {
        if (!(stmt instanceof DefinitionStmt)) {
            return;
        }
        DefinitionStmt dStmt = (DefinitionStmt) stmt;
        Value leftOp = dStmt.getLeftOp();
        if (!(leftOp instanceof Local)) {
            return;
        }
        Value rightOp = dStmt.getRightOp();
        Node node = fetchGraph(rightOp);
        this.localToNode.put(leftOp, node);
        if (!(rightOp instanceof Local) && !node.isStub()) {
            this.nodeToLocal.put(node, leftOp);
        }
    }

    protected Node fetchNode(Value value) {
        Node ret;
        if (value instanceof Local) {
            ret = getNode(value);
            if (ret == null) {
                ret = new Node(value, true);
            }
        } else {
            ret = new Node(this, value);
        }
        return ret;
    }

    protected Node fetchGraph(Value value) {
        AbstractShimpleValueSwitch vs = new AbstractShimpleValueSwitch() { // from class: soot.shimple.toolkits.graph.ValueGraph.1
            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch, soot.jimple.ConstantSwitch
            public void defaultCase(Object object) {
                throw new RuntimeException("Internal error: " + object + " unhandled case.");
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ImmediateSwitch
            public void caseLocal(Local l) {
                setResult(ValueGraph.this.fetchNode(l));
            }

            public void handleConstant(Constant constant) {
                setResult(ValueGraph.this.fetchNode(constant));
            }

            public void handleRef(Ref ref) {
                setResult(ValueGraph.this.fetchNode(ref));
            }

            public void handleBinop(BinopExpr binop, boolean ordered) {
                Node nop1 = ValueGraph.this.fetchNode(binop.getOp1());
                Node nop2 = ValueGraph.this.fetchNode(binop.getOp2());
                List<Node> children = new ArrayList<>();
                children.add(nop1);
                children.add(nop2);
                setResult(new Node(binop, ordered, children));
            }

            public void handleUnknown(Expr expr) {
                setResult(ValueGraph.this.fetchNode(expr));
            }

            public void handleUnop(UnopExpr unop) {
                Node nop = ValueGraph.this.fetchNode(unop.getOp());
                List<Node> child = Collections.singletonList(nop);
                setResult(new Node(unop, true, child));
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ConstantSwitch
            public void caseFloatConstant(FloatConstant v) {
                handleConstant(v);
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ConstantSwitch
            public void caseIntConstant(IntConstant v) {
                handleConstant(v);
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ConstantSwitch
            public void caseLongConstant(LongConstant v) {
                handleConstant(v);
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ConstantSwitch
            public void caseNullConstant(NullConstant v) {
                handleConstant(v);
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ConstantSwitch
            public void caseStringConstant(StringConstant v) {
                handleConstant(v);
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
            public void caseArrayRef(ArrayRef v) {
                handleRef(v);
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
            public void caseStaticFieldRef(StaticFieldRef v) {
                handleRef(v);
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
            public void caseInstanceFieldRef(InstanceFieldRef v) {
                handleRef(v);
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
            public void caseParameterRef(ParameterRef v) {
                handleRef(v);
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
            public void caseCaughtExceptionRef(CaughtExceptionRef v) {
                handleRef(v);
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
            public void caseThisRef(ThisRef v) {
                handleRef(v);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseAddExpr(AddExpr v) {
                handleBinop(v, false);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseAndExpr(AndExpr v) {
                handleBinop(v, false);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseCmpExpr(CmpExpr v) {
                handleBinop(v, true);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseCmpgExpr(CmpgExpr v) {
                handleBinop(v, true);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseCmplExpr(CmplExpr v) {
                handleBinop(v, true);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseDivExpr(DivExpr v) {
                handleBinop(v, true);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseEqExpr(EqExpr v) {
                handleBinop(v, false);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseNeExpr(NeExpr v) {
                handleBinop(v, false);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseGeExpr(GeExpr v) {
                handleBinop(v, true);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseGtExpr(GtExpr v) {
                handleBinop(v, true);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseLeExpr(LeExpr v) {
                handleBinop(v, true);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseLtExpr(LtExpr v) {
                handleBinop(v, true);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseMulExpr(MulExpr v) {
                handleBinop(v, false);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseOrExpr(OrExpr v) {
                handleBinop(v, false);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseRemExpr(RemExpr v) {
                handleBinop(v, true);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseShlExpr(ShlExpr v) {
                handleBinop(v, true);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseShrExpr(ShrExpr v) {
                handleBinop(v, true);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseUshrExpr(UshrExpr v) {
                handleBinop(v, true);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseSubExpr(SubExpr v) {
                handleBinop(v, true);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseXorExpr(XorExpr v) {
                handleBinop(v, false);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
                handleUnknown(v);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
                handleUnknown(v);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseStaticInvokeExpr(StaticInvokeExpr v) {
                handleUnknown(v);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
                handleUnknown(v);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseCastExpr(CastExpr v) {
                setResult(ValueGraph.this.fetchNode(v.getOp()));
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseInstanceOfExpr(InstanceOfExpr v) {
                Node nop1 = ValueGraph.this.fetchNode(v.getOp());
                Value op2 = new TypeValueWrapper(v.getCheckType());
                Node nop2 = ValueGraph.this.fetchNode(op2);
                List<Node> children = new ArrayList<>();
                children.add(nop1);
                children.add(nop2);
                setResult(new Node(v, true, children));
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseNewArrayExpr(NewArrayExpr v) {
                handleUnknown(v);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
                handleUnknown(v);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseNewExpr(NewExpr v) {
                handleUnknown(v);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseLengthExpr(LengthExpr v) {
                handleUnop(v);
            }

            @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
            public void caseNegExpr(NegExpr v) {
                handleUnop(v);
            }

            @Override // soot.shimple.AbstractShimpleValueSwitch, soot.shimple.ShimpleExprSwitch
            public void casePhiExpr(PhiExpr v) {
                List<Node> children = new ArrayList<>();
                for (Value arg : v.getValues()) {
                    children.add(ValueGraph.this.fetchNode(arg));
                }
                setResult(new Node(v, true, children));
            }
        };
        value.apply(vs);
        return (Node) vs.getResult();
    }

    public Node getNode(Value local) {
        return this.localToNode.get(local);
    }

    public Collection<Node> getTopNodes() {
        return this.localToNode.values();
    }

    public Local getLocal(Node node) {
        return (Local) this.nodeToLocal.get(node);
    }

    public String toString() {
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < this.nodeList.size(); i++) {
            tmp.append(this.nodeList.get(i));
            tmp.append("\n");
        }
        return tmp.toString();
    }

    public static void main(String[] args) {
        Scene.v().loadClassAndSupport(args[0]);
        SootClass sc = Scene.v().getSootClass(args[0]);
        SootMethod sm = sc.getMethod(args[1]);
        Body b = sm.retrieveActiveBody();
        ShimpleBody sb = Shimple.v().newBody(b);
        CompleteBlockGraph cfg = new CompleteBlockGraph(sb);
        ValueGraph vg = new ValueGraph(cfg);
        System.out.println(vg);
    }

    /* loaded from: gencallgraphv3.jar:soot/shimple/toolkits/graph/ValueGraph$Node.class */
    public class Node {
        protected int nodeNumber;
        protected Value node;
        protected String nodeLabel;
        protected boolean ordered;
        protected List<Node> children;
        protected boolean stub;

        protected Node(Value local, boolean ignored) {
            this.stub = false;
            this.stub = true;
            setNode(local);
        }

        protected void patchStubs() {
            if (isStub()) {
                throw new RuntimeException("Assertion failed.");
            }
            for (int i = 0; i < this.children.size(); i++) {
                Node child = this.children.get(i);
                if (child.isStub()) {
                    Node newChild = ValueGraph.this.localToNode.get(child.node);
                    if (newChild == null || newChild.isStub()) {
                        throw new RuntimeException("Assertion failed.");
                    }
                    this.children.set(i, newChild);
                }
            }
        }

        protected void checkIfStub() {
            if (isStub()) {
                throw new RuntimeException("Assertion failed:  Attempted operation on invalid node (stub)");
            }
        }

        protected Node(ValueGraph valueGraph, Value node) {
            this(node, true, Collections.emptyList());
        }

        protected Node(Value node, boolean ordered, List<Node> children) {
            this.stub = false;
            setNode(node);
            setOrdered(ordered);
            setChildren(children);
            int i = ValueGraph.this.currentNodeNumber;
            ValueGraph.this.currentNodeNumber = i + 1;
            this.nodeNumber = i;
            updateLabel();
            ValueGraph.this.nodeList.add(this.nodeNumber, this);
        }

        protected void setNode(Value node) {
            this.node = node;
        }

        protected void setOrdered(boolean ordered) {
            this.ordered = ordered;
        }

        protected void setChildren(List<Node> children) {
            this.children = children;
        }

        protected void updateLabel() {
            if (!this.children.isEmpty()) {
                this.nodeLabel = this.node.getClass().getName();
                if (this.node instanceof PhiExpr) {
                    this.nodeLabel = String.valueOf(this.nodeLabel) + ((PhiExpr) this.node).getBlockId();
                    return;
                }
                return;
            }
            this.nodeLabel = this.node.toString();
            if ((this.node instanceof NewExpr) || (this.node instanceof NewArrayExpr) || (this.node instanceof NewMultiArrayExpr) || (this.node instanceof Ref) || (this.node instanceof InvokeExpr)) {
                this.nodeLabel = String.valueOf(this.nodeLabel) + Instruction.argsep + getNodeNumber();
            }
        }

        public boolean isStub() {
            return this.stub;
        }

        public String getLabel() {
            checkIfStub();
            return this.nodeLabel;
        }

        public boolean isOrdered() {
            checkIfStub();
            return this.ordered;
        }

        public List<Node> getChildren() {
            checkIfStub();
            return this.children;
        }

        public int getNodeNumber() {
            checkIfStub();
            return this.nodeNumber;
        }

        public String toString() {
            checkIfStub();
            StringBuffer tmp = new StringBuffer();
            Local local = ValueGraph.this.getLocal(this);
            if (local != null) {
                tmp.append(local.toString());
            }
            tmp.append("\tNode " + getNodeNumber() + ": " + getLabel());
            List<Node> children = getChildren();
            if (!children.isEmpty()) {
                tmp.append(" [" + (isOrdered() ? "ordered" : "unordered") + ": ");
                for (int i = 0; i < children.size(); i++) {
                    if (i != 0) {
                        tmp.append(", ");
                    }
                    tmp.append(children.get(i).getNodeNumber());
                }
                tmp.append("]");
            }
            return tmp.toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/shimple/toolkits/graph/ValueGraph$TypeValueWrapper.class */
    protected static class TypeValueWrapper implements Value {
        protected Type type;

        protected TypeValueWrapper(Type type) {
            this.type = type;
        }

        @Override // soot.Value
        public List<ValueBox> getUseBoxes() {
            return Collections.emptyList();
        }

        @Override // soot.Value
        public Type getType() {
            return this.type;
        }

        @Override // soot.Value
        public Object clone() {
            return new TypeValueWrapper(this.type);
        }

        @Override // soot.Value
        public void toString(UnitPrinter up) {
            up.literal("[Wrapped] " + this.type);
        }

        @Override // soot.util.Switchable
        public void apply(Switch sw) {
            throw new RuntimeException("Not Implemented.");
        }

        public boolean equals(Object o) {
            if (!(o instanceof TypeValueWrapper)) {
                return false;
            }
            return getType().equals(((TypeValueWrapper) o).getType());
        }

        public int hashCode() {
            return getType().hashCode();
        }

        @Override // soot.EquivTo
        public boolean equivTo(Object o) {
            return equals(o);
        }

        @Override // soot.EquivTo
        public int equivHashCode() {
            return hashCode();
        }
    }
}
