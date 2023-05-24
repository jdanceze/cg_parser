package soot.dava.toolkits.base.AST.structuredAnalysis;

import java.util.Iterator;
import soot.Local;
import soot.Value;
import soot.dava.internal.AST.ASTUnaryBinaryCondition;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/structuredAnalysis/ReachingCopies.class */
public class ReachingCopies extends StructuredAnalysis {

    /* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/structuredAnalysis/ReachingCopies$LocalPair.class */
    public class LocalPair {
        private final Local leftLocal;
        private final Local rightLocal;

        public LocalPair(Local left, Local right) {
            this.leftLocal = left;
            this.rightLocal = right;
        }

        public Local getLeftLocal() {
            return this.leftLocal;
        }

        public Local getRightLocal() {
            return this.rightLocal;
        }

        public boolean equals(Object other) {
            if ((other instanceof LocalPair) && this.leftLocal.toString().equals(((LocalPair) other).getLeftLocal().toString()) && this.rightLocal.toString().equals(((LocalPair) other).getRightLocal().toString())) {
                return true;
            }
            return false;
        }

        public boolean contains(Local local) {
            if (this.leftLocal.toString().equals(local.toString()) || this.rightLocal.toString().equals(local.toString())) {
                return true;
            }
            return false;
        }

        public String toString() {
            StringBuffer b = new StringBuffer();
            b.append("<" + this.leftLocal.toString() + "," + this.rightLocal.toString() + ">");
            return b.toString();
        }
    }

    public ReachingCopies(Object analyze) {
        process(analyze, new DavaFlowSet());
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet emptyFlowSet() {
        return new DavaFlowSet();
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public void setMergeType() {
        this.MERGETYPE = 2;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet newInitialFlow() {
        return new DavaFlowSet();
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet cloneFlowSet(DavaFlowSet flowSet) {
        return flowSet.mo2534clone();
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processUnaryBinaryCondition(ASTUnaryBinaryCondition cond, DavaFlowSet input) {
        return input;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processSynchronizedLocal(Local local, DavaFlowSet input) {
        return input;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processSwitchKey(Value key, DavaFlowSet input) {
        return input;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet processStatement(Stmt s, DavaFlowSet input) {
        if (input == this.NOPATH) {
            return input;
        }
        if (s instanceof DefinitionStmt) {
            DavaFlowSet toReturn = cloneFlowSet(input);
            Value leftOp = ((DefinitionStmt) s).getLeftOp();
            Value rightOp = ((DefinitionStmt) s).getRightOp();
            if (leftOp instanceof Local) {
                kill(toReturn, (Local) leftOp);
            }
            if ((leftOp instanceof Local) && (rightOp instanceof Local)) {
                gen(toReturn, (Local) leftOp, (Local) rightOp);
            }
            return toReturn;
        }
        return input;
    }

    public void gen(DavaFlowSet in, Local left, Local right) {
        LocalPair localp = new LocalPair(left, right);
        in.add(localp);
    }

    public void kill(DavaFlowSet<LocalPair> in, Local redefined) {
        Iterator<LocalPair> listIt = in.iterator();
        while (listIt.hasNext()) {
            LocalPair tempPair = listIt.next();
            if (tempPair.contains(redefined)) {
                listIt.remove();
            }
        }
    }

    public DavaFlowSet getReachingCopies(Object node) {
        DavaFlowSet beforeSet = getBeforeSet(node);
        if (beforeSet == null) {
            throw new RuntimeException("Could not get reaching copies of node/stmt");
        }
        return beforeSet;
    }
}
