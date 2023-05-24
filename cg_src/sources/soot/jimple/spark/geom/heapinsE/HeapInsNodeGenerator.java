package soot.jimple.spark.geom.heapinsE;

import java.util.Iterator;
import soot.jimple.spark.geom.dataRep.CgEdge;
import soot.jimple.spark.geom.dataRep.PlainConstraint;
import soot.jimple.spark.geom.geomPA.Constants;
import soot.jimple.spark.geom.geomPA.DummyNode;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.geom.geomPA.IEncodingBroker;
import soot.jimple.spark.geom.geomPA.IVarAbstraction;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.toolkits.callgraph.Edge;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/heapinsE/HeapInsNodeGenerator.class */
public class HeapInsNodeGenerator extends IEncodingBroker {
    private static final int[] full_convertor = {0, 1, 1, 1};

    @Override // soot.jimple.spark.geom.geomPA.IEncodingBroker
    public void initFlowGraph(GeomPointsTo ptAnalyzer) {
        int n_legal_cons = 0;
        Iterator<PlainConstraint> it = ptAnalyzer.constraints.iterator();
        while (it.hasNext()) {
            PlainConstraint cons = it.next();
            if (cons.isActive) {
                IVarAbstraction my_lhs = cons.getLHS().getRepresentative();
                IVarAbstraction my_rhs = cons.getRHS().getRepresentative();
                int nf1 = ptAnalyzer.getMethodIDFromPtr(my_lhs);
                int nf2 = ptAnalyzer.getMethodIDFromPtr(my_rhs);
                int code = ((nf1 == 0 ? 1 : 0) << 1) | (nf2 == 0 ? 1 : 0);
                switch (cons.type) {
                    case 0:
                        my_rhs.add_points_to_3((AllocNode) my_lhs.getWrappedNode(), (code & 1) == 1 ? 0 : 1, (code >> 1) == 1 ? 0 : 1, (code & 1) == 1 ? ptAnalyzer.context_size[nf1] : ptAnalyzer.context_size[nf2]);
                        ptAnalyzer.getWorklist().push(my_rhs);
                        break;
                    case 1:
                        if (cons.interCallEdges != null) {
                            for (Edge sEdge : cons.interCallEdges) {
                                CgEdge q = ptAnalyzer.getInternalEdgeFromSootEdge(sEdge);
                                if (!q.is_obsoleted) {
                                    if (nf2 == q.t) {
                                        if (nf1 == 0) {
                                            my_lhs.add_simple_constraint_3(my_rhs, 0L, q.map_offset, ptAnalyzer.max_context_size_block[q.s]);
                                        } else if (q.s == q.t) {
                                            my_lhs.add_simple_constraint_3(my_rhs, 1L, 1L, ptAnalyzer.context_size[nf1]);
                                        } else {
                                            for (int k = 0; k < ptAnalyzer.block_num[nf1]; k++) {
                                                my_lhs.add_simple_constraint_3(my_rhs, (k * ptAnalyzer.max_context_size_block[nf1]) + 1, q.map_offset, ptAnalyzer.max_context_size_block[nf1]);
                                            }
                                        }
                                    } else if (q.s == q.t) {
                                        my_lhs.add_simple_constraint_3(my_rhs, 1L, 1L, ptAnalyzer.context_size[nf2]);
                                    } else {
                                        for (int k2 = 0; k2 < ptAnalyzer.block_num[nf2]; k2++) {
                                            my_lhs.add_simple_constraint_3(my_rhs, q.map_offset, (k2 * ptAnalyzer.max_context_size_block[nf2]) + 1, ptAnalyzer.max_context_size_block[nf2]);
                                        }
                                    }
                                }
                            }
                            break;
                        } else {
                            my_lhs.add_simple_constraint_3(my_rhs, nf1 == 0 ? 0 : 1, nf2 == 0 ? 0 : 1, nf1 == 0 ? ptAnalyzer.context_size[nf2] : ptAnalyzer.context_size[nf1]);
                            break;
                        }
                    case 2:
                        cons.code = full_convertor[code];
                        cons.otherSide = my_rhs;
                        my_lhs.put_complex_constraint(cons);
                        break;
                    case 3:
                        cons.code = full_convertor[code];
                        cons.otherSide = my_lhs;
                        my_rhs.put_complex_constraint(cons);
                        break;
                    default:
                        throw new RuntimeException("Invalid node type");
                }
                n_legal_cons++;
            }
        }
        ptAnalyzer.ps.printf("Only %d (%.1f%%) constraints are needed for this run.\n", Integer.valueOf(n_legal_cons), Double.valueOf((n_legal_cons / ptAnalyzer.n_init_constraints) * 100.0d));
    }

    @Override // soot.jimple.spark.geom.geomPA.IEncodingBroker
    public IVarAbstraction generateNode(Node vNode) {
        IVarAbstraction ret;
        if ((vNode instanceof AllocNode) || (vNode instanceof FieldRefNode)) {
            ret = new DummyNode(vNode);
        } else {
            ret = new HeapInsNode(vNode);
        }
        return ret;
    }

    @Override // soot.jimple.spark.geom.geomPA.IEncodingBroker
    public String getSignature() {
        return Constants.heapinsE;
    }
}
