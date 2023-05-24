package soot.dava.toolkits.base.AST.structuredAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import soot.Local;
import soot.SootField;
import soot.Value;
import soot.dava.DavaFlowAnalysisException;
import soot.dava.internal.AST.ASTUnaryBinaryCondition;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/structuredAnalysis/MustMayInitialize.class */
public class MustMayInitialize extends StructuredAnalysis {
    HashMap<Object, List> mapping = new HashMap<>();
    DavaFlowSet finalResult;
    public static final int MUST = 0;
    public static final int MAY = 1;
    int MUSTMAY;

    public MustMayInitialize(Object analyze, int MUSTorMAY) {
        this.MUSTMAY = MUSTorMAY;
        setMergeType();
        this.finalResult = process(analyze, new DavaFlowSet());
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet emptyFlowSet() {
        return new DavaFlowSet();
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public void setMergeType() {
        if (this.MUSTMAY == 0) {
            this.MERGETYPE = 2;
        } else if (this.MUSTMAY == 1) {
            this.MERGETYPE = 1;
        } else {
            throw new DavaFlowAnalysisException("Only allowed 0 or 1 for MUST or MAY values");
        }
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
    public DavaFlowSet processStatement(Stmt s, DavaFlowSet inSet) {
        List<Stmt> defs;
        List<Stmt> defs2;
        if (inSet == this.NOPATH) {
            return inSet;
        }
        if (s instanceof DefinitionStmt) {
            DavaFlowSet toReturn = cloneFlowSet(inSet);
            Value leftOp = ((DefinitionStmt) s).getLeftOp();
            if (leftOp instanceof Local) {
                toReturn.add(leftOp);
                List<Stmt> temp = this.mapping.get(leftOp);
                if (temp == null) {
                    defs2 = new ArrayList<>();
                } else {
                    defs2 = (ArrayList) temp;
                }
                defs2.add(s);
                this.mapping.put(leftOp, defs2);
            } else if (leftOp instanceof FieldRef) {
                SootField field = ((FieldRef) leftOp).getField();
                toReturn.add(field);
                List<Stmt> temp2 = this.mapping.get(field);
                if (temp2 == null) {
                    defs = new ArrayList<>();
                } else {
                    defs = (ArrayList) temp2;
                }
                defs.add(s);
                this.mapping.put(field, defs);
            }
            return toReturn;
        }
        return inSet;
    }

    public boolean isMayInitialized(SootField field) {
        if (this.MUSTMAY == 1) {
            Object temp = this.mapping.get(field);
            if (temp == null) {
                return false;
            }
            List list = (List) temp;
            if (list.size() == 0) {
                return false;
            }
            return true;
        }
        throw new RuntimeException("Cannot invoke isMayInitialized for a MUST analysis");
    }

    public boolean isMayInitialized(Value local) {
        if (this.MUSTMAY == 1) {
            Object temp = this.mapping.get(local);
            if (temp == null) {
                return false;
            }
            List list = (List) temp;
            if (list.size() == 0) {
                return false;
            }
            return true;
        }
        throw new RuntimeException("Cannot invoke isMayInitialized for a MUST analysis");
    }

    public boolean isMustInitialized(SootField field) {
        if (this.MUSTMAY == 0) {
            if (this.finalResult.contains(field)) {
                return true;
            }
            return false;
        }
        throw new RuntimeException("Cannot invoke isMustinitialized for a MAY analysis");
    }

    public boolean isMustInitialized(Value local) {
        if (this.MUSTMAY == 0) {
            if (this.finalResult.contains(local)) {
                return true;
            }
            return false;
        }
        throw new RuntimeException("Cannot invoke isMustinitialized for a MAY analysis");
    }

    public List getDefs(Value local) {
        Object temp = this.mapping.get(local);
        if (temp == null) {
            return null;
        }
        return (List) temp;
    }

    public List getDefs(SootField field) {
        Object temp = this.mapping.get(field);
        if (temp == null) {
            return null;
        }
        return (List) temp;
    }
}
