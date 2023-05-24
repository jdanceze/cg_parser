package soot.jbco.jimpleTransformations;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.DoubleType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.PatchingChain;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.Rand;
import soot.jimple.AssignStmt;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.Expr;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.MulExpr;
import soot.jimple.NumericConstant;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jbco/jimpleTransformations/ArithmeticTransformer.class */
public class ArithmeticTransformer extends BodyTransformer implements IJbcoTransform {
    private static int mulPerformed = 0;
    private static int divPerformed = 0;
    private static int total = 0;
    public static String[] dependancies = {"jtp.jbco_cae2bo"};
    public static String name = "jtp.jbco_cae2bo";

    @Override // soot.jbco.IJbcoTransform
    public String[] getDependencies() {
        return dependancies;
    }

    @Override // soot.jbco.IJbcoTransform
    public String getName() {
        return name;
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        int shift;
        Expr e;
        NumericConstant nc;
        Unit newU;
        int shift2;
        int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
        if (weight == 0) {
            return;
        }
        PatchingChain<Unit> units = b.getUnits();
        int localCount = 0;
        Chain<Local> locals = b.getLocals();
        if (output) {
            out.println("*** Performing Arithmetic Transformation on " + b.getMethod().getSignature());
        }
        Iterator<Unit> it = units.snapshotIterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof AssignStmt) {
                AssignStmt as = (AssignStmt) u;
                Value v = as.getRightOp();
                if (v instanceof MulExpr) {
                    total++;
                    MulExpr me = (MulExpr) v;
                    Value op1 = me.getOp1();
                    Value op = null;
                    Value op2 = me.getOp2();
                    NumericConstant nc2 = null;
                    if (op1 instanceof NumericConstant) {
                        nc2 = (NumericConstant) op1;
                        op = op2;
                    } else if (op2 instanceof NumericConstant) {
                        nc2 = (NumericConstant) op2;
                        op = op1;
                    }
                    if (nc2 != null) {
                        if (output) {
                            out.println("Considering: " + as + "\r");
                        }
                        Type opType = op.getType();
                        int max = opType instanceof IntType ? 32 : opType instanceof LongType ? 64 : 0;
                        if (max != 0) {
                            Object[] shft_rem = checkNumericValue(nc2);
                            if (shft_rem[0] != null && ((Integer) shft_rem[0]).intValue() < max && Rand.getInt(10) <= weight) {
                                List<Unit> unitsBuilt = new ArrayList<>();
                                int rand = Rand.getInt(16);
                                int shift3 = ((Integer) shft_rem[0]).intValue();
                                boolean neg = ((Boolean) shft_rem[2]).booleanValue();
                                if (rand % 2 == 0) {
                                    shift = shift3 + (rand * max);
                                } else {
                                    shift = shift3 - (rand * max);
                                }
                                if (shft_rem[1] != null) {
                                    Local tmp2 = null;
                                    int i = localCount;
                                    localCount++;
                                    Local tmp1 = Jimple.v().newLocal("__tmp_shft_lcl" + i, opType);
                                    locals.add(tmp1);
                                    Unit newU2 = Jimple.v().newAssignStmt(tmp1, Jimple.v().newShlExpr(op, IntConstant.v(shift)));
                                    unitsBuilt.add(newU2);
                                    units.insertBefore(newU2, u);
                                    double rem = ((Double) shft_rem[1]).doubleValue();
                                    if (rem != 1.0d) {
                                        if (rem == ((int) rem) && (opType instanceof IntType)) {
                                            nc = IntConstant.v((int) rem);
                                        } else if (rem == ((long) rem) && (opType instanceof LongType)) {
                                            nc = LongConstant.v((long) rem);
                                        } else {
                                            nc = DoubleConstant.v(rem);
                                        }
                                        if (nc instanceof DoubleConstant) {
                                            localCount++;
                                            tmp2 = Jimple.v().newLocal("__tmp_shft_lcl" + localCount, DoubleType.v());
                                            locals.add(tmp2);
                                            Unit newU3 = Jimple.v().newAssignStmt(tmp2, Jimple.v().newCastExpr(op, DoubleType.v()));
                                            unitsBuilt.add(newU3);
                                            units.insertBefore(newU3, u);
                                            newU = Jimple.v().newAssignStmt(tmp2, Jimple.v().newMulExpr(tmp2, nc));
                                        } else {
                                            localCount++;
                                            tmp2 = Jimple.v().newLocal("__tmp_shft_lcl" + localCount, nc.getType());
                                            locals.add(tmp2);
                                            newU = Jimple.v().newAssignStmt(tmp2, Jimple.v().newMulExpr(op, nc));
                                        }
                                        unitsBuilt.add(newU);
                                        units.insertBefore(newU, u);
                                    }
                                    if (tmp2 == null) {
                                        e = Jimple.v().newAddExpr(tmp1, op);
                                    } else if (tmp2.getType().getClass() != tmp1.getType().getClass()) {
                                        int i2 = localCount;
                                        localCount++;
                                        Local tmp3 = Jimple.v().newLocal("__tmp_shft_lcl" + i2, tmp2.getType());
                                        locals.add(tmp3);
                                        Unit newU4 = Jimple.v().newAssignStmt(tmp3, Jimple.v().newCastExpr(tmp1, tmp2.getType()));
                                        unitsBuilt.add(newU4);
                                        units.insertBefore(newU4, u);
                                        e = Jimple.v().newAddExpr(tmp3, tmp2);
                                    } else {
                                        e = Jimple.v().newAddExpr(tmp1, tmp2);
                                    }
                                } else {
                                    e = Jimple.v().newShlExpr(op, IntConstant.v(shift));
                                }
                                Class<?> cls = e.getType().getClass();
                                Expr e2 = e;
                                if (cls != as.getLeftOp().getType().getClass()) {
                                    int i3 = localCount;
                                    localCount++;
                                    Local tmp = Jimple.v().newLocal("__tmp_shft_lcl" + i3, e.getType());
                                    locals.add(tmp);
                                    Unit newU5 = Jimple.v().newAssignStmt(tmp, e);
                                    unitsBuilt.add(newU5);
                                    units.insertAfter(newU5, u);
                                    e2 = Jimple.v().newCastExpr(tmp, as.getLeftOp().getType());
                                }
                                as.setRightOp(e2);
                                unitsBuilt.add(as);
                                if (neg) {
                                    Unit newU6 = Jimple.v().newAssignStmt(as.getLeftOp(), Jimple.v().newNegExpr(as.getLeftOp()));
                                    unitsBuilt.add(newU6);
                                    units.insertAfter(newU6, u);
                                }
                                mulPerformed++;
                                printOutput(unitsBuilt);
                            }
                        }
                    }
                } else if (v instanceof DivExpr) {
                    total++;
                    DivExpr de = (DivExpr) v;
                    Value op22 = de.getOp2();
                    if (op22 instanceof NumericConstant) {
                        NumericConstant nc3 = (NumericConstant) op22;
                        Type opType2 = de.getOp1().getType();
                        int max2 = opType2 instanceof IntType ? 32 : opType2 instanceof LongType ? 64 : 0;
                        if (max2 != 0) {
                            Object[] shft_rem2 = checkNumericValue(nc3);
                            if (shft_rem2[0] != null && (shft_rem2[1] == null || ((Double) shft_rem2[1]).doubleValue() == Const.default_value_double)) {
                                if (((Integer) shft_rem2[0]).intValue() < max2 && Rand.getInt(10) <= weight) {
                                    List<Unit> unitsBuilt2 = new ArrayList<>();
                                    int rand2 = Rand.getInt(16);
                                    int shift4 = ((Integer) shft_rem2[0]).intValue();
                                    boolean neg2 = ((Boolean) shft_rem2[2]).booleanValue();
                                    if (Rand.getInt() % 2 == 0) {
                                        shift2 = shift4 + (rand2 * max2);
                                    } else {
                                        shift2 = shift4 - (rand2 * max2);
                                    }
                                    Expr e3 = Jimple.v().newShrExpr(de.getOp1(), IntConstant.v(shift2));
                                    Class<?> cls2 = e3.getType().getClass();
                                    Expr e4 = e3;
                                    if (cls2 != as.getLeftOp().getType().getClass()) {
                                        int i4 = localCount;
                                        localCount++;
                                        Local tmp4 = Jimple.v().newLocal("__tmp_shft_lcl" + i4, e3.getType());
                                        locals.add(tmp4);
                                        Unit newU7 = Jimple.v().newAssignStmt(tmp4, e3);
                                        unitsBuilt2.add(newU7);
                                        units.insertAfter(newU7, u);
                                        e4 = Jimple.v().newCastExpr(tmp4, as.getLeftOp().getType());
                                    }
                                    as.setRightOp(e4);
                                    unitsBuilt2.add(as);
                                    if (neg2) {
                                        Unit newU8 = Jimple.v().newAssignStmt(as.getLeftOp(), Jimple.v().newNegExpr(as.getLeftOp()));
                                        unitsBuilt2.add(newU8);
                                        units.insertAfter(newU8, u);
                                    }
                                    divPerformed++;
                                    printOutput(unitsBuilt2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void printOutput(List<Unit> unitsBuilt) {
        if (!output) {
            return;
        }
        out.println(" after as: ");
        for (Unit uu : unitsBuilt) {
            out.println("\t" + uu + "\ttype : " + (uu instanceof AssignStmt ? ((AssignStmt) uu).getLeftOp().getType().toString() : ""));
        }
    }

    @Override // soot.jbco.IJbcoTransform
    public void outputSummary() {
        if (!output) {
            return;
        }
        out.println("Replaced mul/div expressions: " + (divPerformed + mulPerformed));
        out.println("Total mul/div expressions: " + total);
    }

    private Object[] checkNumericValue(NumericConstant nc) {
        Double dnc = null;
        if (nc instanceof IntConstant) {
            dnc = Double.valueOf(((IntConstant) nc).value);
        } else if (nc instanceof DoubleConstant) {
            dnc = Double.valueOf(((DoubleConstant) nc).value);
        } else if (nc instanceof FloatConstant) {
            dnc = Double.valueOf(((FloatConstant) nc).value);
        } else if (nc instanceof LongConstant) {
            dnc = Double.valueOf(((LongConstant) nc).value);
        }
        Object[] shift = new Object[3];
        if (dnc != null) {
            shift[2] = Boolean.valueOf(dnc.doubleValue() < Const.default_value_double);
            double[] tmp = checkShiftValue(dnc.doubleValue());
            if (tmp[0] != Const.default_value_double) {
                shift[0] = Integer.valueOf((int) tmp[0]);
                if (tmp[1] != Const.default_value_double) {
                    shift[1] = Double.valueOf(tmp[1]);
                } else {
                    shift[1] = null;
                }
            } else {
                dnc = null;
            }
        }
        if (dnc == null) {
            shift[0] = null;
            shift[1] = null;
        }
        return shift;
    }

    private double[] checkShiftValue(double val) {
        double[] shift = new double[2];
        if (val == Const.default_value_double || val == 1.0d || val == -1.0d) {
            shift[0] = 0.0d;
            shift[1] = 0.0d;
        } else {
            double shift_dbl = Math.log(val) / Math.log(2.0d);
            double shift_int = Math.rint(shift_dbl);
            if (shift_dbl == shift_int) {
                shift[1] = 0.0d;
            } else {
                if (Math.pow(2.0d, shift_int) > val) {
                    shift_int -= 1.0d;
                }
                shift[1] = val - Math.pow(2.0d, shift_int);
            }
            shift[0] = shift_int;
        }
        return shift;
    }
}
