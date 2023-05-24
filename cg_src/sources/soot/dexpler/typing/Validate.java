package soot.dexpler.typing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.ArrayType;
import soot.Body;
import soot.G;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethodRef;
import soot.SootResolver;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NewArrayExpr;
import soot.jimple.StringConstant;
import soot.jimple.ThrowStmt;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.jimple.toolkits.scalar.NopEliminator;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.UnusedLocalEliminator;
/* loaded from: gencallgraphv3.jar:soot/dexpler/typing/Validate.class */
public class Validate {
    public static void validateArrays(Body b) {
        boolean isMore;
        Value r;
        Set<DefinitionStmt> definitions = new HashSet<>();
        Set<Unit> unitWithArrayRef = new HashSet<>();
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof DefinitionStmt) {
                DefinitionStmt s = (DefinitionStmt) u;
                definitions.add(s);
            }
            List<ValueBox> uses = u.getUseBoxes();
            for (ValueBox vb : uses) {
                if (vb.getValue() instanceof ArrayRef) {
                    unitWithArrayRef.add(u);
                }
            }
        }
        LocalDefs localDefs = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(b, true);
        Set<Unit> toReplace = new HashSet<>();
        for (Unit u2 : unitWithArrayRef) {
            boolean ok = false;
            List<ValueBox> uses2 = u2.getUseBoxes();
            for (ValueBox vb2 : uses2) {
                Value v = vb2.getValue();
                if (v instanceof ArrayRef) {
                    ArrayRef ar = (ArrayRef) v;
                    Local base = (Local) ar.getBase();
                    List<Unit> defs = localDefs.getDefsOfAt(base, u2);
                    Set<Unit> alreadyHandled = new HashSet<>();
                    do {
                        isMore = false;
                        Iterator<Unit> it2 = defs.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                break;
                            }
                            Unit d = it2.next();
                            if (!alreadyHandled.contains(d) && (d instanceof AssignStmt)) {
                                AssignStmt ass = (AssignStmt) d;
                                Value r2 = ass.getRightOp();
                                if (r2 instanceof Local) {
                                    defs.addAll(localDefs.getDefsOfAt((Local) r2, d));
                                    alreadyHandled.add(d);
                                    isMore = true;
                                    break;
                                } else if (r2 instanceof ArrayRef) {
                                    ArrayRef arrayRef = (ArrayRef) r2;
                                    Local l = (Local) arrayRef.getBase();
                                    defs.addAll(localDefs.getDefsOfAt(l, d));
                                    alreadyHandled.add(d);
                                    isMore = true;
                                    break;
                                }
                            }
                        }
                    } while (isMore);
                    for (Unit def : defs) {
                        if (def instanceof IdentityStmt) {
                            IdentityStmt idstmt = (IdentityStmt) def;
                            r = idstmt.getRightOp();
                        } else if (def instanceof AssignStmt) {
                            AssignStmt assStmt = (AssignStmt) def;
                            r = assStmt.getRightOp();
                        } else {
                            throw new RuntimeException("error: definition statement not an IdentityStmt nor an AssignStmt! " + def);
                        }
                        if (r instanceof InvokeExpr) {
                            InvokeExpr ie = (InvokeExpr) r;
                            Type t = ie.getType();
                            if (t instanceof ArrayType) {
                                ok = true;
                            }
                        } else if (r instanceof FieldRef) {
                            FieldRef ref = (FieldRef) r;
                            Type t2 = ref.getType();
                            if (t2 instanceof ArrayType) {
                                ok = true;
                            }
                        } else if (r instanceof IdentityRef) {
                            IdentityRef ir = (IdentityRef) r;
                            Type t3 = ir.getType();
                            if (t3 instanceof ArrayType) {
                                ok = true;
                            }
                        } else if (r instanceof CastExpr) {
                            CastExpr c = (CastExpr) r;
                            Type t4 = c.getType();
                            if (t4 instanceof ArrayType) {
                                ok = true;
                            }
                        } else if (!(r instanceof ArrayRef)) {
                            if (r instanceof NewArrayExpr) {
                                ok = true;
                            } else if (!(r instanceof Local) && !(r instanceof Constant)) {
                                throw new RuntimeException("error: unknown right hand side of definition stmt " + def);
                            }
                        }
                        if (ok) {
                            break;
                        }
                    }
                    if (ok) {
                        break;
                    }
                }
            }
            if (!ok) {
                toReplace.add(u2);
            }
        }
        int i = 0;
        for (Unit u3 : toReplace) {
            System.out.println("warning: incorrect array def, replacing unit " + u3);
            RefType throwableType = RefType.v("java.lang.Throwable");
            i++;
            Local ttt = Jimple.v().newLocal("ttt_" + i, throwableType);
            b.getLocals().add(ttt);
            Value r3 = Jimple.v().newNewExpr(throwableType);
            AssignStmt newAssignStmt = Jimple.v().newAssignStmt(ttt, r3);
            List<String> pTypes = new ArrayList<>();
            pTypes.add("java.lang.String");
            SootMethodRef mRef = makeMethodRef("java.lang.Throwable", "<init>", "", pTypes, false);
            List<Value> parameters = new ArrayList<>();
            parameters.add(StringConstant.v("Soot updated this instruction"));
            InvokeExpr ie2 = Jimple.v().newSpecialInvokeExpr(ttt, mRef, parameters);
            InvokeStmt newInvokeStmt = Jimple.v().newInvokeStmt(ie2);
            ThrowStmt newThrowStmt = Jimple.v().newThrowStmt(ttt);
            b.getUnits().swapWith(u3, (Unit) newThrowStmt);
            b.getUnits().insertBefore(newInvokeStmt, (InvokeStmt) newThrowStmt);
            b.getUnits().insertBefore(newAssignStmt, (AssignStmt) newInvokeStmt);
        }
        DeadAssignmentEliminator.v().transform(b);
        UnusedLocalEliminator.v().transform(b);
        NopEliminator.v().transform(b);
        UnreachableCodeEliminator.v().transform(b);
    }

    public static SootMethodRef makeMethodRef(String cName, String mName, String rType, List<String> pTypes, boolean isStatic) {
        Type returnType;
        SootClass sc = SootResolver.v().makeClassRef(cName);
        if (rType == "") {
            returnType = VoidType.v();
        } else {
            returnType = RefType.v(rType);
        }
        List<Type> parameterTypes = new ArrayList<>();
        for (String p : pTypes) {
            parameterTypes.add(RefType.v(p));
        }
        return Scene.v().makeMethodRef(sc, mName, parameterTypes, returnType, isStatic);
    }
}
