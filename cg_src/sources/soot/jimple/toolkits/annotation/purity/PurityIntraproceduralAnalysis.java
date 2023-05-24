package soot.jimple.toolkits.annotation.purity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.RefLikeType;
import soot.SourceLocator;
import soot.Unit;
import soot.Value;
import soot.coffi.Instruction;
import soot.jimple.AnyNewExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.BreakpointStmt;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.Constant;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceOfExpr;
import soot.jimple.Jimple;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.MonitorStmt;
import soot.jimple.NopStmt;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.jimple.UnopExpr;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.util.dot.DotGraph;
import soot.util.dot.DotGraphConstants;
import soot.util.dot.DotGraphEdge;
import soot.util.dot.DotGraphNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/purity/PurityIntraproceduralAnalysis.class */
public class PurityIntraproceduralAnalysis extends ForwardFlowAnalysis<Unit, PurityGraphBox> {
    private static final Logger logger = LoggerFactory.getLogger(PurityIntraproceduralAnalysis.class);
    private final AbstractInterproceduralAnalysis<PurityGraphBox> inter;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PurityIntraproceduralAnalysis(UnitGraph g, AbstractInterproceduralAnalysis<PurityGraphBox> inter) {
        super(g);
        this.inter = inter;
        doAnalysis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public PurityGraphBox newInitialFlow() {
        return new PurityGraphBox();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public PurityGraphBox entryInitialFlow() {
        return new PurityGraphBox();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(PurityGraphBox in1, PurityGraphBox in2, PurityGraphBox out) {
        if (out != in1) {
            out.g = new PurityGraph(in1.g);
        }
        out.g.union(in2.g);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(PurityGraphBox source, PurityGraphBox dest) {
        dest.g = new PurityGraph(source.g);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(PurityGraphBox inValue, Unit unit, PurityGraphBox outValue) {
        Stmt stmt = (Stmt) unit;
        outValue.g = new PurityGraph(inValue.g);
        if (stmt.containsInvokeExpr()) {
            this.inter.analyseCall(inValue, stmt, outValue);
        } else if (stmt instanceof AssignStmt) {
            Value leftOp = ((AssignStmt) stmt).getLeftOp();
            Value rightOp = ((AssignStmt) stmt).getRightOp();
            if (leftOp instanceof Local) {
                if (rightOp instanceof CastExpr) {
                    rightOp = ((CastExpr) rightOp).getOp();
                }
                Local left = (Local) leftOp;
                if (left.getType() instanceof RefLikeType) {
                    if (rightOp instanceof Local) {
                        outValue.g.assignLocalToLocal((Local) rightOp, left);
                    } else if (rightOp instanceof ArrayRef) {
                        outValue.g.assignFieldToLocal(stmt, (Local) ((ArrayRef) rightOp).getBase(), "[]", left);
                    } else if (rightOp instanceof InstanceFieldRef) {
                        outValue.g.assignFieldToLocal(stmt, (Local) ((InstanceFieldRef) rightOp).getBase(), ((InstanceFieldRef) rightOp).getField().getName(), left);
                    } else if (rightOp instanceof StaticFieldRef) {
                        outValue.g.localIsUnknown(left);
                    } else if (!(rightOp instanceof Constant)) {
                        if (rightOp instanceof AnyNewExpr) {
                            outValue.g.assignNewToLocal(stmt, left);
                        } else if (!(rightOp instanceof BinopExpr) && !(rightOp instanceof UnopExpr) && !(rightOp instanceof InstanceOfExpr)) {
                            throw new Error("AssignStmt match failure (rightOp)" + stmt);
                        }
                    }
                }
            } else if (leftOp instanceof ArrayRef) {
                Local left2 = (Local) ((ArrayRef) leftOp).getBase();
                if (!(rightOp instanceof Local)) {
                    if (rightOp instanceof Constant) {
                        outValue.g.mutateField(left2, "[]");
                        return;
                    }
                    throw new Error("AssignStmt match failure (rightOp)" + stmt);
                }
                Local right = (Local) rightOp;
                if (right.getType() instanceof RefLikeType) {
                    outValue.g.assignLocalToField(right, left2, "[]");
                } else {
                    outValue.g.mutateField(left2, "[]");
                }
            } else if (leftOp instanceof InstanceFieldRef) {
                Local left3 = (Local) ((InstanceFieldRef) leftOp).getBase();
                String field = ((InstanceFieldRef) leftOp).getField().getName();
                if (!(rightOp instanceof Local)) {
                    if (rightOp instanceof Constant) {
                        outValue.g.mutateField(left3, field);
                        return;
                    }
                    throw new Error("AssignStmt match failure (rightOp) " + stmt);
                }
                Local right2 = (Local) rightOp;
                if (right2.getType() instanceof RefLikeType) {
                    outValue.g.assignLocalToField(right2, left3, field);
                } else {
                    outValue.g.mutateField(left3, field);
                }
            } else if (leftOp instanceof StaticFieldRef) {
                String field2 = ((StaticFieldRef) leftOp).getField().getName();
                if (!(rightOp instanceof Local)) {
                    if (rightOp instanceof Constant) {
                        outValue.g.mutateStaticField(field2);
                        return;
                    }
                    throw new Error("AssignStmt match failure (rightOp) " + stmt);
                }
                Local right3 = (Local) rightOp;
                if (right3.getType() instanceof RefLikeType) {
                    outValue.g.assignLocalToStaticField(right3, field2);
                } else {
                    outValue.g.mutateStaticField(field2);
                }
            } else {
                throw new Error("AssignStmt match failure (leftOp) " + stmt);
            }
        } else if (stmt instanceof IdentityStmt) {
            Local left4 = (Local) ((IdentityStmt) stmt).getLeftOp();
            Value rightOp2 = ((IdentityStmt) stmt).getRightOp();
            if (rightOp2 instanceof ThisRef) {
                outValue.g.assignThisToLocal(left4);
            } else if (!(rightOp2 instanceof ParameterRef)) {
                if (rightOp2 instanceof CaughtExceptionRef) {
                    outValue.g.localIsUnknown(left4);
                    return;
                }
                throw new Error("IdentityStmt match failure (rightOp) " + stmt);
            } else {
                ParameterRef p = (ParameterRef) rightOp2;
                if (p.getType() instanceof RefLikeType) {
                    outValue.g.assignParamToLocal(p.getIndex(), left4);
                }
            }
        } else if (stmt instanceof ThrowStmt) {
            Value op = ((ThrowStmt) stmt).getOp();
            if (op instanceof Local) {
                outValue.g.localEscapes((Local) op);
            } else if (!(op instanceof Constant)) {
                throw new Error("ThrowStmt match failure " + stmt);
            }
        } else if (!(stmt instanceof ReturnVoidStmt)) {
            if (stmt instanceof ReturnStmt) {
                Value v = ((ReturnStmt) stmt).getOp();
                if (!(v instanceof Local)) {
                    if (!(v instanceof Constant)) {
                        throw new Error("ReturnStmt match failure " + stmt);
                    }
                } else if (v.getType() instanceof RefLikeType) {
                    outValue.g.returnLocal((Local) v);
                }
            } else if (!(stmt instanceof IfStmt) && !(stmt instanceof GotoStmt) && !(stmt instanceof LookupSwitchStmt) && !(stmt instanceof TableSwitchStmt) && !(stmt instanceof MonitorStmt) && !(stmt instanceof BreakpointStmt) && !(stmt instanceof NopStmt)) {
                throw new Error("Stmt match faliure " + stmt);
            }
        }
    }

    public void drawAsOneDot(String prefix, String name) {
        DotGraph dot = new DotGraph(name);
        dot.setGraphLabel(name);
        dot.setGraphAttribute("compound", "true");
        dot.setGraphAttribute("rankdir", "LR");
        Map<Unit, Integer> node = new HashMap<>();
        int id = 0;
        for (Unit stmt : this.graph) {
            DotGraph sub = dot.createSubGraph("cluster" + id);
            DotGraphNode label = sub.drawNode("head" + id);
            String lbl = stmt.toString();
            if (lbl.startsWith(Jimple.LOOKUPSWITCH)) {
                lbl = "lookupswitch...";
            } else if (lbl.startsWith(Jimple.TABLESWITCH)) {
                lbl = "tableswitch...";
            }
            sub.setGraphLabel(Instruction.argsep);
            label.setLabel(lbl);
            label.setAttribute("fontsize", "18");
            label.setShape(DotGraphConstants.NODE_SHAPE_BOX);
            getFlowAfter(stmt).g.fillDotGraph("X" + id, sub);
            node.put(stmt, Integer.valueOf(id));
            id++;
        }
        for (Unit src : this.graph) {
            for (Unit dst : this.graph.getSuccsOf(src)) {
                DotGraphEdge edge = dot.drawEdge("head" + node.get(src), "head" + node.get(dst));
                edge.setAttribute("ltail", "cluster" + node.get(src));
                edge.setAttribute("lhead", "cluster" + node.get(dst));
            }
        }
        File f = new File(SourceLocator.v().getOutputDir(), String.valueOf(prefix) + name + DotGraph.DOT_EXTENSION);
        dot.plot(f.getPath());
    }

    public void copyResult(PurityGraphBox dst) {
        PurityGraph r = new PurityGraph();
        for (Unit u : this.graph.getTails()) {
            r.union(getFlowAfter(u).g);
        }
        r.removeLocals();
        dst.g = r;
    }
}
