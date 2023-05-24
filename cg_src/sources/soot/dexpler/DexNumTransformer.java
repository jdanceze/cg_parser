package soot.dexpler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.DoubleType;
import soot.FloatType;
import soot.Local;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.dexpler.tags.DoubleOpTag;
import soot.dexpler.tags.FloatOpTag;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CmpExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.DoubleConstant;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.IdentityStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.ReturnStmt;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexNumTransformer.class */
public class DexNumTransformer extends DexTransformer {
    private boolean usedAsFloatingPoint;
    boolean doBreak = false;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DexNumTransformer.class.desiredAssertionStatus();
    }

    public static DexNumTransformer v() {
        return new DexNumTransformer();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
        final DexDefUseAnalysis localDefs = new DexDefUseAnalysis(body);
        for (Local loc : getNumCandidates(body)) {
            this.usedAsFloatingPoint = false;
            Set<Unit> defs = localDefs.collectDefinitionsWithAliases(loc);
            this.doBreak = false;
            for (Unit u : defs) {
                final Local l = u instanceof DefinitionStmt ? (Local) ((DefinitionStmt) u).getLeftOp() : null;
                u.apply(new AbstractStmtSwitch() { // from class: soot.dexpler.DexNumTransformer.1
                    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                    public void caseAssignStmt(AssignStmt stmt) {
                        Value r = stmt.getRightOp();
                        if ((r instanceof BinopExpr) && !(r instanceof CmpExpr)) {
                            DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.examineBinopExpr(stmt);
                            DexNumTransformer.this.doBreak = true;
                        } else if (r instanceof FieldRef) {
                            DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.isFloatingPointLike(((FieldRef) r).getFieldRef().type());
                            DexNumTransformer.this.doBreak = true;
                        } else if (r instanceof NewArrayExpr) {
                            NewArrayExpr nae = (NewArrayExpr) r;
                            Type t = nae.getType();
                            DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.isFloatingPointLike(t);
                            DexNumTransformer.this.doBreak = true;
                        } else if (r instanceof ArrayRef) {
                            ArrayRef ar = (ArrayRef) r;
                            Type arType = ar.getType();
                            if (arType instanceof UnknownType) {
                                Type t2 = DexNumTransformer.this.findArrayType(localDefs, stmt, 0, Collections.emptySet());
                                DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.isFloatingPointLike(t2);
                            } else {
                                DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.isFloatingPointLike(ar.getType());
                            }
                            DexNumTransformer.this.doBreak = true;
                        } else if (r instanceof CastExpr) {
                            DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.isFloatingPointLike(((CastExpr) r).getCastType());
                            DexNumTransformer.this.doBreak = true;
                        } else if (r instanceof InvokeExpr) {
                            DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.isFloatingPointLike(((InvokeExpr) r).getType());
                            DexNumTransformer.this.doBreak = true;
                        } else if (r instanceof LengthExpr) {
                            DexNumTransformer.this.usedAsFloatingPoint = false;
                            DexNumTransformer.this.doBreak = true;
                        }
                    }

                    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                    public void caseIdentityStmt(IdentityStmt stmt) {
                        if (stmt.getLeftOp() == l) {
                            DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.isFloatingPointLike(stmt.getRightOp().getType());
                            DexNumTransformer.this.doBreak = true;
                        }
                    }
                });
                if (this.doBreak) {
                    break;
                }
                for (Unit use : localDefs.getUsesOf(l)) {
                    use.apply(new AbstractStmtSwitch() { // from class: soot.dexpler.DexNumTransformer.2
                        private boolean examineInvokeExpr(InvokeExpr e) {
                            List<Value> args = e.getArgs();
                            List<Type> argTypes = e.getMethodRef().parameterTypes();
                            if (DexNumTransformer.$assertionsDisabled || args.size() == argTypes.size()) {
                                for (int i = 0; i < args.size(); i++) {
                                    if (args.get(i) == l && DexNumTransformer.this.isFloatingPointLike(argTypes.get(i))) {
                                        return true;
                                    }
                                }
                                return false;
                            }
                            throw new AssertionError();
                        }

                        @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                        public void caseInvokeStmt(InvokeStmt stmt) {
                            InvokeExpr e = stmt.getInvokeExpr();
                            DexNumTransformer.this.usedAsFloatingPoint = examineInvokeExpr(e);
                        }

                        @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                        public void caseAssignStmt(AssignStmt stmt) {
                            Value left = stmt.getLeftOp();
                            if (left instanceof ArrayRef) {
                                ArrayRef ar = (ArrayRef) left;
                                if (ar.getIndex() == l) {
                                    DexNumTransformer.this.doBreak = true;
                                    return;
                                }
                            }
                            Value r = stmt.getRightOp();
                            if (r instanceof ArrayRef) {
                                if (((ArrayRef) r).getIndex() == l) {
                                    DexNumTransformer.this.doBreak = true;
                                }
                            } else if (r instanceof InvokeExpr) {
                                DexNumTransformer.this.usedAsFloatingPoint = examineInvokeExpr((InvokeExpr) r);
                                DexNumTransformer.this.doBreak = true;
                            } else if (r instanceof BinopExpr) {
                                DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.examineBinopExpr(stmt);
                                DexNumTransformer.this.doBreak = true;
                            } else if (r instanceof CastExpr) {
                                DexNumTransformer.this.usedAsFloatingPoint = stmt.hasTag(FloatOpTag.NAME) || stmt.hasTag(DoubleOpTag.NAME);
                                DexNumTransformer.this.doBreak = true;
                            } else if ((r instanceof Local) && r == l) {
                                if (left instanceof FieldRef) {
                                    FieldRef fr = (FieldRef) left;
                                    if (DexNumTransformer.this.isFloatingPointLike(fr.getType())) {
                                        DexNumTransformer.this.usedAsFloatingPoint = true;
                                    }
                                    DexNumTransformer.this.doBreak = true;
                                } else if (left instanceof ArrayRef) {
                                    ArrayRef ar2 = (ArrayRef) left;
                                    Type arType = ar2.getType();
                                    if (arType instanceof UnknownType) {
                                        arType = DexNumTransformer.this.findArrayType(localDefs, stmt, 0, Collections.emptySet());
                                    }
                                    DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.isFloatingPointLike(arType);
                                    DexNumTransformer.this.doBreak = true;
                                }
                            }
                        }

                        @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                        public void caseReturnStmt(ReturnStmt stmt) {
                            DexNumTransformer.this.usedAsFloatingPoint = stmt.getOp() == l && DexNumTransformer.this.isFloatingPointLike(body.getMethod().getReturnType());
                            DexNumTransformer.this.doBreak = true;
                        }
                    });
                    if (this.doBreak) {
                        break;
                    }
                }
                if (this.doBreak) {
                    break;
                }
            }
            if (this.usedAsFloatingPoint) {
                for (Unit u2 : defs) {
                    replaceWithFloatingPoint(u2);
                }
            }
        }
    }

    protected boolean examineBinopExpr(Unit u) {
        return u.hasTag(FloatOpTag.NAME) || u.hasTag(DoubleOpTag.NAME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFloatingPointLike(Type t) {
        return (t instanceof FloatType) || (t instanceof DoubleType);
    }

    private Set<Local> getNumCandidates(Body body) {
        Set<Local> candidates = new HashSet<>();
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof AssignStmt) {
                AssignStmt a = (AssignStmt) u;
                if (a.getLeftOp() instanceof Local) {
                    Local l = (Local) a.getLeftOp();
                    Value r = a.getRightOp();
                    if ((r instanceof IntConstant) || (r instanceof LongConstant)) {
                        candidates.add(l);
                    }
                }
            }
        }
        return candidates;
    }

    private void replaceWithFloatingPoint(Unit u) {
        if (u instanceof AssignStmt) {
            AssignStmt s = (AssignStmt) u;
            Value v = s.getRightOp();
            if (v instanceof IntConstant) {
                int vVal = ((IntConstant) v).value;
                s.setRightOp(FloatConstant.v(Float.intBitsToFloat(vVal)));
            } else if (v instanceof LongConstant) {
                long vVal2 = ((LongConstant) v).value;
                s.setRightOp(DoubleConstant.v(Double.longBitsToDouble(vVal2)));
            }
        }
    }
}
